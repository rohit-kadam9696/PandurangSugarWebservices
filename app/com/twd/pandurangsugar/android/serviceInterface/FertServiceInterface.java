package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.FertProductResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;

public interface FertServiceInterface {

	FertProductResponse getProduct(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse farmerresponse);

	MainResponse saveFert(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, String accessType,
			MainResponse saveRes);

}
