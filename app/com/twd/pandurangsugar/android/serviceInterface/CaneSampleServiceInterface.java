package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.CaneSamplePlantationData;
import com.twd.pandurangsugar.android.bean.MainResponse;

public interface CaneSampleServiceInterface {

	CaneSamplePlantationData caneInfoByPlotAndYearCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneSample);

	ActionResponse save(String json, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse);

	CaneSamplePlantationData caneSampleInfoByPlotNoAndYearCode(JSONObject req, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneSample);

}
