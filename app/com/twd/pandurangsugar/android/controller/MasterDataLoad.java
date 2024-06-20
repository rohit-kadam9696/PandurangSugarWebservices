package com.twd.pandurangsugar.android.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.BankBranchResponse;
import com.twd.pandurangsugar.android.bean.BranchResponse;
import com.twd.pandurangsugar.android.bean.CaneConfirmationFarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.MasterDataResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.service.MasterDataLoadService;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.MasterDataLoadServiceInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class Farmer
 */
@WebServlet("/loaddata")
public class MasterDataLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MasterDataLoad() {
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
			MainResponse mainresponse= new MasterDataResponse();
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
				MasterDataLoadServiceInterface dataLoad=new MasterDataLoadService();
				switch(action)
				{
					case "all":
						MasterDataResponse all=dataLoad.loadMasterData(reqObj,imei,ramdomstring,chit_boy_id,1,accessType,mainresponse);
						result=new JSONObject(all);
					break;
					case "master":
						MasterDataResponse master=dataLoad.loadMasterData(reqObj,imei,ramdomstring,chit_boy_id,2,accessType,mainresponse);
						result=new JSONObject(master);
					break;
					case "farmerandvillege":
						MasterDataResponse farmerandvillege =dataLoad.loadMasterData(reqObj,imei,ramdomstring,chit_boy_id,3,accessType,mainresponse);
						result=new JSONObject(farmerandvillege);
					break;
					case "caneconfirmationfarmerlist":
						MainResponse confirnationList= new CaneConfirmationFarmerResponse();
						CaneConfirmationFarmerResponse resList =dataLoad.caneConfirmationFarmerList(reqObj,imei,ramdomstring,chit_boy_id,accessType,confirnationList);
						result=new JSONObject(resList);
					break;
					case "bankbranchbybankcode":
						MainResponse bankBranchList= new BankBranchResponse();
						BankBranchResponse branchList =dataLoad.bankBranchByBankCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,bankBranchList);
						result=new JSONObject(branchList);
					break;
					case "bankBranchByCode":
						MainResponse branchRes= new BranchResponse();
						BranchResponse branch =dataLoad.bankBranchByCode(reqObj,imei,ramdomstring,chit_boy_id,accessType,branchRes);
						result=new JSONObject(branch);
					break;
					default :
						mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType);
						mainresponse.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg("Requested Method Not Found COntact to sugar Factory");
						mainresponse.setSe(error);
						break;
				}
				
				
			}catch (Exception e) {
				mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType);
				mainresponse.setSuccess(true);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Master Data Load Issue "+e.getMessage());
				mainresponse.setSe(error);
				e.printStackTrace();
			}
			
			out.print(result);
		}
	}

}
