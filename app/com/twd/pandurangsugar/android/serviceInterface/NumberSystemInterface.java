package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.UserYardResponse;

public interface NumberSystemInterface {

	UserYardResponse userYardInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse userYardResp);

	MainResponse updateYardEmp(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse updateYardRes);

	MainResponse removeYardEmp(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse removeYardRes);

	MainResponse slipDataList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse slipdataRes);

	MainResponse saveNumber(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse savenumberRes);

	MainResponse generateLotList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse generatelotlistRes);

	MainResponse generateLot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse generatelotRes);

	MainResponse singleNumData(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse singlenumdataRes);

	MainResponse numIndExclude(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse numindexcludeRes);
	
	MainResponse singleNumDataBlock(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse singlenumdataRes);

	MainResponse stopNumber(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse stopNumberRes);

	MainResponse loadLot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse loadlotRes);

	MainResponse printLot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse printlotRes);

	MainResponse vehicleRegister(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse vehicleregisterRes);

	MainResponse numWaiting(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse numwaitingRes);

	MainResponse printTokenPass(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse printtokenpassRes);

	MainResponse vehicleStatus(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse vehicleStatusRes);

	MainResponse userRoleInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse userRoleRes);

	MainResponse updateRoleEmp(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse updateRoleRes);

	MainResponse cancelNumber(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse cancelNumberRes);

	MainResponse roleUser(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse roleUserRes);

	MainResponse summaryReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse summaryreportRes);

	MainResponse inwardSummaryReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse inwardSummaryRes);
}
