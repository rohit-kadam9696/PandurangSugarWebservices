package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.AgriReportReponse;
import com.twd.pandurangsugar.android.bean.CrushingPlantHarvVillResponse;
import com.twd.pandurangsugar.android.bean.CrushingReportReponse;
import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableReportBean;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class ManagementDAO {
	
	public CrushingReportReponse generateCrushingReport(String date, String chit_boy_id,
			CrushingReportReponse crushingReportRes, Connection conn) {
		try {
			Calendar cal = Calendar.getInstance();
			int curHour = cal.get(Calendar.HOUR_OF_DAY);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			cal.add(Calendar.HOUR_OF_DAY, -4);
			boolean today = sdf.format(cal.getTime()).equals(date);
			
			TableReportBean dailyCrushing = new TableReportBean();
			int noofheadDailyCrush =2;
			dailyCrushing.setNoofHeads(noofheadDailyCrush);
			dailyCrushing.setFooter(true);
			dailyCrushing.setMarathi(true);
			
			HashMap<String, Integer> rowColDailyCrushing = new HashMap<>();
			rowColDailyCrushing.put("0-1", 2);
			rowColDailyCrushing.put("0-2", 2);
			rowColDailyCrushing.put("0-3", 2);
			rowColDailyCrushing.put("0-4", 2);
			dailyCrushing.setRowColSpan(rowColDailyCrushing);
			
			HashMap<String, String> dailyfloatings=new HashMap<>();
			
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> dailyCrushingTableData = new ArrayList<>();
			ArrayList<String> dailyCrushingRow = new ArrayList<>();
			dailyCrushingRow.add(ConstantVeriables.shift+ ConstantVeriables.rightArrow);
			if(!today || (curHour<12 && curHour>4)) {
				dailyCrushingRow.add(ConstantVeriables.s4to12);
				dailyCrushingRow.add(ConstantVeriables.s12to8);
				dailyCrushingRow.add(ConstantVeriables.s8to4);
			} else if (curHour<20 && curHour>4){
				dailyCrushingRow.add(ConstantVeriables.s12to8);
				dailyCrushingRow.add(ConstantVeriables.s4to12);
				dailyCrushingRow.add(ConstantVeriables.s8to4);
			} else {
				dailyCrushingRow.add(ConstantVeriables.s8to4);
				dailyCrushingRow.add(ConstantVeriables.s12to8);
				dailyCrushingRow.add(ConstantVeriables.s4to12);
			}
			dailyCrushingRow.add(ConstantVeriables.total);
			dailyCrushingTableData.add(dailyCrushingRow);
			
			dailyCrushingRow = new ArrayList<>();
			dailyCrushingRow.add(ConstantVeriables.vehicalType);
			dailyCrushingRow.add(ConstantVeriables.today);
			dailyCrushingRow.add(ConstantVeriables.yesterday);
			dailyCrushingRow.add(ConstantVeriables.today);
			dailyCrushingRow.add(ConstantVeriables.yesterday);
			dailyCrushingRow.add(ConstantVeriables.today);
			dailyCrushingRow.add(ConstantVeriables.yesterday);
			dailyCrushingRow.add(ConstantVeriables.today);
			dailyCrushingRow.add(ConstantVeriables.yesterday);
			dailyCrushingTableData.add(dailyCrushingRow);
			
			int hrTotalToday = 7;
			int hrTotalTodate = 8;
			
			boldIndicator.put("*-"+hrTotalToday, true);
			boldIndicator.put("*-"+hrTotalTodate, true);

			boldIndicator.put("0-4", true);
			/*boldIndicator.put("1-"+hrTotalToday, false);
			boldIndicator.put("1-"+hrTotalTodate, false);*/
			
			JSONObject posJob = new JSONObject();
			try(PreparedStatement pst=conn.prepareStatement("select t.nvehicle_type_id, To_NCHAR(t.vvehicle_type_name_local) as vvehicle_type_name_local from GM_M_VEHICLE_TYPE_MASTER_MAIN t order by t.nvehicle_type_id")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						posJob.put(rs.getString("nvehicle_type_id"), dailyCrushingTableData.size());
						dailyCrushingRow = new ArrayList<>();
						dailyCrushingRow.add(DemoConvert2.ism_to_uni(rs.getString("vvehicle_type_name_local")));
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyCrushingRow.add(ConstantVeriables.defaultTon);
						dailyfloatings.put("*-1", " ");
						dailyfloatings.put("*-2", " ");
						dailyfloatings.put("*-3", " ");
						dailyfloatings.put("*-4", " ");
						dailyfloatings.put("*-5", " ");
						dailyfloatings.put("*-6", " ");
						dailyfloatings.put("*-7", " ");
						dailyfloatings.put("*-8", " ");
						dailyCrushingTableData.add(dailyCrushingRow);
					}
				}
			}
			int totalPosDailyCrush = dailyCrushingTableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			posJob.put("total", totalPosDailyCrush);
			
			dailyCrushingRow = new ArrayList<>();
			dailyCrushingRow.add(ConstantVeriables.total);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingRow.add(ConstantVeriables.defaultTon);
			dailyCrushingTableData.add(dailyCrushingRow);
			
			dailyCrushing.setBoldIndicator(boldIndicator);
			
			boolean isFirst = true;
			try(PreparedStatement pst=conn.prepareStatement("select t.dslip_date - t.dfdate as noofday, t.dslip_date, t.dfdate,t.nshift_id,t.nvehicle_type_id,t.nnet_weight,t.nnet_weight_yesterday from MD_V_SHIFT_DAY_RPT_1 t")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						if(isFirst) {
							crushingReportRes.setNhangamDay(rs.getString("noofday"));
						}
						isFirst=false;
						int nshift_id = rs.getInt("nshift_id");
						if(!today || (curHour<12 && curHour>4)) {
						} else if (curHour<20 && curHour>4){
							switch (nshift_id) {
							case 1:
								nshift_id=2;
								break;
							case 2:
								nshift_id=1;
								break;
							default:
								break;
							}
						} else {
							switch (nshift_id) {
							case 1:
								nshift_id=3;
								break;
							case 3:
								nshift_id=1;
								break;
							default:
								break;
							}
						}
						String nvehicle_type_id = rs.getString("nvehicle_type_id");
						double nnet_weight = rs.getDouble("nnet_weight");
						double nnet_weight_yesterday = rs.getDouble("nnet_weight_yesterday");
						
						int dailyCrushingIndex = posJob.getInt(nvehicle_type_id);
						int totalCrushingIndex = posJob.getInt("total");
						
						dailyCrushingRow = dailyCrushingTableData.get(dailyCrushingIndex);
						ArrayList<String> totalCrushingRow  = dailyCrushingTableData.get(totalCrushingIndex);
						
						dailyCrushingRow.set(nshift_id*2-1, Constant.decimalFormat(nnet_weight, "000"));
						dailyCrushingRow.set(nshift_id*2, Constant.decimalFormat(nnet_weight_yesterday, "000"));
	
						dailyCrushingRow.set(hrTotalToday, Constant.decimalFormat(Double.parseDouble(dailyCrushingRow.get(hrTotalToday)) + nnet_weight, "000"));
						dailyCrushingRow.set(hrTotalTodate, Constant.decimalFormat(Double.parseDouble(dailyCrushingRow.get(hrTotalTodate)) + nnet_weight_yesterday, "000"));
	
						totalCrushingRow.set(nshift_id*2-1, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(nshift_id*2-1)) + nnet_weight, "000"));
						totalCrushingRow.set(nshift_id*2, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(nshift_id*2)) + nnet_weight_yesterday, "000"));
						
						if (dailyfloatings.containsKey("*-" + (nshift_id * 2 - 1))) {
							if(!totalCrushingRow.get(nshift_id * 2 - 1).equals("0.000")) {
								dailyfloatings.remove("*-" + (nshift_id * 2 - 1));
							}
						}

						if (dailyfloatings.containsKey("*-" + (nshift_id * 2 ))) {
							if(!totalCrushingRow.get(nshift_id * 2 ).equals("0.000")) {
								dailyfloatings.remove("*-" + (nshift_id * 2));
							}
						}
						
						if (dailyfloatings.containsKey("*-" + hrTotalToday)) {
							if(!totalCrushingRow.get(hrTotalToday ).equals("0.000")) {
								dailyfloatings.remove("*-" + hrTotalToday);
							}
						}
	
						if (dailyfloatings.containsKey("*-" + hrTotalTodate)) {
							if(!totalCrushingRow.get(hrTotalTodate).equals("0.000")) {
								dailyfloatings.remove("*-" + (hrTotalTodate));
							}
						}
						
						totalCrushingRow.set(hrTotalToday, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(hrTotalToday)) + nnet_weight, "000"));
						totalCrushingRow.set(hrTotalTodate, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(hrTotalTodate)) + nnet_weight_yesterday, "000"));
								
						dailyCrushingTableData.set(dailyCrushingIndex, dailyCrushingRow);
						dailyCrushingTableData.set(totalCrushingIndex, totalCrushingRow);
					}
				}
			}
			
			dailyCrushing.setTableData(dailyCrushingTableData);
			
			dailyCrushing.setFloatings(dailyfloatings);
			crushingReportRes.setDailyCrushing(dailyCrushing);

			TableReportBean labReport = new TableReportBean();
			labReport.setNoofHeads(1);
			labReport.setFooter(false);
			labReport.setMarathi(true);
			
			HashMap<String, Integer> rowColLabReport = new HashMap<>();
			
			
			ArrayList<ArrayList<String>> labReportTableData = new ArrayList<>();
			ArrayList<String> labReportRow = new ArrayList<>();
			labReportRow.add(ConstantVeriables.srno);
			labReportRow.add(ConstantVeriables.details);
			labReportRow.add(ConstantVeriables.today);
			labReportRow.add(ConstantVeriables.todate);
			labReportTableData.add(labReportRow);
			
			
			
			try(PreparedStatement pst=conn.prepareStatement("select t.ddate, t.nnet_weight_daily, t.nnet_weight_todate, t.nsugar_daily, t.nsugar_todate, t.nrecovery_daily, t.nrecovery_todate, t.npol_prc_cane_daily, t.npol_prc_cane_todate, t.nlosses_daily, t.nlosses_todate, t.nwater_prc_cane_daily, t.nwater_prc_cane_todate, t.nwater_prc_fibre_daily, t.nwater_prc_fibre_todate, t.nbagasse_pol_daily, t.nbagasse_pol_todate, t.nmoisture_pol_daily, t.nmoisture_todate, t.nworking_hrs_daily, t.nworking_hrs_todate from MD_V_LAB_DAY_RPT_1 t")){
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						int srNo =1;
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.crushing);
						labReportRow.add(rs.getString("nnet_weight_daily"));
						labReportRow.add(rs.getString("nnet_weight_todate"));
						labReportTableData.add(labReportRow);
						
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.sugarBags);
						labReportRow.add(rs.getString("nsugar_daily"));
						labReportRow.add(rs.getString("nsugar_todate"));
						labReportTableData.add(labReportRow);
						
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.recoveryPer);
						labReportRow.add(rs.getString("nrecovery_daily"));
						labReportRow.add(rs.getString("nrecovery_todate"));
						labReportTableData.add(labReportRow);
						
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.polPer);
						labReportRow.add(rs.getString("npol_prc_cane_daily"));
						labReportRow.add(rs.getString("npol_prc_cane_todate"));
						labReportTableData.add(labReportRow);
						
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.losses);
						labReportRow.add(rs.getString("nlosses_daily"));
						labReportRow.add(rs.getString("nlosses_todate"));
						labReportTableData.add(labReportRow);
						
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.canewaterPer);
						labReportRow.add(rs.getString("nwater_prc_cane_daily"));
						labReportRow.add(rs.getString("nwater_prc_cane_todate"));
						labReportTableData.add(labReportRow);

						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.fibreWaterPer);
						labReportRow.add(rs.getString("nwater_prc_fibre_daily"));
						labReportRow.add(rs.getString("nwater_prc_fibre_todate"));
						labReportTableData.add(labReportRow);
						
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.bagassePol);
						labReportRow.add(rs.getString("nbagasse_pol_daily"));
						labReportRow.add(rs.getString("nbagasse_pol_todate"));
						labReportTableData.add(labReportRow);

						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.moisturePol);
						labReportRow.add(rs.getString("nmoisture_pol_daily"));
						labReportRow.add(rs.getString("nmoisture_todate"));
						labReportTableData.add(labReportRow);
						
						labReportRow = new ArrayList<>();
						labReportRow.add(String.valueOf(srNo++));
						labReportRow.add(ConstantVeriables.workingHrs);
						labReportRow.add(rs.getString("nworking_hrs_daily"));
						labReportRow.add(rs.getString("nworking_hrs_todate"));
						labReportTableData.add(labReportRow);
			
						HashMap<String, Boolean> boldIndicatorLab = new HashMap<>();
						boldIndicatorLab.put("1-*", true);
						boldIndicatorLab.put("2-*", true);
						boldIndicatorLab.put("3-*", true);
						labReport.setBoldIndicator(boldIndicatorLab);
					} else {
						labReportRow = new ArrayList<>();
						labReportRow.add(ConstantVeriables.reportnotfilled);
						labReportTableData.add(labReportRow);
						rowColLabReport.put("1-0", 4);
					}
				}
			}
			
			labReport.setRowColSpan(rowColLabReport);
			labReport.setTableData(labReportTableData);
			crushingReportRes.setDailyLabSummary(labReport);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;

	}

	public CrushingReportReponse generateHangamTonnage(String date, String chit_boy_id,
			CrushingReportReponse crushingReportRes, Connection conn) {
		try {
			TableReportBean hangamTonnage = new TableReportBean();
			int noofheadDailyCrush =1;
			hangamTonnage.setNoofHeads(noofheadDailyCrush);
			hangamTonnage.setFooter(true);
			hangamTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColhangamTonnage = new HashMap<>();
			
			HashMap<String, String> hanagmfloatings=new HashMap<>();
			hanagmfloatings.put("*-2", ".000");
			hanagmfloatings.put("*-3", ".000");
			hangamTonnage.setFloatings(hanagmfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> hangamTonnageTableData = new ArrayList<>();
			ArrayList<String> hangamTonnageRow = new ArrayList<>();
			hangamTonnageRow.add(ConstantVeriables.srno);
			hangamTonnageRow.add(ConstantVeriables.hangam);
			hangamTonnageRow.add(ConstantVeriables.today);
			hangamTonnageRow.add(ConstantVeriables.todate);
			hangamTonnageTableData.add(hangamTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double todayTot = 0, todateTot = 0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nhangam_id, TO_NCHAR(t.vhangam_name) as vhangam_name, t.nnet_weight_today, t.nnet_weight_todate from Md_v_Hangam_Tonnage_1 t ORDER BY t.nhangam_id")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						hangamTonnageRow = new ArrayList<>();
						hangamTonnageRow.add(String.valueOf(srno++));
						hangamTonnageRow.add(DemoConvert2.ism_to_uni(rs.getString("vhangam_name")));
						hangamTonnageRow.add(rs.getString("nnet_weight_today"));
						hangamTonnageRow.add(rs.getString("nnet_weight_todate"));
						hangamTonnageTableData.add(hangamTonnageRow);
						todayTot+=rs.getDouble("nnet_weight_today");
						todateTot+=rs.getDouble("nnet_weight_todate");
					}
				}
			}
			int totalPosDailyCrush = hangamTonnageTableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.000");
			hangamTonnageRow = new ArrayList<>();
			hangamTonnageRow.add(ConstantVeriables.total);
			hangamTonnageRow.add(df.format(todayTot));
			hangamTonnageRow.add(df.format(todateTot));
			hangamTonnageTableData.add(hangamTonnageRow);
			
			rowColhangamTonnage.put(totalPosDailyCrush+"-0", 2);
			hangamTonnage.setRowColSpan(rowColhangamTonnage);
			hangamTonnage.setBoldIndicator(boldIndicator);
			
			hangamTonnage.setTableData(hangamTonnageTableData);
			
			crushingReportRes.setHangamTonnage(hangamTonnage);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;
		
	}

	public CrushingReportReponse generateVarietyTonnage(String date, String chit_boy_id,
			CrushingReportReponse crushingReportRes, Connection conn) {
		try {
			TableReportBean varietyTonnage = new TableReportBean();
			int noofheadDailyCrush =1;
			varietyTonnage.setNoofHeads(noofheadDailyCrush);
			varietyTonnage.setFooter(true);
			varietyTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColvarietyTonnage = new HashMap<>();
			
			HashMap<String, String> varietyfloatings=new HashMap<>();
			varietyfloatings.put("*-2", ".000");
			varietyfloatings.put("*-3", ".000");
			varietyTonnage.setFloatings(varietyfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> varietyTonnageTableData = new ArrayList<>();
			ArrayList<String> varietyTonnageRow = new ArrayList<>();
			varietyTonnageRow.add(ConstantVeriables.srno);
			varietyTonnageRow.add(ConstantVeriables.variety);
			varietyTonnageRow.add(ConstantVeriables.today);
			varietyTonnageRow.add(ConstantVeriables.todate);
			varietyTonnageTableData.add(varietyTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double todayTot = 0, todateTot = 0;
			try(PreparedStatement pst=conn.prepareStatement("select t.navariety_id_main, To_NCHAr(t.vvariety_name_main) as vvariety_name_main, t.nnet_weight_today, t.nnet_weight_todate from MD_V_VARIETY_TONNAGE_1 t order by t.navariety_id_main")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						varietyTonnageRow = new ArrayList<>();
						varietyTonnageRow.add(String.valueOf(srno++));
						varietyTonnageRow.add(DemoConvert2.ism_to_uni(rs.getString("vvariety_name_main"))+" ");// Added alt+0160
						varietyTonnageRow.add(rs.getString("nnet_weight_today"));
						varietyTonnageRow.add(rs.getString("nnet_weight_todate"));
						varietyTonnageTableData.add(varietyTonnageRow);
						todayTot+=rs.getDouble("nnet_weight_today");
						todateTot+=rs.getDouble("nnet_weight_todate");
					}
				}
			}
			int totalPosDailyCrush = varietyTonnageTableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.000");
			varietyTonnageRow = new ArrayList<>();
			varietyTonnageRow.add(ConstantVeriables.total);
			varietyTonnageRow.add(df.format(todayTot));
			varietyTonnageRow.add(df.format(todateTot));
			varietyTonnageTableData.add(varietyTonnageRow);
			
			rowColvarietyTonnage.put(totalPosDailyCrush+"-0", 2);
			varietyTonnage.setRowColSpan(rowColvarietyTonnage);
			varietyTonnage.setBoldIndicator(boldIndicator);
			
			varietyTonnage.setTableData(varietyTonnageTableData);
			
			crushingReportRes.setVarietyTonnage(varietyTonnage);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;
		
	}

	public String updateDate(String date, Connection conn) {
		String result = "1";
		try {
			conn.setAutoCommit(false);
			
			try(PreparedStatement pst=conn.prepareStatement("update MD_R_TEMP_DATES t set t.ddate=To_DATE(?, 'dd-Mon-yyyy'), t.ddate_yesterday=To_DATE(?, 'dd-Mon-yyyy')"))
			{
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(date));
				pst.setString(i++, Constant.AppDateMinus1DbDate(date));
				pst.executeUpdate(); 
			}
			
			String vyearCode= "";
			Date dfdate= null;
			try(PreparedStatement pst=conn.prepareStatement("select t.dfdate,t.vseason_year from GM_M_SEASON_YEAR_MASTER t where t.vactive = 'Y'"))
			{
				try(ResultSet rs = pst.executeQuery()) {
					if(rs.next()) {
						vyearCode=rs.getString("vseason_year");
						dfdate=rs.getDate("dfdate");
					}
				} 
			}
			
			try(PreparedStatement pst=conn.prepareStatement("update CR_R_TEMP_DATE_HARVESTED t set t.dto_date=To_DATE(?, 'dd-Mon-yyyy'), t.dfrom_date=?, t.vyear_id=?"))
			{
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(date));
				pst.setDate(i++, dfdate);
				pst.setString(i++, vyearCode);
				pst.executeUpdate(); 
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			result = "Date Update Error " + e.getMessage();
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public String updateDateAndSeason(String date, String vyearCode, Connection conn) {
		String result = "1";
		try {
			try(PreparedStatement pst=conn.prepareStatement("update MD_R_TEMP_DATES t set t.ddate=To_DATE(?, 'dd-Mon-yyyy'), t.vyear_id=?"))
			{
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(date));
				pst.setString(i++, vyearCode);
				pst.executeUpdate(); 
			}
			
		} catch (Exception e) {
			result = "Date Update Error " + e.getMessage();
			e.printStackTrace();
		}
		return result;
	}

	public CrushingReportReponse generateSectionTonnageAndList(String date, String chit_boy_id,
			CrushingReportReponse crushingReportRes, Connection conn) {
		try {
			TableReportBean sectionTonnage = new TableReportBean();
			int noofheadDailyCrush =1;
			sectionTonnage.setNoofHeads(noofheadDailyCrush);
			sectionTonnage.setFooter(true);
			sectionTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColSectionTonnage = new HashMap<>();
			
			HashMap<String, String> sectionfloatings=new HashMap<>();
			sectionfloatings.put("*-2", ".000");
			sectionfloatings.put("*-3", ".000");
			sectionTonnage.setFloatings(sectionfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> sectionTonnageTableData = new ArrayList<>();
			ArrayList<String> sectionTonnageRow = new ArrayList<>();
			sectionTonnageRow.add(ConstantVeriables.srno);
			sectionTonnageRow.add(ConstantVeriables.section);
			sectionTonnageRow.add(ConstantVeriables.today);
			sectionTonnageRow.add(ConstantVeriables.todate);
			sectionTonnageTableData.add(sectionTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			List<KeyPairBoolData> sectionList = new ArrayList<>();
			double todayTot = 0, todateTot = 0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id_main, To_NCHAR(t.vsection_name_local_main) as vsection_name_local_main, t.nnet_weight_today, t.nnet_weight_todate from Md_v_Section_Tonnage_1 t order by t.nsection_id_main")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						KeyPairBoolData section = new KeyPairBoolData();
						String sectionName = DemoConvert2.ism_to_uni(rs.getString("vsection_name_local_main"));
						section.setId(rs.getInt("nsection_id_main"));
						section.setName(sectionName);
						section.setSelected(rs.getInt("nsection_id_main")==1);
						sectionList.add(section);
						
						sectionTonnageRow = new ArrayList<>();
						sectionTonnageRow.add(String.valueOf(srno++));
						sectionTonnageRow.add(sectionName);
						sectionTonnageRow.add(rs.getString("nnet_weight_today"));
						sectionTonnageRow.add(rs.getString("nnet_weight_todate"));
						sectionTonnageTableData.add(sectionTonnageRow);
						todayTot+=rs.getDouble("nnet_weight_today");
						todateTot+=rs.getDouble("nnet_weight_todate");
					}
				}
			}
			int totalPosDailyCrush = sectionTonnageTableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.000");
			sectionTonnageRow = new ArrayList<>();
			sectionTonnageRow.add(ConstantVeriables.total);
			sectionTonnageRow.add(df.format(todayTot));
			sectionTonnageRow.add(df.format(todateTot));
			sectionTonnageTableData.add(sectionTonnageRow);
			
			rowColSectionTonnage.put(totalPosDailyCrush+"-0", 2);
			sectionTonnage.setRowColSpan(rowColSectionTonnage);
			sectionTonnage.setBoldIndicator(boldIndicator);
			
			sectionTonnage.setTableData(sectionTonnageTableData);
			
			crushingReportRes.setSectionTonnage(sectionTonnage);
			crushingReportRes.setSectionList(sectionList);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;
		
	}
	
	public CrushingReportReponse generateCropTypeTonnage(String date, String chit_boy_id,
			CrushingReportReponse crushingReportRes, Connection conn) {
		try {
			TableReportBean cropTypeTonnage = new TableReportBean();
			int noofheadDailyCrush =2;
			cropTypeTonnage.setNoofHeads(noofheadDailyCrush);
			cropTypeTonnage.setFooter(true);
			cropTypeTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColCropTypeTonnage = new HashMap<>();
			HashMap<String, String> croptypefloatings=new HashMap<>();
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> cropTypeTableData = new ArrayList<>();
			ArrayList<String> cropTypeRow = new ArrayList<>();
			cropTypeRow.add(ConstantVeriables.croptype+ ConstantVeriables.rightArrow);
			
			ArrayList<String> subCropTypeRow = new ArrayList<>();
			subCropTypeRow.add(ConstantVeriables.section);
			
			int pos =1;
			HashMap<Integer, Integer> colPos = new HashMap<>();
			try (PreparedStatement pst =conn.prepareStatement("select t.ncrop_type_id_main, To_NCHAR(t.vcroptype_name_local_main) as vcroptype_name_local_main from WB_M_CROP_TYPE_MASTER_MAIN t order by ncrop_type_id_main")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						rowColCropTypeTonnage.put("0-"+pos, 2);
						colPos.put(rs.getInt("ncrop_type_id_main"), pos);
						if(rs.getInt("ncrop_type_id_main")==2) {
							cropTypeRow.add(ConstantVeriables.valaka);
						} else {
							cropTypeRow.add(DemoConvert2.ism_to_uni(rs.getString("vcroptype_name_local_main")));
						}
						subCropTypeRow.add(ConstantVeriables.today);
						subCropTypeRow.add(ConstantVeriables.todate);
						pos++;
					}
				}
			}
			rowColCropTypeTonnage.put("0-"+pos, 2);
			cropTypeRow.add(ConstantVeriables.total);
			cropTypeTableData.add(cropTypeRow);
			
			subCropTypeRow.add(ConstantVeriables.today);
			subCropTypeRow.add(ConstantVeriables.todate);
			cropTypeTableData.add(subCropTypeRow);
			cropTypeTonnage.setRowColSpan(rowColCropTypeTonnage);
			int hrTotalToday = pos*2-1;
			int hrTotalTodate = pos*2;
			
			boldIndicator.put("*-"+hrTotalToday, true);
			boldIndicator.put("*-"+hrTotalTodate, true);
	
			boldIndicator.put("0-"+pos, true);
			
			HashMap<String, Integer> rowPos= new HashMap<String, Integer>();
			List<KeyPairBoolData> sectionList = crushingReportRes.getSectionList();
			int size = sectionList.size();
			for (int i = 0; i < size+1; i++) {
				cropTypeRow = new ArrayList<>();
				if(i!=size) {
					KeyPairBoolData keyPairBoolData= sectionList.get(i);
					rowPos.put(String.valueOf(keyPairBoolData.getId()), i+2);
					cropTypeRow.add(keyPairBoolData.getName());
				} else {
					boldIndicator.put((i+2)+"-*", true);
					rowPos.put("total", i+2);
					cropTypeRow.add(ConstantVeriables.total);
				}
				for(int k=1;k<pos+1;k++) {
					cropTypeRow.add(ConstantVeriables.defaultTon);
					cropTypeRow.add(ConstantVeriables.defaultTon);
					croptypefloatings.put("*-"+(k*2-1), " ");
					croptypefloatings.put("*-"+(k*2), " ");
				}
				cropTypeTableData.add(cropTypeRow);
			}
			
			
			cropTypeTonnage.setBoldIndicator(boldIndicator);
			
			try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id_main,t.ncrop_type_id_main,t.nnet_weight_today, t.nnet_weight_todate from Md_v_Crop_Type_Tonnage_1 t")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int ncrop_type_id_main = rs.getInt("ncrop_type_id_main");
						int colno=colPos.get(ncrop_type_id_main);
						String nsection_id_main = rs.getString("nsection_id_main");
						double nnet_weight = rs.getDouble("nnet_weight_today");
						double nnet_weight_yesterday = rs.getDouble("nnet_weight_todate");
						
						int dailyCrushingIndex = rowPos.get(nsection_id_main);
						int totalCrushingIndex = rowPos.get("total");
						
						cropTypeRow = cropTypeTableData.get(dailyCrushingIndex);
						ArrayList<String> totalCrushingRow  = cropTypeTableData.get(totalCrushingIndex);
						
						cropTypeRow.set(colno*2-1, Constant.decimalFormat(nnet_weight, "000"));
						cropTypeRow.set(colno*2, Constant.decimalFormat(nnet_weight_yesterday, "000"));
	
						cropTypeRow.set(hrTotalToday, Constant.decimalFormat(Double.parseDouble(cropTypeRow.get(hrTotalToday)) + nnet_weight, "000"));
						cropTypeRow.set(hrTotalTodate, Constant.decimalFormat(Double.parseDouble(cropTypeRow.get(hrTotalTodate)) + nnet_weight_yesterday, "000"));
	
						totalCrushingRow.set(colno*2-1, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(colno*2-1)) + nnet_weight, "000"));
						totalCrushingRow.set(colno*2, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(colno*2)) + nnet_weight_yesterday, "000"));
						
						if (croptypefloatings.containsKey("*-" + (colno * 2 - 1))) {
							if(!totalCrushingRow.get(colno * 2 - 1).equals("0.000")) {
								croptypefloatings.remove("*-" + (colno * 2 - 1));
							}
						}
	
						if (croptypefloatings.containsKey("*-" + (colno * 2 ))) {
							if(!totalCrushingRow.get(colno * 2 ).equals("0.000")) {
								croptypefloatings.remove("*-" + (colno * 2));
							}
						}
						
						if (croptypefloatings.containsKey("*-" + hrTotalToday)) {
							if(!totalCrushingRow.get(hrTotalToday ).equals("0.000")) {
								croptypefloatings.remove("*-" + hrTotalToday);
							}
						}
	
						if (croptypefloatings.containsKey("*-" + hrTotalTodate)) {
							if(!totalCrushingRow.get(hrTotalTodate).equals("0.000")) {
								croptypefloatings.remove("*-" + (hrTotalTodate));
							}
						}
						
						totalCrushingRow.set(hrTotalToday, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(hrTotalToday)) + nnet_weight, "000"));
						totalCrushingRow.set(hrTotalTodate, Constant.decimalFormat(Double.parseDouble(totalCrushingRow.get(hrTotalTodate)) + nnet_weight_yesterday, "000"));
								
						cropTypeTableData.set(dailyCrushingIndex, cropTypeRow);
						cropTypeTableData.set(totalCrushingIndex, totalCrushingRow);
					}
				}
			}
			
			cropTypeTonnage.setTableData(cropTypeTableData);
			cropTypeTonnage.setFloatings(croptypefloatings);
			crushingReportRes.setCropTypeTonnage(cropTypeTonnage);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;
	}

	/* not in use after deploye changed 
	public CrushingPlantHarvVillResponse generatePlantHarvVillTonnage(String date, String chit_boy_id, Long section_code,
			CrushingPlantHarvVillResponse crushingReportRes, Connection conn) {
		try {
			TableReportBean plantHarvVillTonnage = new TableReportBean();
			int noofheadDailyCrush =1;
			plantHarvVillTonnage.setNoofHeads(noofheadDailyCrush);
			plantHarvVillTonnage.setFooter(true);
			plantHarvVillTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColPlantHarvVillTonnage = new HashMap<>();
			
			HashMap<String, String> plantHarvVillfloatings=new HashMap<>();
			plantHarvVillfloatings.put("*-2", ".00");
			plantHarvVillfloatings.put("*-3", " ");
			plantHarvVillfloatings.put("*-4", ".00");
			plantHarvVillfloatings.put("*-5", " ");
			plantHarvVillfloatings.put("*-6", ".00");
			plantHarvVillfloatings.put("*-7", " ");
			
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> plantHarvVillTonnageTableData = new ArrayList<>();
			ArrayList<String> plantHarvVillTonnageRow = new ArrayList<>();
			plantHarvVillTonnageRow.add(ConstantVeriables.srno);
			plantHarvVillTonnageRow.add(ConstantVeriables.village);
			plantHarvVillTonnageRow.add(ConstantVeriables.registerArea);
			plantHarvVillTonnageRow.add(ConstantVeriables.expectedTonnage);
			plantHarvVillTonnageRow.add(ConstantVeriables.harvestedArea);
			plantHarvVillTonnageRow.add(ConstantVeriables.receivedTonnage);
			plantHarvVillTonnageRow.add(ConstantVeriables.remainingArea);
			plantHarvVillTonnageRow.add(ConstantVeriables.expectedreceivedTonnage);
			plantHarvVillTonnageTableData.add(plantHarvVillTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double narea = 0, nexpected_yield = 0,narea_harvested = 0, nnet_weight_harvested = 0, narea_remaining = 0, nyield_remaining = 0;
			try(PreparedStatement pst=conn.prepareStatement("select TO_NCHAR(t.village_name_local) as village_name_local, t.narea, t.nexpected_yield, t.narea_harvested, t.nnet_weight_harvested, t.narea_remaining, t.nyield_remaining from Md_v_Plant_Harv_Vill_Remain_1 t where t.nsection_id_main=?")){
				pst.setLong(1, section_code);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						plantHarvVillTonnageRow = new ArrayList<>();
						plantHarvVillTonnageRow.add(String.valueOf(srno++));
						plantHarvVillTonnageRow.add(DemoConvert2.ism_to_uni(rs.getString("village_name_local")));
						plantHarvVillTonnageRow.add(rs.getString("narea"));
						plantHarvVillTonnageRow.add(rs.getString("nexpected_yield"));
						plantHarvVillTonnageRow.add(rs.getString("narea_harvested"));
						plantHarvVillTonnageRow.add(rs.getString("nnet_weight_harvested"));
						plantHarvVillTonnageRow.add(rs.getString("narea_remaining"));
						plantHarvVillTonnageRow.add(rs.getString("nyield_remaining"));
						plantHarvVillTonnageTableData.add(plantHarvVillTonnageRow);
						narea+=rs.getDouble("narea");
						nexpected_yield+=rs.getDouble("nexpected_yield");
						narea_harvested+=rs.getDouble("narea_harvested");
						nnet_weight_harvested+=rs.getDouble("nnet_weight_harvested");
						narea_remaining+=rs.getDouble("narea_remaining");
						nyield_remaining+=rs.getDouble("nyield_remaining");
					}
				}
			}
			int totalPosDailyCrush = plantHarvVillTonnageTableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			plantHarvVillfloatings.put(totalPosDailyCrush+"-1", ".00");
			plantHarvVillfloatings.put(totalPosDailyCrush+"-2", " ");
			plantHarvVillfloatings.put(totalPosDailyCrush+"-3", ".00");
			plantHarvVillfloatings.put(totalPosDailyCrush+"-4", " ");
			plantHarvVillfloatings.put(totalPosDailyCrush+"-5", ".00");
			plantHarvVillfloatings.put(totalPosDailyCrush+"-6", " ");
			plantHarvVillTonnage.setFloatings(plantHarvVillfloatings);
			
			DecimalFormat df = new DecimalFormat("#0.000");
			plantHarvVillTonnageRow = new ArrayList<>();
			
			plantHarvVillTonnageRow.add(ConstantVeriables.total);
			plantHarvVillTonnageRow.add(df.format(narea));
			plantHarvVillTonnageRow.add(df.format(nexpected_yield));
			plantHarvVillTonnageRow.add(df.format(narea_harvested));
			plantHarvVillTonnageRow.add(df.format(nnet_weight_harvested));
			plantHarvVillTonnageRow.add(df.format(narea_remaining));
			plantHarvVillTonnageRow.add(df.format(nyield_remaining));
			plantHarvVillTonnageTableData.add(plantHarvVillTonnageRow);
			
			rowColPlantHarvVillTonnage.put(totalPosDailyCrush+"-0", 2);
			plantHarvVillTonnage.setRowColSpan(rowColPlantHarvVillTonnage);
			plantHarvVillTonnage.setBoldIndicator(boldIndicator);
			
			plantHarvVillTonnage.setTableData(plantHarvVillTonnageTableData);
			
			crushingReportRes.setPlantHarvVill(plantHarvVillTonnage);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;
	}*/

	public AgriReportReponse generateNondSummary(String date, String chit_boy_id, AgriReportReponse agriReportRes,
			Connection conn) {
		try {

			TableReportBean nondSummaryReport = new TableReportBean();
			nondSummaryReport.setNoofHeads(1);
			nondSummaryReport.setFooter(false);
			nondSummaryReport.setMarathi(true);
			HashMap<String, String> nondFloatings = new HashMap<>();
			nondFloatings.put("5-1", ".00");
			nondFloatings.put("6-1", ".00");
			nondFloatings.put("7-1", ".00");
			nondFloatings.put("8-1", ".00");
			nondSummaryReport.setFloatings(nondFloatings);
			ArrayList<ArrayList<String>> nondSummaryReportTableData = new ArrayList<>();
			ArrayList<String> nondSummaryReportRow = new ArrayList<>();
			nondSummaryReportRow.add(ConstantVeriables.details);
			nondSummaryReportRow.add(ConstantVeriables.numbers);
			nondSummaryReportTableData.add(nondSummaryReportRow);
			
			try(PreparedStatement pst=conn.prepareStatement("select count(distinct t.nentity_uni_id) as regfarmer, count(t.nplot_no) as regplot, sum(case when t.nconfirm_flag=1 or t.nconfirm_flag=2 then 1 end) confirmplot, sum(case when t.nconfirm_flag=0 then 1 end) nonconfirmplot, sum(t.narea) as regarea, sum(t.ntentative_area) as confirmarea, sum(t.ncancelled_area) as cancelarea, sum(t.npending_area) as pendingarea from MD_V_PLANTION t")){
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						
						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.regfarmer);
						nondSummaryReportRow.add(rs.getString("regfarmer"));
						nondSummaryReportTableData.add(nondSummaryReportRow);
						
						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.regplot);
						nondSummaryReportRow.add(rs.getString("regplot"));
						nondSummaryReportTableData.add(nondSummaryReportRow);
						
						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.confirmplot);
						nondSummaryReportRow.add(rs.getString("confirmplot"));
						nondSummaryReportTableData.add(nondSummaryReportRow);

						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.nonconfirmplot);
						nondSummaryReportRow.add(rs.getString("nonconfirmplot"));
						nondSummaryReportTableData.add(nondSummaryReportRow);
						
						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.regarea);
						nondSummaryReportRow.add(rs.getString("regarea"));
						nondSummaryReportTableData.add(nondSummaryReportRow);
						
						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.confirmarea);
						nondSummaryReportRow.add(rs.getString("confirmarea"));
						nondSummaryReportTableData.add(nondSummaryReportRow);

						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.cancelarea);
						nondSummaryReportRow.add(rs.getString("cancelarea"));
						nondSummaryReportTableData.add(nondSummaryReportRow);
						
						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.pendingarea);
						nondSummaryReportRow.add(rs.getString("pendingarea"));
						nondSummaryReportTableData.add(nondSummaryReportRow);
									
					} else {
						HashMap<String, Integer> rowColNondSummaryReport = new HashMap<>();
						nondSummaryReportRow = new ArrayList<>();
						nondSummaryReportRow.add(ConstantVeriables.reportnotfilled);
						nondSummaryReportTableData.add(nondSummaryReportRow);
						rowColNondSummaryReport.put("1-0", 2);
						nondSummaryReport.setRowColSpan(rowColNondSummaryReport);
					}
				}
			}
			
			nondSummaryReport.setTableData(nondSummaryReportTableData);
			agriReportRes.setNondSummary(nondSummaryReport);
		} catch (Exception e) {
			agriReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Agri Report Error " + e.getMessage());
			agriReportRes.setSe(error);
			e.printStackTrace();
		}
		return agriReportRes;

	}

	public AgriReportReponse generateNondAndExpTonnage(String date, String chit_boy_id, AgriReportReponse agriReportRes,
			Connection conn) {
		try {
			TableReportBean nondAndExpTonnage = new TableReportBean();
			int noofheadDailyCrush =1;
			nondAndExpTonnage.setNoofHeads(noofheadDailyCrush);
			nondAndExpTonnage.setFooter(true);
			nondAndExpTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColnondAndExpTonnage = new HashMap<>();
			
			HashMap<String, String> sectionfloatings=new HashMap<>();
			sectionfloatings.put("*-2", ".00");
			sectionfloatings.put("*-3", ".00");
			sectionfloatings.put("*-4", ".00");
			sectionfloatings.put("*-5", ".00");
			sectionfloatings.put("*-6", ".00");
			sectionfloatings.put("*-7", " ");
			
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> nondAndExpTonnageTableData = new ArrayList<>();
			ArrayList<String> nondAndExpTonnageRow = new ArrayList<>();
			nondAndExpTonnageRow.add(ConstantVeriables.srno);
			nondAndExpTonnageRow.add(ConstantVeriables.section);
			nondAndExpTonnageRow.add(ConstantVeriables.nondarea);
			nondAndExpTonnageRow.add(ConstantVeriables.confirmpending);
			nondAndExpTonnageRow.add(ConstantVeriables.cancelarea);
			nondAndExpTonnageRow.add(ConstantVeriables.increasedarea);
			nondAndExpTonnageRow.add(ConstantVeriables.actualarea);
			nondAndExpTonnageRow.add(ConstantVeriables.expectedTonnage);
			
			nondAndExpTonnageTableData.add(nondAndExpTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double narea = 0, npending_area = 0, ncancelled_area = 0, nincreased_area = 0, ntentative_area = 0, nexpected_yield = 0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id_main, TO_NCHAR(t.vsection_name_local_main) as vsection_name_local_main, t.narea, t.npending_area, t.ncancelled_area, t.nincreased_area, t.ntentative_area, t.nexpected_yield from md_v_section_plantion t")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						nondAndExpTonnageRow = new ArrayList<>();
						nondAndExpTonnageRow.add(String.valueOf(srno++));
						nondAndExpTonnageRow.add(DemoConvert2.ism_to_uni(rs.getString("vsection_name_local_main")));// Added alt+0160
						nondAndExpTonnageRow.add(rs.getString("narea"));
						nondAndExpTonnageRow.add(rs.getString("npending_area"));
						nondAndExpTonnageRow.add(rs.getString("ncancelled_area"));
						nondAndExpTonnageRow.add(rs.getString("nincreased_area"));
						nondAndExpTonnageRow.add(rs.getString("ntentative_area"));
						nondAndExpTonnageRow.add(rs.getString("nexpected_yield"));
						nondAndExpTonnageTableData.add(nondAndExpTonnageRow);
						
						narea+=rs.getDouble("narea");
						npending_area+=rs.getDouble("npending_area");
						ncancelled_area+=rs.getDouble("ncancelled_area");
						nincreased_area+=rs.getDouble("nincreased_area");
						ntentative_area+=rs.getDouble("ntentative_area");
						nexpected_yield+=rs.getDouble("nexpected_yield");
					}
				}
			}
			int totalPosSection = nondAndExpTonnageTableData.size();
			boldIndicator.put(totalPosSection+"-*", true);
			sectionfloatings.put(totalPosSection+"-1", ".00");
			sectionfloatings.put(totalPosSection+"-6", " ");
			nondAndExpTonnage.setFloatings(sectionfloatings);
			DecimalFormat df = new DecimalFormat("#0.000");
			nondAndExpTonnageRow = new ArrayList<>();
			nondAndExpTonnageRow.add(ConstantVeriables.total);
			nondAndExpTonnageRow.add(df.format(narea));
			nondAndExpTonnageRow.add(df.format(npending_area));
			nondAndExpTonnageRow.add(df.format(ncancelled_area));
			nondAndExpTonnageRow.add(df.format(nincreased_area));
			nondAndExpTonnageRow.add(df.format(ntentative_area));
			nondAndExpTonnageRow.add(df.format(nexpected_yield));
			nondAndExpTonnageTableData.add(nondAndExpTonnageRow);
			
			rowColnondAndExpTonnage.put(totalPosSection+"-0", 2);
			nondAndExpTonnage.setRowColSpan(rowColnondAndExpTonnage);
			nondAndExpTonnage.setBoldIndicator(boldIndicator);
			
			nondAndExpTonnage.setTableData(nondAndExpTonnageTableData);
			
			agriReportRes.setNondAndExpTonnage(nondAndExpTonnage);
		} catch (Exception e) {
			agriReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Agri Report Error " + e.getMessage());
			agriReportRes.setSe(error);
			e.printStackTrace();
		}
		return agriReportRes;
	}

	public AgriReportReponse generateSectionVarietyNond(String date, String chit_boy_id,
			boolean webbridgeact, AgriReportReponse agriReportRes, Connection conn) {
		try {
			TableReportBean sectionVarietyNond = new TableReportBean();
			int noofheadDailyCrush =1;
			sectionVarietyNond.setNoofHeads(noofheadDailyCrush);
			sectionVarietyNond.setFooter(true);
			sectionVarietyNond.setMarathi(true);
			
			HashMap<String, Integer> rowColsectionVarietyNond = new HashMap<>();
			
			HashMap<String, String> sectionVarietyfloatings=new HashMap<>();
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> sectionVarietyNondTableData = new ArrayList<>();
			ArrayList<String> sectionVarietyNondRow = new ArrayList<>();
			ArrayList<String> sectionVarietyNondEmpty = new ArrayList<>();
			sectionVarietyNondRow.add(ConstantVeriables.srno);
			sectionVarietyNondRow.add(ConstantVeriables.section);
			
			sectionVarietyNondEmpty.add("");
			sectionVarietyNondEmpty.add("");
			HashMap<Integer, Integer> colPos = new HashMap<>();
			int colnoTemp = 2;
			try(PreparedStatement pst = conn.prepareStatement("select distinct t.navariety_id_main, TO_NCHAR(t.vvariety_name_main) as vvariety_name_main from Md_v_Section_Variety_Plantion t order by navariety_id_main")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						sectionVarietyfloatings.put("*-"+colnoTemp, ".00");
						colPos.put(rs.getInt("navariety_id_main"), colnoTemp);
						sectionVarietyNondRow.add(DemoConvert2.ism_to_uni(rs.getString("vvariety_name_main")));
						sectionVarietyNondEmpty.add("0.00");
						colnoTemp++;
					}
				}
			}
			sectionVarietyNondEmpty.add("0.00");
			int hrTotalPos = colnoTemp;
			sectionVarietyfloatings.put("*-"+hrTotalPos, ".00");
			sectionVarietyNondRow.add(ConstantVeriables.total);
			sectionVarietyNondTableData.add(sectionVarietyNondRow);
			
			ArrayList<String> sectionVarietyNondRowTotal = new ArrayList<>();
			sectionVarietyNondRowTotal =(ArrayList<String>) sectionVarietyNondEmpty.clone();
			sectionVarietyNondRowTotal.remove(0);
			sectionVarietyNondRowTotal.set(0,ConstantVeriables.total);
			
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			HashMap<String, Integer> rowPos = new HashMap<>();
			int rowIndex = 1;
			try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id_main, TO_NCHAR(t.vsection_name_local_main) as vsection_name_local_main, t.navariety_id_main, t.narea, t.ntentative_area from Md_v_Section_Variety_Plantion t order by nsection_id_main")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int navariety_id_main = rs.getInt("navariety_id_main");
						int colno=colPos.get(navariety_id_main);
						String nsection_id_main = rs.getString("nsection_id_main");
						double narea = 0;
						if (webbridgeact)
							narea = rs.getDouble("ntentative_area");
						else
							narea = rs.getDouble("narea");
						int sectionVarietyIndex;
						if(!rowPos.containsKey(nsection_id_main)) {
							sectionVarietyIndex=rowIndex;
							sectionVarietyNondRow =(ArrayList<String>) sectionVarietyNondEmpty.clone();
							sectionVarietyNondRow.set(0, String.valueOf(srno++));
							sectionVarietyNondRow.set(1, DemoConvert2.ism_to_uni(rs.getString("vsection_name_local_main")));
							rowPos.put(nsection_id_main, sectionVarietyIndex);
							sectionVarietyNondTableData.add(sectionVarietyNondRow);
							rowIndex++;
						} else {
							sectionVarietyIndex = rowPos.get(nsection_id_main);
							sectionVarietyNondRow = sectionVarietyNondTableData.get(sectionVarietyIndex);
						}
						
						sectionVarietyNondRow.set(colno, Constant.decimalFormat(narea, "00"));
						
						sectionVarietyNondRow.set(hrTotalPos, Constant.decimalFormat(Double.parseDouble(sectionVarietyNondRow.get(hrTotalPos)) + narea, "00"));
						
						sectionVarietyNondRowTotal.set(colno-1, Constant.decimalFormat(Double.parseDouble(sectionVarietyNondRowTotal.get(colno-1)) + narea, "00"));
						
						sectionVarietyNondRowTotal.set(hrTotalPos-1, Constant.decimalFormat(Double.parseDouble(sectionVarietyNondRowTotal.get(hrTotalPos-1)) + narea, "00"));
								
						sectionVarietyNondTableData.set(sectionVarietyIndex, sectionVarietyNondRow);
					}
				}
			}
			int totalPosSectionVariety = sectionVarietyNondTableData.size();
			boldIndicator.put(totalPosSectionVariety+"-*", true);
			sectionVarietyfloatings.put(totalPosSectionVariety+"-1", ".00");
			sectionVarietyNond.setFloatings(sectionVarietyfloatings);
			
			sectionVarietyNondTableData.add(sectionVarietyNondRowTotal);
			
			rowColsectionVarietyNond.put(totalPosSectionVariety+"-0", 2);
			sectionVarietyNond.setRowColSpan(rowColsectionVarietyNond);
			sectionVarietyNond.setBoldIndicator(boldIndicator);
			
			sectionVarietyNond.setTableData(sectionVarietyNondTableData);
			
			agriReportRes.setSectionVarietyNond(sectionVarietyNond);
		} catch (Exception e) {
			agriReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Agri Report Error " + e.getMessage());
			agriReportRes.setSe(error);
			e.printStackTrace();
		}
		return agriReportRes;
	}

	public AgriReportReponse generateSectionHangamNond(String date, String chit_boy_id, boolean webbridgeact, AgriReportReponse agriReportRes,
			Connection conn) {
		try {
			TableReportBean sectionHangamNond = new TableReportBean();
			int noofheadDailyCrush =1;
			sectionHangamNond.setNoofHeads(noofheadDailyCrush);
			sectionHangamNond.setFooter(true);
			sectionHangamNond.setMarathi(true);
			
			HashMap<String, Integer> rowColsectionHangamNond = new HashMap<>();
			
			HashMap<String, String> sectionHangamfloatings=new HashMap<>();
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> sectionHangamNondTableData = new ArrayList<>();
			ArrayList<String> sectionHangamNondRow = new ArrayList<>();
			ArrayList<String> sectionHangamNondEmpty = new ArrayList<>();
			sectionHangamNondRow.add(ConstantVeriables.srno);
			sectionHangamNondRow.add(ConstantVeriables.section);
			
			sectionHangamNondEmpty.add("");
			sectionHangamNondEmpty.add("");
			HashMap<Integer, Integer> colPos = new HashMap<>();
			int colnoTemp = 2;
			try(PreparedStatement pst = conn.prepareStatement("select t.nhangam_id, TO_NCHAR(t.vhangam_name) as vhangam_name from Cr_m_Hangam_Master t order by t.nhangam_id")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						sectionHangamfloatings.put("*-"+colnoTemp, ".00");
						colPos.put(rs.getInt("nhangam_id"), colnoTemp);
						sectionHangamNondRow.add(DemoConvert2.ism_to_uni(rs.getString("vhangam_name")));
						sectionHangamNondEmpty.add("0.00");
						colnoTemp++;
					}
				}
			}
			sectionHangamNondEmpty.add("0.00");
			int hrTotalPos = colnoTemp;
			sectionHangamfloatings.put("*-"+hrTotalPos, ".00");
			sectionHangamNondRow.add(ConstantVeriables.total);
			sectionHangamNondTableData.add(sectionHangamNondRow);
			
			ArrayList<String> sectionHangamNondRowTotal = new ArrayList<>();
			sectionHangamNondRowTotal =(ArrayList<String>) sectionHangamNondEmpty.clone();
			sectionHangamNondRowTotal.remove(0);
			sectionHangamNondRowTotal.set(0,ConstantVeriables.total);
			
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			HashMap<String, Integer> rowPos = new HashMap<>();
			int rowIndex = 1;
			try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id_main, To_NCHAR(t.vsection_name_local_main) as vsection_name_local_main , t.nhangam_id, t.narea, t.ntentative_area from Md_v_Section_hangam_Plantion t order by t.nsection_id_main")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int navariety_id_main = rs.getInt("nhangam_id");
						int colno=colPos.get(navariety_id_main);
						String nsection_id_main = rs.getString("nsection_id_main");
						double narea = 0;
						if (webbridgeact)
							narea = rs.getDouble("ntentative_area");
						else
							narea = rs.getDouble("narea");

						int sectionHangamIndex;
						if(!rowPos.containsKey(nsection_id_main)) {
							sectionHangamIndex=rowIndex;
							sectionHangamNondRow =(ArrayList<String>) sectionHangamNondEmpty.clone();
							sectionHangamNondRow.set(0, String.valueOf(srno++));
							sectionHangamNondRow.set(1, DemoConvert2.ism_to_uni(rs.getString("vsection_name_local_main")));
							rowPos.put(nsection_id_main, sectionHangamIndex);
							sectionHangamNondTableData.add(sectionHangamNondRow);
							rowIndex++;
						} else {
							sectionHangamIndex = rowPos.get(nsection_id_main);
							sectionHangamNondRow = sectionHangamNondTableData.get(sectionHangamIndex);
						}
						
						sectionHangamNondRow.set(colno, Constant.decimalFormat(narea, "00"));
						
						sectionHangamNondRow.set(hrTotalPos, Constant.decimalFormat(Double.parseDouble(sectionHangamNondRow.get(hrTotalPos)) + narea, "00"));
						
						sectionHangamNondRowTotal.set(colno-1, Constant.decimalFormat(Double.parseDouble(sectionHangamNondRowTotal.get(colno-1)) + narea, "00"));
						
						sectionHangamNondRowTotal.set(hrTotalPos-1, Constant.decimalFormat(Double.parseDouble(sectionHangamNondRowTotal.get(hrTotalPos-1)) + narea, "00"));
								
						sectionHangamNondTableData.set(sectionHangamIndex, sectionHangamNondRow);
					}
				}
			}
			int totalPosSectionHangam = sectionHangamNondTableData.size();
			boldIndicator.put(totalPosSectionHangam+"-*", true);
			sectionHangamfloatings.put(totalPosSectionHangam+"-1", ".00");
			sectionHangamNond.setFloatings(sectionHangamfloatings);
			
			sectionHangamNondTableData.add(sectionHangamNondRowTotal);
			
			rowColsectionHangamNond.put(totalPosSectionHangam+"-0", 2);
			sectionHangamNond.setRowColSpan(rowColsectionHangamNond);
			sectionHangamNond.setBoldIndicator(boldIndicator);
			
			sectionHangamNond.setTableData(sectionHangamNondTableData);
			
			agriReportRes.setSectionHangamNond(sectionHangamNond);
		} catch (Exception e) {
			agriReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Agri Report Error " + e.getMessage());
			agriReportRes.setSe(error);
			e.printStackTrace();
		}
		return agriReportRes;
	}

	public AgriReportReponse generateHangamVarietyNond(String date, String chit_boy_id, boolean webbridgeact, AgriReportReponse agriReportRes,
			Connection conn) {
		try {
			TableReportBean hangamVarietyNond = new TableReportBean();
			int noofheadDailyCrush =2;
			hangamVarietyNond.setNoofHeads(noofheadDailyCrush);
			hangamVarietyNond.setFooter(true);
			hangamVarietyNond.setMarathi(true);
			
			HashMap<String, Integer> rowColHangamVarietyNond = new HashMap<>();
			rowColHangamVarietyNond.put("0-0", 2);
			HashMap<String, String> hangamVarietyfloatings=new HashMap<>();
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> hangamVarietyNondTableData = new ArrayList<>();
			ArrayList<String> hangamVarietyNondRow = new ArrayList<>();
			ArrayList<String> hangamVarietyNondHeadRow = new ArrayList<>();
			ArrayList<String> hangamVarietyNondEmpty = new ArrayList<>();
			
			hangamVarietyNondHeadRow.add(ConstantVeriables.variety+ConstantVeriables.rightArrow);
			
			hangamVarietyNondRow.add(ConstantVeriables.srno);
			hangamVarietyNondRow.add(ConstantVeriables.hangam);
			
			hangamVarietyNondEmpty.add("");
			hangamVarietyNondEmpty.add("");
			HashMap<Integer, Integer> colPos = new HashMap<>();
			int colnoTemp = 1;
			try(PreparedStatement pst = conn.prepareStatement("select distinct t.navariety_id_main, TO_NCHAR(t.vvariety_name_main) as vvariety_name_main from Md_v_Hangam_Variety_Plantion t order by navariety_id_main")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						rowColHangamVarietyNond.put("0-"+colnoTemp, 2);
						hangamVarietyfloatings.put("*-"+(colnoTemp*2), ".00");
						hangamVarietyfloatings.put("*-"+(colnoTemp*2+1), " ");
						colPos.put(rs.getInt("navariety_id_main"), colnoTemp);
						hangamVarietyNondHeadRow.add(DemoConvert2.ism_to_uni(rs.getString("vvariety_name_main")));
						hangamVarietyNondRow.add(ConstantVeriables.area);
						hangamVarietyNondRow.add(ConstantVeriables.expTonnage);
						hangamVarietyNondEmpty.add("0.00");
						hangamVarietyNondEmpty.add("0.00");
						colnoTemp++;
					}
				}
			}
			
			hangamVarietyNondEmpty.add("0.00");
			hangamVarietyNondEmpty.add("0.00");
			int hrTotalAreaPos = colnoTemp*2;
			int hrTotalTonnagePos = colnoTemp*2+1;
			rowColHangamVarietyNond.put("0-"+colnoTemp, 2);
			hangamVarietyfloatings.put("*-"+hrTotalAreaPos, ".00");
			hangamVarietyfloatings.put("*-"+hrTotalTonnagePos, " ");
			hangamVarietyNondHeadRow.add(ConstantVeriables.total);
			hangamVarietyNondRow.add(ConstantVeriables.area);
			hangamVarietyNondRow.add(ConstantVeriables.expTonnage);
			hangamVarietyNondTableData.add(hangamVarietyNondHeadRow);
			hangamVarietyNondTableData.add(hangamVarietyNondRow);
			
			ArrayList<String> hangamVarietyNondRowTotal = new ArrayList<>();
			hangamVarietyNondRowTotal =(ArrayList<String>) hangamVarietyNondEmpty.clone();
			hangamVarietyNondRowTotal.remove(0);
			hangamVarietyNondRowTotal.set(0,ConstantVeriables.total);
			
			DecimalFormat df=new DecimalFormat("#0");
			boldIndicator.put("0-*", true);
			int srno = 1;
			HashMap<String, Integer> rowPos = new HashMap<>();
			int rowIndex = 2;
			try(PreparedStatement pst=conn.prepareStatement("select t.nhangam_id, TO_NCHAR(t.vhangam_name) as vhangam_name, t.navariety_id_main, t.narea, t.ntentative_area, t.nexpected_yield from Md_v_Hangam_Variety_Plantion t order by nhangam_id")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int navariety_id_main = rs.getInt("navariety_id_main");
						int colno=colPos.get(navariety_id_main);
						String nhangam_id = rs.getString("nhangam_id");
						double nexpected_yield = rs.getDouble("nexpected_yield");
						double narea = 0;
						if (webbridgeact)
							narea = rs.getDouble("ntentative_area");
						else
							narea = rs.getDouble("narea");

						int hangamVarietyIndex;
						if(!rowPos.containsKey(nhangam_id)) {
							hangamVarietyIndex=rowIndex;
							hangamVarietyNondRow =(ArrayList<String>) hangamVarietyNondEmpty.clone();
							hangamVarietyNondRow.set(0, String.valueOf(srno++));
							hangamVarietyNondRow.set(1, DemoConvert2.ism_to_uni(rs.getString("vhangam_name")));
							rowPos.put(nhangam_id, hangamVarietyIndex);
							hangamVarietyNondTableData.add(hangamVarietyNondRow);
							rowIndex++;
						} else {
							hangamVarietyIndex = rowPos.get(nhangam_id);
							hangamVarietyNondRow = hangamVarietyNondTableData.get(hangamVarietyIndex);
						}
						
						hangamVarietyNondRow.set(colno*2, Constant.decimalFormat(narea, "00"));
						hangamVarietyNondRow.set(colno*2+1, df.format(nexpected_yield));
						
						hangamVarietyNondRow.set(hrTotalAreaPos, Constant.decimalFormat(Double.parseDouble(hangamVarietyNondRow.get(hrTotalAreaPos)) + narea, "00"));
						hangamVarietyNondRow.set(hrTotalTonnagePos, df.format(Double.parseDouble(hangamVarietyNondRow.get(hrTotalTonnagePos)) + nexpected_yield));
						
						hangamVarietyNondRowTotal.set(colno*2-1, Constant.decimalFormat(Double.parseDouble(hangamVarietyNondRowTotal.get(colno*2-1)) + narea, "00"));
						hangamVarietyNondRowTotal.set(colno*2, df.format(Double.parseDouble(hangamVarietyNondRowTotal.get(colno*2)) + nexpected_yield));
						
						hangamVarietyNondRowTotal.set(hrTotalAreaPos-1, Constant.decimalFormat(Double.parseDouble(hangamVarietyNondRowTotal.get(hrTotalAreaPos-1)) + narea, "00"));
						hangamVarietyNondRowTotal.set(hrTotalTonnagePos-1, df.format(Double.parseDouble(hangamVarietyNondRowTotal.get(hrTotalTonnagePos-1)) + nexpected_yield));
								
						hangamVarietyNondTableData.set(hangamVarietyIndex, hangamVarietyNondRow);
					}
				}
			}
			int totalPosHangamVariety = hangamVarietyNondTableData.size();
			boldIndicator.put(totalPosHangamVariety+"-*", true);
			for(int i=1;i<colnoTemp;i++) {
				hangamVarietyfloatings.put(totalPosHangamVariety+"-"+(i*2-1), ".00");
				hangamVarietyfloatings.put(totalPosHangamVariety+"-"+(i*2), " ");
			}
			hangamVarietyNond.setFloatings(hangamVarietyfloatings);
			
			hangamVarietyNondTableData.add(hangamVarietyNondRowTotal);
			
			rowColHangamVarietyNond.put(totalPosHangamVariety+"-0", 2);
			hangamVarietyNond.setRowColSpan(rowColHangamVarietyNond);
			hangamVarietyNond.setBoldIndicator(boldIndicator);
			
			hangamVarietyNond.setTableData(hangamVarietyNondTableData);
			
			agriReportRes.setHangamVarietyNond(hangamVarietyNond);
		} catch (Exception e) {
			agriReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Agri Report Error " + e.getMessage());
			agriReportRes.setSe(error);
			e.printStackTrace();
		}
		return agriReportRes;
	}

	public CrushingPlantHarvVillResponse generateVillTonnage(String date, String chit_boy_id, long sectionCode,
			CrushingPlantHarvVillResponse crushingReportRes, Connection conn) {
		try {
			TableReportBean villageTonnage = new TableReportBean();
			int noofheadDailyCrush =1;
			villageTonnage.setNoofHeads(noofheadDailyCrush);
			villageTonnage.setFooter(true);
			villageTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColvillageTonnage = new HashMap<>();
			
			HashMap<String, String> hanagmfloatings=new HashMap<>();
			hanagmfloatings.put("*-2", ".000");
			hanagmfloatings.put("*-3", ".000");
			villageTonnage.setFloatings(hanagmfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> villageTonnageTableData = new ArrayList<>();
			ArrayList<String> villageTonnageRow = new ArrayList<>();
			villageTonnageRow.add(ConstantVeriables.srno);
			villageTonnageRow.add(ConstantVeriables.village);
			villageTonnageRow.add(ConstantVeriables.today);
			villageTonnageRow.add(ConstantVeriables.todate);
			villageTonnageTableData.add(villageTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double todayTot = 0, todateTot = 0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id, TO_NCHAR(t.village_name_local) as vvillage_name, t.nnet_weight_today, t.nnet_weight_todate from Md_v_Village_Tonnage_1 t where t.nsection_id_main=? ORDER BY t.nvillage_id")){
				pst.setLong(1, sectionCode);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						villageTonnageRow = new ArrayList<>();
						villageTonnageRow.add(String.valueOf(srno++));
						villageTonnageRow.add(DemoConvert2.ism_to_uni(rs.getString("vvillage_name")));
						villageTonnageRow.add(rs.getString("nnet_weight_today"));
						villageTonnageRow.add(rs.getString("nnet_weight_todate"));
						villageTonnageTableData.add(villageTonnageRow);
						todayTot+=rs.getDouble("nnet_weight_today");
						todateTot+=rs.getDouble("nnet_weight_todate");
					}
				}
			}
			int totalPosVillage = villageTonnageTableData.size();
			boldIndicator.put(totalPosVillage+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.000");
			villageTonnageRow = new ArrayList<>();
			villageTonnageRow.add(ConstantVeriables.total);
			villageTonnageRow.add(df.format(todayTot));
			villageTonnageRow.add(df.format(todateTot));
			villageTonnageTableData.add(villageTonnageRow);
			
			rowColvillageTonnage.put(totalPosVillage+"-0", 2);
			villageTonnage.setRowColSpan(rowColvillageTonnage);
			villageTonnage.setBoldIndicator(boldIndicator);
			
			villageTonnage.setTableData(villageTonnageTableData);
			
			crushingReportRes.setVillageTonnage(villageTonnage);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;
		
	}

	public CrushingReportReponse generateRemainCane(String date, String chit_boy_id,
			CrushingReportReponse crushingReportRes, Connection conn) {
		try {
			TableReportBean nondAndExpTonnage = new TableReportBean();
			int noofheadDailyCrush =2;
			nondAndExpTonnage.setNoofHeads(noofheadDailyCrush);
			nondAndExpTonnage.setFooter(true);
			nondAndExpTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColnondAndExpTonnage = new HashMap<>();
			
			HashMap<String, String> sectionfloatings=new HashMap<>();
			sectionfloatings.put("*-2", ".00");
			sectionfloatings.put("*-3", " ");
			sectionfloatings.put("*-4", ".00");
			sectionfloatings.put("*-5", " ");
			sectionfloatings.put("*-6", ".00");
			sectionfloatings.put("*-7", " ");
			sectionfloatings.put("*-8", ".00");
			sectionfloatings.put("*-9", " ");
			sectionfloatings.put("*-10", ".00");
			sectionfloatings.put("*-11", " ");
			sectionfloatings.put("*-12", ".00");
			sectionfloatings.put("*-13", " ");
			
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> nondAndExpTonnageTableData = new ArrayList<>();
			ArrayList<String> nondAndExpTonnageRow = new ArrayList<>();
			
			nondAndExpTonnageRow.add(ConstantVeriables.details + ConstantVeriables.rightArrow);
			nondAndExpTonnageRow.add(ConstantVeriables.actualnond);
			nondAndExpTonnageRow.add(ConstantVeriables.crushingDone);
			nondAndExpTonnageRow.add(ConstantVeriables.otherFactoryCrushing);
			nondAndExpTonnageRow.add(ConstantVeriables.forage);
			nondAndExpTonnageRow.add(ConstantVeriables.sugarBeans);
			nondAndExpTonnageRow.add(ConstantVeriables.totalRemaing);
			rowColnondAndExpTonnage.put("0-0", 2);
			rowColnondAndExpTonnage.put("0-1", 2);
			rowColnondAndExpTonnage.put("0-2", 2);
			rowColnondAndExpTonnage.put("0-3", 2);
			rowColnondAndExpTonnage.put("0-4", 2);
			rowColnondAndExpTonnage.put("0-5", 2);
			rowColnondAndExpTonnage.put("0-6", 2);
			
			nondAndExpTonnageTableData.add(nondAndExpTonnageRow);
			
			nondAndExpTonnageRow = new ArrayList<>();
			nondAndExpTonnageRow.add(ConstantVeriables.srno);
			nondAndExpTonnageRow.add(ConstantVeriables.section);
			nondAndExpTonnageRow.add(ConstantVeriables.area);
			nondAndExpTonnageRow.add(ConstantVeriables.expectedTonnage);
			nondAndExpTonnageRow.add(ConstantVeriables.area);
			nondAndExpTonnageRow.add(ConstantVeriables.receivedTonnage);
			nondAndExpTonnageRow.add(ConstantVeriables.area);
			nondAndExpTonnageRow.add(ConstantVeriables.tonnage);
			nondAndExpTonnageRow.add(ConstantVeriables.area);
			nondAndExpTonnageRow.add(ConstantVeriables.tonnage);
			nondAndExpTonnageRow.add(ConstantVeriables.area);
			nondAndExpTonnageRow.add(ConstantVeriables.tonnage);
			nondAndExpTonnageRow.add(ConstantVeriables.area);
			nondAndExpTonnageRow.add(ConstantVeriables.tonnage);
			
			nondAndExpTonnageTableData.add(nondAndExpTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double narea = 0, nexpected_yield = 0, narea_harvested = 0, nnet_weight = 0, narea_ofc = 0, nnet_weight_ofc = 0, narea_chara = 0, nnet_weight_chara = 0, narea_bene = 0, nnet_weight_bene = 0, narea_remain = 0, nnet_weight_remain = 0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id_main, t.vsection_name_local_main, t.narea, t.nexpected_yield, t.narea_harvested, t.nnet_weight, t.narea_ofc, t.nnet_weight_ofc, t.narea_chara, t.nnet_weight_chara, t.narea_bene, t.nnet_weight_bene from md_v_cane_balance_1 t")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						
						double row_narea=rs.getDouble("narea");
						double row_nexpected_yield=rs.getDouble("nexpected_yield");
						double row_narea_harvested=rs.getDouble("narea_harvested");
						double row_nnet_weight=rs.getDouble("nnet_weight");
						double row_narea_ofc=rs.getDouble("narea_ofc");
						double row_nnet_weight_ofc=rs.getDouble("nnet_weight_ofc");
						double row_narea_chara=rs.getDouble("narea_chara");
						double row_nnet_weight_chara=rs.getDouble("nnet_weight_chara");
						double row_narea_bene=rs.getDouble("narea_bene");
						double row_nnet_weight_bene=rs.getDouble("nnet_weight_bene");
						
						double row_narea_remain=row_narea-row_narea_harvested-row_narea_ofc-row_narea_chara-row_narea_bene;
						double row_nnet_weight_remain=row_nexpected_yield-row_nnet_weight-row_nnet_weight_ofc-row_nnet_weight_chara-row_nnet_weight_bene;
						
						
						narea+=row_narea;
						nexpected_yield+=row_nexpected_yield;
						narea_harvested+=row_narea_harvested;
						nnet_weight+=row_nnet_weight;
						narea_ofc+=row_narea_ofc;
						nnet_weight_ofc+=row_nnet_weight_ofc;
						narea_chara+=row_narea_chara;
						nnet_weight_chara+=row_nnet_weight_chara;
						narea_bene+=row_narea_bene;
						nnet_weight_bene+=row_nnet_weight_bene;
						narea_remain+=row_narea_remain;
						nnet_weight_remain+=row_nnet_weight_remain;
						
						nondAndExpTonnageRow = new ArrayList<>();
						nondAndExpTonnageRow.add(String.valueOf(srno++));
						nondAndExpTonnageRow.add(DemoConvert2.ism_to_uni(rs.getString("vsection_name_local_main")));// Added alt+0160
						nondAndExpTonnageRow.add(String.valueOf(row_narea));
						nondAndExpTonnageRow.add(String.valueOf(row_nexpected_yield));
						nondAndExpTonnageRow.add(String.valueOf(row_narea_harvested));
						nondAndExpTonnageRow.add(String.valueOf(row_nnet_weight));
						nondAndExpTonnageRow.add(String.valueOf(row_narea_ofc));
						nondAndExpTonnageRow.add(String.valueOf(row_nnet_weight_ofc));
						nondAndExpTonnageRow.add(String.valueOf(row_narea_chara));
						nondAndExpTonnageRow.add(String.valueOf(row_nnet_weight_chara));
						nondAndExpTonnageRow.add(String.valueOf(row_narea_bene));
						nondAndExpTonnageRow.add(String.valueOf(row_nnet_weight_bene));
						nondAndExpTonnageRow.add(String.valueOf(row_narea_remain));
						nondAndExpTonnageRow.add(String.valueOf(row_nnet_weight_remain));
						nondAndExpTonnageTableData.add(nondAndExpTonnageRow);
						
						
					}
				}
			}
			int totalPosSection = nondAndExpTonnageTableData.size();
			boldIndicator.put(totalPosSection+"-*", true);
			sectionfloatings.put(totalPosSection+"-1", ".00");
			sectionfloatings.put(totalPosSection+"-2", " ");
			sectionfloatings.put(totalPosSection+"-3", ".00");
			sectionfloatings.put(totalPosSection+"-4", " ");
			sectionfloatings.put(totalPosSection+"-5", ".00");
			sectionfloatings.put(totalPosSection+"-6", " ");
			sectionfloatings.put(totalPosSection+"-7", ".00");
			sectionfloatings.put(totalPosSection+"-8", " ");
			sectionfloatings.put(totalPosSection+"-9", ".00");
			sectionfloatings.put(totalPosSection+"-10", " ");
			sectionfloatings.put(totalPosSection+"-11", ".00");
			sectionfloatings.put(totalPosSection+"-12", " ");
			nondAndExpTonnage.setFloatings(sectionfloatings);
			DecimalFormat df = new DecimalFormat("#0.000");
			nondAndExpTonnageRow = new ArrayList<>();
			nondAndExpTonnageRow.add(ConstantVeriables.total);
			nondAndExpTonnageRow.add(df.format(narea));
			nondAndExpTonnageRow.add(df.format(nexpected_yield));
			nondAndExpTonnageRow.add(df.format(narea_harvested));
			nondAndExpTonnageRow.add(df.format(nnet_weight));
			nondAndExpTonnageRow.add(df.format(narea_ofc));
			nondAndExpTonnageRow.add(df.format(nnet_weight_ofc));
			nondAndExpTonnageRow.add(df.format(narea_chara));
			nondAndExpTonnageRow.add(df.format(nnet_weight_chara));
			nondAndExpTonnageRow.add(df.format(narea_bene));
			nondAndExpTonnageRow.add(df.format(nnet_weight_bene));
			nondAndExpTonnageRow.add(df.format(narea_remain));
			nondAndExpTonnageRow.add(df.format(nnet_weight_remain));
			nondAndExpTonnageTableData.add(nondAndExpTonnageRow);
			
			rowColnondAndExpTonnage.put(totalPosSection+"-0", 2);
			nondAndExpTonnage.setRowColSpan(rowColnondAndExpTonnage);
			nondAndExpTonnage.setBoldIndicator(boldIndicator);
			
			nondAndExpTonnage.setTableData(nondAndExpTonnageTableData);
			
			crushingReportRes.setRemainCane(nondAndExpTonnage);
		} catch (Exception e) {
			crushingReportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			crushingReportRes.setSe(error);
			e.printStackTrace();
		}
		return crushingReportRes;
	}

}
