package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.CaneSample;
import com.twd.pandurangsugar.android.bean.CaneSamplePlantationData;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.dao.CaneSampleDao;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.serviceInterface.CaneSampleServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CaneSampleService implements CaneSampleServiceInterface{


	LoginDao login=new LoginDao();
	CaneSampleDao caneSmapleDao=new CaneSampleDao();
	
	@Override
	public CaneSamplePlantationData caneInfoByPlotAndYearCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneSample) {

		try(Connection conn=DBConnection.getConnection())
		{
			caneSample=login.verifyUser(caneSample,chit_boy_id,ramdomstring,imei,accessType,conn);
			CaneSamplePlantationData caneSampleResponse=(CaneSamplePlantationData) caneSample;
			if(caneSampleResponse.isSuccess())
			{
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				if(nplotNo.trim().isEmpty() || yearCode.trim().isEmpty())
				{
					caneSampleResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter nplotNo Or yearCode");
					caneSampleResponse.setSe(error);
				}else
				{
					caneSampleResponse=caneSmapleDao.caneInfoByPlotAndYearCode(nplotNo,yearCode,caneSampleResponse,true,conn);
				}
				return caneSampleResponse;
			}
	}catch (Exception e) {
			caneSample.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			caneSample.setSe(error);
			e.printStackTrace();
		}
		return (CaneSamplePlantationData) caneSample;
	}

	@Override
	public ActionResponse save(String json, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse) {
	try(Connection conn=DBConnection.getConnection())
		{
		actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveResponse=(ActionResponse) actionResponse;
			if(saveResponse.isSuccess())
			{
				Gson gson = new Gson();
				CaneSample saveSample= gson.fromJson(json, CaneSample.class);
				saveResponse=caneSmapleDao.save(saveSample,saveResponse,conn);
				return saveResponse;
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
	public CaneSamplePlantationData caneSampleInfoByPlotNoAndYearCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneSample) {
		try(Connection conn=DBConnection.getConnection())
		{
			caneSample=login.verifyUser(caneSample,chit_boy_id,ramdomstring,imei,accessType,conn);
			CaneSamplePlantationData caneSampleResponse=(CaneSamplePlantationData) caneSample;
			if(caneSampleResponse.isSuccess())
			{
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				if(nplotNo.trim().isEmpty() || yearCode.trim().isEmpty())
				{
					caneSampleResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter nplotNo Or yearCode");
					caneSampleResponse.setSe(error);
				}else
				{
					caneSampleResponse=caneSmapleDao.caneInfoByPlotAndYearCode(nplotNo,yearCode,caneSampleResponse,false,conn);
					caneSampleResponse=caneSmapleDao.caneSampleInfoByPlotAndYearCode(nplotNo,yearCode,caneSampleResponse,conn);
				}
				return caneSampleResponse;
			}
	}catch (Exception e) {
			caneSample.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			caneSample.setSe(error);
			e.printStackTrace();
		}
		return (CaneSamplePlantationData) caneSample;
	
	}

}
