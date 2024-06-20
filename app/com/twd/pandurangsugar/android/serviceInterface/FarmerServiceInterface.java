package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.FarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;

public interface FarmerServiceInterface {

	FarmerResponse farmerDetailsByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerresponse);

	ActionResponse updateBirthDate(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerresponse);

	ActionResponse updateMobileNoByfarmerCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerresponse);

	ActionResponse updateAadharByFarmerCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse mainresponse);

	ActionResponse updateBankByfarmerCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerresponse);

}
