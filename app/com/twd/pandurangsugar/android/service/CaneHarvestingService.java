package com.twd.pandurangsugar.android.service;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.BulluckCartResponse;
import com.twd.pandurangsugar.android.bean.CaneDailyInwardReportResponse;
import com.twd.pandurangsugar.android.bean.CaneHarvestingPlanReasonResponse;
import com.twd.pandurangsugar.android.bean.CaneTransitResponse;
import com.twd.pandurangsugar.android.bean.CloseTransferResponse;
import com.twd.pandurangsugar.android.bean.CompletePlotResponse;
import com.twd.pandurangsugar.android.bean.ExcessTonPlotReqDataResponse;
import com.twd.pandurangsugar.android.bean.ExcessTonPlotReqResponse;
import com.twd.pandurangsugar.android.bean.FarmerTonnageResponse;
import com.twd.pandurangsugar.android.bean.HarvPlotDetailsResponse;
import com.twd.pandurangsugar.android.bean.HarvReportReponse;
import com.twd.pandurangsugar.android.bean.HarvSlipDetailsResponse;
import com.twd.pandurangsugar.android.bean.HarvestorResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NameListResponse;
import com.twd.pandurangsugar.android.bean.OtherUtilizationResponse;
import com.twd.pandurangsugar.android.bean.RemainingSlipResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.bean.TransporterResponse;
import com.twd.pandurangsugar.android.bean.VillageResonse;
import com.twd.pandurangsugar.android.bean.WeightSlipResponse;
import com.twd.pandurangsugar.android.bean.WireRopeResonse;
import com.twd.pandurangsugar.android.dao.CaneHarvestingDao;
import com.twd.pandurangsugar.android.dao.CaneHarvestingPlanDao;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.dao.SaveHarvestingDao;
import com.twd.pandurangsugar.android.serviceInterface.CaneHarvestingServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CaneHarvestingService implements CaneHarvestingServiceInterface {
	LoginDao login=new LoginDao();
	CaneHarvestingDao harvestingDao=new CaneHarvestingDao();
	@Override
	public HarvPlotDetailsResponse caneInfoByPlotAndYearCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneHarvReq) {
		try(Connection conn=DBConnection.getConnection())
		{
			caneHarvReq=login.verifyUser(caneHarvReq,chit_boy_id,ramdomstring,imei,accessType,conn);
			HarvPlotDetailsResponse caneharvPlanResponse=(HarvPlotDetailsResponse) caneHarvReq;
			if(caneHarvReq.isSuccess())
			{
				
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String plotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				if(yearCode.trim().isEmpty() || plotNo.trim().isEmpty())
				{
					caneharvPlanResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode & plot Number");
					caneharvPlanResponse.setSe(error);
				}else
				{
					caneharvPlanResponse=harvestingDao.findHarvestingPlanStartByYearCode(yearCode,plotNo,chit_boy_id,caneharvPlanResponse,conn);
					if(caneharvPlanResponse.getOpenIntent() != null && caneharvPlanResponse.getOpenIntent()==2)
					{
						int ngroupCode=2;
						CaneHarvestingPlanReasonResponse harvestinngReason=new CaneHarvestingPlanReasonResponse();
						harvestinngReason=new CaneHarvestingPlanDao().ressonByGroupCode(ngroupCode,harvestinngReason,conn);
						caneharvPlanResponse.setReasonList(harvestinngReason.getReasonList());
					}
				}
				return caneharvPlanResponse;
			}
		}catch (Exception e) {
				caneHarvReq.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				caneHarvReq.setSe(error);
				e.printStackTrace();
			}
		return (HarvPlotDetailsResponse)caneHarvReq;
	}
	@Override
	public TransporterResponse findTranspoterByCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneTransRes) {
		try(Connection conn=DBConnection.getConnection())
		{
			caneTransRes=login.verifyUser(caneTransRes,chit_boy_id,ramdomstring,imei,accessType,conn);
			TransporterResponse caneTransResponse=(TransporterResponse) caneTransRes;
			if(caneTransResponse.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String transCode=reqObj.has("transCode")?reqObj.getString("transCode"):"";
				String vehicleType=reqObj.has("vehicleType")?reqObj.getString("vehicleType"):"";
				String nslipNo=reqObj.has("nslipNo")?reqObj.getString("nslipNo"):"";
				String nsectionId=reqObj.has("nsectionId")?reqObj.getString("nsectionId"):"";
				//  check weight slip entry
				if(yearCode.trim().isEmpty() || transCode.trim().isEmpty()|| vehicleType.trim().isEmpty() || nsectionId.isEmpty())
				{
					caneTransResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode, transCode, vehicleType and sectionId");
					caneTransResponse.setSe(error);
				} else {
					int hold = harvestingDao.holdTime(vehicleType, conn);
					if(hold>0) {
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, -hold);
						caneTransResponse = harvestingDao.checkSlipTime(transCode,vehicleType,yearCode, cal.getTime(), hold,caneTransResponse,conn);
					} else {
						caneTransResponse = (TransporterResponse) harvestingDao.checkGutKhade(vehicleType,yearCode, nsectionId, (MainResponse)caneTransResponse,conn);
					}
					if(caneTransResponse.isSuccess()) {
						caneTransResponse=harvestingDao.findTranspoterByCode(transCode,vehicleType,yearCode, true,caneTransResponse,conn);
						if(caneTransResponse.isSuccess()) {
							HashMap<String, Set<String>> list=harvestingDao.isWeightSlipExit(transCode,vehicleType,yearCode,nslipNo,conn);
							caneTransResponse=(TransporterResponse) harvestingDao.getWireropeList(vehicleType,0,0,0,(WireRopeResonse) caneTransResponse,conn);
							if(!transCode.equalsIgnoreCase("1") && !transCode.equalsIgnoreCase("2") && !transCode.equalsIgnoreCase("3"))
								caneTransResponse=harvestingDao.removeWirepoe(vehicleType,list,caneTransResponse);
						}
					}
				}
				return caneTransResponse;
				}
		}catch (Exception e) {
				caneTransRes.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				caneTransRes.setSe(error);
				e.printStackTrace();
			}
		return (TransporterResponse)caneTransRes;
	
	}
	@Override
	public HarvestorResponse findHarvestorByCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneHarvRes) {
		try(Connection conn=DBConnection.getConnection())
		{
			caneHarvRes=login.verifyUser(caneHarvRes,chit_boy_id,ramdomstring,imei,accessType,conn);
			HarvestorResponse caneharvPlanResponse=(HarvestorResponse) caneHarvRes;
			if(caneHarvRes.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String harvCode=reqObj.has("harvCode")?reqObj.getString("harvCode"):"";
				String vehicleType=reqObj.has("vehicleType")?reqObj.getString("vehicleType"):"";
				String nsectionId=reqObj.has("nsectionId")?reqObj.getString("nsectionId"):"";
				if(yearCode.trim().isEmpty() || harvCode.trim().isEmpty() || vehicleType.trim().isEmpty() || nsectionId.trim().isEmpty())
				{
					caneharvPlanResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode, harvCode, vehicleType & sectionId");
					caneharvPlanResponse.setSe(error);
				}else
				{
					caneharvPlanResponse = (HarvestorResponse) harvestingDao.checkGutKhade(vehicleType, yearCode, nsectionId, (MainResponse) caneharvPlanResponse, conn);
					if (caneharvPlanResponse.isSuccess())
						caneharvPlanResponse = harvestingDao.findHarvestingInfoByCode(yearCode, harvCode, true, caneharvPlanResponse, conn);
				}
				return caneharvPlanResponse;
			}
		}catch (Exception e) {
				caneHarvRes.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				caneHarvRes.setSe(error);
				e.printStackTrace();
			}
		return (HarvestorResponse)caneHarvRes;
	}
	@Override
	public BulluckCartResponse findMukadamByCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse caneMukhRes) {
		try(Connection conn=DBConnection.getConnection())
		{
			caneMukhRes=login.verifyUser(caneMukhRes,chit_boy_id,ramdomstring,imei,accessType,conn);
			BulluckCartResponse caneMukhResponse=(BulluckCartResponse) caneMukhRes;
			if(caneMukhResponse.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String mukadamCode=reqObj.has("mukadamCode")?reqObj.getString("mukadamCode"):"";
				String vehicleType=reqObj.has("vehicleType")?reqObj.getString("vehicleType"):"";
				String nsectionId =reqObj.has("nsectionId")?reqObj.getString("nsectionId"):"";
				String nslipNo=reqObj.has("nslipNo")?reqObj.getString("nslipNo"):"";
				if(yearCode.trim().isEmpty() || mukadamCode.trim().isEmpty() || vehicleType.trim().isEmpty() || nsectionId.trim().isEmpty())
				{
					caneMukhResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode, mukadamCode, vehicleType & sectionId");
					caneMukhResponse.setSe(error);
				}else
				{
					HashMap<String, String> oldSlip = new HashMap<>();
					int hold = harvestingDao.holdTime(vehicleType, conn);
					if(hold>0) {
						Calendar cal = Calendar.getInstance();
						cal.add(Calendar.MINUTE, -hold);
						oldSlip = harvestingDao.checkSlipTimeBulluck(mukadamCode,vehicleType,yearCode, cal.getTime(), hold,conn);
					}
					 
					caneMukhResponse = (BulluckCartResponse) harvestingDao.checkGutKhade(vehicleType,yearCode, nsectionId, (MainResponse)caneMukhResponse,conn);
					if (caneMukhResponse.isSuccess()) {
						caneMukhResponse=harvestingDao.findMukadamInfoByCode(yearCode,mukadamCode, true,caneMukhResponse,conn);
						if (caneMukhResponse.isSuccess()) {
								caneMukhResponse=harvestingDao.getBullockList(mukadamCode,vehicleType,yearCode,nslipNo,null, true, oldSlip, hold,caneMukhResponse,conn);
								caneMukhResponse=(BulluckCartResponse) harvestingDao.getWireropeList(vehicleType,0,0,0,(WireRopeResonse) caneMukhResponse,conn);
							}
						}
					}
				return caneMukhResponse;
			}
		}catch (Exception e) {
			caneMukhRes.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				caneMukhRes.setSe(error);
				e.printStackTrace();
			}
		return (BulluckCartResponse)caneMukhRes;
	//caneTransResponse=harvestingDao.getWireropeList(transCode,vehicleType,yearCode,caneTransResponse,conn);
	}
	@Override
	public WeightSlipResponse savews(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse savews) {
		try(Connection conn=DBConnection.getConnection())
		{
			savews=login.verifyUser(savews,chit_boy_id,ramdomstring,imei,accessType,conn);
			WeightSlipResponse savewsRes=(WeightSlipResponse) savews;
			if(savewsRes.isSuccess())
			{
				savewsRes=SaveHarvestingDao.saveWeightSlip(reqObj,savewsRes,conn);
				return savewsRes;
			}
		}catch (Exception e) {
			savews.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				savews.setSe(error);
				e.printStackTrace();
			}
		return (WeightSlipResponse)savews;
	
	}
	@Override
	public RemainingSlipResponse remainingSlipList(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse sliplist) {
		try(Connection conn=DBConnection.getConnection())
		{
			sliplist=login.verifyUser(sliplist,chit_boy_id,ramdomstring,imei,accessType,conn);
			RemainingSlipResponse sliplistResponse=(RemainingSlipResponse) sliplist;
			if(sliplistResponse.isSuccess())
			{
				
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String vehicleType=reqObj.has("vehicleType")?reqObj.getString("vehicleType"):"";
				String transCode=reqObj.has("transCode")?reqObj.getString("transCode"):"";
				String bullockCode=reqObj.has("bullockCode")?reqObj.getString("bullockCode"):"";
				if(yearCode.trim().isEmpty() || vehicleType.trim().isEmpty())
				{
					sliplistResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode & vehicleType");
					sliplistResponse.setSe(error);
				}else
				{
					sliplistResponse=harvestingDao.remainingSlipList(yearCode,vehicleType,transCode,bullockCode,chit_boy_id,sliplistResponse,conn);
				}
				return sliplistResponse;
			}
		}catch (Exception e) {
			sliplist.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				sliplist.setSe(error);
				e.printStackTrace();
			}
		return (RemainingSlipResponse)sliplist;
	
	}
	@Override
	public ActionResponse detivateSlip(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse detivateSlip) {
		try(Connection conn=DBConnection.getConnection())
		{
			detivateSlip=login.verifyUser(detivateSlip,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse savewsRes=(ActionResponse) detivateSlip;
			if(savewsRes.isSuccess())
			{
				String nslipNo=reqObj.has("nslip_no")?reqObj.getString("nslip_no"):"";
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				if(yearCode.trim().isEmpty() || nslipNo.trim().isEmpty())
				{
					savewsRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode & Slip No");
					savewsRes.setSe(error);
				}else
				{
					savewsRes=harvestingDao.detivateSlip(nslipNo,yearCode,chit_boy_id,savewsRes,conn);
				}
				
				return savewsRes;
			}
		}catch (Exception e) {
				detivateSlip.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				detivateSlip.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)detivateSlip;
	}
	@Override
	public HarvSlipDetailsResponse slipeditInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse slipedit) {
		try(Connection conn=DBConnection.getConnection())
		{
			slipedit=login.verifyUser(slipedit,chit_boy_id,ramdomstring,imei,accessType,conn);
			HarvSlipDetailsResponse slipeditRes=(HarvSlipDetailsResponse) slipedit;
			if(slipeditRes.isSuccess())
			{
				String nslipNo=reqObj.has("nslipNo")?reqObj.getString("nslipNo"):"";
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				if(yearCode.trim().isEmpty() || nslipNo.trim().isEmpty())
				{
					slipeditRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode & Slip No");
					slipeditRes.setSe(error);
				}else
				{
					slipeditRes=harvestingDao.slipeditInfo(nslipNo,yearCode,chit_boy_id,slipeditRes,conn);
				}
				return slipeditRes;
			}
		}catch (Exception e) {
			slipedit.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				slipedit.setSe(error);
				e.printStackTrace();
			}
		return (HarvSlipDetailsResponse)slipedit;
	}
	@Override
	public ActionResponse saveExtraPlotRequest(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse saveextraplot) {

		try(Connection conn=DBConnection.getConnection())
		{
			saveextraplot=login.verifyUser(saveextraplot,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse savewsRes=(ActionResponse) saveextraplot;
			if(savewsRes.isSuccess())
			{
					savewsRes=harvestingDao.saveExtraPlotRequest(reqObj,chit_boy_id,savewsRes,conn);
				return savewsRes;
			}
		}catch (Exception e) {
			saveextraplot.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				saveextraplot.setSe(error);
				e.printStackTrace();
			}
		return (WeightSlipResponse)saveextraplot;
	}
	@Override
	public ExcessTonPlotReqResponse extraPlotReqList(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse extraPlotreqList) {
		try(Connection conn=DBConnection.getConnection())
		{
			extraPlotreqList=login.verifyUser(extraPlotreqList,chit_boy_id,ramdomstring,imei,accessType,conn);
			ExcessTonPlotReqResponse extraPlotReqListRes=(ExcessTonPlotReqResponse) extraPlotreqList;
			if(extraPlotReqListRes.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				if(yearCode.trim().isEmpty())
				{
					extraPlotReqListRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode");
					extraPlotReqListRes.setSe(error);
				}else
				{
					extraPlotReqListRes=harvestingDao.extraPlotReqList(yearCode,chit_boy_id,extraPlotReqListRes,conn);
				}
				
				return extraPlotReqListRes;
			}
		}catch (Exception e) {
			extraPlotreqList.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				extraPlotreqList.setSe(error);
				e.printStackTrace();
			}
		return (ExcessTonPlotReqResponse)extraPlotreqList;
	}
	@Override
	public ExcessTonPlotReqDataResponse excessPlotDetails(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse excessPlotDetails) {
		try(Connection conn=DBConnection.getConnection())
		{
			excessPlotDetails=login.verifyUser(excessPlotDetails,chit_boy_id,ramdomstring,imei,accessType,conn);
			ExcessTonPlotReqDataResponse excessPlot=(ExcessTonPlotReqDataResponse) excessPlotDetails;
			if(excessPlot.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				if(yearCode.trim().isEmpty() || nplotNo.trim().isEmpty())
				{
					excessPlot.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					excessPlot.setSe(error);
				}else
				{
					excessPlot=harvestingDao.excessPlotDetails(yearCode,nplotNo,excessPlot,conn);
				}
				
				return excessPlot;
			}
		}catch (Exception e) {
				excessPlotDetails.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				excessPlotDetails.setSe(error);
				e.printStackTrace();
			}
		return (ExcessTonPlotReqDataResponse)excessPlotDetails;
	}
	@Override
	public ActionResponse acceptOrRejectExtraPlot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse acceptPlotReq) {

		try(Connection conn=DBConnection.getConnection())
		{
			acceptPlotReq=login.verifyUser(acceptPlotReq,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse savewsRes=(ActionResponse) acceptPlotReq;
			if(savewsRes.isSuccess())
			{
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				String yearCode=reqObj.has("vyearCode")?reqObj.getString("vyearCode"):"";
				String nstatusId=reqObj.has("nstatusId")?reqObj.getString("nstatusId"):"";
				String nallowedTonnage=reqObj.has("nallowedTonnage")?reqObj.getString("nallowedTonnage"):null;
				if(yearCode.trim().isEmpty() || nplotNo.trim().isEmpty() || nstatusId.trim().isEmpty())
				{
					savewsRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					savewsRes.setSe(error);
				}else
				{
					savewsRes=harvestingDao.acceptOrRejectExtraPlot(nplotNo,yearCode,nstatusId,nallowedTonnage,chit_boy_id,savewsRes,conn);
				}
				
				return savewsRes;
			}
		}catch (Exception e) {
				acceptPlotReq.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				acceptPlotReq.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)acceptPlotReq;
	
	}
	@Override
	public OtherUtilizationResponse otherUtilizationDetails(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse otherUtilization) {
		try(Connection conn=DBConnection.getConnection())
		{
			otherUtilization=login.verifyUser(otherUtilization,chit_boy_id,ramdomstring,imei,accessType,conn);
			OtherUtilizationResponse otherUtilizationRes=(OtherUtilizationResponse) otherUtilization;
			if(otherUtilizationRes.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				if(yearCode.trim().isEmpty() || nplotNo.trim().isEmpty())
				{
					otherUtilizationRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					otherUtilizationRes.setSe(error);
				}else
				{
					otherUtilizationRes=harvestingDao.otherUtilizationDetails(yearCode,nplotNo,chit_boy_id,otherUtilizationRes,conn);
					int ngroupCode=3;
					CaneHarvestingPlanReasonResponse harvestinngReason=new CaneHarvestingPlanReasonResponse();
					harvestinngReason=new CaneHarvestingPlanDao().ressonByGroupCode(ngroupCode,harvestinngReason,conn);
					otherUtilizationRes.setRemarkList(harvestinngReason.getReasonList());
				}
				
				return otherUtilizationRes;
			}
		}catch (Exception e) {
			otherUtilization.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				otherUtilization.setSe(error);
				e.printStackTrace();
			}
		return (OtherUtilizationResponse)otherUtilization;
	}
	@Override
	public ActionResponse saveOtherUtilization(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse saveOtherUti) {
		try(Connection conn=DBConnection.getConnection())
		{
			saveOtherUti=login.verifyUser(saveOtherUti,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveOtherUtiRes=(ActionResponse) saveOtherUti;
			if(saveOtherUtiRes.isSuccess())
			{
				String vyearId=reqObj.has("vyearId")?reqObj.getString("vyearId"):"";
				String nentityUniId=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				String nfactId=reqObj.has("nfactId")?reqObj.getString("nfactId"):"";
				String nreasonId=reqObj.has("nreasonId")?reqObj.getString("nreasonId"):"";
				String nareaAllowed=reqObj.has("nareaAllowed")?reqObj.getString("nareaAllowed"):"";
				String nexpectedYield=reqObj.has("nexpectedYield")?reqObj.getString("nexpectedYield"):"";
				boolean equal=reqObj.has("equal")?reqObj.getBoolean("equal"):false;
				if(vyearId.trim().isEmpty() || nentityUniId.trim().isEmpty() || nplotNo.trim().isEmpty()
						 || nfactId.trim().isEmpty()  || nreasonId.trim().isEmpty()  || nareaAllowed.trim().isEmpty()
						 || nexpectedYield.trim().isEmpty())
				{
					saveOtherUtiRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					saveOtherUtiRes.setSe(error);
				}else
				{
					saveOtherUtiRes=harvestingDao.saveOtherUtilization(vyearId,nentityUniId,nplotNo,nfactId,nreasonId,nareaAllowed,nexpectedYield,equal,chit_boy_id,saveOtherUtiRes,conn);
				}
				
				return saveOtherUtiRes;
			}
		}catch (Exception e) {
			saveOtherUti.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				saveOtherUti.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)saveOtherUti;
	}
	@Override
	public CaneDailyInwardReportResponse dailyInwardReport(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse dailyCaneReport) {
		try(Connection conn=DBConnection.getConnection())
		{
			dailyCaneReport=login.verifyUser(dailyCaneReport,chit_boy_id,ramdomstring,imei,accessType,conn);
			CaneDailyInwardReportResponse dailyCaneReportRes=(CaneDailyInwardReportResponse) dailyCaneReport;
			if(dailyCaneReportRes.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String vehicleType=reqObj.has("vehicleType")?reqObj.getString("vehicleType"):"";
				String slipDate=reqObj.has("slipDate")?reqObj.getString("slipDate"):"";
				if(yearCode.trim().isEmpty() || vehicleType.trim().isEmpty())
				{
					dailyCaneReportRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					dailyCaneReportRes.setSe(error);
				}else
				{
					dailyCaneReportRes=harvestingDao.dailyInwardReport(yearCode,vehicleType,slipDate,chit_boy_id,dailyCaneReportRes,conn);
				}
				return dailyCaneReportRes;
			}
		}catch (Exception e) {
				dailyCaneReport.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				dailyCaneReport.setSe(error);
				e.printStackTrace();
			}
		return (CaneDailyInwardReportResponse)dailyCaneReport;
	}
	@Override
	public ActionResponse verifySlip(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse verifyslip) {
		try(Connection conn=DBConnection.getConnection())
		{
			verifyslip=login.verifyUser(verifyslip,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse verifyslipRes=(ActionResponse) verifyslip;
			if(verifyslipRes.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String nslipNo=reqObj.has("nslipNo")?reqObj.getString("nslipNo"):"";
				if(yearCode.trim().isEmpty() || nslipNo.trim().isEmpty())
				{
					verifyslipRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					verifyslipRes.setSe(error);
				}else
				{
					verifyslipRes=harvestingDao.verifySlip(yearCode,nslipNo,chit_boy_id,verifyslipRes,conn);
				}
				
				return verifyslipRes;
			}
		}catch (Exception e) {
				verifyslip.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				verifyslip.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)verifyslip;
	
	}
	@Override
	public CompletePlotResponse plotByFarmer(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse plotdetails) {

		try(Connection conn=DBConnection.getConnection())
		{
			plotdetails=login.verifyUser(plotdetails,chit_boy_id,ramdomstring,imei,accessType,conn);
			CompletePlotResponse plotdetailsRes=(CompletePlotResponse) plotdetails;
			if(plotdetailsRes.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String nentityUniId=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				if(yearCode.trim().isEmpty() || nentityUniId.trim().isEmpty())
				{
					plotdetailsRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					plotdetailsRes.setSe(error);
				}else
				{
					plotdetailsRes=harvestingDao.plotByFarmer(yearCode,nentityUniId, chit_boy_id,plotdetailsRes,conn);
				}
				return plotdetailsRes;
			}
		}catch (Exception e) {
				plotdetails.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				plotdetails.setSe(error);
				e.printStackTrace();
			}
		return (CompletePlotResponse)plotdetails;
	
	}
	@Override
	public CloseTransferResponse closeTransResponse(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse closetranres) {
		try(Connection conn=DBConnection.getConnection())
		{
			closetranres=login.verifyUser(closetranres,chit_boy_id,ramdomstring,imei,accessType,conn);
			CloseTransferResponse closetranResponse=(CloseTransferResponse) closetranres;
			if(closetranResponse.isSuccess())
			{
				String transfer=reqObj.has("transfer")?reqObj.getString("transfer"):"";
				String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):"";
				String vyearId=reqObj.has("vyearId")?reqObj.getString("vyearId"):"";
				String dmindate=reqObj.has("dmindate")?reqObj.getString("dmindate"):"";
				String narea=reqObj.has("narea")?reqObj.getString("narea"):"";
				if(transfer.trim().isEmpty() || nplotNo.trim().isEmpty() || vyearId.trim().isEmpty() || dmindate.trim().isEmpty())
				{
					closetranResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					closetranResponse.setSe(error);
				}else
				{
					closetranResponse = harvestingDao.closeTransResponse(transfer, nplotNo, vyearId, dmindate, narea, chit_boy_id, closetranResponse, conn);
				}
				return closetranResponse;
			}
		}catch (Exception e) {
				closetranres.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				closetranres.setSe(error);
				e.printStackTrace();
			}
		return (CloseTransferResponse)closetranres;
	}
	@Override
	public CaneTransitResponse vehicleTransitInfo(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType, MainResponse vehicleTransitInfo) {
		try(Connection conn=DBConnection.getConnection())
		{
			vehicleTransitInfo=login.verifyUser(vehicleTransitInfo,chit_boy_id,ramdomstring,imei,accessType,conn);
			CaneTransitResponse caneharvPlanResponse=(CaneTransitResponse) vehicleTransitInfo;
			if(vehicleTransitInfo.isSuccess())
			{
				
				String udate=reqObj.has("dateVal")?reqObj.getString("dateVal"):"";
				String yearId=reqObj.has("yearId")?reqObj.getString("yearId"):"";
				String villageId=reqObj.has("villageId")?reqObj.getString("villageId"):"";
				if(udate.trim().isEmpty() || yearId.trim().isEmpty()  || villageId.trim().isEmpty())
				{
					caneharvPlanResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode, date & villeage ");
					caneharvPlanResponse.setSe(error);
				}else
				{
					// check current date
					 LocalDate today = LocalDate.now();
					 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
					 LocalDate userDate =LocalDate.parse(udate, formatter);
					if(!today.isEqual(userDate))
					{
						caneharvPlanResponse.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg("Date error");
						caneharvPlanResponse.setSe(error);
					}else
					{
						boolean isTimeValidation=timeValidation();
						if(isTimeValidation)
						{
							caneharvPlanResponse=harvestingDao.vehicleTransitInfo(udate,yearId,villageId,chit_boy_id,caneharvPlanResponse,conn);
						}else
						{
							caneharvPlanResponse.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(String.format(ConstantMessage.timeError1,ConstantVeriables.fromTimeRawana,ConstantVeriables.toTimeRawana));
							caneharvPlanResponse.setSe(error);
						}
					}
				}
				return caneharvPlanResponse;
			}
		}catch (Exception e) {
			vehicleTransitInfo.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				vehicleTransitInfo.setSe(error);
				e.printStackTrace();
			}
		return (CaneTransitResponse)vehicleTransitInfo;
	
	}
	private boolean timeValidation() {
		Calendar todayCal = Calendar.getInstance();
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();
        fromCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ConstantVeriables.fromTimeRawana.split(":")[0]));
        fromCal.set(Calendar.MINUTE, Integer.parseInt(ConstantVeriables.fromTimeRawana.split(":")[1]));
        toCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ConstantVeriables.toTimeRawana.split(":")[0]));
        toCal.set(Calendar.MINUTE, Integer.parseInt(ConstantVeriables.toTimeRawana.split(":")[1]));
        return fromCal.getTime().before(todayCal.getTime()) && toCal.getTime().after(todayCal.getTime());
	}
	@Override
	public ActionResponse saveTransit(String json, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse saveTransit) {
		try(Connection conn=DBConnection.getConnection())
		{
			saveTransit=login.verifyUser(saveTransit,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse saveRes=(ActionResponse) saveTransit;
			if(saveRes.isSuccess())
			{
				Gson gson = new Gson();
				CaneTransitResponse saveReq = gson.fromJson(json, CaneTransitResponse.class);
				String udate = saveReq.getDateVal();
				LocalDate today = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate userDate = LocalDate.parse(udate, formatter);
				if (!today.isEqual(userDate)) {
					saveRes.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Date error");
					saveRes.setSe(error);
				} else {
					boolean isTimeValidation=timeValidation();
					if(isTimeValidation)
					{
						saveRes = harvestingDao.saveTransit(saveReq, chit_boy_id, saveRes, conn);
					}else
					{
						saveRes.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(String.format(ConstantMessage.timeError1,ConstantVeriables.fromTimeRawana,ConstantVeriables.toTimeRawana));
						saveRes.setSe(error);
					}
					
				}
				return saveRes;
			}
		}catch (Exception e) {
				saveTransit.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				saveTransit.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)saveTransit;
	}
	@Override
	public TableResponse sectionWiseUsRawanaReport(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType, MainResponse sectionWiseUsRawanaReport) {
		try(Connection conn=DBConnection.getConnection())
		{
			sectionWiseUsRawanaReport=login.verifyUser(sectionWiseUsRawanaReport,chit_boy_id,ramdomstring,imei,accessType,conn);
			TableResponse reqResponse=(TableResponse) sectionWiseUsRawanaReport;
			if(reqResponse.isSuccess())
			{
				String udate=reqObj.has("dateVal")?reqObj.getString("dateVal"):"";
				String yearId=reqObj.has("yearId")?reqObj.getString("yearId"):"";
				if(udate.trim().isEmpty() || yearId.trim().isEmpty())
				{
					sectionWiseUsRawanaReport.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode, date");
					sectionWiseUsRawanaReport.setSe(error);
				}else
				{
					// check current date
					reqResponse=harvestingDao.sectionWiseUsRawanaReport(udate,yearId,reqResponse,conn);
				}
				return reqResponse;
			
			}
		}catch (Exception e) {
			sectionWiseUsRawanaReport.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				sectionWiseUsRawanaReport.setSe(error);
				e.printStackTrace();
			}
		return (TableResponse)sectionWiseUsRawanaReport;
	}
	@Override
	public TableResponse villeageWiseUsRawanaReport(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType, MainResponse villeageWiseUsRawanaReport) {
		try(Connection conn=DBConnection.getConnection())
		{
			villeageWiseUsRawanaReport=login.verifyUser(villeageWiseUsRawanaReport,chit_boy_id,ramdomstring,imei,accessType,conn);
			TableResponse reqResponse=(TableResponse) villeageWiseUsRawanaReport;
			if(reqResponse.isSuccess())
			{
				String udate=reqObj.has("dateVal")?reqObj.getString("dateVal"):"";
				String yearId=reqObj.has("yearId")?reqObj.getString("yearId"):"";
				String sectionId=reqObj.has("sectionId")?reqObj.getString("sectionId"):"";
				if(udate.trim().isEmpty() || yearId.trim().isEmpty() || sectionId.trim().isEmpty())
				{
					villeageWiseUsRawanaReport.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode, date and sectionId ");
					villeageWiseUsRawanaReport.setSe(error);
				}else
				{
					// check current date
					reqResponse=harvestingDao.villeageWiseUsRawanaReport(udate,yearId,sectionId,reqResponse,conn);
				}
				return reqResponse;
			
			}
		}catch (Exception e) {
			villeageWiseUsRawanaReport.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				villeageWiseUsRawanaReport.setSe(error);
				e.printStackTrace();
			}
		return (TableResponse)villeageWiseUsRawanaReport;
	
	}
	@Override
	public VillageResonse sectionByVillageList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse villageResonse) {
		try(Connection conn=DBConnection.getConnection())
		{
			villageResonse=login.verifyUser(villageResonse,chit_boy_id,ramdomstring,imei,accessType,conn);
			VillageResonse reqResponse=(VillageResonse) villageResonse;
			if(reqResponse.isSuccess())
			{
				String yearId=reqObj.has("yearId")?reqObj.getString("yearId"):"";
				if(yearId.trim().isEmpty())
				{
					reqResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode");
					reqResponse.setSe(error);
				}else
				{
					// check current date
					reqResponse=harvestingDao.sectionByVillageList(yearId,chit_boy_id,reqResponse,conn);
				}
				return reqResponse;
			
			}
		}catch (Exception e) {
				villageResonse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				villageResonse.setSe(error);
				e.printStackTrace();
			}
		return (VillageResonse)villageResonse;
	}
	@Override
	public VillageResonse villBySection(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse villageResonse) {
		try (Connection conn = DBConnection.getConnection()) {
			villageResonse = login.verifyUser(villageResonse, chit_boy_id, ramdomstring, imei, accessType, conn);
			VillageResonse reqResponse = (VillageResonse) villageResonse;
			if (reqResponse.isSuccess()) {
				String sectionId = reqObj.has("sectionId") ? reqObj.getString("sectionId") : "";
				String attach = reqObj.has("attach") ? reqObj.getString("attach") : "2";
				if (sectionId.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter sectionId");
					reqResponse.setSe(error);
				} else {
					// check current date
					reqResponse = harvestingDao.villBySection(sectionId, attach, chit_boy_id, reqResponse, conn);
				}
				return reqResponse;

			}
		} catch (Exception e) {
			villageResonse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			villageResonse.setSe(error);
			e.printStackTrace();
		}
		return (VillageResonse)villageResonse;
	}
	@Override
	public HarvReportReponse harvReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse harvResonse) {
		try (Connection conn = DBConnection.getConnection()) {
			harvResonse = login.verifyUser(harvResonse, chit_boy_id, ramdomstring, imei, accessType, conn);
			HarvReportReponse reqResponse = (HarvReportReponse) harvResonse;
			if (reqResponse.isSuccess()) {
				String date = reqObj.has("date") ? reqObj.getString("date") : "";
				String villageId = reqObj.has("villageId") ? reqObj.getString("villageId") : "";
				String vyearCode = reqObj.has("vyearCode") ? reqObj.getString("vyearCode") : "";
				if (date.trim().isEmpty() || villageId.trim().isEmpty() || vyearCode.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter date or villageId or vyearCode");
					reqResponse.setSe(error);
				} else {
					// check current date
					reqResponse = harvestingDao.dailyCaneRemain(date, villageId, vyearCode, reqResponse, conn);
					reqResponse = harvestingDao.remainingCaneInfo(villageId, vyearCode, reqResponse, conn);
				}
				return reqResponse;

			}
		} catch (Exception e) {
			harvResonse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			harvResonse.setSe(error);
			e.printStackTrace();
		}
		return (HarvReportReponse)harvResonse;
	}
	@Override
	public FarmerTonnageResponse farmerTonnageReport(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType, MainResponse tonnageResponse) {
		try (Connection conn = DBConnection.getConnection()) {
			tonnageResponse = login.verifyUser(tonnageResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			FarmerTonnageResponse reqResponse = (FarmerTonnageResponse) tonnageResponse;
			if (reqResponse.isSuccess()) {
				String nfarmercode = reqObj.has("nfarmercode") ? reqObj.getString("nfarmercode") : "";
				String vyearCode = reqObj.has("vyearCode") ? reqObj.getString("vyearCode") : "";
				if (nfarmercode.trim().isEmpty() || vyearCode.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter nfarmercode or vyearCode");
					reqResponse.setSe(error);
				} else {
					// check current date
					reqResponse = harvestingDao.farmerTonnageReport(nfarmercode, vyearCode,chit_boy_id, reqResponse, conn);
				}
				return reqResponse;

			}
		} catch (Exception e) {
			tonnageResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			tonnageResponse.setSe(error);
			e.printStackTrace();
		}
		return (FarmerTonnageResponse)tonnageResponse;
	}
	@Override
	public TableResponse farmerTonnageDetailsReport(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType, MainResponse tableResponse) {
		try (Connection conn = DBConnection.getConnection()) {
			tableResponse = login.verifyUser(tableResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			TableResponse reqResponse = (TableResponse) tableResponse;
			if (reqResponse.isSuccess()) {
				String nfarmercode = reqObj.has("farmercode") ? reqObj.getString("farmercode") : "";
				String vyearCode = reqObj.has("yearId") ? reqObj.getString("yearId") : "";
				if (nfarmercode.trim().isEmpty() || vyearCode.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter nfarmercode or vyearCode");
					reqResponse.setSe(error);
				} else {
					// check current date
					reqResponse = harvestingDao.farmerTonnageDetailsReport(nfarmercode, vyearCode,chit_boy_id, reqResponse, conn);
				}
				return reqResponse;

			}
		} catch (Exception e) {
			tableResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			tableResponse.setSe(error);
			e.printStackTrace();
		}
		return (TableResponse)tableResponse;
	}
	@Override
	public NameListResponse farmerListByName(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse nameListResponse) {
		try (Connection conn = DBConnection.getConnection()) {
			nameListResponse = login.verifyUser(nameListResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			NameListResponse reqResponse = (NameListResponse) nameListResponse;
			if (reqResponse.isSuccess()) {
				String name = reqObj.has("name") ? reqObj.getString("name") : "";
				if (name.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter nfarmercode or vyearCode");
					reqResponse.setSe(error);
				} else {
					// check current date
					reqResponse = harvestingDao.farmerListByName(name, reqResponse, conn);
				}
				return reqResponse;

			}
		} catch (Exception e) {
			nameListResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			nameListResponse.setSe(error);
			e.printStackTrace();
		}
		return (NameListResponse)nameListResponse;
	}
	@Override
	public MainResponse updateAndPrint(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse regenrateSlip) {

		try(Connection conn=DBConnection.getConnection())
		{
			regenrateSlip=login.verifyUser(regenrateSlip,chit_boy_id,ramdomstring,imei,accessType,conn);
			RemainingSlipResponse sliplistResponse=(RemainingSlipResponse) regenrateSlip;
			if(sliplistResponse.isSuccess())
			{
				
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String nslipNo=reqObj.has("nslipNo")?reqObj.getString("nslipNo"):"";
				if(yearCode.trim().isEmpty() || nslipNo.trim().isEmpty())
				{
					sliplistResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode & nslipNo");
					sliplistResponse.setSe(error);
				}else
				{
					sliplistResponse=harvestingDao.updateAndPrint(yearCode,nslipNo,chit_boy_id,sliplistResponse,conn);
				}
				return sliplistResponse;
			}
		}catch (Exception e) {
			regenrateSlip.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				regenrateSlip.setSe(error);
				e.printStackTrace();
			}
		return (RemainingSlipResponse)regenrateSlip;
	}

}
