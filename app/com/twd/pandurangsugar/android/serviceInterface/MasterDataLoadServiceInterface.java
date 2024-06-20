package com.twd.pandurangsugar.android.serviceInterface;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.BankBranchResponse;
import com.twd.pandurangsugar.android.bean.BranchResponse;
import com.twd.pandurangsugar.android.bean.CaneConfirmationFarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.MasterDataResponse;

public interface MasterDataLoadServiceInterface {

	MasterDataResponse loadMasterData(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id, int loadType,String accessType, MainResponse mainresponse);

	CaneConfirmationFarmerResponse caneConfirmationFarmerList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse confirnationList);

	BankBranchResponse bankBranchByBankCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse bankBranchList);

	BranchResponse bankBranchByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse branchRes);

}
