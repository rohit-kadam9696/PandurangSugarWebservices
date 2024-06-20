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
import com.twd.pandurangsugar.android.bean.CaneHarvestingPlanReasonResponse;
import com.twd.pandurangsugar.android.bean.CaneHarvestingPlanStart;
import com.twd.pandurangsugar.android.bean.GutKhadeResponse;
import com.twd.pandurangsugar.android.bean.HarvestingPlanFarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.service.CaneHarvestingPlanService;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.serviceInterface.CaneHarvestingPlanServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class CaneHarvestingPlan
 */
@WebServlet("/caneharvestingplan")
public class CaneHarvestingPlan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CaneHarvestingPlan() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getParameter("action");
		String json=request.getParameter("json");
		String imei=request.getParameter("imei");
		String chit_boy_id=request.getParameter("chit_boy_id");
		String ramdomstring=request.getParameter("randomstring");
		String versionId=request.getParameter("versionId");
		String accessType = request.getParameter("accessType");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		if (accessType == null || accessType.trim().isEmpty())
			accessType = "1";
		try(PrintWriter out=response.getWriter())
		{
			JSONObject result=null;
			LoginServiceInterface login = new LoginService();
			MainResponse actionResponse= new ActionResponse();
			try {
				JSONObject reqObj = new JSONObject(json);
				actionResponse = login.checkAppUpdate(versionId, accessType, actionResponse);
				if (actionResponse.getUpdateResponse() != null && actionResponse.getUpdateResponse().isForceUpdate()) {
					result=new JSONObject(actionResponse);
					out.print(result);
					return;
				}
				CaneHarvestingPlanServiceInterface harvestingPlanService=new CaneHarvestingPlanService();
				switch(action)
				{
					case "startplan":
						MainResponse planStart= new CaneHarvestingPlanStart();
						planStart=harvestingPlanService.findHarvestingPlanStartByYearCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,planStart);
						result=new JSONObject(planStart);
					break;
					case "savestartplan":
						actionResponse=harvestingPlanService.saveHarvestingPlanStart(reqObj,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(actionResponse);
					break;
					case "savedateplan":
						actionResponse=harvestingPlanService.saveHarvestingPlanDate(reqObj,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(actionResponse);
					break;
					case "farmerByCode":
						MainResponse farmerResponse= new HarvestingPlanFarmerResponse();
						farmerResponse=harvestingPlanService.farmerByCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,farmerResponse);
						result=new JSONObject(farmerResponse);
					break;
					case "harvprogramreason":
						MainResponse harvestinngReason= new CaneHarvestingPlanReasonResponse();
						int ngroupCode=1;
						harvestinngReason=harvestingPlanService.reasonByGroupCode(ngroupCode,imei,ramdomstring,chit_boy_id,accessType,harvestinngReason);
						result=new JSONObject(harvestinngReason);
					break;
					case "savespcialplan":
						actionResponse=harvestingPlanService.saveHarvestingSpecialPlan(reqObj,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(actionResponse);
					break;
					case "saveorupdategutkhade":
						actionResponse=harvestingPlanService.saveGutKhade(json,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(actionResponse);
					break;
					case "gutkhadelist":
						MainResponse gutList=new TableResponse();
						gutList=harvestingPlanService.gutkhadelist(reqObj,imei,ramdomstring,chit_boy_id,accessType,gutList);
						result=new JSONObject(gutList);
						break;
					case "editgutkhade":
						MainResponse editKhade=new GutKhadeResponse();
						editKhade=harvestingPlanService.editGutKhade(reqObj,imei,ramdomstring,chit_boy_id,accessType,editKhade);
						result=new JSONObject(editKhade);
						break;
					case "deletegutkhade":
						actionResponse=harvestingPlanService.deleteGutKhade(reqObj,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(actionResponse);
						break;
					default :
						actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType);
						actionResponse.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg("Requested Method Not Found Contact to sugar Factory");
						actionResponse.setSe(error);
						result=new JSONObject(actionResponse);
					break;
				}
				
				
			}catch (Exception e) {
				actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType);
				actionResponse.setSuccess(true);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("controller Issue "+e.getMessage());
				actionResponse.setSe(error);
				e.printStackTrace();
			}
			out.print(result);
		}
	
	
	
	}

}
