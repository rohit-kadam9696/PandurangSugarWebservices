package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.FarmerListResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.dao.RequestActionDao;
import com.twd.pandurangsugar.android.serviceInterface.RequestActionServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class RequestActionService implements RequestActionServiceInterface {

	RequestActionDao actiondao=new RequestActionDao();
	LoginDao login=new LoginDao();
	@Override
	public ActionResponse save(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveActionResponse=(ActionResponse) actionResponse;
			if(actionResponse.isSuccess())
			{
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"0";
				String nvillageId=reqObj.has("nvillage_id")?reqObj.getString("nvillage_id"):"0";
				saveActionResponse=actiondao.checkEntryExit(farmerCode,chit_boy_id,saveActionResponse,conn);
				if(saveActionResponse.isActionComplete())
				{
					saveActionResponse=actiondao.save(farmerCode,nvillageId,chit_boy_id,saveActionResponse,conn);	
				}
				
				return saveActionResponse;
			}
	}catch (Exception e) {
		actionResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			actionResponse.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) actionResponse;
	
	
	}
	@Override
	public ActionResponse verifyOrReject(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse) {

		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveActionResponse=(ActionResponse) actionResponse;
			if(actionResponse.isSuccess())
			{
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"0";
				String nvillageId=reqObj.has("nvillage_id")?reqObj.getString("nvillage_id"):"0";
				String status=reqObj.has("status")?reqObj.getString("status"):"0";
				String requestChitboyId=reqObj.has("requestChitboyId")?reqObj.getString("requestChitboyId"):"0";
				saveActionResponse=actiondao.verifyOrReject(farmerCode,nvillageId,chit_boy_id,status,requestChitboyId,saveActionResponse,conn);
				return saveActionResponse;
			}
	}catch (Exception e) {
		actionResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			actionResponse.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) actionResponse;
	
	
	
	}
	@Override
	public FarmerListResponse farmerList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerList,boolean isveriftList) {
		try(Connection conn=DBConnection.getConnection())
		{
			farmerList=login.verifyUser(farmerList,chit_boy_id,ramdomstring,imei,accessType,conn);
			FarmerListResponse farmerListResponse=(FarmerListResponse) farmerList;
			if(farmerList.isSuccess())
			{
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"0";
				String nvillageId=reqObj.has("nvillage_id")?reqObj.getString("nvillage_id"):"0";
				String status=reqObj.has("status")?reqObj.getString("status"):"0";
				farmerListResponse=actiondao.farmerList(farmerCode,nvillageId,chit_boy_id,status,farmerListResponse,isveriftList,conn);
				return farmerListResponse;
			}
		}catch (Exception e) {
			farmerList.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				farmerList.setSe(error);
				e.printStackTrace();
			}
			return (FarmerListResponse) farmerList;
		}

}
