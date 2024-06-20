package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.CanePlantationResponse;
import com.twd.pandurangsugar.android.bean.EmpVillResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NondReportHangamReponse;
import com.twd.pandurangsugar.android.bean.NondReportReponse;

public interface CanePlantationServiceInterface {

	CanePlantationResponse savePlantation(JSONArray reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,MainResponse mainresponse);

	CanePlantationResponse updatePlantation(String reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse mainresponse);

	NondReportReponse nondReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse nondReport);

	NondReportHangamReponse nondHangamReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse nondHangam);

	EmpVillResponse empData(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse empVillResponse);

	MainResponse removeEmpVill(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse empVillResponse);

	MainResponse addEmpVill(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse empVillResponse);

}
