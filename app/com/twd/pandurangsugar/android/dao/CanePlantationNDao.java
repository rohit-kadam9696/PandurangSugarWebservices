package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.EmpVillResponse;
import com.twd.pandurangsugar.android.bean.NondReportHangamReponse;
import com.twd.pandurangsugar.android.bean.NondReportReponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableReportBean;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CanePlantationNDao {

	public NondReportReponse plantSummary(String villageId, String vyearCode,
			NondReportReponse reqResponse, Connection conn) {
		try {
			TableReportBean nondSummary = new TableReportBean();
			int noofheadDailyCrush =1;
			nondSummary.setNoofHeads(noofheadDailyCrush);
			nondSummary.setFooter(true);
			nondSummary.setMarathi(true);
			
			HashMap<String, Integer> rowColvarietyTonnage = new HashMap<>();
			
			HashMap<String, String> varietyfloatings=new HashMap<>();
			varietyfloatings.put("*-2", ".00");
			varietyfloatings.put("*-3", ".00");
			varietyfloatings.put("*-4", ".00");
			varietyfloatings.put("*-5", ".00");
			varietyfloatings.put("*-6", ".00");
			varietyfloatings.put("*-7", " ");
			nondSummary.setFloatings(varietyfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();
			ArrayList<String> tableRow = new ArrayList<>();
			tableRow.add(ConstantVeriables.srno);
			tableRow.add(ConstantVeriables.village);
			tableRow.add(ConstantVeriables.nondarea);
			tableRow.add(ConstantVeriables.rujuwatpending);
			tableRow.add(ConstantVeriables.modtodarea);
			tableRow.add(ConstantVeriables.vadhalelarea);
			tableRow.add(ConstantVeriables.cutarea);
			tableRow.add(ConstantVeriables.aprotommage);	
			tableData.add(tableRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double totalplantArea = 0, totalpendingArea = 0,totalmodelelArea=0,totalvadhalelArea=0,totalexpendedYield=0,totaltutnareArea=0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,v.village_name_local,sum(t.narea) plantArea, sum(case when t.nconfirm_flag = 0 then t.narea else 0 end) pendingArea, sum(case when t.nconfirm_flag <> 0 and t.narea > t.ntentative_area then t.narea-t.ntentative_area else 0 end) modelelArea, sum(case when t.nconfirm_flag <> 0 and t.narea < t.ntentative_area then t.narea-t.ntentative_area else 0 end) vadhalelArea, sum(case when t.nconfirm_flag <> 0 then t.ntentative_area else 0 end) tutnareArea, sum(case when t.nconfirm_flag <> 0 then t.nexpected_yield else 0 end) expendedYield from CR_T_PLANTATION t, GM_M_VILLAGE_MASTER v where t.nvillage_id = v.nvillage_id and t.vyear_id = ? and t.nvillage_id = ? group by t.nvillage_id,v.village_name_local order by t.nvillage_id,v.village_name_local")){
				int i=1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						tableRow = new ArrayList<>();
						tableRow.add(String.valueOf(srno++));
						tableRow.add(DemoConvert2.ism_to_uni(rs.getString("village_name_local"))+" ");// Added alt+0160
						tableRow.add(rs.getString("plantArea"));
						tableRow.add(rs.getString("pendingArea"));
						tableRow.add(rs.getString("modelelArea"));
						tableRow.add(rs.getString("vadhalelArea"));
						tableRow.add(rs.getString("tutnareArea"));
						tableRow.add(rs.getString("expendedYield"));
						tableData.add(tableRow);
						totalplantArea+=rs.getDouble("plantArea");
						totalpendingArea+=rs.getDouble("pendingArea");
						totalmodelelArea+=rs.getDouble("modelelArea");
						totalvadhalelArea+=rs.getDouble("vadhalelArea");
						totaltutnareArea+=rs.getDouble("tutnareArea");
						totalexpendedYield+=rs.getDouble("expendedYield");
					}
				}
			}
			int totalPosDailyCrush = tableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.00");
			tableRow = new ArrayList<>();
			tableRow.add(ConstantVeriables.total);
			tableRow.add(df.format(totalplantArea));
			tableRow.add(df.format(totalpendingArea));
			tableRow.add(df.format(totalmodelelArea));
			tableRow.add(df.format(totalvadhalelArea));
			tableRow.add(df.format(totaltutnareArea));
			tableRow.add(String.valueOf(totalexpendedYield));
			tableData.add(tableRow);
			
			rowColvarietyTonnage.put(totalPosDailyCrush+"-0", 2);
			nondSummary.setRowColSpan(rowColvarietyTonnage);
			nondSummary.setBoldIndicator(boldIndicator);
			nondSummary.setColWidth(new Integer[]{6,16,13,13,13,13,13,13});
			nondSummary.setTableData(tableData);
			
			reqResponse.setNondSummary(nondSummary);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
		
	}

	public NondReportReponse hangamSummary(String villageId, String vyearCode, NondReportReponse reqResponse,
			Connection conn) {
		
		try {
			TableReportBean hangamSummary = new TableReportBean();
			int noofhead =1;
			hangamSummary.setNoofHeads(noofhead);
			hangamSummary.setFooter(true);
			hangamSummary.setMarathi(true);
			
			HashMap<String, Integer> rowColhangamsummary = new HashMap<>();
			
			HashMap<String, String> hangamsummaryfloatings=new HashMap<>();
			hangamsummaryfloatings.put("*-2", ".00");
			hangamsummaryfloatings.put("*-3", ".00");
			hangamsummaryfloatings.put("*-4", " ");
			hangamSummary.setFloatings(hangamsummaryfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();
			ArrayList<String> tableRow = new ArrayList<>();
			tableRow.add(ConstantVeriables.srno);
			tableRow.add(ConstantVeriables.hangam);
			tableRow.add(ConstantVeriables.nondarea);		
			tableRow.add(ConstantVeriables.cutarea);
			tableRow.add(ConstantVeriables.aprotommage);	
			tableData.add(tableRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double totalplantArea = 0,totalexpendedYield=0,totaltutnareArea=0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,v.village_name_local,t.nhangam_id,h.vhangam_name,sum(t.narea) as plantArea,sum(t.ntentative_area) as tutnareArea ,sum(t.nexpected_yield) as expendedYield from CR_T_PLANTATION t, GM_M_VILLAGE_MASTER v, CR_M_HANGAM_MASTER h where t.nvillage_id = v.nvillage_id and t.nhangam_id = h.nhangam_id  and t.vyear_id = ? and t.nvillage_id = ? group by t.nvillage_id,v.village_name_local,t.nhangam_id,h.vhangam_name order by t.nvillage_id,v.village_name_local,t.nhangam_id,h.vhangam_name ")){
				int i=1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						tableRow = new ArrayList<>();
						tableRow.add(String.valueOf(srno++));
						tableRow.add(DemoConvert2.ism_to_uni(rs.getString("vhangam_name"))+" ");// Added alt+0160
						tableRow.add(rs.getString("plantArea"));						
						tableRow.add(rs.getString("tutnareArea"));
						tableRow.add(rs.getString("expendedYield"));
						tableData.add(tableRow);
						totalplantArea+=rs.getDouble("plantArea");
						totaltutnareArea+=rs.getDouble("tutnareArea");
						totalexpendedYield+=rs.getDouble("expendedYield");
					}
				}
			}
			int totalPosDailyCrush = tableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.00");
			tableRow = new ArrayList<>();
			tableRow.add(ConstantVeriables.total);
			tableRow.add(df.format(totalplantArea));
			tableRow.add(df.format(totaltutnareArea));
			tableRow.add(String.valueOf(totalexpendedYield));
			tableData.add(tableRow);
			
			rowColhangamsummary.put(totalPosDailyCrush+"-0", 2);
			hangamSummary.setRowColSpan(rowColhangamsummary);
			hangamSummary.setBoldIndicator(boldIndicator);
			hangamSummary.setColWidth(new Integer[] { 10, 30, 20, 20, 20 });
			hangamSummary.setTableData(tableData);
			
			reqResponse.setHangamSummary(hangamSummary);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;  
	}

	public NondReportReponse varietySummary(String villageId, String vyearCode, NondReportReponse reqResponse,
			Connection conn) {

		try {
			TableReportBean hangamSummary = new TableReportBean();
			int noofhead =1;
			hangamSummary.setNoofHeads(noofhead);
			hangamSummary.setFooter(true);
			hangamSummary.setMarathi(true);
			
			HashMap<String, Integer> rowColhangamsummary = new HashMap<>();
			
			HashMap<String, String> hangamsummaryfloatings=new HashMap<>();
			hangamsummaryfloatings.put("*-2", ".00");
			hangamsummaryfloatings.put("*-3", ".00");
			hangamsummaryfloatings.put("*-3", " ");
			hangamSummary.setFloatings(hangamsummaryfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();
			ArrayList<String> tableRow = new ArrayList<>();
			tableRow.add(ConstantVeriables.srno);
			tableRow.add(ConstantVeriables.variety);
			tableRow.add(ConstantVeriables.nondarea);		
			tableRow.add(ConstantVeriables.cutarea);
			tableRow.add(ConstantVeriables.aprotommage);	
			tableData.add(tableRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			double totalplantArea = 0,totalexpendedYield=0,totaltutnareArea=0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,v.village_name_local,t.nvariety_id,a.vvariety_name,sum(t.narea) as plantArea,sum(t.ntentative_area) as tutnareArea,sum(t.nexpected_yield) as expendedYield from CR_T_PLANTATION t, GM_M_VILLAGE_MASTER v, CR_M_VARIETY_MASTER a where t.nvillage_id = v.nvillage_id and t.nvariety_id = a.navariety_id and t.vyear_id = ? and t.nvillage_id = ? group by t.nvillage_id,v.village_name_local,t.nvariety_id,a.vvariety_name order by t.nvillage_id,v.village_name_local,t.nvariety_id,a.vvariety_name")){
				int i=1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						tableRow = new ArrayList<>();
						tableRow.add(String.valueOf(srno++));
						tableRow.add(DemoConvert2.ism_to_uni(rs.getString("vvariety_name"))+" ");// Added alt+0160
						tableRow.add(rs.getString("plantArea"));						
						tableRow.add(rs.getString("tutnareArea"));
						tableRow.add(rs.getString("expendedYield"));
						tableData.add(tableRow);
						totalplantArea+=rs.getDouble("plantArea");
						totaltutnareArea+=rs.getDouble("tutnareArea");
						totalexpendedYield+=rs.getDouble("expendedYield");
					}
				}
			}
			int totalPosDailyCrush = tableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.00");
			DecimalFormat dfTon = new DecimalFormat("#0");
			tableRow = new ArrayList<>();
			tableRow.add(ConstantVeriables.total);
			tableRow.add(df.format(totalplantArea));
			tableRow.add(df.format(totaltutnareArea));
			tableRow.add(dfTon.format(totalexpendedYield));
			tableData.add(tableRow);
			
			rowColhangamsummary.put(totalPosDailyCrush+"-0", 2);
			hangamSummary.setRowColSpan(rowColhangamsummary);
			hangamSummary.setBoldIndicator(boldIndicator);
			hangamSummary.setColWidth(new Integer[] { 10, 30, 20, 20, 20 });
			hangamSummary.setTableData(tableData);
			
			reqResponse.setVarietySummary(hangamSummary);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;  
	}

	public NondReportHangamReponse monthvarietySummary(String villageId, String vyearCode, String hangamId,
			NondReportHangamReponse reqResponse, Connection conn) {
		
		try {
			TableReportBean cropTypeTonnage = new TableReportBean();
			int noofheadDailyCrush =2;
			cropTypeTonnage.setNoofHeads(noofheadDailyCrush);
			cropTypeTonnage.setFooter(true);
			cropTypeTonnage.setMarathi(true);
			
			HashMap<String, Integer> rowColCropTypeTonnage = new HashMap<>();
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> cropTypeTableData = new ArrayList<>();
			ArrayList<String> cropTypeRow = new ArrayList<>();

			cropTypeRow.add(ConstantVeriables.month + ConstantVeriables.rightArrow);//1
			cropTypeRow.add(ConstantVeriables.n6 + ConstantVeriables.wa); //3
			cropTypeRow.add(ConstantVeriables.n7 + ConstantVeriables.wa); //5
			cropTypeRow.add(ConstantVeriables.n8 + ConstantVeriables.wa); //7
			cropTypeRow.add(ConstantVeriables.n9 + ConstantVeriables.wa); //9
			cropTypeRow.add(ConstantVeriables.n10 + ConstantVeriables.wa); //11
			cropTypeRow.add(ConstantVeriables.n11 + ConstantVeriables.wa); //13
			cropTypeRow.add(ConstantVeriables.n12 + ConstantVeriables.wa); //15
			cropTypeRow.add(ConstantVeriables.n1 + ConstantVeriables.la); //17
			cropTypeRow.add(ConstantVeriables.n2 + ConstantVeriables.ra); //19
			cropTypeRow.add(ConstantVeriables.n3 + ConstantVeriables.ra); //21
			cropTypeRow.add(ConstantVeriables.n4 + ConstantVeriables.tha); //23
			cropTypeRow.add(ConstantVeriables.n5 + ConstantVeriables.wa); //25
			cropTypeRow.add(ConstantVeriables.total); //27
			rowColCropTypeTonnage.put("0-0", 2);
			rowColCropTypeTonnage.put("0-1", 2);
			rowColCropTypeTonnage.put("0-2", 2);
			rowColCropTypeTonnage.put("0-3", 2);
			rowColCropTypeTonnage.put("0-4", 2);
			rowColCropTypeTonnage.put("0-5", 2);
			rowColCropTypeTonnage.put("0-6", 2);
			rowColCropTypeTonnage.put("0-7", 2);
			rowColCropTypeTonnage.put("0-8", 2);
			rowColCropTypeTonnage.put("0-9", 2);
			rowColCropTypeTonnage.put("0-10", 2);
			rowColCropTypeTonnage.put("0-11", 2);
			rowColCropTypeTonnage.put("0-12", 2);
			rowColCropTypeTonnage.put("0-13", 2);
			
			cropTypeTableData.add(cropTypeRow);
			
			JSONObject jobMonthPos = new JSONObject();
			jobMonthPos.put("06", 3);
			jobMonthPos.put("07", 5);
			jobMonthPos.put("08", 7);
			jobMonthPos.put("09", 9);
			jobMonthPos.put("10", 11);
			jobMonthPos.put("11", 13);
			jobMonthPos.put("12", 15);
			jobMonthPos.put("01", 17);
			jobMonthPos.put("02", 19);
			jobMonthPos.put("03", 21);
			jobMonthPos.put("04", 23);
			jobMonthPos.put("05", 25);
			jobMonthPos.put("tot", 27);
			
			
			cropTypeRow = new ArrayList<>();
			cropTypeRow.add(ConstantVeriables.srno);
			cropTypeRow.add(ConstantVeriables.variety);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			cropTypeRow.add(ConstantVeriables.cutareaShort);
			cropTypeRow.add(ConstantVeriables.expectedTonnage);
			
			cropTypeTableData.add(cropTypeRow);
			
			ArrayList<String> cropTypeRowTotal = new ArrayList<>();
			cropTypeRowTotal.add("");
			cropTypeRowTotal.add(ConstantVeriables.total);
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			cropTypeRowTotal.add("0.00");
			cropTypeRowTotal.add("0");
			int totalCol=4;
			HashMap<String, Boolean> visibility  = new HashMap<>();
			visibility.put("0-0", true);
			visibility.put("0-1", false);
			visibility.put("0-2", false);
			visibility.put("0-3", false);
			visibility.put("0-4", false);
			visibility.put("0-5", false);
			visibility.put("0-6", false);
			visibility.put("0-7", false);
			visibility.put("0-8", false);
			visibility.put("0-9", false);
			visibility.put("0-10", false);
			visibility.put("0-11", false);
			visibility.put("0-12", false);
			visibility.put("0-13", true);
			
			
			visibility.put("*-0", true);
			visibility.put("*-1", true);
			visibility.put("*-2", false);
			visibility.put("*-3", false);
			visibility.put("*-4", false);
			visibility.put("*-5", false);
			visibility.put("*-6", false);
			visibility.put("*-7", false);
			visibility.put("*-8", false);
			visibility.put("*-9", false);
			visibility.put("*-10", false);
			visibility.put("*-11", false);
			visibility.put("*-12", false);
			visibility.put("*-13", false);
			visibility.put("*-14", false);
			visibility.put("*-15", false);
			visibility.put("*-16", false);
			visibility.put("*-17", false);
			visibility.put("*-18", false);
			visibility.put("*-19", false);
			visibility.put("*-20", false);
			visibility.put("*-21", false);
			visibility.put("*-22", false);
			visibility.put("*-23", false);
			visibility.put("*-24", false);
			visibility.put("*-25", false);
			visibility.put("*-26", true);
			visibility.put("*-27", true);
			
			
			boldIndicator.put("0-*", true);
			boldIndicator.put("1-*", true);
			int srno = 1;
			JSONObject varietyPos = new JSONObject();
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,t.nhangam_id, b.navariety_id_main,TO_CHAR(b.vvariety_name_main) as vvariety_name_main, to_char(dplantation_date,'MM') as Plantation_month, sum(case when t.nconfirm_flag <> 0 then t.ntentative_area else t.narea end ) as ntentative_area, sum(t.nexpected_yield) as nexpected_yield from CR_T_PLANTATION t, CR_M_VARIETY_MASTER a, CR_M_VARIETY_MASTER_MAIN b where t.nvariety_id = a.navariety_id and a.navariety_id_main = b.navariety_id_main and t.vyear_id = ? and t.nvillage_id = ? and t.nhangam_id = ? group by t.nvillage_id,t.nhangam_id, b.navariety_id_main,b.vvariety_name_main, to_char(dplantation_date,'MM') order by t.nvillage_id,t.nhangam_id, b.navariety_id_main,b.vvariety_name_main, to_char(dplantation_date,'MM') ")){
				int i = 1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				pst.setString(i++, hangamId);
				
				DecimalFormat df = new DecimalFormat("#0.00");
				DecimalFormat dfTon = new DecimalFormat("#0");
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String navarietyId = rs.getString("navariety_id_main");
						String plantationMonth = rs.getString("Plantation_month");
						if(varietyPos.has(navarietyId)) {
							int myPos = varietyPos.getInt(navarietyId);
							cropTypeRow = cropTypeTableData.get(myPos);
							if (jobMonthPos.has(plantationMonth)) {
								int monthPos = jobMonthPos.getInt(plantationMonth);
								Double ntentativeArea = rs.getDouble("ntentative_area");
								Double nexpectedYield = rs.getDouble("nexpected_yield");
								cropTypeRow.set(monthPos-1, df.format(ntentativeArea));
								cropTypeRow.set(monthPos, dfTon.format(nexpectedYield));
								int totMonthPos = jobMonthPos.getInt("tot");
								cropTypeRow.set(totMonthPos - 1, df.format(Double.parseDouble(cropTypeRow.get(totMonthPos - 1)) + ntentativeArea));
								cropTypeRow.set(totMonthPos, dfTon.format(Double.parseDouble(cropTypeRow.get(totMonthPos)) + nexpectedYield));
								
								cropTypeRowTotal.set(monthPos - 1, df.format(Double.parseDouble(cropTypeRowTotal.get(monthPos - 1)) + ntentativeArea));
								cropTypeRowTotal.set(monthPos, dfTon.format(Double.parseDouble(cropTypeRowTotal.get(monthPos)) + nexpectedYield));
								cropTypeRowTotal.set(totMonthPos - 1, df.format(Double.parseDouble(cropTypeRowTotal.get(totMonthPos - 1)) + ntentativeArea));
								cropTypeRowTotal.set(totMonthPos, dfTon.format(Double.parseDouble(cropTypeRowTotal.get(totMonthPos)) + nexpectedYield));
								
								if(!visibility.get("*-" + (monthPos - 1))) {
									totalCol+=2;
								}
								
								visibility.put("0-" + (int)((monthPos - 1)/2), true);
								visibility.put("*-" + (monthPos - 1), true);
								visibility.put("*-" + monthPos, true);
							}
							cropTypeTableData.set(myPos, cropTypeRow);
						} else {
							cropTypeRow = new ArrayList<>();
							cropTypeRow.add(String.valueOf(srno));
							cropTypeRow.add(DemoConvert2.ism_to_uni(rs.getString("vvariety_name_main")));
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							cropTypeRow.add("0.00");
							cropTypeRow.add("0");
							
							Double ntentativeArea = rs.getDouble("ntentative_area");
							Double nexpectedYield = rs.getDouble("nexpected_yield");
							
							cropTypeRow.add(df.format(ntentativeArea));
							cropTypeRow.add(dfTon.format(nexpectedYield));
							if (jobMonthPos.has(plantationMonth)) {
								int monthPos = jobMonthPos.getInt(plantationMonth);
								cropTypeRow.set(monthPos-1, df.format(ntentativeArea));
								cropTypeRow.set(monthPos, dfTon.format(nexpectedYield));
								
								int totMonthPos = jobMonthPos.getInt("tot");
								cropTypeRowTotal.set(monthPos - 1, df.format(Double.parseDouble(cropTypeRowTotal.get(monthPos - 1)) + ntentativeArea));
								cropTypeRowTotal.set(monthPos, dfTon.format(Double.parseDouble(cropTypeRowTotal.get(monthPos)) + nexpectedYield));
								cropTypeRowTotal.set(totMonthPos - 1, df.format(Double.parseDouble(cropTypeRowTotal.get(totMonthPos - 1)) + ntentativeArea));
								cropTypeRowTotal.set(totMonthPos, dfTon.format(Double.parseDouble(cropTypeRowTotal.get(totMonthPos)) + nexpectedYield));
								
								if(!visibility.get("*-" + (monthPos - 1))) {
									totalCol+=2;
								}
								visibility.put("0-" + (int)((monthPos - 1)/2), true);
								visibility.put("*-" + (monthPos - 1), true);
								visibility.put("*-" + monthPos, true);
								
								
							}
							varietyPos.put(navarietyId, srno+1);
							srno++;
							cropTypeTableData.add(cropTypeRow);
						}
					}
				}
			}
			int totMonthPos = jobMonthPos.getInt("tot");
			boldIndicator.put("*-" + (totMonthPos-1), true);
			
			int lastItemSize = cropTypeTableData.size();
			boldIndicator.put(lastItemSize+"-*", true);
			cropTypeTableData.add(cropTypeRowTotal);
			cropTypeTonnage.setRowColSpan(rowColCropTypeTonnage);
			cropTypeTonnage.setBoldIndicator(boldIndicator);
			cropTypeTonnage.setVisibility(visibility);
			int colSize = 85 / (totalCol - 2);
			int colSizeBal = 85 % (totalCol - 2);
			Integer[] width = new Integer[totalCol];
			width[0] = 5;
			width[1] = 10 + colSizeBal;
			for (int k = 2; k < totalCol; k++) {
				width[k] = colSize;
			}
			cropTypeTonnage.setColWidth(width);
			cropTypeTonnage.setTableData(cropTypeTableData);
			reqResponse.setHangamMonthVarietyNond(cropTypeTonnage);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Crushing Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
	}

	public EmpVillResponse empData(String employeecode, EmpVillResponse reqResponse,
			Connection conn) {
		try {
			TableReportBean empdata = new TableReportBean();
			
			int noofheadDailyCrush =1;
			empdata.setNoofHeads(noofheadDailyCrush);
			empdata.setFooter(true);
			empdata.setMarathi(false);

			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			HashMap<String, Boolean> visibility = new HashMap<>();
			visibility.put("*-2", false);
			visibility.put("*-3", false);
			
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();
			ArrayList<String> tableRow = new ArrayList<>();
			tableRow.add(ConstantVeriables.section);
			tableRow.add(ConstantVeriables.village);
			tableRow.add("U1");
			tableRow.add("U2");
			tableData.add(tableRow);
			
			boldIndicator.put("0-*", true);
			try(PreparedStatement pst=conn.prepareStatement("select TO_NCHAR(t.vfull_name_local) as vfull_name_local from App_m_User_Master t where t.nuser_name=?")){
				int i=1;
				pst.setString(i++, employeecode);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						reqResponse.setEmployeeCode(employeecode);
						reqResponse.setEmployeeName(DemoConvert2.ism_to_uni(rs.getString("vfull_name_local")));
					}
				}
			}
			try(PreparedStatement pst=conn.prepareStatement("select t.napp_user_1, t.napp_user_2, t.nvillage_id, TO_NCHAR(t.village_name_local) as village_name_local, TO_NCHAR(s.vsection_name_local) as vsection_name_local from GM_M_VILLAGE_MASTER t, GM_M_SECTION_MASTER s where t.nsection_id=s.nsection_id and (t.napp_user_1=? or t.napp_user_2=?) order by t.nsection_id, t.nvillage_id asc")){
				int i=1;
				pst.setString(i++, employeecode);
				pst.setString(i++, employeecode);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						tableRow = new ArrayList<>();
						tableRow.add(DemoConvert2.ism_to_uni(rs.getString("vsection_name_local"))+" ");// Added alt+0160
						tableRow.add(DemoConvert2.ism_to_uni(rs.getString("village_name_local"))+" ");// Added alt+0160
						tableRow.add(rs.getString("napp_user_1")==null?"":rs.getString("napp_user_1"));
						tableRow.add(rs.getString("napp_user_2")==null?"":rs.getString("napp_user_2"));
						tableRow.add(rs.getString("nvillage_id"));
						tableData.add(tableRow);
					}
				}
			}
			empdata.setVisibility(visibility);
			empdata.setBoldIndicator(boldIndicator);
			empdata.setTableData(tableData);
			empdata.setFooter(false);
			reqResponse.setVillageList(empdata);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
		
	}

	public EmpVillResponse removeEmpVill(String empcode, String rm1, String rm2, EmpVillResponse reqResponse,
			Connection conn) {
		try {
			if(!rm1.equals("")) {
				try(PreparedStatement pst=conn.prepareStatement("update GM_M_VILLAGE_MASTER set napp_user_1=null where nvillage_id in (" + rm1 + ") and napp_user_1=?")) {
					int i = 1;
					pst.setString(i++, empcode);
					int r = pst.executeUpdate();
					if (r > 0) {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(true);
						reqResponse.setSuccessMsg(ConstantMessage.saveSuccess);
					} else {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(false);
						reqResponse.setFailMsg(ConstantMessage.saveFailed);
					}
				}
			} else {
				reqResponse.setSuccess(true);
				reqResponse.setActionComplete(false);
				reqResponse.setFailMsg(ConstantMessage.saveFailed);
			}
			if(!rm2.equals("")) {
				try(PreparedStatement pst=conn.prepareStatement("update GM_M_VILLAGE_MASTER set napp_user_2=null where nvillage_id in (" + rm2 + ") and napp_user_2=?")) {
					int i = 1;
					pst.setString(i++, empcode);
					int r = pst.executeUpdate();
					if (r > 0) {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(true);
						reqResponse.setSuccessMsg(ConstantMessage.saveSuccess);
					} else {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(false);
						reqResponse.setFailMsg(ConstantMessage.saveFailed);
					}
				}
			}
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("farmer birthDate Update " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
	}

	public EmpVillResponse addEmpVill(String empcode, String ad1, String ad2, EmpVillResponse reqResponse,
			Connection conn) {
		try {
			if(!ad1.equals("")) {
				try(PreparedStatement pst=conn.prepareStatement("update GM_M_VILLAGE_MASTER set napp_user_1=? where nvillage_id in (" + ad1 + ") and napp_user_1 is null")) {
					int i = 1;
					pst.setString(i++, empcode);
					int r = pst.executeUpdate();
					if (r > 0) {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(true);
						reqResponse.setSuccessMsg(ConstantMessage.saveSuccess);
					} else {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(false);
						reqResponse.setFailMsg(ConstantMessage.saveFailed);
					}
				}
			} else {
				reqResponse.setSuccess(true);
				reqResponse.setActionComplete(false);
				reqResponse.setFailMsg(ConstantMessage.saveFailed);
			}
			if(!ad2.equals("")) {
				try(PreparedStatement pst=conn.prepareStatement("update GM_M_VILLAGE_MASTER set napp_user_2=? where nvillage_id in (" + ad2 + ") and napp_user_2 is null")) {
					int i = 1;
					pst.setString(i++, empcode);
					int r = pst.executeUpdate();
					if (r > 0) {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(true);
						reqResponse.setSuccessMsg(ConstantMessage.saveSuccess);
					} else {
						reqResponse.setSuccess(true);
						reqResponse.setActionComplete(false);
						reqResponse.setFailMsg(ConstantMessage.saveFailed);
					}
				}
			}
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("farmer birthDate Update " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
	}

	

}
