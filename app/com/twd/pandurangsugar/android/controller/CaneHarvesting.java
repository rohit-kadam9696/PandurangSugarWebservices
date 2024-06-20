package com.twd.pandurangsugar.android.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.BulluckCartResponse;
import com.twd.pandurangsugar.android.bean.CaneDailyInwardReportResponse;
import com.twd.pandurangsugar.android.bean.CaneTransitResponse;
import com.twd.pandurangsugar.android.bean.CloseTransferResponse;
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
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.bean.TransporterResponse;
import com.twd.pandurangsugar.android.bean.VillageResonse;
import com.twd.pandurangsugar.android.bean.WeightSlipResponse;
import com.twd.pandurangsugar.android.service.CaneHarvestingService;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.serviceInterface.CaneHarvestingServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class RegistrationApproval
 */
@WebServlet("/harvdata")
public class CaneHarvesting extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CaneHarvesting() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String json = request.getParameter("json");
		String imei = request.getParameter("imei");
		String chit_boy_id = request.getParameter("chit_boy_id");
		String ramdomstring = request.getParameter("randomstring");
		String versionId = request.getParameter("versionId");
		String accessType = request.getParameter("accessType");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (accessType == null || accessType.trim().isEmpty())
			accessType = "1";
		try (PrintWriter out = response.getWriter()) {
			JSONObject result = null;
			MainResponse actionResponse = new ActionResponse();
			LoginServiceInterface login = new LoginService();
			try {
				actionResponse = login.checkAppUpdate(versionId, accessType, actionResponse);
				if (actionResponse.getUpdateResponse() != null && actionResponse.getUpdateResponse().isForceUpdate()) {
					result = new JSONObject(actionResponse);
					out.print(result);
					return;
				}
				CaneHarvestingServiceInterface caneHarController = new CaneHarvestingService();
				JSONObject reqObj = new JSONObject(json);
				switch (action) {
				case "slipdataorextrarequest":
					MainResponse caneharvRes = new HarvPlotDetailsResponse();
					HarvPlotDetailsResponse harvRes = caneHarController.caneInfoByPlotAndYearCode(reqObj, imei,
							ramdomstring, chit_boy_id, accessType, caneharvRes);
					result = new JSONObject(harvRes);
					break;
				case "transdetails":
					MainResponse caneTransRes = new TransporterResponse();
					TransporterResponse transRes = caneHarController.findTranspoterByCode(reqObj, imei, ramdomstring,
							chit_boy_id, accessType, caneTransRes);
					result = new JSONObject(transRes);
					break;
				case "harvdetails":
					MainResponse caneHarvRes = new HarvestorResponse();
					HarvestorResponse harvResponse = caneHarController.findHarvestorByCode(reqObj, imei, ramdomstring,
							chit_boy_id, accessType, caneHarvRes);
					result = new JSONObject(harvResponse);
					break;
				case "mukadamdetails":
					MainResponse caneMukhRes = new BulluckCartResponse();
					BulluckCartResponse mukhResponse = caneHarController.findMukadamByCode(reqObj, imei, ramdomstring,
							chit_boy_id, accessType, caneMukhRes);
					result = new JSONObject(mukhResponse);

					break;
				case "savews":
					MainResponse savews = new WeightSlipResponse();
					savews = caneHarController.savews(reqObj, imei, ramdomstring, chit_boy_id, accessType, savews);
					result = new JSONObject(savews);
					break;
				case "remainingsliplist":
					MainResponse sliplist = new RemainingSlipResponse();
					sliplist = caneHarController.remainingSlipList(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							sliplist);
					result = new JSONObject(sliplist);
					break;
				case "deactivateslip":
					MainResponse detivateSlip = new ActionResponse();
					detivateSlip = caneHarController.detivateSlip(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							detivateSlip);
					result = new JSONObject(detivateSlip);
					break;
				case "slipedit":
					MainResponse slipedit = new HarvSlipDetailsResponse();
					slipedit = caneHarController.slipeditInfo(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							slipedit);
					result = new JSONObject(slipedit);
					break;
				case "saveextraplotrequest":
					MainResponse saveextraplot = new ActionResponse();
					saveextraplot = caneHarController.saveExtraPlotRequest(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, saveextraplot);
					result = new JSONObject(saveextraplot);
					break;
				case "extraplotreqlist":
					MainResponse extraPlotreqList = new ExcessTonPlotReqResponse();
					extraPlotreqList = caneHarController.extraPlotReqList(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, extraPlotreqList);
					result = new JSONObject(extraPlotreqList);
					break;
				case "excessplotdetails":
					MainResponse excessPlotDetails = new ExcessTonPlotReqDataResponse();
					excessPlotDetails = caneHarController.excessPlotDetails(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, excessPlotDetails);
					result = new JSONObject(excessPlotDetails);
					break;
				case "acceptorrejectextraplot":
					MainResponse acceptPlotReq = new ActionResponse();
					acceptPlotReq = caneHarController.acceptOrRejectExtraPlot(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, acceptPlotReq);
					result = new JSONObject(acceptPlotReq);
					break;
				case "otherutilization":
					MainResponse otherUtilization = new OtherUtilizationResponse();
					otherUtilization = caneHarController.otherUtilizationDetails(reqObj, imei, ramdomstring,
							chit_boy_id, accessType, otherUtilization);
					result = new JSONObject(otherUtilization);
					break;
				case "saveotheruti":
					MainResponse saveOtherUti = new ActionResponse();
					saveOtherUti = caneHarController.saveOtherUtilization(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, saveOtherUti);
					result = new JSONObject(saveOtherUti);
					break;
				case "dailyinwardreport":
					MainResponse dailyCaneReport = new CaneDailyInwardReportResponse();
					dailyCaneReport = caneHarController.dailyInwardReport(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, dailyCaneReport);
					result = new JSONObject(dailyCaneReport);
					break;
				case "verifyslip":
					MainResponse verifyslip = new ActionResponse();
					verifyslip = caneHarController.verifySlip(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							verifyslip);
					result = new JSONObject(verifyslip);
					break;
				case "plotbyfarmer":
					MainResponse plotdetails = new CompletePlotResponse();
					plotdetails = caneHarController.plotByFarmer(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							plotdetails);
					result = new JSONObject(plotdetails);
					break;
				case "closeandtransfer":
					MainResponse closetranres = new CloseTransferResponse();
					closetranres = caneHarController.closeTransResponse(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, closetranres);
					result = new JSONObject(closetranres);
					break;
				case "vehicletransitinfo":
					MainResponse vehicleTransitInfo = new CaneTransitResponse();
					vehicleTransitInfo = caneHarController.vehicleTransitInfo(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, vehicleTransitInfo);
					result = new JSONObject(vehicleTransitInfo);
					break;
				case "saveorupdatecanetransit":
					MainResponse saveTransit = new ActionResponse();
					saveTransit = caneHarController.saveTransit(json, imei, ramdomstring, chit_boy_id, accessType,
							saveTransit);
					result = new JSONObject(saveTransit);
					break;
				case "rawanasectreport":
					MainResponse sectionWiseUsRawanaReport = new TableResponse();
					sectionWiseUsRawanaReport = caneHarController.sectionWiseUsRawanaReport(reqObj, imei, ramdomstring,
							chit_boy_id, accessType, sectionWiseUsRawanaReport);
					result = new JSONObject(sectionWiseUsRawanaReport);
					break;
				case "rawanavillreport":
					MainResponse villeageWiseUsRawanaReport = new TableResponse();
					villeageWiseUsRawanaReport = caneHarController.villeageWiseUsRawanaReport(reqObj, imei,
							ramdomstring, chit_boy_id, accessType, villeageWiseUsRawanaReport);
					result = new JSONObject(villeageWiseUsRawanaReport);
					break;
				case "sectionByVillageList":
					MainResponse villageResonse = new VillageResonse();
					villageResonse = caneHarController.sectionByVillageList(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, villageResonse);
					result = new JSONObject(villageResonse);
					break;
				case "villbysection":
					MainResponse villResonse = new VillageResonse();
					villResonse = caneHarController.villBySection(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							villResonse);
					result = new JSONObject(villResonse);
					break;
				case "harvreport":
					MainResponse harvResonse = new HarvReportReponse();
					harvResonse = caneHarController.harvReport(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							harvResonse);
					result = new JSONObject(harvResonse);
					break;
				case "farmertonnage":
					MainResponse tonnageResponse = new FarmerTonnageResponse();
					tonnageResponse = caneHarController.farmerTonnageReport(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, tonnageResponse);
					result = new JSONObject(tonnageResponse);
					break;
				case "farmertonnagedetails":
					MainResponse tableResponse = new TableResponse();
					tableResponse = caneHarController.farmerTonnageDetailsReport(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, tableResponse);
					result = new JSONObject(tableResponse);
					break;
				case "farmerlistbyname":
					MainResponse nameListResponse = new NameListResponse();
					nameListResponse = caneHarController.farmerListByName(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, nameListResponse);
					result = new JSONObject(nameListResponse);
					break;
				case "regenrateslip":
					MainResponse regenrateSlip = new RemainingSlipResponse();
					regenrateSlip = caneHarController.updateAndPrint(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							regenrateSlip);
					result = new JSONObject(regenrateSlip);
					break;
				default:
					actionResponse = login.verifyUser(actionResponse, chit_boy_id, ramdomstring, imei, accessType);
					actionResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Requested Method Not Found Contact to sugar Factory");
					actionResponse.setSe(error);
					result = new JSONObject(actionResponse);
					break;
				}
			} catch (Exception e) {
				actionResponse = login.verifyUser(actionResponse, chit_boy_id, ramdomstring, imei, accessType);
				actionResponse.setSuccess(true);
				ServerError error = new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("controller Issue " + e.getMessage());
				actionResponse.setSe(error);
				e.printStackTrace();
			}
			out.print(result);
			out.flush();
		}

	}

}
