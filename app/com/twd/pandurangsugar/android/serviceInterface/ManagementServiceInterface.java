package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.MainResponse;

public interface ManagementServiceInterface {

	MainResponse crushingReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse crushingReport);

	MainResponse plantHarvVillReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse crushingReport);

	MainResponse agriReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse agriReport);

	

}
