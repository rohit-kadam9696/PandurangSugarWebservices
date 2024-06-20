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
import com.twd.pandurangsugar.android.bean.CaneSamplePlantationData;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.service.CaneSampleService;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.serviceInterface.CaneSampleServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class RegistrationApproval
 */
@WebServlet("/caneSample")
public class CaneSample extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CaneSample() {
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
		if(accessType==null || accessType.trim().isEmpty())
			accessType="1";
		try(PrintWriter out=response.getWriter())
		{
			JSONObject result=null;
			MainResponse actionResponse= new ActionResponse();
			LoginServiceInterface login = new LoginService();
			try 
			{
				actionResponse=login.checkAppUpdate(versionId,accessType,actionResponse);
				if(actionResponse.getUpdateResponse()!=null && actionResponse.getUpdateResponse().isForceUpdate())
				{
					result=new JSONObject(actionResponse);
					out.print(result);
					return;
				}
				CaneSampleServiceInterface sampleService=new CaneSampleService();
				switch(action)
				{
					case "caneInfoByPlotAndYearCode":
						MainResponse caneSample= new CaneSamplePlantationData();
						JSONObject reqObj=new JSONObject(json);
						CaneSamplePlantationData sample =sampleService.caneInfoByPlotAndYearCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,caneSample);
						result=new JSONObject(sample);
					break;
					case "save":
						ActionResponse verify=sampleService.save(json,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(verify);
					break;
					case "canesampleinfo":
						MainResponse caneSampleInfo= new CaneSamplePlantationData();
						JSONObject req=new JSONObject(json);
						CaneSamplePlantationData sampleInfo =sampleService.caneSampleInfoByPlotNoAndYearCode(req,imei,ramdomstring,chit_boy_id,accessType,caneSampleInfo);
						result=new JSONObject(sampleInfo);
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
