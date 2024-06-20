package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.CaneHarvestingPlanReasonResponse;
import com.twd.pandurangsugar.android.bean.CaneHarvestingPlanStart;
import com.twd.pandurangsugar.android.bean.GutKhadeResponse;
import com.twd.pandurangsugar.android.bean.HarvestingPlanFarmerResponse;
import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.TableReportBean;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CaneHarvestingPlanDao {

	public CaneHarvestingPlanStart findHarvestingPlanStartByYearCode(String yearCode,
			CaneHarvestingPlanStart canePlanStartResponse, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.vyear_id,t.dentry_date,t.ndays_permit,t.nlimit_tonnage, t.nlimit_tonnage_extra from APP_M_HARVESTING_PROGRAM_START t where t.vyear_id=?"))
		{
			pst.setString(1, yearCode);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					canePlanStartResponse.setNdaysPermit(rs.getInt("ndays_permit"));
					canePlanStartResponse.setDentryDate(Constant.DbDateToAppDate(rs.getDate("dentry_date")));
					canePlanStartResponse.setNlimitTonnage(Constant.decimalFormat(rs.getDouble("nlimit_tonnage"), "00"));
					canePlanStartResponse.setNlimitTonnageExtra(Constant.decimalFormat(rs.getDouble("nlimit_tonnage_extra"), "00"));
				}
				canePlanStartResponse.setSuccess(true);
				String year[] = yearCode.split("-");
				canePlanStartResponse.setDstartDate("01/06/"+year[0]);
				canePlanStartResponse.setDendDate("31/05/"+year[1]);
			}
		}catch (Exception e) {
			e.printStackTrace();
			canePlanStartResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("find harvesting Plan Start  : "+e.getLocalizedMessage());
			canePlanStartResponse.setSe(error);
		}
		return canePlanStartResponse;
	}

	public ActionResponse saveHarvestingPlanStart(String yearCode, String dentryDate, String ndaysPermit, String nlimitTonnage, String nlimitTonnageExtra, ActionResponse saveResponse, Connection conn) {
		try{
				boolean isEntryExit=false; 
				try(PreparedStatement pst=conn.prepareStatement("select t.vyear_id from APP_M_HARVESTING_PROGRAM_START t where t.vyear_id=?")){
					pst.setString(1, yearCode);
					try(ResultSet rs=pst.executeQuery())
					{
						if(rs.next())
						{
							isEntryExit=true;
						}
					}
				}
				int res=0;
				if(isEntryExit)
				{
					try(PreparedStatement pst=conn.prepareStatement("UPDATE APP_M_HARVESTING_PROGRAM_START SET dentry_date=TO_DATE(?,'dd-Mon-yyyy'),ndays_permit=?,nlimit_tonnage=?, nlimit_tonnage_extra=? WHERE vyear_id=?"))
					{
						int i=1;
						pst.setString(i++, Constant.AppDateDbDate(dentryDate));
						pst.setString(i++, ndaysPermit);
						pst.setString(i++, nlimitTonnage);
						pst.setString(i++, nlimitTonnageExtra);
						pst.setString(i++, yearCode);
						res=pst.executeUpdate(); 
					}
				}else
				{
					try(PreparedStatement pst=conn.prepareStatement("INSERT INTO APP_M_HARVESTING_PROGRAM_START(vyear_id,dentry_date,ndays_permit,nlimit_tonnage,nlimit_tonnage_extra) VALUES(?,TO_DATE(?,'dd-Mon-yyyy'),?,?,?)"))
					{
						int i=1;
						pst.setString(i++, yearCode);
						pst.setString(i++, Constant.AppDateDbDate(dentryDate));
						pst.setString(i++, ndaysPermit);
						pst.setString(i++, nlimitTonnage);
						pst.setString(i++, nlimitTonnageExtra);
						res=pst.executeUpdate();
					}
				}
					
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
				}catch (Exception e) {
					saveResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Harvesting Plan Start Save "+e.getMessage());
					saveResponse.setSe(error);
					e.printStackTrace();
			}
			return saveResponse;
	}

	public ActionResponse saveHarvestingPlanDate(String yearCode, String dplantFromDate, String dplantToDate,
			String nhangam, String nvariety, ActionResponse saveResponse, Connection conn) {
		try{
			int res=0;
				try(PreparedStatement pst=conn.prepareStatement("INSERT INTO APP_M_HARVESTING_PROGRAM(vyear_id,dplant_from_date,dplant_to_date,nhangam,nvariety,nentry_user_id) VALUES(?,TO_DATE(?,'dd-Mon-yyyy'),TO_DATE(?,'dd-Mon-yyyy'),?,?,?)"))
				{
					int vlen=nvariety.split(",").length;
					int hlen=nhangam.split(",").length;
					String varity[]=nvariety.split(",");
					String hangam[]=nhangam.split(",");
					for(int a=0;a<hlen;a++)
					{
						for(int s=0;s<vlen;s++)
						{
							int i=1;
							pst.setString(i++, yearCode);
							pst.setString(i++, Constant.AppDateDbDate(dplantFromDate));
							pst.setString(i++, Constant.AppDateDbDate(dplantToDate));
							pst.setString(i++, hangam[a]);
							pst.setString(i++, varity[s]);
							pst.setString(i++, saveResponse.getSlipboycode());
							res+=pst.executeUpdate();
						}
					}
					
				}
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
			}catch (SQLIntegrityConstraintViolationException e) {
				saveResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				if(e.getMessage().contains("EXACT_DUP_ENTRY"))
					error.setMsg(ConstantMessage.caneTodniProgramAlreadyExit);
				else
					error.setMsg("Harvesting Plan By Date Save "+e.getMessage());
				saveResponse.setSe(error);
			}
			catch (Exception e) {
				saveResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Harvesting Plan By Date Save "+e.getMessage());
				saveResponse.setSe(error);
				e.printStackTrace();
			}
		return saveResponse;
	}
	public HarvestingPlanFarmerResponse farmerByCode(String farmerCode, String yearCode,
			HarvestingPlanFarmerResponse farmerResponse, Connection conn) {
		try{
			try(PreparedStatement pst=conn.prepareStatement("select t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as ventity_name_local from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_uni_id=?"))
			{
				pst.setString(1, farmerCode);
				try(ResultSet rs=pst.executeQuery()){
					if(rs.next())
					{
						farmerResponse.setFarmerCode(farmerCode);
						farmerResponse.setFarmerName(rs.getString("ventity_name_local")!=null?DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")):"NO Name");
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select t.nplot_no from CR_T_PLANTATION t where t.vyear_id=? and t.nentity_uni_id=?"))
			{
				pst.setString(1, yearCode);
				pst.setString(2, farmerCode);
				ArrayList<Long> list=new ArrayList<>();
				try(ResultSet rs=pst.executeQuery()){
					while(rs.next())
					{
						list.add(rs.getLong("nplot_no"));
					}
					farmerResponse.setPlotList(list);
				}
			}
		}catch (Exception e) {
				farmerResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Cane Harvesting Farmer By Code "+e.getMessage());
				farmerResponse.setSe(error);
				e.printStackTrace();
			}
		return farmerResponse;
	}

	public CaneHarvestingPlanReasonResponse ressonByGroupCode(int ngroupCode, CaneHarvestingPlanReasonResponse canePlanResonResponse,
			Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.nreason_id,TO_NCHAR(t.vreason_name) as vreason_name from APP_M_REASON t where t.nreason_block='N' and t.nreason_group_id=?"))
			{
				pst.setInt(1, ngroupCode);
				List<KeyPairBoolData> list=new ArrayList<>();
				try(ResultSet rs=pst.executeQuery()){
					while(rs.next())
					{
						KeyPairBoolData data=new KeyPairBoolData();
						data.setId(rs.getInt("nreason_id"));
						data.setName(rs.getString("vreason_name")!=null?DemoConvert2.ism_to_uni(rs.getString("vreason_name")):"NO Name");
						list.add(data);
					}
					canePlanResonResponse.setReasonList(list);
					
				}
		}catch (Exception e) {
				canePlanResonResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Cane Harvesting Farmer By Code "+e.getMessage());
				canePlanResonResponse.setSe(error);
				e.printStackTrace();
			}
		return canePlanResonResponse;
	}

	public ActionResponse saveHarvestingSpecialPlan(String yearCode, String farmerCode, String nplotNo,
			String nreasonId,String userId, ActionResponse saveResponse, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("INSERT INTO APP_M_HARVESTING_PROGRAM_SPL(vyear_id,nentity_uni_id,nplot_no,nreason_id,nentry_user_id) VALUES(?,?,?,?,?)"))
		{
			int i=1;
			pst.setString(i++, yearCode);
			pst.setString(i++, farmerCode);
			pst.setString(i++, nplotNo);
			pst.setString(i++, nreasonId);
			pst.setString(i++, userId);
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
		}catch (SQLIntegrityConstraintViolationException e) {
			saveResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			if(e.getMessage().contains("EXACT_DUP_ENTRY"))
				error.setMsg(ConstantMessage.caneSpecialTodniProgramAlreadyExit);
			else
				error.setMsg("Harvesting Plan By Date Save "+e.getMessage());
			saveResponse.setSe(error);
		}catch (Exception e) {
			saveResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Save Special Harvesting Plan "+e.getMessage());
			saveResponse.setSe(error);
			e.printStackTrace();
		}
		return saveResponse;
	}

	public int slipCount(String yearCode, String farmerCode, String nplotNo, Connection conn) {
		int slipCount=0;
		try(PreparedStatement pst=conn.prepareStatement("select count(t.nslip_no)as nslip_no from WB_T_WEIGHT_SLIP t where t.nplot_no =? and t.nentity_uni_id=? and t.vyear_id=?"))
		{
			int i=1;
			pst.setString(i++, nplotNo);
			pst.setString(i++, farmerCode);
			pst.setString(i++, yearCode);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					slipCount=rs.getInt("nslip_no");
				}
			}
						
		}catch (Exception e) {
			slipCount=0;
			e.printStackTrace();
		}
		return slipCount;
	}

	public int updateHarvestingFlag(String yearCode, String farmerCode, String nplotNo, Connection conn) {
		int updateFlag=0;
		try(PreparedStatement pst=conn.prepareStatement("update CR_T_PLANTATION t set t.nharvested_flag=0 where t.nharvested_flag=1 and t.nplot_no=? and t.nentity_uni_id=? and t.vyear_id=?"))
		{
			int i=1;
			pst.setString(i++, nplotNo);
			pst.setString(i++, farmerCode);
			pst.setString(i++, yearCode);
			updateFlag=pst.executeUpdate();
		}catch (Exception e) {
			updateFlag=0;
			e.printStackTrace();
		}
		return updateFlag;
	}

	public ActionResponse saveGutKhade(GutKhadeResponse saveReq, String chit_boy_id, ActionResponse saveRes,
			Connection conn) {
		try {
			conn.setAutoCommit(false);
			String sql="INSERT INTO APP_M_GUTKHADE(vyear_id,nvehicle_type_id,dfrom_date,vtime_start,dto_date,vtime_end,nsection_id,nentry_user_id,dentry_date,ntrans_id) values (?,?,TO_DATE(?,'dd/MM/yyyy'),TO_DATE(?,'dd/MM/yyyy hh24:mi'),TO_DATE(?,'dd/MM/yyyy'),TO_DATE(?,'dd/MM/yyyy hh24:mi'),?,?,SYSDATE,?)";
			if(saveReq.getTransId()!=null && !saveReq.getTransId().equals("0"))
				sql=" UPDATE APP_M_GUTKHADE SET vyear_id=?,nvehicle_type_id=?,dfrom_date=TO_DATE(?,'dd/MM/yyyy'),vtime_start=TO_DATE(?,'dd/MM/yyyy hh24:mi'),dto_date=TO_DATE(?,'dd/MM/yyyy'),vtime_end=TO_DATE(?,'dd/MM/yyyy hh24:mi'),nsection_id=?,nupdate_user_id=?,dupdate_date=SYSDATE WHERE ntrans_id=?";
			else
			{
				try(PreparedStatement pst=conn.prepareStatement("SELECT nvl(MAX(ntrans_id),0)+1 as pkid  FROM APP_M_GUTKHADE WHERE vyear_id=?")){
					pst.setString(1, saveReq.getYearId()==null?"":saveReq.getYearId());
					try(ResultSet rs=pst.executeQuery())
					{
						if(rs.next())
						{
							saveReq.setTransId(rs.getString("pkid"));
						}
					}
				} catch (SQLException e) {
					saveRes.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("save Cane Trans " + e.getMessage());
					saveRes.setSe(error);
					e.printStackTrace();
				}
			}
			String vehicalType[] =  saveReq.getVehicleTypeId()==null?new String[]{""}:saveReq.getVehicleTypeId().split(",");
			int vehicalTypeSize = vehicalType.length; 
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				int res = 0;
				for(int j = 0; j< vehicalTypeSize; j++) {
					int i=1;
					pst.setString(i++, saveReq.getYearId()==null?"":saveReq.getYearId());
					pst.setString(i++, vehicalType[j]);
					pst.setString(i++, saveReq.getFromDate()==null?"":(saveReq.getFromDate().substring(0, 10)));
					pst.setString(i++, saveReq.getFromDate()==null?"":saveReq.getFromDate());
					pst.setString(i++, saveReq.getToDate()==null?"":(saveReq.getToDate().substring(0, 10)));
					pst.setString(i++, saveReq.getToDate()==null?"":saveReq.getToDate());
					pst.setString(i++, saveReq.getSectionId());
					pst.setString(i++, chit_boy_id);
					pst.setInt(i++, Integer.parseInt(saveReq.getTransId())+j);
					res+=pst.executeUpdate();
				}
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
			}
			conn.commit();
		} catch (Exception e) {
			saveRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("save Cane Trans " + e.getMessage());
			saveRes.setSe(error);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
	public TableResponse gutkhadeList(TableResponse gutListRes, String dateVal, String yearId, String sectionId,
			TableResponse gutListRes2, Connection conn) {

		try {
			TableReportBean gutKhadeTableBean = new TableReportBean();
			int noofheadDailyCrush =1;
			gutKhadeTableBean.setNoofHeads(noofheadDailyCrush);
			gutKhadeTableBean.setFooter(false);
			gutKhadeTableBean.setMarathi(false);
			
			HashMap<String, Boolean> boldIndicator = new HashMap<>();
			
			ArrayList<ArrayList<String>> varietyTonnageTableData = new ArrayList<>();
			ArrayList<String> gutKhadeRow = new ArrayList<>();
			gutKhadeRow.add(ConstantVeriables.srno);
			gutKhadeRow.add(ConstantVeriables.fdate);
			gutKhadeRow.add(ConstantVeriables.tdate);
			gutKhadeRow.add(ConstantVeriables.vehicalType);
			gutKhadeRow.add(ConstantVeriables.section);
			gutKhadeRow.add(ConstantVeriables.status);
			gutKhadeRow.add("");
			varietyTonnageTableData.add(gutKhadeRow);
			
			boldIndicator.put("0-*", true);
			int srno = 1;
			String sql="select t.vyear_id,t.ntrans_id,t.nvehicle_type_id,to_char(t.vtime_start,'dd/MM/yyyy hh24:mi') as from_date,to_char(t.vtime_end,'dd/MM/yyyy hh24:mi') as to_date,t.nsection_id,t.vactive,TO_NCHAR(t1.vvehicle_type_name_local)as vehicle_type,TO_NCHAR(t2.vsection_name_local)as section_name from APP_M_GUTKHADE t,gm_m_vehicle_type_master t1,gm_m_section_master t2 where t.nvehicle_type_id=t1.nvehicle_type_id and t.nsection_id=t2.nsection_id and t.vyear_id=? and t.dfrom_date<=TO_DATE(?,'dd/MM/yyyy') and t.vtime_end>=TO_DATE(?,'dd/MM/yyyy')";
			if(sectionId!=null && !sectionId.trim().isEmpty())
			sql+=" and t.nsection_id=?";
			sql+=" order by ntrans_id ASC ";
			try(PreparedStatement pst=conn.prepareStatement(sql)){
				pst.setString(1, yearId);
				pst.setString(2, dateVal);
				pst.setString(3, dateVal);
				if(sectionId!=null && !sectionId.trim().isEmpty())
					pst.setString(4, sectionId);
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						gutKhadeRow = new ArrayList<>();
						gutKhadeRow.add(String.valueOf(srno++));
						gutKhadeRow.add(rs.getString("from_date"));
						gutKhadeRow.add(rs.getString("to_date"));
						gutKhadeRow.add(DemoConvert2.ism_to_uni(rs.getString("vehicle_type")));
						gutKhadeRow.add(DemoConvert2.ism_to_uni(rs.getString("section_name")));
						gutKhadeRow.add(rs.getString("vactive").equalsIgnoreCase("A")?ConstantVeriables.active:ConstantVeriables.deactive);
						gutKhadeRow.add(rs.getString("ntrans_id"));
						varietyTonnageTableData.add(gutKhadeRow);
					}
				}
			}
			gutKhadeTableBean.setTableData(varietyTonnageTableData);
			
			gutListRes.setTableData(gutKhadeTableBean);
		} catch (Exception e) {
			gutListRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("gut Khade List Error " + e.getMessage());
			gutListRes.setSe(error);
			e.printStackTrace();
		}
		return gutListRes;
	}

	public GutKhadeResponse editGutKhade(String yearId, String transId,
			GutKhadeResponse gutRes, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("SELECT nvehicle_type_id,to_char(vtime_start,'dd/MM/yyyy hh24:mi') as fdate,to_char(vtime_end,'dd/MM/yyyy hh24:mi') as tdate,nsection_id,ntrans_id from APP_M_GUTKHADE WHERE vyear_id=? and ntrans_id=?")){
			pst.setString(1, yearId);
			pst.setString(2, transId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) 
				{
					gutRes.setFromDate(rs.getString("fdate"));
					gutRes.setToDate(rs.getString("tdate"));
					gutRes.setVehicleTypeId(rs.getString("nvehicle_type_id"));
					gutRes.setSectionId(rs.getString("nsection_id"));
					gutRes.setTransId(rs.getString("ntrans_id"));
				}
			}
		}catch (Exception e) {
			gutRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("gut Khade List Error " + e.getMessage());
			gutRes.setSe(error);
			e.printStackTrace();
		}
		return gutRes;
	}

	public ActionResponse deleteGutKhade(String yearId, String transId, String chit_boy_id, ActionResponse saveResponse,
			Connection conn) {
		String vactive="A";
		try(PreparedStatement pst=conn.prepareStatement("SELECT vactive from APP_M_GUTKHADE WHERE vyear_id=? and ntrans_id=?")){
			pst.setString(1, yearId);
			pst.setString(2, transId);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) 
				{
					vactive = rs.getString("vactive");
					if (vactive.equalsIgnoreCase("A"))
						vactive = "D";
					else 
						vactive = "A";
					try(PreparedStatement pst1=conn.prepareStatement("update APP_M_GUTKHADE set vactive=?,nupdate_user_id=?, dupdate_date=sysdate WHERE vyear_id=? and ntrans_id=?"))
					{
						pst1.setString(1, vactive);
						pst1.setString(2, chit_boy_id);
						pst1.setString(3, yearId);
						pst1.setString(4, transId);
						int res = pst1.executeUpdate();
						if (res > 0) {
							saveResponse.setActionComplete(true);
							saveResponse.setSuccessMsg(ConstantMessage.saveSuccess);
							if (vactive.equals("A")) {
								saveResponse.setNewStatus(ConstantVeriables.active);
							} else {
								saveResponse.setNewStatus(ConstantVeriables.deactive);
							}
						} else {
							saveResponse.setActionComplete(false);
							saveResponse.setFailMsg(ConstantMessage.saveFailed);
						}
					}
				}
			}
		}catch (Exception e) {
			saveResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("delete gut Khade Error " + e.getMessage());
			saveResponse.setSe(error);
			e.printStackTrace();
		}
		return saveResponse;
	}

	

	
}
	
