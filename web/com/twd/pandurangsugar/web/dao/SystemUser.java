package com.twd.pandurangsugar.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.web.bean.Department;
import com.twd.pandurangsugar.web.bean.Permission;
import com.twd.pandurangsugar.web.bean.UserBean;

public class SystemUser {

	public ArrayList<Department> getDepartmentList(ArrayList<Department> deptList, Connection conn) throws SQLException {
		try(PreparedStatement pst=conn.prepareStatement("select t.nsubdept_id as id,TO_NCHAR(t.vsubdept_name_local)as name from GM_M_SUBDEPT_MASTER t order by t.nsubdept_id asc"))
		{
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					Department dept=new Department();
					dept.setNsubdeptId(rs.getInt("id"));
					dept.setVsubdeptNameLocal(DemoConvert2.ism_to_uni(rs.getString("name")));
					deptList.add(dept);
				}
			}
		}
		return deptList;
	}

	public ArrayList<Permission> getRoleList(ArrayList<Permission> roleList, Connection conn) throws SQLException {
		try(PreparedStatement pst=conn.prepareStatement("select t.nuser_role_id,t.vuser_role from APP_M_USER_ROLE_MASTER t order by t.nuser_role_id asc"))
		{
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					Permission role=new Permission();
					role.setId(rs.getInt("nuser_role_id"));
					role.setName(rs.getString("vuser_role"));
					roleList.add(role);
				}
			}
		}
		return roleList;
	}

	public JSONObject getSugarTypeAndLocationList(JSONObject result, String nuserRoleId, Connection conn) throws SQLException, JSONException {
		JSONArray typeList=new JSONArray();
		JSONArray locationList=new JSONArray();
		try(PreparedStatement pst=conn.prepareStatement("select t.nsug_type_id as id,TO_NCHAR(t.vsug_type_name_local)as name from MS_M_SUGAR_TYPE t order by t.nsug_type_id asc"))
		{
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					JSONObject data=new JSONObject();
					data.put("id",rs.getInt("id"));
					data.put("name",DemoConvert2.ism_to_uni(rs.getString("name")));
					typeList.put(data);
				}
			}
		}
		if(nuserRoleId.equals("108") || nuserRoleId.equals("110"))
		{
			String sql="select t.nlocation_id as id,TO_NCHAR(t.vlocation)as name from MS_M_SUG_CARD_LOCATION t order by t.nlocation_id asc";
			if(nuserRoleId.equals("108"))
				sql="select t.nsection_id as id,TO_NCHAR(t.vsection_name_local)as name from GM_M_SECTION_MASTER_FERTILIZER t order by t.nsection_id asc";
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						JSONObject data=new JSONObject();
						data.put("id",rs.getInt("id"));
						data.put("name",DemoConvert2.ism_to_uni(rs.getString("name")));
						locationList.put(data);
					}
				}
			}
		}
		result.put("typeList", typeList);
		result.put("locationList", locationList);
		return result;
	}

	public JSONObject checkFieldNoExitOrNot(JSONObject result, String mobileNo,String fieldName, String nuserName, Connection conn) throws SQLException, JSONException {
		try(PreparedStatement pst=conn.prepareStatement("select t.nuser_name from APP_M_USER_MASTER t where t."+fieldName+"=? AND t.nuser_name<>?"))
		{
			pst.setString(1, mobileNo);
			pst.setString(2, nuserName==null?"0":nuserName);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
					result.put("success", true);
				else
					result.put("success", false);
			}
		}
		return result;
	}

	public JSONObject saveSystemUser(JSONObject result, UserBean ubean, Connection conn) throws SQLException, JSONException {
		try(PreparedStatement pst=conn.prepareStatement("INSERT INTO APP_M_USER_MASTER(nuser_name,vuser_name,"
				+ "vfull_name,npassword,vactive,ndept_id,vfull_name_local,nuser_role_id,nlocation_id,nmobile_no,"
				+ "nsug_type_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)"))
		{
			int i=1;
			pst.setInt(i++, ubean.getNuserName());
			pst.setString(i++, ubean.getVuserName());
			pst.setString(i++, ubean.getVfullName());
			pst.setString(i++, ubean.getNpassword());
			pst.setString(i++, ubean.getVactive());
			pst.setString(i++, ubean.getNdeptId());
			pst.setString(i++, ubean.getVfullNameLocal());
			pst.setString(i++, ubean.getNuserRoleId());
			pst.setString(i++, ubean.getNlocationId());
			pst.setString(i++, ubean.getNmobileNo());
			pst.setString(i++, ubean.getNsugTypeId());
			int s=pst.executeUpdate();
			if(s>0)
				result.put("success", true);
			else
				result.put("success", false);
		}
		return result;
	}

	public JSONObject getInfoById(JSONObject result, String nuserName, Connection conn) throws SQLException {
		try(PreparedStatement pst=conn.prepareStatement("select t.nuser_name,t.vuser_name,t.vfull_name,t.npassword,"
				+ "t.vactive,t.ndept_id,t.vfull_name_local,t.nuser_role_id,t.nlocation_id,t.nmobile_no,t.nsug_type_id from APP_M_USER_MASTER t WHERE t.nuser_name=?"))
		{
			pst.setString(1, nuserName);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					UserBean ubean=new UserBean();
					ubean.setNuserName(rs.getInt("nuser_name"));
					ubean.setVuserName(rs.getString("vuser_name"));
					ubean.setVfullName(rs.getString("vfull_name"));
					ubean.setNdeptId(rs.getString("ndept_id"));
					ubean.setVfullNameLocal(rs.getString("vfull_name_local"));
					ubean.setNuserRoleId(rs.getString("nuser_role_id"));
					ubean.setNlocationId(rs.getString("nlocation_id"));
					ubean.setNmobileNo(rs.getString("nmobile_no"));
					ubean.setNsugTypeId(rs.getString("nsug_type_id"));
					ubean.setSuccess(true);
					result=new JSONObject(ubean);
				}
			}
		}
		return result;
	}

	public JSONObject updateSystemUser(JSONObject result, UserBean ubean, Connection conn) throws JSONException, SQLException {

		try(PreparedStatement pst=conn.prepareStatement("UPDATE APP_M_USER_MASTER SET vuser_name=?,vfull_name=?,ndept_id=?,vfull_name_local=?,nuser_role_id=?,nlocation_id=?,nmobile_no=?,nsug_type_id=? where nuser_name=?"))
		{
			int i=1;
			pst.setString(i++, ubean.getVuserName());
			pst.setString(i++, ubean.getVfullName());
			pst.setString(i++, ubean.getNdeptId());
			pst.setString(i++, ubean.getVfullNameLocal());
			pst.setString(i++, ubean.getNuserRoleId());
			pst.setString(i++, ubean.getNlocationId());
			pst.setString(i++, ubean.getNmobileNo());
			pst.setString(i++, ubean.getNsugTypeId());
			pst.setInt(i++, ubean.getNuserName());
			int s=pst.executeUpdate();
			if(s>0)
				result.put("success", true);
			else
				result.put("success", false);
		}
		return result;
	
	}

	public ArrayList<UserBean> viewUserList(ArrayList<UserBean> viewUserList, Connection conn) throws SQLException {

		try(PreparedStatement pst=conn.prepareStatement("select t.nuser_name,t.vuser_name,t.vfull_name,t.npassword,"
				+ "t.vactive,t.ndept_id,t.vfull_name_local,t.nuser_role_id,t.nlocation_id,t.nmobile_no,t.nsug_type_id,TO_NCHAR(t1.vsubdept_name_local)as deptName from APP_M_USER_MASTER t,GM_M_SUBDEPT_MASTER t1 where  t.ndept_id=t1.nsubdept_id"))
		{
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					UserBean ubean=new UserBean();
					ubean.setNuserName(rs.getInt("nuser_name"));
					ubean.setVuserName(rs.getString("vuser_name"));
					ubean.setVfullName(rs.getString("vfull_name"));
					ubean.setDeptName(DemoConvert2.ism_to_uni(rs.getString("deptName")));
					ubean.setVfullNameLocal(rs.getString("vfull_name_local"));
					ubean.setNuserRoleId(rs.getString("nuser_role_id"));
					ubean.setNlocationId(rs.getString("nlocation_id"));
					ubean.setNmobileNo(rs.getString("nmobile_no"));
					ubean.setNsugTypeId(rs.getString("nsug_type_id"));
					ubean.setVactive(rs.getString("vactive"));
					viewUserList.add(ubean);
				}
			}
		}
		return viewUserList;
	
	}

	public JSONObject activeDeactiveById(JSONObject result, String uid, String status, Connection conn) throws JSONException, SQLException {
		try(PreparedStatement pst=conn.prepareStatement("UPDATE APP_M_USER_MASTER SET vactive=? where nuser_name=?"))
		{
			int i=1;
			pst.setString(i++, status);
			pst.setString(i++, uid);
			int s=pst.executeUpdate();
			if(s>0)
				result.put("success", true);
			else
				result.put("success", false);
		}
		return result;
	}

	

}
