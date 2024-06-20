package com.twd.pandurangsugar.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;
import com.twd.pandurangsugar.web.bean.UserBean;

public class WebLogin {

	public UserBean validateWebLogin(UserBean res, String username, String password, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.nuser_name,t.vuser_name,t.vfull_name,t.vactive,"
				+ "t.nuser_role_id,t.npassword from APP_M_USER_MASTER t where t.nuser_name=?")){
			pst.setString(1, username);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					res.setSuccess(true);
					res.setNuserName(rs.getInt("nuser_name"));
					res.setVuserName(rs.getString("vuser_name"));
					res.setVfullName(rs.getString("vfull_name"));
					res.setVactive(rs.getString("vactive"));
					res.setNuserRoleId(rs.getString("nuser_role_id"));
					res.setNpassword(rs.getString("npassword"));
				}else
				{
					res.setSuccess(false);
					res.setMsg("Invalid username...!");
				}
			}
		} catch (SQLException e) {
			res.setSuccess(false);
			res.setError(ConstantVeriables.ERROR_006);
			res.setMsg("login "+e.getMessage());
			//e.printStackTrace();
		}
		return res;
	}

	public JSONObject getWebPermission(JSONObject menu, int nuserRoleId, Connection conn) {
			try (PreparedStatement pst = conn.prepareStatement("select t1.nid,TO_NCHAR(t1.vname)as vname,t1.vurl,t1.nparentid,t1.ninner from APP_M_SUBMENU_ROLE t,app_m_submenu t1 where t.nid=t1.nid and t.nuser_role_id=? and t1.vurl is not null and (t1.vurl LIKE 'menu%' OR t1.vurl='#') ");) {
				int i=1;
				pst.setInt(i++, nuserRoleId);
				try (ResultSet rs = pst.executeQuery();) {
					boolean ismenuexit=false;
					while (rs.next()) {
						if(rs.getString("nparentid")!=null){
							JSONObject jjob = new JSONObject();
							jjob.put("permission_name", DemoConvert2.ism_to_uni(rs.getString("vname")));
							jjob.put("permission_url", rs.getString("vurl"));
							JSONObject job = menu.getJSONObject(rs.getString("nparentid"));
							job.put("child", job.has("child")?job.getJSONObject("child").put(rs.getString("nid"), jjob):new JSONObject().put(rs.getString("nparentid"), jjob));
							menu.put(rs.getString("nparentid"), job);
						}else{
							JSONObject jjob = new JSONObject();
							jjob.put("permission_name", DemoConvert2.ism_to_uni(rs.getString("vname")));
							jjob.put("permission_url", rs.getString("vurl"));
							menu.put(rs.getString("nid"), jjob);
						}
						ismenuexit=true;
					}
					if(ismenuexit)
					{
						menu.put("success",true);
					}else
					{
						menu.put("success",false);
						menu.put("msg","Permission not given to access web panel");
					}
				} 
			} catch (Exception e) {
				try {
					menu.put("success",false);
					menu.put("msg",e.getLocalizedMessage());
					e.printStackTrace();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
			} 
			System.out.println("menu :"+menu);
		return menu;
	}

}
