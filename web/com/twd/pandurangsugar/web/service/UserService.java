package com.twd.pandurangsugar.web.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.web.bean.Department;
import com.twd.pandurangsugar.web.bean.Permission;
import com.twd.pandurangsugar.web.bean.UserBean;
import com.twd.pandurangsugar.web.dao.SystemUser;
import com.twd.pandurangsugar.web.serviceInterface.UserServiceInterface;

public class UserService implements UserServiceInterface{

	SystemUser userdao=new SystemUser();
	@Override
	public ArrayList<Department> getDepartmentList() {
		ArrayList<Department> deptList=new ArrayList<>();
		try(Connection conn=DBConnection.getConnection())
		{
			deptList=userdao.getDepartmentList(deptList,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deptList;
	}
	@Override
	public ArrayList<Permission> getRoleList() {
		ArrayList<Permission> releList=new ArrayList<>();
		try(Connection conn=DBConnection.getConnection())
		{
			releList=userdao.getRoleList(releList,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return releList;
	
	}
	@Override
	public JSONObject getSugarTypeAndLocationList(JSONObject result,String nuserRoleId) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.getSugarTypeAndLocationList(result,nuserRoleId,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	
	
	}
	@Override
	public JSONObject checkMobileNoExitOrNot(JSONObject result, String mobileNo,String nuserName) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.checkFieldNoExitOrNot(result,mobileNo,"nmobile_no",nuserName,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public JSONObject checkUserCodeExitOrNot(JSONObject result, String userCode,String nuserName) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.checkFieldNoExitOrNot(result,userCode,"nuser_name",nuserName,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public JSONObject checkUserNameExitOrNot(JSONObject result, String userName,String nuserName) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.checkFieldNoExitOrNot(result,userName,"vuser_name",nuserName,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public JSONObject saveSystemUser(JSONObject result, UserBean ubean) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.saveSystemUser(result,ubean,conn);
		} catch (SQLException e) {
			try {
				result.put("success", false);
				result.put("msg", "Record Not Saved"+e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}e.printStackTrace();
		} catch (JSONException e) {
			try {
				result.put("success", false);
				result.put("msg", "Record Not Saved"+e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public JSONObject getInfoById(JSONObject result, String nuserName) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.getInfoById(result,nuserName,conn);
		} catch (SQLException e) {
			try {
				result.put("success", false);
				result.put("msg", "Record Not Saved"+e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}e.printStackTrace();
		}
		return result;
	}
	@Override
	public JSONObject updateSystemUser(JSONObject result, UserBean ubean) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.updateSystemUser(result,ubean,conn);
		} catch (SQLException e) {
			try {
				result.put("success", false);
				result.put("msg", "Record Not Updated"+e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}e.printStackTrace();
		} catch (JSONException e) {
			try {
				result.put("success", false);
				result.put("msg", "Record Not Updated"+e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}
	@Override
	public ArrayList<UserBean> viewUserList(ArrayList<UserBean> viewUserList) {

		try(Connection conn=DBConnection.getConnection())
		{
			viewUserList=userdao.viewUserList(viewUserList,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return viewUserList;
	
	}
	@Override
	public JSONObject activeDeactiveById(JSONObject result, String uid, String status) {
		try(Connection conn=DBConnection.getConnection())
		{
			result=userdao.activeDeactiveById(result,uid,status,conn);
		} catch (SQLException e) {
			try {
				result.put("success", false);
				result.put("msg", "Record Not Updated"+e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}e.printStackTrace();
		} catch (JSONException e) {
			try {
				result.put("success", false);
				result.put("msg", "Record Not Updated"+e.getLocalizedMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}
	

}
