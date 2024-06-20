package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.DataListResonse;
import com.twd.pandurangsugar.android.bean.FarmerSugarResponse;
import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SugarInwardResponse;
import com.twd.pandurangsugar.android.bean.SugarSaleSavePrintResponse;
import com.twd.pandurangsugar.android.bean.TableReportBean;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class SugarSaleDao {

	public FarmerSugarResponse farmerSugarInfo(String nentityUniId, FarmerSugarResponse sugarInfoResponse,
			Connection conn) {
		try (PreparedStatement pst = conn.prepareStatement("select t.vyear_id,To_NCHAR(e.vevent_name) as vevent_name,t.nentity_uni_id,To_NCHAR(t.ventity_name_local) farmer_name, t.nsugar_for,TO_NCHAR(t.village_name_local) village_name,t.nsugar_qty,t.nrate,t.namount,t.nlocked,TO_CHAR(t.dsug_date, 'dd/MM/yyyy') as dsug_date, TO_CHAR(t.ddelivery_date, 'dd/MM/yyyy HH24:mi:ss') as ddelivery_date,t.nemp_count_id,t.nlocation, t.vlatitue_vatap, t.vlongitude_vatap, t.vphoto_path from " + ConstantVeriables.MS_T_SUG_CARD_HEADER + "  t, MS_M_SUG_CARD_EVENT e, MS_M_SUG_CARD_YEAR y where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.nentity_uni_id=?")) {
			pst.setString(1, nentityUniId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					String nblocked = rs.getString("nlocked");
					sugarInfoResponse.setBlocked(nblocked);
					if (nblocked.equalsIgnoreCase("N")) {
						sugarInfoResponse.setVvillname(DemoConvert2.ism_to_uni(rs.getString("village_name")));
						sugarInfoResponse.setSugarYear(rs.getString("vyear_id"));
						sugarInfoResponse.setSugarYear(rs.getString("vyear_id"));
						sugarInfoResponse.setEventName(DemoConvert2.ism_to_uni(rs.getString("vevent_name")));
						sugarInfoResponse.setEventId(rs.getString("nsugar_for"));
						sugarInfoResponse.setNentityUniId(rs.getString("nentity_uni_id"));
						sugarInfoResponse.setVentityUniName(DemoConvert2.ism_to_uni(rs.getString("farmer_name")));
						sugarInfoResponse.setSugarInKg(rs.getDouble("nsugar_qty"));
						sugarInfoResponse.setRate(rs.getDouble("nrate"));
						sugarInfoResponse.setAmount(rs.getDouble("namount"));
					} else if (nblocked.equalsIgnoreCase("Y")) {
						sugarInfoResponse.setSuccess(true);
						
						sugarInfoResponse.setPrevDate(rs.getString("ddelivery_date"));
						sugarInfoResponse.setPrevLat(rs.getDouble("vlatitue_vatap"));
						sugarInfoResponse.setPrevLong(rs.getDouble("vlongitude_vatap"));
						
						String nemp_count_id = rs.getString("nemp_count_id");
						try (PreparedStatement pst2 = conn.prepareStatement("select TO_NCHAR(t.vfull_name_local) as fullname from APP_M_USER_MASTER t where t.nuser_name=?")) {
							pst2.setString(1, nemp_count_id);
							try (ResultSet rs2 = pst2.executeQuery()) {
								if (rs2.next()) {
									String nemp_count_name = DemoConvert2.ism_to_uni(rs2.getString("fullname"));
									sugarInfoResponse.setDistUserName(nemp_count_name);
								}
							}
						}
						sugarInfoResponse.setPrevPhoto(rs.getString("vphoto_path"));
						String nlocation = rs.getString("nlocation");
						try (PreparedStatement pst2 = conn.prepareStatement("select TO_NCHAR(t.vlocation) as vlocation from MS_M_SUG_CARD_LOCATION t where t.nlocation_id=?")) {
							pst2.setString(1, nlocation);
							try (ResultSet rs2 = pst2.executeQuery()) {
								if (rs2.next()) {
									String nlocationname = DemoConvert2.ism_to_uni(rs2.getString("vlocation"));
									sugarInfoResponse.setPrevLocation(nlocationname);
								}
							}
						}
					} else {
						sugarInfoResponse.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						try (PreparedStatement pst1 = conn.prepareStatement("select TO_NCHAR(t.vreason_name) as vreason_name from APP_M_REASON t where t.vreason_id=? and t.nreason_group_id=4")) {
							pst1.setString(1, nblocked);
							try (ResultSet rs1 = pst1.executeQuery()) {
								if (rs1.next()) {
									String vreason_name = DemoConvert2.ism_to_uni(rs1.getString("vreason_name"));
									if(vreason_name.contains("%1$s") && !vreason_name.contains("%2$s")) 
										vreason_name = String.format(vreason_name, rs.getString("dsug_date"));
									else if (vreason_name.contains("%2$s")){
										String nemp_count_id = rs.getString("nemp_count_id");
										String nlocation = rs.getString("nlocation");
										String nemp_count_name = "N.A.",nlocationname = "N.A.";
										try (PreparedStatement pst2 = conn.prepareStatement("select TO_NCHAR(t.vlocation) as vlocation from MS_M_SUG_CARD_LOCATION t where t.nlocation_id=?")) {
											pst2.setString(1, nlocation);
											try (ResultSet rs2 = pst2.executeQuery()) {
												if (rs2.next()) {
													nlocationname = DemoConvert2.ism_to_uni(rs2.getString("vlocation"));
												}
											}
										}
										
										try (PreparedStatement pst2 = conn.prepareStatement("select TO_NCHAR(t.vfull_name_local) as fullname from APP_M_USER_MASTER t where t.nuser_name=?")) {
											pst2.setString(1, nemp_count_id);
											try (ResultSet rs2 = pst2.executeQuery()) {
												if (rs2.next()) {
													nemp_count_name = DemoConvert2.ism_to_uni(rs2.getString("fullname"));
												}
											}
										}
										vreason_name = String.format(vreason_name, rs.getString("dsug_date"), nemp_count_name, nlocationname);
									}
									error.setMsg(vreason_name);
								} else {
									error.setMsg("Unknown Blocked " + nblocked);
								}
							}
						}
						sugarInfoResponse.setSe(error);
					}
				} else {
					sugarInfoResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg(ConstantMessage.sugarInofNotFound);
					sugarInfoResponse.setSe(error);
				}
			}
		} catch (Exception e) {
			sugarInfoResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Fetch Sugar " + e.getMessage());
			sugarInfoResponse.setSe(error);
			e.printStackTrace();
		}
		return sugarInfoResponse;
	}

	public SugarSaleSavePrintResponse saveSugarSale(String logitude, String latitude, String sugarSeason, String eventId, String nentityUniId, String photopath, String chit_boy_id, String entryType, SugarSaleSavePrintResponse sugarSaveResponse, Connection conn) {
		String financialYear=Constant.getVyearcode(new Date(), 4, 3);
		try (PreparedStatement pst = conn.prepareStatement("UPDATE " + ConstantVeriables.MS_T_SUG_CARD_HEADER + "  t SET t.DSUG_DATE=TO_CHAR(SYSDATE,'dd-mon-yyyy'),t.VPHOTO_PATH=?,t.NLOCKED='Y',t.NLOCATION=?,t.NEMP_COUNT_ID=?,DDELIVERY_DATE=SYSDATE,VFINYEAR_ID=?,VLATITUE_VATAP=?,VLONGITUDE_VATAP=?,nvatap_flag=? where t.vyear_id = ? and t.nsugar_for =? and t.nentity_uni_id=?")) {
			int i=1;
			pst.setString(i++, photopath);
			pst.setInt(i++, sugarSaveResponse.getNlocationId());
			pst.setString(i++, chit_boy_id);
			pst.setString(i++, financialYear);
			pst.setString(i++, latitude);
			pst.setString(i++, logitude);
			pst.setString(i++, entryType);
			pst.setString(i++, sugarSeason);
			pst.setString(i++, eventId);
			pst.setString(i++, nentityUniId);
			int res = pst.executeUpdate();
			if (res > 0) {
				sugarSaveResponse.setActionComplete(true);
				sugarSaveResponse.setSuccessMsg(ConstantMessage.saveSuccess);
			} else {
				sugarSaveResponse.setActionComplete(false);
				sugarSaveResponse.setFailMsg(ConstantMessage.saveFailed);
			}
			sugarSaveResponse.setSuccess(true);

		} catch (Exception e) {
			sugarSaveResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Update Sugar " + e.getMessage());
			sugarSaveResponse.setSe(error);
			e.printStackTrace();
		}
		return sugarSaveResponse;
	}

	public SugarSaleSavePrintResponse printSugarSale(String nentityUniId, SugarSaleSavePrintResponse sugarInfoResponse,
			Connection conn) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try (PreparedStatement pst = conn.prepareStatement("select t.vyear_id,To_NCHAR(e.vevent_name) as vevent_name,t.nentity_uni_id,To_NCHAR(t.ventity_name_local) farmer_name, t.nsugar_for,TO_NCHAR(t.village_name_local) village_name,t.nsugar_qty,t.nrate,t.namount,t.nlocked, TO_CHAR(t.ddelivery_date, 'dd/MM/yyyy HH24:mi:ss') as dsug_date,TO_NCHAR(cl.vlocation)as vlocation,TO_NCHAR(um.vfull_name_local) as user_name from " + ConstantVeriables.MS_T_SUG_CARD_HEADER + "  t, MS_M_SUG_CARD_EVENT e, MS_M_SUG_CARD_YEAR y,MS_M_SUG_CARD_LOCATION cl,app_m_user_master um where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and cl.nlocation_id=t.nlocation and um.nuser_name=t.nemp_count_id and t.nentity_uni_id=?")) {
			pst.setString(1, nentityUniId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					String nblocked = rs.getString("nlocked");
					if (nblocked.equalsIgnoreCase("Y")) {
						String print="<table>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblSugar+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+rs.getString("vyear_id")+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblSugarFor+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+DemoConvert2.ism_to_uni(rs.getString("vevent_name"))+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblDate+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+rs.getString("dsug_date")+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblPrintDate+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+sdf.format(new Date())+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblLocation+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+DemoConvert2.ism_to_uni(rs.getString("vlocation"))+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblFarmerCodeAndName+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+nentityUniId+" "+DemoConvert2.ism_to_uni(rs.getString("farmer_name"))+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblSugarKg+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+rs.getString("nsugar_qty")+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblSugarBag+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+(rs.getInt("nsugar_qty")/30)+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblAmount+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+rs.getString("namount")+"</td>"
								+ "</tr>";
						print+="<tr>"
								+ "<td class='cbodysmall'>"+ConstantVeriables.lblUserName+"</td>"
								+ "<td class='cbodysmall'>:</td>"
								+ "<td  class='cbody'>"+DemoConvert2.ism_to_uni(rs.getString("user_name"))+"</td>"
								+ "</tr>";
						print+="</table>";
						
						sugarInfoResponse.setHtmlContent(print);
					} else {
						sugarInfoResponse.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(ConstantVeriables.sugarNotReceived);
						sugarInfoResponse.setSe(error);
					}
				}
			}
		} catch (Exception e) {
			sugarInfoResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Fetch Sugar " + e.getMessage());
			sugarInfoResponse.setSe(error);
			e.printStackTrace();
		}
		return sugarInfoResponse;
	}

	public SugarInwardResponse findInward(String ndoId, String invoiceDate, String avakDate, SugarInwardResponse inward, Connection conn) {
		try{
			
		try (PreparedStatement pst = conn.prepareStatement("select t.vvehicle_no,d.nqty,d.nwt_per_bag,d.nactual_qty,t.vfin_year from SALE_T_DO_HEADER t,sale_t_do_detail d where t.vfin_year = d.vfin_year and t.ndo_id = d.ndo_id and t.nsugar_sale_type_id = 3 and t.dinvoice_date =TO_DATE(?,'dd-mon-yyyy') and t.ndo_id =?")) {
			pst.setString(1, Constant.AppDateDbDate(invoiceDate));
			pst.setString(2, ndoId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					inward.setVvehicleNo(rs.getString("vvehicle_no"));
					inward.setNqty(rs.getDouble("nqty"));
					inward.setNwtBag(rs.getDouble("nwt_per_bag"));
					inward.setNbags(rs.getDouble("nactual_qty"));
					inward.setVfinYear(rs.getString("vfin_year"));
					inward.setDawakDate(avakDate);
					inward.setDdoDate(invoiceDate);
					inward.setNdoNo(ndoId);
				}
			}
		}
		try (PreparedStatement pst = conn.prepareStatement("select TO_NCHAR(t.vlocation)as location from MS_M_SUG_CARD_LOCATION t where t.nlocation_id=?")) {
			pst.setInt(1, inward.getNlocationId());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					inward.setLocationNameInward(DemoConvert2.ism_to_uni(rs.getString("location")));
				}
			}
		} 
		
		try (PreparedStatement pst = conn.prepareStatement("select t.ncode,TO_NCHAR(t.vevent_name)as vevent_name from MS_M_SUG_CARD_EVENT t where t.vevent_active='Y'")) {
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					inward.setSugarFor(DemoConvert2.ism_to_uni(rs.getString("vevent_name")));
					inward.setNsugarFor(rs.getInt("ncode"));
				}
			}
		}
		try (PreparedStatement pst = conn.prepareStatement("select t.vsugar_year from ms_m_sug_card_year t WHERE t.vsug_year_active='Y'")) {
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					inward.setVsugYearId(rs.getString("vsugar_year"));
				}
			}
		}
		}catch (Exception e) {
			inward.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Fetch Sugar Inward" + e.getMessage());
			inward.setSe(error);
			e.printStackTrace();
		}
		return inward;
	}

	public ActionResponse saveInward(SugarInwardResponse inward, String chit_boy_id, ActionResponse saveAvak, Connection conn) {
		try{
			long ntransNo=1l;
			try (PreparedStatement pst = conn.prepareStatement("select nvl(MAX(t.ntrans_no),0)as ntrans_no from MS_T_SUG_CARD_AWAK t")) {
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						ntransNo=rs.getLong("ntrans_no")+1;
					}
				}
			}
			SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
			Date avkDate=null;
			try
			{
				avkDate=df.parse(inward.getDawakDate());
			}catch (Exception e) {
				saveAvak.setSuccess(false);
				ServerError error = new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Invalid Avak Date " + e.getMessage());
				saveAvak.setSe(error);
				return saveAvak;
			}
			String financialYear=Constant.getVyearcode(avkDate, 4, 3);
			try (PreparedStatement pst = conn.prepareStatement("INSERT INTO MS_T_SUG_CARD_AWAK(NTRANS_NO,VFIN_YEAR,DAWAK_DATE,NDO_DATE,NDO_NO,VVEHICAL_NO,NQTY,NBAGS,NWT_BAG,VYEAR_ID,NSUGAR_FOR,NSUG_TYPE_ID,NUSER_ID,NLOCATION) VALUES(?,?,TO_DATE(?,'dd-mon-yyyy'),TO_DATE(?,'dd-mon-yyyy'),?,?,?,?,?,?,?,?,?,?)")) {
				int i=1;
				pst.setLong(i++, ntransNo);
				pst.setString(i++, financialYear);
				pst.setString(i++, Constant.AppDateDbDate(inward.getDawakDate()));
				pst.setString(i++, Constant.AppDateDbDate(inward.getDdoDate()));
				pst.setString(i++, inward.getNdoNo());
				pst.setString(i++, inward.getVvehicleNo());
				pst.setDouble(i++, inward.getNbags());
				pst.setDouble(i++, inward.getNqty());
				pst.setDouble(i++, inward.getNwtBag());
				pst.setString(i++, inward.getVsugYearId());
				pst.setInt(i++, inward.getNsugarFor());
				pst.setInt(i++, saveAvak.getNsugTypeId());
				pst.setString(i++, chit_boy_id);
				pst.setInt(i++, saveAvak.getNlocationId());
				int res=pst.executeUpdate();
				if(res>0)
				{
					saveAvak.setActionComplete(true);
					saveAvak.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					saveAvak.setActionComplete(false);
					saveAvak.setFailMsg(ConstantMessage.saveFailed);
				}
				saveAvak.setSuccess(true);
			}
		}catch (Exception e) {
			saveAvak.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("save Sugar Inward" + e.getMessage());
			saveAvak.setSe(error);
			e.printStackTrace();
		}
		return saveAvak;
	}

	public DataListResonse loaddoList(String dinvoiceDate, String chit_boy_id, DataListResonse loaddoResponse,
			Connection conn) {
		List<KeyPairBoolData> dataList=new ArrayList<>();
			
		try (PreparedStatement pst = conn.prepareStatement("select t.ndo_id,t1.ndo_no,t.vvehicle_no from SALE_T_DO_HEADER t LEFT JOIN MS_T_SUG_CARD_AWAK t1 on t.ndo_id=t1.ndo_no and t.dinvoice_date=t1.ndo_date where  t.nsugar_sale_type_id = 3 and t.dinvoice_date =TO_DATE(?,'dd-mon-yyyy') order by t.ndo_id asc")) {
			pst.setString(1, Constant.AppDateDbDate(dinvoiceDate));
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					if(rs.getInt("ndo_no")==0)
					{
						KeyPairBoolData data=new KeyPairBoolData();
						data.setId(rs.getInt("ndo_id"));
						data.setName(rs.getString("vvehicle_no")+" - "+rs.getString("ndo_id"));
						dataList.add(data);
					}
				}
				loaddoResponse.setDataList(dataList);
			}
		}catch (Exception e) {
			loaddoResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Fetch loaddo List" + e.getMessage());
			loaddoResponse.setSe(error);
			e.printStackTrace();
		}
		return loaddoResponse;
	}
	
	public TableResponse sugarSummaryReport(String rdate, String chit_boy_id, TableResponse reqResponse,
			Connection conn) {
		try {
			TableReportBean sumarryBean = new TableReportBean();
			int noofheadDailyCrush =4;
			sumarryBean.setNoofHeads(noofheadDailyCrush);
			sumarryBean.setFooter(true);
			sumarryBean.setMarathi(true);
			
			HashMap<String, Integer> rowColSpan = new HashMap<>();
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();
			boldIndicator.put("0-*", true);
			boldIndicator.put("1-*", true);
			boldIndicator.put("2-*", true);
			boldIndicator.put("3-*", true);
			rowColSpan.put("0-0", 9);
			rowColSpan.put("1-0", 9);
			rowColSpan.put("2-0", 2);
			rowColSpan.put("2-1", 3);
			rowColSpan.put("2-2", 3);
			int count=1;
			int daysCount=0;
			DecimalFormat df2 = new DecimalFormat("#0.00");
			JSONObject sectPos = new JSONObject();
			double avak1=0,avak2=0,qty1=0,qty2=0,total=0;
			int cnt1=0,cnt2=0;
			String veventName="",yearCode="";
			try(PreparedStatement pst=conn.prepareStatement("select t.dsug_date,sum(t.namount),TO_NCHAR(e.vevent_name)as vevent_name,y.vsugar_year  from MS_T_SUG_CARD_HEADER t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e, Ms_m_Sug_Card_Location l where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and t.nlocation = l.nlocation_id and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.nlocked = 'Y' and t.dsug_date <= ? group by t.dsug_date,e.vevent_name,y.vsugar_year order by t.dsug_date"))
			{
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						veventName=DemoConvert2.ism_to_uni(rs.getString("vevent_name"));
						yearCode=rs.getString("vsugar_year");
						daysCount++;
						
					}
				}
			}
			ArrayList<String> head=new ArrayList<>();
			head.add(veventName+"  "+yearCode+"  "+ConstantVeriables.lblDailySugarSaleReportHeading+" "+rdate);
			tableData.add(head);
			head=new ArrayList<>();
			head.add(ConstantVeriables.lblDaysCountHeading+" "+daysCount);
			tableData.add(head);
			head=new ArrayList<>();
			head.add("");
			head.add(ConstantVeriables.lblToDaySugarSale1);
			head.add(ConstantVeriables.lblToDateSugarSale);
			head.add(ConstantVeriables.total);
			tableData.add(head);
			head=new ArrayList<>();
			head.add(ConstantVeriables.lblSrno);
			head.add(ConstantVeriables.lblSectionName);
			head.add(ConstantVeriables.lblInward);
			head.add(ConstantVeriables.lblSale);
			head.add(ConstantVeriables.lblCount);
			head.add(ConstantVeriables.lblInward);
			head.add(ConstantVeriables.lblSale);
			head.add(ConstantVeriables.lblCount);
			head.add(ConstantVeriables.lblRemaning);
			tableData.add(head);
			try(PreparedStatement pst=conn.prepareStatement("select t.nlocation_id,TO_NCHAR(t.vlocation)as vlocation  from Ms_m_Sug_Card_Location t order by t.nlocation_id asc")){
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int pos = -1;
						String nlocationId=rs.getString("nlocation_id");
						ArrayList<String> data=new ArrayList<>();
						if(sectPos.has(nlocationId)) {
							pos = sectPos.getInt(nlocationId);
							data = tableData.get(pos);
						}else
						{
							data.add(""+(count++));
							data.add(DemoConvert2.ism_to_uni(rs.getString("vlocation")));
						}
						data.add("0");
						data.add("0");
						data.add("0");
						data.add("0");
						data.add("0");
						data.add("0");
						data.add("0");
						if(pos==-1) {
							sectPos.put(nlocationId, tableData.size());
							tableData.add(data);							
						} else {
							tableData.set(pos, data);			
						}
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select sum(t.nqty) as todate_avak,t.nlocation from MS_T_SUG_CARD_AWAK t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.dawak_date <= ? group by t.nlocation")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int pos = -1;
						String nlocationId=rs.getString("nlocation");
						ArrayList<String> data=new ArrayList<>();
						if(sectPos.has(nlocationId)) {
							pos = sectPos.getInt(nlocationId);
							data = tableData.get(pos);
							data.set(5,df2.format(rs.getDouble("todate_avak")));
							avak2+=rs.getDouble("todate_avak");
							if(pos==-1) {
								sectPos.put(nlocationId, tableData.size());
								tableData.add(data);							
							} else {
								tableData.set(pos, data);			
							}
						}
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select y.vsugar_year,l.nlocation_id,TO_NCHAR(l.vlocation)as vlocation,l.vlocation_eng,sum(t.nsugar_qty/100)as nsugar_qty,count(t.nentity_uni_id)as farmer_cnt,sum(t.namount)as amt,TO_NCHAR(e.vevent_name)as vevent_name from MS_T_SUG_CARD_HEADER t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e, Ms_m_Sug_Card_Location l where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and t.nlocation = l.nlocation_id and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.nlocked = 'Y' and t.dsug_date <=? group by l.nlocation_id,l.vlocation,l.vlocation_eng,e.vevent_name,y.vsugar_year order by l.nlocation_id,l.vlocation,l.vlocation_eng")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {

						int pos = -1;
						String nlocationId=rs.getString("nlocation_id");
						ArrayList<String> data=new ArrayList<>();
						if(sectPos.has(nlocationId)) {
							pos = sectPos.getInt(nlocationId);
							data = tableData.get(pos);
							data.set(6,df2.format(rs.getDouble("nsugar_qty")));
							qty2+=rs.getDouble("nsugar_qty");
							data.set(7,rs.getString("farmer_cnt"));
							cnt2+=rs.getInt("farmer_cnt");
							double avak=Double.parseDouble(data.get(5));
							double tot=avak-rs.getDouble("nsugar_qty");
							data.set(8,df2.format(tot));
							total+=tot;
							if(pos==-1) {
								sectPos.put(nlocationId, tableData.size());
								tableData.add(data);							
							} else {
								tableData.set(pos, data);			
							}
						}
					}
				}
			}
			
			// today
			try(PreparedStatement pst=conn.prepareStatement("select sum(t.nqty) as today_avak,t.nlocation from MS_T_SUG_CARD_AWAK t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.dawak_date = ? group by t.nlocation")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int pos = -1;
						String nlocationId=rs.getString("nlocation");
						ArrayList<String> data=new ArrayList<>();
						if(sectPos.has(nlocationId)) {
							pos = sectPos.getInt(nlocationId);
							data = tableData.get(pos);
							data.set(2,df2.format(rs.getDouble("today_avak")));
							avak1+=rs.getDouble("today_avak");
							if(pos==-1) {
								sectPos.put(nlocationId, tableData.size());
								tableData.add(data);							
							} else {
								tableData.set(pos, data);			
							}
						}
					}
				}
			}
			try(PreparedStatement pst=conn.prepareStatement("select t.nlocation,sum(t.nsugar_qty/100)as qty,count(t.nentity_uni_id)as farmer_cnt from MS_T_SUG_CARD_HEADER t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e  where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.nlocked = 'Y' and t.dsug_date =? group by t.nlocation order by t.nlocation")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int pos = -1;
						String nlocationId=rs.getString("nlocation");
						ArrayList<String> data=new ArrayList<>();
						if(sectPos.has(nlocationId)) {
							pos = sectPos.getInt(nlocationId);
							data = tableData.get(pos);
							data.set(3,df2.format(rs.getDouble("qty")));
							qty1+=rs.getDouble("qty");
							data.set(4,""+rs.getInt("farmer_cnt"));
							cnt1+=rs.getInt("farmer_cnt");
						}
					}
				}
			}
			
			ArrayList<String> footer=new ArrayList<>();	
			footer.add("");
			footer.add(ConstantVeriables.total);
			footer.add(df2.format(avak1));
			footer.add(df2.format(qty1));
			footer.add(""+cnt1);
			footer.add(df2.format(avak2));
			footer.add(df2.format(qty2));
			footer.add(""+cnt2);
			footer.add(df2.format(total));
			tableData.add(footer);
			
			boldIndicator.put((tableData.size()-1)+"-*", true);
			sumarryBean.setRowColSpan(rowColSpan);
			sumarryBean.setBoldIndicator(boldIndicator);
			sumarryBean.setTableData(tableData);
			sumarryBean.setFooter(true);
			sumarryBean.setColWidth(new Integer[]{6,17,11,11,11,11,11,11,11});
			reqResponse.setTableData(sumarryBean);
			
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

	public TableResponse sugarOutwardReport(String rdate, String chit_boy_id, TableResponse reqResponse,
			Connection conn) {
		try {
			//chit_boy_id="1096";
			TableReportBean outwardBean = new TableReportBean();
			int noofheadDailyCrush =1;
			outwardBean.setNoofHeads(noofheadDailyCrush);
			outwardBean.setFooter(true);
			outwardBean.setMarathi(true);
			
			HashMap<String, Integer> rowColSpan = new HashMap<>();
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			boldIndicator.put("0-*", true);
			rowColSpan.put("0-0", 2);
			double todateInwardQty = 0,todateSaleQty = 0,todateRemaningQty=0,todateAmount=0, todaySaleQty = 0,todaySaleAmount = 0;
			int todateNoOFFarmerQty=0,todayNoOFFarmerQty=0,todateSaleBag=0,todaySaleBag=0;
			try(PreparedStatement pst=conn.prepareStatement("select sum(t.nqty)as nqty from MS_T_SUG_CARD_AWAK t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.dawak_date <=? and t.nuser_id=?")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				pst.setString(i++, chit_boy_id);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						todateInwardQty=rs.getDouble("nqty");
					}
				}
			}

			try(PreparedStatement pst=conn.prepareStatement("select sum(t.nsugar_qty/100)as todate_sale,count(t.nentity_uni_id) as todate_farmer_qty,sum(t.nsugar_qty/30)as todate_sale_bg,sum(t.namount) as namount from MS_T_SUG_CARD_HEADER t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.nlocked = 'Y' and t.dsug_date <= ? and t.nemp_count_id=?")){
				int i = 1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				pst.setString(i++, chit_boy_id);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						todateSaleQty = rs.getDouble("todate_sale");
						todateNoOFFarmerQty = rs.getInt("todate_farmer_qty");
						todateAmount=rs.getDouble("namount");
						todateSaleBag=rs.getInt("todate_sale_bg");
					}
				}
			}
			todateRemaningQty = todateInwardQty - todateSaleQty;
			
			try(PreparedStatement pst=conn.prepareStatement(" select sum(t.nsugar_qty/100)as today_qty,sum(t.nsugar_qty/30)as today_sale_bg,count(t.nentity_uni_id)as today_farmer_qty,sum(t.namount)as today_amt from MS_T_SUG_CARD_HEADER t, MS_M_SUG_CARD_YEAR y, MS_M_SUG_CARD_EVENT e where t.vyear_id = y.vsugar_year and t.nsugar_for = e.ncode and y.vsug_year_active = 'Y' and e.vevent_active = 'Y' and t.nlocked = 'Y' and t.dsug_date =? and t.nemp_count_id=?")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				pst.setString(i++, chit_boy_id);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						todaySaleQty=rs.getDouble("today_qty");
						todayNoOFFarmerQty=rs.getInt("today_farmer_qty");
						todaySaleAmount=rs.getDouble("today_amt");
						todaySaleBag=rs.getInt("today_sale_bg");
					}
				}
			}

			DecimalFormat df = new DecimalFormat("#0");
			DecimalFormat df2 = new DecimalFormat("#0.00");
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();

			ArrayList<String> data = new ArrayList<>();
			data.add(rdate + " " + ConstantVeriables.lblDailyInwardReportHeading+"\n"+reqResponse.getVfullName());
			tableData.add(data);
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDateInward);
			data.add(df2.format(todateInwardQty));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDateSales);
			data.add(df2.format(todateSaleQty));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDateSalesBag);
			data.add(df2.format(todateSaleBag));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDateRemaning);
			data.add(df2.format(todateRemaningQty));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDatefarmerCount);
			data.add(df.format(todateNoOFFarmerQty));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDateSaleAmount);
			data.add(df2.format(todateAmount));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDaySugarSale);
			data.add(df2.format(todaySaleQty));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDaySalesBag);
			data.add(df2.format(todaySaleBag));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDayfarmerCount);
			data.add(df.format(todayNoOFFarmerQty));
			tableData.add(data);
			
			data = new ArrayList<>();
			data.add(ConstantVeriables.lblToDaySaleAmount);
			data.add(df2.format(todaySaleAmount));
			tableData.add(data);

			outwardBean.setRowColSpan(rowColSpan);
			outwardBean.setBoldIndicator(boldIndicator);
			outwardBean.setTableData(tableData);
			outwardBean.setFooter(false);
			outwardBean.setColWidth(new Integer[]{70,30});
			reqResponse.setTableData(outwardBean);
		
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

	public TableResponse sugarSummaryReport2(String rdate, String chit_boy_id, TableResponse reqResponse,
			Connection conn) {
		try {
			TableReportBean sumarryBean = new TableReportBean();
			int noofheadDailyCrush =2;
			sumarryBean.setNoofHeads(noofheadDailyCrush);
			sumarryBean.setFooter(true);
			sumarryBean.setMarathi(true);
			
			HashMap<String, Integer> rowColSpan = new HashMap<>();
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			HashMap<String, Boolean> visibility = new HashMap<>();
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();
			boldIndicator.put("0-*", true);
			boldIndicator.put("1-*", true);
			rowColSpan.put("0-3", 2);
			rowColSpan.put("0-4", 2);
			rowColSpan.put("0-5", 2);
			rowColSpan.put("0-6", 2);
			visibility.put("*-2", false);
			
			DecimalFormat df2 = new DecimalFormat("#0.00");
			DecimalFormat df = new DecimalFormat("#0");
			JSONObject sectPos = new JSONObject();
			ArrayList<String> head=new ArrayList<>();
			head.add(ConstantVeriables.lblSrno);
			head.add(ConstantVeriables.lblSectionName);
			head.add(ConstantVeriables.lblSectionName);
			head.add(ConstantVeriables.couponfarmer);
			head.add(ConstantVeriables.sugarDistFarmerCount);
			head.add(ConstantVeriables.sugarDistquntal);
			head.add(ConstantVeriables.sugarDistremain);
			tableData.add(head);
			head=new ArrayList<>();
			head.add("");
			head.add("");
			head.add("");
			head.add(ConstantVeriables.farmerCount);
			head.add(ConstantVeriables.sugarQuntal);
			head.add(ConstantVeriables.today);
			head.add(ConstantVeriables.todate);
			head.add(ConstantVeriables.today);
			head.add(ConstantVeriables.todate);
			head.add(ConstantVeriables.farmerCount);
			head.add(ConstantVeriables.sugarQuntal);
			tableData.add(head);
			int srno = 1;
			
			ArrayList<String> footer=new ArrayList<>();	
			footer.add("");
			footer.add(ConstantVeriables.total);
			footer.add("");
			footer.add("0");
			footer.add("0.00");
			footer.add("0");
			footer.add("0");
			footer.add("0");
			footer.add("0");
			footer.add("0");
			footer.add("0.00");
			
			try(PreparedStatement pst=conn.prepareStatement("select t4.nsection_id, t.nlocked, t5.vsection_name_local, t5.vsection_name, count(t.dsug_date) as notnulldate, count(t.nentity_uni_id) as total, (sum(t.nsugar_qty)/100) as totalsug from MS_T_SUG_CARD_HEADER t, MS_M_SUG_CARD_EVENT t1, MS_M_SUG_CARD_YEAR t2, GM_M_ENTITY_MASTER_DETAIL t3, GM_M_VILLAGE_MASTER t4, Gm_m_Section_Master t5 where t.nsugar_for = t1.ncode and t1.vevent_active='Y'  and  t.vyear_id=t2.vsugar_year and t2.vsug_year_active='Y' and t3.nentity_uni_id = t.nentity_uni_id and t3.nvillage_id=t4.nvillage_id  and t4.nsection_id = t5.nsection_id and (t.dsug_date<=To_DATE(?, 'dd/MM/yyyy') or t.dsug_date is null) group by t4.nsection_id, t.nlocked, t5.vsection_name_local, t5.vsection_name order by t4.nsection_id, t.nlocked")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int pos = -1;
						String nsectionId = rs.getString("nsection_id");
						ArrayList<String> data = null;
						if (sectPos.has(nsectionId)) {
							pos = sectPos.getInt(nsectionId);
							data = tableData.get(pos);
						} else {
							data = new ArrayList<>();
							data.add(String.valueOf(srno++));
							data.add(rs.getString("nsection_id") + " " + DemoConvert2.ism_to_uni(rs.getString("vsection_name_local")));
							data.add(rs.getString("vsection_name"));
							data.add("0");
							data.add("0.00");
							data.add("0");
							data.add("0");
							data.add("0");
							data.add("0");
							data.add("0");
							data.add("0.00");
							sectPos.put(nsectionId, srno);
						}
						String nlocked = rs.getString("nlocked");
						int total;
						if (nlocked.equals("L"))
							total = rs.getInt("notnulldate");
						else
							total = rs.getInt("total");
						boolean withdraw = nlocked.equals("Y");
						double totalsug = rs.getDouble("totalsug");

						data.set(3, df.format(Integer.parseInt(data.get(3)) + total));
						data.set(4, df2.format(Double.parseDouble(data.get(4)) + totalsug));
						
						footer.set(3, df.format(Integer.parseInt(footer.get(3)) + total));
						footer.set(4, df2.format(Double.parseDouble(footer.get(4)) + totalsug));

						if (withdraw) {
							data.set(6, df.format(Integer.parseInt(data.get(6)) + total));
							data.set(8, df2.format(Double.parseDouble(data.get(8)) + totalsug));
							
							footer.set(6, df.format(Integer.parseInt(footer.get(6)) + total));
							footer.set(8, df2.format(Double.parseDouble(footer.get(8)) + totalsug));
						} else {
							data.set(9, df.format(Integer.parseInt(data.get(9)) + total));
							data.set(10, df2.format(Double.parseDouble(data.get(10)) + totalsug));
							
							footer.set(9, df.format(Integer.parseInt(footer.get(9)) + total));
							footer.set(10, df2.format(Double.parseDouble(footer.get(10)) + totalsug));
						}
						if (pos == -1) {
							sectPos.put(nsectionId, tableData.size());
							tableData.add(data);
						} else {
							tableData.set(pos, data);
						}
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select t4.nsection_id, count(t.nentity_uni_id) as total, sum(t.nsugar_qty)/100 as totalsug from MS_T_SUG_CARD_HEADER t, MS_M_SUG_CARD_EVENT t1, MS_M_SUG_CARD_YEAR t2, GM_M_ENTITY_MASTER_DETAIL t3, GM_M_VILLAGE_MASTER t4 where t.nsugar_for = t1.ncode and t1.vevent_active='Y'  and  t.vyear_id=t2.vsugar_year and t2.vsug_year_active='Y'  and t3.nentity_uni_id = t.nentity_uni_id and t3.nvillage_id=t4.nvillage_id  and t.dsug_date=To_DATE(?, 'dd-Mon-yyyy') and t.nlocked= 'Y' group by t4.nsection_id order by t4.nsection_id")){
				int i=1;
				pst.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						int pos = -1;
						String nsectionId = rs.getString("nsection_id");
						ArrayList<String> data = null;
						if (sectPos.has(nsectionId)) {
							pos = sectPos.getInt(nsectionId);
							data = tableData.get(pos);
						} else
							continue;
						int total = rs.getInt("total");
						double totalsug = rs.getDouble("totalsug");

						data.set(5, df.format(Integer.parseInt(data.get(5)) + total));
						data.set(7, df2.format(Double.parseDouble(data.get(7)) + totalsug));
						
						footer.set(5, df.format(Integer.parseInt(footer.get(5)) + total));
						footer.set(7, df2.format(Double.parseDouble(footer.get(7)) + totalsug));

						tableData.set(pos, data);
					}
				}
			}
			
			tableData.add(footer);
			
			boldIndicator.put((tableData.size()-1)+"-*", true);
			sumarryBean.setRowColSpan(rowColSpan);
			sumarryBean.setBoldIndicator(boldIndicator);
			sumarryBean.setTableData(tableData);
			sumarryBean.setFooter(true);
			sumarryBean.setVisibility(visibility);
			sumarryBean.setColWidth(new Integer[]{6,14,10,10,10,10,10,10,10,10});
			reqResponse.setTableData(sumarryBean);
			
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

}
