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
import com.twd.pandurangsugar.android.bean.CaneYardBalanceResponse;
import com.twd.pandurangsugar.android.bean.DataListResonse;
import com.twd.pandurangsugar.android.bean.DataTwoListResonse;
import com.twd.pandurangsugar.android.bean.LotGenResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NumIndResponse;
import com.twd.pandurangsugar.android.bean.NumSlipListResponse;
import com.twd.pandurangsugar.android.bean.SavePrintResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SingleNumDataResponse;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.bean.UserRoleResponse;
import com.twd.pandurangsugar.android.bean.UserYardResponse;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.service.NumberSystemService;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.NumberSystemInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class NumberSystem
 */
@WebServlet("/numbersys")
public class NumberSystem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NumberSystem() {
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
		// TODO Auto-generated method stub
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
				NumberSystemInterface numbersys = new NumberSystemService();
				JSONObject reqObj = new JSONObject(json);
				switch (action) {
				case "yardempinfo":
					MainResponse userYardResp = new UserYardResponse();
					userYardResp = numbersys.userYardInfo(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							userYardResp);
					result = new JSONObject(userYardResp);
					break;
				case "updateyardemp":
					MainResponse updateYardRes = new ActionResponse();
					updateYardRes = numbersys.updateYardEmp(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							updateYardRes);
					result = new JSONObject(updateYardRes);
					break;
				case "removeryardemp":
					MainResponse removeYardRes = new ActionResponse();
					removeYardRes = numbersys.removeYardEmp(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							removeYardRes);
					result = new JSONObject(removeYardRes);
					break;
				case "slipdata":
					MainResponse slipdataRes = new NumSlipListResponse();
					slipdataRes = numbersys.slipDataList(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							slipdataRes);
					result = new JSONObject(slipdataRes);
					break;
				case "generatelotlist":
					MainResponse generatelotlistRes = new TableResponse();
					generatelotlistRes = numbersys.generateLotList(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							generatelotlistRes);
					result = new JSONObject(generatelotlistRes);
					break;
				case "generatelot":
					MainResponse generatelotRes = new LotGenResponse();
					generatelotRes = numbersys.generateLot(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							generatelotRes);
					result = new JSONObject(generatelotRes);
					break;
				case "singlenumdata":
					MainResponse singlenumdataRes = new SingleNumDataResponse();
					singlenumdataRes = numbersys.singleNumData(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							singlenumdataRes);
					result = new JSONObject(singlenumdataRes);
					break;
				case "numindexclude":
					MainResponse numindexcludeRes = new SavePrintResponse();
					numindexcludeRes = numbersys.numIndExclude(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							numindexcludeRes);
					result = new JSONObject(numindexcludeRes);
					break;
				case "singlenumdatablock":
					MainResponse singleNumDataBlockRes = new SingleNumDataResponse();
					singleNumDataBlockRes = numbersys.singleNumDataBlock(reqObj, imei, ramdomstring, chit_boy_id,
							accessType, singleNumDataBlockRes);
					result = new JSONObject(singleNumDataBlockRes);
					break;
				case "stopnumber":
					MainResponse stopNumberRes = new ActionResponse();
					stopNumberRes = numbersys.stopNumber(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							stopNumberRes);
					result = new JSONObject(stopNumberRes);
					break;
				case "loadlot":
					MainResponse loadlotRes = new DataListResonse();
					loadlotRes = numbersys.loadLot(reqObj, imei, ramdomstring, chit_boy_id, accessType, loadlotRes);
					result = new JSONObject(loadlotRes);
					break;
				case "printlot":
					MainResponse printlotRes = new SavePrintResponse();
					printlotRes = numbersys.printLot(reqObj, imei, ramdomstring, chit_boy_id, accessType, printlotRes);
					result = new JSONObject(printlotRes);
					break;
				case "vehicleregister":
					MainResponse vehicleregisterRes = new TableResponse();
					vehicleregisterRes = numbersys.vehicleRegister(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							vehicleregisterRes);
					result = new JSONObject(vehicleregisterRes);
					break;
				case "numwaiting":
					MainResponse numwaitingRes = new NumIndResponse();
					numwaitingRes = numbersys.numWaiting(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							numwaitingRes);
					result = new JSONObject(numwaitingRes);
					break;
				case "printtokenpass":
					MainResponse printtokenpassRes = new SavePrintResponse();
					printtokenpassRes = numbersys.printTokenPass(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							printtokenpassRes);
					result = new JSONObject(printtokenpassRes);
					break;
				case "vehiclestatus":
					MainResponse vehicleStatusRes = new DataListResonse();
					vehicleStatusRes = numbersys.vehicleStatus(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							vehicleStatusRes);
					result = new JSONObject(vehicleStatusRes);
					break;
				case "roleempinfo":
					MainResponse userRoleRes = new UserRoleResponse();
					userRoleRes = numbersys.userRoleInfo(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							userRoleRes);
					result = new JSONObject(userRoleRes);
					break;
				case "updateemprole":
					MainResponse updateRoleRes = new ActionResponse();
					updateRoleRes = numbersys.updateRoleEmp(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							updateRoleRes);
					result = new JSONObject(updateRoleRes);
					break;
				case "cancelnumber":
					MainResponse cancelNumberRes = new ActionResponse();
					updateRoleRes = numbersys.cancelNumber(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							cancelNumberRes);
					result = new JSONObject(updateRoleRes);
					break;
				case "roleuser":
					MainResponse roleUserRes = new DataTwoListResonse();
					roleUserRes = numbersys.roleUser(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							roleUserRes);
					result = new JSONObject(roleUserRes);
					break;
				case "summaryreport":
					MainResponse summaryreportRes = new CaneYardBalanceResponse();
					summaryreportRes = numbersys.summaryReport(reqObj, imei, ramdomstring, chit_boy_id, accessType,
							summaryreportRes);
					result = new JSONObject(summaryreportRes);
					break;

				case "inwardsummary":
					MainResponse inwardSummaryRes= new TableResponse();
					inwardSummaryRes=numbersys.inwardSummaryReport(reqObj,imei,ramdomstring,chit_boy_id,accessType,inwardSummaryRes);
					result=new JSONObject(inwardSummaryRes);
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
			// System.out.println("result is : "+result);
			out.print(result);
			out.flush();
		}
	}

}
