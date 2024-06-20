package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.FertProduct;
import com.twd.pandurangsugar.android.bean.FertProductResponse;
import com.twd.pandurangsugar.android.bean.SavePrintResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class FertDao {

	public FertProductResponse getProductList(String saletypes, FertProductResponse fertProduct, Connection conn) {
		try {
			String sql="select t4.vsubstore_id, TO_NCHAR(t4.vsubstore_name_local) vsubstore_name_local, t.vitem_id, TO_NCHAR(t.vitem_name_mar) vitem_name_mar, TO_NCHAR(t1.vunit_name) vunit_name, t1.nunit_id, t.nhsn_id, t2.vhsn_code, t2.ntax_per, t3.nitem_rate, t5.nbal_qty from IN_M_ITEM_MASTER t, GM_M_UNIT_MASTER t1, GM_M_HSN_SAC_MASTER t2, IN_M_ITEM_RATE_MASTER t3, IN_M_SUBSTORE_MASTER t4, in_v_fertilizer_item_balance_2 t5 where t5.vitem_id=t.vitem_id and t.vsubstore_id=t5.vsubstore_id and t5.nbal_qty>0 and t1.nunit_id = t.nunit_id and t.nhsn_id=t2.nhsn_id and t.vitem_id = t3.vitem_id and t3.vsale_type=? and t.vsubstore_id=t4.vsubstore_id and t4.vsubstore_ferti_flag='Y' and t.vitem_ferti_flag = 'Y' and t3.dfrom_date<=sysdate and t3.dto_date>=To_date(To_Char(sysdate, 'dd-Mon-yyyy'), 'dd-Mon-yyyy') order by t.vitem_id";
			try (PreparedStatement pst = conn.prepareStatement(sql)) {
				int i = 1;
				pst.setString(i++, saletypes);
				try (ResultSet rs = pst.executeQuery()) {
					List<FertProduct> datalist = new ArrayList<>();
					while (rs.next()) {
						FertProduct data=new FertProduct();
						data.setSubstoreId(rs.getString("vsubstore_id"));
						data.setSubstoreName(DemoConvert2.ism_to_uni(rs.getString("vsubstore_name_local")));
					 	data.setItemId(rs.getString("vitem_id"));
						data.setItemName(DemoConvert2.ism_to_uni(rs.getString("vitem_name_mar")));
						data.setUnitId(rs.getString("nunit_id"));
						data.setUnitName(DemoConvert2.ism_to_uni(rs.getString("vunit_name")));
						data.setNhsnId(rs.getString("nhsn_id"));
						data.setHsnCode(rs.getString("vhsn_code"));
						data.setTaxPer(rs.getString("ntax_per"));
						data.setItemRate(rs.getString("nitem_rate"));
						data.setItemStock(rs.getString("nbal_qty"));
						datalist.add(data);
					}
					fertProduct.setFertProducts(datalist);
				}
			}
		} catch (Exception e) {
			fertProduct.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Cane Confirmation Load Data Issue " + e.getMessage());
			fertProduct.setSe(error);
			e.printStackTrace();
		}
		return fertProduct;

	}

	public SavePrintResponse saveFert(String vyearId, String dissueDate, String nentityUniId, String nfertGuarantor1,
			String nfertGuarantor2, String nfertiVatapArea, String vsaleType, String vseasonYear, String nplotNo,
			String nlocationId, String chit_boy_id, String farmername, String guarantor1name, String guarantor2name, JSONArray details, SavePrintResponse saveRes, Connection conn) {
		try {
			conn.setAutoCommit(false);
			int nissue_id = 1;
			int  i = 1;
			try (PreparedStatement pst = conn.prepareStatement("select max(t.nissue_id) as nissue_id from " + ConstantVeriables.fertilizerHeader + " t where t.vyear_id = ?")) {
				pst.setString(i++, vyearId);
				try (ResultSet rs = pst.executeQuery()) {
					while(rs.next()) {
						nissue_id = rs.getInt("nissue_id") + 1;
					}
				}
			}
			
			HashMap<String, String> group = new HashMap<>();
			HashMap<String, String> entity = new HashMap<>();
			try (PreparedStatement pst = conn.prepareStatement("select t.vitem_id, t.ngroup_id, t.nentity_uni_id from IN_M_ITEM_FERTILIZER_TYPE t")) {
				try (ResultSet rs = pst.executeQuery()) {
					while(rs.next()) {
						group.put(rs.getString("vitem_id"), rs.getString("ngroup_id"));
						entity.put(rs.getString("vitem_id"), rs.getString("nentity_uni_id"));
					}
				}
			}
			
			int totRows=0;
			 String lineBreak = "<hr style=\"height:1px; background-color:#000; border:none;\">";
			//create html here 
			String print = "<table>";
			print += "<tr>"
					+ "<td class='cbodysmall' colspan='2'>" + ConstantVeriables.lblDate + "</td>"
					+ "<td  class='cbody' colspan='2'>:" + dissueDate + "</td>" 
					+ "</tr>";
			print += "<tr>" 
					+ "<td class='cbodysmall' colspan='2'>" + ConstantVeriables.hangam + "</td>"
					+ "<td  class='cbody' colspan='2'>:" + vseasonYear + "</td>" 
					+ "</tr>";
			print += "<tr>" 
					+ "<td class='cbodysmall' colspan='4'>" + ConstantVeriables.lblFarmer + ":" + farmername + "</td>" 
					+ "</tr>";
			if (guarantor1name != null && !guarantor1name.trim().isEmpty()) {
				print += "<tr><td class='cbodysmall' colspan='4'>" + ConstantVeriables.lblguarantor + " 1 :" + guarantor1name.replace(":", "-") + "</td></tr>";
			}

			if (guarantor2name != null && !guarantor2name.trim().isEmpty()) {
				print += "<tr><td class='cbodysmall' colspan='4'>" + ConstantVeriables.lblguarantor + " 2 :" + guarantor2name.replace(":", "-") + "</td></tr>";
			}
			print += "<tr><td class='cbody center txtcenter' colspan='4'><b>" + ConstantVeriables.lblFertSale + "</b></td></tr>";
			print += "<tr><td colspan='4'>" + lineBreak + "<td></tr>";
			print += "<tr><td class='cbodysmall borderTop' colspan='4'>" + ConstantVeriables.lblcodeAndFertName + "</td></tr>";
			
			print += "<tr>" 
					+ "<td class='cbodysmall borderBottom txtright'>" + ConstantVeriables.nag + "</td>"
					+ "<td class='cbodysmall borderBottom txtright'>" + ConstantVeriables.rate + "</td>" 
					+ "<td class='cbodysmall borderBottom txtright'>" + ConstantVeriables.tax + "</td>" 
					+ "<td class='cbodysmall borderBottom txtright'>" + ConstantVeriables.total + "</td>" 
					+ "</tr>";
			
			try (PreparedStatement pst = conn.prepareStatement("insert into " + ConstantVeriables.fertilizerHeader + " (vyear_id,nissue_id,dissue_date,nissue_type_id,nentity_uni_id,dcreate_date,dupdate_date,ncreate_user_id,ncharge_type,vget_pass,nsubdept_id,ngroup_id,cissue_closed,nuser_role_id,nfert_guarantor_1,nfert_guarantor_2,nlocation_id,n_ferti_vatap_area,vsale_type,vseason_year,nplot_no) values (?,?,TO_DATE(?, 'dd-Mon-yyyy'),4,?,sysdate,sysdate,?,2,'N',13,120,'N',108,?,?,?,?,?,?,?)")) {
				i = 1;
				pst.setString(i++, vyearId);
				pst.setInt(i++, nissue_id);
				pst.setString(i++, Constant.AppDateDbDate(dissueDate));
				pst.setString(i++, nentityUniId);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, nfertGuarantor1);
				pst.setString(i++, nfertGuarantor2);
				pst.setString(i++, nlocationId);
				pst.setString(i++, nfertiVatapArea);
				pst.setString(i++, vsaleType);
				pst.setString(i++, vseasonYear);
				pst.setString(i++, nplotNo.contains(",") ? null : nplotNo);
				totRows += pst.executeUpdate();
			}
			DecimalFormat df = new DecimalFormat("#0.00");
			double totalAmount = 0, totalTax = 0;
			
			int size = details.length();
			if (size > 0) {
				try (PreparedStatement pst = conn.prepareStatement("insert into " + ConstantVeriables.fertilizerDetails + " (vyear_id,nissue_id,vitem_id,nindent_qty,nissued_qty,nentity_uni_id,nsr_no,nunit_id,navg_rate,namount,nsale_rate,nsale_amount,ngroup_id,ncost_id,ncgst_rate,ncgst_amt,nsgst_rate,nsgst_amt,nigst_rate,nigst_amt) values (?,?,?,?,?,?,?,?,0,?,?,?,?,10,?,?,?,?,0,0)")) {
					for (int k = 0; k < size; k++) {
						JSONObject innerJob = details.getJSONObject(k);
						i = 1;
						String vitem_id = innerJob.has("vitem_id") ? innerJob.getString("vitem_id") : "";
						String vitem_name = innerJob.has("vitem_name") ? innerJob.getString("vitem_name") : "";
						String nindent_qty = innerJob.has("nindent_qty") ? innerJob.getString("nindent_qty") : "";
						String nsale_rate = innerJob.has("nsale_rate") ? innerJob.getString("nsale_rate") : "";
						String nsale_amount = innerJob.has("nsale_amount") ? innerJob.getString("nsale_amount") : "";
						String namount = innerJob.has("namount") ? innerJob.getString("namount") : "";
						String ncgst_amt = innerJob.has("ncgst_amt") ? innerJob.getString("ncgst_amt") : "";
						String nsgst_amt = innerJob.has("nsgst_amt") ? innerJob.getString("nsgst_amt") : "";
						double tax = (Double.parseDouble(ncgst_amt) + Double.parseDouble(nsgst_amt));
						pst.setString(i++, vyearId);
						pst.setInt(i++, nissue_id);
						pst.setString(i++, vitem_id);
						pst.setString(i++, nindent_qty);
						pst.setString(i++, innerJob.has("nissued_qty") ? innerJob.getString("nissued_qty") : "");
						pst.setString(i++, entity.containsKey(vitem_id) ? entity.get(vitem_id) : "");
						pst.setString(i++, String.valueOf(k + 1));
						pst.setString(i++, innerJob.has("nunit_id") ? innerJob.getString("nunit_id") : "");
						pst.setString(i++, innerJob.has("namount") ? innerJob.getString("namount") : "");
						pst.setString(i++, nsale_rate);
						pst.setString(i++, nsale_amount);
						pst.setString(i++, group.containsKey(vitem_id) ? group.get(vitem_id) : "");
						pst.setString(i++, innerJob.has("ncgst_rate") ? innerJob.getString("ncgst_rate") : "");
						pst.setString(i++, ncgst_amt);
						pst.setString(i++, innerJob.has("nsgst_rate") ? innerJob.getString("nsgst_rate") : "");
						pst.setString(i++, nsgst_amt);
						totRows += pst.executeUpdate();
						
						print += "<tr><td class='cbodysmall borderTop' colspan='4'>" + vitem_id + "-" + vitem_name + "</td></tr>";
						
						print += "<tr>" 
								+ "<td class='cbodysmall borderBottom txtright'>" + nindent_qty + "</td>"
								+ "<td class='cbodysmall borderBottom txtright'>" + nsale_rate + "</td>" 
								+ "<td class='cbodysmall borderBottom txtright'>" + df.format(tax) + "</td>" 
								+ "<td class='cbodysmall borderBottom txtright'>" + namount + "</td>" 
								+ "</tr>";

						totalAmount += Double.parseDouble(nsale_amount);
						totalTax += tax;
					}
				}
			} else {
				throw new Exception("Item Not Found");
			}
			try (PreparedStatement pst = conn.prepareStatement("update cr_t_plantation set v_ferti_vatap_flag = 'Y' where vyear_id=? and nplot_no in (" + nplotNo + ")")) {
				i = 1;
				pst.setString(i++, vseasonYear);
				totRows += pst.executeUpdate();
			}
			if (totRows >= (size + 2)) {
				conn.commit();
				saveRes.setActionComplete(true);
				saveRes.setSuccessMsg(ConstantMessage.saveSuccess);
				
				// add sub foot
				print+="<tr>"
						+ "<td class='cbodysmall' colspan='2'>"+ConstantVeriables.total+"</td>"
						+ "<td  class='cbody txtright' colspan='2'>"+df.format(totalAmount)+"</td>"
						+ "</tr>";
				print+="<tr>"
						+ "<td class='cbodysmall' colspan='2'>"+ConstantVeriables.tax+"</td>"
						+ "<td  class='cbody txtright' colspan='2' >"+df.format(totalTax)+"</td>"
						+ "</tr>";
				print+="<tr>"
						+ "<td class='cbodysmall' colspan='2'>"+ConstantVeriables.finalTotal+"</td>"
						+ "<td  class='cbody txtright' colspan='2' >"+df.format(totalTax+totalAmount)+"</td>"
						+ "</tr>";
				print+="<tr>"
						+ "<td class='cbodysmall' colspan='4'>"+ConstantVeriables.lblUserName+ ":" + saveRes.getVfullName() +"</td>"
						+ "</tr>";
				print+="</table>";
				
				saveRes.setHtmlContent(print);
			} else {
				saveRes.setActionComplete(false);
				saveRes.setFailMsg(ConstantMessage.saveFailed);
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			saveRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg(ConstantMessage.fromSavedFailed + " : " + e.getLocalizedMessage());
			saveRes.setSe(error);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return saveRes;
	}

}
