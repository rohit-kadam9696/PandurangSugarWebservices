package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.CaneHarvestingPlanReasonResponse;
import com.twd.pandurangsugar.android.bean.CaneHarvestingPlanStart;
import com.twd.pandurangsugar.android.bean.GutKhadeResponse;
import com.twd.pandurangsugar.android.bean.HarvestingPlanFarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.dao.CaneHarvestingPlanDao;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.serviceInterface.CaneHarvestingPlanServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CaneHarvestingPlanService implements CaneHarvestingPlanServiceInterface {

	LoginDao login=new LoginDao();
	CaneHarvestingPlanDao harvestingPlanDao=new CaneHarvestingPlanDao();
	@Override
	public MainResponse findHarvestingPlanStartByYearCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse planStart) {
		try(Connection conn=DBConnection.getConnection())
		{
			planStart=login.verifyUser(planStart,chit_boy_id,ramdomstring,imei,accessType,conn);
			CaneHarvestingPlanStart canePlanStartResponse=(CaneHarvestingPlanStart) planStart;
			if(planStart.isSuccess())
			{
				
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				if(yearCode.trim().isEmpty())
				{
					canePlanStartResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode");
					canePlanStartResponse.setSe(error);
				}else
				{
					canePlanStartResponse=harvestingPlanDao.findHarvestingPlanStartByYearCode(yearCode,canePlanStartResponse,conn);
				}
				return canePlanStartResponse;
			}
		}catch (Exception e) {
			planStart.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				planStart.setSe(error);
				e.printStackTrace();
			}
		return (CaneHarvestingPlanStart)planStart;
	}
	@Override
	public MainResponse saveHarvestingPlanStart(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveResponse=(ActionResponse) actionResponse;
			if(actionResponse.isSuccess())
			{
				
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String dentryDate=reqObj.has("dentryDate")?reqObj.getString("dentryDate"):"";
				String ndaysPermit=reqObj.has("ndaysPermit")?reqObj.getString("ndaysPermit"):"";
				String nlimitTonnage=reqObj.has("nlimitTonnage")?reqObj.getString("nlimitTonnage"):"";
				String nlimitTonnageExtra=reqObj.has("nlimitTonnageExtra")?reqObj.getString("nlimitTonnageExtra"):"";
				if(yearCode.trim().isEmpty() || dentryDate.trim().isEmpty()|| ndaysPermit.trim().isEmpty()|| nlimitTonnage.trim().isEmpty()|| nlimitTonnageExtra.trim().isEmpty())
				{
					saveResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					saveResponse.setSe(error);
				}else
				{
					saveResponse=harvestingPlanDao.saveHarvestingPlanStart(yearCode,dentryDate,ndaysPermit,nlimitTonnage,nlimitTonnageExtra,saveResponse,conn);
				}
				return actionResponse;
			}
		}catch (Exception e) {
				actionResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				actionResponse.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)actionResponse;
	}
	@Override
	public MainResponse saveHarvestingPlanDate(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse) {

		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveResponse=(ActionResponse) actionResponse;
			if(actionResponse.isSuccess())
			{
				
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String dplantFromDate=reqObj.has("dplantFromDate")?reqObj.getString("dplantFromDate"):"";
				String dplantToDate=reqObj.has("dplantToDate")?reqObj.getString("dplantToDate"):"";
				String nhangam=reqObj.has("nhangam")?reqObj.getString("nhangam"):"";
				String nvariety=reqObj.has("nvariety")?reqObj.getString("nvariety"):"";
				if(yearCode.trim().isEmpty() || nvariety.trim().isEmpty()|| dplantToDate.trim().isEmpty()|| nhangam.trim().isEmpty()|| nvariety.trim().isEmpty())
				{
					saveResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					saveResponse.setSe(error);
				}else
				{
					saveResponse=harvestingPlanDao.saveHarvestingPlanDate(yearCode,dplantFromDate,dplantToDate,nhangam,nvariety,saveResponse,conn);
				}
				return actionResponse;
			}
		}catch (Exception e) {
				actionResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				actionResponse.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)actionResponse;
	
	}
	@Override
	public HarvestingPlanFarmerResponse farmerByCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse farmerRes) {
		try(Connection conn=DBConnection.getConnection())
		{
			farmerRes=login.verifyUser(farmerRes,chit_boy_id,ramdomstring,imei,accessType,conn);
			HarvestingPlanFarmerResponse farmerResponse=(HarvestingPlanFarmerResponse) farmerRes;
			if(farmerRes.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String farmerCode=reqObj.has("farmerCode")?reqObj.getString("farmerCode"):"";
				if(yearCode.trim().isEmpty())
				{
					farmerRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode");
					farmerRes.setSe(error);
				}else
				{
					farmerResponse=harvestingPlanDao.farmerByCode(farmerCode,yearCode,farmerResponse,conn);
				}
				return farmerResponse;
			}
		}catch (Exception e) {
				farmerRes.setSuccess(false);
				ServerError error=new ServerError();
				
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				farmerRes.setSe(error);
				e.printStackTrace();
			}
		return (HarvestingPlanFarmerResponse)farmerRes;
	
	}
	@Override
	public CaneHarvestingPlanReasonResponse reasonByGroupCode(int ngroupCode, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse reasonResponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			reasonResponse=login.verifyUser(reasonResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			CaneHarvestingPlanReasonResponse canePlanStartResponse=(CaneHarvestingPlanReasonResponse) reasonResponse;
			if(reasonResponse.isSuccess())
			{
				canePlanStartResponse=harvestingPlanDao.ressonByGroupCode(ngroupCode,canePlanStartResponse,conn);
				return canePlanStartResponse;
			}
		}catch (Exception e) {
			reasonResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				reasonResponse.setSe(error);
				e.printStackTrace();
			}
		return (CaneHarvestingPlanReasonResponse)reasonResponse;
	
	}
	@Override
	public ActionResponse saveHarvestingSpecialPlan(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse actionResponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveResponse=(ActionResponse) actionResponse;
			if(actionResponse.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String farmerCode=reqObj.has("farmerCode")?reqObj.getString("farmerCode"):"";
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				String nreasonId=reqObj.has("nreasonId")?reqObj.getString("nreasonId"):"";
				if(yearCode.trim().isEmpty() || farmerCode.trim().isEmpty()|| nplotNo.trim().isEmpty()|| nreasonId.trim().isEmpty())
				{
					saveResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					saveResponse.setSe(error);
				}else
				{
					if(nreasonId.equals("7"))
					{
						int slipCount=harvestingPlanDao.slipCount(yearCode,farmerCode,nplotNo,conn);
						if(slipCount==0)
						{
							saveResponse.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(ConstantMessage.ERROR_SlipNotExitSingleHarvestingPlan);
							saveResponse.setSe(error);
							return saveResponse;
						}else
						{
							int updateFlag=harvestingPlanDao.updateHarvestingFlag(yearCode,farmerCode,nplotNo,conn);
							if(updateFlag==0)
							{
								saveResponse.setSuccess(false);
								ServerError error=new ServerError();
								error.setError(ConstantVeriables.ERROR_006);
								error.setMsg(ConstantMessage.ERROR_plotExitSingleHarvestingPlan);
								saveResponse.setSe(error);
								return saveResponse;
							}
						}
						
					}
					saveResponse=harvestingPlanDao.saveHarvestingSpecialPlan(yearCode,farmerCode,nplotNo,nreasonId,chit_boy_id,saveResponse,conn);
					return saveResponse;
				}
				
			}
		}catch (Exception e) {
				actionResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				actionResponse.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)actionResponse;
	
	}
	@Override
	public ActionResponse saveGutKhade(String json, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse actionResponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveResponse=(ActionResponse) actionResponse;
			if(actionResponse.isSuccess())
			{
				Gson gson = new Gson();
				GutKhadeResponse saveReq = gson.fromJson(json, GutKhadeResponse.class);
				saveResponse=harvestingPlanDao.saveGutKhade(saveReq,chit_boy_id,saveResponse,conn);
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
		return (ActionResponse)actionResponse;
	}
	@Override
	public TableResponse gutkhadelist(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse gutList) {
		try(Connection conn=DBConnection.getConnection())
		{
			gutList=login.verifyUser(gutList,chit_boy_id,ramdomstring,imei,accessType,conn);
			TableResponse gutListRes=(TableResponse) gutList;
			if(gutListRes.isSuccess())
			{
				String dateVal=reqObj.has("dateVal")?reqObj.getString("dateVal"):"";
				String yearId=reqObj.has("yearId")?reqObj.getString("yearId"):"";
				String sectionId=reqObj.has("sectionId")?reqObj.getString("sectionId"):"";
				if(dateVal.trim().isEmpty() || yearId.trim().isEmpty())
				{
					gutListRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					gutListRes.setSe(error);
				}
				gutListRes=harvestingPlanDao.gutkhadeList(gutListRes,dateVal,yearId,sectionId,gutListRes,conn);
				return gutListRes;
				
			}
		}catch (Exception e) {
				gutList.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				gutList.setSe(error);
				e.printStackTrace();
			}
		return (TableResponse)gutList;
	}
	@Override
	public GutKhadeResponse editGutKhade(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse editKhade) {
		try(Connection conn=DBConnection.getConnection())
		{
			editKhade=login.verifyUser(editKhade,chit_boy_id,ramdomstring,imei,accessType,conn);
			GutKhadeResponse gutRes=(GutKhadeResponse) editKhade;
			if(gutRes.isSuccess())
			{
				String yearId=reqObj.has("yearId")?reqObj.getString("yearId"):"";
				String transId=reqObj.has("transId")?reqObj.getString("transId"):"";
				if(yearId.trim().isEmpty() || transId.trim().isEmpty())
				{
					gutRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					gutRes.setSe(error);
				}
				gutRes=harvestingPlanDao.editGutKhade(yearId,transId,gutRes,conn);
				return gutRes;
				
			}
		}catch (Exception e) {
			editKhade.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				editKhade.setSe(error);
				e.printStackTrace();
			}
		return (GutKhadeResponse)editKhade;
	
	}
	@Override
	public MainResponse deleteGutKhade(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse actionResponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveResponse=(ActionResponse) actionResponse;
			if(actionResponse.isSuccess())
			{
				String yearId=reqObj.has("yearId")?reqObj.getString("yearId"):"";
				String transId=reqObj.has("transId")?reqObj.getString("transId"):"";
				saveResponse=harvestingPlanDao.deleteGutKhade(yearId,transId,chit_boy_id,saveResponse,conn);
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
		return (ActionResponse)actionResponse;
	
	}
	

}
