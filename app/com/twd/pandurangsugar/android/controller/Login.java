package com.twd.pandurangsugar.android.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.LoginResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class Login
 */
@WebServlet("/app_login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		LoginServiceInterface login = new LoginService();
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
			MainResponse mainresponse=new LoginResponse();
			try 
			{
				JSONObject reqObj=new JSONObject(json);
				mainresponse=login.checkAppUpdate(versionId,accessType,mainresponse);
				if(mainresponse.getUpdateResponse()!=null && mainresponse.getUpdateResponse().isForceUpdate())
				{
					result=new JSONObject(mainresponse);
					out.print(result);
					return;
				}
				switch(action)
				{
					case "login":
						LoginResponse loginRes=new LoginResponse();
						loginRes=login.appLogin(reqObj,imei,accessType,loginRes);
						result=new JSONObject(loginRes);
					break;
					case "verifyotp":
						mainresponse=login.verifyOTP(mainresponse,reqObj,imei,ramdomstring,chit_boy_id,accessType);
						result=new JSONObject(mainresponse);
						break;
					case "resendotp":
						mainresponse=login.resendOTP(mainresponse,reqObj,imei,ramdomstring,chit_boy_id,accessType);
						result=new JSONObject(mainresponse);
						break;
						default :
							mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType);
							mainresponse.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg("Requested Method Not Found COntact to sugar Factory");
							mainresponse.setSe(error);
							result=new JSONObject(mainresponse);
							break;
				}
			}catch (Exception e) {
				mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType);
				mainresponse.setSuccess(true);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("controller Issue "+e.getMessage());
				mainresponse.setSe(error);
				e.printStackTrace();
			}
			out.print(result);
		}
	}

}
