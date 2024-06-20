package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.BulluckCartResponse;
import com.twd.pandurangsugar.android.bean.CaneDailyInwardReportResponse;
import com.twd.pandurangsugar.android.bean.CaneTransitResponse;
import com.twd.pandurangsugar.android.bean.CompletePlotResponse;
import com.twd.pandurangsugar.android.bean.ExcessTonPlotReqDataResponse;
import com.twd.pandurangsugar.android.bean.ExcessTonPlotReqResponse;
import com.twd.pandurangsugar.android.bean.FarmerTonnageResponse;
import com.twd.pandurangsugar.android.bean.HarvPlotDetailsResponse;
import com.twd.pandurangsugar.android.bean.HarvReportReponse;
import com.twd.pandurangsugar.android.bean.HarvSlipDetailsResponse;
import com.twd.pandurangsugar.android.bean.HarvestorResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NameListResponse;
import com.twd.pandurangsugar.android.bean.OtherUtilizationResponse;
import com.twd.pandurangsugar.android.bean.RemainingSlipResponse;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.bean.TransporterResponse;
import com.twd.pandurangsugar.android.bean.VillageResonse;

public interface CaneHarvestingServiceInterface {

	HarvPlotDetailsResponse caneInfoByPlotAndYearCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneSample);

	TransporterResponse findTranspoterByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse caneTransRes);

	HarvestorResponse findHarvestorByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse caneHarvRes);

	BulluckCartResponse findMukadamByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse caneMukhRes);

	MainResponse savews(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType, MainResponse savews);

	RemainingSlipResponse remainingSlipList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse sliplist);

	ActionResponse detivateSlip(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse detivateSlip);

	HarvSlipDetailsResponse slipeditInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse slipedit);

	ActionResponse saveExtraPlotRequest(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse saveextraplot);

	ExcessTonPlotReqResponse extraPlotReqList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse extraPlotreqList);

	ExcessTonPlotReqDataResponse excessPlotDetails(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse excessPlotDetails);

	ActionResponse acceptOrRejectExtraPlot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse acceptPlotReq);

	OtherUtilizationResponse otherUtilizationDetails(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse otherUtilization);

	ActionResponse saveOtherUtilization(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse saveOtherUti);

	CaneDailyInwardReportResponse dailyInwardReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse dailyCaneReport);

	ActionResponse verifySlip(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse verifyslip);

	CompletePlotResponse plotByFarmer(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse plotdetails);

	MainResponse closeTransResponse(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse closetranres);

	CaneTransitResponse vehicleTransitInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse vehicleTransitInfo);

	ActionResponse saveTransit(String json, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse saveTransit);

	TableResponse sectionWiseUsRawanaReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sectionWiseUsRawanaReport);

	TableResponse villeageWiseUsRawanaReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse villeageWiseUsRawanaReport);

	VillageResonse sectionByVillageList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse villageResonse);
	
	VillageResonse villBySection(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse villResonse);

	HarvReportReponse harvReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse harvResonse);

	FarmerTonnageResponse farmerTonnageReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse tonnageResponse);

	TableResponse farmerTonnageDetailsReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse tableResponse);

	NameListResponse farmerListByName(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse nameListResponse);

	MainResponse updateAndPrint(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse regenrateSlip);


}
