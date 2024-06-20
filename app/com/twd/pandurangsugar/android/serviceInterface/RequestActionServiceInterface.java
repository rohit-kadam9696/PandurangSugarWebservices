/**
 * 
 */
package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.FarmerListResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;

/**
 * @author Administrator
 *
 */
public interface RequestActionServiceInterface {

	ActionResponse save(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse);

	ActionResponse verifyOrReject(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse actionResponse);

	FarmerListResponse farmerList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse farmerList, boolean isverifyList);

}
