package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.EntityMasterDetail;
import com.twd.pandurangsugar.android.bean.FarmerListResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class RequestActionDao {

	public ActionResponse save(String farmerCode, String nvillageId,String chit_boy_id, ActionResponse saveActionResponse,
			Connection conn) {
		try
		{
		try(PreparedStatement pst=conn.prepareStatement("INSERT INTO CR_T_REGISTRATION_APPROVAL(nentity_uni_id,nuser_id,nvillage_id_requetsed,vstatus,dentry_date) values(?,?,?,1,sysdate)"))
			{
				int i=1;
				pst.setString(i++, farmerCode);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, nvillageId);
				int res=pst.executeUpdate();
				saveActionResponse.setSuccess(true);
				if(res>0)
				{
					saveActionResponse.setActionComplete(true);
					saveActionResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					saveActionResponse.setActionComplete(false);
					saveActionResponse.setSuccessMsg(ConstantMessage.saveFailed);
				}
					
			}
			
		}
			catch (SQLException e) {
				saveActionResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Request Plantation From "+e.getMessage());
				saveActionResponse.setSe(error);
				e.printStackTrace();
			}
		return saveActionResponse;
	}

	public ActionResponse verifyOrReject(String farmerCode, String nvillageId, String chit_boy_id,String status,String requestChitboyId,
			ActionResponse saveActionResponse, Connection conn) {
		try
		{
			try(PreparedStatement pst=conn.prepareStatement("UPDATE CR_T_REGISTRATION_APPROVAL SET vstatus=?,naction_user_id=?,daction_date=SYSDATE WHERE nentity_uni_id=? and nvillage_id_requetsed=? AND nuser_id=?"))
			{
				int i=1;
				pst.setString(i++, status);
				pst.setString(i++, chit_boy_id);
				pst.setString(i++, farmerCode);
				pst.setString(i++, nvillageId);
				pst.setString(i++, requestChitboyId);
				int res=pst.executeUpdate();
				saveActionResponse.setSuccess(true);
				if(res>0)
				{
					saveActionResponse.setActionComplete(true);
					saveActionResponse.setSuccessMsg(ConstantMessage.saveSuccess);
				}else
				{
					saveActionResponse.setActionComplete(false);
					saveActionResponse.setSuccessMsg(ConstantMessage.saveFailed);
				}
					
			}
			
		}
			catch (SQLException e) {
				saveActionResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Request Platation From "+e.getMessage());
				saveActionResponse.setSe(error);
				e.printStackTrace();
			}
		return saveActionResponse;
	}

	public FarmerListResponse farmerList(String farmerCode, String nvillageId, String chit_boy_id, String status,
			FarmerListResponse farmerListResponse, boolean isveriftList, Connection conn) {
		try
		{
			ArrayList<EntityMasterDetail> farmerList= new ArrayList<>();
			String sql="select t1.nuser_id,t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as ventity_name_local,t1.nvillage_id_requetsed,t.nfarmer_type_id,t1.vstatus,TO_NCHAR(t2.village_name_local)as vill_name from GM_M_ENTITY_MASTER_DETAIL t,CR_T_REGISTRATION_APPROVAL t1,gm_m_village_master t2 where t.nentity_uni_id=t1.nentity_uni_id and t1.nvillage_id_requetsed=t2.nvillage_id and t.nentity_category_id = 1  and t.vblocked = 'N' and t1.nuser_id="+chit_boy_id+" order by t.ventity_name_local";
			if(isveriftList)
				sql="select t1.nuser_id,t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as ventity_name_local,t1.nvillage_id_requetsed,t.nfarmer_type_id,TO_NCHAR(t2.vfull_name_local)as chitboyname,TO_NCHAR(t3.village_name_local)as vill_name from GM_M_ENTITY_MASTER_DETAIL t,CR_T_REGISTRATION_APPROVAL t1,app_m_user_master t2,gm_m_village_master t3 where t.nentity_uni_id=t1.nentity_uni_id and t1.nuser_id=t2.nuser_name and t1.nvillage_id_requetsed=t3.nvillage_id and t.nentity_category_id = 1  and t.vblocked = 'N' and t1.vstatus=1 order by t.ventity_name_local";
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						EntityMasterDetail farmer=new EntityMasterDetail();
						farmer.setNentityUniId(rs.getString("nentity_uni_id"));
						farmer.setNfarmerTypeId(rs.getInt("nfarmer_type_id"));
						farmer.setNvillageId(rs.getInt("nvillage_id_requetsed"));
						farmer.setNbankId(rs.getInt("nuser_id"));
						farmer.setVillname(rs.getString("vill_name")!=null?DemoConvert2.ism_to_uni(rs.getString("vill_name")):"NO Village Name");
						farmer.setVentityNameLocal(rs.getString("ventity_name_local")!=null?DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")):"NO Name");
						if(isveriftList)
							farmer.setNuserName(rs.getString("chitboyname")!=null?DemoConvert2.ism_to_uni(rs.getString("chitboyname")):"NO Name");
						else
							farmer.setStatus(rs.getInt("vstatus")==1?ConstantVeriables.statusSubmit:rs.getInt("vstatus")==2?ConstantVeriables.statusApproved:ConstantVeriables.statusReject);
						farmerList.add(farmer);
					}
				}
			}
			farmerListResponse.setEntityMasterDetailList(farmerList);
		}
			catch (SQLException e) {
				farmerListResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("farmer Issue "+e.getMessage());
				farmerListResponse.setSe(error);
				e.printStackTrace();
			}
		return farmerListResponse;
	}

	public ActionResponse checkEntryExit(String farmerCode, String chit_boy_id, ActionResponse saveActionResponse, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.dentry_date from CR_T_REGISTRATION_APPROVAL t where t.nentity_uni_id=? and t.nuser_id=?")){
			pst.setString(1, farmerCode);
			pst.setString(2, chit_boy_id);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					saveActionResponse.setActionComplete(false);
					saveActionResponse.setFailMsg(ConstantMessage.caneApprovalEntryAlreadyExit+" : "+Constant.DbDateTimeToAppDateTime(rs.getTimestamp("dentry_date")));
				}else
				{
					saveActionResponse.setActionComplete(true);
				}
			}
		}catch (SQLException | ParseException e) {
			saveActionResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("REGISTRATION APPROVAL check already exit entry  "+e.getMessage());
			saveActionResponse.setSe(error);
			e.printStackTrace();
		}
		return saveActionResponse;
	}

}
