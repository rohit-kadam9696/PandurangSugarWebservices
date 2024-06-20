package com.twd.pandurangsugar.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;
import com.twd.pandurangsugar.both.constant.WebConstant;

public class ReportDao {

	public ArrayList<KeyPairBoolData> viewSectionList(ArrayList<KeyPairBoolData> sectionList, Connection conn) throws SQLException {
		try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id,TO_NCHAR(t.vsection_name_local)as section_name from gm_m_section_master t order by t.nsection_id asc"))
		{
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					KeyPairBoolData ubean=new KeyPairBoolData();
					ubean.setId(rs.getLong("nsection_id"));
					ubean.setName(DemoConvert2.ism_to_uni(rs.getString("section_name")));
					sectionList.add(ubean);
				}
			}
		}
		return sectionList;
	}

	public ArrayList<KeyPairBoolData> viewVillageList(String nsectionId, ArrayList<KeyPairBoolData> villageList,
			Connection conn) throws SQLException {
		try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,TO_NCHAR(t.village_name_local)as village_name from gm_m_village_master t where t.nsection_id=? order by t.nvillage_id asc"))
		{
			pst.setString(1, nsectionId);
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					KeyPairBoolData ubean=new KeyPairBoolData();
					ubean.setId(rs.getLong("nvillage_id"));
					ubean.setName(DemoConvert2.ism_to_uni(rs.getString("village_name")));
					villageList.add(ubean);
				}
			}
		}
		return villageList;
	}
	public JSONObject viewPlantationMapReport(String yearId, String nvillageId, String nfarmerCode,
			JSONObject result, Connection conn) {
			JSONObject firstLatLong = new JSONObject();
			JSONArray mainResult = new JSONArray();
			String tempplotlist = null;
			JSONObject outerobj = new JSONObject();
			JSONArray poli = new JSONArray();
			int prevplot=0;
			int nextplot=0;
			boolean isFirst=true;
			DecimalFormat pdf=new DecimalFormat("#0.00");
			String fcode="";
			for(int i=0;i<nfarmerCode.split(",").length;i++)
			{
				if(!fcode.trim().isEmpty())
					fcode=fcode+",";
				
				fcode+="\'F"+nfarmerCode.split(",")[i]+"\'";
			}
			String sql="select t.nplot_no,t.vsurve_no,t.dplantation_date,t1.nentity_uni_id,TO_NCHAR(t1.ventity_name_local)as farmer_name,TO_NCHAR(t2.village_name_local)as vill_name,TO_NCHAR(t3.vfarmer_type_name_local)as farmer_type,TO_NCHAR(t4.vvariety_name)as variety_name,TO_NCHAR(t5.vhangam_name)as hangam_name,t6.vlatitude,t6.vlongitude,t.narea from CR_T_PLANTATION t,GM_M_ENTITY_MASTER_DETAIL t1,GM_M_VILLAGE_MASTER t2,GM_M_FARMER_TYPE_MASTER t3,CR_M_VARIETY_MASTER t4,CR_M_HANGAM_MASTER t5,APP_T_REGISTRATION_LATLNGS t6 where t.nentity_uni_id=t1.nentity_uni_id and t.nvillage_id=t2.nvillage_id and t1.nfarmer_type_id=t3.nfarmer_type_id and t.nvariety_id=t4.navariety_id and t.nhangam_id=t5.nhangam_id and t.vyear_id=t6.vyear_id and t.nplot_no=t6.nplot_no and t.vyear_id=? and t.nvillage_id=? ";
			sql+=" AND t1.nentity_uni_id IN("+fcode+")";
			try(PreparedStatement pst1=conn.prepareStatement(sql)){
				int i  = 1;
				pst1.setString(i++, yearId);
				pst1.setString(i++, nvillageId);
				try (ResultSet rs = pst1.executeQuery()) {
					while (rs.next()) {
						nextplot = rs.getInt("nplot_no");
						if(tempplotlist==null)
							tempplotlist=rs.getString("nplot_no");
						else
							tempplotlist=tempplotlist+","+rs.getString("nplot_no");
						if(prevplot!=nextplot && prevplot!=0) {
							outerobj.put("poli", poli);
							mainResult.put(outerobj);
							isFirst =true;	
							outerobj = new JSONObject();
							poli = new JSONArray();
						}
						JSONObject latlong = new JSONObject();
						double lats = rs.getDouble("vlatitude");
						double longs = rs.getDouble("vlongitude");
						if(prevplot==0) {
							firstLatLong.put("lat", lats);
							firstLatLong.put("lng", longs);
						}
						latlong.put("lat", lats);
						latlong.put("lng", longs);
						if(isFirst) {
							outerobj.put("lat", lats);
							outerobj.put("lng", longs);
							outerobj.put("name", ConstantVeriables.lblPlotNo+" : " + nextplot +"   "+ ConstantVeriables.lblFarmerName+" : "  +rs.getString("nentity_uni_id")+" - " + DemoConvert2.ism_to_uni(rs.getString("farmer_name")) +"   "+ConstantVeriables.area+" : "+pdf.format(rs.getDouble("narea")) +"   "+ConstantVeriables.hangam+" : "+DemoConvert2.ism_to_uni(rs.getString("hangam_name"))+"   "+ConstantVeriables.variety+" : "+DemoConvert2.ism_to_uni(rs.getString("variety_name")));
							outerobj.put("plotno", "p_"+nextplot);
						}
						poli.put(latlong);
						isFirst = false;
						prevplot = nextplot;
					}
				}
				if(prevplot!=0) {
					outerobj.put("poli", poli);
					mainResult.put(outerobj);
					isFirst =true;	
				}
				JSONObject harvesting=new JSONObject();
				if(tempplotlist!=null)
				{
					JSONArray nplot_noArr=WebConstant.SplitNplotList(tempplotlist);
					int slen=nplot_noArr.length();
					for(int scount=0;scount<slen;scount++)
					{
						try(PreparedStatement pst2=conn.prepareStatement("select SUM(t.nnet_weight)as nwt,t.nplot_no from WB_T_WEIGHT_SLIP t where t.vyear_id=? and t.nplot_no in("+nplot_noArr.getString(scount)+") group by t.nplot_no "))
						{
							pst2.setString(1, yearId);
							try(ResultSet rs2=pst2.executeQuery())
							{
								while(rs2.next())
								{
									harvesting.put("p_"+rs2.getString("nplot_no"), pdf.format(rs2.getDouble("nwt")));
								}
							}
						}
					}
				}
				result.put("firstlat", firstLatLong);
				result.put("main", mainResult);
				result.put("harvesting", harvesting);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

	public ArrayList<KeyPairBoolData> loadFarmerByVillege(String nvillageId,String yearId, ArrayList<KeyPairBoolData> farmerList,
			Connection conn) throws SQLException {
		try(PreparedStatement pst=conn.prepareStatement("select DISTINCT  t1.nentity_uni_id,t1.ventity_name as farmer_name  from CR_T_PLANTATION t,GM_M_ENTITY_MASTER_DETAIL t1 where t.nentity_uni_id=t1.nentity_uni_id and t.vyear_id=? and t.nvillage_id=? order by t1.nentity_uni_id  ASC "))
		{
			pst.setString(1, yearId);
			pst.setString(2, nvillageId);
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					KeyPairBoolData ubean=new KeyPairBoolData();
					long farmerCode=Long.parseLong(rs.getString("nentity_uni_id").replace("F", "").trim());
					ubean.setId(farmerCode);
					ubean.setName(DemoConvert2.ism_to_uni(rs.getString("farmer_name")));
					farmerList.add(ubean);
				}
			}
		}
		return farmerList;
	}

	public ArrayList<String> viewYearList(ArrayList<String> yearList, Connection conn) throws SQLException {
		try(PreparedStatement pst=conn.prepareStatement("select t.vseason_year,t.vseason_back_year from GM_M_SEASON_YEAR_AGRI t where t.vactive = 'Y' "))
		{
			try(ResultSet rs=pst.executeQuery())
			{
				while(rs.next())
				{
					yearList.add(rs.getString("vseason_year"));
					yearList.add(rs.getString("vseason_back_year"));
				}
			}
		}
		return yearList;
	}
	
}
