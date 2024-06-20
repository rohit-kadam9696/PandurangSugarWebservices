package com.twd.pandurangsugar.android.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.BulluckCartResponse;
import com.twd.pandurangsugar.android.bean.CaneDailyInwardReport;
import com.twd.pandurangsugar.android.bean.CaneDailyInwardReportResponse;
import com.twd.pandurangsugar.android.bean.CaneTransitResponse;
import com.twd.pandurangsugar.android.bean.CloseTransferResponse;
import com.twd.pandurangsugar.android.bean.CompletePlotDetails;
import com.twd.pandurangsugar.android.bean.CompletePlotResponse;
import com.twd.pandurangsugar.android.bean.ExcessTonPlotReq;
import com.twd.pandurangsugar.android.bean.ExcessTonPlotReqDataResponse;
import com.twd.pandurangsugar.android.bean.ExcessTonPlotReqResponse;
import com.twd.pandurangsugar.android.bean.FarmerTonnage;
import com.twd.pandurangsugar.android.bean.FarmerTonnageResponse;
import com.twd.pandurangsugar.android.bean.HarvPlotDetailsResponse;
import com.twd.pandurangsugar.android.bean.HarvReportReponse;
import com.twd.pandurangsugar.android.bean.HarvSlipDetailsResponse;
import com.twd.pandurangsugar.android.bean.HarvestorResponse;
import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NameData;
import com.twd.pandurangsugar.android.bean.NameListResponse;
import com.twd.pandurangsugar.android.bean.OtherUtilizationResponse;
import com.twd.pandurangsugar.android.bean.RemainingSlipBean;
import com.twd.pandurangsugar.android.bean.RemainingSlipResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableReportBean;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.bean.TransporterResponse;
import com.twd.pandurangsugar.android.bean.VillageResonse;
import com.twd.pandurangsugar.android.bean.WireRopeResonse;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;
import com.twd.pandurangsugar.both.constant.QRConstant;

public class CaneHarvestingDao {

