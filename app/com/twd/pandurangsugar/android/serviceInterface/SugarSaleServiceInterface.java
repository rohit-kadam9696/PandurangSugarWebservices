package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.DataListResonse;
import com.twd.pandurangsugar.android.bean.FarmerSugarResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.SugarInwardResponse;
import com.twd.pandurangsugar.android.bean.SugarSaleSavePrintResponse;
import com.twd.pandurangsugar.android.bean.TableResponse;

public interface SugarSaleServiceInterface {

	FarmerSugarResponse farmerSugarInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarInfo);

	SugarSaleSavePrintResponse saveSugarSale(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse saveSugar);

	MainResponse printSugarSale(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse printSugar);

	SugarInwardResponse findSugarInward(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarInward);

	ActionResponse saveInward(String json, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse saveInward);

	DataListResonse loaddoList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse loaddoList);

	TableResponse sugarSummaryReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarSummaryReport);

	TableResponse sugarOutwardReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarOutwardReport);

}
