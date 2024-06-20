package com.twd.pandurangsugar.web.service;

import java.sql.Connection;

import org.json.JSONException;
import org.json.JSONObject;

import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;
import com.twd.pandurangsugar.web.bean.UserBean;
import com.twd.pandurangsugar.web.dao.WebLogin;
import com.twd.pandurangsugar.web.serviceInterface.WebLoginServiceInterface;

public class WebLoginService implements WebLoginServiceInterface{

	@Override
	public UserBean validateWebLogin(String username, String password) {
		UserBean res=new UserBean();
		try(Connection conn=DBConnection.getConnection())
		{
			res=new WebLogin().validateWebLogin(res,username,password,conn);
			String pass=res.getNpassword();
			String vactive=res.getVactive();
			if(res.isSuccess())
			{
				if(pass==null || !pass.equals(password))
				{
					res.setSuccess(false);
					res.setError(ConstantVeriables.ERROR_006);
					res.setMsg("Wrong Password..!");
				}else if(vactive==null || !vactive.equalsIgnoreCase("Y"))
				{
					res.setSuccess(false);
					res.setError(ConstantVeriables.ERROR_006);
					res.setMsg("User Deactivated..!");
				}else if(!res.getNuserRoleId().equals("105") && !res.getNuserRoleId().equals("113"))
				{
					res.setSuccess(false);
					res.setError(ConstantVeriables.ERROR_006);
					res.setMsg("Permission Not for access Web Panel..!");
				}
			}
		}catch (Exception e) {
			res.setSuccess(true);
			res.setError(ConstantVeriables.ERROR_006);
			res.setMsg("Connection Issue "+e.getMessage());
			
			//e.printStackTrace();
		}
		return res;
	}

	@Override
	public JSONObject getWebPermission(String nuserRoleId) {
		JSONObject	permission=new JSONObject();
		int roleId=Integer.parseInt(nuserRoleId);
		if(roleId!=105 && roleId!=113)
		{
			try {
				permission.put("success",false);
				permission.put("msg","You have not allowed to login web panel...!");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else
		{
			try(Connection conn=DBConnection.getConnection())
			{
				permission=new WebLogin().getWebPermission(permission,roleId,conn);
			} catch (Exception e) {
				try {
					permission.put("success",false);
					permission.put("msg","Permission : "+e.getMessage());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		}
		return permission;
	}

}
