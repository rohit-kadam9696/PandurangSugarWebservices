package com.twd.pandurangsugar.web.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.web.dao.ReportDao;
import com.twd.pandurangsugar.web.dao.SystemUser;
import com.twd.pandurangsugar.web.serviceInterface.ReportServiceInterface;

public class ReportService implements ReportServiceInterface {
	ReportDao reportdao=new ReportDao();
	
	@Override
	public ArrayList<KeyPairBoolData> viewSectionList(ArrayList<KeyPairBoolData> sectionList) {
		try(Connection conn=DBConnection.getConnection())
		{
			sectionList=reportdao.viewSectionList(sectionList,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return sectionList;
	}
	
	@Override
	public ArrayList<KeyPairBoolData> loadFarmerByVillege(String nvillageId,String yearId, ArrayList<KeyPairBoolData> farmerList) {
		try(Connection conn=DBConnection.getConnection())
		{
			farmerList=reportdao.loadFarmerByVillege(nvillageId,yearId,farmerList,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return farmerList;
	}
	
	@Override
	public ArrayList<KeyPairBoolData> viewVillageList(String nsectionId, ArrayList<KeyPairBoolData> villageList) {
		try(Connection conn=DBConnection.getConnection())
		{
			villageList=reportdao.viewVillageList(nsectionId,villageList,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return villageList;
	}
	@Override
	public JSONObject viewPlantationMapReport(String yearId, String nvillageId, String nfarmerCode,
			JSONObject plantationMapReport) {
		try(Connection conn=DBConnection.getConnection())
		{
			plantationMapReport=reportdao.viewPlantationMapReport(yearId,nvillageId,nfarmerCode,plantationMapReport,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return plantationMapReport;
	}

	@Override
	public ArrayList<String> viewYearList(ArrayList<String> yearList) {
		try(Connection conn=DBConnection.getConnection())
		{
			yearList=reportdao.viewYearList(yearList,conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return yearList;
	}
	

}
