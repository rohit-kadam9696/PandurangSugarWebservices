package com.twd.pandurangsugar.android.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.FertProductResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.SavePrintResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.service.FarmerService;
import com.twd.pandurangsugar.android.service.FertService;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.serviceInterface.FarmerServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.FertServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class Fertilizer
 */
@WebServlet("/fert")
public class Fertilizer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Fertilizer() {
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
			MainResponse updateRes= new MainResponse();
			try 
			{
				if(json!=null) {
					json= new String(json.getBytes("ISO-8859-1"),"UTF-8");
				}
				JSONObject reqObj=new JSONObject(json);
				updateRes=login.checkAppUpdate(versionId,accessType,updateRes);
				if(updateRes.getUpdateResponse()!=null && updateRes.getUpdateResponse().isForceUpdate())
				{
					result=new JSONObject(updateRes);
					out.print(result);
					return;
				}
				FertServiceInterface fertService = new FertService();
				switch(action)
				{
					case "farmerdetailsbycode":
						FarmerServiceInterface farmer=new FarmerService();
						MainResponse farmerresponse= new MainResponse();
						farmerresponse=farmer.farmerDetailsByCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,farmerresponse);
						result=new JSONObject(farmerresponse);
					break;
					case "loadproduct":
						MainResponse fertProduct= new FertProductResponse();
						fertProduct=fertService.getProduct(reqObj,imei,ramdomstring,chit_boy_id,accessType,fertProduct);
						result=new JSONObject(fertProduct);
					break;
					case "savefert":
						MainResponse saveRes= new SavePrintResponse();
						fertProduct=fertService.saveFert(reqObj,imei,ramdomstring,chit_boy_id,accessType,saveRes);
						result=new JSONObject(saveRes);
					break;
					default :
						updateRes=login.verifyUser(updateRes,chit_boy_id,ramdomstring,imei,accessType);
						updateRes.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg("Requested Method Not Found Contact to sugar Factory");
						updateRes.setSe(error);
						result=new JSONObject(updateRes);
						break;
				}
				
				
			}catch (Exception e) {
				updateRes=login.verifyUser(updateRes,chit_boy_id,ramdomstring,imei,accessType);
				updateRes.setSuccess(true);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("controller Issue "+e.getMessage());
				updateRes.setSe(error);
				e.printStackTrace();
			}
			out.print(result);
			out.flush();
		}
	}

}
