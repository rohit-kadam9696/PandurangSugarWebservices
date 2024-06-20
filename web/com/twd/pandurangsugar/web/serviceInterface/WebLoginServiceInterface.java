package com.twd.pandurangsugar.web.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.web.bean.UserBean;

public interface WebLoginServiceInterface {

	UserBean validateWebLogin(String username, String password);

	JSONObject getWebPermission(String nuserRoleId);

}
