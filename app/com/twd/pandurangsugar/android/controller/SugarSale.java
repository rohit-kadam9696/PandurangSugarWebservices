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
import com.twd.pandurangsugar.android.bean.DataListResonse;
import com.twd.pandurangsugar.android.bean.FarmerSugarResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SugarInwardResponse;
import com.twd.pandurangsugar.android.bean.SugarSaleSavePrintResponse;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.service.SugarSaleService;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.SugarSaleServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class CaneHarvestingPlan
 */
@WebServlet("/sugarsale")
public class SugarSale extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SugarSale() {
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
				SugarSaleServiceInterface sugarSaleService=new SugarSaleService();
				//System.out.println(action);
				switch(action)
				{
					case "farmersugarinfo":
						MainResponse sugarInfo= new FarmerSugarResponse();
						sugarInfo=sugarSaleService.farmerSugarInfo(reqObj,imei,ramdomstring,chit_boy_id,accessType,sugarInfo);
						result=new JSONObject(sugarInfo);
					break;
					case "updatesugar":
						MainResponse saveSugar= new SugarSaleSavePrintResponse();
						saveSugar=sugarSaleService.saveSugarSale(reqObj,imei,ramdomstring,chit_boy_id,accessType,saveSugar);
						result=new JSONObject(saveSugar);
						break;
					case "sugarfarmerreprint":
						MainResponse printSugar= new SugarSaleSavePrintResponse();
						printSugar=sugarSaleService.printSugarSale(reqObj,imei,ramdomstring,chit_boy_id,accessType,printSugar);
						result=new JSONObject(printSugar);
						break;
					case "loaddo":
						MainResponse loaddoList= new DataListResonse();
						loaddoList=sugarSaleService.loaddoList(reqObj,imei,ramdomstring,chit_boy_id,accessType,loaddoList);
						result=new JSONObject(loaddoList);
						break;
					case "findSugarInward":
						MainResponse sugarInward= new SugarInwardResponse();
						sugarInward=sugarSaleService.findSugarInward(reqObj,imei,ramdomstring,chit_boy_id,accessType,sugarInward);
						result=new JSONObject(sugarInward);
						break;
					case "saveinward":
						MainResponse saveInward= new ActionResponse();
						saveInward=sugarSaleService.saveInward(json,imei,ramdomstring,chit_boy_id,accessType,saveInward);
						result=new JSONObject(saveInward);
						break;
					case "sugaroutwardreport":
						MainResponse sugarOutwardReport= new TableResponse();
						sugarOutwardReport=sugarSaleService.sugarOutwardReport(reqObj,imei,ramdomstring,chit_boy_id,accessType,sugarOutwardReport);
						result=new JSONObject(sugarOutwardReport);
						break;
					case "sugarsummaryreport":
						MainResponse sugarSummaryReport= new TableResponse();
						sugarSummaryReport=sugarSaleService.sugarSummaryReport(reqObj,imei,ramdomstring,chit_boy_id,accessType,sugarSummaryReport);
						result=new JSONObject(sugarSummaryReport);
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
			//System.out.println("result :"+result);
			out.print(result);
			out.flush();
		}
	
	
	
	}

}
