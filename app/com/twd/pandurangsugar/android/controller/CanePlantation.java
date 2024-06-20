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
import com.twd.pandurangsugar.android.bean.EmpVillResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NondReportHangamReponse;
import com.twd.pandurangsugar.android.bean.NondReportReponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.service.CanePlantationService;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.serviceInterface.CanePlantationServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class CanePlantation
 */
@WebServlet("/canePlant")
public class CanePlantation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CanePlantation() {
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
			LoginServiceInterface login = new LoginService();
			MainResponse actionResponse = new ActionResponse();
			try {
				JSONObject reqObj = new JSONObject(json);
				actionResponse = login.checkAppUpdate(versionId, accessType, actionResponse);
				if (actionResponse.getUpdateResponse() != null && actionResponse.getUpdateResponse().isForceUpdate()) {
					result = new JSONObject(actionResponse);
					out.print(result);
					return;
				}
				CanePlantationServiceInterface canePlantation = new CanePlantationService();
				MainResponse empVillResponse = null;
				switch (action) {
				case "nondreport":
					MainResponse nondReport = new NondReportReponse();
					nondReport = canePlantation.nondReport(reqObj, imei, ramdomstring, chit_boy_id, accessType, nondReport);
					result = new JSONObject(nondReport);
					break;
				case "nondhangamreport":
					MainResponse nondHangam = new NondReportHangamReponse();
					nondHangam = canePlantation.nondHangamReport(reqObj, imei, ramdomstring, chit_boy_id, accessType, nondHangam);
					result = new JSONObject(nondHangam);
					break;
				case "empdata":
					empVillResponse = new EmpVillResponse();
					empVillResponse = canePlantation.empData(reqObj, imei, ramdomstring, chit_boy_id, accessType, empVillResponse);
					result = new JSONObject(empVillResponse);
					break;
				case "removeempvill":
					empVillResponse = new EmpVillResponse();
					empVillResponse = canePlantation.removeEmpVill(reqObj, imei, ramdomstring, chit_boy_id, accessType, empVillResponse);
					result = new JSONObject(empVillResponse);
					break;
				case "addempvill":
					empVillResponse = new EmpVillResponse();
					empVillResponse = canePlantation.addEmpVill(reqObj, imei, ramdomstring, chit_boy_id, accessType, empVillResponse);
					result = new JSONObject(empVillResponse);
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
				result = new JSONObject(actionResponse);
				e.printStackTrace();
			}
			out.print(result);
			out.flush();
		}
	}

}
