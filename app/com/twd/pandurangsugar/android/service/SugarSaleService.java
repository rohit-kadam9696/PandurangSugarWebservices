package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.DataListResonse;
import com.twd.pandurangsugar.android.bean.FarmerSugarResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SugarInwardResponse;
import com.twd.pandurangsugar.android.bean.SugarSaleSavePrintResponse;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.dao.SugarSaleDao;
import com.twd.pandurangsugar.android.serviceInterface.SugarSaleServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class SugarSaleService implements SugarSaleServiceInterface {

	LoginDao login=new LoginDao();
	SugarSaleDao sugarSaleDao=new SugarSaleDao();
	
	@Override
	public FarmerSugarResponse farmerSugarInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarInfo) {
		try(Connection conn=DBConnection.getConnection())
		{
			sugarInfo=login.verifyUser(sugarInfo,chit_boy_id,ramdomstring,imei,accessType,conn);
			FarmerSugarResponse sugarInfoResponse=(FarmerSugarResponse) sugarInfo;
			if(sugarInfo.isSuccess())
			{
				String nentityUniId=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				if(nentityUniId.trim().isEmpty())
				{
					sugarInfoResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					sugarInfoResponse.setSe(error);
				}else
				{
					sugarInfoResponse=sugarSaleDao.farmerSugarInfo(nentityUniId,sugarInfoResponse,conn);
				}
				return sugarInfoResponse;
			}
		}catch (Exception e) {
				sugarInfo.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				sugarInfo.setSe(error);
				e.printStackTrace();
			}
		return (FarmerSugarResponse)sugarInfo;
	}

	@Override
	public SugarSaleSavePrintResponse saveSugarSale(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType, MainResponse saveSugar) {
		try(Connection conn=DBConnection.getConnection())
		{
			saveSugar=login.verifyUser(saveSugar,chit_boy_id,ramdomstring,imei,accessType,conn);
			SugarSaleSavePrintResponse sugarInfoResponse=(SugarSaleSavePrintResponse) saveSugar;
			if(saveSugar.isSuccess())
			{
				String logitude=reqObj.has("logitude")?reqObj.getString("logitude"):"";
				String latitude=reqObj.has("latitude")?reqObj.getString("latitude"):"";
				String sugarSeason=reqObj.has("sugarSeason")?reqObj.getString("sugarSeason"):"";
				String eventId=reqObj.has("eventId")?reqObj.getString("eventId"):"";
				String nentityUniId=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				String entryType=reqObj.has("entryType")?reqObj.getString("entryType"):"";
				String photopath=reqObj.has("photopath")?reqObj.getString("photopath"):"";
				if(logitude.trim().isEmpty() || latitude.trim().isEmpty() || sugarSeason.trim().isEmpty()
						 || eventId.trim().isEmpty() || nentityUniId.trim().isEmpty() || entryType.trim().isEmpty() || photopath.trim().isEmpty())
				{
					sugarInfoResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					sugarInfoResponse.setSe(error);
				}else
				{
					sugarInfoResponse=sugarSaleDao.saveSugarSale(logitude,latitude,sugarSeason,eventId,nentityUniId, photopath,chit_boy_id,entryType,sugarInfoResponse,conn);
					if(sugarInfoResponse.isActionComplete())
					{
						
						sugarInfoResponse=sugarSaleDao.printSugarSale(nentityUniId,sugarInfoResponse,conn);
					}
				}
				return sugarInfoResponse;
			}
		}catch (Exception e) {
				saveSugar.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				saveSugar.setSe(error);
				e.printStackTrace();
			}
		return (SugarSaleSavePrintResponse)saveSugar;
	}

	@Override
	public MainResponse printSugarSale(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse printSugar) {

		try(Connection conn=DBConnection.getConnection())
		{
			printSugar=login.verifyUser(printSugar,chit_boy_id,ramdomstring,imei,accessType,conn);
			SugarSaleSavePrintResponse sugarInfoResponse=(SugarSaleSavePrintResponse) printSugar;
			if(printSugar.isSuccess())
			{
				String nentityUniId=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				if(nentityUniId.trim().isEmpty())
				{
					sugarInfoResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					sugarInfoResponse.setSe(error);
				}else
				{
					sugarInfoResponse=sugarSaleDao.printSugarSale(nentityUniId,sugarInfoResponse,conn);
				}
				return sugarInfoResponse;
			}
		}catch (Exception e) {
			printSugar.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				printSugar.setSe(error);
				e.printStackTrace();
			}
		return (SugarSaleSavePrintResponse)printSugar;
	}

	@Override
	public DataListResonse loaddoList(JSONObject json, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse loaddoList) {
		try(Connection conn=DBConnection.getConnection())
		{
			loaddoList=login.verifyUser(loaddoList,chit_boy_id,ramdomstring,imei,accessType,conn);
			DataListResonse loaddoResponse=(DataListResonse) loaddoList;
			if(loaddoList.isSuccess())
			{
				String dinvoiceDate=json.has("dinvoice_date")?json.getString("dinvoice_date"):"";
				if (dinvoiceDate == null)
				{
					loaddoResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					loaddoResponse.setSe(error);
				} else
				{
					loaddoResponse=sugarSaleDao.loaddoList(dinvoiceDate,chit_boy_id,loaddoResponse,conn);
				}
				return loaddoResponse;
			}
		}catch (Exception e) {
				loaddoList.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				loaddoList.setSe(error);
				e.printStackTrace();
			}
		return (DataListResonse)loaddoList;
	}
	@Override
	public SugarInwardResponse findSugarInward(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarInward) {
		try(Connection conn=DBConnection.getConnection())
		{
			sugarInward=login.verifyUser(sugarInward,chit_boy_id,ramdomstring,imei,accessType,conn);
			SugarInwardResponse sugarInfoResponse=(SugarInwardResponse) sugarInward;
			if(sugarInward.isSuccess())
			{
				String invoiceDate=reqObj.has("dinvoice_date")?reqObj.getString("dinvoice_date"):"";
				String avakDate=reqObj.has("avakDate")?reqObj.getString("avakDate"):"";
				String ndoId=reqObj.has("ndo_id")?reqObj.getString("ndo_id"):"";
				if(invoiceDate.trim().isEmpty() || ndoId.trim().isEmpty())
				{
					sugarInfoResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					sugarInfoResponse.setSe(error);
				}else
				{
					sugarInfoResponse=sugarSaleDao.findInward(ndoId,invoiceDate,avakDate,sugarInfoResponse,conn);
				}
				return sugarInfoResponse;
			}
		}catch (Exception e) {
			sugarInward.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				sugarInward.setSe(error);
				e.printStackTrace();
			}
		return (SugarInwardResponse)sugarInward;
	}

	@Override
	public ActionResponse saveInward(String json, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse saveInward) {
		try(Connection conn=DBConnection.getConnection())
		{
			saveInward=login.verifyUser(saveInward,chit_boy_id,ramdomstring,imei,accessType,conn);
			ActionResponse sugarInfoResponse=(ActionResponse) saveInward;
			if(saveInward.isSuccess())
			{
				Gson gson = new GsonBuilder().create();
				SugarInwardResponse inward = gson.fromJson(json, SugarInwardResponse.class);
				if (inward.getNdoNo()==null || inward.getDawakDate()==null || inward.getDdoDate()==null)
				{
					sugarInfoResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					sugarInfoResponse.setSe(error);
				} else
				{
					sugarInfoResponse=sugarSaleDao.saveInward(inward,chit_boy_id,sugarInfoResponse,conn);
				}
				return sugarInfoResponse;
			}
		}catch (Exception e) {
				saveInward.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				saveInward.setSe(error);
				e.printStackTrace();
			}
		return (ActionResponse)saveInward;
	}

	@Override
	public TableResponse sugarSummaryReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarSummaryReport) {
		try (Connection conn = DBConnection.getConnection()) {
			sugarSummaryReport = login.verifyUser(sugarSummaryReport, chit_boy_id, ramdomstring, imei, accessType, conn);
			TableResponse reqResponse = (TableResponse) sugarSummaryReport;
			if (reqResponse.isSuccess()) {
				String rdate = reqObj.has("rdate") ? reqObj.getString("rdate") : "";
				String mnu_id = reqObj.has("mnu_id") ? reqObj.getString("mnu_id") : "60";
				if (rdate.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode");
					reqResponse.setSe(error);
				} else {
					// check current date
					if (mnu_id.equals("60")) {
						reqResponse = sugarSaleDao.sugarSummaryReport(rdate, chit_boy_id, reqResponse, conn);
					} else {
						reqResponse = sugarSaleDao.sugarSummaryReport2(rdate, chit_boy_id, reqResponse, conn);
					}
				}
				return reqResponse;

			}
		} catch (Exception e) {
			sugarSummaryReport.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			sugarSummaryReport.setSe(error);
			e.printStackTrace();
		}
		return (TableResponse) sugarSummaryReport;
	}

	@Override
	public TableResponse sugarOutwardReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse sugarOutwardReport) {
		try(Connection conn=DBConnection.getConnection())
		{
			sugarOutwardReport=login.verifyUser(sugarOutwardReport,chit_boy_id,ramdomstring,imei,accessType,conn);
			TableResponse reqResponse=(TableResponse) sugarOutwardReport;
			if(reqResponse.isSuccess())
			{
				String rdate=reqObj.has("rdate")?reqObj.getString("rdate"):"";
				if(rdate.trim().isEmpty())
				{
					reqResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode");
					reqResponse.setSe(error);
				}else
				{
					// check current date
					reqResponse=sugarSaleDao.sugarOutwardReport(rdate,chit_boy_id,reqResponse,conn);
				}
				return reqResponse;
			
			}
		}catch (Exception e) {
				sugarOutwardReport.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				sugarOutwardReport.setSe(error);
				e.printStackTrace();
			}
		return (TableResponse)sugarOutwardReport;
	}

}
