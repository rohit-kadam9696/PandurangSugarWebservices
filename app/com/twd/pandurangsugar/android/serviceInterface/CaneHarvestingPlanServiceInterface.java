package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.GutKhadeResponse;
import com.twd.pandurangsugar.android.bean.HarvestingPlanFarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.TableResponse;

public interface CaneHarvestingPlanServiceInterface {

	MainResponse findHarvestingPlanStartByYearCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse planStart);

	MainResponse saveHarvestingPlanStart(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse);

	MainResponse saveHarvestingPlanDate(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse);

	HarvestingPlanFarmerResponse farmerByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse planfarmerResponse);

	MainResponse reasonByGroupCode(int ngroupCode, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerResponse);

	MainResponse saveHarvestingSpecialPlan(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse);

	ActionResponse saveGutKhade(String json, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse actionResponse);

	TableResponse gutkhadelist(JSONObject json, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse gutList);

	GutKhadeResponse editGutKhade(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse editKhade);

	MainResponse deleteGutKhade(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse actionResponse);


}
