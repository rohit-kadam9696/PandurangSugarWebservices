package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.LoginResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;

public interface LoginServiceInterface {

	LoginResponse appLogin(JSONObject reqObj,String imei, String accessType, LoginResponse loginRes);

	MainResponse verifyOTP(MainResponse otpRes,JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType);

	MainResponse resendOTP(MainResponse resendOTPRes, JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType);

	MainResponse checkAppUpdate(String versionId, String accessType, MainResponse farmerresponse);

	MainResponse verifyUser(MainResponse actionResponse, String chit_boy_id, String ramdomstring, String imei,String accessType);
}
