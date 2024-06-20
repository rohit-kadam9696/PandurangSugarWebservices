package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.CaneSample;
import com.twd.pandurangsugar.android.bean.CaneSamplePlantationData;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CaneSampleDao {
	public CaneSamplePlantationData caneInfoByPlotAndYearCode(String nplotNo, String yearCode,
			CaneSamplePlantationData caneSampleResponse, boolean isDateCheckFilter, Connection conn) {
		try
		{
			if(isDateCheckFilter)
			{
				try(PreparedStatement pst=conn.prepareStatement("select t.ntrans_no from CR_T_RECOVERY_DETAIL t where t.vyear_id=? and t.nplot_no=? and t.dsample_date=TO_DATE(?,'dd-Mon-yyyy')")){
					pst.setString(1, yearCode);
					pst.setString(2, nplotNo);
					pst.setString(3, Constant.getCurrentDate());
					try(ResultSet rs=pst.executeQuery())
					{
						if(rs.next())
						{
							caneSampleResponse.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(ConstantMessage.ERROR_SampleEntryExit+" "+rs.getString("ntrans_no"));
							caneSampleResponse.setSe(error);
							return caneSampleResponse;
						}
					}
				}
			}
			
				DecimalFormat df=new DecimalFormat("#0.00");
				String sql="select TO_NCHAR(t1.ventity_name_local)as farmerName,TO_NCHAR(t2.village_name_local)as vill_name,TO_NCHAR(t3.vhangam_name)as vhangam_name,TO_NCHAR(t4.vvariety_name) as vvariety_name,t.narea  from CR_T_PLANTATION t,GM_M_ENTITY_MASTER_DETAIL t1,GM_M_VILLAGE_MASTER t2,cr_m_hangam_master t3,cr_m_variety_master t4 where t.nentity_uni_id=t1.nentity_uni_id and t.nvillage_id=t2.nvillage_id and t.nhangam_id=t3.nhangam_id and t.nvariety_id=t4.navariety_id and t.vyear_id=? and t.nplot_no=? ";
				try(PreparedStatement pst=conn.prepareStatement(sql))
				{
					pst.setString(1, yearCode);
					pst.setString(2, nplotNo);
					try(ResultSet rs=pst.executeQuery())
					{
						if(rs.next())
						{
							caneSampleResponse.setYearCode(yearCode);
							caneSampleResponse.setNplotNo(nplotNo);
							caneSampleResponse.setFarmerName(DemoConvert2.ism_to_uni(rs.getString("farmerName")));
							caneSampleResponse.setVilleageName(DemoConvert2.ism_to_uni(rs.getString("vill_name")));
							caneSampleResponse.setHungamName(DemoConvert2.ism_to_uni(rs.getString("vhangam_name")));
							caneSampleResponse.setVarityName(DemoConvert2.ism_to_uni(rs.getString("vvariety_name")));
							caneSampleResponse.setNarea(df.format(rs.getDouble("narea")));
							caneSampleResponse.setSuccess(true);
						}else
						{
							caneSampleResponse.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg(ConstantMessage.informationNotFound);
							caneSampleResponse.setSe(error);
						}
					}
				}
			}catch (Exception e) {
			caneSampleResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Cane Confirmation Load Data Issue "+e.getMessage());
			caneSampleResponse.setSe(error);
			e.printStackTrace();
		}
		return caneSampleResponse;
	}

	public ActionResponse save(CaneSample caneSample, ActionResponse saveResponse, Connection conn) {
	try
		{
		long ntransNo=1l;
			try(PreparedStatement pst=conn.prepareStatement("select MAX(ntrans_no)as ntrans_no from CR_T_RECOVERY_DETAIL t where t.vyear_id=?")){
				pst.setString(1, caneSample.getVyearId());
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						ntransNo=rs.getLong("ntrans_no")+1;
					}
				}
			}
				try(PreparedStatement pst=conn.prepareStatement("INSERT INTO CR_T_RECOVERY_DETAIL(ntrans_no,vyear_id,dsample_date,nplot_no,nbrix,npole,npurity,nrecovery) VALUES(?,?,TO_DATE(?,'dd-Mon-yyyy'),?,?,?,?,?)"))
				{
					int i=1;
					pst.setLong(i++, ntransNo);
					pst.setString(i++, caneSample.getVyearId());
					pst.setString(i++, Constant.getCurrentDate());
					pst.setString(i++, caneSample.getNplotNo());
					pst.setString(i++, caneSample.getNbrix());
					pst.setString(i++, caneSample.getNpole());
					pst.setString(i++, caneSample.getNpurity());
					pst.setString(i++, caneSample.getNrecovery());
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
			}catch (Exception e) {
				saveResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Cane Sample Save "+e.getMessage());
			saveResponse.setSe(error);
			e.printStackTrace();
		}
		return saveResponse;
	}

	public CaneSamplePlantationData caneSampleInfoByPlotAndYearCode(String nplotNo, String yearCode,
			CaneSamplePlantationData caneSampleResponse, Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select NTRANS_NO,VYEAR_ID,DSAMPLE_DATE,NPLOT_NO,NBRIX,NPOLE,NPURITY,NRECOVERY from CR_T_RECOVERY_DETAIL t where t.vyear_id=? and t.nplot_no=?")){
			pst.setString(1, yearCode);
			pst.setString(2, nplotNo);
			try(ResultSet rs=pst.executeQuery())
			{
				List<CaneSample> caneSampleList=new ArrayList<>();
				while(rs.next())
				{
					CaneSample cs=new  CaneSample();
					cs.setNbrix(rs.getString("NBRIX"));
					cs.setNplotNo(rs.getString("NPLOT_NO"));
					cs.setNpole(rs.getString("NPOLE"));
					cs.setNpurity(rs.getString("NPURITY"));
					cs.setNrecovery(rs.getString("NRECOVERY"));
					cs.setSampleDate(Constant.DbDateToAppDate(rs.getDate("DSAMPLE_DATE")));
					cs.setVyearId(yearCode);
					caneSampleList.add(cs);
				}
				caneSampleResponse.setCaneSampleList(caneSampleList);
			}
		} catch (Exception e) {
			caneSampleResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Cane Sample Report "+e.getMessage());
			caneSampleResponse.setSe(error);
			e.printStackTrace();
		}
		return caneSampleResponse;
	}
}
