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
import com.twd.pandurangsugar.android.bean.FarmerListResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.service.RequestActionService;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.RequestActionServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class RegistrationApproval
 */
@WebServlet("/regApproval")
public class RegistrationApproval extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationApproval() {
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
			LoginServiceInterface login = new LoginService();
			MainResponse actionResponse= new ActionResponse();
			try 
			{
				JSONObject reqObj=new JSONObject(json);
				actionResponse=login.checkAppUpdate(versionId,accessType,actionResponse);
				if(actionResponse.getUpdateResponse()!=null && actionResponse.getUpdateResponse().isForceUpdate())
				{
					result=new JSONObject(actionResponse);
					out.print(result);
					return;
				}
				RequestActionServiceInterface actionService=new RequestActionService();
				switch(action)
				{
					case "save":
						ActionResponse save=actionService.save(reqObj,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(save);
					break;
					case "verifyOrReject":
						ActionResponse verify=actionService.verifyOrReject(reqObj,imei,ramdomstring,chit_boy_id,accessType,actionResponse);
						result=new JSONObject(verify);
					break;
					case "listforverify":
						MainResponse farmerList= new FarmerListResponse();
						farmerList=actionService.farmerList(reqObj,imei,ramdomstring,chit_boy_id,accessType,farmerList,true);
						result=new JSONObject(farmerList);
					break;
					case "listforchitboy":
						farmerList= new FarmerListResponse();
						farmerList=actionService.farmerList(reqObj,imei,ramdomstring,chit_boy_id,accessType,farmerList,false);
						result=new JSONObject(farmerList);
					break;
					default :
						actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType);
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
