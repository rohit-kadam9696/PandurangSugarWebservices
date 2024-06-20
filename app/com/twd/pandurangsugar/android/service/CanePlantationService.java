package com.twd.pandurangsugar.android.service;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twd.pandurangsugar.android.bean.CanePlantationResponse;
import com.twd.pandurangsugar.android.bean.EmpVillResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NondReportHangamReponse;
import com.twd.pandurangsugar.android.bean.NondReportReponse;
import com.twd.pandurangsugar.android.bean.PlantationBean;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.dao.CanePlantationDao;
import com.twd.pandurangsugar.android.dao.CanePlantationNDao;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.serviceInterface.CanePlantationServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CanePlantationService implements CanePlantationServiceInterface {

	LoginDao login=new LoginDao();
	@Override
	public CanePlantationResponse savePlantation(JSONArray reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType,MainResponse mainresponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			CanePlantationResponse canePlantResponse=(CanePlantationResponse) mainresponse;
			if(mainresponse.isSuccess())
			{
				List<PlantationBean> reqList=new ArrayList<PlantationBean>();
				Type listType = new TypeToken<List<PlantationBean>>() {}.getType();
				reqList = new Gson().fromJson(reqObj.toString(), listType);
				canePlantResponse=CanePlantationDao.savePlantation(reqList,chit_boy_id,mainresponse,canePlantResponse,conn);
				if(!canePlantResponse.isSuccess())
				{
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.fromSavedFailed);
					mainresponse.setSe(error);
				}
				return canePlantResponse;
			}
		}catch (Exception e) {
				mainresponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				mainresponse.setSe(error);
				e.printStackTrace();
			}
		return (CanePlantationResponse)mainresponse;
	}
	@Override
	public CanePlantationResponse updatePlantation(String json, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse mainresponse) {

		try(Connection conn=DBConnection.getConnection())
		{
			mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			CanePlantationResponse canePlantResponse=(CanePlantationResponse) mainresponse;
			if(mainresponse.isSuccess())
			{
				Gson gson = new Gson();
				PlantationBean updatePlantationBean= gson.fromJson(json, PlantationBean.class);
				
				canePlantResponse=CanePlantationDao.updatePlantation(updatePlantationBean,chit_boy_id,mainresponse,canePlantResponse,conn);
				if(!canePlantResponse.isSuccess())
				{
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.fromSavedFailed);
					mainresponse.setSe(error);
				}
				return canePlantResponse;
			}
		}catch (Exception e) {
				mainresponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Connection Issue "+e.getMessage());
				mainresponse.setSe(error);
				e.printStackTrace();
			}
		return (CanePlantationResponse)mainresponse;
	}
	@Override
	public NondReportReponse nondReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse nondReport) {
		try (Connection conn = DBConnection.getConnection()) {
			CanePlantationNDao canePlantationDao = new CanePlantationNDao();
			nondReport = login.verifyUser(nondReport, chit_boy_id, ramdomstring, imei, accessType, conn);
			NondReportReponse reqResponse = (NondReportReponse) nondReport;
			if (reqResponse.isSuccess()) {
				String hangamId = reqObj.has("hangamId") ? reqObj.getString("hangamId") : "";
				String villageId = reqObj.has("villageId") ? reqObj.getString("villageId") : "";
				String vyearCode = reqObj.has("vyearCode") ? reqObj.getString("vyearCode") : "";
				if (hangamId.trim().isEmpty() || villageId.trim().isEmpty() || vyearCode.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter hangamId or villageId or vyearCode");
					reqResponse.setSe(error);
				} else {
					// check current date
					//reqResponse = harvestingDao.villBySection(sectionId, chit_boy_id, reqResponse, conn);
					reqResponse = canePlantationDao.plantSummary(villageId, vyearCode, reqResponse, conn);
					reqResponse = canePlantationDao.hangamSummary(villageId, vyearCode, reqResponse, conn);
					reqResponse = canePlantationDao.varietySummary(villageId, vyearCode, reqResponse, conn);
					reqResponse = (NondReportReponse) canePlantationDao.monthvarietySummary(villageId, vyearCode,hangamId, reqResponse, conn);
				}
				return reqResponse;
			}
		} catch (Exception e) {
			nondReport.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			nondReport.setSe(error);
			e.printStackTrace();
		}
		return (NondReportReponse)nondReport;
	}
	@Override
	public NondReportHangamReponse nondHangamReport(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, String accessType, MainResponse nondHangamReport) {
		try (Connection conn = DBConnection.getConnection()) {
			CanePlantationNDao canePlantationDao = new CanePlantationNDao();
			nondHangamReport = login.verifyUser(nondHangamReport, chit_boy_id, ramdomstring, imei, accessType, conn);
			NondReportHangamReponse reqResponse = (NondReportHangamReponse) nondHangamReport;
			if (reqResponse.isSuccess()) {
				String hangamId = reqObj.has("hangamId") ? reqObj.getString("hangamId") : "";
				String villageId = reqObj.has("villageId") ? reqObj.getString("villageId") : "";
				String vyearCode = reqObj.has("vyearCode") ? reqObj.getString("vyearCode") : "";
				if (hangamId.trim().isEmpty() || villageId.trim().isEmpty() || vyearCode.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter hangamId or villageId or vyearCode");
					reqResponse.setSe(error);
				} else {
					reqResponse = canePlantationDao.monthvarietySummary(villageId, vyearCode,hangamId, reqResponse, conn);
					
				}
				return reqResponse;

			}
		} catch (Exception e) {
			nondHangamReport.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			nondHangamReport.setSe(error);
			e.printStackTrace();
		}
		return (NondReportReponse)nondHangamReport;
	}
	@Override
	public EmpVillResponse empData(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse empVillResponse) {
		try (Connection conn = DBConnection.getConnection()) {
			CanePlantationNDao canePlantationDao = new CanePlantationNDao();
			empVillResponse = login.verifyUser(empVillResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			EmpVillResponse reqResponse = (EmpVillResponse) empVillResponse;
			if (reqResponse.isSuccess()) {
				String employeecode = reqObj.has("employeecode") ? reqObj.getString("employeecode") : "";
			
				if (employeecode.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter employee code");
					reqResponse.setSe(error);
				} else {
					reqResponse = canePlantationDao.empData(employeecode, reqResponse, conn);
					
				}
				return reqResponse;

			}
		} catch (Exception e) {
			empVillResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			empVillResponse.setSe(error);
			e.printStackTrace();
		}
		return (EmpVillResponse) empVillResponse;
	}
	@Override
	public MainResponse removeEmpVill(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse empVillResponse) {
		try (Connection conn = DBConnection.getConnection()) {
			CanePlantationNDao canePlantationDao = new CanePlantationNDao();
			empVillResponse = login.verifyUser(empVillResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			EmpVillResponse reqResponse = (EmpVillResponse) empVillResponse;
			if (reqResponse.isSuccess()) {
				String empcode = reqObj.has("empcode") ? reqObj.getString("empcode") : "";
				String rm1 = reqObj.has("rm1") ? reqObj.getString("rm1") : "";
				String rm2 = reqObj.has("rm2") ? reqObj.getString("rm2") : "";
			
				if (empcode.trim().isEmpty() || (rm1.equals("") && rm2.equals(""))) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					reqResponse.setSe(error);
				} else {
					reqResponse = canePlantationDao.removeEmpVill(empcode, rm1, rm2, reqResponse, conn);
					if(reqResponse.isActionComplete()) 
						reqResponse = canePlantationDao.empData(empcode, reqResponse, conn);
				}
				return reqResponse;
			}
		} catch (Exception e) {
			empVillResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			empVillResponse.setSe(error);
			e.printStackTrace();
		}
		return (EmpVillResponse) empVillResponse;
	}
	@Override
	public MainResponse addEmpVill(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse empVillResponse) {
		try (Connection conn = DBConnection.getConnection()) {
			CanePlantationNDao canePlantationDao = new CanePlantationNDao();
			empVillResponse = login.verifyUser(empVillResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			EmpVillResponse reqResponse = (EmpVillResponse) empVillResponse;
			if (reqResponse.isSuccess()) {
				String empcode = reqObj.has("empcode") ? reqObj.getString("empcode") : "";
				String ad1 = reqObj.has("ad1") ? reqObj.getString("ad1") : "";
				String ad2 = reqObj.has("ad2") ? reqObj.getString("ad2") : "";
			
				if (empcode.trim().isEmpty() || (ad1.equals("") && ad2.equals(""))) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					reqResponse.setSe(error);
				} else {
					reqResponse = canePlantationDao.addEmpVill(empcode, ad1, ad2, reqResponse, conn);
					if(reqResponse.isActionComplete()) 
						reqResponse = canePlantationDao.empData(empcode, reqResponse, conn);
				}
				return reqResponse;

			}
		} catch (Exception e) {
			empVillResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			empVillResponse.setSe(error);
			e.printStackTrace();
		}
		return (EmpVillResponse) empVillResponse;
	}

}
