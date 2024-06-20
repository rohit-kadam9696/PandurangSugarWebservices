package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.ActionResponse;
import com.twd.pandurangsugar.android.bean.CaneYardBalanceResponse;
import com.twd.pandurangsugar.android.bean.DataListResonse;
import com.twd.pandurangsugar.android.bean.DataTwoListResonse;
import com.twd.pandurangsugar.android.bean.LotGenResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.NumIndResponse;
import com.twd.pandurangsugar.android.bean.NumSlipListResponse;
import com.twd.pandurangsugar.android.bean.SavePrintResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SingleNumDataResponse;
import com.twd.pandurangsugar.android.bean.TableResponse;
import com.twd.pandurangsugar.android.bean.UserRoleResponse;
import com.twd.pandurangsugar.android.bean.UserYardResponse;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.dao.NumberSystemDao;
import com.twd.pandurangsugar.android.serviceInterface.NumberSystemInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class NumberSystemService implements NumberSystemInterface {

	LoginDao login=new LoginDao();
	NumberSystemDao numbersysdao=new NumberSystemDao();
	
	@Override
	public UserYardResponse userYardInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse userYardResp) {
		try (Connection conn = DBConnection.getConnection()) {
			userYardResp = login.verifyUser(userYardResp, chit_boy_id, ramdomstring, imei, accessType, conn);
			UserYardResponse userYardResponse = (UserYardResponse) userYardResp;
			if (userYardResponse.isSuccess()) {
				String empcode = reqObj.has("empcode") ? reqObj.getString("empcode") : "";
				if (empcode.trim().isEmpty()) {
					userYardResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter empcode");
					userYardResponse.setSe(error);
				} else {
					userYardResponse = numbersysdao.userYardInfo(empcode, userYardResponse, conn);
				}
				return userYardResponse;
			}
		} catch (Exception e) {
			userYardResp.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			userYardResp.setSe(error);
			e.printStackTrace();
		}
		return (UserYardResponse) userYardResp;

	}

	@Override
	public MainResponse updateYardEmp(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse updateYardRes) {
		try (Connection conn = DBConnection.getConnection()) {
			updateYardRes = login.verifyUser(updateYardRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			ActionResponse updateYardResponse = (ActionResponse) updateYardRes;
			if (updateYardResponse.isSuccess()) {
				String empcode = reqObj.has("empcode") ? reqObj.getString("empcode") : null;
				String yardid = reqObj.has("yardid") ? reqObj.getString("yardid") : null;
				
				if (empcode.trim().isEmpty() || yardid.trim().isEmpty()) {
					updateYardResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter empcode");
					updateYardResponse.setSe(error);
				} else {
					updateYardResponse = numbersysdao.updateYardEmp(empcode, yardid, updateYardResponse, conn);
				}
				return updateYardResponse;
			}
		} catch (Exception e) {
			updateYardRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			updateYardRes.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) updateYardRes;
	}

	@Override
	public MainResponse removeYardEmp(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse removeYardRes) {
		try (Connection conn = DBConnection.getConnection()) {
			removeYardRes = login.verifyUser(removeYardRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			ActionResponse updateYardResponse = (ActionResponse) removeYardRes;
			if (updateYardResponse.isSuccess()) {
				String empcode = reqObj.has("empcode") ? reqObj.getString("empcode") : null;
				String yardid = reqObj.has("yardid") ? reqObj.getString("yardid") : null;
				
				if (empcode.trim().isEmpty() || yardid.trim().isEmpty()) {
					updateYardResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter empcode");
					updateYardResponse.setSe(error);
				} else {
					updateYardResponse = numbersysdao.removeYardEmp(empcode, yardid, updateYardResponse, conn);
				}
				return updateYardResponse;
			}
		} catch (Exception e) {
			removeYardRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			removeYardRes.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) removeYardRes;
	}

	@Override
	public MainResponse slipDataList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse slipdataRes) {
		try (Connection conn = DBConnection.getConnection()) {
			slipdataRes = login.verifyUser(slipdataRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			NumSlipListResponse slipDataResponse = (NumSlipListResponse) slipdataRes;
			if (slipDataResponse.isSuccess()) {
			
				String type = reqObj.has("type") ? reqObj.getString("type") : null;
				String code = reqObj.has("code") ? reqObj.getString("code") : null;
				String vtype = reqObj.has("vtype") ? reqObj.getString("vtype") : null;
				String vyearid = reqObj.has("vyear_id") ? reqObj.getString("vyear_id") : null;		
				
				if (type.trim().isEmpty() || vyearid.trim().isEmpty() || code.trim().isEmpty() || (type.equalsIgnoreCase("C") && vtype.trim().isEmpty()) ) {
					slipDataResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					slipDataResponse.setSe(error);
				} else {
					slipDataResponse = numbersysdao.slipDataList(type, code, vtype, vyearid, slipDataResponse, conn);
				}
				return slipDataResponse;
			}
		} catch (Exception e) {
			slipdataRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			slipdataRes.setSe(error);
			e.printStackTrace();
		}
		return (NumSlipListResponse) slipdataRes;
	}

	@Override
	public MainResponse saveNumber(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse savenumberRes) {
		try (Connection conn = DBConnection.getConnection()) {
			savenumberRes = login.verifyUser(savenumberRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			SavePrintResponse saveNumberResponse = (SavePrintResponse) savenumberRes;
			if (saveNumberResponse.isSuccess()) {			
				String vyear_id = reqObj.has("vyear_id") ? reqObj.getString("vyear_id") : null;
				String nslip_no = reqObj.has("nslip_no") ? reqObj.getString("nslip_no") : null;
				String nvillage_id = reqObj.has("nvillage_id") ? reqObj.getString("nvillage_id") : null;
				String nentity_uni_id = reqObj.has("nentity_uni_id") ? reqObj.getString("nentity_uni_id") : null;
				String nvehicle_group_id = reqObj.has("nvehicle_group_id") ? reqObj.getString("nvehicle_group_id") : null;
				String nvehicle_type = reqObj.has("nvehicle_type") ? reqObj.getString("nvehicle_type") : null;
				String vvehicle_no = reqObj.has("vvehicle_no") ? reqObj.getString("vvehicle_no") : null;
				String vlatitude = reqObj.has("vlatitude") ? reqObj.getString("vlatitude") : null;
				String vlongitude = reqObj.has("vlongitude") ? reqObj.getString("vlongitude") : null;
				String vaccuracy = reqObj.has("vaccuracy") ? reqObj.getString("vaccuracy") : null;
				String vphoto = reqObj.has("vphoto") ? reqObj.getString("vphoto") : null;
				String vvillage_name = reqObj.has("vvillage_name") ? reqObj.getString("vvillage_name") : null;
				String vtrans_name = reqObj.has("vtrans_name") ? reqObj.getString("vtrans_name") : null;
				
				if (vyear_id.trim().isEmpty() || nslip_no.trim().isEmpty() || nvillage_id.trim().isEmpty() ||  nentity_uni_id.trim().isEmpty()||  nvehicle_type.trim().isEmpty() ||  vvehicle_no.trim().isEmpty()  ||  vlatitude.trim().isEmpty() ||  vlongitude.trim().isEmpty()  ||  vaccuracy.trim().isEmpty()  ||  vphoto.trim().isEmpty() || nvehicle_group_id.trim().isEmpty() || vvillage_name.trim().isEmpty() || vtrans_name.trim().isEmpty()) {
					saveNumberResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter empcode");
					saveNumberResponse.setSe(error);
				} else {
					saveNumberResponse = NumberSystemDao.saveNumber(vyear_id,nslip_no,nvillage_id,nentity_uni_id,nvehicle_type,vvehicle_no,vlatitude,vlongitude,vaccuracy,vphoto,chit_boy_id,nvehicle_group_id,vvillage_name,vtrans_name, saveNumberResponse, conn);
				}
				return saveNumberResponse;
			}
		} catch (Exception e) {
			savenumberRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			savenumberRes.setSe(error);
			e.printStackTrace();
		}
		return (NumSlipListResponse) savenumberRes;}

	@Override
	public MainResponse generateLotList(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse generatelotlistRes) {
		try (Connection conn = DBConnection.getConnection()) {
			generatelotlistRes = login.verifyUser(generatelotlistRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			TableResponse generatelotlistResponse = (TableResponse) generatelotlistRes;
			if (generatelotlistResponse.isSuccess()) {
				
				long vehicleGroupId = reqObj.has("vehicleGroupId") ? reqObj.getLong("vehicleGroupId") : 0;
				long yardId = reqObj.has("yardId") ? reqObj.getLong("yardId") : 0;
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;		
				
				if (vehicleGroupId==0 || yardId==0 || yearId.trim().isEmpty() ) {
					generatelotlistResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					generatelotlistResponse.setSe(error);
				} else {
					generatelotlistResponse = (TableResponse) numbersysdao.generateLotList(String.valueOf(vehicleGroupId), String.valueOf(yardId), yearId,chit_boy_id, generatelotlistResponse, conn);
				}
				return generatelotlistResponse;
			}
		} catch (Exception e) {
			generatelotlistRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			generatelotlistRes.setSe(error);
			e.printStackTrace();
		}
		return (TableResponse) generatelotlistRes;
	}

	@Override
	public MainResponse generateLot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse generatelotRes) {
		try (Connection conn = DBConnection.getConnection()) {
			generatelotRes = login.verifyUser(generatelotRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			LotGenResponse generatelotResponse = (LotGenResponse) generatelotRes;
			if (generatelotResponse.isSuccess()) {
				
				long vehicleGroupId = reqObj.has("vehicleGroupId") ? reqObj.getLong("vehicleGroupId") : 0;
				long yardId = reqObj.has("yardId") ? reqObj.getLong("yardId") : 0;
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;		
				
				if (vehicleGroupId==0 || yardId==0 || yearId.trim().isEmpty() ) {
					generatelotResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					generatelotResponse.setSe(error);
				} else {
					generatelotResponse = (LotGenResponse) numbersysdao.generateLotList(String.valueOf(vehicleGroupId), String.valueOf(yardId), yearId,chit_boy_id, generatelotResponse, conn);
				}
				return generatelotResponse;
			}
		} catch (Exception e) {
			generatelotRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			generatelotRes.setSe(error);
			e.printStackTrace();
		}
		return (LotGenResponse) generatelotRes;
	}

	@Override
	public MainResponse singleNumData(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse singlenumdataRes) {
		try (Connection conn = DBConnection.getConnection()) {
			singlenumdataRes = login.verifyUser(singlenumdataRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			SingleNumDataResponse singlenumdataResponse = (SingleNumDataResponse) singlenumdataRes;
			if (singlenumdataResponse.isSuccess()) {
				
				
				String type = reqObj.has("type") ? reqObj.getString("type") : null;
				String code = reqObj.has("code") ? reqObj.getString("code") : null;	
				String vtype = reqObj.has("vtype") ? reqObj.getString("vtype") : null;
				String vyearid = reqObj.has("vyear_id") ? reqObj.getString("vyear_id") : null;	
				if ( type.trim().isEmpty() || code.trim().isEmpty()|| vyearid.trim().isEmpty()  ) {
					singlenumdataResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					singlenumdataResponse.setSe(error);
				} else {
					singlenumdataResponse =  numbersysdao.singleNumData(type,code,vyearid,vtype,chit_boy_id, singlenumdataResponse, conn);
				}
				return singlenumdataResponse;
			}
		} catch (Exception e) {
			singlenumdataRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			singlenumdataRes.setSe(error);
			e.printStackTrace();
		}
		return (SingleNumDataResponse) singlenumdataRes;
	}

	@Override
	public MainResponse numIndExclude(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse numindexcludeRes) {

		try (Connection conn = DBConnection.getConnection()) {
			numindexcludeRes = login.verifyUser(numindexcludeRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			SavePrintResponse numindexcludeResponse = (SavePrintResponse) numindexcludeRes;
			if (numindexcludeResponse.isSuccess()) {
				
				String transId = reqObj.has("transId") ? reqObj.getString("transId") : null;
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;
				String nslipNo = reqObj.has("nslipNo") ? reqObj.getString("nslipNo") : null;
				String nyardId = reqObj.has("nyardId") ? reqObj.getString("nyardId") : null;
				long nreasonId = reqObj.has("nreasonId") ? reqObj.getLong("nreasonId") : 0;
				String nvehicleGroupId = reqObj.has("nvehicleGroupId") ? reqObj.getString("nvehicleGroupId") : null;
				
				if (transId.trim().isEmpty() || yearId.trim().isEmpty() || nslipNo.trim().isEmpty() || nyardId.trim().isEmpty() || nvehicleGroupId.trim().isEmpty() || nreasonId ==0) {
					numindexcludeResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					numindexcludeResponse.setSe(error);
				} else {
					numindexcludeResponse = numbersysdao.numIndExclude(transId,yearId,nslipNo,nyardId,nvehicleGroupId, nreasonId, chit_boy_id, numindexcludeResponse, conn);
				}
				return numindexcludeResponse;
			}
		} catch (Exception e) {
			numindexcludeRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			numindexcludeRes.setSe(error);
			e.printStackTrace();
		}
		return (SavePrintResponse) numindexcludeRes;
	
	}

	@Override
	public MainResponse singleNumDataBlock(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse singlenumdataRes) {
		try (Connection conn = DBConnection.getConnection()) {
			singlenumdataRes = login.verifyUser(singlenumdataRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			SingleNumDataResponse singlenumdataResponse = (SingleNumDataResponse) singlenumdataRes;
			if (singlenumdataResponse.isSuccess()) {
				
				
				String type = reqObj.has("type") ? reqObj.getString("type") : null;
				String code = reqObj.has("code") ? reqObj.getString("code") : null;	
				String vtype = reqObj.has("vtype") ? reqObj.getString("vtype") : null;
				String vyearid = reqObj.has("vyear_id") ? reqObj.getString("vyear_id") : null;	
				if ( type.trim().isEmpty() || code.trim().isEmpty()|| vyearid.trim().isEmpty()  ) {
					singlenumdataResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					singlenumdataResponse.setSe(error);
				} else {
					singlenumdataResponse =  numbersysdao.singleNumDataBlock(type,code,vyearid,vtype,chit_boy_id, singlenumdataResponse, conn);
				}
				return singlenumdataResponse;
			}
		} catch (Exception e) {
			singlenumdataRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			singlenumdataRes.setSe(error);
			e.printStackTrace();
		}
		return (SingleNumDataResponse) singlenumdataRes;
	}

	@Override
	public MainResponse stopNumber(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse numindexcludeRes) {

		try (Connection conn = DBConnection.getConnection()) {
			numindexcludeRes = login.verifyUser(numindexcludeRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			ActionResponse numindexcludeResponse = (ActionResponse) numindexcludeRes;
			if (numindexcludeResponse.isSuccess()) {
				
				String transId = reqObj.has("transId") ? reqObj.getString("transId") : null;
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;
				String nslipNo = reqObj.has("nslipNo") ? reqObj.getString("nslipNo") : null;
				String nyardId = reqObj.has("nyardId") ? reqObj.getString("nyardId") : null;
				String statusId = reqObj.has("statusId") ? reqObj.getString("statusId") : null;
				long nreasonId = reqObj.has("nreasonId") ? reqObj.getLong("nreasonId") : 0;
				String nvehicleGroupId = reqObj.has("nvehicleGroupId") ? reqObj.getString("nvehicleGroupId") : null;
				
				if (transId.trim().isEmpty() || yearId.trim().isEmpty() || nslipNo.trim().isEmpty() || nyardId.trim().isEmpty() || nvehicleGroupId.trim().isEmpty() || statusId.trim().isEmpty() || (statusId.equals("4") && nreasonId==0)) {
					numindexcludeResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					numindexcludeResponse.setSe(error);
				} else {
					numindexcludeResponse = numbersysdao.stopNumber(transId,yearId,nslipNo,nyardId,nvehicleGroupId, nreasonId, statusId, chit_boy_id, numindexcludeResponse, conn);
				}
				return numindexcludeResponse;
			}
		} catch (Exception e) {
			numindexcludeRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			numindexcludeRes.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) numindexcludeRes;
	
	}

	@Override
	public MainResponse loadLot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse loadlotRes) {
		try (Connection conn = DBConnection.getConnection()) {
			loadlotRes = login.verifyUser(loadlotRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			DataListResonse loadLotResponse = (DataListResonse) loadlotRes;
			if (loadLotResponse.isSuccess()) {				
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;
				long nyardId = reqObj.has("yardId") ? reqObj.getLong("yardId") : 0;
				long nvehicleGroupId = reqObj.has("vehicleGroupId") ? reqObj.getLong("vehicleGroupId") : 0;
				
				if ( yearId.trim().isEmpty() || nyardId==0 || nvehicleGroupId==0) {
					loadLotResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					loadLotResponse.setSe(error);
				} else {
					loadLotResponse = numbersysdao.loadLot(yearId,nyardId,nvehicleGroupId, chit_boy_id, loadLotResponse, conn);
				}
				return loadLotResponse;
			}
		} catch (Exception e) {
			loadlotRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			loadlotRes.setSe(error);
			e.printStackTrace();
		}
		return (DataListResonse) loadlotRes;
	
	}

	@Override
	public MainResponse printLot(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse printlotRes) {


		try (Connection conn = DBConnection.getConnection()) {
			printlotRes = login.verifyUser(printlotRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			SavePrintResponse printlotResponse = (SavePrintResponse) printlotRes;
			if (printlotResponse.isSuccess()) {
				
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;
				long nyardId = reqObj.has("yardId") ? reqObj.getLong("yardId") : 0;
				long nvehicleGroupId = reqObj.has("vehicleGroupId") ? reqObj.getLong("vehicleGroupId") : 0;
				long lotno = reqObj.has("lotno") ? reqObj.getLong("lotno") : 0;
				
				
				if ( yearId.trim().isEmpty() || nyardId==0 || nvehicleGroupId==0 || lotno ==0) {
					printlotResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					printlotResponse.setSe(error);
				} else {
					printlotResponse = numbersysdao.printLot(yearId,nyardId,nvehicleGroupId, lotno, chit_boy_id, printlotResponse, conn);
				}
				return printlotResponse;
			}
		} catch (Exception e) {
			printlotRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			printlotRes.setSe(error);
			e.printStackTrace();
		}
		return (SavePrintResponse) printlotRes;
	
	
	}

	@Override
	public MainResponse vehicleRegister(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse vehicleregisterRes) {

		try (Connection conn = DBConnection.getConnection()) {
			vehicleregisterRes = login.verifyUser(vehicleregisterRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			TableResponse vehicleregisterResponse = (TableResponse) vehicleregisterRes;
			if (vehicleregisterResponse.isSuccess()) {
				
				
				String yardId = reqObj.has("yardId") ? reqObj.getString("yardId") : "0";
				String shiftId = reqObj.has("shiftId") ? reqObj.getString("shiftId") : "0";
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;		
				String dateVal = reqObj.has("dateVal") ? reqObj.getString("dateVal") : null;	
				String vehicleStatus = reqObj.has("vehicleStatus") ? reqObj.getString("vehicleStatus") : "-1";	
				
				if (dateVal.trim().isEmpty() || yardId.equals("0") || yearId.trim().isEmpty() ) {
					vehicleregisterResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					vehicleregisterResponse.setSe(error);
				} else {
					vehicleregisterResponse =numbersysdao.vehicleRegister(dateVal, yardId, shiftId, yearId, vehicleStatus,chit_boy_id, vehicleregisterResponse, conn);
				}
				return vehicleregisterResponse;
			}
		} catch (Exception e) {
			vehicleregisterRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			vehicleregisterRes.setSe(error);
			e.printStackTrace();
		}
		return (TableResponse) vehicleregisterRes;
	
	}

	@Override
	public MainResponse numWaiting(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse numwaitingRes) {

		try (Connection conn = DBConnection.getConnection()) {
			numwaitingRes = login.verifyUser(numwaitingRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			NumIndResponse numwaitingResponse = (NumIndResponse) numwaitingRes;
			if (numwaitingResponse.isSuccess()) {
				
				
				String nyearId = reqObj.has("nyearId") ? reqObj.getString("nyearId") : null;
				String type = reqObj.has("type") ? reqObj.getString("type") : null;		
				String code = reqObj.has("code") ? reqObj.getString("code") : null;		
				String vtype = reqObj.has("vtype") ? reqObj.getString("vtype") : null;		
				
				if (nyearId.trim().isEmpty() || type.trim().isEmpty() || code.trim().isEmpty() || (type.equals("C") && vtype.trim().isEmpty())) {
					numwaitingResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					numwaitingResponse.setSe(error);
				} else {
					numwaitingResponse =numbersysdao.numWaiting(nyearId,type,code,vtype,chit_boy_id, numwaitingResponse, conn);
				}
				return numwaitingResponse;
			}
		} catch (Exception e) {
			numwaitingRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			numwaitingRes.setSe(error);
			e.printStackTrace();
		}
		return (NumIndResponse) numwaitingRes;
	
	}

	@Override
	public MainResponse printTokenPass(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse printtokenpassRes) {
		try (Connection conn = DBConnection.getConnection()) {
			printtokenpassRes = login.verifyUser(printtokenpassRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			SavePrintResponse printtokenpassResponse = (SavePrintResponse) printtokenpassRes;
			if (printtokenpassResponse.isSuccess()) {			
				
				String printtype = reqObj.has("printtype") ? reqObj.getString("printtype") : null;
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;
				String vtype = reqObj.has("vtype") ? reqObj.getString("vtype") : null;
				String code = reqObj.has("code") ? reqObj.getString("code") : null;
								
				if (printtype.trim().isEmpty() || yearId.trim().isEmpty() || vtype.trim().isEmpty() || code.trim().isEmpty()) {
					printtokenpassResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter ");
					printtokenpassResponse.setSe(error);
				} else {
					printtokenpassResponse = numbersysdao.printTokenPass(printtype,yearId,vtype,code,chit_boy_id, printtokenpassResponse, conn);
				}
				return printtokenpassResponse;
			}
		} catch (Exception e) {
			printtokenpassRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			printtokenpassRes.setSe(error);
			e.printStackTrace();
		}
		return (SavePrintResponse) printtokenpassRes;
	
	
	}

	@Override
	public MainResponse vehicleStatus(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse vehicleStatusRes) {
		try (Connection conn = DBConnection.getConnection()) {
			vehicleStatusRes = login.verifyUser(vehicleStatusRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			DataListResonse vehicleStatusResponse = (DataListResonse) vehicleStatusRes;
			if (vehicleStatusResponse.isSuccess()) {				
				vehicleStatusResponse = numbersysdao.vehicleStatus(vehicleStatusResponse, conn);
				return vehicleStatusResponse;
			}
		} catch (Exception e) {
			vehicleStatusRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			vehicleStatusRes.setSe(error);
			e.printStackTrace();
		}
		return (DataListResonse) vehicleStatusRes;
	}
	
	@Override
	public UserRoleResponse userRoleInfo(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse userRoleResp) {
		try (Connection conn = DBConnection.getConnection()) {
			userRoleResp = login.verifyUser(userRoleResp, chit_boy_id, ramdomstring, imei, accessType, conn);
			UserRoleResponse userRoleResponse = (UserRoleResponse) userRoleResp;
			if (userRoleResponse.isSuccess()) {
				String empcode = reqObj.has("empcode") ? reqObj.getString("empcode") : "";
				if (empcode.trim().isEmpty()) {
					userRoleResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter empcode");
					userRoleResponse.setSe(error);
				} else {
					userRoleResponse = numbersysdao.userRoleInfo(empcode, userRoleResponse, conn);
				}
				return userRoleResponse;
			}
		} catch (Exception e) {
			userRoleResp.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			userRoleResp.setSe(error);
			e.printStackTrace();
		}
		return (UserRoleResponse) userRoleResp;

	}

	@Override
	public MainResponse updateRoleEmp(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse updateRoleRes) {
		try (Connection conn = DBConnection.getConnection()) {
			updateRoleRes = login.verifyUser(updateRoleRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			ActionResponse updateRoleResponse = (ActionResponse) updateRoleRes;
			if (updateRoleResponse.isSuccess()) {
				String empcode = reqObj.has("empcode") ? reqObj.getString("empcode") : null;
				String roleid = reqObj.has("roleid") ? reqObj.getString("roleid") : null;
				
				if (empcode.trim().isEmpty() || roleid.trim().isEmpty()) {
					updateRoleResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter empcode");
					updateRoleResponse.setSe(error);
				} else {
					updateRoleResponse = numbersysdao.updateRoleEmp(empcode, roleid, updateRoleResponse, conn);
				}
				return updateRoleResponse;
			}
		} catch (Exception e) {
			updateRoleRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			updateRoleRes.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) updateRoleRes;
	}
	
	@Override
	public MainResponse cancelNumber(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse updateRoleRes) {
		try (Connection conn = DBConnection.getConnection()) {
			updateRoleRes = login.verifyUser(updateRoleRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			ActionResponse updateRoleResponse = (ActionResponse) updateRoleRes;
			if (updateRoleResponse.isSuccess()) {
				String transId = reqObj.has("transId") ? reqObj.getString("transId") : null;
				String nslipNo = reqObj.has("nslipNo") ? reqObj.getString("nslipNo") : null;
				String nreasonId = reqObj.has("nreasonId") ? String.valueOf(reqObj.getLong("nreasonId")) : null;
				String yearId = reqObj.has("yearId") ? reqObj.getString("yearId") : null;
				
				if (transId.trim().isEmpty() || nslipNo.trim().isEmpty() || nreasonId.trim().isEmpty() || yearId.trim().isEmpty()) {
					updateRoleResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter empcode");
					updateRoleResponse.setSe(error);
				} else {
					updateRoleResponse = numbersysdao.cancelNumber(transId, nslipNo, nreasonId, yearId, chit_boy_id, updateRoleResponse, conn);
				}
				return updateRoleResponse;
			}
		} catch (Exception e) {
			updateRoleRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			updateRoleRes.setSe(error);
			e.printStackTrace();
		}
		return (ActionResponse) updateRoleRes;
	}

	@Override
	public MainResponse roleUser(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse roleUserRes) {
		try (Connection conn = DBConnection.getConnection()) {
			roleUserRes = login.verifyUser(roleUserRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			DataTwoListResonse userRoleResponse = (DataTwoListResonse) roleUserRes;
			if (userRoleResponse.isSuccess()) {
				userRoleResponse = numbersysdao.roleUser(userRoleResponse, conn);
				return userRoleResponse;
			}
		} catch (Exception e) {
			roleUserRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			roleUserRes.setSe(error);
			e.printStackTrace();
		}
		return (UserRoleResponse) roleUserRes;

	}

	@Override
	public MainResponse summaryReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse summaryreportRes) {
		try (Connection conn = DBConnection.getConnection()) {
			summaryreportRes = login.verifyUser(summaryreportRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			CaneYardBalanceResponse summaryreportResponse = (CaneYardBalanceResponse) summaryreportRes;
			if (summaryreportResponse.isSuccess()) {
				summaryreportResponse = numbersysdao.summaryReport(summaryreportResponse,chit_boy_id, conn);
				return summaryreportResponse;
			}
		} catch (Exception e) {
			summaryreportRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			summaryreportRes.setSe(error);
			e.printStackTrace();
		}
		return (CaneYardBalanceResponse) summaryreportRes;

	}

	@Override
	public MainResponse inwardSummaryReport(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,
			String accessType, MainResponse inwardSummaryRes) {
		try (Connection conn = DBConnection.getConnection()) {
			inwardSummaryRes = login.verifyUser(inwardSummaryRes, chit_boy_id, ramdomstring, imei, accessType, conn);
			TableResponse reqResponse = (TableResponse) inwardSummaryRes;
			if (reqResponse.isSuccess()) {
				String rdate = reqObj.has("rdate") ? reqObj.getString("rdate") : "";
				if (rdate.trim().isEmpty()) {
					reqResponse.setSuccess(false);
					ServerError error = new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter yearCode");
					reqResponse.setSe(error);
				} else {
					// check current date
					reqResponse = numbersysdao.inwardSummaryReport(rdate, chit_boy_id, reqResponse, conn);
				}
				return reqResponse;

			}
		} catch (Exception e) {
			inwardSummaryRes.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue " + e.getMessage());
			inwardSummaryRes.setSe(error);
			e.printStackTrace();
		}
		return (TableResponse) inwardSummaryRes;
	}
}
