package com.twd.pandurangsugar.android.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SlipBeanR;
import com.twd.pandurangsugar.android.bean.WeightSlipResponse;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantMessage;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;
import com.twd.pandurangsugar.both.constant.QRConstant;

public class SaveHarvestingDao {

	public static synchronized WeightSlipResponse saveWeightSlip(JSONObject reqObj, WeightSlipResponse savewsRes, Connection conn) {
		try
		{
			int plotNo=reqObj.has("nplotNo")?reqObj.getInt("nplotNo"):0;
			String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
			String nentityUniId=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):null;
			int nfarmerTypeId=reqObj.has("nfarmerTypeId")?reqObj.getInt("nfarmerTypeId"):0;
			int nsectionId=reqObj.has("nsectionId")?reqObj.getInt("nsectionId"):0;
			int nvillageId=reqObj.has("nvillageId")?reqObj.getInt("nvillageId"):0;
			long ncropId=reqObj.has("ncropId")?reqObj.getInt("ncropId"):0;
			int nvarietyId=reqObj.has("nvarietyId")?reqObj.getInt("nvarietyId"):0;
			String ndistancesave=reqObj.has("ndistancesave")?reqObj.getString("ndistancesave"):"";
			String vehicleType=reqObj.has("vehicleType")?reqObj.getString("vehicleType"):null;
			String nharvestorId=reqObj.has("nharvestorId")?reqObj.getString("nharvestorId"):null;
			String ntransportorId=reqObj.has("ntransportorId")?reqObj.getString("ntransportorId"):null;
			String vvehicleNo=reqObj.has("vvehicleNo")?reqObj.getString("vvehicleNo"):"";
			String ntailorFront=reqObj.has("ntailorFront")?String.valueOf(reqObj.getLong("ntailorFront")):null;
			String ntailorBack=reqObj.has("ntailorBack")?String.valueOf(reqObj.getLong("ntailorBack")):null;
			String nwireropeNo=reqObj.has("nwireropeNo")?String.valueOf(reqObj.getLong("nwireropeNo")):null;
			if(nwireropeNo==null && vehicleType!=null && vehicleType.equals("3"))
				nwireropeNo = "1";
			String nbulluckcartMainId=reqObj.has("nbulluckcartMainId")?reqObj.getString("nbulluckcartMainId"):null;
			String nslipboyId=reqObj.has("nslipboyId")?reqObj.getString("nslipboyId"):null;
			String ngadiDokiId=reqObj.has("ngadiDokiId")?reqObj.getString("ngadiDokiId"):null;
			if (vehicleType.equals("4"))
				ngadiDokiId = "3";
			else if (vehicleType.equals("3"))
				ngadiDokiId = "5";
			String tokenno=reqObj.has("tokenno")?reqObj.getString("tokenno"):null;
			int nfactId=reqObj.has("nfactId")?reqObj.getInt("nfactId"):0;
			int nhangamId=reqObj.has("nhangamId")?reqObj.getInt("nhangamId"):0;
			long nremarkId=reqObj.has("nremarkId")?reqObj.getLong("nremarkId"):0;
			String nseqWeight=reqObj.has("nseqWeight")?reqObj.getString("nseqWeight"):null;
			JSONArray bulluckdata=reqObj.has("bulluckdata")?reqObj.getJSONArray("bulluckdata"):null;
			String nslipNo=reqObj.has("nslipNo")?reqObj.getString("nslipNo"):null;
			String slipDate=reqObj.has("slipDate")?reqObj.getString("slipDate"):null;
			String userDistance=reqObj.has("userDistance")?reqObj.getString("userDistance"):null;
			long slipno=0;
			int count=0;
			boolean isEntryExit=false;
			List<SlipBeanR> list=new ArrayList<>();
			
			if(userDistance!=null && userDistance.equals("1")) {
				try(PreparedStatement pst=conn.prepareStatement("UPDATE "+ConstantVeriables.tabeAgriPlantaton+" cr set cr.ndistance=? where cr.nplot_no=? and cr.vyear_id=?")){
					int i=1;
					pst.setString(i++, ndistancesave);
					pst.setInt(i++, plotNo);
					pst.setString(i++, yearCode);
					pst.executeUpdate();
				}
			}
			
			if(nslipNo!=null && !nslipNo.trim().isEmpty())
			{
				int blistsize=1;
				int cnt=0;
				
				try(PreparedStatement pst=conn.prepareStatement("UPDATE "+ConstantVeriables.weightSlipTableName+" t SET t.nvehicle_type_id=?,t.ngadi_doki_id=?,t.nharvestor_id=?,t.ntransportor_id=?,t.vvehicle_no=?,t.ntailor_front=?,t.ntailor_back=?,t.nwirerope_no=?,t.nbulluckcart_id=?,t.nbulluckcart_main_id=?,t.ndistance=?,t.ncrop_id=?,t.nremark_id=?,t.nslip_seq=? WHERE t.nslip_no=? and t.vyear_id=?")){
					if(bulluckdata!=null && bulluckdata.length()>0)
						blistsize=bulluckdata.length();
					for(int s=0;s<blistsize;s++)
					{
						String bullockCode=null;
						if(bulluckdata!=null && bulluckdata.length()>0)
						{
							JSONObject bullock=bulluckdata.getJSONObject(s);
							JSONObject dtlObj=bullock.has("object")?new JSONObject((String)bullock.get("object")):new JSONObject();
							bullockCode=dtlObj.has("code")?dtlObj.getString("code"):"";
							vvehicleNo = dtlObj.has("vehicleNo")?dtlObj.getString("vehicleNo"):"";
						}
							int i=1;
							pst.setString(i++, vehicleType);
							pst.setString(i++, ngadiDokiId); //NGADI_DOKI_ID
							pst.setString(i++, nharvestorId); 
							pst.setString(i++, ntransportorId); 
							pst.setString(i++, vvehicleNo); 
							pst.setString(i++, ntailorFront); 
							pst.setString(i++, ntailorBack); 
							pst.setString(i++, nwireropeNo); 
							pst.setString(i++, bullockCode);
							pst.setString(i++, nbulluckcartMainId);
							pst.setString(i++, ndistancesave);
							pst.setLong(i++, ncropId);
							pst.setLong(i++, nremarkId);
							pst.setString(i++, nseqWeight);
							pst.setString(i++, nslipNo);
							pst.setString(i++, yearCode);
							cnt=pst.executeUpdate();
							/*SlipBeanR slipres=new SlipBeanR();
							slipres.setNslipNo(nslipNo);
							slipres.setNbullokCode(bullockCode);
							list.add(slipres);*/
							
							try(PreparedStatement pst11=conn.prepareStatement("select t.nslip_no,t.dslip_date_slipboy,t.nbulluckcart_id from "+ConstantVeriables.weightSlipTableName+" t where t.vyear_id=? and t.nslip_no=?"))
							{
								pst11.setString(1, yearCode);
								pst11.setString(2, nslipNo);
								try(ResultSet rs11=pst11.executeQuery())
								{
									if(rs11.next())
									{
										SlipBeanR slipres=new SlipBeanR();
										slipres.setNslipNo(rs11.getString("nslip_no"));
										slipres.setNbullokCode(rs11.getString("nbulluckcart_id"));
										slipres.setExtraQr(ConstantVeriables.lblExtraQR);
										slipres.setSlipDate(Constant.DbDateTimeToAppDateTime(rs11.getTimestamp("dslip_date_slipboy")));
										list.add(slipres);
									}
								}
							}
					}
					if(cnt>0 || list.size()>0)
					{
						savewsRes.setActionComplete(true);
						if(cnt>0) 
						{
							savewsRes.setSuccessMsg(ConstantMessage.saveSuccess);
						}
						else 
							savewsRes.setSuccessMsg(ConstantMessage.saveFailed);
						savewsRes.setDate(slipDate);
						savewsRes.setSlipBeanrList(list);
					}else
					{
						savewsRes.setActionComplete(false);
						savewsRes.setFailMsg(ConstantMessage.saveFailed);
					}
					savewsRes.setSuccess(true);
				}
				return savewsRes;
			}
			try(PreparedStatement pst=conn.prepareStatement("select t.NSLIP_NO,t.nbulluckcart_id,t.dslip_date_slipboy from "+ConstantVeriables.weightSlipTableName+" t where t.nslip_no_offline=? and t.vyear_id=?")){
				int i=1;
				pst.setString(i++, tokenno);
				pst.setString(i++, yearCode);
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next()) {
						isEntryExit=true;
						SlipBeanR slipres=new SlipBeanR();
						slipres.setNslipNo(rs.getString("NSLIP_NO"));
						slipres.setNbullokCode(rs.getString("nbulluckcart_id"));
						slipres.setSlipDate(Constant.DbDateTimeToAppDateTime(rs.getTimestamp("dslip_date_slipboy")));
						list.add(slipres);
					}
				}
			}
			if(!isEntryExit)
			{
				int slipCount=0;
				/*try(PreparedStatement pst=conn.prepareStatement("select MAX(case when t.nslip_no like ? then t.NSLIP_NO end ) as slip,COUNT(t.NSLIP_NO) as slipcnt from "+ConstantVeriables.weightSlipTableName+" t where t.nplot_no=? and t.vyear_id=?")){
					int i=1;
					pst.setString(i++, plotNo+"___");
					pst.setInt(i++, plotNo);
					pst.setString(i++, yearCode);
					try(ResultSet rs=pst.executeQuery())
					{
						if(rs.next()) {
							if(rs.getLong("slip")!=0)
							{
								slipno=rs.getLong("slip")+1;
								slipCount=rs.getInt("slipcnt")+1;
							}
							else
							{
								slipno=Long.parseLong(""+(plotNo+"001"));
								slipCount=1;
							}
						}
						else
						{
							slipno=Long.parseLong(""+(plotNo+"001"));
							slipCount=1;
						}
					}
				}*/
				try(PreparedStatement pst=conn.prepareStatement("select COUNT(t.NSLIP_NO) as slipcnt from "+ConstantVeriables.weightSlipTableName+" t where t.nplot_no=? and t.vyear_id=?")){
					int i=1;
					pst.setInt(i++, plotNo);
					pst.setString(i++, yearCode);
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							slipCount = rs.getInt("slipcnt") + 1;
						} else {
							slipCount = 1;
						}
					}
				}
				String vercodeseq = yearCode.replaceAll("-", "");
				try (PreparedStatement pst = conn.prepareStatement("SELECT COUNT(*) as seq FROM user_sequences WHERE sequence_name = 'SEQ_TWD_SLIP" + vercodeseq + "'")) {
					try (ResultSet rs = pst.executeQuery()) {
						if (rs.next()) {
							if (rs.getInt("seq") == 0) {
								try (PreparedStatement pst1 = conn.prepareStatement("CREATE SEQUENCE SEQ_TWD_SLIP" + vercodeseq + " START WITH 10000000 INCREMENT BY 1 NOCACHE NOCYCLE")) {
									pst1.execute();
								}
							}
						}
					}
				}
				while(true) {
					try (PreparedStatement pst = conn.prepareStatement("Select SEQ_TWD_SLIP" + vercodeseq + ".Nextval as FIELDSLIP_NO from dual")) {

						try (ResultSet rs = pst.executeQuery()) {
							if (rs.next()) {
								slipno = rs.getLong("FIELDSLIP_NO");
							}
						}
					}
					try(PreparedStatement pst=conn.prepareStatement("select t.NSLIP_NO from "+ConstantVeriables.weightSlipTableName+" t where t.NSLIP_NO=? and t.vyear_id=?")){
						int i=1;
						pst.setLong(i++, slipno);
						pst.setString(i++, yearCode);
						try(ResultSet rs=pst.executeQuery())
						{
							if(rs.next()) {
								//slipno++;
							}
							else
							{
								break;
							}
						}
					}
				}
				int blistsize=1;
				try(PreparedStatement pst=conn.prepareStatement("INSERT INTO "+ConstantVeriables.weightSlipTableName+"(VYEAR_ID,"
						+ "NSLIP_NO,NSHIFT_ID,DSLIP_DATE,NPLOT_NO,NENTITY_UNI_ID,NFARMER_TYPE_ID,NSECTION_ID,"
						+ "NVILLAGE_ID,NCROP_ID,NVARIETY_ID,NDISTANCE,NVEHICLE_TYPE_ID,NGADI_DOKI_ID,NHARVESTOR_ID,"
						+ "NTRANSPORTOR_ID,VVEHICLE_NO,NTAILOR_FRONT,NTAILOR_BACK,NWIREROPE_NO,NBULLUCKCART_ID,"
						+ "NBULLUCKCART_MAIN_ID,NSLIPBOY_ID,NFACT_ID,NHANGAM_ID,NREMARK_ID,nslip_no_offline,dslip_date_slipboy,NUWS_CARD_ID,NSLIP_SEQ,NSEQ_WEIGHT,vcreate_user_id,vqr_photo_path,vqr_string,cqr_image_wt_slip) VALUES(?,?,?,trunc(sysdate),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?)")){
					if(bulluckdata!=null && bulluckdata.length()>0)
						blistsize=bulluckdata.length();
					for(int s=0;s<blistsize;s++)
					{
						String bullockCode=null;
						if(bulluckdata!=null && bulluckdata.length()>0)
						{
							JSONObject bullock=bulluckdata.getJSONObject(s);
							JSONObject dtlObj=bullock.has("object")?new JSONObject((String)bullock.get("object")):new JSONObject();
							bullockCode=dtlObj.has("code")?dtlObj.getString("code"):"";
							vvehicleNo = dtlObj.has("vehicleNo")?dtlObj.getString("vehicleNo"):"";
						}
						//NUWS_CARD_ID
						String nuwsCardId=null;
						if((ntailorFront!=null && ntailorFront.equals("1")) && (ntailorBack!=null && ntailorBack.equals("1")))
						{
							try(PreparedStatement pst1=conn.prepareStatement("select u.ncard_id from GM_M_ENTITY_MASTER_DETAIL t, GM_M_SEASON_YEAR_AGRI a, HT_M_UHF_CARD_MASTER u where t.nentity_uni_id = u.ntransportor_id and u.vyear_id = a.vseason_year and (t.nentity_uni_id = ? OR t.nentity_uni_id = ?) and u.vstatus = 'A' and a.vactive_wb = 'Y' order by t.ventity_name_local")){
								int i=1;
								pst1.setString(i++, ntransportorId);
								pst1.setString(i++, bullockCode);
								try(ResultSet rs1=pst1.executeQuery())
								{
									if(rs1.next()) {
										nuwsCardId=rs1.getString("ncard_id");
									}
								}
							}
						}
						int i=1;
						pst.setString(i++, yearCode);
						pst.setLong(i++, slipno);
						pst.setInt(i++, 1);
						pst.setInt(i++, plotNo);
						pst.setString(i++, nentityUniId);
						pst.setInt(i++, nfarmerTypeId);
						pst.setInt(i++, nsectionId);
						pst.setInt(i++, nvillageId);
						pst.setLong(i++, ncropId);
						pst.setInt(i++, nvarietyId);
						pst.setString(i++, ndistancesave);
						pst.setString(i++, vehicleType);
						pst.setString(i++, ngadiDokiId); //NGADI_DOKI_ID
						pst.setString(i++, nharvestorId); 
						pst.setString(i++, ntransportorId); 
						pst.setString(i++, vvehicleNo); 
						pst.setString(i++, ntailorFront); 
						pst.setString(i++, ntailorBack); 
						pst.setString(i++, nwireropeNo); 
						pst.setString(i++, bullockCode);
						pst.setString(i++, nbulluckcartMainId);
						pst.setString(i++, nslipboyId);
						pst.setInt(i++, nfactId);
						pst.setInt(i++, nhangamId);
						pst.setLong(i++, nremarkId);
						pst.setString(i++, tokenno);
						pst.setString(i++, nuwsCardId);
						pst.setInt(i++, slipCount);
						pst.setString(i++, nseqWeight);
						pst.setString(i++, nslipboyId);
						QRConstant qrConstant = new QRConstant();
						String qrString = slipno + ConstantVeriables.lblExtraQR;
						String qrPath= qrConstant.createImage(qrString, yearCode);
						
						File qrfile = new File(ConstantVeriables.baseFilePath+ConstantVeriables.qrcode  + qrPath);

						pst.setString(i++, qrPath);
						pst.setString(i++, qrString);
						FileInputStream fis = new FileInputStream(qrfile);
						pst.setBinaryStream(i++, fis, (int) qrfile.length());
						count+=pst.executeUpdate();
						try(PreparedStatement pst11=conn.prepareStatement("select t.nslip_no,t.dslip_date_slipboy,t.nbulluckcart_id from "+ConstantVeriables.weightSlipTableName+" t where t.vyear_id=? and t.nslip_no=?"))
						{
							pst11.setString(1, yearCode);
							pst11.setLong(2, slipno);
							try(ResultSet rs11=pst11.executeQuery())
							{
								if(rs11.next())
								{
									SlipBeanR slipres=new SlipBeanR();
									slipres.setNslipNo(rs11.getString("nslip_no"));
									slipres.setNbullokCode(rs11.getString("nbulluckcart_id"));
									slipres.setExtraQr(ConstantVeriables.lblExtraQR);
									slipres.setSlipDate(Constant.DbDateTimeToAppDateTime(rs11.getTimestamp("dslip_date_slipboy")));
									list.add(slipres);
								}
							}
						}
						slipno++;
						slipCount++;
					}
				}
			}
			if (count > 0 || list.size() > 0) {
				savewsRes.setActionComplete(true);
				if (count > 0)
					savewsRes.setSuccessMsg(ConstantMessage.saveSuccess);
				else
					savewsRes.setSuccessMsg(ConstantMessage.weightSlipAlreadySaved);
				savewsRes.setSlipBeanrList(list);
				savewsRes.setDate(Constant.getCurrentAppDate());
			} else {
				savewsRes.setActionComplete(false);
				savewsRes.setFailMsg(ConstantMessage.saveFailed);
			}
			savewsRes.setSuccess(true);
		} catch (Exception e) {
			savewsRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("weight slip save " + e.getMessage());
			savewsRes.setSe(error);
			e.printStackTrace();
		}
		return savewsRes;
	}

	

	
}
