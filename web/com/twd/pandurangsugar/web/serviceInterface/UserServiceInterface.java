package com.twd.pandurangsugar.web.serviceInterface;

import java.util.ArrayList;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.web.bean.Department;
import com.twd.pandurangsugar.web.bean.Permission;
import com.twd.pandurangsugar.web.bean.UserBean;

public interface UserServiceInterface {

	ArrayList<Department> getDepartmentList();

	ArrayList<Permission> getRoleList();

	JSONObject getSugarTypeAndLocationList(JSONObject result, String nuserRoleId);

	JSONObject checkMobileNoExitOrNot(JSONObject result, String mobileNo,String nuserName);

	JSONObject checkUserCodeExitOrNot(JSONObject result, String userCode,String nuserName);

	JSONObject checkUserNameExitOrNot(JSONObject result, String userName,String nuserName);

	JSONObject saveSystemUser(JSONObject result, UserBean ubean);

	JSONObject getInfoById(JSONObject result, String nuserName);

	JSONObject updateSystemUser(JSONObject result, UserBean ubean);

	ArrayList<UserBean> viewUserList(ArrayList<UserBean> viewUserList);

	JSONObject activeDeactiveById(JSONObject result, String uid, String status);

	
}
