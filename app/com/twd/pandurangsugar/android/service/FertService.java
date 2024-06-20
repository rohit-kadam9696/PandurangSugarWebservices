package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.FertProductResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.SavePrintResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.dao.FertDao;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.serviceInterface.FertServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class FertService implements FertServiceInterface {

	LoginDao login = new LoginDao();
	FertDao fertdao = new FertDao();

	@Override
	public FertProductResponse getProduct(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse mainResponse) {

		try (Connection conn = DBConnection.getConnection()) {
			mainResponse = login.verifyUser(mainResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			FertProductResponse fertProduct = (FertProductResponse) mainResponse;
			
			if (mainResponse.isSuccess()) {
				String saletypes = reqObj.has("saletypes") ? reqObj.getString("saletypes") : "";
				if (saletypes.trim().isEmpty()) {
					mainResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					mainResponse.setSe(error);
				} else {
					fertProduct = fertdao.getProductList(saletypes, fertProduct, conn);
					return fertProduct;
				}
			}
		} catch (Exception e) {
			mainResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			mainResponse.setSe(error);
			e.printStackTrace();
		}
		return (FertProductResponse) mainResponse;
	}

	@Override
	public MainResponse saveFert(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse mainResponse) {

		try (Connection conn = DBConnection.getConnection()) {
			mainResponse = login.verifyUser(mainResponse, chit_boy_id, ramdomstring, imei, accessType, conn);
			SavePrintResponse saveRes = (SavePrintResponse) mainResponse;
			
			if (mainResponse.isSuccess()) {
				String vyearId=reqObj.has("vyear_id")?reqObj.getString("vyear_id"):"";
				String dissueDate=reqObj.has("dissue_date")?reqObj.getString("dissue_date"):"";
				String nentityUniId=reqObj.has("nentity_uni_id")?reqObj.getString("nentity_uni_id"):"";
				String nfertGuarantor1=reqObj.has("nfert_guarantor_1")?reqObj.getString("nfert_guarantor_1"):null;
				String nfertGuarantor2=reqObj.has("nfert_guarantor_2")?reqObj.getString("nfert_guarantor_2"):null;
				String nfertiVatapArea=reqObj.has("n_ferti_vatap_area")?reqObj.getString("n_ferti_vatap_area"):"";
				String vsaleType=reqObj.has("vsale_type")?reqObj.getString("vsale_type"):"";
				String vseasonYear=reqObj.has("vseason_year")?reqObj.getString("vseason_year"):"";
				String nplotNo=reqObj.has("nplot_no")?reqObj.getString("nplot_no"):"";
				String nlocationId=reqObj.has("nlocation_id")?reqObj.getString("nlocation_id"):"";
				String farmername=reqObj.has("farmername")?reqObj.getString("farmername"):"";
				String guarantor1name=reqObj.has("guarantor1name")?reqObj.getString("guarantor1name"):"";
				String guarantor2name=reqObj.has("guarantor2name")?reqObj.getString("guarantor2name"):"";
				JSONArray details=reqObj.has("details")?reqObj.getJSONArray("details"):null;
				if(vyearId.trim().isEmpty() || dissueDate.trim().isEmpty() || nentityUniId.trim().isEmpty()
						 || nfertiVatapArea.trim().isEmpty() || vsaleType.trim().isEmpty() || vseasonYear.trim().isEmpty() || nplotNo.trim().isEmpty() || nlocationId.trim().isEmpty() || details==null)
				{
				
					mainResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter");
					mainResponse.setSe(error);
				} else {
					saveRes = fertdao.saveFert(vyearId,dissueDate, nentityUniId, nfertGuarantor1, nfertGuarantor2, nfertiVatapArea, vsaleType, vseasonYear, nplotNo, nlocationId, chit_boy_id, farmername, guarantor1name, guarantor2name, details, saveRes, conn);
					return saveRes;
				}
			}
		} catch (Exception e) {
			mainResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			mainResponse.setSe(error);
			e.printStackTrace();
		}
		return (FertProductResponse) mainResponse;
	}

}
