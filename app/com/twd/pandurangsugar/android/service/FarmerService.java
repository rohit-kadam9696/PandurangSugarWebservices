package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.FarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.dao.FarmerDao;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.serviceInterface.FarmerServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class FarmerService implements FarmerServiceInterface {

	FarmerDao farmerdao=new FarmerDao();
	LoginDao login=new LoginDao();
	@Override
	public FarmerResponse farmerDetailsByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerresponse) {

		try(Connection conn=DBConnection.getConnection())
		{
			farmerresponse=login.verifyUser(farmerresponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			FarmerResponse dataLoadResponse=(FarmerResponse) farmerresponse;
			if(farmerresponse.isSuccess())
			{
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"0";
				dataLoadResponse=farmerdao.farmerDetailsByCode(farmerCode,dataLoadResponse,conn);
				return dataLoadResponse;
			}
	}catch (Exception e) {
			farmerresponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			farmerresponse.setSe(error);
			e.printStackTrace();
		}
		return (FarmerResponse) farmerresponse;
	
	}
	@Override
	public ActionResponse updateBirthDate(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerresponse) {
		try (Connection conn = DBConnection.getConnection()) {
			farmerresponse = login.verifyUser(farmerresponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			ActionResponse dataLoadResponse = (ActionResponse) farmerresponse;
			if (farmerresponse.isSuccess()) {
				String farmerCode = reqObj.has("nentityUniId") ? reqObj.getString("nentityUniId") : "";
				String dbirthDate = reqObj.has("dbirthDate") ? reqObj.getString("dbirthDate") : "";
				String age = reqObj.has("age") ? reqObj.getString("age") : "";
				dataLoadResponse = farmerdao.updateBirthDate(farmerCode, dbirthDate, age, chit_boy_id, dataLoadResponse, conn);
				return dataLoadResponse;
			}
		} catch (Exception e) {
			farmerresponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			farmerresponse.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) farmerresponse;
	}
	@Override
	public ActionResponse updateMobileNoByfarmerCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse farmerresponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			farmerresponse=login.verifyUser(farmerresponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse farmerResponse=(ActionResponse) farmerresponse;
			if(farmerresponse.isSuccess())
			{
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				String vmobileNo=reqObj.has("vmobileNo")?reqObj.getString("vmobileNo"):"";
				farmerResponse=farmerdao.updateMobileNoByfarmerCode(farmerCode,vmobileNo,chit_boy_id,farmerResponse,conn);
				return farmerResponse	;
			}
	}catch (Exception e) {
			farmerresponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			farmerresponse.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) farmerresponse;	
	}
	@Override
	public ActionResponse updateAadharByFarmerCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse farmerresponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			farmerresponse=login.verifyUser(farmerresponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse farmerResponse=(ActionResponse) farmerresponse;
			if(farmerresponse.isSuccess())
			{
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				String vaadhaarNo=reqObj.has("vaadhaarNo")?reqObj.getString("vaadhaarNo"):"";
				String vaadhaarPhoto=reqObj.has("vaadhaarPhoto")?reqObj.getString("vaadhaarPhoto"):"";
				if(farmerCode.trim().isEmpty() || vaadhaarNo.trim().isEmpty() || vaadhaarPhoto.trim().isEmpty()){
					farmerResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					farmerresponse.setSe(error);
				}else
				{
					farmerResponse=farmerdao.updateAadharByFarmerCode(farmerCode,vaadhaarNo,vaadhaarPhoto,chit_boy_id,farmerResponse,conn);
					return farmerResponse;
				}
				
			}
	}catch (Exception e) {
			farmerresponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			farmerresponse.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) farmerresponse;
	}
	@Override
	public ActionResponse updateBankByfarmerCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse farmerresponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			farmerresponse=login.verifyUser(farmerresponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse farmerResponse=(ActionResponse) farmerresponse;
			if(farmerresponse.isSuccess())
			{
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				String nbankId=reqObj.has("nbankId")?reqObj.getString("nbankId"):"";
				String vpassbookPhoto=reqObj.has("vpassbookPhoto")?reqObj.getString("vpassbookPhoto"):"";
				String vbankAcNo=reqObj.has("vbankAcNo")?reqObj.getString("vbankAcNo"):"";
				if(farmerCode.trim().isEmpty() || nbankId.trim().isEmpty() || vpassbookPhoto.trim().isEmpty() || vbankAcNo.trim().isEmpty() ){
					farmerResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					farmerresponse.setSe(error);
				}else
				{
					farmerResponse=farmerdao.updateBankByFarmerCode(farmerCode,nbankId,vpassbookPhoto,vbankAcNo,chit_boy_id,farmerResponse,conn);
					return farmerResponse;
				}
				
			}
	}catch (Exception e) {
			farmerresponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			farmerresponse.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) farmerresponse;
	}


	
}
