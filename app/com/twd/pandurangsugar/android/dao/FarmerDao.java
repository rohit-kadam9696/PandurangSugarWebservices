package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.EntityMasterDetail;
import com.twd.pandurangsugar.android.bean.FarmerResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class FarmerDao {

	public FarmerResponse farmerDetailsByCode(String farmerCode,FarmerResponse dataLoadResponse, Connection conn) {
		try
		{
			try(PreparedStatement pst=conn.prepareStatement("select sysdate as currentdate,t.nbank_id,t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as ventity_name_local,"
					+ "t.nfarmer_type_id,t.vmobile_no,t.vaadhaar_no,t.vaadhaar_photo,t.vpan_no,t.vpassbook_photo,"
					+ "t.vbank_ac_no,t.dbirth_date,t.nage from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_category_id = 1 AND t.nentity_uni_id=?"))
			{
				pst.setString(1, farmerCode);
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						EntityMasterDetail farmer=new EntityMasterDetail();
						farmer.setNentityUniId(rs.getString("nentity_uni_id"));
						farmer.setNfarmerTypeId(rs.getInt("nfarmer_type_id"));
						farmer.setVentityNameLocal(rs.getString("ventity_name_local")!=null?DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")):"NO Name");
						if(rs.getString("dbirth_date")!=null && rs.getString("dbirth_date").trim().length()>0)
						{
							farmer.setDateOfBirth(Constant.DbDateToAppDate(rs.getDate("dbirth_date")));
						}
						farmer.setAadharNo(rs.getString("vaadhaar_no")==null?"":rs.getString("vaadhaar_no"));
						farmer.setAadharPhoto(rs.getString("vaadhaar_photo")==null?"":rs.getString("vaadhaar_photo"));
						farmer.setPassbookPhoto(rs.getString("vpassbook_photo")==null?"":rs.getString("vpassbook_photo"));
						farmer.setNbankId(rs.getInt("nbank_id"));
						farmer.setVbankAcNo(rs.getString("vbank_ac_no")==null?"":rs.getString("vbank_ac_no"));
						farmer.setVmobileNo(rs.getString("vmobile_no")==null?"":rs.getString("vmobile_no"));
						dataLoadResponse.setEntityMasterDetail(farmer);
					}else
					{
						dataLoadResponse.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg(ConstantMessage.informationNotFound);
						dataLoadResponse.setSe(error);
					}
				}
			}
			
		}
			catch (Exception e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("farmer Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public ActionResponse updateBirthDate(String farmerCode, String dbirthDate, String age, String chit_boy_id,
			ActionResponse farmerResponse, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("update GM_M_ENTITY_MASTER_DETAIL SET dbirth_date=TO_DATE(?,'dd-Mon-yyyy'),nage=?,nupdate_user_id=?, dupdate_date=sysdate WHERE nentity_category_id = 1 AND nentity_uni_id=?"))
		{
			int i=1;
			pst.setString(i++, Constant.AppDateDbDate(dbirthDate));
			pst.setString(i++, age);
			pst.setString(i++, chit_boy_id);
			pst.setString(i++, farmerCode);
			int r=pst.executeUpdate();
			if(r>0)
			{
				farmerResponse.setSuccess(true);
				farmerResponse.setActionComplete(true);
				farmerResponse.setSuccessMsg(ConstantMessage.saveSuccess);
			}else
			{
				farmerResponse.setSuccess(true);
				farmerResponse.setActionComplete(false);
				farmerResponse.setFailMsg(ConstantMessage.saveFailed);
			}
		} catch (Exception e) {
			farmerResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("farmer birthDate Update "+e.getMessage());
			farmerResponse.setSe(error);
			e.printStackTrace();
		}
		return farmerResponse;
	}

	public ActionResponse updateMobileNoByfarmerCode(String farmerCode, String vmobileNo, String chit_boy_id,
			ActionResponse farmerResponse, Connection conn) {
		try {
			conn.setAutoCommit(false);
			String oldMobNo = null;
			try (PreparedStatement pst = conn.prepareStatement("select t.vmobile_no from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_category_id = 1 AND t.nentity_uni_id=?")) {
				pst.setString(1, farmerCode);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						oldMobNo = rs.getString("vmobile_no");
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("update GM_M_ENTITY_MASTER_DETAIL SET vmobile_no=?, nupdate_user_id=?, dupdate_date=sysdate WHERE nentity_category_id = 1 AND nentity_uni_id=?"))
			{
				int i=1;
				pst.setString(i++, vmobileNo);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, farmerCode);
				int r=pst.executeUpdate();
				if(r>0)
				{
					farmerResponse.setSuccess(true);
					farmerResponse.setActionComplete(true);
					farmerResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					farmerResponse.setSuccess(true);
					farmerResponse.setActionComplete(false);
					farmerResponse.setFailMsg(ConstantMessage.saveFailed);
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("insert into APP_T_ENTITY_MODIFIED (nentity_uni_id,nupdate_user_id,dupdate_date,vmobile_no_old,vmobile_no_new,nupdate_mode) values (?,?,sysdate,?,?,2)"))
			{
				int i=1;
				pst.setString(i++, farmerCode);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, oldMobNo);
				pst.setString(i++, vmobileNo);
				int r=pst.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			farmerResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Farmer Mobile Number Update "+e.getMessage());
			farmerResponse.setSe(error);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return farmerResponse;
	}

	public ActionResponse updateAadharByFarmerCode(String farmerCode, String vaadhaarNo, String vaadhaarPhoto,  String chit_boy_id,
			ActionResponse farmerResponse, Connection conn) {
		try {
			conn.setAutoCommit(false);
			String vaadhaar_no_old = null;
			String vaadhaar_photo_old = null;
			try (PreparedStatement pst = conn.prepareStatement("select t.vaadhaar_no, t.vaadhaar_photo from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_category_id = 1 AND t.nentity_uni_id=?")) {
				pst.setString(1, farmerCode);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						vaadhaar_no_old = rs.getString("vaadhaar_no");
						vaadhaar_photo_old = rs.getString("vaadhaar_photo");
					}
				}
			}

			try(PreparedStatement pst=conn.prepareStatement("update GM_M_ENTITY_MASTER_DETAIL SET vaadhaar_no=?,vaadhaar_photo=?, nupdate_user_id=?, dupdate_date=sysdate WHERE nentity_category_id = 1 AND nentity_uni_id=?"))
			{
				int i=1;
				pst.setString(i++, vaadhaarNo);
				pst.setString(i++, vaadhaarPhoto);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, farmerCode);
				int r=pst.executeUpdate();
				if(r>0)
				{
					farmerResponse.setSuccess(true);
					farmerResponse.setActionComplete(true);
					farmerResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					farmerResponse.setSuccess(true);
					farmerResponse.setActionComplete(false);
					farmerResponse.setFailMsg(ConstantMessage.saveFailed);
				}
			}
			try(PreparedStatement pst=conn.prepareStatement("insert into APP_T_ENTITY_MODIFIED (nentity_uni_id,nupdate_user_id,dupdate_date,vaadhaar_no_old,vaadhaar_no_new,vaadhaar_photo_old,vaadhaar_photo_new,nupdate_mode) values (?,?,sysdate,?,?,?,?,2)"))
			{
				int i=1;
				pst.setString(i++, farmerCode);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, vaadhaar_no_old);
				pst.setString(i++, vaadhaarNo);
				pst.setString(i++, vaadhaar_photo_old);
				pst.setString(i++, vaadhaarPhoto);
				int r=pst.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			farmerResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Farmer Aadhar Update "+e.getMessage());
			farmerResponse.setSe(error);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return farmerResponse;
	}
	
	public ActionResponse updateBankByFarmerCode(String farmerCode, String nbankId, String vpassbookPhoto,String vbankAcNo, String chit_boy_id,
			ActionResponse farmerResponse, Connection conn) {
		try {
		conn.setAutoCommit(false);
		String nbankIdOld = null;
		String vpassbookPhotoOld = null;
		String vbankAcNoOld = null;
			try (PreparedStatement pst = conn.prepareStatement("select t.nbank_id, t.vpassbook_photo,t.vbank_ac_no from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_category_id = 1 AND t.nentity_uni_id=?")) {
				pst.setString(1, farmerCode);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
						nbankIdOld = rs.getString("nbank_id");
						vpassbookPhotoOld = rs.getString("vpassbook_photo");
						vbankAcNoOld = rs.getString("vbank_ac_no");
					}
				}
			}
			try(PreparedStatement pst=conn.prepareStatement("update GM_M_ENTITY_MASTER_DETAIL SET nbank_id=?,vpassbook_photo=?,vbank_ac_no=?, nupdate_user_id=?, dupdate_date=sysdate WHERE nentity_category_id = 1 AND nentity_uni_id=?"))
			{
				int i=1;
				pst.setString(i++, nbankId);
				pst.setString(i++, vpassbookPhoto);
				pst.setString(i++, vbankAcNo);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, farmerCode);
				int r=pst.executeUpdate();
				if(r>0)
				{
					farmerResponse.setSuccess(true);
					farmerResponse.setActionComplete(true);
					farmerResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					farmerResponse.setSuccess(true);
					farmerResponse.setActionComplete(false);
					farmerResponse.setFailMsg(ConstantMessage.saveFailed);
				}
				
			} 
			try(PreparedStatement pst1=conn.prepareStatement("insert into APP_T_ENTITY_MODIFIED (nentity_uni_id,nupdate_user_id,dupdate_date,nbank_id_old,nbank_id_new,vbank_ac_no_old,vbank_ac_no_new,vpassbook_photo_old,vpassbook_photo_new,nupdate_mode) values (?,?,sysdate,?,?,?,?,?,?,2)"))
			{
				int i=1;
				pst1.setString(i++, farmerCode);
				pst1.setString(i++, chit_boy_id);
				pst1.setString(i++, nbankIdOld);
				pst1.setString(i++, nbankId);
				pst1.setString(i++, vbankAcNoOld);
				pst1.setString(i++, vbankAcNo);
				pst1.setString(i++, vpassbookPhotoOld);
				pst1.setString(i++, vpassbookPhoto);
				int r=pst1.executeUpdate();
			}
			conn.commit();
		} catch (Exception e) {
			farmerResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Farmer Bank Details Update "+e.getMessage());
			farmerResponse.setSe(error);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return farmerResponse;
	}

}
