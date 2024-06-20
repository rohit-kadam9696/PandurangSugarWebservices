package com.twd.pandurangsugar.web.serviceInterface;

import java.util.ArrayList;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.KeyPairBoolData;

public interface ReportServiceInterface {

	ArrayList<KeyPairBoolData> viewSectionList(ArrayList<KeyPairBoolData> sectionList);

	ArrayList<KeyPairBoolData> viewVillageList(String nsectionId,  ArrayList<KeyPairBoolData> villageList);

	ArrayList<KeyPairBoolData> loadFarmerByVillege(String nvillageId,String yearId, ArrayList<KeyPairBoolData> farmerList);

	JSONObject viewPlantationMapReport(String yearId, String nvillageId, String nfarmerCode,
			JSONObject plantationMapReport);

	ArrayList<String> viewYearList(ArrayList<String> yearList);

}
