package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.twd.pandurangsugar.android.bean.CanePlantationResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.PlantLocation;
import com.twd.pandurangsugar.android.bean.PlantationBean;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class CanePlantationDao {


public static synchronized CanePlantationResponse savePlantation(List<PlantationBean> reqList, String chit_boy_id, MainResponse mainresponse, CanePlantationResponse canePlantResponse, Connection conn) {
			// get max plot no
			ArrayList<String>removeEntry=new ArrayList<>();
			DecimalFormat df=new DecimalFormat("#0.00");
			try
			{
				conn.setAutoCommit(false);
				long nplotNo=0l;
				int saveData=0;
				
				try(PreparedStatement pst=conn.prepareStatement("insert into "+ConstantVeriables.tabeAgriPlantaton+"(vyear_id,nentity_uni_id,"
						+ "nplot_no,nhangam_id,vsurve_no,dentry_date,dplantation_date,nirrigation_id,nvariety_id,narea,"
						+ "nvillage_id,ndistance,dcreate_date,nsection_id,nfarmer_type_id,ngps_area,ngps_distance,"
						+ "nconfirm_flag,vphoto_path,ncropwater_condition,vsoil_test,vgreen_fert,nlane_type_id,vbene_treat,"
						+ "vbesal_dose,vdrip_used,nharvest_type_id,nplot_no_offline,nreg_flag_offline,ncreate_user_id,nregn_gps_flag, vstanding_latitue, vstanding_longitude) values(?,?,?,?,?,TO_DATE(?,'dd-Mon-yyyy'),TO_DATE(?,'dd-Mon-yyyy'),?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"))
				{
					for(PlantationBean plantation:reqList)
					{
						if(plantation.getNentryType()==1)
						{
							boolean isExit = false;
							try(PreparedStatement pst1=conn.prepareStatement("select nplot_no FROM "+ConstantVeriables.tabeAgriPlantaton+" WHERE nplot_no_offline=? AND vyear_id=?"))
							{
								pst1.setString(1,plantation.getNplotNoOffline());
								pst1.setString(2,plantation.getVyearId());
								try(ResultSet rs=pst1.executeQuery())
								{
									if(rs.next())
									{
										isExit=true;
										removeEntry.add(plantation.getNplotNoOffline());
									}
								}
							}
							if(!isExit)
							{
								try(PreparedStatement pst1=conn.prepareStatement("select MAX(nplot_no) as nplot_no FROM "+ConstantVeriables.tabeAgriPlantaton+" WHERE vyear_id=?"))
								{
									pst1.setString(1,plantation.getVyearId());
									try(ResultSet rs=pst1.executeQuery())
									{
										if(rs.next())
											nplotNo=rs.getLong("nplot_no")+1;
										else
											nplotNo=nplotNo+1;
									}
								}
								int i=1;
								pst.setString(i++, plantation.getVyearId());
								pst.setString(i++, plantation.getNentityUniId());
								pst.setLong(i++, nplotNo);
								pst.setInt(i++, plantation.getNhangamId());
								pst.setString(i++, plantation.getVsurveNo());
								pst.setString(i++, Constant.AppDateDbDate(plantation.getDentryDate()));
								pst.setString(i++, Constant.AppDateDbDate(plantation.getDplantationDate()));
								pst.setInt(i++, plantation.getNirrigationId());
								pst.setInt(i++, plantation.getNvarietyId());
								pst.setString(i++, df.format(plantation.getNarea()));
								pst.setInt(i++, plantation.getNvillageId());
								pst.setDouble(i++, plantation.getNdistance());
								pst.setInt(i++, plantation.getNsectionId());
								pst.setString(i++, plantation.getNfarmerTypeId());
								pst.setString(i++, df.format(plantation.getNgpsArea()));
								pst.setDouble(i++, plantation.getNgpsDistance());
								pst.setInt(i++, plantation.getNconfirmFlag());
								pst.setString(i++, plantation.getVphotoPath());
								pst.setInt(i++, plantation.getNcropwaterCondition());
								pst.setString(i++, plantation.getVsoilTest());
								pst.setString(i++, plantation.getVgreenFert());
								pst.setInt(i++, plantation.getNlaneTypeId());
								pst.setString(i++, plantation.getVbeneTreat());
								pst.setString(i++, plantation.getVbesalDose());
								pst.setString(i++, plantation.getVdripUsed());
								pst.setInt(i++, plantation.getNharvestTypeId());
								pst.setString(i++, plantation.getNplotNoOffline());
								pst.setInt(i++, plantation.getNregFlagOffline());
								pst.setString(i++, chit_boy_id);
								pst.setInt(i++, plantation.getNgpsFlag());
								pst.setString(i++, plantation.getVstandingLatitude());
								pst.setString(i++, plantation.getVstandinglongitude());
								saveData+=pst.executeUpdate();
								removeEntry.add(plantation.getNplotNoOffline());
								int kk = 1;
								// insert plot details
								try(PreparedStatement pst1=conn.prepareStatement("INSERT INTO APP_T_REGISTRATION_LATLNGS(ntrans_no,NPLOT_NO,VYEAR_ID,VLATITUDE,VLONGITUDE,NACC)VALUES(?,?,?,?,?,?)"))
								{
									for(PlantLocation location:plantation.getPlantLocations())
									{
										int s=1;
										pst1.setInt(s++, kk++);
										pst1.setLong(s++, nplotNo);
										pst1.setString(s++, location.getVyearId());
										pst1.setDouble(s++, location.getVlatitude());
										pst1.setDouble(s++, location.getVlongitude());
										pst1.setDouble(s++, location.getNaccuracy());
										pst1.executeUpdate();
									}
								}
								//nplotNo++;
								conn.commit();
							}
						}
					}
				}
				for(PlantationBean plantation:reqList)
				{
					String sql="update "+ConstantVeriables.tabeConfirmAgriPlantaton+" set nexpected_yield=?,nconfirm_flag=?,vconfirmphoto_path=?"
							+ ",ntentative_area=?,ncropwater_condition=?,nconf_flag_offline=?, vstanding_latitue_cnfrm=?, vstanding_longitude_cnfrm=?, dconfirmation_date=To_Date(?, 'dd/MM/yyyy HH24:mi:ss'), vsurve_no=?,ngps_distance=?";
					if(plantation.getNconfirmFlag()==1)
						sql+=",njune_flag=?";
					else if(plantation.getNconfirmFlag()==2)
						sql+=",naugust_flag=?";
					sql+=",nupdate_user_id=?,dupdate_date=sysdate WHERE nplot_no=? and vyear_id=?";
					try(PreparedStatement pst=conn.prepareStatement(sql))
					{
						int i=1;
						pst.setDouble(i++, plantation.getNexpectedYield());
						pst.setInt(i++, plantation.getNconfirmFlag());
						pst.setString(i++, plantation.getVconfirmphotoPath());
						pst.setString(i++, df.format(plantation.getNtentativeArea()));
						pst.setInt(i++, plantation.getNcropwaterCondition());
						pst.setInt(i++, plantation.getNconfFlagOffline());
						pst.setString(i++, plantation.getVstandingLatitude());
						pst.setString(i++, plantation.getVstandinglongitude());
						pst.setString(i++, plantation.getDentryDate());
						pst.setString(i++, plantation.getVsurveNo());
						pst.setDouble(i++, plantation.getNgpsDistance());
						if(plantation.getNconfirmFlag()==1)
							pst.setInt(i++, plantation.getNjuneFlag());
						else if(plantation.getNconfirmFlag()==2)
							pst.setInt(i++, plantation.getNaugustFlag());
						pst.setString(i++, chit_boy_id);
						pst.setLong(i++, plantation.getNplotNo());
						pst.setString(i++, plantation.getVyearId());
						saveData+=pst.executeUpdate();
						conn.commit();
						removeEntry.add(plantation.getNplotNoOffline());
					}
				}
				if(saveData>0 || removeEntry.size()>0)
				{
					canePlantResponse.setSuccessMessage(ConstantMessage.fromSavedSuccess);
					canePlantResponse.setRemoveEntry(removeEntry);
					canePlantResponse.setSuccess(true);
				}
				
			}catch (Exception e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				canePlantResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg(ConstantMessage.fromSavedFailed+" : "+e.getLocalizedMessage());
				mainresponse.setSe(error);
			}
			finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		return canePlantResponse;
	}
	public static synchronized CanePlantationResponse updatePlantation(PlantationBean plantation , String chit_boy_id,
			MainResponse mainresponse, CanePlantationResponse canePlantResponse, Connection conn) {
		DecimalFormat df=new DecimalFormat("#0.00");
		try
		{
			conn.setAutoCommit(false);
			int saveData=0;
			String sql="update "+ConstantVeriables.tabeConfirmAgriPlantaton+" set nexpected_yield=?,nconfirm_flag=?,vconfirmphoto_path=?"
					+ ",ntentative_area=?,ncropwater_condition=?,nconf_flag_offline=?, vstanding_latitue_cnfrm=?, vstanding_longitude_cnfrm=?, dconfirmation_date=To_Date(?, 'dd/MM/yyyy HH24:mi:ss'), vsurve_no=?,ngps_distance=?";
			if(plantation.getNconfirmFlag()==1)
				sql+=",njune_flag=?";
			else if(plantation.getNconfirmFlag()==2)
				sql+=",naugust_flag=?";
			sql+=",nupdate_user_id=?,dupdate_date=sysdate WHERE nplot_no=? and vyear_id=?";
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				int i=1;
				pst.setDouble(i++, plantation.getNexpectedYield());
				pst.setInt(i++, plantation.getNconfirmFlag());
				pst.setString(i++, plantation.getVconfirmphotoPath());
				pst.setString(i++, df.format(plantation.getNtentativeArea()));
				pst.setInt(i++, plantation.getNcropwaterCondition());
				pst.setInt(i++, plantation.getNconfFlagOffline());
				pst.setString(i++, plantation.getVstandingLatitude());
				pst.setString(i++, plantation.getVstandinglongitude());
				pst.setString(i++, plantation.getDentryDate());
				pst.setString(i++, plantation.getVsurveNo());
				pst.setDouble(i++, plantation.getNgpsDistance());
				if(plantation.getNconfirmFlag()==1)
					pst.setInt(i++, plantation.getNjuneFlag());
				else if(plantation.getNconfirmFlag()==2)
					pst.setInt(i++, plantation.getNaugustFlag());
				pst.setString(i++, chit_boy_id);
				pst.setLong(i++, plantation.getNplotNo());
				pst.setString(i++, plantation.getVyearId());
				
				saveData+=pst.executeUpdate();
				conn.commit();
				if(saveData>0)
				{
					canePlantResponse.setSuccessMessage(ConstantMessage.fromSavedSuccess);
					canePlantResponse.setSuccess(true);
				}
			}
		}catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			canePlantResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg(ConstantMessage.fromSavedFailed+" : "+e.getLocalizedMessage());
			mainresponse.setSe(error);
		}
		finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	return canePlantResponse;

	}
	

}