	public HarvPlotDetailsResponse findHarvestingPlanStartByYearCode(String yearCode, String plotNo,
			String chit_boy_id, HarvPlotDetailsResponse caneharvPlanResponse, Connection conn) {
		try
		{
			DecimalFormat df=new DecimalFormat("#0.000");
			DecimalFormat df2=new DecimalFormat("#0.00");
			double nnetWeight=0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nlimit_tonnage, t.nlimit_tonnage_extra, t.nplot_no,TO_NCHAR(t.ventity_name_local)as farmer_name, TO_NCHAR(vfarmer_type_name_local) as farmer_type,TO_NCHAR(t.vsection_name_local)as section_name,TO_NCHAR(t.village_name_local)as villeage_name,TO_NCHAR(t.vhangam_name)as hangam_name,TO_NCHAR(t.vvariety_name)as variety_name,t.vsurve_no,t.ntentative_area,t.nexpected_yield,t.ngps_distance,t.dplantation_date,t.nexpected_yield,t.nentity_uni_id,t.nfarmer_type_id,t.nsection_id,t.nvillage_id,t.nvariety_id,t.nhangam_id,t.ndistance,t.napp_user_1,t.napp_user_2 from app_v_harvesting_program t where t.nplot_no = ? and t.vyear_id =?"))
			{
				pst.setString(1, plotNo);
				pst.setString(2, yearCode);
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						boolean isVilleageAssign=false;
						if(!caneharvPlanResponse.getNuserRoleId().equals("113") && !caneharvPlanResponse.getNuserRoleId().equals("114"))
						{
							if(chit_boy_id.equals(rs.getString("napp_user_1")) || chit_boy_id.equals(rs.getString("napp_user_2")))
							{
								isVilleageAssign=true;
							}else
							{
								caneharvPlanResponse.setSuccess(false);
								ServerError error=new ServerError();
								error.setError(ConstantVeriables.ERROR_006);
								error.setMsg(ConstantMessage.villeageNotAssignForWeightSlip);
								caneharvPlanResponse.setSe(error);
								caneharvPlanResponse.setSuccess(false);
							}
						}else
						{
							isVilleageAssign=true;
						}
						if(isVilleageAssign)
						{
							try(PreparedStatement pst2=conn.prepareStatement("select t.nfact_id,TO_NCHAR(t.vfact_name_local)as vname from GM_M_FACTORY_MASTER t where t.nfact_id=1"))
							{
								try(ResultSet rs2=pst2.executeQuery())
								{
									if(rs2.next())
									{
										caneharvPlanResponse.setVfactNameLocal(DemoConvert2.ism_to_uni(rs2.getString("vname")));
										caneharvPlanResponse.setNfactId(rs2.getInt("nfact_id"));
									}
								}
							}
							try(PreparedStatement pst2=conn.prepareStatement("select sum(t.nnet_weight) as nnet_weight from "+ConstantVeriables.weightSlipTableName+" t where t.nplot_no=? and t.vyear_id=? and t.nnet_weight>0"))
							{
								pst2.setString(1, plotNo);
								pst2.setString(2, yearCode);
								try(ResultSet rs2=pst2.executeQuery())
								{
									if(rs2.next())
									{
										nnetWeight=rs2.getDouble("nnet_weight");
									}
								}
							}
							
							double bullTon = 0;
							JSONObject expTonJob = new JSONObject();
							try(PreparedStatement pst2=conn.prepareStatement("select t.nvehicle_type_id, nvl(t.nwirerope_no,0) nwirerope_no, nvl(t.ntailor_front,0) ntailor_front, nvl(t.ntailor_back,0) ntailor_back, t.nexp_harv_tonnage from APP_M_EXPECTED_TON_VEHICLE t"))
							{
								try(ResultSet rs2=pst2.executeQuery())
								{
									while(rs2.next())
									{
										String key =rs2.getString("nvehicle_type_id") + "-" + rs2.getString("nwirerope_no") + "-" + rs2.getString("ntailor_front")+ "-" + rs2.getString("ntailor_back");
										expTonJob.put(key, rs2.getDouble("nexp_harv_tonnage"));
										if(rs2.getString("nvehicle_type_id").equals("3")) {
											bullTon=rs2.getDouble("nexp_harv_tonnage");
										}
									}
								}
							}
							double expTon = 0;
							try(PreparedStatement pst2=conn.prepareStatement("select t.nvehicle_type_id, nvl(t.nwirerope_no,0) nwirerope_no, nvl(t.ntailor_front,0) ntailor_front, nvl(t.ntailor_back,0) ntailor_back from "+ConstantVeriables.weightSlipTableName+" t where t.nplot_no=? and t.vyear_id=? and nvl(t.nnet_weight,0)=0 and t.vactive_dactive='A'"))
							{
								pst2.setString(1, plotNo);
								pst2.setString(2, yearCode);
								try(ResultSet rs2=pst2.executeQuery())
								{
									while(rs2.next())
									{
										String key =rs2.getString("nvehicle_type_id") + "-" + rs2.getString("nwirerope_no") + "-" + rs2.getString("ntailor_front")+ "-" + rs2.getString("ntailor_back");
										if (expTonJob.has(key)) {
											expTon += expTonJob.getDouble(key);
										}
										
									}
								}
							}
							
							nnetWeight+=expTon;
							caneharvPlanResponse.setNplotNo(rs.getInt("nplot_no"));
							caneharvPlanResponse.setOpenIntent(1);
							caneharvPlanResponse.setVyearId(yearCode);
							caneharvPlanResponse.setVentityNameLocal(DemoConvert2.ism_to_uni(rs.getString("farmer_name")));
							caneharvPlanResponse.setVfarmerTypeNameLocal(DemoConvert2.ism_to_uni(rs.getString("farmer_type")));
							caneharvPlanResponse.setVsectionNameLocal(DemoConvert2.ism_to_uni(rs.getString("section_name")));
							caneharvPlanResponse.setVillageNameLocal(DemoConvert2.ism_to_uni(rs.getString("villeage_name")));
							caneharvPlanResponse.setVhangamName(DemoConvert2.ism_to_uni(rs.getString("hangam_name")));
							caneharvPlanResponse.setVvarietyName(DemoConvert2.ism_to_uni(rs.getString("variety_name")));
							caneharvPlanResponse.setVsurvyNo(rs.getString("vsurve_no"));
							caneharvPlanResponse.setNtentativeArea(df2.format(rs.getDouble("ntentative_area")));
							caneharvPlanResponse.setNexpectedYield(df.format(rs.getDouble("nexpected_yield")));
							caneharvPlanResponse.setNgpsDistance(rs.getString("ngps_distance"));
							caneharvPlanResponse.setDplantaitonDate(Constant.DbDateToAppDate(rs.getDate("dplantation_date")));
							double expyld=rs.getDouble("nexpected_yield");
							double nlimit_tonnage;
							double nlimit_tonnage_extra = 0;
							if(rs.getString("nlimit_tonnage")!=null) {
								nlimit_tonnage=rs.getDouble("nlimit_tonnage");
								nlimit_tonnage_extra=rs.getDouble("nlimit_tonnage_extra");
							} else {
								try(PreparedStatement pst2=conn.prepareStatement("select t.nlimit_tonnage, t.nlimit_tonnage_extra from APP_M_HARVESTING_PROGRAM_START t where t.vyear_id=?"))
								{
									pst2.setString(1, yearCode);
									try(ResultSet rs2=pst2.executeQuery())
									{
										if(rs2.next())
										{
											nlimit_tonnage=rs2.getDouble("nlimit_tonnage");
											nlimit_tonnage_extra=rs2.getDouble("nlimit_tonnage_extra");
										} else {
											nlimit_tonnage=1.0;
										}
									}
								}
							}
							expyld=expyld*nlimit_tonnage;
							caneharvPlanResponse.setNextendedTonnage(Constant.decimalFormat(expyld, "000"));
							caneharvPlanResponse.setNallowedTon(Constant.decimalFormat(expyld*nlimit_tonnage_extra, "000"));
							caneharvPlanResponse.setNbalanceTonnage(Constant.decimalFormat(expyld-nnetWeight, "000"));
							caneharvPlanResponse.setNbullckcartLimit(String.valueOf(Math.round((expyld-nnetWeight)/bullTon)));
							if(nnetWeight>expyld)
							{
								//
								//expyld+=totton;
								// select t.nstatus_id from APP_T_EXCESS_TON_PLOT_REQ t where t.vyear_id= and t.nplot_no= and t.nstatus_id<>2
								// 3 message by nstatus_id
							//else intent 2 
								try(PreparedStatement pst1=conn.prepareStatement("select t.nallowed_tonnage, t.nharvested_tonnage from APP_T_EXCESS_TON_PLOT_REQ t where t.nstatus_id=2 and t.nplot_no=? and  t.vyear_id=?"))
								{
									pst1.setString(1, plotNo);
									pst1.setString(2, yearCode);
									try(ResultSet rs1=pst1.executeQuery())
									{
										if(rs1.next())
										{
											expyld=rs1.getDouble("nharvested_tonnage")+rs1.getDouble("nallowed_tonnage");
										}
									}
								}
								caneharvPlanResponse.setNbalanceTonnage(Constant.decimalFormat(expyld-nnetWeight, "000"));
								caneharvPlanResponse.setNbullckcartLimit(String.valueOf(Math.round((expyld-nnetWeight)/bullTon)));
								if(nnetWeight>expyld)
								{
									try(PreparedStatement pst1=conn.prepareStatement("select nstatus_id from APP_T_EXCESS_TON_PLOT_REQ t where t.nstatus_id<>3 AND t.nplot_no=? and  t.vyear_id=?"))
									{
										pst1.setString(1, plotNo);
										pst1.setString(2, yearCode);
										try(ResultSet rs1=pst1.executeQuery())
										{
											if(rs1.next())
											{
												if(rs1.getInt("nstatus_id")==2)
													caneharvPlanResponse.setVstatusMessage(ConstantMessage.tonnageAlreadyAllowed);
												else
													caneharvPlanResponse.setVstatusMessage(ConstantMessage.tonnageEntryPending);
												caneharvPlanResponse.setOpenIntent(3);
												
											}else
											{
												caneharvPlanResponse.setNharvestedTonnage(df.format(nnetWeight));
												caneharvPlanResponse.setOpenIntent(2);
											}
										}
									}
								}
							}
							caneharvPlanResponse.setNentityUniId(rs.getString("nentity_uni_id"));
							if(caneharvPlanResponse.getOpenIntent()==1)
							{
								caneharvPlanResponse.setNfarmerTypeId(rs.getInt("nfarmer_type_id"));
								caneharvPlanResponse.setNsectionId(rs.getInt("nsection_id"));
								caneharvPlanResponse.setNvillageId(rs.getInt("nvillage_id"));
								caneharvPlanResponse.setNvarietyId(rs.getInt("nvariety_id"));
								caneharvPlanResponse.setNhangamId(rs.getInt("nhangam_id"));
								caneharvPlanResponse.setNdistance(rs.getInt("ndistance"));
							}
							caneharvPlanResponse.setSuccess(true);
						}
					}else
					{
						caneharvPlanResponse.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(ConstantMessage.informationNotFound);
						caneharvPlanResponse.setSe(error);
						caneharvPlanResponse.setSuccess(false);
					}
				}
			}
		}
		 catch (Exception e) {
			 	caneharvPlanResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("fetch By Plot No "+e.getMessage());
				caneharvPlanResponse.setSe(error);
				e.printStackTrace();
		}
		return caneharvPlanResponse;
	}

	public HashMap<String,Set<String>> isWeightSlipExit(String transCode, String vehicleType, String yearCode, String nslipNo, Connection conn) {
		HashMap<String,Set<String>>list=new HashMap<>();
		String sql="select t.nwirerope_no,t.ntailor_front,t.ntailor_back from "+ConstantVeriables.weightSlipTableName+" t where nvl(t.ngross_weight,0)=0 and t.vactive_dactive='A' and t.ntransportor_id=? and t.nvehicle_type_id=? and t.vyear_id=? ";
		if(nslipNo!=null && !nslipNo.trim().isEmpty())
			sql+="  and t.nslip_no <> ? ";
		try(PreparedStatement pst=conn.prepareStatement(sql))
		{
			String wirerope =ConstantVeriables.wire;
			String frontTailer = ConstantVeriables.front;
			String backTailer = ConstantVeriables.back;
			int i=1;
			pst.setString(i++, "T"+transCode);
			pst.setString(i++, vehicleType);
			pst.setString(i++, yearCode);
			if(nslipNo!=null && !nslipNo.trim().isEmpty())
				pst.setString(i++, nslipNo);
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					Set<String> wireroptSet = null;
					if(list.containsKey(wirerope))
						wireroptSet = list.get(wirerope);
					else
						wireroptSet = new TreeSet<>();
					String wireropeValue= rs.getString("nwirerope_no");
					if(wireropeValue!=null && !wireropeValue.equals("0") && !wireropeValue.equals("5")) {
						wireroptSet.add(wireropeValue);
						if(wireropeValue.equals("1")) {
							wireroptSet.add("all");
						} else {
							wireroptSet.add("1");
						}
						list.put(wirerope, wireroptSet);
					}
					
					Set<String> frontSet = null;
					if(list.containsKey(frontTailer)) 
						frontSet = list.get(frontTailer);
					else
						frontSet = new TreeSet<>();
					String frontValue= rs.getString("ntailor_front");
					if(frontValue!=null && !frontValue.equals("0") && !frontValue.equals("5")) {
						frontSet.add(frontValue);
						if(frontValue.equals("1")) {
							frontSet.add("all");
						} else {
							frontSet.add("1");
						}
						list.put(frontTailer, frontSet);
					}
					
					Set<String> backSet = null;
					if(list.containsKey(backTailer)) 
						backSet = list.get(backTailer);
					else 
						backSet = new TreeSet<>();
					
					String backValue= rs.getString("ntailor_back");
					if(backValue!=null && !backValue.equals("0") && !backValue.equals("5")) {
						backSet.add(backValue);
						if(backValue.equals("1")) {
							backSet.add("all");
						} else {
							backSet.add("1");
						}
						list.put(backTailer, backSet);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
	}
		return list;
	}

	public TransporterResponse findTranspoterByCode(String transCode, String vehicleType, String yearCode, boolean prefix, TransporterResponse caneTransRes, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.vvehicle_no,t.nentity_uni_id,TO_NCHAR(t.ventity_name_local) as ventity_name_local from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_uni_id=?  and t.vblocked = 'N' and t.nvehicle_type_id = ? "))
		{
			int i=1;
			pst.setString(i++, (prefix?"T":"")+transCode);
			pst.setString(i++, vehicleType);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					caneTransRes.setSuccess(true);
					caneTransRes.setCode(rs.getString("nentity_uni_id"));
					caneTransRes.setName(DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
					caneTransRes.setVehicalNo(rs.getString("vvehicle_no"));
				}else
				{
					caneTransRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.transpoterInformationNotFound);
					caneTransRes.setSe(error);
				}
			}
		} catch (Exception e) {
			caneTransRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("fetch transpoter info "+e.getMessage());
			caneTransRes.setSe(error);
			e.printStackTrace();
	}
		return caneTransRes;
	}
	
	public int holdTime(String vehicleType, Connection conn) {
		int time = 0;
		try(PreparedStatement pst=conn.prepareStatement("select t.nminutes from APP_M_SLIP_RSTRN_TIME t where t.nvehicle_type_id=?"))
		{
			int i = 1;
			pst.setString(i++, vehicleType);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					time = rs.getInt("nminutes");
				} else {
					return 0;
				}
			}
		} catch (Exception e) {
			time = -1;
			e.printStackTrace();
		}
		return time;
	}

	public WireRopeResonse getWireropeList(String vehicleType,int wireropeno,int fronttailer,int backtailer,WireRopeResonse caneTransRes, Connection conn) {
		try{
		String sql="select t.nwirerope_id as code,t.vwire_name as name from WB_M_WIREROPE_MASTER t order by t.nwirerope_id ASC";
		if(vehicleType.equals("2") || vehicleType.equals("4")) {
			sql="select t.ntailor_remark_id as code,t. vremark_name as name from WB_M_TAILOR_MASTER t ";
			if(vehicleType.equals("4")) {
				sql+=" where t.vactive_bajat='Y' ";
			}
			sql+= "order by t.ntailor_remark_id asc";
		}
		try(PreparedStatement pst1=conn.prepareStatement(sql)){
                try(ResultSet rs=pst1.executeQuery())
                {
                	ArrayList<KeyPairBoolData> list=new  ArrayList<>();
                	ArrayList<KeyPairBoolData> list2=new  ArrayList<>();
                	while(rs.next())
                	{
                		KeyPairBoolData pbd=new KeyPairBoolData();
                		pbd.setId(rs.getInt("code"));
                		pbd.setName(DemoConvert2.ism_to_uni(rs.getString("name")));
                		if(wireropeno!=0) {
                			pbd.setSelected(rs.getInt("code")==wireropeno);
                		} else if(vehicleType.equals("2") || vehicleType.equals("4")) {
                			KeyPairBoolData pbdback  = new KeyPairBoolData(pbd);
                			pbdback.setSelected(rs.getInt("code")==backtailer);
                			list2.add(pbdback);
                			pbd.setSelected(rs.getInt("code")==fronttailer);
                		}
                		list.add(pbd);
                	}
                	if(vehicleType.equals("2") || vehicleType.equals("4"))
                	{
                		caneTransRes.setWireropefront(list);
                		caneTransRes.setWireropeback(list2);
                	}else
                	{
                		caneTransRes.setWirerope(list);
                	}
                }
		}
		
		sql="select t.nwirerope_remark,t.nseq_weight from WB_M_UHF_CARD_COMBINATION t where t.ntailor_front_remark=0 and t.ntailor_back_remark=0";
		if(vehicleType.equals("2") || vehicleType.equals("4"))
			sql="select t.ntailor_front_remark,t.ntailor_back_remark,t.nseq_weight from WB_M_UHF_CARD_COMBINATION t where t.nwirerope_remark=0";
		try(PreparedStatement pst1=conn.prepareStatement(sql)){
                try(ResultSet rs=pst1.executeQuery())
                {
                	HashMap<String, String> data=new HashMap<>();
                	while(rs.next())
                	{
                		if(vehicleType.equals("2") || vehicleType.equals("4"))
                			data.put(rs.getString("ntailor_front_remark")+","+rs.getString("ntailor_back_remark"), rs.getString("nseq_weight"));
                		else
                			data.put(rs.getString("nwirerope_remark"), rs.getString("nseq_weight"));
                	}
                	caneTransRes.setCombos(data);
                }
		}
		} catch (Exception e) {
			caneTransRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("fetch wirerope info "+e.getMessage());
			caneTransRes.setSe(error);
			e.printStackTrace();
	}
		return caneTransRes;
	
	}

	public HarvestorResponse findHarvestingInfoByCode(String yearCode, String harvCode,
			 boolean prefix, HarvestorResponse caneharvPlanRes, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.ngadi_doki,t.nentity_uni_id,TO_NCHAR(t.ventity_name_local) as ventity_name_local,TO_NCHAR(t1.vgadi_doki_name) as doki_name from GM_M_ENTITY_MASTER_DETAIL t,wb_m_gadi_doki_master t1 where t.ngadi_doki=t1.ngadi_doki_id and t.nentity_uni_id=?  and t.vblocked = 'N' ")) // and t.nvehicle_type_id = 1
		{
			int i=1;
			pst.setString(i++, (prefix?"H":"")+harvCode);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					caneharvPlanRes.setSuccess(true);
					caneharvPlanRes.setCode(rs.getString("nentity_uni_id"));
					caneharvPlanRes.setName(DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
					caneharvPlanRes.setType(DemoConvert2.ism_to_uni(rs.getString("doki_name")));
					caneharvPlanRes.setTypeCode(rs.getString("ngadi_doki"));
				}else
				{
					caneharvPlanRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.havesterInformationNotFound);
					caneharvPlanRes.setSe(error);
				}
			}
		} catch (Exception e) {
			caneharvPlanRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("fetch harvestor info "+e.getMessage());
			caneharvPlanRes.setSe(error);
			e.printStackTrace();
	}
		return caneharvPlanRes;
	}

	public BulluckCartResponse findMukadamInfoByCode(String yearCode, String mukadamCode,
			boolean prefix, BulluckCartResponse caneMukhResponse, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.vvehicle_no, t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as name from GM_M_ENTITY_MASTER_DETAIL t where t.vblocked = 'N'  AND t.ngadi_doki = 4 AND t.nentity_uni_id=?"))
		{
			int i=1;
			pst.setString(i++, (prefix?"B":"")+mukadamCode);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					caneMukhResponse.setSuccess(true);
					caneMukhResponse.setMukadamCode(rs.getString("nentity_uni_id"));
					caneMukhResponse.setMukadamName(DemoConvert2.ism_to_uni(rs.getString("name")));
					caneMukhResponse.setVehicalNo(rs.getString("vvehicle_no"));
				}else
				{
					caneMukhResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.mukadamInformationNotFound);
					caneMukhResponse.setSe(error);
				}
			}
		} catch (Exception e) {
			caneMukhResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("fetch mukadam info "+e.getMessage());
			caneMukhResponse.setSe(error);
			e.printStackTrace();
	}
		return caneMukhResponse;
	
	}

	public BulluckCartResponse getBullockList(String mukadamCode,String vehicleType, String yearCode,String nspliNo,String bullockCode, boolean prefix, HashMap<String, String> oldSlip, int hold, BulluckCartResponse caneMukhResponse,
			Connection conn) {
		// IMP Comment :: After partial load work start we need to on all isVehicalBulluckart filters and add the season filter only for bulluckcart
		boolean isVehicalBulluckart =vehicleType.equals("3");
		String sql = "select t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as name,t.vvehicle_no ";
		//if (isVehicalBulluckart)
			sql += ",wt.nslip_no, wt.ntailor_front, wt.ntailor_back ";
		/*else
			sql += ",null as nslip_no ";*/
		sql+= " from GM_M_ENTITY_MASTER_DETAIL t ";
		//if (isVehicalBulluckart)
			sql+= " left join " + ConstantVeriables.weightSlipTableName + " wt ON wt.nbulluckcart_id = t.nentity_uni_id and wt.vyear_id = ? and nvl(wt.ngross_weight,0)=0 and vactive_dactive='A' ";
		if(nspliNo!=null  && !nspliNo.trim().isEmpty())
			sql+=" and wt.nslip_no<>? ";
		sql+=" and wt.nbulluckcart_id<>'B749' ";
			sql+= " where t.vblocked = 'N'  AND t.ngadi_doki = ? AND t.nentity_father_id=? order by t.nentity_uni_id";
		
			JSONObject pos = new JSONObject();
			try (PreparedStatement pst = conn.prepareStatement(sql))
		{
			int i=1;
			pst.setString(i++, yearCode);
			if(nspliNo!=null  && !nspliNo.trim().isEmpty())
				pst.setString(i++, nspliNo);
			if(isVehicalBulluckart) {
				//pst.setString(i++, yearCode);
				pst.setString(i++, "5");
			} else 
				pst.setString(i++, "3");
			pst.setString(i++, (prefix ? "B" : "") + mukadamCode);
			
			try(ResultSet rs=pst.executeQuery())
			{
					List<KeyPairBoolData> list=new  ArrayList<>();
                	while(rs.next())
                	{
                		String code = rs.getString("nentity_uni_id");
                		String nslip_no = rs.getString("nslip_no");
                		if(pos.has(code)){
                			int mypos =pos.getInt(code);
                			KeyPairBoolData pbd=list.get(mypos);
                			pbd.setName(pbd.getName()+ "," + nslip_no);
                			JSONObject bulluckBean=new JSONObject(pbd.getObject().toString());
                			String front = rs.getString("ntailor_front");
							String back = rs.getString("ntailor_back");
							
							JSONObject removewire = bulluckBean.has("removewire")?bulluckBean.getJSONObject("removewire"):new JSONObject();
							
							String prevFront = removewire.has("front")?removewire.getString("front"):null;
							String prevBack = removewire.has("back")?removewire.getString("back"):null;
							
							if(!code.equalsIgnoreCase("B749") && !code.equalsIgnoreCase("B1486"))
							{
								prevFront=null;
								prevBack=null;
							}
							
							if(front!=null && !front.equals("5")) {
								if(prevFront!=null)
									front=prevFront+","+front;
								else 
									front="1,"+front;
								removewire.put("front", front);
							} 
							
							if(back!=null && !back.equals("5")) {
								if(prevBack!=null)
									back=prevBack+","+back;
								else 
									back="1,"+back;
								removewire.put("back", back);
							} 
							removewire.put("frontall", removewire.getBoolean("frontall") || (front!=null && front.equals("1")));
							removewire.put("backall", removewire.getBoolean("backall") || (back!=null && back.equals("1")));
							bulluckBean.put("removewire", removewire);
							pbd.setObject(bulluckBean.toString());
							list.set(mypos, pbd);
                		} else {
                			KeyPairBoolData pbd=new KeyPairBoolData();
                    		
                    		
                    		pbd.setId(Long.parseLong(code.replaceAll("B", "")));
                    		String name = code + " : " + DemoConvert2.ism_to_uni(rs.getString("name"));
                    		JSONObject bulluckBean=new JSONObject();
                    		bulluckBean.put("name", name);
                    		if (nslip_no == null) 
    							bulluckBean.put("allow", true);
    						else {
    							if(isVehicalBulluckart)
    								bulluckBean.put("allow", false);
    							else 
    								bulluckBean.put("allow", true);
    							name += " Slip No " + nslip_no;
    							String front = rs.getString("ntailor_front");
    							String back = rs.getString("ntailor_back");
    							
    							JSONObject removewire = new JSONObject();
    							removewire.put("keepid", "5");
    							if(front!=null && !front.equals("5")) {
    								removewire.put("front", front.equals("1")?front:"1,"+front);
    							} 
    							
    							if(back!=null && !back.equals("5")) {
    								removewire.put("back", back.equals("1")?back:"1,"+back);
    							} 
    							removewire.put("frontall", front!=null && front.equals("1"));
    							removewire.put("backall", back!=null && back.equals("1"));
    							bulluckBean.put("removewire", removewire);
    							
    						}
                    		if(oldSlip!=null && bulluckBean.getBoolean("allow")) {
                    			if(oldSlip.containsKey(code)){
                    				bulluckBean.put("allow", false);
                    				bulluckBean.put("msg", String.format(ConstantMessage.ERROR_Time_Slip, String.valueOf(hold), oldSlip.get(code)));
                    				name += " out time " +oldSlip.get(code);
                    			}
                    		}
                    		pbd.setName(name);
                    		bulluckBean.put("code", code);
                    		bulluckBean.put("vehicleNo", rs.getString("vvehicle_no"));
                    		pbd.setSelected(bullockCode!=null && bullockCode.equalsIgnoreCase(code));
                    		pbd.setObject(bulluckBean.toString());
                    		pos.put(code, list.size());
                    		list.add(pbd);
                		}
                		
                		
                	}
                	caneMukhResponse.setBulluckcartList(list);
				if(caneMukhResponse.getBulluckcartList()==null || caneMukhResponse.getBulluckcartList().size()==0)
				{
					caneMukhResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.bullockInformationNotFound);
					caneMukhResponse.setSe(error);
				}
			}
		} catch (Exception e) {
			caneMukhResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("fetch bullock info "+e.getMessage());
			caneMukhResponse.setSe(error);
			e.printStackTrace();
	}
		return caneMukhResponse;
	}

	public RemainingSlipResponse remainingSlipList(String yearCode, String vehicleType,
			String transCode, String bullockCode, String chit_boy_id, RemainingSlipResponse sliplistResponse,
			Connection conn) {
		try
		{
			JSONObject wirerope=new JSONObject();
			try(PreparedStatement pst=conn.prepareStatement("select t.nwirerope_id,t.vwire_name from WB_M_WIREROPE_MASTER t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						wirerope.put(rs.getString("nwirerope_id"), DemoConvert2.ism_to_uni(rs.getString("vwire_name")));
					}
				}
			}
			JSONObject tailor=new JSONObject();
			try(PreparedStatement pst=conn.prepareStatement("select t.ntailor_remark_id as code,t. vremark_name as name from WB_M_TAILOR_MASTER t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						tailor.put(rs.getString("code"), DemoConvert2.ism_to_uni(rs.getString("name")));
					}
				}
			}
			
			JSONObject gadi_doki=new JSONObject();
			try(PreparedStatement pst=conn.prepareStatement("select t.ngadi_doki_id,t.vgadi_doki_name from WB_M_GADI_DOKI_MASTER t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						gadi_doki.put(rs.getString("ngadi_doki_id"), DemoConvert2.ism_to_uni(rs.getString("vgadi_doki_name")));
					}
				}
			}
			
			JSONObject varity=new JSONObject();
			try(PreparedStatement pst=conn.prepareStatement("select t.navariety_id,TO_NCHAR(t.vvariety_name)as vvariety_name from CR_M_VARIETY_MASTER t "))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						varity.put(rs.getString("navariety_id"), DemoConvert2.ism_to_uni(rs.getString("vvariety_name")));
					}
				}
			}
			String sql="select t.nplot_no,t.nvillage_id,TO_NCHAR(t1.village_name_local)as village_name,t.nentity_uni_id,TO_NCHAR(t2.ventity_name_local)as vfarmerName,t.nvehicle_type_id,t.nremark_id,t.nslip_no,t.dslip_date,t.vvehicle_no,t.nbulluckcart_main_id,TO_NCHAR(t3.ventity_name_local)as vtransporterName,t.nbulluckcart_id,TO_NCHAR(t4.ventity_name_local)as vharvestorName,t.nwirerope_no,t.ntailor_front,t.ntailor_back,TO_NCHAR(t5.vfull_name_local) as vfull_name_local,t.ngadi_doki_id,t.nvariety_id,t.dslip_date_slipboy from " + ConstantVeriables.weightSlipTableName + " t,GM_M_VILLAGE_MASTER t1,GM_M_ENTITY_MASTER_DETAIL t2,GM_M_ENTITY_MASTER_DETAIL t3,GM_M_ENTITY_MASTER_DETAIL t4,app_m_user_master t5  WHERE t.nvillage_id=t1.nvillage_id and t.nentity_uni_id=t2.nentity_uni_id and t.nbulluckcart_main_id=t3.nentity_uni_id and t.nbulluckcart_id=t4.nentity_uni_id  and  nvl(t.ngross_weight,0)=0  and t.nslipboy_id=t5.vuser_name  and t.vactive_dactive='A'  and t.vyear_id=? and t.nvehicle_type_id=? and t.nbulluckcart_id=?";
			if(vehicleType.equals("1") || vehicleType.equals("2"))
				sql="select t.nplot_no,t.nvillage_id,TO_NCHAR(t1.village_name_local)as village_name,t.nentity_uni_id,TO_NCHAR(t2.ventity_name_local)as vfarmerName,t.nvehicle_type_id,t.nremark_id,t.nslip_no,t.dslip_date,t.vvehicle_no,t.ntransportor_id,TO_NCHAR(t3.ventity_name_local)as vtransporterName,t.nharvestor_id,TO_NCHAR(t4.ventity_name_local)as vharvestorName,t.nwirerope_no,t.ntailor_front,t.ntailor_back,TO_NCHAR(t5.vfull_name_local) as vfull_name_local,t.ngadi_doki_id,t.nvariety_id,t.dslip_date_slipboy from " + ConstantVeriables.weightSlipTableName + " t,GM_M_VILLAGE_MASTER t1,GM_M_ENTITY_MASTER_DETAIL t2,GM_M_ENTITY_MASTER_DETAIL t3,GM_M_ENTITY_MASTER_DETAIL t4,app_m_user_master t5  WHERE t.nvillage_id=t1.nvillage_id and t.nentity_uni_id=t2.nentity_uni_id and t.ntransportor_id=t3.nentity_uni_id and t.nharvestor_id=t4.nentity_uni_id  and  nvl(t.ngross_weight,0)=0  and t.nslipboy_id=t5.vuser_name  and t.vactive_dactive='A'  and t.vyear_id=? and t.nvehicle_type_id=? and t.ntransportor_id=?";
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				int i=1;
				pst.setString(i++, yearCode);
				pst.setString(i++, vehicleType);
				if(vehicleType.equals("1") || vehicleType.equals("2"))
					pst.setString(i++, "T"+transCode);
				else
					pst.setString(i++, "B"+bullockCode);
				try(ResultSet rs=pst.executeQuery())
				{
					List<RemainingSlipBean> list=new ArrayList<>();
					while(rs.next())
					{
						RemainingSlipBean data=new RemainingSlipBean();
						data.setDslipDate(Constant.DbDateTimeToAppDateTime(rs.getTimestamp("dslip_date_slipboy")));
						data.setNentityUnitId(rs.getString("nentity_uni_id"));
						data.setNplotNo(rs.getString("nplot_no"));
						data.setNremarkId(rs.getString("nremark_id"));
						data.setNslipNo(rs.getString("nslip_no"));
						data.setNvillageCode(rs.getString("nvillage_id"));
						data.setVfarmerName(DemoConvert2.ism_to_uni(rs.getString("vfarmerName")));
						data.setNvehicalTypeId(rs.getString("nvehicle_type_id"));
						if(vehicleType.equals("1") || vehicleType.equals("2")) {
							data.setNharvestorId(rs.getString("nharvestor_id"));
							data.setVharvestorName(DemoConvert2.ism_to_uni(rs.getString("vharvestorName")));
							data.setNtransporterId(rs.getString("ntransportor_id"));
							String harvestorType=gadi_doki.has(rs.getString("ngadi_doki_id"))?gadi_doki.getString(rs.getString("ngadi_doki_id")):null;
							data.setVharvestorType(harvestorType);
							data.setVtransporterName(DemoConvert2.ism_to_uni(rs.getString("vtransporterName")));
						} else {
							data.setNbullockCode(rs.getString("nbulluckcart_id"));
							data.setNbullockMainCode(rs.getString("nbulluckcart_main_id"));
							data.setVbullockMainName(DemoConvert2.ism_to_uni(rs.getString("vtransporterName")));
							data.setVbullockName(DemoConvert2.ism_to_uni(rs.getString("vharvestorName")));
						}
						
						
						String vtailerFrontName=tailor.has(rs.getString("ntailor_front"))?tailor.getString(rs.getString("ntailor_front")):null;
						String vtailerBackName=tailor.has(rs.getString("ntailor_back"))?tailor.getString(rs.getString("ntailor_back")):null;
						data.setVtailerBackName(vtailerBackName);
						data.setVtailerFrontName(vtailerFrontName);
						data.setVvehicleNo(rs.getString("vvehicle_no"));
						data.setVvillageNameLocal(DemoConvert2.ism_to_uni(rs.getString("village_name")));
						String vwireropeName=wirerope.has(rs.getString("nwirerope_no"))?wirerope.getString(rs.getString("nwirerope_no")):null;
						data.setVwireropeName(vwireropeName);
						data.setChitboyName(DemoConvert2.ism_to_uni(rs.getString("vfull_name_local")));
						data.setVvarietyName(varity.has(rs.getString("nvariety_id"))?varity.getString(rs.getString("nvariety_id")):"");
						data.setExtraQr(ConstantVeriables.lblExtraQR);
						list.add(data);
					}
					sliplistResponse.setRemainingSlipBeans(list);
					sliplistResponse.setSuccess(true);
				}
			}
		}
		 catch (Exception e) {
			 	sliplistResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("slip list info "+e.getMessage());
				sliplistResponse.setSe(error);
				e.printStackTrace();
		}
		return sliplistResponse;
	}

	public ActionResponse detivateSlip(String nslipNo, String yearCode,String chit_boy_id, ActionResponse savewsRes, Connection conn) {
		try{
			int res=0;
			try(PreparedStatement pst=conn.prepareStatement("update " + ConstantVeriables.weightSlipTableName + " t set t.vactive_dactive='D',t.NUSER_DEACTIVE=?, t.DDATE_DEACTIVE=SYSDATE where t.nslip_no=? and t.vyear_id=?"))
			{
				int i=1;
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, nslipNo);
				pst.setString(i++, yearCode);
				res=pst.executeUpdate(); 
			}	if(res>0)
				{
				savewsRes.setActionComplete(true);
				savewsRes.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					savewsRes.setActionComplete(false);
					savewsRes.setFailMsg(ConstantMessage.saveFailed);
				}
			savewsRes.setSuccess(true);
			}catch (Exception e) {
				savewsRes.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Deativate Weight Slip "+e.getMessage());
				savewsRes.setSe(error);
				e.printStackTrace();
		}
		return savewsRes;
	}

	public HarvSlipDetailsResponse slipeditInfo(String nslipNo, String yearCode, String chit_boy_id, HarvSlipDetailsResponse slipeditRes,
			Connection conn) {
		try
		{
			try(PreparedStatement pst=conn.prepareStatement("select t.nplot_no,t.dslip_date,t.nslip_no,t.ncrop_id,t.nvehicle_type_id,t.ndistance,t.nharvestor_id,"
					+ "t.ngadi_doki_id,t.ntransportor_id,t.vvehicle_no,t.nbulluckcart_id,t.nbulluckcart_main_id,t.nwirerope_no,t.ntailor_front,"
					+ "t.ntailor_back,t.nslip_no_offline,t.nremark_id from " + ConstantVeriables.weightSlipTableName + " t where  nvl(t.ngross_weight,0)=0 and t.nslipboy_id=? and t.nslip_no=? and t.vyear_id=?"))
			{
				int i=1;
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, nslipNo);
				pst.setString(i++, yearCode);
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						slipeditRes.setNslipNo(nslipNo);
						slipeditRes.setNcropId(rs.getInt("ncrop_id"));
						slipeditRes.setVehicleType(rs.getInt("nvehicle_type_id"));
						slipeditRes.setNdistancesave(rs.getString("ndistance"));
						slipeditRes.setNharvestorId(rs.getString("nharvestor_id"));
						slipeditRes.setNgadiDokiId(rs.getString("ngadi_doki_id"));
						slipeditRes.setNtransportorId(rs.getString("ntransportor_id"));
						slipeditRes.setVvehicleNo(rs.getString("vvehicle_no"));
						slipeditRes.setNbulluckcartMainId(rs.getString("nbulluckcart_main_id"));
						slipeditRes.setNslipOfflineNo(rs.getString("nslip_no_offline"));
						slipeditRes.setNremarkId(rs.getInt("nremark_id"));
						slipeditRes.setSlipDate(Constant.DbDateToAppDate(rs.getDate("dslip_date")));
						JSONObject gadi_doki=new JSONObject();
						try(PreparedStatement pst1=conn.prepareStatement("select t.ngadi_doki_id,t.vgadi_doki_name from WB_M_GADI_DOKI_MASTER t"))
						{
							try(ResultSet rs1=pst1.executeQuery())
							{
								while(rs1.next())
								{
									gadi_doki.put(rs1.getString("ngadi_doki_id"), DemoConvert2.ism_to_uni(rs1.getString("vgadi_doki_name")));
								}
							}
						}
						String harvetortype=gadi_doki.has(rs.getString("ngadi_doki_id"))?gadi_doki.getString(rs.getString("ngadi_doki_id")):"";
						slipeditRes.setHarvetortype(harvetortype);
						if(slipeditRes.getVehicleType()==1 || slipeditRes.getVehicleType()==2)
						{
							TransporterResponse tres=new TransporterResponse();
							tres=findTranspoterByCode(rs.getString("ntransportor_id"),rs.getString("nvehicle_type_id"),yearCode, false,tres,conn);
							slipeditRes.setTransportername(tres.getName());
							
							HarvestorResponse harres=new HarvestorResponse();
							harres=findHarvestingInfoByCode(yearCode,rs.getString("nharvestor_id"),false,harres,conn);
							slipeditRes.setHarvetorname(harres.getName());
						}else
						{
							BulluckCartResponse mukhRes=new BulluckCartResponse();
							mukhRes=findMukadamInfoByCode(yearCode,rs.getString("nbulluckcart_main_id"), false,mukhRes,conn);
							slipeditRes.setBulluckcartMainName(mukhRes.getMukadamName());
							BulluckCartResponse bullkRes=new BulluckCartResponse();
							bullkRes=getBullockList(rs.getString("nbulluckcart_main_id"),rs.getString("nvehicle_type_id"),yearCode,nslipNo,rs.getString("nbulluckcart_id"), false, null, 0,bullkRes,conn);
							slipeditRes.setBulluckcartList(bullkRes.getBulluckcartList());
						}
						if(slipeditRes.getVehicleType()==2 || slipeditRes.getVehicleType()==4)
						{
							TransporterResponse tres=new TransporterResponse();
							HashMap<String, Set<String>> list=isWeightSlipExit(rs.getString("ntransportor_id"),rs.getString("nvehicle_type_id"),yearCode,nslipNo,conn);
							tres=(TransporterResponse) getWireropeList(rs.getString("nvehicle_type_id"),rs.getInt("nwirerope_no"),rs.getInt("ntailor_front"),rs.getInt("ntailor_back"),(WireRopeResonse) tres,conn);
							tres=removeWirepoe(rs.getString("nvehicle_type_id"),list,tres);
							slipeditRes.setWireropefront(tres.getWireropefront());
							slipeditRes.setWireropeback(tres.getWireropeback());
							slipeditRes.setCombos(tres.getCombos());
						} else {
							TransporterResponse tres=new TransporterResponse();
							HashMap<String, Set<String>> list=isWeightSlipExit(rs.getString("ntransportor_id"),rs.getString("nvehicle_type_id"),yearCode,nslipNo,conn);
							tres=(TransporterResponse) getWireropeList(rs.getString("nvehicle_type_id"),rs.getInt("nwirerope_no"),rs.getInt("ntailor_front"),rs.getInt("ntailor_back"),(WireRopeResonse) tres,conn);
							tres=removeWirepoe(rs.getString("nvehicle_type_id"),list,tres);
							slipeditRes.setWirerope(tres.getWirerope());
							slipeditRes.setCombos(tres.getCombos());
						}
						
						try(PreparedStatement pst1=conn.prepareStatement("select t.vyear_id,TO_NCHAR(f.ventity_name_local)as farmer_name,TO_NCHAR(ft.vfarmer_type_name_local)as farmer_type,TO_NCHAR(sm.vsection_name_local)as section_name,TO_NCHAR(vm.village_name_local)as villeage_name,TO_NCHAR(hm.vhangam_name)as hangam_name,TO_NCHAR(vm1.vvariety_name)as variety_name,t.vsurve_no,t.ntentative_area,t.nexpected_yield,vm.ngps_distance,t.dplantation_date,t.nentity_uni_id,t.nfarmer_type_id,t.nsection_id,t.nvillage_id,t.ncrop_id,t.nhangam_id,t.nvariety_id,t.ndistance from CR_T_PLANTATION t,GM_M_ENTITY_MASTER_DETAIL f,GM_M_FARMER_TYPE_MASTER ft,Gm_m_Section_Master sm, GM_M_VILLAGE_MASTER vm,CR_M_HANGAM_MASTER hm,CR_M_VARIETY_MASTER vm1 where t.nentity_uni_id=f.nentity_uni_id and f.nfarmer_type_id=ft.nfarmer_type_id and t.nsection_id=sm.nsection_id and t.nvillage_id=vm.nvillage_id and t.nhangam_id=hm.nhangam_id and t.nvariety_id=vm1.navariety_id and t.nplot_no=? and t.vyear_id=?")){
							pst1.setInt(1, rs.getInt("nplot_no"));
							pst1.setString(2, yearCode);
							try(ResultSet rs1=pst1.executeQuery())
							{
								if(rs1.next())
								{
									DecimalFormat df=new DecimalFormat("#0.000");
									DecimalFormat df2=new DecimalFormat("#0.00");
									
									slipeditRes.setNentityUniId(rs1.getString("nentity_uni_id"));
									slipeditRes.setNfarmerTypeId(rs1.getInt("nfarmer_type_id"));
									slipeditRes.setNsectionId(rs1.getInt("nsection_id"));
									slipeditRes.setNvillageId(rs1.getInt("nvillage_id"));
									slipeditRes.setNvarietyId(rs1.getInt("nvariety_id"));
									slipeditRes.setNhangamId(rs1.getInt("nhangam_id"));
									slipeditRes.setNdistance(rs1.getInt("ndistance"));
									slipeditRes.setNplotNo(rs.getInt("nplot_no"));
									slipeditRes.setVyearId(yearCode);
									slipeditRes.setVentityNameLocal(DemoConvert2.ism_to_uni(rs1.getString("farmer_name")));
									slipeditRes.setVfarmerTypeNameLocal(DemoConvert2.ism_to_uni(rs1.getString("farmer_type")));
									slipeditRes.setVsectionNameLocal(DemoConvert2.ism_to_uni(rs1.getString("section_name")));
									slipeditRes.setVillageNameLocal(DemoConvert2.ism_to_uni(rs1.getString("villeage_name")));
									slipeditRes.setVhangamName(DemoConvert2.ism_to_uni(rs1.getString("hangam_name")));
									slipeditRes.setVvarietyName(DemoConvert2.ism_to_uni(rs1.getString("variety_name")));
									slipeditRes.setVsurvyNo(rs1.getString("vsurve_no"));
									slipeditRes.setNtentativeArea(df2.format(rs1.getDouble("ntentative_area")));
									slipeditRes.setNexpectedYield(df.format(rs1.getDouble("nexpected_yield")));
									slipeditRes.setNgpsDistance(rs1.getString("ngps_distance"));
									slipeditRes.setDplantaitonDate(Constant.DbDateToAppDate(rs1.getDate("dplantation_date")));
								}
							}
						}
						try(PreparedStatement pst2=conn.prepareStatement("select t.nfact_id,TO_NCHAR(t.vfact_name_local)as vname from GM_M_FACTORY_MASTER t where t.nfact_id=1"))
						{
							try(ResultSet rs2=pst2.executeQuery())
							{
								if(rs2.next())
								{
									slipeditRes.setVfactNameLocal(DemoConvert2.ism_to_uni(rs2.getString("vname")));
									slipeditRes.setNfactId(rs2.getInt("nfact_id"));
								}
							}
						}
						slipeditRes.setSuccess(true);
					}else
					{
						slipeditRes.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(ConstantMessage.informationNotFound);
						slipeditRes.setSe(error);
					}
				}
			}
		}
		 catch (Exception e) {
			 slipeditRes.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("slip list info "+e.getMessage());
				slipeditRes.setSe(error);
				e.printStackTrace();
		}
		return slipeditRes;
	
	}

	public TransporterResponse removeWirepoe(String vehicleType, HashMap<String, Set<String>> list,
			TransporterResponse caneTransResponse) {
		if(vehicleType.equals("2")) {
			if(list.containsKey(ConstantVeriables.front)) {
				Set<String> frontSet = list.get(ConstantVeriables.front);
				List<KeyPairBoolData> frontData = caneTransResponse.getWireropefront();
				int size = frontData.size();
				if(frontSet.contains("all")) {
					for (int i = size-1; i >= 0; i--) {
						KeyPairBoolData data = frontData.get(i);
						if (data.getId() != 5)
							frontData.remove(i);
					}
				} else {
					for (int i = size-1; i >= 0; i--) {
						KeyPairBoolData data = frontData.get(i);
						if (frontSet.contains(String.valueOf(data.getId())))
							frontData.remove(i);
					}
				}
				caneTransResponse.setWireropefront(frontData);
			}
			if(list.containsKey(ConstantVeriables.back)) {
				Set<String> backSet = list.get(ConstantVeriables.back);
				List<KeyPairBoolData> backData = caneTransResponse.getWireropeback();
				int size = backData.size();
				if (backSet.contains("all")) {
					for (int i = size-1; i >= 0; i--) {
						KeyPairBoolData data = backData.get(i);
						if (data.getId() != 5)
							backData.remove(i);
					}
				} else {
					for (int i = size-1; i >= 0; i--) {
						KeyPairBoolData data = backData.get(i);
						if (backSet.contains(String.valueOf(data.getId())))
							backData.remove(i);
					}
				}
				caneTransResponse.setWireropeback(backData);
			}
			if(caneTransResponse.getWireropefront().size()<=1 && caneTransResponse.getWireropeback().size()<=1 ) {
				caneTransResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg(ConstantMessage.ERROR_Full_Load);
				caneTransResponse.setSe(error);
			}
		} else {
			if(list.containsKey(ConstantVeriables.wire)) {
				Set<String> wireSet = list.get(ConstantVeriables.wire);
				List<KeyPairBoolData> wireData = caneTransResponse.getWirerope();
				int size = wireData.size();
				if (wireSet.contains("all")) {
					for (int i = size-1; i >= 0; i--) {
						KeyPairBoolData data = wireData.get(i);
						if (data.getId() != 5)
							wireData.remove(i);
					}
				} else {
					for (int i = size-1; i >= 0; i--) {
						KeyPairBoolData data = wireData.get(i);
						if (wireSet.contains(String.valueOf(data.getId())))
							wireData.remove(i);
					}
				}
				caneTransResponse.setWirerope(wireData);
				if(wireData.size()<=1) {
					caneTransResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.ERROR_Full_Load);
					caneTransResponse.setSe(error);
				}
			}
		}
		return caneTransResponse;
	}

	public ActionResponse acceptOrRejectExtraPlot(String nplotNo, String yearCode, String nstatusId,
			String nallowedTonnage, String chit_boy_id, ActionResponse savewsRes, Connection conn) {
		try{
			int res=0;
			try(PreparedStatement pst=conn.prepareStatement("update APP_T_EXCESS_TON_PLOT_REQ t set t.nstatus_id=?,t.nallowed_tonnage=?,t.naction_user_id=?,t.daction_date_time=sysdate where t.nstatus_id=1 and t.nplot_no=? and t.vyear_id=?"))
			{
				int i=1;
				pst.setString(i++, nstatusId);
				pst.setString(i++, nallowedTonnage);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, nplotNo);
				pst.setString(i++, yearCode);
				res=pst.executeUpdate(); 
			}	if(res>0)
				{
				savewsRes.setActionComplete(true);
				savewsRes.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					savewsRes.setActionComplete(false);
					savewsRes.setFailMsg(ConstantMessage.saveFailed);
				}
			savewsRes.setSuccess(true);
			}catch (Exception e) {
				savewsRes.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Deativate Weight Slip "+e.getMessage());
				savewsRes.setSe(error);
				e.printStackTrace();
		}
		return savewsRes;
	}
	
	public  ActionResponse saveExtraPlotRequest(JSONObject reqObj,String chit_boy_id, ActionResponse saveResponse,
			Connection conn) {
		try{
			String vyearId=reqObj.has("vyearId")?reqObj.getString("vyearId"):null;
			String nentityUniId=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):null;
			String nplotNo=reqObj.has("nplotNo")?reqObj.getString("nplotNo"):null;
			String ntentativeArea=reqObj.has("ntentativeArea")?reqObj.getString("ntentativeArea"):null;
			String nharvestedTonnage=reqObj.has("nharvestedTonnage")?reqObj.getString("nharvestedTonnage"):null;
			String nreasonId=reqObj.has("nreasonId")?reqObj.getString("nreasonId"):null;
			String nareaRemaining=reqObj.has("nareaRemaining")?reqObj.getString("nareaRemaining"):null;
			String nrequestedTonnage=reqObj.has("nrequestedTonnage")?reqObj.getString("nrequestedTonnage"):null;
			String nexpectedYield=reqObj.has("nexpectedYield")?reqObj.getString("nexpectedYield"):null;
			String extendedtonnage=reqObj.has("extendedtonnage")?reqObj.getString("extendedtonnage"):null;
			if(vyearId==null ||nentityUniId==null ||nplotNo==null ||ntentativeArea==null ||nharvestedTonnage==null ||nreasonId==null /*||nareaRemaining==null*/ || nrequestedTonnage==null)
			{
				saveResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Invalid Parameter ");
				saveResponse.setSe(error);
			}else
			{
				try(PreparedStatement pst=conn.prepareStatement("INSERT INTO APP_T_EXCESS_TON_PLOT_REQ(vyear_id,nentity_uni_id,nplot_no,ntentative_area,nharvested_tonnage,nreason_id,narea_remaining,nrequested_tonnage,nentry_user_id,dentry_date_time,nstatus_id,nexpected_yield,nlimit_yield) VALUES(?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?)"))
				{
					int i=1;
					pst.setString(i++, vyearId);
					pst.setString(i++, nentityUniId);
					pst.setString(i++, nplotNo);
					pst.setString(i++, ntentativeArea);
					pst.setString(i++, nharvestedTonnage);
					pst.setString(i++, nreasonId);
					pst.setString(i++, nareaRemaining);
					pst.setString(i++, nrequestedTonnage);
					pst.setString(i++, chit_boy_id);
					pst.setInt(i++, 1);
					pst.setString(i++, nexpectedYield);
					pst.setString(i++, extendedtonnage);
				
					int res=pst.executeUpdate();
					if(res>0)
					{
						saveResponse.setActionComplete(true);
						saveResponse.setSuccessMsg(ConstantMessage.saveSuccess);
					}else
					{
						saveResponse.setActionComplete(false);
						saveResponse.setFailMsg(ConstantMessage.saveFailed);
					}
					saveResponse.setSuccess(true);
				}
			}
		}catch (Exception e) {
			saveResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("extra plot request save "+e.getMessage());
			saveResponse.setSe(error);
			e.printStackTrace();
		}
		return saveResponse;
	}

	public  ExcessTonPlotReqResponse extraPlotReqList(String yearCode, String chit_boy_id,
			ExcessTonPlotReqResponse extraPlotReqListRes, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.nplot_no,t.nentity_uni_id,TO_NCHAR(t1.ventity_name_local)as farmer_name from APP_T_EXCESS_TON_PLOT_REQ t,gm_m_entity_master_detail t1 where t.nentity_uni_id=t1.nentity_uni_id and t.nstatus_id=1 and t.vyear_id=?")){
			pst.setString(1, yearCode);
			try(ResultSet rs=pst.executeQuery())
			{
				List<ExcessTonPlotReq> list=new ArrayList<>();
				while(rs.next())
				{
					ExcessTonPlotReq data=new ExcessTonPlotReq();
					data.setPlotNo(rs.getString("nplot_no"));
					String farmerName=rs.getString("nentity_uni_id")+" - "+DemoConvert2.ism_to_uni(rs.getString("farmer_name"));
					data.setFarmerName(farmerName);
					list.add(data);
				}
				if(list.size()>0)
				{
					extraPlotReqListRes.setList(list);
					extraPlotReqListRes.setSuccess(true);
				}else
				{
					extraPlotReqListRes.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.informationNotFound);
					extraPlotReqListRes.setSe(error);
				}
				
			}
		} catch (SQLException e) {
			extraPlotReqListRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("extra tonnage  plot request "+e.getMessage());
			extraPlotReqListRes.setSe(error);
			e.printStackTrace();
		}
		return extraPlotReqListRes;
	}

	public  ExcessTonPlotReqDataResponse excessPlotDetails(String yearCode, String nplotNo,
			ExcessTonPlotReqDataResponse excessPlot, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.nplot_no,t.nentity_uni_id,TO_NCHAR(t1.ventity_name_local)as farmer_name,TO_NCHAR(t2.vreason_name)as vreason_name,TO_NCHAR(t3.vfull_name_local)as vfull_name,t.ntentative_area,t.nexpected_yield,t.nlimit_yield,t.nharvested_tonnage,t.narea_remaining,t.nrequested_tonnage,t.nallowed_tonnage from APP_T_EXCESS_TON_PLOT_REQ t,gm_m_entity_master_detail t1,app_m_reason t2,app_m_user_master t3 where t.nentity_uni_id=t1.nentity_uni_id and t.nreason_id=t2.nreason_id and t.nentry_user_id=t3.nuser_name and t.nstatus_id=1  and t.nplot_no=? and t.vyear_id=?")){
			pst.setString(1, nplotNo);
			pst.setString(2, yearCode);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					excessPlot.setNplotNo(rs.getString("nplot_no"));
					excessPlot.setNentityUniId(rs.getString("nentity_uni_id"));
					String farmerName=DemoConvert2.ism_to_uni(rs.getString("farmer_name"));
					excessPlot.setVfarmerName(farmerName);
					excessPlot.setNtentativeArea(Constant.decimalFormat(rs.getDouble("ntentative_area"), "00"));
					excessPlot.setNexpectedYield(Constant.decimalFormat(rs.getDouble("nexpected_yield"), "000"));
					excessPlot.setNlimitYield(Constant.decimalFormat(rs.getDouble("nlimit_yield"), "000"));
					excessPlot.setNharvestedTonnage(Constant.decimalFormat(rs.getDouble("nharvested_tonnage"), "000"));
					excessPlot.setNremainingArea(Constant.decimalFormat(rs.getDouble("narea_remaining"), "00"));
					excessPlot.setNremainingTonnage(Constant.decimalFormat(rs.getDouble("nrequested_tonnage"), "000"));
					excessPlot.setVreasonName(DemoConvert2.ism_to_uni(rs.getString("vreason_name")));
					excessPlot.setVchitboyname(DemoConvert2.ism_to_uni(rs.getString("vfull_name")));
				}else
				{
					excessPlot.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.informationNotFound);
					excessPlot.setSe(error);
				}
			}
		} catch (SQLException e) {
			excessPlot.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("extra tonnage plot request update "+e.getMessage());
			excessPlot.setSe(error);
			e.printStackTrace();
		}
		return excessPlot;
	}

	public  OtherUtilizationResponse otherUtilizationDetails(String yearCode, String nplotNo,String chit_boy_id,
			OtherUtilizationResponse otherUtilizationRes, Connection conn) {
		try{
			
			try(PreparedStatement pst=conn.prepareStatement("select t.nharvested_flag,t.nplot_no,t.nentity_uni_id,TO_NCHAR(f.ventity_name_local)as farmer_name,v.nvillage_id,TO_NCHAR(v.village_name_local)as village_name,t.nhangam_id,t.nvariety_id,t.ntentative_area,t.nexpected_yield from "+ConstantVeriables.tabeAgriPlantaton+" t,GM_M_ENTITY_MASTER_DETAIL f,GM_M_VILLAGE_MASTER v where t.nentity_uni_id=f.nentity_uni_id and t.nvillage_id=v.nvillage_id and t.nplot_no=? and t.vyear_id=? and (v.napp_user_1=? OR v.napp_user_2=?)")){
				pst.setString(1, nplotNo);
				pst.setString(2, yearCode);
				pst.setString(3, chit_boy_id);
				pst.setString(4, chit_boy_id);
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						if(rs.getInt("nharvested_flag")>0)
						{
							otherUtilizationRes.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(ConstantMessage.otherUtilizationPlotClose);
							otherUtilizationRes.setSe(error);
						}else
						{
							otherUtilizationRes.setNplotNo(rs.getString("nplot_no"));
							String farmerName=DemoConvert2.ism_to_uni(rs.getString("farmer_name"));
							otherUtilizationRes.setVfarmerName(farmerName);
							otherUtilizationRes.setNentityUniId(rs.getString("nentity_uni_id"));
							String villeageName=rs.getString("nvillage_id")+" - "+DemoConvert2.ism_to_uni(rs.getString("village_name"));
							otherUtilizationRes.setVvillageName(villeageName);
							otherUtilizationRes.setNhangamCode(rs.getString("nhangam_id"));
							otherUtilizationRes.setNvarietyCode(rs.getString("nvariety_id"));
							otherUtilizationRes.setNtentativeArea(Constant.decimalFormat(rs.getDouble("ntentative_area"), "00"));
							otherUtilizationRes.setNexpectedYield(Constant.decimalFormat(rs.getDouble("nexpected_yield"), "000"));
							try(PreparedStatement pst2=conn.prepareStatement("select t.nfact_id,TO_NCHAR(t.vfact_name_local)as vname from GM_M_FACTORY_MASTER t where t.vactive_cane_outward='Y'"))
							{
								try(ResultSet rs2=pst2.executeQuery())
								{
									ArrayList<KeyPairBoolData> factoryList=new ArrayList<>();
									while(rs2.next())
									{ 
										KeyPairBoolData data=new KeyPairBoolData();
										data.setName(DemoConvert2.ism_to_uni(rs2.getString("vname")));
										data.setId(rs2.getInt("nfact_id"));
										factoryList.add(data);
									}
									otherUtilizationRes.setFactoryList(factoryList);
								}
							}
							String utilizArea="0";
							try(PreparedStatement pst2=conn.prepareStatement("select SUM(t.narea_outward)as area from CR_T_CANE_OUTWARD t where t.nplot_no=? and t.vyear_id=?")){
								pst2.setString(1, nplotNo);
								pst2.setString(2, yearCode);
								try(ResultSet rs2=pst2.executeQuery())
								{
									if(rs2.next())
									{
										utilizArea=Constant.decimalFormat(rs2.getDouble("area"), "00");
									}
									otherUtilizationRes.setNutilizationArea(utilizArea);
								}
							}
						}
					}else
						{
							otherUtilizationRes.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(ConstantMessage.informationNotFound);
							otherUtilizationRes.setSe(error);
						}
				}
			}
		} catch (SQLException e) {
			otherUtilizationRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Other Utilization request "+e.getMessage());
			otherUtilizationRes.setSe(error);
			e.printStackTrace();
		}
		return otherUtilizationRes;
	}

	public ActionResponse saveOtherUtilization(String vyearId, String nentityUniId, String nplotNo, String nfactId,
			String nreasonId, String nareaAllowed, String nexpectedYield, boolean equal,String chit_boy_id, ActionResponse saveOtherUtiRes,
			Connection conn) {
		//check 
		if(equal)
		{
			try(PreparedStatement pst=conn.prepareStatement("SELECT count(w.nslip_no) as cnt FROM Wb_t_Weight_Slip w WHERE w.vyear_id=? and w.nplot_no=?")){
				int i=1;
				pst.setString(i++, vyearId);
				pst.setString(i++, nplotNo);
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						if(rs.getInt("cnt")>0)
						{
							saveOtherUtiRes.setActionComplete(false);
							saveOtherUtiRes.setFailMsg(ConstantMessage.otherUtilizationPlot);
							saveOtherUtiRes.setSuccess(true);
							return saveOtherUtiRes;
						}
					}
				}
			} catch (SQLException e) {
				saveOtherUtiRes.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Other Utilization Save "+e.getMessage());
				saveOtherUtiRes.setSe(error);
				e.printStackTrace();
			}
		}
		try(PreparedStatement pst=conn.prepareStatement("INSERT INTO CR_T_CANE_OUTWARD(vyear_id,nplot_no,dentry_date,"
				+ "nfact_id,nslipboy_id,nreason_id,narea_outward,nentity_uni_id,nexpected_yield) VALUES(?,?,SYSDATE,?,?,?,?,?,?)"))
		{
			int i=1;
			pst.setString(i++, vyearId);
			pst.setString(i++, nplotNo);
			pst.setString(i++, nfactId);
			pst.setString(i++, chit_boy_id);
			pst.setString(i++, nreasonId);
			pst.setString(i++, nareaAllowed);
			pst.setString(i++, nentityUniId);
			pst.setString(i++, nexpectedYield);
			int res=pst.executeUpdate();
			if(equal)
			{
				try(PreparedStatement pst1=conn.prepareStatement("update "+ConstantVeriables.tabeAgriPlantaton+" t set t.nharvested_flag=2 where t.nplot_no=? and t.vyear_id=?")){
					pst1.setString(1, nplotNo);
					pst1.setString(2, vyearId);
					pst1.executeUpdate();
				}
			}
			if(res>0)
			{
				saveOtherUtiRes.setActionComplete(true);
				saveOtherUtiRes.setSuccessMsg(ConstantMessage.saveSuccess);
			}else
			{
				saveOtherUtiRes.setActionComplete(false);
				saveOtherUtiRes.setFailMsg(ConstantMessage.saveFailed);
			}
			saveOtherUtiRes.setSuccess(true);
		} catch (Exception e) {
			saveOtherUtiRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Other Utilization Save "+e.getMessage());
			saveOtherUtiRes.setSe(error);
			e.printStackTrace();
		}
	
		return saveOtherUtiRes;
	}

	public CaneDailyInwardReportResponse dailyInwardReport(String yearCode, String vehicleType,String slipDate,String chit_boy_id,
			CaneDailyInwardReportResponse dailyCaneReportRes, Connection conn) {
		try {
			JSONObject wireRope = null;
			JSONObject trailer = new JSONObject();
			if(vehicleType.equals("2") || vehicleType.equals("4")) {
				String sql = "select t.ntailor_remark_id as code, TO_NCHAR(t.vremark_name) as name from WB_M_TAILOR_MASTER t order by t.ntailor_remark_id asc";
				try(PreparedStatement pst=conn.prepareStatement(sql)){
					try(ResultSet rs=pst.executeQuery())
					{
						while(rs.next())
						{
							trailer.put(rs.getString("code"), DemoConvert2.ism_to_uni(rs.getString("name")));
						}
					}
				}
			} else {
				wireRope = new JSONObject();
				String sql = "select t.nwirerope_id as code,TO_NCHAR(t.vwire_name) as name from WB_M_WIREROPE_MASTER t order by t.nwirerope_id ASC";
				try(PreparedStatement pst=conn.prepareStatement(sql)){
					try(ResultSet rs=pst.executeQuery())
					{
						while(rs.next())
						{
							wireRope.put(rs.getString("code"), DemoConvert2.ism_to_uni(rs.getString("name")));
						}
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select t.ntailor_front, t.ntailor_back, t.nwirerope_no, TO_CHAR(t.dslip_date,'dd/mm/yyyy')as dslip_date,t.nslip_no,t.nplot_no,t.nentity_uni_id,TO_NCHAR(t1.ventity_name_local)as farmer_name,t.nvillage_id,TO_NCHAR(t2.village_name_local)as village_name,t.nvehicle_type_id,TO_NCHAR(t3.vvehicle_type_name_local)as vehicle_type,t.ntransportor_id,t.nharvestor_id,t.nbulluckcart_main_id,t.nbulluckcart_id,t.ndistance,t.vvehicle_no,t.vremark,t.nnet_weight,TO_NCHAR(t4.vremark_name_local)as remark from "+ConstantVeriables.weightSlipTableName+" t,gm_m_entity_master_detail t1,gm_m_village_master t2,gm_m_vehicle_type_master t3,WB_M_SLIP_REMARK t4 where t.nentity_uni_id=t1.nentity_uni_id and t.nvillage_id=t2.nvillage_id and t.nvehicle_type_id=t3.nvehicle_type_id and t.nremark_id=t4.nremark_id and nvl(t.nnet_weight,0)>0 and t.VSLIP_VERIFY='N' and t.nslipboy_id=? and t.dslip_date=TO_DATE(?,'dd-mon-yyyy') and t.nvehicle_type_id=? and t.vyear_id =?")){
				int i=1;
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, Constant.AppDateDbDate(slipDate));
				pst.setString(i++, vehicleType);
				pst.setString(i++, yearCode);
				try(ResultSet rs=pst.executeQuery())
				{
					List<CaneDailyInwardReport> list=new ArrayList<>();
					while(rs.next())
					{
						CaneDailyInwardReport data=new CaneDailyInwardReport();
						data.setNslipNo(rs.getString("nslip_no"));
						String html="<table class='borderBottom subhead' style=\"width:100%;\"><tbody>";
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblDate+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+rs.getString("dslip_date")+"</td></tr>";
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblSLipNo+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+rs.getString("nslip_no")+"</td></tr>";
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblPlotNo+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+rs.getString("nplot_no")+"</td></tr>";
						String farmerName=rs.getString("nentity_uni_id")+" - "+DemoConvert2.ism_to_uni(rs.getString("farmer_name"));
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblFarmerName+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+farmerName+"</td></tr>";
						if(trailer!=null) {
							if(rs.getString("ntailor_front")!=null && !rs.getString("ntailor_front").trim().isEmpty()){
								html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblFrontTailer+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+trailer.getString(rs.getString("ntailor_front"))+"</td></tr>";
							} 
							if(rs.getString("ntailor_back")!=null && !rs.getString("ntailor_back").trim().isEmpty()){
								html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblBackTailer+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+trailer.getString(rs.getString("ntailor_back"))+"</td></tr>";
							}
						}
						if(wireRope!=null) {
							if(rs.getString("nwirerope_no")!=null && !rs.getString("nwirerope_no").trim().isEmpty()){
								html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblWireRope+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+wireRope.getString(rs.getString("nwirerope_no"))+"</td></tr>";
							}
						}
						String villeageName=rs.getString("nvillage_id")+" - "+DemoConvert2.ism_to_uni(rs.getString("village_name"));
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblVilleage+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+villeageName+"</td></tr>";
						String vehicleTypeName=rs.getString("nvehicle_type_id")+" - "+DemoConvert2.ism_to_uni(rs.getString("vehicle_type"));
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblVehicleType+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+vehicleTypeName+"</td></tr>";
						if(rs.getString("nharvestor_id")!=null && !rs.getString("nharvestor_id").trim().isEmpty()){
							try(PreparedStatement pst1=conn.prepareStatement("select TO_NCHAR(t.ventity_name_local)as name from gm_m_entity_master_detail t WHERE t.nentity_uni_id=?"))
							{
								pst1.setString(1, rs.getString("nharvestor_id"));
								try(ResultSet rs1=pst1.executeQuery())
								{
									if(rs1.next())
									{
										String name=rs.getString("nharvestor_id")+" - "+DemoConvert2.ism_to_uni(rs1.getString("name"));
										html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblHarvestorName+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+name+"</td></tr>";
									}
								}
							}
						}
						if(rs.getString("nbulluckcart_main_id")!=null && !rs.getString("nbulluckcart_main_id").trim().isEmpty()){
							try(PreparedStatement pst1=conn.prepareStatement("select TO_NCHAR(t.ventity_name_local)as name from gm_m_entity_master_detail t WHERE t.nentity_uni_id=?"))
							{
								pst1.setString(1, rs.getString("nbulluckcart_main_id"));
								try(ResultSet rs1=pst1.executeQuery())
								{
									if(rs1.next())
									{
										String name=rs.getString("nbulluckcart_main_id")+" - "+DemoConvert2.ism_to_uni(rs1.getString("name"));
										html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblMukadamName+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+name+"</td></tr>";
									}
								}
							}
						}
						if(rs.getString("ntransportor_id")!=null && !rs.getString("ntransportor_id").trim().isEmpty()){
							try(PreparedStatement pst1=conn.prepareStatement("select TO_NCHAR(t.ventity_name_local)as name from gm_m_entity_master_detail t WHERE t.nentity_uni_id=?"))
							{
								pst1.setString(1, rs.getString("ntransportor_id"));
								try(ResultSet rs1=pst1.executeQuery())
								{
									if(rs1.next())
									{
										String name=rs.getString("ntransportor_id")+" - "+DemoConvert2.ism_to_uni(rs1.getString("name"));
										html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblTranspoterName+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+name+"</td></tr>";
									}
								}
							}
						}
						if(rs.getString("nbulluckcart_id")!=null && !rs.getString("nbulluckcart_id").trim().isEmpty()){
							try(PreparedStatement pst1=conn.prepareStatement("select TO_NCHAR(t.ventity_name_local)as name from gm_m_entity_master_detail t WHERE t.nentity_uni_id=?"))
							{
								pst1.setString(1, rs.getString("nbulluckcart_id"));
								try(ResultSet rs1=pst1.executeQuery())
								{
									if(rs1.next())
									{
										String name=rs.getString("nbulluckcart_id")+" - "+DemoConvert2.ism_to_uni(rs1.getString("name"));
										html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblGadiwan+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+name+"</td></tr>";
									}
								}
							}
						}
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblVehicleNo+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+rs.getString("vvehicle_no")+"</td></tr>";
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblDistance+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+rs.getString("ndistance")+"</td></tr>";
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblShera+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+(rs.getString("remark")!=null?DemoConvert2.ism_to_uni(rs.getString("remark")):"")+"</td></tr>";
						html+="<tr><td class=\"cbody\">"+ConstantVeriables.lblNetWeight+"</td>"+"<td class=\"cbody\"> : </td><td class=\"cbody\">"+Constant.decimalFormat(rs.getDouble("nnet_weight"),"000")+"</td></tr>";
						html+="</tbody></table>";
						data.setHtml(html);
						list.add(data);
					}
					if(list.size()>0)
						{
						dailyCaneReportRes.setList(list);
						}else
						{
							dailyCaneReportRes.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(ConstantMessage.informationNotFound);
							dailyCaneReportRes.setSe(error);
						}
				}
			}
		} catch (SQLException | ParseException | JSONException e) {
			dailyCaneReportRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Cane Daily Inward Report "+e.getMessage());
			dailyCaneReportRes.setSe(error);
			e.printStackTrace();
		}
		return dailyCaneReportRes;
	}

	public ActionResponse verifySlip(String yearCode, String nslipNo, String chit_boy_id, ActionResponse verifyslipRes,
			Connection conn) {
		try(PreparedStatement pst1=conn.prepareStatement("update "+ConstantVeriables.weightSlipTableName+" t set t.VSLIP_VERIFY='Y' where t.nslip_no=? and t.vyear_id=?")){
					pst1.setString(1, nslipNo);
					pst1.setString(2, yearCode);
					int res=pst1.executeUpdate();
				if(res>0)
				{
					verifyslipRes.setActionComplete(true);
					verifyslipRes.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					verifyslipRes.setActionComplete(false);
					verifyslipRes.setFailMsg(ConstantMessage.saveFailed);
				}
			verifyslipRes.setSuccess(true);
		} catch (Exception e) {
			verifyslipRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Verify Slip "+e.getMessage());
			verifyslipRes.setSe(error);
			e.printStackTrace();
		}
		return verifyslipRes;
	}

	public CompletePlotResponse plotByFarmer(String yearCode, String farmerCode, String chit_boy_id, CompletePlotResponse plotdetailsRes,
			Connection conn) {
	try{
			try(PreparedStatement pst=conn.prepareStatement("select t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as farmer_name,TO_NCHAR(t1.village_name_local)as village_name,TO_NCHAR(t2.vsection_name_local)as vsection_name from GM_M_ENTITY_MASTER_DETAIL t,gm_m_village_master t1,gm_m_section_master t2  where t.nvillage_id=t1.nvillage_id and t1.nsection_id=t2.nsection_id and t.nentity_uni_id=?"))
			{
				pst.setString(1, farmerCode);
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						plotdetailsRes.setVyearId(yearCode);
						plotdetailsRes.setNentityUniId(farmerCode);
						plotdetailsRes.setVfarmerName(DemoConvert2.ism_to_uni(rs.getString("farmer_name")));
						plotdetailsRes.setVvillageName(DemoConvert2.ism_to_uni(rs.getString("village_name")));
						plotdetailsRes.setVsectName(DemoConvert2.ism_to_uni(rs.getString("vsection_name")));
					}
				}
			}
			
			HashMap<String, String> plotNextYear = new HashMap<>();
			String nextYear=(Integer.parseInt(yearCode.split("-")[0])+1)+"-"+(Integer.parseInt(yearCode.split("-")[1])+1);
			try(PreparedStatement pst=conn.prepareStatement("select t.nplot_no, t.Nplot_No_PREV_Year from "+ConstantVeriables.tabeAgriPlantaton+" t where t.Nplot_No_PREV_Year is not null and t.nentity_uni_id=? and t.vyear_id=?"))
			{
				pst.setString(1, farmerCode);
				pst.setString(2, nextYear);
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						plotNextYear.put(rs.getString("Nplot_No_PREV_Year"),rs.getString("nplot_no"));
					}
				}
			}
			ArrayList<CompletePlotDetails> plotDetailList = new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select To_NCHAR( vm.village_name_local) as vvillageName, t.nvillage_id,t.nplot_no,t.nhangam_id,t.ntentative_area,t.nvariety_id,t.nexpected_yield,t.dplantation_date,t.nharvested_flag,t.Nplot_No_PREV_Year,sum(t1.nnet_weight)as nharvestedTonnage, LISTAGG(t1.nslipboy_id, ',') WITHIN GROUP (ORDER BY t1.nslipboy_id)  emplist,TO_CHAR( MIN(t1.dslip_date) + (MAX(t1.dslip_date)  - MIN(t1.dslip_date) )/2 , 'dd/MM/yyyy') as mindate from "+ConstantVeriables.tabeAgriPlantaton+" t,"+ConstantVeriables.weightSlipTableName+" t1, Gm_m_Village_Master vm where vm.nvillage_id=t.nvillage_id and t.nplot_no=t1.nplot_no and t.vyear_id=t1.vyear_id and t.nentity_uni_id=t1.nentity_uni_id and t.nentity_uni_id=? and t.vyear_id=? and t1.nnet_weight>0 group by vm.village_name_local,t.nvillage_id,t.nplot_no,t.nhangam_id,t.ntentative_area,t.nvariety_id,t.nexpected_yield,t.dplantation_date,t.nharvested_flag,t.Nplot_No_PREV_Year"))
			{
				pst.setString(1, farmerCode);
				pst.setString(2, yearCode);
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						String emplist = "," + rs.getString("emplist") + ",";
						if(emplist.contains( "," + chit_boy_id + ",") || plotdetailsRes.getNuserRoleId().equals("113")) {
							CompletePlotDetails plotdtl=new CompletePlotDetails();
							plotdtl.setDplantationDate(Constant.DbDateToAppDate(rs.getDate("dplantation_date")));
							plotdtl.setNexpectedYield(Constant.decimalFormat(rs.getDouble("nexpected_yield"), "00"));
							plotdtl.setNhangamCode(rs.getString("nhangam_id"));
							plotdtl.setNharvestedTonnage(Constant.decimalFormat(rs.getDouble("nharvestedTonnage"), "000"));
							plotdtl.setNplotNo(rs.getString("nplot_no"));
							plotdtl.setNtentativeArea(Constant.decimalFormat(rs.getDouble("ntentative_area"), "00"));
							plotdtl.setNvarietyCode(rs.getString("nvariety_id"));
							plotdtl.setNvillCode(DemoConvert2.ism_to_uni(rs.getString("vvillageName")));
							plotdtl.setDmindate(rs.getString("mindate"));
							if(rs.getInt("nharvested_flag")==0)
							{
								plotdtl.setNstatus("1");
								plotdtl.setVstatusName(ConstantVeriables.statusName);
							}else
							{
								String nplotNo = rs.getString("nplot_no");
								if(plotNextYear.containsKey(nplotNo))
								{
									plotdtl.setNstatus("2");
									plotdtl.setVstatusName(ConstantVeriables.confirmRegister+plotNextYear.get(nplotNo));
								}else
								{
									plotdtl.setNstatus("3");
									plotdtl.setVstatusName(ConstantVeriables.confirm);
								}
							}
							if(plotdetailsRes.getNuserRoleId().equals("113")) {
								plotdtl.setNstatus("3");
							}
							plotDetailList.add(plotdtl);
						}
						
					}
				}
				plotdetailsRes.setPlotList(plotDetailList);
			}
		} catch (Exception e) {
			plotdetailsRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Plot By Farmer "+e.getMessage());
			plotdetailsRes.setSe(error);
			e.printStackTrace();
		}
		return plotdetailsRes;
	}

	public TransporterResponse checkSlipTime(String transCode, String vehicleType, String yearCode,
			Date slipGrator, int hold, TransporterResponse caneTransResponse, Connection conn) {
		String sql="select t.vout_time from "+ConstantVeriables.weightSlipTableName+" t where t.ntransportor_id=? and t.nvehicle_type_id=? and t.vyear_id=? and t.nnet_weight>0 and t.vout_time>? order by t.vout_time desc";
		try(PreparedStatement pst=conn.prepareStatement(sql))
		{
			DecimalFormat df = new DecimalFormat("00");
			java.sql.Timestamp slipGratorDate = new java.sql.Timestamp(slipGrator.getTime());
			int i = 1;
			pst.setString(i++, "T" + transCode);
			pst.setString(i++, vehicleType);
			pst.setString(i++, yearCode);
			pst.setTimestamp(i++, slipGratorDate);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					caneTransResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					
					String hrmin = (df.format((int)(hold/60)) + ":" + df.format(hold%60));
					error.setMsg(String.format(ConstantMessage.ERROR_Time_Slip, String.valueOf(hrmin), Constant.DbDateTimeToAppDateTime(rs.getTimestamp("vout_time"))));
					caneTransResponse.setSe(error);
				} else {
					caneTransResponse.setSuccess(true);
				}
			}
		} catch (Exception e) {
			caneTransResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Plot By Farmer " + e.getMessage());
			caneTransResponse.setSe(error);
			e.printStackTrace();
		}
		return caneTransResponse;
	}

	public HashMap<String, String> checkSlipTimeBulluck(String mukadamCode, String vehicleType, String yearCode,
			Date slipGrator, int hold, Connection conn) {
		HashMap<String, String> repeatData = new HashMap<>();
		String sql="select t.nbulluckcart_id, t.vout_time from "+ConstantVeriables.weightSlipTableName+" t where t.nbulluckcart_main_id=? and t.nvehicle_type_id=? and t.vyear_id=? and t.nnet_weight>0 and t.vout_time>? order by t.vout_time ASC";
		try(PreparedStatement pst=conn.prepareStatement(sql))
		{
			 java.sql.Timestamp slipGratorDate = new java.sql.Timestamp(slipGrator.getTime());
			int i=1;
			pst.setString(i++, "B"+mukadamCode);
			pst.setString(i++, vehicleType);
			pst.setString(i++, yearCode);
			pst.setTimestamp(i++, slipGratorDate);
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					repeatData.put(rs.getString("nbulluckcart_id"), Constant.DbDateTimeToAppDateTime(rs.getTimestamp("vout_time")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
	}
		return repeatData;
	
	}

	public CloseTransferResponse closeTransResponse(String transfer, String nplotNo, String vyearId, String dmindate,
			String narea, String chit_boy_id, CloseTransferResponse closetranResponse, Connection conn) {
		
		try {
			conn.setAutoCommit(false);
			String sql="update " + ConstantVeriables.tabeAgriPlantaton + " set nharvested_flag=1  where vyear_id=? and nplot_no=?";

			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, vyearId);
				pst.setString(i++, nplotNo);
				int upcount = pst.executeUpdate();
				if(upcount==0) {
					throw new Exception(ConstantMessage.ERROR_Plot_Not_Updated);
				}
			}
			long nplotNoNew = 1;
			if(transfer.equals("1")) {
				String yearCodeArr[] = vyearId.split("-");
				String saveYearCode = yearCodeArr[1] + "-" + (Integer.parseInt(yearCodeArr[1]) + 1);
				try (PreparedStatement pst = conn.prepareStatement("SELECT VYEAR_ID,NENTITY_UNI_ID,NPLOT_NO,VFORM_NO,NCROP_ID,NHANGAM_ID,VSURVE_NO,DENTRY_DATE,DPLANTATION_DATE,NIRRIGATION_ID,NVARIETY_ID,NAREA,NEXPECTED_YIELD,NIRRIGATION_METHOD_ID,NLAGAN_TYPE_ID,NVILLAGE_ID,NDISTANCE,DCREATE_DATE,DUPDATE_DATE,NCREATE_USER_ID,NUPDATE_USER_ID,NCOMPUTER,NSECTION_ID,CHANGE_FALG,DLATE_ENTRY_DATE,NFARMER_TYPE_ID,NFACT_ID,NGPS_AREA,NGPS_DISTANCE,NCONFIRM_FLAG,VPHOTO_PATH,VCONFIRMPHOTO_PATH,NTENTATIVE_AREA,NCROPWATER_CONDITION,NJUNE_FLAG,NAUGUST_FLAG,VSOIL_TEST,VGREEN_FERT,NLANE_TYPE_ID,VBENE_TREAT,VBESAL_DOSE,VDRIP_USED,NHARVEST_TYPE_ID,V_FERTI_VATAP_FLAG,NPLOT_NO_OFFLINE,NREG_FLAG_OFFLINE,NCONF_FLAG_OFFLINE,NHARVESTED_FLAG,NREGN_GPS_FLAG,VSTANDING_LATITUE,VSTANDING_LONGITUDE FROM " + ConstantVeriables.tabeAgriPlantaton + " WHERE NPLOT_NO=? AND VYEAR_ID=?")) {
					pst.setString(1, nplotNo);
					pst.setString(2, vyearId);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							
							// max plot no
							try (PreparedStatement pst1 = conn.prepareStatement("select max(NPLOT_NO) as plotno from "+ConstantVeriables.tabeAgriPlantaton+" t where t.VYEAR_ID=?")) {
								pst1.setString(1, saveYearCode);
								try (ResultSet rs1 = pst1.executeQuery()) {
									if (rs1.next()) {
										nplotNoNew = rs1.getLong("plotno") + 1;
									}
								}
							}
							
							try (PreparedStatement pst2 = conn.prepareStatement("INSERT INTO "+ConstantVeriables.tabeAgriPlantaton+"(VYEAR_ID,NENTITY_UNI_ID,NPLOT_NO,VFORM_NO,NCROP_ID,NHANGAM_ID,VSURVE_NO,DENTRY_DATE,DPLANTATION_DATE,NIRRIGATION_ID,NVARIETY_ID,NAREA,NEXPECTED_YIELD,NIRRIGATION_METHOD_ID,NLAGAN_TYPE_ID,NVILLAGE_ID,NDISTANCE,DCREATE_DATE,DUPDATE_DATE,NCREATE_USER_ID,NUPDATE_USER_ID,NCOMPUTER,NSECTION_ID,CHANGE_FALG,DLATE_ENTRY_DATE,NFARMER_TYPE_ID,NFACT_ID,NGPS_AREA,NGPS_DISTANCE,NCONFIRM_FLAG,VPHOTO_PATH,VCONFIRMPHOTO_PATH,NTENTATIVE_AREA,NCROPWATER_CONDITION,NJUNE_FLAG,NAUGUST_FLAG,VSOIL_TEST,VGREEN_FERT,NLANE_TYPE_ID,VBENE_TREAT,VBESAL_DOSE,VDRIP_USED,NHARVEST_TYPE_ID,V_FERTI_VATAP_FLAG,NPLOT_NO_OFFLINE,NREG_FLAG_OFFLINE,NCONF_FLAG_OFFLINE,NHARVESTED_FLAG,NREGN_GPS_FLAG,VSTANDING_LATITUE,VSTANDING_LONGITUDE,Nplot_No_PREV_Year) "
									+ "VALUES(?,?,?,?,?,?,?,TRUNC(SYSDATE),TO_DATE(?,'dd-Mon-yyyy'),?,?,?,?,?,?,?,?,SYSDATE,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
								int i = 1;
								pst2.setString(i++, saveYearCode);
								pst2.setString(i++, rs.getString("NENTITY_UNI_ID"));
								pst2.setLong(i++, nplotNoNew);
								pst2.setString(i++, rs.getString("VFORM_NO"));
								pst2.setString(i++, rs.getString("NCROP_ID"));
								int hangamId = rs.getInt("NHANGAM_ID");
								if (hangamId < 4)
									pst2.setInt(i++, 4);
								else if (hangamId < 6)
									pst2.setInt(i++, 6);
								else 
									throw new Exception(ConstantMessage.ERROR_KHODAVA_NIDAVA_NOT_ALLOW);
								pst2.setString(i++, rs.getString("VSURVE_NO"));
								pst2.setString(i++, Constant.AppDateDbDate(dmindate));
								pst2.setString(i++, rs.getString("NIRRIGATION_ID"));
								pst2.setString(i++, rs.getString("NVARIETY_ID"));
								pst2.setString(i++, narea);
								pst2.setString(i++, null); //NEXPECTED_YIELD
								pst2.setString(i++, rs.getString("NIRRIGATION_METHOD_ID"));
								pst2.setString(i++, rs.getString("NLAGAN_TYPE_ID"));
								pst2.setString(i++, rs.getString("NVILLAGE_ID"));
								pst2.setString(i++, rs.getString("NDISTANCE"));
								pst2.setString(i++, chit_boy_id);
								pst2.setString(i++, chit_boy_id);
								pst2.setString(i++, rs.getString("NCOMPUTER"));
								pst2.setString(i++, rs.getString("NSECTION_ID"));
								pst2.setString(i++, "0");//"CHANGE_FALG"
								pst2.setDate(i++, rs.getDate("DLATE_ENTRY_DATE"));
								pst2.setString(i++, rs.getString("NFARMER_TYPE_ID"));
								pst2.setString(i++, "1");//rs.getString("NFACT_ID")
								pst2.setString(i++, rs.getString("NGPS_AREA"));
								pst2.setString(i++, rs.getString("NGPS_DISTANCE"));
								pst2.setInt(i++, 0);//NCONFIRM_FLAG
								pst2.setString(i++, rs.getString("VPHOTO_PATH"));
								pst2.setString(i++, null);//VCONFIRMPHOTO_PATH
								pst2.setString(i++, null);//NTENTATIVE_AREA
								pst2.setString(i++, null);//rs.getString("NCROPWATER_CONDITION")
								pst2.setInt(i++, 0);//NJUNE_FLAG
								pst2.setInt(i++, 0);//NAUGUST_FLAG
								pst2.setString(i++, rs.getString("VSOIL_TEST"));
								pst2.setString(i++, rs.getString("VGREEN_FERT"));
								pst2.setString(i++, rs.getString("NLANE_TYPE_ID"));
								pst2.setString(i++, rs.getString("VBENE_TREAT"));
								pst2.setString(i++, rs.getString("VBESAL_DOSE"));
								pst2.setString(i++, rs.getString("VDRIP_USED"));
								pst2.setString(i++, rs.getString("NHARVEST_TYPE_ID"));
								pst2.setString(i++, rs.getString("V_FERTI_VATAP_FLAG"));
								pst2.setString(i++, System.currentTimeMillis()+"@"+chit_boy_id);
								pst2.setString(i++, "1");
								pst2.setString(i++, null); //NCONF_FLAG_OFFLINE
								pst2.setString(i++, "0"); //NHARVESTED_FLAG
								pst2.setString(i++, "3"); //rs.getString("NREGN_GPS_FLAG")
								pst2.setString(i++, rs.getString("VSTANDING_LATITUE"));
								pst2.setString(i++, rs.getString("VSTANDING_LONGITUDE"));
								pst2.setString(i++, nplotNo);
								int upcount = pst2.executeUpdate();
								if(upcount==0) 
									throw new Exception(ConstantMessage.ERROR_Plot_Not_Inserted);		
								try (PreparedStatement pst1 = conn.prepareStatement("select t.nplot_no, t.vyear_id, t.vlatitude, t.vlongitude, t.nacc, t.ntrans_no from APP_T_REGISTRATION_LATLNGS t where t.nplot_no=? and t.vyear_id=?")) {
									pst1.setString(1, nplotNo);
									pst1.setString(2, vyearId);
									try(ResultSet rs1 = pst1.executeQuery()) {
										while(rs1.next()) {
											try (PreparedStatement pst3 = conn.prepareStatement("INSERT INTO APP_T_REGISTRATION_LATLNGS(vyear_id,nplot_no,vlatitude,vlongitude,nacc,ntrans_no) VALUES(?,?,?,?,?,?)")) {
												i = 1;
												pst3.setString(i++, saveYearCode);
												pst3.setLong(i++, nplotNoNew);
												pst3.setString(i++, rs1.getString("vlatitude"));
												pst3.setString(i++, rs1.getString("vlongitude"));
												pst3.setString(i++, rs1.getString("nacc"));
												pst3.setString(i++, rs1.getString("ntrans_no"));
												pst3.executeUpdate();
											}
										}
									}
								}
							}
						}
					}
				}				
			}
			closetranResponse.setSuccess(true);
			closetranResponse.setActionComplete(true);
			closetranResponse.setSuccessMsg(ConstantMessage.saveSuccess);
			int transferInt = Integer.parseInt(transfer);
			if (transferInt == 2) {
				closetranResponse.setNstatusNew("2");
				closetranResponse.setVstatusNameNew(ConstantVeriables.confirm);
			} else {
				closetranResponse.setNstatusNew("3");
				closetranResponse.setVstatusNameNew(ConstantVeriables.confirmRegister + nplotNoNew);
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			closetranResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Close Plot " + e.getMessage());
			closetranResponse.setSe(error);
			e.printStackTrace();
		} finally {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return closetranResponse;
	}

	public CaneTransitResponse vehicleTransitInfo(String date, String yearId, String villageId, String chit_boy_id,
			CaneTransitResponse caneTransResponse, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.vyear_id, t.ddate, t.nentry_user_id, t.nvillage_id, t.nspare_area, t.nspare_tonnage, t.ntoli_local, t.ntoli_outside, t.ntoli_self, t.ntoli_total, t.ntoli_upto_4, t.ntoli_after_4, t.ntoli_absent, t.ntoli_extra_trip, t.nbajat_local, t.nbajat_outside, t.nbajat_self, t.nbajat_total, t.nbajat_upto_4, t.nbajat_after_4, t.nbajat_absent, t.nbajat_extra_trip, t.nmachine_local, t.nmachine_outside, t.nmachine_total, t.nmachine_upto_4, t.nmachine_after_4, t.nbailgadi_local, t.nbailgadi_outside, t.nbailgadi_total, t.dentry_date, t.dupdate_date from APP_T_VEHICLE_TRANSIT t where t.ddate=TO_DATE(?,'dd-Mon-yyyy') and t.nentry_user_id=? and t.nvillage_id=? and vyear_id=?"))
		{
			int i=1;
			pst.setString(i++, Constant.AppDateDbDate(date));
			pst.setString(i++, chit_boy_id);
			pst.setString(i++, villageId);
			pst.setString(i++, yearId);
			try(ResultSet rs=pst.executeQuery())
			{
				caneTransResponse.setTransEdit("0");
				if(rs.next())
				{
					caneTransResponse.setTransEdit("1");
					caneTransResponse.setDateVal(Constant.DbDateToAppDate(rs.getDate("ddate")));
					/*caneTransResponse.setYearId(rs.getString("vyear_id"));*/
					caneTransResponse.setVillageId(rs.getString("nvillage_id"));
				    caneTransResponse.setLocalTolya(rs.getString("ntoli_local"));
				    caneTransResponse.setLocalBajat(rs.getString("nbajat_local"));
				    caneTransResponse.setLocalMachine(rs.getString("nmachine_local"));
				    caneTransResponse.setLocalBulluckCart(rs.getString("nbailgadi_local"));
				    caneTransResponse.setOutsideTolya(rs.getString("ntoli_outside"));
				    caneTransResponse.setOutsideBajat(rs.getString("nbajat_outside"));
				    caneTransResponse.setOutsideMachine(rs.getString("nmachine_outside"));
				    caneTransResponse.setOutsideBulluckCart(rs.getString("nbailgadi_outside"));
				    caneTransResponse.setSelfTolya(rs.getString("ntoli_self"));
				    caneTransResponse.setSelfBajat(rs.getString("nbajat_self"));
				    caneTransResponse.setTotalTolya(rs.getString("ntoli_total"));
				    caneTransResponse.setTotalBajat(rs.getString("nbajat_total"));
				    caneTransResponse.setTotalMachine(rs.getString("nmachine_total"));
				    caneTransResponse.setTotalBulluckCart(rs.getString("nbailgadi_total"));
				    caneTransResponse.setCloseTolya(rs.getString("ntoli_absent"));
				    caneTransResponse.setCloseBajat(rs.getString("nbajat_absent"));
				    caneTransResponse.setBefore4pmTolya(rs.getString("ntoli_upto_4"));
				    caneTransResponse.setBefore4pmBajat(rs.getString("nbajat_upto_4"));
				    caneTransResponse.setBefore4pmMachine(rs.getString("nmachine_upto_4"));
				    caneTransResponse.setAfter4pmTolya(rs.getString("ntoli_after_4"));
				    caneTransResponse.setAfter4pmBajat(rs.getString("nbajat_after_4"));
				    caneTransResponse.setAfter4pmMachine(rs.getString("nmachine_after_4"));
				    caneTransResponse.setExtraTripToli(rs.getString("ntoli_extra_trip"));
				    caneTransResponse.setExtraTripBajat(rs.getString("nbajat_extra_trip"));
				    caneTransResponse.setSpareArea(rs.getString("nspare_area"));
				    caneTransResponse.setSpareTonnage(rs.getString("nspare_tonnage"));


				}
			}
		} catch (Exception e) {
			caneTransResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("get Cane Trans " + e.getMessage());
			caneTransResponse.setSe(error);
			e.printStackTrace();
		}
		return caneTransResponse;
	}
	
	public ActionResponse saveTransit(CaneTransitResponse saveTransit, String chit_boy_id, ActionResponse saveRes,
			Connection conn) {
		String sql="insert into APP_T_VEHICLE_TRANSIT (nspare_area,nspare_tonnage,ntoli_local,ntoli_outside,ntoli_self,ntoli_total,ntoli_upto_4,ntoli_after_4,ntoli_absent,ntoli_extra_trip,nbajat_local,nbajat_outside,nbajat_self,nbajat_total,nbajat_upto_4,nbajat_after_4,nbajat_absent,nbajat_extra_trip,nmachine_local,nmachine_outside,nmachine_total,nmachine_upto_4,nmachine_after_4,nbailgadi_local,nbailgadi_outside,nbailgadi_total,dentry_date,ddate,nentry_user_id,nvillage_id,vyear_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?)";
		if(!saveTransit.getTransEdit().equals("0"))
			sql="UPDATE APP_T_VEHICLE_TRANSIT SET nspare_area=?,nspare_tonnage=?,ntoli_local=?,ntoli_outside=?,ntoli_self=?,ntoli_total=?,ntoli_upto_4=?,ntoli_after_4=?,ntoli_absent=?,ntoli_extra_trip=?,nbajat_local=?,nbajat_outside=?,nbajat_self=?,nbajat_total=?,nbajat_upto_4=?,nbajat_after_4=?,nbajat_absent=?,nbajat_extra_trip=?,nmachine_local=?,nmachine_outside=?,nmachine_total=?,nmachine_upto_4=?,nmachine_after_4=?,nbailgadi_local=?,nbailgadi_outside=?,nbailgadi_total=?,dupdate_date=SYSDATE WHERE ddate=TO_DATE(?,'dd-Mon-yyyy') AND nentry_user_id=? AND nvillage_id=? AND vyear_id=?";
		try(PreparedStatement pst=conn.prepareStatement(sql))
		{
			int i=1;
		    pst.setString(i++, saveTransit.getSpareArea());
		    pst.setString(i++, saveTransit.getSpareTonnage());
		    pst.setString(i++, saveTransit.getLocalTolya());
		    pst.setString(i++, saveTransit.getOutsideTolya());
		    pst.setString(i++, saveTransit.getSelfTolya());
		    pst.setString(i++, saveTransit.getTotalTolya());
		    pst.setString(i++, saveTransit.getBefore4pmTolya());
		    pst.setString(i++, saveTransit.getAfter4pmTolya());
		    pst.setString(i++, saveTransit.getCloseTolya());
		    pst.setString(i++, saveTransit.getExtraTripToli());
		    pst.setString(i++, saveTransit.getLocalBajat());
		    pst.setString(i++, saveTransit.getOutsideBajat());
		    pst.setString(i++, saveTransit.getSelfBajat());
		    pst.setString(i++, saveTransit.getTotalBajat());
		    pst.setString(i++, saveTransit.getBefore4pmBajat());
		    pst.setString(i++, saveTransit.getAfter4pmBajat());
		    pst.setString(i++, saveTransit.getCloseBajat());
		    pst.setString(i++, saveTransit.getExtraTripBajat());
			pst.setString(i++, saveTransit.getLocalMachine());
			pst.setString(i++, saveTransit.getOutsideMachine());
			pst.setString(i++, saveTransit.getTotalMachine());
			pst.setString(i++, saveTransit.getBefore4pmMachine());
			pst.setString(i++, saveTransit.getAfter4pmMachine());
		    pst.setString(i++, saveTransit.getLocalBulluckCart());
		    pst.setString(i++, saveTransit.getOutsideBulluckCart());
		    pst.setString(i++, saveTransit.getTotalBulluckCart());
			pst.setString(i++, Constant.AppDateDbDate(saveTransit.getDateVal()));
			pst.setString(i++, chit_boy_id);
			pst.setString(i++, saveTransit.getVillageId());
			pst.setString(i++, saveTransit.getYearId());
			int res=pst.executeUpdate();
			if(res>0)
			{
				saveRes.setActionComplete(true);
				saveRes.setSuccessMsg(ConstantMessage.saveSuccess);
			}else
			{
				saveRes.setActionComplete(false);
				saveRes.setFailMsg(ConstantMessage.saveFailed);
			}
			saveRes.setSuccess(true);
		}catch (Exception e) {
			saveRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("save Cane Trans " + e.getMessage());
			saveRes.setSe(error);
			e.printStackTrace();
		}
		
		return saveRes;
	}

	public MainResponse checkGutKhade(String vehicleType, String yearCode, String nsectionId,
			MainResponse mainResponse, Connection conn) {
		String sql="select to_char(t.vtime_end - interval '6' hour,'dd/MM/yyyy hh24:mi') as valDate, t.vtime_end from APP_M_GUTKHADE t where t.vactive = 'A' and t.vtime_start<=sysdate and t.vtime_end - interval '6' hour > sysdate and t.nsection_id =? and t.vyear_id=? and t.nvehicle_type_id=?";
		try(PreparedStatement pst=conn.prepareStatement(sql))
		{
			int i = 1;
			pst.setString(i++, nsectionId);
			pst.setString(i++, yearCode);
			pst.setString(i++, vehicleType);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					mainResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(String.format(ConstantMessage.GUT_KHADE_ERROR, rs.getString("valDate")));
					mainResponse.setSe(error);
				} else {
					mainResponse.setSuccess(true);
				}
			}
		} catch (Exception e) {
			mainResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Gut khade Check " + e.getMessage());
			mainResponse.setSe(error);
			e.printStackTrace();
		}
		return mainResponse;
	}

	public TableResponse sectionWiseUsRawanaReport(String dateVal, String yearId, TableResponse reqResponse,
			Connection conn) {
		try {
			TableReportBean usRawanaBean = new TableReportBean();
			int noofHeadRow =4;
			usRawanaBean.setNoofHeads(noofHeadRow);
			usRawanaBean.setFooter(true);
			usRawanaBean.setMarathi(true);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			HashMap<String, Integer> colSpan = new HashMap<>();
			HashMap<String, Integer> rowSpan = new HashMap<>();
			HashMap<String, Integer> textAlign = new HashMap<>();
			
			ArrayList<ArrayList<String>> usRawanaTabaleData = new ArrayList<>();
			ArrayList<String> usRawanaRow = new ArrayList<>();
			usRawanaRow.add(ConstantVeriables.factoryName);
			usRawanaTabaleData.add(usRawanaRow);
			usRawanaRow = new ArrayList<>();
			usRawanaRow.add(String.format(ConstantVeriables.usRawanaReportHead, dateVal));
			usRawanaTabaleData.add(usRawanaRow);
			
			usRawanaRow = new ArrayList<>();
			usRawanaRow.add("");
			usRawanaRow.add("");
			usRawanaRow.add("");
			usRawanaRow.add("");
			usRawanaRow.add(ConstantVeriables.rawanaToli);
			usRawanaRow.add(ConstantVeriables.rawanaBajat);
			usRawanaRow.add(ConstantVeriables.rawanaTodniYantra);
			usRawanaRow.add(ConstantVeriables.rawanaBaigadi);
			usRawanaTabaleData.add(usRawanaRow);
			
			usRawanaRow = new ArrayList<>();
			
			usRawanaRow.add(ConstantVeriables.srno);
			usRawanaRow.add(ConstantVeriables.village);
			usRawanaRow.add(ConstantVeriables.area);
			usRawanaRow.add(ConstantVeriables.tonnage);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.selfHarv);
			usRawanaRow.add(ConstantVeriables.total);
			usRawanaRow.add(ConstantVeriables.before_4_ravana);
			usRawanaRow.add(ConstantVeriables.after_4_ravana);
			usRawanaRow.add(ConstantVeriables.band_yantrana);
			usRawanaRow.add(ConstantVeriables.extra_trips);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.selfHarv);
			usRawanaRow.add(ConstantVeriables.total);
			usRawanaRow.add(ConstantVeriables.before_4_ravana);
			usRawanaRow.add(ConstantVeriables.after_4_ravana);
			usRawanaRow.add(ConstantVeriables.band_yantrana);
			usRawanaRow.add(ConstantVeriables.extra_trips);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.total);
			usRawanaRow.add(ConstantVeriables.before_4_ravana);
			usRawanaRow.add(ConstantVeriables.after_4_ravana);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.total);
			
			usRawanaTabaleData.add(usRawanaRow);
			
			
			boldIndicator.put("0-*", true);
			boldIndicator.put("1-*", true);
			boldIndicator.put("2-*", true);
			boldIndicator.put("3-*", true);
			
			colSpan.put("0-0", 28);
			colSpan.put("1-0", 28);
			colSpan.put("2-4", 8);
			colSpan.put("2-5", 8);
			colSpan.put("2-6", 5);
			colSpan.put("2-7", 3);
			
			rowSpan.put("2-0", 2);
			rowSpan.put("2-1", 2);
			rowSpan.put("2-2", 2);
			rowSpan.put("2-3", 2);
			
			rowSpan.put("3-0", 4);
			rowSpan.put("3-1", 4);
			rowSpan.put("3-2", 4);
			rowSpan.put("3-3", 4);
			
			int srno = 1;
			
			ArrayList<String> usRawanaFooter = new ArrayList<>();
			
			usRawanaFooter.add(ConstantVeriables.total);
			usRawanaFooter.add("0.00");
			usRawanaFooter.add("0.000");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			DecimalFormat dfArea = new DecimalFormat("#0.00");
			DecimalFormat dfTon = new DecimalFormat("#0.000");
			
			try(PreparedStatement pst=conn.prepareStatement("select m.nsection_id_main,TO_NCHAR(m.vsection_name_local_main)as section_name,sum(t2.nspare_area) as nspare_area,sum(t2.nspare_tonnage) as nspare_tonnage, sum(nvl(t2.ntoli_local,0))as ntoli_local, sum(nvl(t2.ntoli_outside,0))as ntoli_outside,sum(nvl(t2.ntoli_self,0))as ntoli_self,sum(nvl(t2.ntoli_total,0))as ntoli_total,sum(nvl(t2.ntoli_upto_4,0))as ntoli_upto_4, sum(nvl(t2.ntoli_after_4,0))as ntoli_after_4, sum(nvl(t2.ntoli_absent,0)) as ntoli_absent, sum(nvl(t2.ntoli_extra_trip,0)) as ntoli_extra_trip , sum(nvl(t2.nbajat_local,0))as nbajat_local, sum(nvl(t2.nbajat_outside,0))as nbajat_outside,sum(nvl(t2.nbajat_self,0))as nbajat_self,sum(nvl(t2.nbajat_total,0)) as nbajat_total,sum(nvl(t2.nbajat_upto_4,0))as nbajat_upto_4, sum(nvl(t2.nbajat_after_4,0))as nbajat_after_4, sum(nvl(t2.nbajat_absent,0)) as nbajat_absent, sum(nvl(t2.nbajat_extra_trip,0)) as nbajat_extra_trip , sum(nvl(t2.nmachine_local,0))as nmachine_local, sum(nvl(t2.nmachine_outside,0))as nmachine_outside,sum(nvl(t2.nmachine_total,0))as nmachine_total, sum(nvl(t2.nmachine_upto_4,0))as nmachine_upto_4,sum(nvl(t2.nmachine_after_4,0))as nmachine_after_4, sum(nvl(t2.nbailgadi_local,0))as nbailgadi_local, sum(nvl(t2.nbailgadi_outside,0))as nbailgadi_outside,sum(nvl(t2.nbailgadi_total,0))as nbailgadi_total from gm_m_section_master_main m, gm_m_section_master t Left join gm_m_village_master t1 on t.nsection_id=t1.nsection_id LEFT JOIN App_t_Vehicle_Transit t2 on t1.nvillage_id=t2.nvillage_id  and t2.vyear_id=? and t2.ddate=TO_DATE(?,'dd/MM/yyyy') WHERE m.nsection_id_main = t.nsection_id_main group by m.nsection_id_main,m.vsection_name_local_main order by m.nsection_id_main,m.vsection_name_local_main")){
				int i = 1;
				pst.setString(i++, yearId);
				pst.setString(i++, dateVal);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						
						double nspare_area=rs.getDouble("nspare_area");
						double nspare_tonnage=rs.getDouble("nspare_tonnage");
		
						int ntoli_local=rs.getInt("ntoli_local");
						int ntoli_outside=rs.getInt("ntoli_outside");
						int ntoli_self=rs.getInt("ntoli_self");
						int ntoli_total=rs.getInt("ntoli_total");
						int ntoli_upto_4=rs.getInt("ntoli_upto_4");
						int ntoli_after_4=rs.getInt("ntoli_after_4");
						int ntoli_absent=rs.getInt("ntoli_absent");
						int ntoli_extra_trip=rs.getInt("ntoli_extra_trip");
						
						int nbajat_local=rs.getInt("nbajat_local");
						int nbajat_outside=rs.getInt("nbajat_outside");
						int nbajat_self=rs.getInt("nbajat_self");
						int nbajat_total=rs.getInt("nbajat_total");
						int nbajat_upto_4=rs.getInt("nbajat_upto_4");
						int nbajat_after_4=rs.getInt("nbajat_after_4");
						int nbajat_absent=rs.getInt("nbajat_absent");
						int nbajat_extra_trip=rs.getInt("nbajat_extra_trip");
												
						int nmachine_local=rs.getInt("nmachine_local");
						int nmachine_outside=rs.getInt("nmachine_outside");
						int nmachine_total=rs.getInt("nmachine_total");
						int nmachine_upto_4=rs.getInt("nmachine_upto_4");
						int nmachine_after_4=rs.getInt("nmachine_after_4");
						
						int nbailgadi_local=rs.getInt("nbailgadi_local");
						int nbailgadi_outside=rs.getInt("nbailgadi_outside");
						int nbailgadi_total=rs.getInt("nbailgadi_total");
						
						usRawanaRow = new ArrayList<>();
						usRawanaRow.add(String.valueOf(srno++));
						usRawanaRow.add(DemoConvert2.ism_to_uni(rs.getString("section_name")));
						usRawanaRow.add(dfArea.format(nspare_area));
						usRawanaRow.add(dfTon.format(nspare_tonnage));

						usRawanaRow.add(String.valueOf(ntoli_local));
						usRawanaRow.add(String.valueOf(ntoli_outside));
						usRawanaRow.add(String.valueOf(ntoli_self));
						usRawanaRow.add(String.valueOf(ntoli_total));
						usRawanaRow.add(String.valueOf(ntoli_upto_4));
						usRawanaRow.add(String.valueOf(ntoli_after_4));
						usRawanaRow.add(String.valueOf(ntoli_absent));
						usRawanaRow.add(String.valueOf(ntoli_extra_trip));

						usRawanaRow.add(String.valueOf(nbajat_local));
						usRawanaRow.add(String.valueOf(nbajat_outside));
						usRawanaRow.add(String.valueOf(nbajat_self));
						usRawanaRow.add(String.valueOf(nbajat_total));
						usRawanaRow.add(String.valueOf(nbajat_upto_4));
						usRawanaRow.add(String.valueOf(nbajat_after_4));
						usRawanaRow.add(String.valueOf(nbajat_absent));
						usRawanaRow.add(String.valueOf(nbajat_extra_trip));

						usRawanaRow.add(String.valueOf(nmachine_local));
						usRawanaRow.add(String.valueOf(nmachine_outside));
						usRawanaRow.add(String.valueOf(nmachine_total));
						usRawanaRow.add(String.valueOf(nmachine_upto_4));
						usRawanaRow.add(String.valueOf(nmachine_after_4));

						usRawanaRow.add(String.valueOf(nbailgadi_local));
						usRawanaRow.add(String.valueOf(nbailgadi_outside));
						usRawanaRow.add(String.valueOf(nbailgadi_total));

						usRawanaTabaleData.add(usRawanaRow);
						int pos = 1;
						usRawanaFooter.set(pos, dfArea.format(Double.parseDouble(usRawanaFooter.get(pos++)) + nspare_area));
						usRawanaFooter.set(pos, dfTon.format(Double.parseDouble(usRawanaFooter.get(pos++)) + nspare_tonnage));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_self));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_total));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_upto_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_after_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_absent));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_extra_trip));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_self));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_total));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_upto_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_after_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_absent));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_extra_trip));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_total));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_upto_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_after_4));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbailgadi_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbailgadi_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbailgadi_total));
					}
				}
			}
			
			int totRow = usRawanaTabaleData.size();
			boldIndicator.put(totRow + "-*", true);
			colSpan.put(totRow + "-0", 2);
			textAlign.put(totRow + "-0", 2);
			usRawanaTabaleData.add(usRawanaFooter);
			
			usRawanaBean.setTableData(usRawanaTabaleData);
			usRawanaBean.setBoldIndicator(boldIndicator);
			usRawanaBean.setRowColSpan(colSpan);
			usRawanaBean.setRowSpan(rowSpan);
			usRawanaBean.setTextAlign(textAlign);
			usRawanaBean.setColWidth(new Integer[]{6,10,6,6,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3});
			reqResponse.setTableData(usRawanaBean);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("US rawana report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
	}

	public TableResponse villeageWiseUsRawanaReport(String dateVal, String yearId, String sectionId,
			TableResponse reqResponse, Connection conn) {
		try {
			TableReportBean usRawanaBean = new TableReportBean();
			int noofHeadRow =4;
			usRawanaBean.setNoofHeads(noofHeadRow);
			usRawanaBean.setFooter(true);
			usRawanaBean.setMarathi(true);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			HashMap<String, Integer> colSpan = new HashMap<>();
			HashMap<String, Integer> rowSpan = new HashMap<>();
			HashMap<String, Integer> textAlign = new HashMap<>();
			
			ArrayList<ArrayList<String>> usRawanaTabaleData = new ArrayList<>();
			ArrayList<String> usRawanaRow = new ArrayList<>();
			usRawanaRow.add(ConstantVeriables.factoryName);
			usRawanaTabaleData.add(usRawanaRow);
			usRawanaRow = new ArrayList<>();
			usRawanaRow.add(String.format(ConstantVeriables.usRawanaReportHead, dateVal));
			usRawanaTabaleData.add(usRawanaRow);
			
			usRawanaRow = new ArrayList<>();
			usRawanaRow.add("");
			usRawanaRow.add("");
			usRawanaRow.add("");
			usRawanaRow.add("");
			usRawanaRow.add(ConstantVeriables.rawanaToli);
			usRawanaRow.add(ConstantVeriables.rawanaBajat);
			usRawanaRow.add(ConstantVeriables.rawanaTodniYantra);
			usRawanaRow.add(ConstantVeriables.rawanaBaigadi);
			usRawanaTabaleData.add(usRawanaRow);
			
			usRawanaRow = new ArrayList<>();
			
			usRawanaRow.add(ConstantVeriables.srno);
			usRawanaRow.add(ConstantVeriables.village);
			usRawanaRow.add(ConstantVeriables.area);
			usRawanaRow.add(ConstantVeriables.tonnage);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.selfHarv);
			usRawanaRow.add(ConstantVeriables.total);
			usRawanaRow.add(ConstantVeriables.before_4_ravana);
			usRawanaRow.add(ConstantVeriables.after_4_ravana);
			usRawanaRow.add(ConstantVeriables.band_yantrana);
			usRawanaRow.add(ConstantVeriables.extra_trips);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.selfHarv);
			usRawanaRow.add(ConstantVeriables.total);
			usRawanaRow.add(ConstantVeriables.before_4_ravana);
			usRawanaRow.add(ConstantVeriables.after_4_ravana);
			usRawanaRow.add(ConstantVeriables.band_yantrana);
			usRawanaRow.add(ConstantVeriables.extra_trips);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.total);
			usRawanaRow.add(ConstantVeriables.before_4_ravana);
			usRawanaRow.add(ConstantVeriables.after_4_ravana);
			
			usRawanaRow.add(ConstantVeriables.local);
			usRawanaRow.add(ConstantVeriables.outside);
			usRawanaRow.add(ConstantVeriables.total);
			
			usRawanaTabaleData.add(usRawanaRow);
			
			boldIndicator.put("0-*", true);
			boldIndicator.put("1-*", true);
			boldIndicator.put("2-*", true);
			boldIndicator.put("3-*", true);
			
			colSpan.put("0-0", 28);
			colSpan.put("1-0", 28);
			colSpan.put("2-4", 8);
			colSpan.put("2-5", 8);
			colSpan.put("2-6", 5);
			colSpan.put("2-7", 3);
			
			rowSpan.put("2-0", 2);
			rowSpan.put("2-1", 2);
			rowSpan.put("2-2", 2);
			rowSpan.put("2-3", 2);
			
			rowSpan.put("3-0", 4);
			rowSpan.put("3-1", 4);
			rowSpan.put("3-2", 4);
			rowSpan.put("3-3", 4);
			
			int srno = 1;
			
			ArrayList<String> usRawanaFooter = new ArrayList<>();
			
			usRawanaFooter.add(ConstantVeriables.total);
			usRawanaFooter.add("0.00");
			usRawanaFooter.add("0.000");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			usRawanaFooter.add("0");
			
			String villCode="";
			try (PreparedStatement pst = conn.prepareStatement("select DISTINCT t.nvillage_id from cr_t_plantation t,gm_m_village_master t1 where t.nvillage_id=t1.nvillage_id and  t.nconfirm_flag <> 0 and t.nexpected_yield > 0 and t.nsection_id=? and t.vyear_id=?")) {
				int i = 1;
				pst.setString(i++, sectionId);
				pst.setString(i++, yearId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						if (!villCode.trim().isEmpty())
							villCode = villCode + ",";
						villCode += rs.getString("nvillage_id");
					}
				}
			}

			ArrayList<Integer> vlist=new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t2.nvillage_id from App_t_Vehicle_Transit t2 WHERE t2.vyear_id=? and t2.ddate=TO_DATE(?,'dd/MM/yyyy') and t2.ntoli_total=0 and t2.nbajat_total=0 and t2.nmachine_local=0 and t2.nbailgadi_total=0")){
				int i = 1;
				pst.setString(i++, yearId);
				pst.setString(i++, dateVal);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						vlist.add(rs.getInt("nvillage_id"));
					}
				}
			}
			
			DecimalFormat dfArea = new DecimalFormat("#0.00");
			DecimalFormat dfTon = new DecimalFormat("#0.000");
			try(PreparedStatement pst=conn.prepareStatement("select t1.nvillage_id,TO_NCHAR(t1.village_name_local)as vill_name,sum(t2.nspare_area) as nspare_area,sum(t2.nspare_tonnage) as nspare_tonnage, sum(nvl(t2.ntoli_local,0))as ntoli_local, sum(nvl(t2.ntoli_outside,0))as ntoli_outside,sum(nvl(t2.ntoli_self,0))as ntoli_self,sum(nvl(t2.ntoli_total,0))as ntoli_total,sum(nvl(t2.ntoli_upto_4,0))as ntoli_upto_4, sum(nvl(t2.ntoli_after_4,0))as ntoli_after_4, sum(nvl(t2.ntoli_absent,0)) as ntoli_absent, sum(nvl(t2.ntoli_extra_trip,0)) as ntoli_extra_trip , sum(nvl(t2.nbajat_local,0))as nbajat_local, sum(nvl(t2.nbajat_outside,0))as nbajat_outside,sum(nvl(t2.nbajat_self,0))as nbajat_self,sum(nvl(t2.nbajat_total,0)) as nbajat_total,sum(nvl(t2.nbajat_upto_4,0))as nbajat_upto_4, sum(nvl(t2.nbajat_after_4,0))as nbajat_after_4, sum(nvl(t2.nbajat_absent,0)) as nbajat_absent, sum(nvl(t2.nbajat_extra_trip,0)) as nbajat_extra_trip , sum(nvl(t2.nmachine_local,0))as nmachine_local, sum(nvl(t2.nmachine_outside,0))as nmachine_outside,sum(nvl(t2.nmachine_total,0))as nmachine_total, sum(nvl(t2.nmachine_upto_4,0))as nmachine_upto_4,sum(nvl(t2.nmachine_after_4,0))as nmachine_after_4, sum(nvl(t2.nbailgadi_local,0))as nbailgadi_local, sum(nvl(t2.nbailgadi_outside,0))as nbailgadi_outside,sum(nvl(t2.nbailgadi_total,0))as nbailgadi_total from gm_m_village_master t1 LEFT JOIN App_t_Vehicle_Transit t2 on t1.nvillage_id=t2.nvillage_id  and t2.vyear_id=? and t2.ddate=TO_DATE(?,'dd/MM/yyyy') WHERE t1.nvillage_id in("+villCode+") AND  t1.nsection_id=?  group by t1.nvillage_id,t1.village_name_local order by t1.nvillage_id,t1.village_name_local")){
				int i = 1;
				pst.setString(i++, yearId);
				pst.setString(i++, dateVal);
				pst.setString(i++, sectionId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						if (vlist.contains(rs.getInt("nvillage_id")))
							continue;
						
						double nspare_area=rs.getDouble("nspare_area");
						double nspare_tonnage=rs.getDouble("nspare_tonnage");
		
						int ntoli_local=rs.getInt("ntoli_local");
						int ntoli_outside=rs.getInt("ntoli_outside");
						int ntoli_self=rs.getInt("ntoli_self");
						int ntoli_total=rs.getInt("ntoli_total");
						int ntoli_upto_4=rs.getInt("ntoli_upto_4");
						int ntoli_after_4=rs.getInt("ntoli_after_4");
						int ntoli_absent=rs.getInt("ntoli_absent");
						int ntoli_extra_trip=rs.getInt("ntoli_extra_trip");
						
						int nbajat_local=rs.getInt("nbajat_local");
						int nbajat_outside=rs.getInt("nbajat_outside");
						int nbajat_self=rs.getInt("nbajat_self");
						int nbajat_total=rs.getInt("nbajat_total");
						int nbajat_upto_4=rs.getInt("nbajat_upto_4");
						int nbajat_after_4=rs.getInt("nbajat_after_4");
						int nbajat_absent=rs.getInt("nbajat_absent");
						int nbajat_extra_trip=rs.getInt("nbajat_extra_trip");
												
						int nmachine_local=rs.getInt("nmachine_local");
						int nmachine_outside=rs.getInt("nmachine_outside");
						int nmachine_total=rs.getInt("nmachine_total");
						int nmachine_upto_4=rs.getInt("nmachine_upto_4");
						int nmachine_after_4=rs.getInt("nmachine_after_4");
						
						int nbailgadi_local=rs.getInt("nbailgadi_local");
						int nbailgadi_outside=rs.getInt("nbailgadi_outside");
						int nbailgadi_total=rs.getInt("nbailgadi_total");
						
						usRawanaRow = new ArrayList<>();
						usRawanaRow.add(String.valueOf(srno++));
						usRawanaRow.add(DemoConvert2.ism_to_uni(rs.getString("vill_name")));
						usRawanaRow.add(dfArea.format(nspare_area));
						usRawanaRow.add(dfTon.format(nspare_tonnage));

						usRawanaRow.add(String.valueOf(ntoli_local));
						usRawanaRow.add(String.valueOf(ntoli_outside));
						usRawanaRow.add(String.valueOf(ntoli_self));
						usRawanaRow.add(String.valueOf(ntoli_total));
						usRawanaRow.add(String.valueOf(ntoli_upto_4));
						usRawanaRow.add(String.valueOf(ntoli_after_4));
						usRawanaRow.add(String.valueOf(ntoli_absent));
						usRawanaRow.add(String.valueOf(ntoli_extra_trip));

						usRawanaRow.add(String.valueOf(nbajat_local));
						usRawanaRow.add(String.valueOf(nbajat_outside));
						usRawanaRow.add(String.valueOf(nbajat_self));
						usRawanaRow.add(String.valueOf(nbajat_total));
						usRawanaRow.add(String.valueOf(nbajat_upto_4));
						usRawanaRow.add(String.valueOf(nbajat_after_4));
						usRawanaRow.add(String.valueOf(nbajat_absent));
						usRawanaRow.add(String.valueOf(nbajat_extra_trip));

						usRawanaRow.add(String.valueOf(nmachine_local));
						usRawanaRow.add(String.valueOf(nmachine_outside));
						usRawanaRow.add(String.valueOf(nmachine_total));
						usRawanaRow.add(String.valueOf(nmachine_upto_4));
						usRawanaRow.add(String.valueOf(nmachine_after_4));

						usRawanaRow.add(String.valueOf(nbailgadi_local));
						usRawanaRow.add(String.valueOf(nbailgadi_outside));
						usRawanaRow.add(String.valueOf(nbailgadi_total));

						usRawanaTabaleData.add(usRawanaRow);
						int pos = 1;
						usRawanaFooter.set(pos, dfArea.format(Double.parseDouble(usRawanaFooter.get(pos++)) + nspare_area));
						usRawanaFooter.set(pos, dfTon.format(Double.parseDouble(usRawanaFooter.get(pos++)) + nspare_tonnage));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_self));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_total));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_upto_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_after_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_absent));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + ntoli_extra_trip));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_self));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_total));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_upto_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_after_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_absent));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbajat_extra_trip));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_total));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_upto_4));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nmachine_after_4));

						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbailgadi_local));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbailgadi_outside));
						usRawanaFooter.set(pos, String.valueOf(Integer.parseInt(usRawanaFooter.get(pos++)) + nbailgadi_total));
						
					}
				}
			}
			
			int totRow = usRawanaTabaleData.size();
			boldIndicator.put(totRow + "-*", true);
			colSpan.put(totRow + "-0", 2);
			textAlign.put(totRow + "-0", 2);
			usRawanaTabaleData.add(usRawanaFooter);
			
			usRawanaBean.setTableData(usRawanaTabaleData);
			usRawanaBean.setBoldIndicator(boldIndicator);
			usRawanaBean.setRowColSpan(colSpan);
			usRawanaBean.setRowSpan(rowSpan);
			usRawanaBean.setTextAlign(textAlign);
			usRawanaBean.setColWidth(new Integer[]{6,10,6,6,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3});
			reqResponse.setTableData(usRawanaBean);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("US rawana report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
	}

	public VillageResonse sectionByVillageList(String yearId, String chit_boy_id, VillageResonse reqResponse,
			Connection conn) {
		try {
			List<KeyPairBoolData> list = new ArrayList<>();
			try (PreparedStatement pst = conn.prepareStatement("select DISTINCT t.nvillage_id,TO_NCHAR(t1.village_name_local)as vill_name from cr_t_plantation t,gm_m_village_master t1 where t.nvillage_id=t1.nvillage_id and  t.nconfirm_flag <> 0 and t.nexpected_yield > 0 and t1.nsection_id=? and t.vyear_id=?")){
				//try(PreparedStatement pst=conn.prepareStatement("select DISTINCT t.nvillage_id,TO_NCHAR(t1.village_name_local)as vill_name from cr_t_plantation t,gm_m_village_master t1 where t.nvillage_id=t1.nvillage_id and  t.nconfirm_flag <> 0 and t.nexpected_yield > 0 and (t1.napp_user_1=? OR t1.napp_user_2=?) and t.vyear_id=?")){
				int i = 1;
				pst.setInt(i++, reqResponse.getNuserSectionId());
				pst.setString(i++, yearId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						KeyPairBoolData data = new KeyPairBoolData();
						data.setId(rs.getLong("nvillage_id"));
						data.setName(DemoConvert2.ism_to_uni(rs.getString("vill_name")));
						list.add(data);
					}
				}
			}
			reqResponse.setVillList(list);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("US rawana report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
	}

	public VillageResonse villBySection(String sectionId, String attach, String chit_boy_id, VillageResonse reqResponse,
			Connection conn) {
		try {
			List<KeyPairBoolData> list=new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select napp_user_1, napp_user_2, t1.nvillage_id,TO_NCHAR(t1.village_name_local)as vill_name from gm_m_village_master t1 where t1.nsection_id =?")){
				pst.setString(1, sectionId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						KeyPairBoolData data = new KeyPairBoolData();
						data.setId(rs.getLong("nvillage_id"));
						String name = DemoConvert2.ism_to_uni(rs.getString("vill_name"));
						if (attach != null && attach.equals("1")) {
							String u1 = rs.getString("napp_user_1");
							String u2 = rs.getString("napp_user_2");
							JSONObject job = new JSONObject();
							if (u1 != null) {
								name += ", U1 = " + u1;
								job.put("u1", u1);
							}
							if (u2 != null) {
								name += ", U2 = " + u2;
								job.put("u2", u2);
							}
							data.setObject(job.toString());
						}
						data.setName(name);
						list.add(data);
					}
				}
			}
			reqResponse.setVillList(list);
			
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Village Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
	}

	public HarvReportReponse dailyCaneRemain(String date, String villageId, String vyearCode,
			HarvReportReponse reqResponse, Connection conn) {

		try {
			TableReportBean dailyCaneInward = new TableReportBean();
			int noofheadDailyCrush =1;
			dailyCaneInward.setNoofHeads(noofheadDailyCrush);
			dailyCaneInward.setFooter(true);
			dailyCaneInward.setMarathi(true);
			
			HashMap<String, Integer> rowColTonnage = new HashMap<>();
			
			HashMap<String, String> sectionfloatings=new HashMap<>();
			sectionfloatings.put("*-2", ".000");
			sectionfloatings.put("*-3", ".000");
			dailyCaneInward.setFloatings(sectionfloatings);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> dailyCrushTableData = new ArrayList<>();
			ArrayList<String> dailyCrushTonnageRow = new ArrayList<>();
			dailyCrushTonnageRow.add(ConstantVeriables.srno);
			dailyCrushTonnageRow.add(ConstantVeriables.vehicalType);
			dailyCrushTonnageRow.add(ConstantVeriables.today);
			dailyCrushTonnageRow.add(ConstantVeriables.todate);
			dailyCrushTableData.add(dailyCrushTonnageRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			String dbdate = Constant.AppDateDbDate(date);
			JSONObject jobPos = new JSONObject();
			double todayTot = 0, todateTot = 0;
			try(PreparedStatement pst=conn.prepareStatement("select t.nvehicle_type_id,TO_NCHAR(a.vvehicle_type_name_local) as vvehicle_type_name_local,sum(t.nnet_weight) as todate from WB_T_WEIGHT_SLIP t, GM_M_VEHICLE_TYPE_MASTER a where t.nvehicle_type_id = a.nvehicle_type_id and t.nnet_weight > 0 and t.vyear_id = ? and t.dslip_date <= ? and t.nvillage_id = ? group by t.nvehicle_type_id,a.vvehicle_type_name_local order by t.nvehicle_type_id,a.vvehicle_type_name_local")){
				int i = 1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, dbdate);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String key = rs.getString("nvehicle_type_id");
						jobPos.put(key, srno);
						dailyCrushTonnageRow = new ArrayList<>();
						dailyCrushTonnageRow.add(String.valueOf(srno++));
						dailyCrushTonnageRow.add(DemoConvert2.ism_to_uni(rs.getString("vvehicle_type_name_local")));
						dailyCrushTonnageRow.add("0.00");
						dailyCrushTonnageRow.add(rs.getString("todate"));
						dailyCrushTableData.add(dailyCrushTonnageRow);
						todateTot+=rs.getDouble("todate");
					}
				}
			}
			try(PreparedStatement pst=conn.prepareStatement("select t.nvehicle_type_id,sum(t.nnet_weight) today from WB_T_WEIGHT_SLIP t where t.nnet_weight > 0 and t.vyear_id = ? and t.dslip_date = ? and t.nvillage_id = ? group by t.nvehicle_type_id order by t.nvehicle_type_id")){
				int i = 1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, dbdate);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String key = rs.getString("nvehicle_type_id");
						if(jobPos.has(key))  {
						int pos = jobPos.getInt(key);
						dailyCrushTonnageRow = dailyCrushTableData.get(pos);
						dailyCrushTonnageRow.set(2, rs.getString("today"));
						dailyCrushTableData.set(pos, dailyCrushTonnageRow);
						todayTot+=rs.getDouble("today");
						}
					}
				}
			}
			int totalPosDailyCrush = dailyCrushTableData.size();
			boldIndicator.put(totalPosDailyCrush+"-*", true);
			
			DecimalFormat df = new DecimalFormat("#0.000");
			dailyCrushTonnageRow = new ArrayList<>();
			dailyCrushTonnageRow.add(ConstantVeriables.total);
			dailyCrushTonnageRow.add(df.format(todayTot));
			dailyCrushTonnageRow.add(df.format(todateTot));
			dailyCrushTableData.add(dailyCrushTonnageRow);
			
			rowColTonnage.put(totalPosDailyCrush+"-0", 2);
			dailyCaneInward.setRowColSpan(rowColTonnage);
			dailyCaneInward.setBoldIndicator(boldIndicator);
			dailyCaneInward.setColWidth(new Integer[] { 10, 40, 25, 25 });
			dailyCaneInward.setTableData(dailyCrushTableData);
			
			reqResponse.setDailyCaneInward(dailyCaneInward);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
		
	
	}

	public HarvReportReponse remainingCaneInfo(String villageId, String vyearCode,
			HarvReportReponse reqResponse, Connection conn) {

		try {
			TableReportBean dailyCaneInward = new TableReportBean();
			int noofheadremainingCane =2;
			dailyCaneInward.setNoofHeads(noofheadremainingCane);
			dailyCaneInward.setFooter(true);
			dailyCaneInward.setMarathi(true);
			
			HashMap<String, Integer> rowColTonnage = new HashMap<>();
			rowColTonnage.put("0-1", 2);
			rowColTonnage.put("0-2", 2);
			rowColTonnage.put("0-3", 2);
			rowColTonnage.put("0-4", 2);
			rowColTonnage.put("0-5", 2);
			rowColTonnage.put("0-6", 2);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> remainingCaneTableData = new ArrayList<>();
			ArrayList<String> remainingCaneTonnageRow = new ArrayList<>();
			remainingCaneTonnageRow.add("");
			remainingCaneTonnageRow.add(ConstantVeriables.totalharvesting);
			remainingCaneTonnageRow.add(ConstantVeriables.totalCrushing);
			remainingCaneTonnageRow.add(ConstantVeriables.otherCrushing);
			remainingCaneTonnageRow.add(ConstantVeriables.charavbene);
			remainingCaneTonnageRow.add(ConstantVeriables.totalVilhewat);
			remainingCaneTonnageRow.add(ConstantVeriables.totalRemaing);
			remainingCaneTableData.add(remainingCaneTonnageRow);
			
			remainingCaneTonnageRow = new ArrayList<>();
			remainingCaneTonnageRow.add(ConstantVeriables.srno);
			remainingCaneTonnageRow.add(ConstantVeriables.area);
			remainingCaneTonnageRow.add(ConstantVeriables.expectedTonnage);
			remainingCaneTonnageRow.add(ConstantVeriables.area);
			remainingCaneTonnageRow.add(ConstantVeriables.expectedTonnage);
			remainingCaneTonnageRow.add(ConstantVeriables.area);
			remainingCaneTonnageRow.add(ConstantVeriables.expectedTonnage);
			remainingCaneTonnageRow.add(ConstantVeriables.area);
			remainingCaneTonnageRow.add(ConstantVeriables.expectedTonnage);
			remainingCaneTonnageRow.add(ConstantVeriables.area);
			remainingCaneTonnageRow.add(ConstantVeriables.expectedTonnage);
			remainingCaneTonnageRow.add(ConstantVeriables.area);
			remainingCaneTonnageRow.add(ConstantVeriables.expectedTonnage);
			remainingCaneTableData.add(remainingCaneTonnageRow);
			
			
			
			boldIndicator.put("0-*", true);
			boldIndicator.put("1-*", true);
			int srno = 1;
			DecimalFormat dfArea = new DecimalFormat("#0.00");
			DecimalFormat dfTon = new DecimalFormat("#0.000");
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,sum(case when t.nconfirm_flag <> 0 then t.ntentative_area end) as totalArea,sum(case when t.nconfirm_flag <> 0 then t.nexpected_yield end) as totalTon ,sum(case when t.nharvested_flag = 1 then t.ntentative_area end) as crusharea from CR_T_PLANTATION t where t.nconfirm_flag <> 0 and t.vyear_id = ? and t.nvillage_id = ? group by t.nvillage_id order by t.nvillage_id")){
				int i = 1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						remainingCaneTonnageRow = new ArrayList<>();
						remainingCaneTonnageRow.add(String.valueOf(srno++));
						remainingCaneTonnageRow.add(dfArea.format(rs.getDouble("totalArea")));
						remainingCaneTonnageRow.add(dfTon.format(rs.getDouble("totalTon")));
						remainingCaneTonnageRow.add(dfArea.format(rs.getDouble("crusharea")));
						remainingCaneTonnageRow.add("0.000");
						remainingCaneTonnageRow.add("0.00");
						remainingCaneTonnageRow.add("0.000");
						remainingCaneTonnageRow.add("0.00");
						remainingCaneTonnageRow.add("0.000");
						remainingCaneTonnageRow.add(dfArea.format(rs.getDouble("crusharea")));
						remainingCaneTonnageRow.add("0.000");
						remainingCaneTonnageRow.add(dfArea.format(rs.getDouble("totalArea")-rs.getDouble("crusharea")) );
						remainingCaneTonnageRow.add(dfTon.format(rs.getDouble("totalTon")));
					}
				}
			}
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,sum(t.nnet_weight) as crushingTon from WB_T_WEIGHT_SLIP t where t.vyear_id = ? and t.nvillage_id = ? group by t.nvillage_id order by t.nvillage_id")){
				int i = 1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						remainingCaneTonnageRow.set(4, dfTon.format(rs.getDouble("crushingTon")));
						remainingCaneTonnageRow.set(10, dfTon.format(rs.getDouble("crushingTon")));
						remainingCaneTonnageRow.set(12, dfTon.format(Double.parseDouble(remainingCaneTonnageRow.get(12)) - rs.getDouble("crushingTon")));
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select p.nvillage_id,sum(t.narea_outward) as otherArea,sum(t.nexpected_yield) as otherTon from CR_T_CANE_OUTWARD t, CR_T_PLANTATION p where t.vyear_id = p.vyear_id and t.nplot_no = p.nplot_no and t.nreason_id in (1,5) and t.vyear_id = ? and p.nvillage_id = ? group by p.nvillage_id order by p.nvillage_id")){
				int i = 1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						remainingCaneTonnageRow.set(5, dfArea.format(rs.getDouble("otherArea")));
						remainingCaneTonnageRow.set(6, dfTon.format(rs.getDouble("otherTon")));
						remainingCaneTonnageRow.set(9, dfArea.format(Double.parseDouble(remainingCaneTonnageRow.get(9)) + rs.getDouble("otherArea")));
						remainingCaneTonnageRow.set(10, dfTon.format(Double.parseDouble(remainingCaneTonnageRow.get(10)) + rs.getDouble("otherTon")));
						remainingCaneTonnageRow.set(11, dfArea.format(Double.parseDouble(remainingCaneTonnageRow.get(11)) - rs.getDouble("otherArea")));
						remainingCaneTonnageRow.set(12, dfTon.format(Double.parseDouble(remainingCaneTonnageRow.get(12)) - rs.getDouble("otherTon")));
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select p.nvillage_id,sum(t.narea_outward) as otherArea,sum(t.nexpected_yield) as otherTon from CR_T_CANE_OUTWARD t, CR_T_PLANTATION p where t.vyear_id = p.vyear_id and t.nplot_no = p.nplot_no and t.nreason_id in (2,3,4) and t.vyear_id = ? and p.nvillage_id = ? group by p.nvillage_id order by p.nvillage_id")){
				int i = 1;
				pst.setString(i++, vyearCode);
				pst.setString(i++, villageId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						remainingCaneTonnageRow.set(7, dfArea.format(rs.getDouble("otherArea")));
						remainingCaneTonnageRow.set(8, dfTon.format(rs.getDouble("otherTon")));
						remainingCaneTonnageRow.set(9, dfArea.format(Double.parseDouble(remainingCaneTonnageRow.get(9)) + rs.getDouble("otherArea")));
						remainingCaneTonnageRow.set(10, dfTon.format(Double.parseDouble(remainingCaneTonnageRow.get(10)) + rs.getDouble("otherTon")));
						remainingCaneTonnageRow.set(11, dfArea.format(Double.parseDouble(remainingCaneTonnageRow.get(11)) - rs.getDouble("otherArea")));
						remainingCaneTonnageRow.set(12, dfTon.format(Double.parseDouble(remainingCaneTonnageRow.get(12)) - rs.getDouble("otherTon")));
					}
				}
			}
			remainingCaneTableData.add(remainingCaneTonnageRow);
			dailyCaneInward.setRowColSpan(rowColTonnage);
			dailyCaneInward.setBoldIndicator(boldIndicator);
			dailyCaneInward.setFooter(false);
			dailyCaneInward.setTableData(remainingCaneTableData);
			dailyCaneInward.setColWidth(new Integer[]{4,6,10,6,10,6,10,6,10,6,10,6,10});
			reqResponse.setRemainingCaneInfo(dailyCaneInward);
		} catch (Exception e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;
		
	
	}

	public FarmerTonnageResponse farmerTonnageReport(String nfarmercode, String vyearCode,String chit_boy_id,
			FarmerTonnageResponse reqResponse, Connection conn) {

		DecimalFormat df = new DecimalFormat("#0.000");
    	HashMap<String, Integer> pos = new HashMap<>();
    	List<FarmerTonnage> farmerTonnages = new ArrayList<>();
    	String userrollid=reqResponse.getNuserRoleId();
    	
        try {
        	
        	
        	try (PreparedStatement pst1 = conn.prepareStatement("select TO_NCHAR(t.ventity_name_local) as ventity_name_local,nentity_uni_id from Gm_m_Entity_Master_Detail t,Gm_m_Village_Master t1 where t.nvillage_id=t1.nvillage_id and  t.nentity_uni_id =?")) {
	            int i = 1;
	            pst1.setString(i++, "F"+nfarmercode);
	            
	            try (ResultSet rs = pst1.executeQuery()) {
	                if (rs.next()) {
	                	reqResponse.setFarmerName(DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
	                	
					}
				}
			}

			String sql = "select count(cws.nslip_no) as countslip, sum(cws.nnet_weight) as netwt,cws.nvehicle_type_id, cws.nplot_no from WB_T_WEIGHT_SLIP cws ";

			if (!userrollid.equalsIgnoreCase("113"))
				sql += " ,Gm_m_Village_Master vm where cws.nvillage_id=vm.nvillage_id and (vm.napp_user_1=? or vm.napp_user_2=?) and";
			else
				sql += " where ";
			sql += " cws.nnet_weight>0 AND cws.nentity_uni_id=? and cws.vyear_id=? GROUP BY cws.nvehicle_type_id, cws.nplot_no order by cws.nplot_no";

			try (PreparedStatement pst1 = conn.prepareStatement(sql)) {
				int i = 1;
				if (!userrollid.equalsIgnoreCase("113")) {
					pst1.setString(i++, chit_boy_id);
					pst1.setString(i++, chit_boy_id);
				}
				pst1.setString(i++, "F" + nfarmercode);
				pst1.setString(i++, vyearCode);

				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						String nplotNo = rs.getString("nplot_no");
						FarmerTonnage farmerTonnage = null;
					   	int itemPos= -1;
	                	if(pos.containsKey(nplotNo)) {
	                		itemPos = pos.get(nplotNo);
	                		farmerTonnage = farmerTonnages.get(itemPos);
	                	} else {
	                		farmerTonnage = new FarmerTonnage();
	                		farmerTonnage.setNplotNo(nplotNo);
	                		farmerTonnage.setTruckCount("0");
	                		farmerTonnage.setTruckTonnage("0.000");
	                		farmerTonnage.setTractorCount("0");
	                		farmerTonnage.setTractorTonnage("0.000");
	                		farmerTonnage.setTractorGadiCount("0");
	                		farmerTonnage.setTractorGadiTonnage("0.000");
	                		farmerTonnage.setBullockcartCount("0");
	                		farmerTonnage.setBullockcartTonnage("0.000");
	                		farmerTonnage.setTotalTonnage("0.000");
	                		itemPos = farmerTonnages.size();
	                		pos.put(nplotNo, itemPos);
	                		farmerTonnages.add(farmerTonnage);
	           			}
	                	if (rs.getInt("nvehicle_type_id") == 1) {
							farmerTonnage.setTruckCount("" + rs.getInt("countslip"));
							farmerTonnage.setTruckTonnage(df.format(rs.getDouble("netwt")));
						} else if (rs.getInt("nvehicle_type_id") == 2) {
							farmerTonnage.setTractorCount("" + rs.getInt("countslip"));
							farmerTonnage.setTractorTonnage(df.format(rs.getDouble("netwt")));
						} else if (rs.getInt("nvehicle_type_id") == 4) {
							farmerTonnage.setTractorGadiCount("" + rs.getInt("countslip"));
							farmerTonnage.setTractorGadiTonnage(df.format(rs.getDouble("netwt")));
						} else {
							farmerTonnage.setBullockcartCount("" + (Integer.parseInt(farmerTonnage.getBullockcartCount()) + rs.getInt("countslip")));
							farmerTonnage.setBullockcartTonnage(df.format(Double.parseDouble(farmerTonnage.getBullockcartTonnage()) + rs.getDouble("netwt")));
						}
						farmerTonnage.setTotalTonnage(df.format(Double.parseDouble(farmerTonnage.getTotalTonnage()) + rs.getDouble("netwt")));
						farmerTonnages.set(itemPos, farmerTonnage);
					}

				}
			}
	        reqResponse.setFarmerTonnages(farmerTonnages);
        } catch (SQLException e) {
        	reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
        }
        return reqResponse;
	}

	public TableResponse farmerTonnageDetailsReport(String nfarmercode, String vyearCode,String chit_boy_id, TableResponse reqResponse,
			Connection conn) {
		String userrollid=reqResponse.getNuserRoleId();
		TableReportBean reportBean=new TableReportBean();
        DecimalFormat df = new DecimalFormat("#0.000");
        DecimalFormat dfArea = new DecimalFormat("#0.00");
        ArrayList<ArrayList<String>> mainList = new ArrayList<>();
        ArrayList<String> head = new ArrayList<>();
        HashMap<String, Boolean> visibility = new HashMap<>();
        visibility.put("*-4", false);
        head.add(ConstantVeriables.lblPlotNo);
        head.add(ConstantVeriables.lblRujuwatArea);
        head.add(ConstantVeriables.expectedTonnage);
        head.add(ConstantVeriables.tonnage);
        mainList.add(head);
        
        double total = 0;
        double rujuwat_total = 0;
        double nexpected_total = 0;
    	HashMap<String, Boolean> boldIndicator = new HashMap<>();
		boldIndicator.put("0-*", true);
		
		String sql = "select cws.nplot_no,crt.ntentative_area,crt.nexpected_yield, sum(cws.nnet_weight) as totaltonage from WB_T_WEIGHT_SLIP cws,CR_T_PLANTATION crt";

		if (!userrollid.equalsIgnoreCase("113"))
			sql += " ,Gm_m_Village_Master vm where cws.nvillage_id=vm.nvillage_id and (vm.napp_user_1=? or vm.napp_user_2=?) and ";
		else
			sql += " where ";
		sql += " cws.nplot_no=crt.nplot_no and cws.vyear_id=crt.vyear_id and cws.nnet_weight>0 AND cws.nentity_uni_id=? and cws.vyear_id=? GROUP BY cws.nplot_no,crt.ntentative_area,crt.nexpected_yield order by cws.nplot_no ASC";
		try (PreparedStatement pst1 = conn.prepareStatement(sql)) {
			int i = 1;
			if (!userrollid.equalsIgnoreCase("113")) {
				pst1.setString(i++, chit_boy_id);
				pst1.setString(i++, chit_boy_id);
			}
			pst1.setString(i++, "F" + nfarmercode);
			pst1.setString(i++, vyearCode);

			try (ResultSet rs = pst1.executeQuery()) {
                while (rs.next()) {
                	 ArrayList<String> data = new ArrayList<>();
                    double tonnage = rs.getDouble("totaltonage");
                    double ntentative_area = rs.getDouble("ntentative_area");
                    double nexpected_yield = rs.getDouble("nexpected_yield");
                    data.add(rs.getString("nplot_no"));
                    data.add(dfArea.format(ntentative_area));
                    data.add(df.format(nexpected_yield));
                    data.add(df.format(tonnage));
                    mainList.add(data);
                    rujuwat_total+=ntentative_area;
                    nexpected_total+=nexpected_yield;
                    total+=tonnage;
                }

                ArrayList<String>  footer = new  ArrayList<>();
                footer.add(ConstantVeriables.total);
                footer.add(dfArea.format(rujuwat_total));
                footer.add(df.format(nexpected_total));
                footer.add(df.format(total));
                mainList.add(footer);
                boldIndicator.put(mainList.size()-1+"-*", true);
                reportBean.setBoldIndicator(boldIndicator);
                reportBean.setColWidth(new Integer[]{25,25,25,25});
                reportBean.setFloatings(null);
                reportBean.setFooter(true);
                reportBean.setMarathi(true);
                reportBean.setNoofHeads(1);
                reportBean.setRowColSpan(null);
                reportBean.setRowSpan(null);
                reportBean.setTableData(mainList);
                reportBean.setTextAlign(null);
                reportBean.setVisibility(visibility);
                reqResponse.setTableData(reportBean);
            }
        } catch (SQLException e) {
        	reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Report Error " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
        }
        return reqResponse;
	}

	public NameListResponse farmerListByName(String name, NameListResponse reqResponse, Connection conn) {

		List<NameData> nameDatas = new ArrayList<>();
		String sql = "select t.nentity_uni_id,t.ventity_name as farmer_name_en from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_uni_id LIKE 'F%' AND UPPER(t.ventity_name) like ? ";
		try (PreparedStatement pst1 = conn.prepareStatement(sql)) {
			int i = 1;
			pst1.setString(i++, "%" + name.toUpperCase() + "%");
			try (ResultSet rs = pst1.executeQuery()) {
				while (rs.next()) {
					NameData data = new NameData();
					data.setCode(rs.getString("nentity_uni_id").replace("F", ""));
					data.setName(rs.getString("farmer_name_en"));
					nameDatas.add(data);
				}
			}
			reqResponse.setNameDataList(nameDatas);
		} catch (SQLException e) {
			reqResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("farmer List " + e.getMessage());
			reqResponse.setSe(error);
			e.printStackTrace();
		}
		return reqResponse;

	}

	public RemainingSlipResponse updateAndPrint(String yearCode, String nslipNo, String chit_boy_id,
			RemainingSlipResponse sliplistResponse, Connection conn) {
		try {
			try (PreparedStatement pst = conn.prepareStatement("UPDATE " + ConstantVeriables.weightSlipTableName
					+ " SET vqr_photo_path = ?,vqr_string =?,cqr_image_wt_slip=? WHERE VYEAR_ID =? AND NSLIP_NO=?")) {
				int i = 1;

				QRConstant qrConstant = new QRConstant();
				String qrString = nslipNo + ConstantVeriables.lblExtraQR;
				String qrPath = qrConstant.createImage(qrString, yearCode);

				File qrfile = new File(ConstantVeriables.baseFilePath + ConstantVeriables.qrcode + qrPath);

				pst.setString(i++, qrPath);
				pst.setString(i++, qrString);
				FileInputStream fis = new FileInputStream(qrfile);

				pst.setBinaryStream(i++, fis, (int) qrfile.length());
				pst.setString(i++, yearCode);
				pst.setString(i++, nslipNo);
				pst.executeUpdate();

			}
			
			List<RemainingSlipBean> list = new ArrayList<>();
			String sql = "select t.nplot_no,t.nvillage_id,TO_NCHAR(t1.village_name_local)as village_name,t.nentity_uni_id,t.nvehicle_type_id,t.nremark_id,t.nslip_no,t.dslip_date,t.vvehicle_no,t.nbulluckcart_id, t.nbulluckcart_main_id, t.ntransportor_id, t.nharvestor_id,t.nwirerope_no,t.ntailor_front,t.ntailor_back,TO_NCHAR(t5.vfull_name_local) as vfull_name_local,t.ngadi_doki_id,t.nvariety_id,t.dslip_date_slipboy, t.dcreate_date from "
					+ ConstantVeriables.weightSlipTableName
					+ " t,GM_M_VILLAGE_MASTER t1,app_m_user_master t5  WHERE t.nvillage_id=t1.nvillage_id and t.nslipboy_id=t5.vuser_name and t.vyear_id=? and t.nslip_no=?";
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, yearCode);
				pst.setString(i++, nslipNo);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String ntailor_front, ntailor_back, nwirerope_no, nvariety_id;
						ntailor_front = rs.getString("ntailor_front");
						ntailor_back = rs.getString("ntailor_back");
						nwirerope_no = rs.getString("nwirerope_no");
						nvariety_id = rs.getString("nvariety_id");
						
						RemainingSlipBean data = new RemainingSlipBean();
						data.setDslipDate(Constant.DbDateTimeToAppDateTime(rs.getTimestamp("dcreate_date")));
						data.setNentityUnitId(rs.getString("nentity_uni_id"));
						data.setNplotNo(rs.getString("nplot_no"));
						data.setNremarkId(rs.getString("nremark_id"));
						data.setNslipNo(rs.getString("nslip_no"));
						data.setNvillageCode(rs.getString("nvillage_id"));

						data.setNvehicalTypeId(rs.getString("nvehicle_type_id"));

						String entId = "'"+ rs.getString("nentity_uni_id") + "','";
						String nbulluckcart_id = rs.getString("nbulluckcart_id");
						String nbulluckcart_main_id = rs.getString("nbulluckcart_main_id");
						String ntransportor_id = rs.getString("ntransportor_id");
						String nharvestor_id = rs.getString("nharvestor_id");

						if (nbulluckcart_id != null && !nbulluckcart_id.isEmpty() && !nbulluckcart_id.equals("0")) {
							entId += nbulluckcart_id + "','" + nbulluckcart_main_id + "'";
						} else {
							entId += ntransportor_id + "','" + nharvestor_id + "'";
						}
						
						try (PreparedStatement pstinn = conn.prepareStatement("select t.nentity_uni_id, TO_NCHAR(t.ventity_name_local) as ventity_name_local from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_uni_id in (" + entId + ")")) {
							try (ResultSet rsinn = pstinn.executeQuery()) {
								while (rsinn.next()) {
									String uniId = rsinn.getString("nentity_uni_id");
									String ventity_name_local = DemoConvert2
											.ism_to_uni(rsinn.getString("ventity_name_local"));
									if (uniId.equals(data.getNentityUnitId())) {
										data.setVfarmerName(ventity_name_local);
									} else if (uniId.equals(nbulluckcart_id)) {
										data.setNbullockCode(uniId);
										data.setVbullockName(ventity_name_local);
									} else if (uniId.equals(nbulluckcart_main_id)) {
										data.setNbullockMainCode(uniId);
										data.setVbullockMainName(ventity_name_local);
									} else if (uniId.equals(ntransportor_id)) {
										data.setNtransporterId(uniId);
										data.setVtransporterName(ventity_name_local);
									} else if (uniId.equals(nharvestor_id)) {
										data.setNharvestorId(uniId);
										data.setVharvestorName(ventity_name_local);

										try (PreparedStatement pstin = conn.prepareStatement("select t.ngadi_doki_id,t.vgadi_doki_name from WB_M_GADI_DOKI_MASTER t where t.ngadi_doki_id = ?")) {
											i = 1;
											pstin.setString(i++, rs.getString("ngadi_doki_id"));
											try (ResultSet rsin = pstin.executeQuery()) {
												while (rsin.next()) {
													data.setVharvestorType(DemoConvert2.ism_to_uni(rsin.getString("vgadi_doki_name")));
												}
											}
										}
									}
								}
							}
						}
						data.setVvehicleNo(rs.getString("vvehicle_no"));
						data.setVvillageNameLocal(DemoConvert2.ism_to_uni(rs.getString("village_name")));
						
						if(ntailor_front!=null && !ntailor_front.equals("0")) {
							String trailer_code = null;
							if (ntailor_front.equals(ntailor_back)) {
								trailer_code = ntailor_front;
							} else {
								trailer_code = ntailor_front + "," + ntailor_back;
							}
							try(PreparedStatement pstin=conn.prepareStatement("select t.ntailor_remark_id as code,t. vremark_name as name from WB_M_TAILOR_MASTER t where t.ntailor_remark_id in (" + trailer_code + ")")) {
								try (ResultSet rsin = pstin.executeQuery()) {
									while (rsin.next()) {
										String code = rsin.getString("code");
										String name = DemoConvert2.ism_to_uni(rsin.getString("name"));
										if (code.equals(ntailor_back))
											data.setVtailerBackName(name);
										if (code.equals(ntailor_front))
											data.setVtailerFrontName(name);
									}
								}
							}
						} else {
							try (PreparedStatement pstin = conn.prepareStatement("select t.nwirerope_id,t.vwire_name from WB_M_WIREROPE_MASTER t where t.nwirerope_id=?")) {
								i = 1;
								pstin.setString(i++, nwirerope_no);
								try (ResultSet rsin = pstin.executeQuery()) {
									while (rs.next()) {
										data.setVwireropeName(DemoConvert2.ism_to_uni(rsin.getString("vwire_name")));
									}
								}
							}
						}

						data.setChitboyName(DemoConvert2.ism_to_uni(rs.getString("vfull_name_local")));
						try(PreparedStatement pstin=conn.prepareStatement("select t.navariety_id,TO_NCHAR(t.vvariety_name)as vvariety_name from CR_M_VARIETY_MASTER t where t.navariety_id=?")){
							i = 1;
							pstin.setString(i++, nvariety_id);
							try (ResultSet rsin = pstin.executeQuery()) {
								while (rsin.next()) {
									data.setVvarietyName(DemoConvert2.ism_to_uni(rsin.getString("vvariety_name")));
								}
							}
						}
						data.setExtraQr(ConstantVeriables.lblExtraQR);
						list.add(data);
					}
					sliplistResponse.setRemainingSlipBeans(list);
					sliplistResponse.setSuccess(true);
				}
			}
		} catch (Exception e) {
			sliplistResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("slip list info " + e.getMessage());
			sliplistResponse.setSe(error);
			e.printStackTrace();
		}
		return sliplistResponse;
	}

}
