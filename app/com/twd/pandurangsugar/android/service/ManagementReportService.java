package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.AgriReportReponse;
import com.twd.pandurangsugar.android.bean.CrushingPlantHarvVillResponse;
import com.twd.pandurangsugar.android.bean.CrushingReportReponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.dao.ManagementDAO;
import com.twd.pandurangsugar.android.serviceInterface.ManagementServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class ManagementReportService implements ManagementServiceInterface {
	LoginDao login=new LoginDao();
	ManagementDAO managementDao=new ManagementDAO();
	
	@Override
	public MainResponse crushingReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse crushingReport) {
		try (Connection conn = DBConnection.getConnection()) {
			crushingReport = login.verifyUser(crushingReport, chit_boy_id, ramdomstring, imei,accessType, conn);
			CrushingReportReponse crushingReportRes = (CrushingReportReponse) crushingReport;
			if (crushingReport.isSuccess()) {

				String date = reqObj.has("date") ? reqObj.getString("date") : "";
				if (date.trim().isEmpty()) {
					crushingReportRes.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter Date");
					crushingReportRes.setSe(error);
				} else {
					String result = managementDao.updateDate(date, conn);
					if(result.equals("1")) {
						try (Connection conn1 = DBConnection.getConnection()) {
							crushingReportRes = managementDao.generateCrushingReport(date, chit_boy_id, crushingReportRes, conn1);
							crushingReportRes = managementDao.generateHangamTonnage(date, chit_boy_id, crushingReportRes, conn1);
							crushingReportRes = managementDao.generateVarietyTonnage(date, chit_boy_id, crushingReportRes, conn1);
							crushingReportRes = managementDao.generateSectionTonnageAndList(date, chit_boy_id, crushingReportRes, conn1);
							crushingReportRes = managementDao.generateCropTypeTonnage(date, chit_boy_id, crushingReportRes, conn1);
							crushingReportRes = managementDao.generateRemainCane(date, chit_boy_id, crushingReportRes, conn1);
							//crushingReportRes = (CrushingReportReponse) managementDao.generatePlantHarvVillTonnage(date, chit_boy_id, 1L, (CrushingPlantHarvVillResponse) crushingReportRes, conn1);
							crushingReportRes = (CrushingReportReponse) managementDao.generateVillTonnage(date, chit_boy_id, 1L, (CrushingPlantHarvVillResponse) crushingReportRes, conn1);
						}
					} else {
						crushingReportRes.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(result);
						crushingReportRes.setSe(error);
					}
					
				}
				return crushingReportRes;
			}
		} catch (Exception e) {
			crushingReport.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			crushingReport.setSe(error);
			e.printStackTrace();
		}
		return (CrushingReportReponse) crushingReport;
	}

	@Override
	public MainResponse plantHarvVillReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse plantHarvVillReport) {
		try (Connection conn = DBConnection.getConnection()) {
			plantHarvVillReport = login.verifyUser(plantHarvVillReport, chit_boy_id, ramdomstring, imei,accessType, conn);
			CrushingPlantHarvVillResponse plantHarvVillReportRes = (CrushingPlantHarvVillResponse) plantHarvVillReport;
			if (plantHarvVillReport.isSuccess()) {

				String date = reqObj.has("date") ? reqObj.getString("date") : "";
				Long sectionCode = reqObj.has("sectionCode") ? reqObj.getLong("sectionCode") : null;
				if (date.trim().isEmpty() || sectionCode==null) {
					plantHarvVillReportRes.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter Date And Section");
					plantHarvVillReportRes.setSe(error);
				} else {
					String result = managementDao.updateDate(date, conn);
					if(result.equals("1")) {
						try (Connection conn1 = DBConnection.getConnection()) {
							//plantHarvVillReportRes = managementDao.generatePlantHarvVillTonnage(date, chit_boy_id, sectionCode, (CrushingPlantHarvVillResponse) plantHarvVillReportRes, conn1);
							plantHarvVillReportRes = managementDao.generateVillTonnage(date, chit_boy_id, sectionCode, (CrushingPlantHarvVillResponse) plantHarvVillReportRes, conn1);
						}
					} else {
						plantHarvVillReportRes.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(result);
						plantHarvVillReportRes.setSe(error);
					}
				}
				return plantHarvVillReportRes;
			}
		} catch (Exception e) {
			plantHarvVillReport.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			plantHarvVillReport.setSe(error);
			e.printStackTrace();
		}
		return (CrushingPlantHarvVillResponse) plantHarvVillReport;
	}

	@Override
	public MainResponse agriReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse agriReport) {

		try (Connection conn = DBConnection.getConnection()) {
			agriReport = login.verifyUser(agriReport, chit_boy_id, ramdomstring, imei,accessType, conn);
			AgriReportReponse agriReportRes = (AgriReportReponse) agriReport;
			if (agriReport.isSuccess()) {

				String date = reqObj.has("date") ? reqObj.getString("date") : "";
				String vyearCode = reqObj.has("vyearCode") ? reqObj.getString("vyearCode") : "";
				if (date.trim().isEmpty() || vyearCode.trim().isEmpty() ) {
					agriReportRes.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter Date And Year Code");
					agriReportRes.setSe(error);
				} else {
					String result = managementDao.updateDateAndSeason(date, vyearCode, conn);
					if(result.equals("1")) {
						boolean webbridgeact = agriReport.getHarvestingYearCode().contains(vyearCode);
						agriReportRes = managementDao.generateNondSummary(date, chit_boy_id, agriReportRes, conn);
						agriReportRes = managementDao.generateNondAndExpTonnage(date, chit_boy_id, agriReportRes, conn);
						agriReportRes = managementDao.generateSectionHangamNond(date, chit_boy_id, webbridgeact, agriReportRes, conn);
						agriReportRes = managementDao.generateSectionVarietyNond(date, chit_boy_id, webbridgeact, agriReportRes, conn);
						agriReportRes = managementDao.generateHangamVarietyNond(date, chit_boy_id, webbridgeact, agriReportRes, conn);
					} else {
						agriReportRes.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(result);
						agriReportRes.setSe(error);
					}
				}
				return agriReportRes;
			}
		} catch (Exception e) {
			agriReport.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			agriReport.setSe(error);
			e.printStackTrace();
		}
		return (CrushingPlantHarvVillResponse) agriReport;
	
	}
	
}
