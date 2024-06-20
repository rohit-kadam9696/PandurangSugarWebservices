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
import com.twd.pandurangsugar.android.bean.FarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.service.FarmerService;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.serviceInterface.FarmerServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class Farmer
 */
@WebServlet("/farmer")
public class Farmer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Farmer() {
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
			MainResponse farmerresponse= new FarmerResponse();
			try 
			{
				JSONObject reqObj=new JSONObject(json);
				farmerresponse=login.checkAppUpdate(versionId,accessType,farmerresponse);
				if(farmerresponse.getUpdateResponse()!=null && farmerresponse.getUpdateResponse().isForceUpdate())
				{
					result=new JSONObject(farmerresponse);
					out.print(result);
					return;
				}
				FarmerServiceInterface farmer=new FarmerService();
				switch(action)
				{
					case "farmerdetailsbycode":
						FarmerResponse all=farmer.farmerDetailsByCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,farmerresponse);
						result=new JSONObject(all);
					break;
					case "updateBirthDateByfarmerCode":
						MainResponse updateBDate= new ActionResponse();
						updateBDate=farmer.updateBirthDate(reqObj,imei,ramdomstring,chit_boy_id,accessType,updateBDate);
						result=new JSONObject(updateBDate);
					break;
					case "updateMobileNoByfarmerCode":
						MainResponse updateMobileNo= new ActionResponse();
						updateMobileNo=farmer.updateMobileNoByfarmerCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,updateMobileNo);
						result=new JSONObject(updateMobileNo);
					break;
					default :
						farmerresponse=login.verifyUser(farmerresponse,chit_boy_id,ramdomstring,imei,accessType);
						farmerresponse.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg("Requested Method Not Found Contact to sugar Factory");
						farmerresponse.setSe(error);
						result=new JSONObject(farmerresponse);
						break;
				}
				
				
			}catch (Exception e) {
				farmerresponse=login.verifyUser(farmerresponse,chit_boy_id,ramdomstring,imei,accessType);
				farmerresponse.setSuccess(true);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("controller Issue "+e.getMessage());
				farmerresponse.setSe(error);
				e.printStackTrace();
			}
			out.print(result);
		}
	
	}

}
