package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.CaneYardBalanceResponse;
import com.twd.pandurangsugar.android.bean.DataListResonse;
import com.twd.pandurangsugar.android.bean.DataTwoListResonse;
import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.android.bean.LotGenResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NumIndResponse;
import com.twd.pandurangsugar.android.bean.NumSlip;
import com.twd.pandurangsugar.android.bean.NumSlipListResponse;
import com.twd.pandurangsugar.android.bean.SavePrintResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SingleNumDataResponse;
import com.twd.pandurangsugar.android.bean.TableReportBean;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.bean.UserRoleResponse;
import com.twd.pandurangsugar.android.bean.UserYardResponse;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class NumberSystemDao {

	String lineBreak = "<tr><td colspan='3'><hr style=\"height:1px; background-color:#000; border:none;\"></td><tr>";
	public UserYardResponse userYardInfo(String empcode, UserYardResponse userYardResponse, Connection conn) {

		try {
			
			try(PreparedStatement pst=conn.prepareStatement("select TO_NCHAR(t.vfull_name_local) as vfull_name_local, nyard_id from App_m_User_Master t where t.nuser_name=?")){
				int i=1;
				pst.setString(i++, empcode);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						userYardResponse.setUserId(empcode);
						userYardResponse.setUserName(DemoConvert2.ism_to_uni(rs.getString("vfull_name_local")));
						userYardResponse.setCurrentYardId(rs.getString("nyard_id"));
					}
				}
			}
		} catch (Exception e) {
			userYardResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			userYardResponse.setSe(error);
			e.printStackTrace();
		}
		return userYardResponse;
	}

	public ActionResponse updateYardEmp(String empcode, String yardid, ActionResponse updateYardResponse,
			Connection conn) {
		try
		{
			try(PreparedStatement pst=conn.prepareStatement("UPDATE App_m_User_Master SET nyard_id =? WHERE nuser_name=?"))
			{
				int i=1;
				pst.setString(i++, yardid);
				pst.setString(i++, empcode);
				int res=pst.executeUpdate();
				updateYardResponse.setSuccess(true);
				if (res > 0) {
					updateYardResponse.setActionComplete(true);
					updateYardResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				} else {
					updateYardResponse.setActionComplete(false);
					updateYardResponse.setSuccessMsg(ConstantMessage.saveFailed);
				}
			}
		} catch (SQLException e) {
			updateYardResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Request Platation From " + e.getMessage());
			updateYardResponse.setSe(error);
			e.printStackTrace();
		}
		return updateYardResponse;
	}

	public ActionResponse removeYardEmp(String empcode, String yardid, ActionResponse updateYardResponse,
			Connection conn) {

		try
		{
			try(PreparedStatement pst=conn.prepareStatement("UPDATE App_m_User_Master SET nyard_id = null WHERE  nyard_id =? and nuser_name=?"))
			{
				int i=1;
				pst.setString(i++, yardid);
				pst.setString(i++, empcode);
				int res=pst.executeUpdate();
				updateYardResponse.setSuccess(true);
				if (res > 0) {
					updateYardResponse.setActionComplete(true);
					updateYardResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				} else {
					updateYardResponse.setActionComplete(false);
					updateYardResponse.setSuccessMsg(ConstantMessage.saveFailed);
				}
			}
		} catch (SQLException e) {
			updateYardResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Request Platation From " + e.getMessage());
			updateYardResponse.setSe(error);
			e.printStackTrace();
		}
		return updateYardResponse;
	
	}

	public NumSlipListResponse slipDataList(String type, String code, String vtype, String vyearid,
			NumSlipListResponse slipDataResponse, Connection conn) {
		try {
			int yardId = slipDataResponse.getNyardId();
			String slipNo=code.replace(ConstantVeriables.lblExtraQR, "");
			if (type.equalsIgnoreCase("Q")) {
				
				try (PreparedStatement pst = conn.prepareStatement("select t.ntransportor_id,t.nbulluckcart_id, t.rowid from "+ConstantVeriables.weightSlipTableName+" t where t.vyear_id=? and t.nslip_no=?")) {
					int i = 1;
					pst.setString(i++, vyearid);
					pst.setString(i++, slipNo);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							String transportorCode=rs.getString("ntransportor_id");
							String bulluckcartCode=rs.getString("nbulluckcart_id");
							if(transportorCode!=null && !transportorCode.equalsIgnoreCase("")){
								code=transportorCode;
								vtype="T";
							}else if(bulluckcartCode!=null && !bulluckcartCode.equalsIgnoreCase("")){
								code=bulluckcartCode;
								vtype="B";
							}							
						}
					}
				}
			}
			
			boolean isSelf = false;
			try (PreparedStatement pst = conn.prepareStatement("select t.nentity_uni_id from APP_M_TRANS_SELF_OF t where t.nentity_uni_id=?")) {
				int i = 1;
				pst.setString(i++, code);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						isSelf = true;			
					}
				}
			}
			
			if (isSelf && type.equalsIgnoreCase("C")) {
				throw new Exception(ConstantMessage.errorthiscodeallowonlythroughqr);	
			}
			
			String sql = "select t.vyear_id from app_t_numbertaker t where t.vyear_id=? and t.vactive_status in (1,4) ";
			if (!isSelf)
				sql += " and t.nentity_uni_id=? ";
			else
				sql += " and t.nslip_no=? ";
			
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, vyearid);
				if (!isSelf)
					pst.setString(i++, code);
				else
					pst.setString(i++, slipNo);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						throw new Exception(ConstantMessage.errorpreviousno);							
					}
				}
			}
			
			HashMap<Integer, String> wirerope = new HashMap<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.nwirerope_id, t.vwire_name from wb_m_wirerope_master t ")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						wirerope.put(rs.getInt("nwirerope_id"), DemoConvert2.ism_to_uni(rs.getString("vwire_name")));			
					}
				}
			}
			
			List<NumSlip> slipdataList = new ArrayList();
			sql = "select t.ntailor_front, t.ntailor_back, t.nwirerope_no, t.vyear_id,t.nslip_no,t.nentity_uni_id,To_Nchar(d.ventity_name_local) as farmerName, t.ntransportor_id,t.nbulluckcart_id,To_Nchar(vt.ventity_name_local) as transname,t.vvehicle_no,t.nvehicle_type_id,t.nvillage_id,vtm.nvehicle_group_id,vm.village_name_local "; 
			sql += " from "+ConstantVeriables.weightSlipTableName+" t,Gm_m_Entity_Master_Detail d,Gm_m_Entity_Master_Detail vt,GM_M_VEHICLE_TYPE_MASTER vtm,GM_M_VILLAGE_MASTER vm where d.nentity_uni_id=t.nentity_uni_id  and t.nvehicle_type_id=vtm.nvehicle_type_id and  t.nvillage_id=vm.nvillage_id and nvl(t.ngross_weight,0)=0 ";
			if (vtype.equalsIgnoreCase("T"))
				sql += " and t.ntransportor_id=vt.nentity_uni_id and t.vyear_id=? and t.ntransportor_id=? and t.ntoken_no is null and t.nlot_no is null and t.vactive_dactive='A'";
			else
				sql += " and t.nbulluckcart_id=vt.nentity_uni_id and t.vyear_id=? and t.nbulluckcart_id=? and t.ntoken_no is null and t.nlot_no is null and t.vactive_dactive='A'";
			if(isSelf) {
				sql += " and t.nslip_no=? ";
			}
					 
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i=1;
				pst.setString(i++, vyearid);
				pst.setString(i++, code);
				if (isSelf) {
					pst.setString(i++, slipNo);
				}
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String nvehgroupid = rs.getString("nvehicle_group_id");
						String yardName = checkYardavailabile(yardId, nvehgroupid, conn);
						if (yardName!=null) {
							String nvehicle_type_id = rs.getString("nvehicle_type_id");
							NumSlip slipdata = new NumSlip();
							slipdata.setSeason(rs.getString("vyear_id"));
							slipdata.setSlipno(rs.getString("nslip_no"));
							slipdata.setVehicleno(rs.getString("vvehicle_no"));
							slipdata.setVillageid(rs.getString("nvillage_id"));
							slipdata.setVehicletype(nvehicle_type_id);
							slipdata.setVehiclegroupid(nvehgroupid);
							slipdata.setVillageName(DemoConvert2.ism_to_uni(rs.getString("village_name_local")));
							slipdata.setFarmer(rs.getString("nentity_uni_id") + " " + DemoConvert2.ism_to_uni(rs.getString("farmerName")));
							
							if (nvehicle_type_id.equals("1")) {
								slipdata.setFrontTailer(wirerope.get(rs.getInt("nwirerope_no")));
							} else {
								slipdata.setFrontTailer(wirerope.get(rs.getInt("ntailor_front")));
								slipdata.setBackTailer(wirerope.get(rs.getInt("ntailor_back")));
							}

							if (vtype.equalsIgnoreCase("T")){
								slipdata.setTranscode(rs.getString("ntransportor_id"));
								slipdata.setTransporter(rs.getString("ntransportor_id") + " " + DemoConvert2.ism_to_uni(rs.getString("transname")));
							}
							else{
								slipdata.setTranscode(rs.getString("nbulluckcart_id"));
								slipdata.setTransporter(rs.getString("nbulluckcart_id") + " " + DemoConvert2.ism_to_uni(rs.getString("transname")));
							}
							slipdataList.add(slipdata);
						} else {
							slipDataResponse.setSuccess(false);
							ServerError error = new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(ConstantMessage.errorYardavailabile);
							slipDataResponse.setSe(error);
							break;
						}
					}
				}
			}
			slipDataResponse.setNumSlips(slipdataList);
			if(slipDataResponse.isSuccess() && slipdataList.size()<1){
				slipDataResponse.setSuccess(false);
				ServerError error = new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg(ConstantMessage.errorwslipnotavailble);
				slipDataResponse.setSe(error);
			}

		} catch (Exception e) {
			slipDataResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			slipDataResponse.setSe(error);
			//e.printStackTrace();
		}
		return slipDataResponse;

	}

	private String checkYardavailabile(int yardId, String nvehgroupid, Connection conn) {
		String result=null;
		String sql="select To_nchar(t.vyard_name_mar) as vyardname from APP_M_CANEYARD t where t.nyard_id=? and ";
		if (nvehgroupid.equalsIgnoreCase("1"))
			sql += "t.vtractor_truck='Y'";
		else if (nvehgroupid.equalsIgnoreCase("2"))
			sql += "t.vbajat='Y'";
		else
			return result;
		try (PreparedStatement pst = conn.prepareStatement(sql)) {
			int i = 1;
			pst.setInt(i++, yardId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) 
					result=DemoConvert2.ism_to_uni(rs.getString("vyardname"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static synchronized SavePrintResponse saveNumber(String vyear_id, String nslip_no, String nvillage_id, String nentity_uni_id,
			String nvehicle_type, String vvehicle_no, String vlatitude, String vlongitude, String vaccuracy,
			String vphoto, String chit_boy_id,String nvehicle_group_id,String vvillage_name,String vtrans_name, SavePrintResponse saveNumberResponse, Connection conn) {
		try {
			conn.setAutoCommit(false);
			int yardId = saveNumberResponse.getNyardId();
			NumberSystemDao numsysdao = new NumberSystemDao();
			//int lotLimit = 20;
			int transId = 1;
			//int lotno = 1;
			int tokenno = 1;
			//int curlottoken = 0;
			String shiftName="";
			String yardName = numsysdao.checkYardavailabile(yardId, nvehicle_group_id, conn);
			if (yardName!=null) {
				/*try (PreparedStatement pst = conn.prepareStatement("select t.nlot_limit from APP_M_NUMBER_VALIDATION t where t.vyear_id = ? and t.nvehicle_group_id = ?")) {
					int i = 1;
					pst.setString(i++, vyear_id);
					pst.setString(i++, nvehicle_group_id);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							lotLimit = rs.getInt("nlot_limit");
						}
					}
				}*/
				
				boolean isSelf = false;
				try (PreparedStatement pst = conn.prepareStatement("select t.nentity_uni_id from APP_M_TRANS_SELF_OF t where t.nentity_uni_id=?")) {
					int i = 1;
					pst.setString(i++, nentity_uni_id);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							isSelf = true;			
						}
					}
				}
				
			String sql="select t.vyear_id from app_t_numbertaker t where t.vyear_id=? and t.vactive_status in (1,4)";
				if (!isSelf)
					sql += " and t.nentity_uni_id=? ";
				else
					sql += " and t.nslip_no=? ";
				try (PreparedStatement pst = conn.prepareStatement(sql)) {
					int i = 1;
					pst.setString(i++, vyear_id);
					pst.setString(i++, nentity_uni_id);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							throw new Exception(ConstantMessage.errorpreviousno);							
						}
					}
				}
				
				try (PreparedStatement pst = conn.prepareStatement("select max(t.ntrans_id) as maxid from APP_T_NUMBERTAKER t where t.vyear_id=?")) {
					int i = 1;
					pst.setString(i++, vyear_id);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							transId = rs.getInt("maxid") + 1;
						}
					}
				}
				
				/*try (PreparedStatement pst = conn.prepareStatement("select nvl(max(t.nlot_no),1) as lotno from APP_T_NUMBERTAKER t where t.vyear_id=? and t.nvehicle_group_id=? and t.nyard_id=?")) {
					int i = 1;
					pst.setString(i++, vyear_id);
					pst.setString(i++, nvehicle_group_id);
					pst.setInt(i++, saveNumberResponse.getNyardId());
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							lotno = rs.getInt("lotno");
						}
					}
				}*/
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.HOUR_OF_DAY, -4);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
				try (PreparedStatement pst = conn.prepareStatement("select max(t.ntoken_no) as tokenno from APP_T_NUMBERTAKER t where t.vyear_id=? and t.nvehicle_group_id=? and t.nyard_id=? and t.dcrush_date=To_date(?, 'dd-MM-yyyy')")) { //and t.nlot_no=? //, count(t.ntoken_no) as curlottoken
					int i = 1;
					pst.setString(i++, vyear_id);
					pst.setString(i++, nvehicle_group_id);
					pst.setInt(i++, saveNumberResponse.getNyardId());
					//pst.setInt(i++, lotno);
					pst.setString(i++, sdf.format(cal.getTime()));
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							tokenno = rs.getInt("tokenno") + 1;
							//curlottoken = rs.getInt("curlottoken");
						}
					}
				}

				/*if (lotLimit == curlottoken) {
					lotno += 1;
				}*/
				DecimalFormat df = new DecimalFormat("#0.000");
				DecimalFormat dfTime = new DecimalFormat("#00");
				int size = 0;
				String sqlinsert = "insert into app_t_numbertaker(vyear_id, ntrans_id, ddate, nslip_no, nvillage_id, nentity_uni_id, nvehicle_group_id, vvehicle_no, dcrush_date, nshift_no, nyard_id, ntoken_no, vlatitude, vlongitude, vaccuracy, vphoto, vactive_status, ncreate_user) values(?,?,sysdate,?,?,?,?,?,To_date(?, 'dd-MM-yyyy'),?,?,?,?,?,?,?,?,?)";//nlot_no,?, 
				try (PreparedStatement pst = conn.prepareStatement(sqlinsert)) {
					int i = 1;
					pst.setString(i++, vyear_id);
					pst.setInt(i++, transId);
					pst.setString(i++, nslip_no);
					pst.setString(i++, nvillage_id);
					pst.setString(i++, nentity_uni_id);
					pst.setString(i++, nvehicle_group_id);
					pst.setString(i++, vvehicle_no);
					pst.setString(i++, sdf.format(cal.getTime()));
					int hoursOfDay = cal.get(Calendar.HOUR_OF_DAY);
					if (hoursOfDay < 8) {
						shiftName = "4-12";
						pst.setInt(i++, 1);
					} else if (hoursOfDay < 16) {
						shiftName = "12-8";
						pst.setInt(i++, 2);
					} else {
						shiftName = "8-4";
						pst.setInt(i++, 3);
					}
					pst.setInt(i++, yardId);
					// pst.setInt(i++,lotno);
					pst.setInt(i++, tokenno);
					pst.setString(i++, vlatitude);
					pst.setString(i++, vlongitude);
					pst.setString(i++, df.format(Double.parseDouble(vaccuracy)));
					pst.setString(i++, vphoto);
					pst.setString(i++, "1");// active Status
					pst.setString(i++, chit_boy_id);
					size += pst.executeUpdate();
				}
				
				String sqlupdate = "update " + ConstantVeriables.weightSlipTableName + " t set t.ntoken_no=? where t.vyear_id = ? AND t.nslip_no in (" + nslip_no + ")"; // ,t.nlot_no=?
				try (PreparedStatement pst = conn.prepareStatement(sqlupdate)) {
					int i = 1;
					pst.setInt(i++, tokenno);
					// pst.setInt(i++, lotno);
					pst.setString(i++, vyear_id);
					size += pst.executeUpdate();
				}
				if (size >= 2) {
					conn.commit();
					saveNumberResponse.setSuccess(true);
					saveNumberResponse.setActionComplete(true);
					saveNumberResponse.setSuccessMsg(ConstantMessage.numbersavesuccess);
					cal.add(Calendar.HOUR_OF_DAY, 4);
					String print = "<table>";
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lblSlipNoShort + "</td>"
							+ "<td class='cbody'> : " + nslip_no + "</td>"
							+ "</tr>";
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lblDate + "</td>"
							+ "<td class='cbody'> : " + sdf.format(cal.getTime()) + "</td>"
							+ "</tr>";
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lbltime + " : " + "</td>"
							+ "<td class='cbody'> : " +  dfTime.format(cal.get(Calendar.HOUR_OF_DAY)) + ":" + dfTime.format(cal.get(Calendar.MINUTE)) + ":" + dfTime.format(cal.get(Calendar.SECOND)) + "</td>"
							+ "</tr>";
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lblshift + "</td>"
							+ "<td class='cbody'> : " + shiftName + "</td>"
							+ "</tr>";
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lblYard + "</td>"
							+ "<td class='cbody'> : " + yardName + "</td>"
							+ "</tr>";
					/*print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lblLotno + "</td>"
							+ "<td class='cbody'> : " + lotno + "</td>"
							+ "</tr>";*/
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lbltokenno + "</td>"
							+ "<td class='cbody'> : " + tokenno + "</td>"
							+ "</tr>";
					print += "<tr>"
							+ "<td class='cbodysmall'>" + ConstantVeriables.lblTranspoterName + "</td>"
							+ "<td class='cbodysmall'> : " + vtrans_name + "</td>"
							+ "</tr>";
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lblVehicleNoShort + "</td>"
							+ "<td class='cbody'> : " + vvehicle_no + "</td>"
							+ "</tr>";
					print += "<tr>"
							+ "<td class='cbody'>" + ConstantVeriables.lblVilleage + "</td>"
							+ "<td class='cbody'> : " + vvillage_name + "</td>"
							+ "</tr>";
					print += "</table>";
					saveNumberResponse.setHtmlContent(print);
				} else {
					conn.rollback();
					saveNumberResponse.setSuccess(true);
					saveNumberResponse.setActionComplete(false);
					saveNumberResponse.setFailMsg(ConstantMessage.errorNumebrSave);
				}
			} else {
				saveNumberResponse.setSuccess(false);
				ServerError error = new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg(ConstantMessage.errorYardavailabile);
				saveNumberResponse.setSe(error);
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			saveNumberResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg(e.getLocalizedMessage());
			saveNumberResponse.setSe(error);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return saveNumberResponse;
	}

	public MainResponse generateLotList(String vehicleGroupId, String yardId, String yearId,String chit_boy_id,
			MainResponse lotResponse, Connection conn) {

		try {
			boolean headAndPrint = lotResponse instanceof LotGenResponse;
			int shiftId=0;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -4);
			
			int hoursOfDay = cal.get(Calendar.HOUR_OF_DAY);
			if (hoursOfDay < 8)
				shiftId = 1;
			else if (hoursOfDay < 16)
				shiftId = 2;
			else
				shiftId = 3;
				
			TableReportBean lotlistTableBean = new TableReportBean();
			int noofheadDailyCrush =1;
			lotlistTableBean.setNoofHeads(noofheadDailyCrush);
			lotlistTableBean.setFooter(false);
			lotlistTableBean.setMarathi(false);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> varietyTonnageTableData = new ArrayList<>();
			ArrayList<String> lotListRow = new ArrayList<>();
			lotListRow.add(ConstantVeriables.lbltokenno);
			lotListRow.add(ConstantVeriables.lblTranspoterName);
			lotListRow.add(ConstantVeriables.lblVehicleNo);
			lotListRow.add(ConstantVeriables.lblDate);
			lotListRow.add(ConstantVeriables.lblshift);
			lotListRow.add(ConstantVeriables.lblVilleage);
			
			varietyTonnageTableData.add(lotListRow);
			
			boldIndicator.put("0-*", true);
			String sql="select t.nlot_limit from APP_M_NUMBER_VALIDATION t where t.vyear_id=? and t.nvehicle_group_id=? and t.nshift_id=?";
			int nlot_limit=0;
			try(PreparedStatement pst=conn.prepareStatement(sql)){
				int i=1;
				pst.setString(i++, yearId);
				pst.setString(i++, vehicleGroupId);
				pst.setInt(i++, shiftId);				
				
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						nlot_limit=rs.getInt("nlot_limit");
					}
				}
			}
			if(!chit_boy_id.equalsIgnoreCase("1129")){
				Date ddate=null;
				 sql="SELECT ddate FROM (SELECT t.ddate FROM APP_T_LOT_DETAILS t where t.nlot_no > 0 and t.vyear_id=? and t.nvehicle_group_id=? and t.nyard_id=? ORDER BY t.ddate desc ) WHERE ROWNUM <= 1";
				try(PreparedStatement pst=conn.prepareStatement(sql)){
					int i=1;
					pst.setString(i++, yearId);
					pst.setString(i++, vehicleGroupId);
					pst.setString(i++, yardId);				
					
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							if(rs.getString("ddate")!=null)
							{
								ddate=rs.getTimestamp("ddate");
							}
						}
					}
				}
				if(ddate!=null){
					sql="select t.nlimit_time from APP_M_CANEYARD t where t.nyard_id=?";
					try(PreparedStatement pst=conn.prepareStatement(sql)){
						int i=1;
						pst.setString(i++, yardId);				
						
						try (ResultSet rs = pst.executeQuery()) {
							if (rs.next()) {
								int nlimit_time=rs.getInt("nlimit_time");
								if(nlimit_time>0){
									Calendar cal2 = Calendar.getInstance();
									cal2.add(Calendar.MINUTE, nlimit_time *(-1));
									if(ddate.after(cal2.getTime())){
										throw new Exception(String.format(ConstantMessage.errorlottime,nlimit_time)); 
									}
								}							
								
							}
						}
					}
				}
			}
			
			ArrayList<String> transSelf = new ArrayList<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.nentity_uni_id from APP_M_TRANS_SELF_OF t")) {
				
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						transSelf.add(rs.getString("nentity_uni_id"));
					}
				}
			}
			
			sql="select t1.ntrans_id, t1.nslip_no, t1.ntoken_no,t1.nentity_uni_id,t1.vvehicle_no,t1.ddate,t1.nshift_no,t1.nvillage_id,t1.ventity_name_local,t1.village_name_local from (select t.ntrans_id, t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate,t.nshift_no,t.nvillage_id,emd.ventity_name_local,vm.village_name_local from APP_T_NUMBERTAKER t, Gm_m_Entity_Master_Detail emd,GM_M_VILLAGE_MASTER vm where t.nentity_uni_id=emd.nentity_uni_id and t.nvillage_id=vm.nvillage_id and t.vyear_id=? and t.nyard_id=? and t.nvehicle_group_id=? and t.nlot_no is null and t.vactive_status=1 order by t.dcrush_date,t.ddate,t.ntoken_no asc) t1 where ROWNUM <= ?";

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			SimpleDateFormat sdfprint = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			String shift[] = new String[] { "", "4-12", "12-8", "8-4" };
			
			String htmlContent = "<table width='100%'>";
			htmlContent += "<tr>" 
					+ "<td><b><span class='cbody' style='float:left;width: 40%;'>" + ConstantVeriables.lbltokenno + "</span> <span class='cbodysmall' style='text-align:right;float:right;width: 60%;'>" + ConstantVeriables.lblcodeAndName + "</span></b></td>"
					+ "</tr><tr>"
					+ "<td><b><span class='cbody' style='float:left;width: 40%;'>" + ConstantVeriables.lblVehicleNo + "</span> <span class='cbodysmall' style='text-align:right;float:right;width:60%;'>" + ConstantVeriables.lblentrytime + "</span></b></td>" 
					+ "</tr><tr>"
					+ "<td class='cbody'><b>" + ConstantVeriables.lblSLipNo + "</b></td>"
					+ "</tr>";
			htmlContent += lineBreak;
			
			String transId=null,nslipno=null;
			
			try(PreparedStatement pst=conn.prepareStatement(sql)){
				int i=1;
				pst.setString(i++, yearId);				
				pst.setString(i++, yardId);	
				pst.setString(i++, vehicleGroupId);
				pst.setInt(i++, nlot_limit);					
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						if (transId == null) {
							transId="";
							nslipno="";
						} else {
							transId+=",";
							nslipno+=",";
						}
						String slip_no = rs.getString("nslip_no");
						transId+=rs.getString("ntrans_id");
						nslipno+=slip_no;
						String tokenno = rs.getString("ntoken_no");
						String vvehicle_no = rs.getString("vvehicle_no");
						String nentity_uni_id = rs.getString("nentity_uni_id");
						String name = DemoConvert2.ism_to_uni(rs.getString("ventity_name_local"));
						
						
						htmlContent += "<tr>" 
								+ "<td><span class='cbody' style='float:left;width: 15%;'>" + tokenno + "</span> <span class='cbodysmall' style='text-align:right;float:right;width: 85%;'>" + nentity_uni_id + " " + name + "</span></td>"
								+ "</tr><tr>"
								+ "<td><span class='cbody' style='float:left;width: 42%;'>" + vvehicle_no + "</span> <span class='cbodysmall' style='text-align:right;float:right;width: 58%;'>" + sdfprint.format(rs.getTimestamp("ddate")) + "</span></td>"
								+ "</tr><tr>"
								+ "<td class='cbody'>" + slip_no + "</td>" 
								+ "</tr>";
						htmlContent += lineBreak;
						lotListRow = new ArrayList<>();
						lotListRow.add(tokenno);
						lotListRow.add(rs.getString("nentity_uni_id") + " " + DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
						lotListRow.add(vvehicle_no);
						lotListRow.add(sdf.format(rs.getTimestamp("ddate")));
						lotListRow.add(shift[rs.getInt("nshift_no")]);
						lotListRow.add(DemoConvert2.ism_to_uni(rs.getString("village_name_local")));

						varietyTonnageTableData.add(lotListRow);
						
					}
				}
			}
			htmlContent += "</table>";
			
			lotlistTableBean.setTableData(varietyTonnageTableData);
			lotlistTableBean.setColWidth(new Integer[]{10,33,15,20,7,15});
			if(headAndPrint) {
				if(transId==null)
					throw new Exception(ConstantMessage.errornonumberavaialble);
				
				
				
				int newlot=1;
				sql="SELECT max(t.nlot_no) as newlotno FROM APP_T_LOT_DETAILS t where t.nlot_no > 0 and t.vyear_id=? and t.nvehicle_group_id=? and t.nyard_id=?";
				try(PreparedStatement pst=conn.prepareStatement(sql)){
					int i=1;
					pst.setString(i++, yearId);
					pst.setString(i++, vehicleGroupId);
					pst.setString(i++, yardId);				
					
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							newlot=rs.getInt("newlotno")+1;
						}
					}
				}
				
				int  res=0;
				conn.setAutoCommit(false);
				try(PreparedStatement pst=conn.prepareStatement("Insert into  APP_T_LOT_DETAILS (VYEAR_ID,DDATE,NSHIFT_ID,NYARD_ID,NVEHICLE_GROUP_ID,NLOT_NO,NCREATE_USER_ID) Values(?,sysdate,?,?,?,?,?)"))
				{
					int i=1;
					pst.setString(i++, yearId);
					pst.setInt(i++, shiftId);
					pst.setString(i++, yardId);
					pst.setString(i++, vehicleGroupId);
					pst.setInt(i++, newlot);
					pst.setString(i++, chit_boy_id);
					res+=pst.executeUpdate();
					
				}
				
				try(PreparedStatement pst=conn.prepareStatement("UPDATE APP_T_NUMBERTAKER t SET t.vactive_status=4, t.nlot_no =? WHERE t.vyear_id =? and t.ntrans_id in("+transId+")"))
				{
					int i=1;
					pst.setInt(i++, newlot);
					pst.setString(i++, yearId);
					res+=pst.executeUpdate();
					
				}
				
				try(PreparedStatement pst=conn.prepareStatement("UPDATE "+ConstantVeriables.weightSlipTableName+" t SET t.nlot_no =? WHERE t.vyear_id =? and t.nslip_no in("+nslipno+")"))
				{
					int i=1;
					pst.setInt(i++, newlot);
					pst.setString(i++, yearId);
					res+=pst.executeUpdate();
					
				}
				LotGenResponse lotres = (LotGenResponse) lotResponse;
				if(res>=3){
					conn.commit();
					Calendar cal3 = Calendar.getInstance();
					SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
					String head = "वाहतूक नंबर दिनांक "+sdf3.format(cal3.getTime())+", लॉट नं. "+newlot+", यार्ड नाव : %1s, वाहन प्रकार ग्रुप : %2s";
					String printHead = "दिनांक "+sdf3.format(cal3.getTime())+", लॉट नं. "+newlot;					
					lotres.setHtmlContent(htmlContent);
					lotres.setVprintHead(printHead);
					lotres.setVhead(head);
					lotres.setTableData(lotlistTableBean);
					lotres.setSuccess(true);
					lotres.setActionComplete(true);
					lotres.setSuccessMsg(ConstantMessage.saveSuccess);
				}else{
					conn.rollback();
					lotres.setSuccess(true);
					lotres.setActionComplete(false);
					lotres.setFailMsg(ConstantMessage.saveFailed);
				}
				
				return lotres;				
			} else {
				TableResponse tblres = (TableResponse) lotResponse;
				tblres.setTableData(lotlistTableBean);
				return tblres;
			}
			
		} catch (Exception e) {
			
			lotResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg(" List Error " + e.getMessage());
			lotResponse.setSe(error);
			e.printStackTrace();
		}
		
		return lotResponse;
	}

	public SingleNumDataResponse singleNumData(String type, String code,String vyearid,String vtype, String chit_boy_id,
			SingleNumDataResponse singlenumdataResponse, Connection conn) {	
		try {

			if (type.equalsIgnoreCase("Q")) {
				String slipNo = code.replace(ConstantVeriables.lblExtraQR, "");
				try (PreparedStatement pst = conn.prepareStatement("select t.ntransportor_id,t.nbulluckcart_id, t.rowid from "+ConstantVeriables.weightSlipTableName+" t where t.vyear_id=? and t.nslip_no=?")) {
					int i = 1;
					pst.setString(i++, vyearid);
					pst.setString(i++, slipNo);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							String transportorCode = rs.getString("ntransportor_id");
							String bulluckcartCode = rs.getString("nbulluckcart_id");
							if (transportorCode != null && !transportorCode.equalsIgnoreCase("")) {
								code = transportorCode;
								vtype = "T";
							} else if (bulluckcartCode != null && !bulluckcartCode.equalsIgnoreCase("")) {
								code = bulluckcartCode;
								vtype = "B";
							}
						}
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				
			try (PreparedStatement pst = conn.prepareStatement("select t.nyard_id,t.nvehicle_group_id,t.ntrans_id,t.nslip_no,t.ntoken_no,TO_NCHAR(em.ventity_name_local) ventity_name_local,t.ddate,TO_NCHAR(amc.vyard_name_mar) vyard_name_mar from app_t_numbertaker t,Gm_m_Entity_Master_Detail em,APP_M_CANEYARD amc where t.nentity_uni_id=em.nentity_uni_id and t.nyard_id=amc.nyard_id and  t.vyear_id=? and t.nentity_uni_id=? and t.vactive_status=1 and t.nlot_no is null")) {
				int i = 1;
				pst.setString(i++, vyearid);
				pst.setString(i++, code);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						singlenumdataResponse.setName(code + " " + DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
						singlenumdataResponse.setTime(sdf.format(rs.getTimestamp("ddate")));
						singlenumdataResponse.setYardName(DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")));
						singlenumdataResponse.setTansId(rs.getString("ntrans_id"));
						singlenumdataResponse.setSlipNo(rs.getString("nslip_no"));
						singlenumdataResponse.setNtokenNo(rs.getString("ntoken_no"));
						singlenumdataResponse.setNyardNumId(rs.getString("nyard_id"));
						singlenumdataResponse.setNvehicleGroupId(rs.getString("nvehicle_group_id"));
						singlenumdataResponse.setSuccess(true);
					} else {
						singlenumdataResponse.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(ConstantMessage.informationNotFound);
						singlenumdataResponse.setSe(error);
					}
				}
			}

		} catch (Exception e) {
			singlenumdataResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			singlenumdataResponse.setSe(error);
			e.printStackTrace();
		}
		return singlenumdataResponse;
	}

	public SavePrintResponse numIndExclude(String transId, String yearId, String nslipNo, String nyardId,
			String nvehicleGroupId, long nreasonId, String chit_boy_id, SavePrintResponse numindexcludeResponse, Connection conn) {

		try {
			conn.setAutoCommit(false);
			int shiftId;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -4);
			int hoursOfDay = cal.get(Calendar.HOUR_OF_DAY);
			if (hoursOfDay < 8)
				shiftId = 1;
			else if (hoursOfDay < 16)
				shiftId = 2;
			else
				shiftId = 3;

			int newlot = 1;
			String sql = "SELECT min(t.nlot_no) as newlotno FROM APP_T_LOT_DETAILS t where t.nlot_no < 0 and t.vyear_id=? and t.nvehicle_group_id=? and t.nyard_id=?";
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, yearId);
				pst.setString(i++, nvehicleGroupId);
				pst.setString(i++, nyardId);

				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						newlot = rs.getInt("newlotno") - 1;
					}
				}
			}

			String reasonName = "";
			sql = "select t.nreason_id, To_NCHAR(t.vreason_name) vreason_name from APP_M_REASON t where t.nreason_group_id= 5 and t.nreason_id=?";
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setLong(i++, nreasonId);
				

				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						reasonName = DemoConvert2.ism_to_uni(rs.getString("vreason_name"));
					}
				}
			}
			int res = 0;
			
			try (PreparedStatement pst = conn.prepareStatement("Insert into APP_T_LOT_DETAILS (VYEAR_ID,DDATE,NSHIFT_ID,NYARD_ID,NVEHICLE_GROUP_ID,NLOT_NO,NCREATE_USER_ID, NREASON_ID) Values(?,sysdate,?,?,?,?,?,?)")) {
				int i = 1;
				pst.setString(i++, yearId);
				pst.setInt(i++, shiftId);
				pst.setString(i++, nyardId);
				pst.setString(i++, nvehicleGroupId);
				pst.setInt(i++, newlot);
				pst.setString(i++, chit_boy_id);
				pst.setLong(i++, nreasonId);
				res += pst.executeUpdate();
			}
		
			try (PreparedStatement pst = conn.prepareStatement("UPDATE APP_T_NUMBERTAKER t SET t.vactive_status=2, t.nlot_no =? WHERE t.vyear_id =? and t.ntrans_id =?")) {
				int i = 1;
				pst.setInt(i++, newlot);
				pst.setString(i++, yearId);
				pst.setString(i++, transId);
				res += pst.executeUpdate();
			}

			try (PreparedStatement pst = conn.prepareStatement("UPDATE " + ConstantVeriables.weightSlipTableName + " t SET t.nlot_no =? WHERE t.vyear_id =? and t.nslip_no in(" + nslipNo + ")")) {
				int i = 1;
				pst.setInt(i++, newlot);
				pst.setString(i++, yearId);
				res += pst.executeUpdate();
			}

			if (res >= 3) {
				conn.commit();
				numindexcludeResponse.setSuccess(true);
				numindexcludeResponse.setActionComplete(true);
				numindexcludeResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				
				//Print 
				
				ArrayList<String> transSelf = new ArrayList<>();
				try (PreparedStatement pst = conn.prepareStatement("select t.nentity_uni_id from APP_M_TRANS_SELF_OF t")) {
					
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							transSelf.add(rs.getString("nentity_uni_id"));
						}
					}
				}
				
				sql="select  t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate, To_NCHAR(cy.vyard_name_mar) vyard_name_mar, t.nlot_no, TO_NCHAR(gmm.ventity_name_local) as ventity_name_local, TO_NCHAR(vtm.vvehicle_type_name_local) as vvehicle_type_name_local, sysdate as throughpass_date from APP_T_NUMBERTAKER t, App_m_caneyard cy, gm_m_entity_master_detail gmm, Gm_m_Vehicle_Type_Master vtm where cy.nyard_id = t.nyard_id and gmm.nentity_uni_id = t.nentity_uni_id and gmm.nvehicle_type_id=vtm.nvehicle_type_id and t.vyear_id=? and t.ntrans_id=?";

				SimpleDateFormat sdfprint = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				
				String htmlContent = "<table>";
				/*htmlContent += "<tr>"
						+ "<td class='cbody txtcenter'><b>" + ConstantVeriables.lbltokennoShort + "</b></td>"
						+ "<td class='cbody txtcenter'><b>" + ConstantVeriables.lblVehicleNo + "</b></td>"
						+ "<td class='cbody txtcenter'><b>" + ConstantVeriables.lblentrytime+ "</b></td>"
						+ "</tr>";
				*/
				try(PreparedStatement pst=conn.prepareStatement(sql)){
					int i=1;
					pst.setString(i++, yearId);				
					pst.setString(i++, transId);	
					try (ResultSet rs = pst.executeQuery()) {
						while (rs.next()) {
							//String tokenno = rs.getString("ntoken_no");
							//String vvehicle_no = rs.getString("vvehicle_no");
							//String nentity_uni_id = rs.getString("nentity_uni_id");
							
							htmlContent += lineBreak;
							htmlContent += "<tr>" 
							        + "<td class='cbodysmall' >" + ConstantVeriables.lblsodaleledi + " : " + sdfprint.format(rs.getTimestamp("throughpass_date")) + "</td>"
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblYard + " : " + DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")) + "</td>" 
							        + "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblLotno + " : " + rs.getString("nlot_no") + "</td>" 
							        + "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblVehicleType + " : " +  DemoConvert2.ism_to_uni(rs.getString("vvehicle_type_name_local")) + "</td>"
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lbltokenno + " : " + rs.getString("ntoken_no") + "</td>" 
							        		+ "</tr> <tr>"							        
							        + "<td class='cbody'>" + ConstantVeriables.lblnonddi + " : " + sdfprint.format(rs.getTimestamp("ddate")) + "</td>" 
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblTranspoterName + " : " + DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")) + "</td>" 
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblVehicleNo + " : " + rs.getString("vvehicle_no") + "</td>"
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblreason + " : " + reasonName + "</td>"							       
									+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblSLipNo + " : " + rs.getString("nslip_no") + "</td>"
							        + "</tr>";
												
						}
					}
				}
				htmlContent += "</table>";
				//Calendar cal3 = Calendar.getInstance();
				//SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
				//String printHead = "दिनांक "+sdf3.format(cal3.getTime())+", लॉट नं. "+newlot;					
				numindexcludeResponse.setHtmlContent(htmlContent);
				numindexcludeResponse.setPrintHead(ConstantVeriables.throughPass);
			} else {
				conn.rollback();
				numindexcludeResponse.setSuccess(true);
				numindexcludeResponse.setActionComplete(false);
				numindexcludeResponse.setFailMsg(ConstantMessage.saveFailed);
			}

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			numindexcludeResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			numindexcludeResponse.setSe(error);
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return numindexcludeResponse;
	}

	public SingleNumDataResponse singleNumDataBlock(String type, String code, String vyearid, String vtype,
			String chit_boy_id, SingleNumDataResponse singlenumdataResponse, Connection conn) {	
		try {

			if (type.equalsIgnoreCase("Q")) {
				String slipNo = code.replace(ConstantVeriables.lblExtraQR, "");
				try (PreparedStatement pst = conn.prepareStatement("select t.ntransportor_id,t.nbulluckcart_id, t.rowid from "+ConstantVeriables.weightSlipTableName+" t where t.vyear_id=? and t.nslip_no=?")) {
					int i = 1;
					pst.setString(i++, vyearid);
					pst.setString(i++, slipNo);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							String transportorCode = rs.getString("ntransportor_id");
							String bulluckcartCode = rs.getString("nbulluckcart_id");
							if (transportorCode != null && !transportorCode.equalsIgnoreCase("")) {
								code = transportorCode;
								vtype = "T";
							} else if (bulluckcartCode != null && !bulluckcartCode.equalsIgnoreCase("")) {
								code = bulluckcartCode;
								vtype = "B";
							}
						}
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				
			HashMap<Integer, String> status = new HashMap<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.nstatus_id, t.vstatus_name_local from APP_M_VEHICLE_STATUS t")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						status.put(rs.getInt("nstatus_id"), DemoConvert2.ism_to_uni(rs.getString("vstatus_name_local")));
					}
				}
			}
			
			try (PreparedStatement pst = conn.prepareStatement("select t.nyard_id,t.nvehicle_group_id,t.ntrans_id,t.nslip_no,t.ntoken_no,em.ventity_name_local,t.ddate,amc.vyard_name_mar, t.vactive_status from app_t_numbertaker t,Gm_m_Entity_Master_Detail em,APP_M_CANEYARD amc where t.nentity_uni_id=em.nentity_uni_id and t.nyard_id=amc.nyard_id and  t.vyear_id=? and t.nentity_uni_id=? and t.vactive_status in (1,2,3,4)")) {
				int i = 1;
				pst.setString(i++, vyearid);
				pst.setString(i++, code);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						int statusId = rs.getInt("vactive_status");
						singlenumdataResponse.setName(DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
						singlenumdataResponse.setTime(sdf.format(rs.getTimestamp("ddate")));
						singlenumdataResponse.setYardName(DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")));
						singlenumdataResponse.setTansId(rs.getString("ntrans_id"));
						singlenumdataResponse.setSlipNo(rs.getString("nslip_no"));
						singlenumdataResponse.setNtokenNo(rs.getString("ntoken_no"));
						singlenumdataResponse.setNyardNumId(rs.getString("nyard_id"));
						singlenumdataResponse.setNvehicleGroupId(rs.getString("nvehicle_group_id"));
						singlenumdataResponse.setStatusId(String.valueOf(statusId));
						singlenumdataResponse.setStatusName(status.get(statusId));
						singlenumdataResponse.setSuccess(true);
					} else {
						singlenumdataResponse.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(ConstantMessage.informationNotFound);
						singlenumdataResponse.setSe(error);
					}
				}
			}

		} catch (Exception e) {
			singlenumdataResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			singlenumdataResponse.setSe(error);
			e.printStackTrace();
		}
		return singlenumdataResponse;
	}

	public ActionResponse stopNumber(String transId, String yearId, String nslipNo, String nyardId,
			String nvehicleGroupId, long nreasonId, String statusId, String chit_boy_id,
			ActionResponse numindexcludeResponse, Connection conn) {

		try {
			conn.setAutoCommit(false);
			/*int shiftId;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -4);
			int hoursOfDay = cal.get(Calendar.HOUR_OF_DAY);
			if (hoursOfDay < 8)
				shiftId = 1;
			else if (hoursOfDay < 16)
				shiftId = 2;
			else
				shiftId = 3;
*/

			int res = 0;
			if(statusId.equals("3")) {
				try (PreparedStatement pst = conn.prepareStatement("insert into APP_T_VEHICLE_HOLD(vyear_id, ntrans_id, nreason_id, nhold_user_id, dhold_date, vactive_status) values (?,?,?,?,sysdate,'A')")) {
					int i = 1;
					pst.setString(i++, yearId);
					pst.setString(i++, transId);
					pst.setLong(i++, nreasonId);
					pst.setString(i++, chit_boy_id);
					res += pst.executeUpdate();
				}
			} else {
				try (PreparedStatement pst = conn.prepareStatement("update APP_T_VEHICLE_HOLD set vactive_status='D', nrelease_user_id=?, drelease_date=sysdate where vyear_id = ? and ntrans_id=?")) {
					int i = 1;
					pst.setString(i++, chit_boy_id);
					pst.setString(i++, yearId);
					pst.setString(i++, transId);
					res += pst.executeUpdate();
				}
			}
		
			try (PreparedStatement pst = conn.prepareStatement("UPDATE APP_T_NUMBERTAKER t SET t.nlot_no = null, t.vactive_status=?  WHERE t.vyear_id =? and t.ntrans_id =?")) {
				int i = 1;
				pst.setString(i++, statusId);
				pst.setString(i++, yearId);
				pst.setString(i++, transId);
				res += pst.executeUpdate();
			}

			try (PreparedStatement pst = conn.prepareStatement("UPDATE " + ConstantVeriables.weightSlipTableName + " t SET t.nlot_no = null WHERE t.vyear_id =? and t.nslip_no in(" + nslipNo + ")")) {
				int i = 1;
				pst.setString(i++, yearId);
				res += pst.executeUpdate();
			}

			if (res >= 2) {
				conn.commit();
				numindexcludeResponse.setSuccess(true);
				numindexcludeResponse.setActionComplete(true);
				numindexcludeResponse.setSuccessMsg(ConstantMessage.saveSuccess);
			} else {
				conn.rollback();
				numindexcludeResponse.setSuccess(true);
				numindexcludeResponse.setActionComplete(false);
				numindexcludeResponse.setFailMsg(ConstantMessage.saveFailed);
			}

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			numindexcludeResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			numindexcludeResponse.setSe(error);
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return numindexcludeResponse;
	}

	public DataListResonse loadLot(String yearId, long nyardId, long nvehicleGroupId, String chit_boy_id,
			DataListResonse loadLotResponse, Connection conn) {
		try {
			ArrayList<KeyPairBoolData> lotList = new ArrayList<>();
			String sql = "select distinct t1.nlot_no from app_t_numbertaker t1 where t1.nlot_no>0 and t1.vyear_id=? and t1.nyard_id=? and t1.nvehicle_group_id=? and t1.vactive_status=4";
			String lotno = null;
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, yearId);
				pst.setLong(i++, nyardId);
				pst.setLong(i++, nvehicleGroupId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						if (lotno == null)
							lotno = "";
						else
							lotno += ",";
						lotno += rs.getString("nlot_no");
					}
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			sql = "select t.nlot_no,t.ddate from APP_T_LOT_DETAILS t where t.vyear_id=? and t.nyard_id=? and t.nvehicle_group_id=?  and t.nlot_no in(" + lotno + ")";
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, yearId);
				pst.setLong(i++, nyardId);
				pst.setLong(i++, nvehicleGroupId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						KeyPairBoolData dataList = new KeyPairBoolData();
						dataList.setId(rs.getLong("nlot_no"));
						dataList.setName(rs.getLong("nlot_no") + " -> " + sdf.format(rs.getTimestamp("ddate")));
						lotList.add(dataList);
					}
				}
			}
			if (lotList.size() > 0) {
				loadLotResponse.setDataList(lotList);
				loadLotResponse.setSuccess(true);

			} else {
				loadLotResponse.setSuccess(false);
				ServerError error = new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg(ConstantMessage.informationNotFound);
				loadLotResponse.setSe(error);
			}
		} catch (Exception e) {
			loadLotResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			loadLotResponse.setSe(error);
			e.printStackTrace();
		}
		return loadLotResponse;
	}

	public SavePrintResponse printLot(String yearId, long nyardId, long nvehicleGroupId, long lotno, String chit_boy_id,
			SavePrintResponse printlotResponse, Connection conn) {
		try {
			ArrayList<String> transSelf = new ArrayList<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.nentity_uni_id from APP_M_TRANS_SELF_OF t")) {

				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						transSelf.add(rs.getString("nentity_uni_id"));
					}
				}
			}
			SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
			String lotDate = "";
			String sql = "select t.ddate from APP_T_LOT_DETAILS t where t.vyear_id=? and t.nyard_id=? and t.nvehicle_group_id=? and t.nlot_no=? ";
			
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, yearId);
				pst.setLong(i++, nyardId);
				pst.setLong(i++, nvehicleGroupId);
				pst.setLong(i++, lotno);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						lotDate = sdf3.format(rs.getTimestamp("ddate"));
					}
				}
			}
			
			sql = "select t.ntrans_id, t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate,t.nshift_no,t.nvillage_id,emd.ventity_name_local from APP_T_NUMBERTAKER t, Gm_m_Entity_Master_Detail emd where t.nentity_uni_id=emd.nentity_uni_id and t.vyear_id=? and t.nyard_id=? and t.nvehicle_group_id=? and t.nlot_no=? and t.vactive_status=4 order by t.ddate, t.ntoken_no asc";
			SimpleDateFormat sdfprint = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

			String htmlContent = "<table width='100%'>";
			htmlContent += "<tr>" 
					+ "<td><b><span class='cbody' style='float:left;width: 40%;'>" + ConstantVeriables.lbltokenno + "</span> <span class='cbodysmall' style='text-align:right;float:right;width: 60%;'>" + ConstantVeriables.lblcodeAndName + "</span></b></td>"
					+ "</tr><tr>"
					+ "<td><b><span class='cbody' style='float:left;width: 40%;'>" + ConstantVeriables.lblVehicleNo + "</span> <span class='cbodysmall' style='text-align:right;float:right;width:60%;'>" + ConstantVeriables.lblentrytime + "</span></b></td>" 
					+ "</tr><tr>"
					+ "<td class='cbody'><b>" + ConstantVeriables.lblSLipNo + "</b></td>"
					+ "</tr>";
			htmlContent += lineBreak;
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, yearId);
				pst.setLong(i++, nyardId);
				pst.setLong(i++, nvehicleGroupId);
				pst.setLong(i++, lotno);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String tokenno = rs.getString("ntoken_no");
						String vvehicle_no = rs.getString("vvehicle_no");
						String nentity_uni_id = rs.getString("nentity_uni_id");
						String name = DemoConvert2.ism_to_uni(rs.getString("ventity_name_local"));
						String slipNo = rs.getString("nslip_no");
						
						htmlContent += "<tr>" 
								+ "<td><span class='cbody' style='float:left;width: 15%;'>" + tokenno + "</span> <span class='cbodysmall' style='text-align:right;float:right;width: 85%;'>" + nentity_uni_id + " " + name + "</span></td>"
								+ "</tr><tr>"
								+ "<td><span class='cbody' style='float:left;width: 42%;'>" + vvehicle_no + "</span> <span class='cbodysmall' style='text-align:right;float:right;width: 58%;'>" + sdfprint.format(rs.getTimestamp("ddate")) + "</span></td>"
								+ "</tr><tr>"
								+ "<td class='cbody'>" + slipNo + "</td>" 
								+ "</tr>";
						htmlContent += lineBreak;
					}
				}
			}
			htmlContent += "</table>";
			//Calendar cal3 = Calendar.getInstance();
			//SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
			String printHead = "दिनांक " + lotDate + ", लॉट नं. " + lotno;
			printlotResponse.setHtmlContent(htmlContent);
			printlotResponse.setPrintHead(printHead);
			printlotResponse.setActionComplete(true);

		} catch (Exception e) {
			printlotResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			printlotResponse.setSe(error);
			e.printStackTrace();
		}
		return printlotResponse;

	}

	public TableResponse vehicleRegister(String dateVal, String yardId, String shiftId, String yearId, String vehicleStatus, String chit_boy_id,
			TableResponse vehicleregisterResponse, Connection conn) {

		try {
		
			/*int shiftId=0;
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, -4);
			
			int hoursOfDay = cal.get(Calendar.HOUR_OF_DAY);
			if (hoursOfDay < 8)
				shiftId = 1;
			else if (hoursOfDay < 16)
				shiftId = 2;
			else
				shiftId = 3;*/
				
			TableReportBean lotlistTableBean = new TableReportBean();
			int noofheadDailyCrush =1;
			lotlistTableBean.setNoofHeads(noofheadDailyCrush);
			lotlistTableBean.setFooter(false);
			lotlistTableBean.setMarathi(false);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> varietyTonnageTableData = new ArrayList<>();
			ArrayList<String> lotListRow = new ArrayList<>();
			lotListRow.add(ConstantVeriables.lblSrno);
			lotListRow.add(ConstantVeriables.lbltokenno);
			lotListRow.add(ConstantVeriables.lblSLipNo);
			lotListRow.add(ConstantVeriables.lblTranspoterName);
			lotListRow.add(ConstantVeriables.lblVehicleNo);
			lotListRow.add(ConstantVeriables.lblDate);
			lotListRow.add(ConstantVeriables.lblshift);
			lotListRow.add(ConstantVeriables.lblVilleage);
			lotListRow.add(ConstantVeriables.lblLotno);
			lotListRow.add(ConstantVeriables.lblUserName);
			lotListRow.add(ConstantVeriables.status);
			varietyTonnageTableData.add(lotListRow);
			
			boldIndicator.put("0-*", true);
			HashMap<Integer, String> staus = new HashMap<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.nstatus_id, TO_NCHAR(t.vstatus_name_local) vstatus_name_local from APP_M_VEHICLE_STATUS t")) {
				
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						staus.put(rs.getInt("nstatus_id"), DemoConvert2.ism_to_uni(rs.getString("vstatus_name_local")));
					}
				}
			}

			String shiftfilter = "";
			if(!shiftId.equals("0")) {
				shiftfilter = " and t.nshift_no=? ";
			}
			String sql = "";
			if (!vehicleStatus.equals("2") && !vehicleStatus.equals("3")) {
				sql = "select t.nvehicle_group_id, t.vactive_status, t.nlot_no, t.ntrans_id, t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate,t.nshift_no,t.nvillage_id,emd.ventity_name_local,vm.village_name_local, u.nuser_name, TO_NCHAR(u.vfull_name_local) as vfullname from APP_T_NUMBERTAKER t, Gm_m_Entity_Master_Detail emd,GM_M_VILLAGE_MASTER vm, App_m_User_Master u where u.nuser_name=t.ncreate_user and t.nentity_uni_id=emd.nentity_uni_id and t.nvillage_id=vm.nvillage_id and t.vyear_id=? and t.nyard_id=? and t.dcrush_date=To_date(?,'dd/mm/yyyy')";
				if (!vehicleStatus.equals("-1"))
					sql += " and t.vactive_status=? ";
				sql += shiftfilter + " order by t.nvehicle_group_id,t.ntoken_no asc";
			} else if(vehicleStatus.equals("2")) 
				sql = "select t.nvehicle_group_id, t.vactive_status, t.nlot_no, t.ntrans_id, t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate,t.nshift_no,t.nvillage_id,emd.ventity_name_local,vm.village_name_local, u.nuser_name, TO_NCHAR(u.vfull_name_local) as vfullname from APP_T_NUMBERTAKER t, Gm_m_Entity_Master_Detail emd,GM_M_VILLAGE_MASTER vm, App_m_User_Master u, App_t_lot_details d where d.vyear_id=t.vyear_id and d.nyard_id=t.nyard_id and d.nlot_no=t.nlot_no and t.nlot_no<0 and u.nuser_name=t.ncreate_user and t.nentity_uni_id=emd.nentity_uni_id and t.nvillage_id=vm.nvillage_id and t.vyear_id=? and t.nyard_id=? and t.dcrush_date=To_date(?,'dd/mm/yyyy') " + shiftfilter + " order by t.nvehicle_group_id,t.ntoken_no asc";
			else if (vehicleStatus.equals("3"))
				sql = "select t.nvehicle_group_id, t.vactive_status, t.nlot_no, t.ntrans_id, t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate,t.nshift_no,t.nvillage_id,emd.ventity_name_local,vm.village_name_local, u.nuser_name, TO_NCHAR(u.vfull_name_local) as vfullname from APP_T_NUMBERTAKER t, Gm_m_Entity_Master_Detail emd,GM_M_VILLAGE_MASTER vm, App_m_User_Master u, App_t_Vehicle_hold vh where vh.vyear_id=t.vyear_id and vh.ntrans_id=t.ntrans_id and u.nuser_name=t.ncreate_user and t.nentity_uni_id=emd.nentity_uni_id and t.nvillage_id=vm.nvillage_id and t.vyear_id=? and t.nyard_id=? and t.dcrush_date=To_date(?,'dd/mm/yyyy') " + shiftfilter + " order by t.nvehicle_group_id,t.ntoken_no asc";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String shift[] = new String[] { "", "4-12", "12-8", "8-4" };
			
			
			try(PreparedStatement pst=conn.prepareStatement(sql)){
				int i=1;
				pst.setString(i++, yearId);				
				pst.setString(i++, yardId);
				pst.setString(i++, dateVal);
				if (!vehicleStatus.equals("2") && !vehicleStatus.equals("3") && !vehicleStatus.equals("-1")) 
					pst.setString(i++, vehicleStatus);
				if(!shiftId.equals("0")) 
					pst.setString(i++, shiftId);				
				int previousVGId = 0, newVGId = 0;
				int srno = 1;
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						newVGId = rs.getInt("nvehicle_group_id");
						if (newVGId != previousVGId) 
							srno = 1;
						previousVGId = newVGId;
						String tokenno = rs.getString("ntoken_no");
						String vvehicle_no = rs.getString("vvehicle_no");
						String nentity_uni_id = rs.getString("nentity_uni_id");
				
						lotListRow = new ArrayList<>();
						lotListRow.add(String.valueOf(srno++));
						lotListRow.add(tokenno);
						lotListRow.add(rs.getString("nslip_no"));
						lotListRow.add(nentity_uni_id + " " + DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
						lotListRow.add(vvehicle_no);
						lotListRow.add(sdf.format(rs.getTimestamp("ddate")));
						lotListRow.add(shift[rs.getInt("nshift_no")]);
						lotListRow.add(DemoConvert2.ism_to_uni(rs.getString("village_name_local")));
						lotListRow.add(rs.getString("nlot_no"));
						lotListRow.add(rs.getString("nuser_name") + " " + DemoConvert2.ism_to_uni(rs.getString("vfullname")));
						lotListRow.add(staus.getOrDefault(rs.getInt("vactive_status"), "N.A."));

						varietyTonnageTableData.add(lotListRow);
					}
				}
			}
			
			lotlistTableBean.setTableData(varietyTonnageTableData);
			lotlistTableBean.setColWidth(new Integer[]{5,5,12,13,12,13,5,10,5,10,10});	
			vehicleregisterResponse.setTableData(lotlistTableBean);
			return vehicleregisterResponse;
			
			
		} catch (Exception e) {
			
			vehicleregisterResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg(" List Error " + e.getMessage());
			vehicleregisterResponse.setSe(error);
			e.printStackTrace();
		}
		
		return vehicleregisterResponse;
	
	}

	public NumIndResponse numWaiting(String nyearId, String type, String code, String vtype, String chit_boy_id,
			NumIndResponse numwaitingResponse, Connection conn) {	
		try {

			if (type.equalsIgnoreCase("Q")) {
				String slipNo = code.replace(ConstantVeriables.lblExtraQR, "");
				try (PreparedStatement pst = conn.prepareStatement("select t.ntransportor_id,t.nbulluckcart_id, t.rowid from "+ConstantVeriables.weightSlipTableName+" t where t.vyear_id=? and t.nslip_no=?")) {
					int i = 1;
					pst.setString(i++, nyearId);
					pst.setString(i++, slipNo);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							String transportorCode = rs.getString("ntransportor_id");
							String bulluckcartCode = rs.getString("nbulluckcart_id");
							if (transportorCode != null && !transportorCode.equalsIgnoreCase("")) {
								code = transportorCode;
								vtype = "T";
							} else if (bulluckcartCode != null && !bulluckcartCode.equalsIgnoreCase("")) {
								code = bulluckcartCode;
								vtype = "B";
							}
						}
					}
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			int vactiveStatus=0,nvehicleGroupId=0, nlotId=0, ntransId = 0;
			
			try (PreparedStatement pst = conn.prepareStatement("select t.ntrans_id, t.nslip_no, t.nvehicle_group_id, t.nlot_no,t.nyard_id,t.ntoken_no, t.nentity_uni_id,TO_NCHAR(em.ventity_name_local) ventity_name_local,t.ddate, t.vvehicle_no,t.vphoto,  t.vactive_status  from app_t_numbertaker t,Gm_m_Entity_Master_Detail em where t.nentity_uni_id=em.nentity_uni_id and t.vyear_id=? and t.nentity_uni_id=? and t.vactive_status <= 4")) {
				int i = 1;
				pst.setString(i++, nyearId);
				pst.setString(i++, code);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						numwaitingResponse.setTransId(rs.getString("ntrans_id"));
						numwaitingResponse.setSlipNo(rs.getString("nslip_no"));
						numwaitingResponse.setTransName(rs.getString("nentity_uni_id") + " " + DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")));
						numwaitingResponse.setVvehicleNo(rs.getString("vvehicle_no"));
						numwaitingResponse.setTokenDate(sdf.format(rs.getTimestamp("ddate")));
						numwaitingResponse.setYardId(rs.getString("nyard_id"));
						numwaitingResponse.setTokenNo(rs.getString("ntoken_no"));
						numwaitingResponse.setPhotopath(rs.getString("vphoto"));
						vactiveStatus = rs.getInt("vactive_status");
						nvehicleGroupId = rs.getInt("nvehicle_group_id");
						nlotId = rs.getInt("nlot_no");
						ntransId = rs.getInt("ntrans_id");
						numwaitingResponse.setSuccess(true);
					} else {
						numwaitingResponse.setSuccess(false);
						ServerError error = new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(ConstantMessage.informationNotFound);
						numwaitingResponse.setSe(error);
					}
				}
			}

			if (numwaitingResponse.isSuccess()) {
				if (vactiveStatus == 1) {
					try (PreparedStatement pst = conn.prepareStatement("select nvl(count(t.ntrans_id),0) as waiting from app_t_numbertaker t where t.vyear_id=? and t.nyard_id=? and t.vactive_status = 1 and t.ddate<TO_DATE(?, 'dd-Mon-yyyy hh24:mi:ss')")) {
						int i = 1;
						pst.setString(i++, nyearId);
						pst.setString(i++, numwaitingResponse.getYardId());
						pst.setString(i++, numwaitingResponse.getTokenDate());
						try (ResultSet rs = pst.executeQuery()) {
							if (rs.next()) {
								numwaitingResponse.setTotalWaiting(rs.getString("waiting"));
							} else {
								numwaitingResponse.setTotalWaiting("0");
							}
						}
					}
				} else {
					try (PreparedStatement pst = conn.prepareStatement("select TO_NCHAR(t.vstatus_name_local) vstatus_name_local from APP_M_VEHICLE_STATUS t where t.nstatus_id = ?")) {
						int i = 1;
						pst.setInt(i++, vactiveStatus);
						try (ResultSet rs = pst.executeQuery()) {
							if (rs.next()) {
								String extra = "";
								if(vactiveStatus == 2 || vactiveStatus == 4) {
									extra = " (" + nlotId + ")";
								}
								numwaitingResponse.setTotalWaiting("स्थिती : " + DemoConvert2.ism_to_uni(rs.getString("vstatus_name_local")) + extra);
							} else {
								numwaitingResponse.setTotalWaiting("0");
							}
						}
					}
					
					int reasonId =0;
					if(vactiveStatus==2 || vactiveStatus==4) {
						try (PreparedStatement pst = conn.prepareStatement("select t.nreason_id, ddate from APP_T_LOT_DETAILS t where t.vyear_id=? and t.nvehicle_group_id=? and t.nlot_no=? ")) {
							int i = 1;
							pst.setString(i++, nyearId);
							pst.setInt(i++, nvehicleGroupId);
							pst.setInt(i++, nlotId);
							try (ResultSet rs = pst.executeQuery()) {
								if (rs.next()) {
									numwaitingResponse.setTotalWaiting(numwaitingResponse.getTotalWaiting() + "\nवेळ : " + sdf.format(rs.getTimestamp("ddate")) + "");
									reasonId = rs.getInt("nreason_id");
								} 
							}
						}
					} else if(vactiveStatus==3) {
						try (PreparedStatement pst = conn.prepareStatement("select t.nreason_id, dhold_date from APP_t_vehicle_hold t where t.vyear_id=? and t.ntrans_id=? ")) {
							int i = 1;
							pst.setString(i++, nyearId);
							pst.setInt(i++, ntransId);
							try (ResultSet rs = pst.executeQuery()) {
								if (rs.next()) {
									numwaitingResponse.setTotalWaiting(numwaitingResponse.getTotalWaiting() + "\nवेळ : " + sdf.format(rs.getTimestamp("dhold_date")) + "");
									reasonId = rs.getInt("nreason_id");
								} 
							}
						}
					}
					
					if(reasonId!=0) {
						try (PreparedStatement pst = conn.prepareStatement("select TO_NCHAR(t.vreason_name) vreason_name from App_m_Reason t where t.nreason_id=?")) {
							int i = 1;
							pst.setInt(i++, reasonId);
							try (ResultSet rs = pst.executeQuery()) {
								if (rs.next()) {
									numwaitingResponse.setTotalWaiting(numwaitingResponse.getTotalWaiting() + "\nकारण : " + DemoConvert2.ism_to_uni(rs.getString("vreason_name")));
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			numwaitingResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			numwaitingResponse.setSe(error);
			e.printStackTrace();
		}
		return numwaitingResponse;
	}

	public SavePrintResponse printTokenPass(String printtype, String yearId, String vtype, String code,
			String chit_boy_id, SavePrintResponse printtokenpassResponse, Connection conn) {
		try {
				//Print 
			String siftname[] = { "", "4-12", "12-8", "8-4" };
			String sql = null;
	
			if (printtype.equalsIgnoreCase("P"))
				sql = "select  t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate, To_NCHAR(cy.vyard_name_mar) vyard_name_mar,To_NCHAR(rm.vreason_name) vreason_name, t.nlot_no, TO_NCHAR(gmm.ventity_name_local) as ventity_name_local, TO_NCHAR(vtm.vvehicle_type_name_local) as vvehicle_type_name_local, ld.ddate as throughpass_date from APP_T_NUMBERTAKER t, App_m_caneyard cy, gm_m_entity_master_detail gmm, Gm_m_Vehicle_Type_Master vtm, App_t_lot_details ld, App_m_Reason rm where cy.nyard_id = t.nyard_id and gmm.nentity_uni_id = t.nentity_uni_id and ld.vyear_id=t.vyear_id and ld.nyard_id=t.nyard_id and ld.nlot_no=t.nlot_no and ld.nreason_id=rm.nreason_id and gmm.nvehicle_type_id=vtm.nvehicle_type_id and t.vactive_status=2 and t.vyear_id=? and t.nentity_uni_id=? ";
			else
				sql = "select  t.nshift_no,t.nslip_no, t.ntoken_no,t.nentity_uni_id,t.vvehicle_no,t.ddate, To_NCHAR(cy.vyard_name_mar) vyard_name_mar, TO_NCHAR(gmm.ventity_name_local) as ventity_name_local,To_NCHAR(vm.village_name_local) as villagename from APP_T_NUMBERTAKER t, App_m_caneyard cy, gm_m_entity_master_detail gmm,gm_m_village_master vm where cy.nyard_id = t.nyard_id and gmm.nentity_uni_id = t.nentity_uni_id  and t.nvillage_id=vm.nvillage_id and t.vactive_status<=4 and t.vyear_id=? and t.nentity_uni_id=? ";
				
				
				String htmlContent = "<table>";
				/*htmlContent += "<tr>"
						+ "<td class='cbody txtcenter'><b>" + ConstantVeriables.lbltokennoShort + "</b></td>"
						+ "<td class='cbody txtcenter'><b>" + ConstantVeriables.lblVehicleNo + "</b></td>"
						+ "<td class='cbody txtcenter'><b>" + ConstantVeriables.lblentrytime+ "</b></td>"
						+ "</tr>";
				*/
				try(PreparedStatement pst=conn.prepareStatement(sql)){
					int i=1;
					pst.setString(i++, yearId);				
					pst.setString(i++, code);	
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							//String tokenno = rs.getString("ntoken_no");
							//String vvehicle_no = rs.getString("vvehicle_no");
							//String nentity_uni_id = rs.getString("nentity_uni_id");

						if (printtype.equalsIgnoreCase("P")) {
							SimpleDateFormat sdfprint = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
							htmlContent += lineBreak;
							htmlContent += "<tr>" 
							        + "<td class='cbodysmall' >" + ConstantVeriables.lblsodaleledi + " : " + sdfprint.format(rs.getTimestamp("throughpass_date")) + "</td>"
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblYard + " : " + DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")) + "</td>" 
							        + "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblLotno + " : " + rs.getString("nlot_no") + "</td>" 
							        + "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblVehicleType + " : " +  DemoConvert2.ism_to_uni(rs.getString("vvehicle_type_name_local")) + "</td>"
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lbltokenno + " : " + rs.getString("ntoken_no") + "</td>" 
							        		+ "</tr> <tr>"							        
							        + "<td class='cbody'>" + ConstantVeriables.lblnonddi + " : " + sdfprint.format(rs.getTimestamp("ddate")) + "</td>" 
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblTranspoterName + " : " + rs.getString("nentity_uni_id") + " " + DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")) + "</td>" 
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblVehicleNo + " : " + rs.getString("vvehicle_no") + "</td>"
							        		+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblreason + " : " + DemoConvert2.ism_to_uni(rs.getString("vreason_name")) + "</td>"							       
									+ "</tr> <tr>"
							        + "<td class='cbody'>" + ConstantVeriables.lblSLipNo + " : " + rs.getString("nslip_no") + "</td>"
							        + "</tr>";
							printtokenpassResponse.setPrintHead(ConstantVeriables.throughPass);

						} else if (printtype.equalsIgnoreCase("T")) {
							SimpleDateFormat sdfdate = new SimpleDateFormat("dd-MMM-yyyy");
							SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lblSlipNoShort + "</td>"
										+ "<td class='cbody'> : " + rs.getString("nslip_no")  + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lblDate + "</td>"
										+ "<td class='cbody'> : " + sdfdate.format(rs.getTimestamp("ddate"))  + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lbltime + " : " + "</td>"
										+ "<td class='cbody'> : " +  sdfTime.format(rs.getTimestamp("ddate")) + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lblshift + "</td>"
										+ "<td class='cbody'> : " + siftname[rs.getInt("nshift_no")] + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lblYard + "</td>"
										+ "<td class='cbody'> : " + DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")) + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lbltokenno + "</td>"
										+ "<td class='cbody'> : " + rs.getString("ntoken_no") + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbodysmall'>" + ConstantVeriables.lblTranspoterName + "</td>"
										+ "<td class='cbodysmall'> : " + rs.getString("nentity_uni_id") + " " + DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")) + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lblVehicleNoShort + "</td>"
										+ "<td class='cbody'> : " + rs.getString("vvehicle_no") + "</td>"
										+ "</tr>";
								htmlContent += "<tr>"
										+ "<td class='cbody'>" + ConstantVeriables.lblVilleage + "</td>"
										+ "<td class='cbody'> : " + DemoConvert2.ism_to_uni(rs.getString("villagename")) + "</td>"
										+ "</tr>";
								printtokenpassResponse.setPrintHead(ConstantVeriables.reprinttoken);
						}
						htmlContent += "</table>";
						printtokenpassResponse.setHtmlContent(htmlContent);
						printtokenpassResponse.setSuccess(true);
						printtokenpassResponse.setActionComplete(true);
						printtokenpassResponse.setSuccessMsg(ConstantMessage.saveSuccess);

					} else {
						printtokenpassResponse.setSuccess(true);
						printtokenpassResponse.setActionComplete(false);
						printtokenpassResponse.setFailMsg(ConstantMessage.informationNotFound);
					}
				}
			}			

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			printtokenpassResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			printtokenpassResponse.setSe(error);
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return printtokenpassResponse;
	
	}

	public DataListResonse vehicleStatus(DataListResonse vehicleStatusResponse, Connection conn) {
		try {
			ArrayList<KeyPairBoolData> statusList = new ArrayList<>();
			KeyPairBoolData dataList = new KeyPairBoolData();
			dataList.setId(-1);
			dataList.setName("All");
			dataList.setSelected(true);
			statusList.add(dataList);
			String sql = "select t.nstatus_id, TO_NCHAR(t.vstatus_name_local) as vstatus_name_local from APP_M_VEHICLE_STATUS t where t.nflag_rpt=1";
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						dataList = new KeyPairBoolData();
						dataList.setId(rs.getLong("nstatus_id"));
						dataList.setName(DemoConvert2.ism_to_uni(rs.getString("vstatus_name_local")));
						statusList.add(dataList);
					}
				}
			}

			vehicleStatusResponse.setDataList(statusList);
			vehicleStatusResponse.setSuccess(true);
		} catch (Exception e) {
			vehicleStatusResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			vehicleStatusResponse.setSe(error);
			e.printStackTrace();
		}
		return vehicleStatusResponse;
	}
	
	public UserRoleResponse userRoleInfo(String empcode, UserRoleResponse userRoleResponse, Connection conn) {
		try {
			try (PreparedStatement pst = conn.prepareStatement("select TO_NCHAR(t.vfull_name_local) as vfull_name_local, nuser_role_id from App_m_User_Master t where t.nuser_name=?")) {
				int i = 1;
				pst.setString(i++, empcode);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						userRoleResponse.setUserId(empcode);
						userRoleResponse.setUserName(DemoConvert2.ism_to_uni(rs.getString("vfull_name_local")));
						userRoleResponse.setCurrentRoleId(rs.getString("nuser_role_id"));
					}
				}
			}
			
			ArrayList<KeyPairBoolData> userRoleList = new ArrayList<>();
			try (PreparedStatement pst = conn.prepareStatement("select TO_NCHAR(t.vuser_role) vuser_role from App_m_User_Role_Master t where t.nuser_role_id = ?")) {
				int i = 1;
				pst.setString(i++, userRoleResponse.getCurrentRoleId());
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						String name = DemoConvert2.ism_to_uni(rs.getString("vuser_role"));
						userRoleResponse.setCurrentRoleName(userRoleResponse.getCurrentRoleId() + " " + name);
					}
				}
			}
		} catch (Exception e) {
			userRoleResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			userRoleResponse.setSe(error);
			e.printStackTrace();
		}
		return userRoleResponse;
	}

	public ActionResponse updateRoleEmp(String empcode, String roleid, ActionResponse updateRoleResponse,
			Connection conn) {
		try {
			try (PreparedStatement pst = conn.prepareStatement("UPDATE App_m_User_Master t SET t.nuser_role_id =? WHERE nuser_name=?")) {
				int i=1;
				pst.setString(i++, roleid);
				pst.setString(i++, empcode);
				int res=pst.executeUpdate();
				updateRoleResponse.setSuccess(true);
				if (res > 0) {
					updateRoleResponse.setActionComplete(true);
					updateRoleResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				} else {
					updateRoleResponse.setActionComplete(false);
					updateRoleResponse.setSuccessMsg(ConstantMessage.saveFailed);
				}
			}
		} catch (SQLException e) {
			updateRoleResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Request Platation From " + e.getMessage());
			updateRoleResponse.setSe(error);
			e.printStackTrace();
		}
		return updateRoleResponse;
	}

	public ActionResponse cancelNumber(String transId, String nslipNo, String nreasonId, String yearId,
			String chit_boy_id, ActionResponse updateRoleResponse, Connection conn) {
		try {
			int res=0;
			Timestamp voutTime = null; 
			conn.setAutoCommit(false);
			try (PreparedStatement pst = conn.prepareStatement("select max(t.vout_time) vout_time from wb_t_weight_slip t where t.vyear_id=? and t.nslip_no in (" + nslipNo + ") and t.nnet_weight>0 ")) {
				int i = 1;
				pst.setString(i++, yearId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						voutTime = rs.getTimestamp("vout_time");
					}
				}
			}
			
			try (PreparedStatement pst = conn.prepareStatement("UPDATE App_t_numbertaker t SET t.vactive_status = 6, t.ddate_unload=?, t.nreason_cancel=?, t.ncancel_user_id=?, t.dcancel_date=sysdate WHERE t.vyear_id = ? and t.ntrans_id=?")) {
				int i=1;
				pst.setTimestamp(i++, voutTime);
				pst.setString(i++, nreasonId);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, yearId);
				pst.setString(i++, transId);
				res+=pst.executeUpdate();
			}
			
			try (PreparedStatement pst = conn.prepareStatement("UPDATE Wb_t_Weight_Slip t SET t.ntoken_no = null, t.nlot_no = null WHERE t.vyear_id = ? and t.nslip_no in (" + nslipNo + ")")) {
				int i=1;
				pst.setString(i++, yearId);
				res+=pst.executeUpdate();
			}
			
			updateRoleResponse.setSuccess(true);
			if (res >= 2) {
				conn.commit();
				updateRoleResponse.setActionComplete(true);
				updateRoleResponse.setSuccessMsg(ConstantMessage.saveSuccess);
			} else {
				conn.rollback();
				updateRoleResponse.setActionComplete(false);
				updateRoleResponse.setSuccessMsg(ConstantMessage.saveFailed);
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateRoleResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Request Platation From " + e.getMessage());
			updateRoleResponse.setSe(error);
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return updateRoleResponse;
	}

	public DataTwoListResonse roleUser(DataTwoListResonse dataTwoList, Connection conn) {
		try {
			ArrayList<KeyPairBoolData> userRoleList = new ArrayList<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.nuser_role_id, TO_NCHAR(t.vuser_role) vuser_role from App_m_User_Role_Master t where t.nuser_role_id not in (105, 111, 112, 113)")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						Long userRoleId = rs.getLong("nuser_role_id");
						String name = DemoConvert2.ism_to_uni(rs.getString("vuser_role"));
						KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
						keyPairBoolData.setId(userRoleId);
						keyPairBoolData.setName(userRoleId + " " + name);
						userRoleList.add(keyPairBoolData);
						
					}
				}
			}
			dataTwoList.setDataList(userRoleList);
			ArrayList<KeyPairBoolData> userList = new ArrayList<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.nuser_name, TO_NCHAR(t.vfull_name_local) vfull_name_local from App_m_User_Master t where t.nuser_role_id in (115, 117)")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						Long userRoleId = rs.getLong("nuser_name");
						String name = DemoConvert2.ism_to_uni(rs.getString("vfull_name_local"));
						KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
						keyPairBoolData.setId(userRoleId);
						keyPairBoolData.setName(userRoleId + " " + name);
						userList.add(keyPairBoolData);
					}
				}
			}
			dataTwoList.setDataListTwo(userList);
		} catch (Exception e) {
			dataTwoList.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			dataTwoList.setSe(error);
			e.printStackTrace();
		}
		return dataTwoList;
	}

	public CaneYardBalanceResponse summaryReport(CaneYardBalanceResponse summaryreportResponse, String chit_boy_id,
			Connection conn) {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdfdt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
			String yearcode = Constant.getVyearcode(cal.getTime(), Constant.wsstartdate, Constant.wsenddate);
			ArrayList<ArrayList<String>> inyardTableData = new ArrayList<>();
			ArrayList<ArrayList<String>> outyardTableData = new ArrayList<>();
			ArrayList<ArrayList<String>> emptyTableData = new ArrayList<>();
			
			HashMap<Integer, Integer> seqAll = new HashMap();
			HashMap<Integer, Integer> yardPos = new HashMap();
			ArrayList<String> head = new ArrayList<>();
			ArrayList<String> head1 = new ArrayList<>();
			ArrayList<String> head2 = new ArrayList<>();
			ArrayList<String> head3 = new ArrayList<>();
			
			head.add(ConstantVeriables.lblSrno);
			head.add(ConstantVeriables.lblYard);
			
			head1.add(ConstantVeriables.lblSrno);
			head1.add(ConstantVeriables.details);
			
			head2.add("");
			head2.add("");
			
			head3.add("");
			head3.add("");
			
			
			
			HashMap<String, Integer> rowColSpan = new HashMap<>();
			
			
			try (PreparedStatement pst1 = conn.prepareStatement("select t.nvehicle_type_id,TO_NCHAR(t.vvehicle_type_name_local)as vehicle_type  from gm_m_vehicle_type_master t ")) {
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						String vehicle_type = DemoConvert2.ism_to_uni(rs.getString("vehicle_type"));
						head.add(vehicle_type);
						head1.add(vehicle_type);
						int pos = head.size()-1;
						seqAll.put(rs.getInt("nvehicle_type_id"), pos);
						
						head2.add(ConstantVeriables.lblCount);
						head2.add(ConstantVeriables.expTonnage);
						
						head3.add(ConstantVeriables.lblCount);
						head3.add(ConstantVeriables.tonnage);
						
						rowColSpan.put("0-" + pos, 2);
					}
				}
			}
			head.add(ConstantVeriables.total);
			head1.add(ConstantVeriables.total);
			
			head2.add(ConstantVeriables.lblCount);
			head2.add(ConstantVeriables.expTonnage);
			
			head3.add(ConstantVeriables.lblCount);
			head3.add(ConstantVeriables.tonnage);
			
			int totalelement  = head.size();
			int rowtotalpos = totalelement-1;
			rowColSpan.put("0-" + rowtotalpos, 2);
			inyardTableData.add(head);
			inyardTableData.add(head2);
			
			outyardTableData.add(head1);
			outyardTableData.add(head2);
			
			emptyTableData.add(head1);
			emptyTableData.add(head3);
			
			int srno = 1;
			try (PreparedStatement pst1 = conn.prepareStatement("select t.nyard_id, TO_NCHAR(t.vyard_name_mar) as vyard_name_mar from App_m_caneyard t")) {
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						yardPos.put(rs.getInt("nyard_id"), inyardTableData.size());
						ArrayList<String> dummyData = new ArrayList<>();
						dummyData.add(String.valueOf(srno++));
						dummyData.add(DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")));
						for (int i = 2; i < totalelement; i++) {
							dummyData.add("0");
							dummyData.add("0.000");
						}
						inyardTableData.add(dummyData);
					}
				}
			}
			
			ArrayList<String> dummyData = new ArrayList<>();
			String tempArr[] = new String[] { ConstantVeriables.outsidewt, ConstantVeriables.onwt, ConstantVeriables.total };
			int size = tempArr.length;
			for (int i = 0; i < size; i++) {
				dummyData = new ArrayList<>();
				if (size - 1 == i)
					dummyData.add("");
				else
					dummyData.add(String.valueOf(inyardTableData.size()));
				dummyData.add(tempArr[i]);
				for (int j = 2; j < totalelement; j++) {
					dummyData.add("0");
					dummyData.add("0.000");
				}
				inyardTableData.add(dummyData);
			}	
			
			int totrowspos = inyardTableData.size() - 1;
		
			//number taker
			DecimalFormat dfTon = new DecimalFormat("#0.000");
			String sql="select w.nvehicle_type_id, t.nyard_id,count(distinct t.ntrans_id) as yardvehicle, sum(v.nexp_harv_tonnage) as ton from app_t_numbertaker t, Wb_t_Weight_Slip w, APP_M_EXPECTED_TON_VEHICLE v where t.vyear_id=w.vyear_id and t.ntoken_no = w.ntoken_no and nvl(t.nlot_no,0) = nvl(w.nlot_no,0) and (t.nentity_uni_id = w.ntransportor_id or t.nentity_uni_id=w.nbulluckcart_id) and w.nvehicle_type_id = v.nvehicle_type_id and nvl(w.nwirerope_no,0)=nvl(v.nwirerope_no,0) and nvl(w.ntailor_front,0) = nvl(v.ntailor_front,0) and nvl(w.ntailor_back,0) = nvl(v.ntailor_back,0) and w.ngross_weight is null and t.vyear_id=? and t.vactive_status in (1,3) and w.vactive_dactive='A' group by w.nvehicle_type_id, t.nyard_id";
			try (PreparedStatement pst1 = conn.prepareStatement(sql)) { // and t.ngross_weight is null
				int i = 1;
				pst1.setString(i++, yearcode);
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						int nvehicle_type = rs.getInt("nvehicle_type_id");
						int nyard_id = rs.getInt("nyard_id");
						int pos = seqAll.get(nvehicle_type);
						
						int yardCurPos = yardPos.get(nyard_id);
						int countpos = (pos - 1) * 2;
						int tonpos = countpos + 1;
						
						int totalCountPos = (rowtotalpos - 1) * 2;
						int totalTonPos = (totalCountPos + 1);
						
						ArrayList<String> data  = inyardTableData.get(yardCurPos);
						
						int yardvehicle = rs.getInt("yardvehicle");
						double expTon = rs.getDouble("ton");
						data.set(countpos, String.valueOf(yardvehicle));
						data.set(tonpos, dfTon.format(expTon));
						
						data.set(totalCountPos, String.valueOf(Integer.parseInt(data.get(totalCountPos)) + yardvehicle));
						data.set(totalTonPos, dfTon.format((Double.parseDouble(data.get(totalTonPos)) + expTon)));
						
						inyardTableData.set(yardCurPos, data);
						
						ArrayList<String> foot = inyardTableData.get(totrowspos);
						
						foot.set(countpos, String.valueOf(Integer.parseInt(foot.get(countpos)) + yardvehicle));
						foot.set(tonpos, dfTon.format((Double.parseDouble(foot.get(tonpos)) + expTon)));
						
						foot.set(totalCountPos, String.valueOf(Integer.parseInt(foot.get(totalCountPos)) + yardvehicle));
						foot.set(totalTonPos, dfTon.format((Double.parseDouble(foot.get(totalTonPos)) + expTon)));
						
						inyardTableData.set(totrowspos, foot);
					}
				}
			}
			
			
			int curPos = totrowspos-1;
			
			//yarda bhaherl vechile
			try (PreparedStatement pst1 = conn.prepareStatement("select w.nvehicle_type_id,w.nshift_id,st.nentity_uni_id as t,sb.nentity_uni_id as b, count (case when st.nentity_uni_id is not null then st.nentity_uni_id when sb.nentity_uni_id is not null then sb.nentity_uni_id else null end) as selfother, count(distinct case when w.ntransportor_id is not null then w.ntransportor_id else w.nbulluckcart_id end) as vehicle, sum(ev.nexp_harv_tonnage) as ton from Wb_t_Weight_Slip w , App_m_Trans_Self_Of st,  App_m_Trans_Self_Of sb, App_m_Expected_Ton_Vehicle ev where (w.ntransportor_id = st.nentity_uni_id(+) and w.nbulluckcart_id = sb.nentity_uni_id(+)) and ev.nvehicle_type_id = w.nvehicle_type_id and nvl(ev.nwirerope_no,0)=nvl(w.nwirerope_no,0) and nvl(ev.ntailor_front,0) = nvl(w.ntailor_front,0) and nvl(ev.ntailor_back,0) = nvl(w.ntailor_back,0) and w.ngross_weight>0 and w.nnet_weight is null and w.vyear_id=? group by w.nvehicle_type_id, w.nshift_id,st.nentity_uni_id,sb.nentity_uni_id order by w.nvehicle_type_id, w.nshift_id")) {
				int i = 1;
				pst1.setString(i++, yearcode);
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						int nvehicle_type = rs.getInt("nvehicle_type_id");
						int pos = seqAll.get(nvehicle_type);

						int countpos = (pos - 1) * 2;
						int tonpos = countpos + 1;

						int totalCountPos = (rowtotalpos - 1) * 2;
						int totalTonPos = (totalCountPos + 1);

						ArrayList<String> data  = inyardTableData.get(curPos);
						int emptyVeh = rs.getInt("vehicle");
						int selfother = rs.getInt("selfother");
						
						int vehicle =0;
						if (selfother == 0)
							vehicle = emptyVeh;
						else
							vehicle = selfother;
						double expTon = rs.getDouble("ton");
						data.set(countpos, String.valueOf(Integer.parseInt(data.get(countpos)) + vehicle));
						data.set(tonpos, dfTon.format((Double.parseDouble(data.get(tonpos)) + expTon)));
							
						data.set(totalCountPos, String.valueOf(Integer.parseInt(data.get(totalCountPos)) + vehicle));
						data.set(totalTonPos, dfTon.format((Double.parseDouble(data.get(totalTonPos)) + expTon)));
						
						inyardTableData.set(curPos, data);
						
						ArrayList<String> foot = inyardTableData.get(totrowspos);
						
						foot.set(countpos, String.valueOf(Integer.parseInt(foot.get(countpos)) + vehicle));
						foot.set(tonpos, dfTon.format((Double.parseDouble(foot.get(tonpos)) + expTon)));
						
						foot.set(totalCountPos, String.valueOf(Integer.parseInt(foot.get(totalCountPos)) + vehicle));
						foot.set(totalTonPos, dfTon.format((Double.parseDouble(foot.get(totalTonPos)) + expTon)));
						
						inyardTableData.set(totrowspos, foot);
					}
				}
			}
			
			curPos = totrowspos - 2;
			//yarda bhaherl vechile
			try (PreparedStatement pst1 = conn.prepareStatement("select w.nvehicle_type_id,count(distinct t.ntrans_id) as yardvehicle, sum(v.nexp_harv_tonnage) as ton from app_t_numbertaker t, Wb_t_Weight_Slip w, APP_M_EXPECTED_TON_VEHICLE v where t.vyear_id=w.vyear_id and t.ntoken_no = w.ntoken_no and nvl(t.nlot_no,0) = nvl(w.nlot_no,0) and (t.nentity_uni_id = w.ntransportor_id or t.nentity_uni_id=w.nbulluckcart_id) and w.nvehicle_type_id = v.nvehicle_type_id and nvl(w.nwirerope_no,0)=nvl(v.nwirerope_no,0) and nvl(w.ntailor_front,0) = nvl(v.ntailor_front,0) and nvl(w.ntailor_back,0) = nvl(v.ntailor_back,0) and w.ngross_weight is null and t.vyear_id=? and t.vactive_status in (2,4) and w.vactive_dactive='A' group by w.nvehicle_type_id")) {
				int i = 1;
				pst1.setString(i++, yearcode);
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						int nvehicle_type = rs.getInt("nvehicle_type_id");
						int pos = seqAll.get(nvehicle_type);
						
						int countpos = (pos - 1) * 2;
						int tonpos = countpos + 1;
						
						int totalCountPos  = (rowtotalpos-1)*2;
						int totalTonPos  = (totalCountPos+1);
						
						ArrayList<String> data  = inyardTableData.get(curPos);
						
						int yardvehicle = rs.getInt("yardvehicle");
						double expTon = rs.getDouble("ton");
						data.set(countpos, String.valueOf(yardvehicle));
						data.set(tonpos, dfTon.format(expTon));
						
						data.set(totalCountPos, String.valueOf(Integer.parseInt(data.get(totalCountPos)) + yardvehicle));
						data.set(totalTonPos, dfTon.format((Double.parseDouble(data.get(totalTonPos)) + expTon)));
						
						inyardTableData.set(curPos, data);
						
						ArrayList<String> foot = inyardTableData.get(totrowspos);
						
						foot.set(countpos, String.valueOf(Integer.parseInt(foot.get(countpos)) + yardvehicle));
						foot.set(tonpos, dfTon.format((Double.parseDouble(foot.get(tonpos)) + expTon)));
						
						foot.set(totalCountPos, String.valueOf(Integer.parseInt(foot.get(totalCountPos)) + yardvehicle));
						foot.set(totalTonPos, dfTon.format((Double.parseDouble(foot.get(totalTonPos)) + expTon)));
						
						inyardTableData.set(totrowspos, foot);
					}
				}
			}
			
			dummyData = new ArrayList<>();

			dummyData.add(String.valueOf(1));
			dummyData.add(ConstantVeriables.onRoad);
			for (int j = 2; j < totalelement; j++) {
				dummyData.add("0");
				dummyData.add("0.000");
			}
			outyardTableData.add(dummyData);
			curPos = 2;
			try (PreparedStatement pst1 = conn.prepareStatement("select w.nvehicle_type_id,w.nshift_id,st.nentity_uni_id as t,sb.nentity_uni_id as b, count (case when st.nentity_uni_id is not null then st.nentity_uni_id when sb.nentity_uni_id is not null then sb.nentity_uni_id else null end) as selfother, count(distinct case when w.ntransportor_id is not null then w.ntransportor_id else w.nbulluckcart_id end) as vehicle, sum(ev.nexp_harv_tonnage) as ton from Wb_t_Weight_Slip w , App_m_Trans_Self_Of st,  App_m_Trans_Self_Of sb, App_m_Expected_Ton_Vehicle ev where (w.ntransportor_id = st.nentity_uni_id(+) and w.nbulluckcart_id = sb.nentity_uni_id(+)) and ev.nvehicle_type_id = w.nvehicle_type_id and nvl(ev.nwirerope_no,0)=nvl(w.nwirerope_no,0) and nvl(ev.ntailor_front,0) = nvl(w.ntailor_front,0) and nvl(ev.ntailor_back,0) = nvl(w.ntailor_back,0) and w.vyear_id=? and w.vactive_dactive = 'A' and w.ngross_weight is null and w.ntoken_no is null and w.dcreate_date >= sysdate-3 group by w.nvehicle_type_id, w.nshift_id,st.nentity_uni_id,sb.nentity_uni_id order by w.nvehicle_type_id, w.nshift_id")) {
				int i = 1;
				pst1.setString(i++, yearcode);
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						int nvehicle_type = rs.getInt("nvehicle_type_id");
						int pos = seqAll.get(nvehicle_type);
						
						int countpos = (pos - 1) * 2;
						int tonpos = countpos + 1;
						
						int totalCountPos  = (rowtotalpos-1)*2;
						int totalTonPos  = (totalCountPos+1);
						
						ArrayList<String> data  = outyardTableData.get(curPos);
						
						int emptyVeh = rs.getInt("vehicle");
						int selfother = rs.getInt("selfother");
						
						int vehicle =0;
						if (selfother == 0)
							vehicle = emptyVeh;
						else
							vehicle = selfother;
					
						double expTon = rs.getDouble("ton");
						data.set(countpos, String.valueOf(Integer.parseInt(data.get(countpos)) + vehicle));
						data.set(tonpos, dfTon.format((Double.parseDouble(data.get(tonpos)) + expTon)));
						
						
						data.set(totalCountPos, String.valueOf(Integer.parseInt(data.get(totalCountPos)) + vehicle));
						data.set(totalTonPos, dfTon.format((Double.parseDouble(data.get(totalTonPos)) + expTon)));
						
						outyardTableData.set(curPos, data);
						
						/*ArrayList<String> foot = outyardTableData.get(totPos);
						
						foot.set(countpos, String.valueOf(Integer.parseInt(foot.get(countpos)) + vehicle));
						foot.set(tonpos, dfTon.format((Double.parseDouble(foot.get(tonpos)) + expTon)));
						
						foot.set(totalCountPos, String.valueOf(Integer.parseInt(foot.get(totalCountPos)) + vehicle));
						foot.set(totalTonPos, dfTon.format((Double.parseDouble(foot.get(totalTonPos)) + expTon)));
						
						outyardTableData.set(totPos, foot);*/
					}
				}
			}
			
			dummyData = new ArrayList<>();
			dummyData.add("1");
			dummyData.add(ConstantVeriables.weightDone);
			for (int j = 2; j < totalelement; j++) {
				dummyData.add("0");
				dummyData.add("0.000");
			}
			emptyTableData.add(dummyData);
		
			cal.add(Calendar.HOUR_OF_DAY, -4);
			SimpleDateFormat sdforcl = new SimpleDateFormat("dd-MMM-yyyy");
			try (PreparedStatement pst1 = conn.prepareStatement("select w.nvehicle_type_id, w.nshift_id,st.nentity_uni_id as t,sb.nentity_uni_id as b,count (case when st.nentity_uni_id is not null then st.nentity_uni_id when sb.nentity_uni_id is not null then sb.nentity_uni_id else null end) as selfother,count(distinct case when w.ntransportor_id is not null then w.ntransportor_id else w.nbulluckcart_id end) as emptyvehicle, sum(w.nnet_weight) as ton from Wb_t_Weight_Slip w , App_m_Trans_Self_Of st,  App_m_Trans_Self_Of sb where (w.ntransportor_id = st.nentity_uni_id(+) and w.nbulluckcart_id = sb.nentity_uni_id(+)) and w.nnet_weight > 0 and w.vyear_id=? and w.dslip_date=To_date(?, 'dd-Mon-yyyy') group by w.nvehicle_type_id, w.nshift_id,st.nentity_uni_id,sb.nentity_uni_id order by w.nvehicle_type_id, w.nshift_id")) {
				int i = 1;
				pst1.setString(i++, yearcode);
				pst1.setString(i++, sdforcl.format(cal.getTime()));
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						int nvehicle_type = rs.getInt("nvehicle_type_id");
						int pos = seqAll.get(nvehicle_type);
						
						int countpos = (pos - 1) * 2;
						int tonpos = countpos + 1;
						
						int totalCountPos  = (rowtotalpos-1)*2;
						int totalTonPos  = (totalCountPos+1);
						
						int emptyVeh = rs.getInt("emptyvehicle");
						int selfother = rs.getInt("selfother");
						
						int vehicle =0;
						if (selfother == 0)
							vehicle = emptyVeh;
						else
							vehicle = selfother;
						double expTon = rs.getDouble("ton");
						
						ArrayList<String> data = emptyTableData.get(2);
						
						data.set(countpos, String.valueOf(Integer.parseInt(data.get(countpos)) + vehicle));
						data.set(tonpos, dfTon.format((Double.parseDouble(data.get(tonpos)) + expTon)));
						
						data.set(totalCountPos, String.valueOf(Integer.parseInt(data.get(totalCountPos)) + vehicle));
						data.set(totalTonPos, dfTon.format((Double.parseDouble(data.get(totalTonPos)) + expTon)));
						
						emptyTableData.set(2, data);
					}
				}
			}
			
			Integer width[] = new Integer[totrowspos*2];
			
			int widthall = 80 / (totrowspos * 2 - 2);
			int widthRemain = 80 % (totrowspos * 2 - 2);

			width[0] = 5;
			width[1] = 15 + widthRemain;
			
			for (int j = 2; j < width.length; j++) {
				width[j] = widthall;
			}
			
			TableReportBean inyard = new TableReportBean();
			inyard.setNoofHeads(2);
			inyard.setFooter(true);
			inyard.setMarathi(true);
			inyard.setFloatings(null);
			inyard.setRowColSpan(rowColSpan);
			inyard.setBoldIndicator(null);
			inyard.setTableData(inyardTableData);
			inyard.setColWidth(width);

			TableReportBean outyard = new TableReportBean();
			outyard.setNoofHeads(2);
			outyard.setFooter(false);
			outyard.setMarathi(true);
			outyard.setFloatings(null);
			outyard.setRowColSpan(rowColSpan);
			outyard.setBoldIndicator(null);
			outyard.setTableData(outyardTableData);
			outyard.setColWidth(width);

			TableReportBean empty = new TableReportBean();
			empty.setNoofHeads(2);
			empty.setFooter(false);
			empty.setMarathi(true);
			empty.setFloatings(null);
			empty.setRowColSpan(rowColSpan);
			empty.setBoldIndicator(null);
			empty.setTableData(emptyTableData);
			empty.setColWidth(width);
			
			summaryreportResponse.setInyardVehicle(inyard);
			summaryreportResponse.setOutyardVehicle(outyard);
			summaryreportResponse.setEmptyVehicle(empty);
			summaryreportResponse.setSuccess(true);
			summaryreportResponse.setDateTime(sdfdt.format(Calendar.getInstance().getTime()));
			
		} catch (Exception e) {
			e.printStackTrace();
			summaryreportResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Error " + e.getMessage());
			summaryreportResponse.setSe(error);
			e.printStackTrace();
		}
		return summaryreportResponse;
	}

	public TableResponse inwardSummaryReport(String rdate, String chit_boy_id, TableResponse reqResponse,
			Connection conn) {
		try {
			TableReportBean sumarryBean = new TableReportBean();
			int noofheadDailyCrush =2;
			sumarryBean.setNoofHeads(noofheadDailyCrush);
			sumarryBean.setFooter(true);
			sumarryBean.setMarathi(true);
			
			HashMap<String, Integer> rowColSpan = new HashMap<>();
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			ArrayList<ArrayList<String>> tableData = new ArrayList<>();
			boldIndicator.put("0-*", true);
			boldIndicator.put("1-*", true);
			
			ArrayList<String> head=new ArrayList<>();
			head.add(ConstantVeriables.lblYard);
			
			ArrayList<String> tempHead=new ArrayList<>();
			HashMap<Integer, Integer> seqAll = new HashMap();
			
			try (PreparedStatement pst1 = conn.prepareStatement("select t.nvehicle_group_id, TO_NCHAR(t.vvehicle_group_name_local) vehicle_type from APP_M_VEHICLE_GROUP t")) {
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						String vehicle_type = DemoConvert2.ism_to_uni(rs.getString("vehicle_type"));
						tempHead.add(vehicle_type);
						int pos = tempHead.size();
						seqAll.put(rs.getInt("nvehicle_group_id"), pos);
					}
				}
			}
			
			head.add("4-12");
			head.add("12-8");
			head.add("8-4");
			head.add(ConstantVeriables.total);
			tableData.add(head);
			
			head = new ArrayList<>();
			head.add("");
			head.addAll(tempHead);
			head.addAll(tempHead);
			head.addAll(tempHead);
			head.addAll(tempHead);
			tableData.add(head);
			
			int size = tempHead.size();
			rowColSpan.put("0-1", size);
			rowColSpan.put("0-2", size);
			rowColSpan.put("0-3", size);
			rowColSpan.put("0-4", size);
			HashMap<Integer, Integer> yardPos = new HashMap();
			
			try (PreparedStatement pst1 = conn.prepareStatement("select t.nyard_id, TO_NCHAR(t.vyard_name_mar) as vyard_name_mar from App_m_caneyard t")) {
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						yardPos.put(rs.getInt("nyard_id"), tableData.size());
						ArrayList<String> dummyData = new ArrayList<>();
						
						dummyData.add(DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")));
						for (int i = 0; i < size; i++) {
							dummyData.add("0");
							dummyData.add("0");
							dummyData.add("0");
							dummyData.add("0");
						}
						tableData.add(dummyData);
					}
				}
			}
			
			ArrayList<String> dummyData = new ArrayList<>();
			dummyData.add(ConstantVeriables.total);
			for (int i = 0; i < size; i++) {
				dummyData.add("0");
				dummyData.add("0");
				dummyData.add("0");
				dummyData.add("0");
			}
			tableData.add(dummyData);
			int footpos = tableData.size()-1;
			String sql="select t.nyard_id, t.nshift_no, t.nvehicle_group_id, count(t.ntrans_id) as total from app_t_numbertaker t where t.vactive_status <> 6 and t.dcrush_date = TO_DATE(?,'dd-Mon-yyyy') group by t.nyard_id, t.nshift_no, t.nvehicle_group_id order by t.nvehicle_group_id";
			try (PreparedStatement pst1 = conn.prepareStatement(sql)) { // and t.ngross_weight is null
				int i = 1;
				pst1.setString(i++, Constant.AppDateDbDate(rdate));
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						int nvehicle_type = rs.getInt("nvehicle_group_id");
						int nyard_id = rs.getInt("nyard_id");
						int nshift_no = rs.getInt("nshift_no");
						int pos = seqAll.get(nvehicle_type);

						int yardCurPos = yardPos.get(nyard_id);
						int countpos = (nshift_no - 1) * size + pos;
						int totcountpos = (4 - 1) * size + pos;
						
						ArrayList<String> data  = tableData.get(yardCurPos);
						
						int total = rs.getInt("total");
						data.set(countpos, String.valueOf(total));
						data.set(totcountpos, String.valueOf(Integer.parseInt(data.get(totcountpos)) + total));
						tableData.set(yardCurPos, data);
						
						ArrayList<String> foot = tableData.get(footpos);
						
						foot.set(countpos, String.valueOf(Integer.parseInt(foot.get(countpos)) + total));
						foot.set(totcountpos, String.valueOf(Integer.parseInt(foot.get(totcountpos)) + total));
						
						tableData.set(footpos, foot);
					}
				}
			}
			
			Integer width[] = new Integer[size*4+1];
			
			int widthall = 90 / (size*4);
			int widthRemain = 90 % (size*4);

			width[0] = 10 + widthRemain;

			for (int j = 1; j < size*4+1; j++) {
				width[j] = widthall;
			}
			
			boldIndicator.put(footpos+"-*", true);
			sumarryBean.setRowColSpan(rowColSpan);
			sumarryBean.setBoldIndicator(boldIndicator);
			sumarryBean.setTableData(tableData);
			sumarryBean.setFooter(true);
			sumarryBean.setColWidth(width);
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