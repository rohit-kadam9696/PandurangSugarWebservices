package com.twd.pandurangsugar.android.service;

import java.sql.Connection;
import java.util.ArrayList;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.BankBranchResponse;
import com.twd.pandurangsugar.android.bean.BranchResponse;
import com.twd.pandurangsugar.android.bean.CaneConfirmationFarmerResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.MasterDataResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.VillageMaster;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.dao.MasterDataLoadDao;
import com.twd.pandurangsugar.android.serviceInterface.MasterDataLoadServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

public class MasterDataLoadService implements MasterDataLoadServiceInterface{

	LoginDao login=new LoginDao();
	MasterDataLoadDao mdataLoad=new MasterDataLoadDao();
	
	@Override
	public MasterDataResponse loadMasterData(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id, int loadType,String accessType, MainResponse mainresponse) {
		try(Connection conn=DBConnection.getConnection())
		{
			mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType,conn);
			MasterDataResponse dataLoadResponse=(MasterDataResponse) mainresponse;
			if(mainresponse.isSuccess())
			{
				/*dataLoadResponse.setSuccess(true);
				dataLoadResponse.setDesignation(mainresponse.getDesignation());
				dataLoadResponse.setHarvestingYearCode(mainresponse.getHarvestingYearCode());
				dataLoadResponse.setMobileno(mainresponse.getMobileno());
				dataLoadResponse.setNuserRoleId(mainresponse.getNuserRoleId());
				dataLoadResponse.setPerbygroup(mainresponse.getPerbygroup());
				dataLoadResponse.setPggroups(mainresponse.getPggroups());
				dataLoadResponse.setSlipboycode(mainresponse.getSlipboycode());
				dataLoadResponse.setUniquestring(mainresponse.getUniquestring());
				dataLoadResponse.setUpdate(mainresponse.isSuccess());
				dataLoadResponse.setUpdateResponse(mainresponse.getUpdateResponse());
				dataLoadResponse.setVfullName(mainresponse.getVfullName());
				dataLoadResponse.setYearCode(mainresponse.getYearCode());*/
				if(loadType!=3)
				{
					dataLoadResponse = mdataLoad.loadBank(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.CropType(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.FarmerCategory(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.FarmerType(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.Hangam(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.HarvestingType(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.IrrigationType(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.LagneType(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.menu(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.section(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.variety(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.cropWater(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.vehicleType(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.caneType(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.remarkList(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.resonList(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.fertSaleTypeList(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.caneYardList(dataLoadResponse, conn);
					dataLoadResponse = mdataLoad.vehicleGroupList(dataLoadResponse, conn);
				}
				if(loadType==1 || loadType==3)
				{
					dataLoadResponse=mdataLoad.village(dataLoadResponse,chit_boy_id,conn);
					String villList="0";
					ArrayList<VillageMaster> VillageList=dataLoadResponse.getVillageList();
					for(VillageMaster  vm:VillageList)
					{
						if(villList.equals("0"))
							villList=""+vm.getNvillageId();
						else
							villList=villList+","+vm.getNvillageId();
					}
					if(!villList.equals("0"))
					{
						// cane confirmation list
						String yearCode=mainresponse.getHarvestingYearCode();
						if(yearCode!=null  && !yearCode.trim().isEmpty())
						{
							dataLoadResponse=mdataLoad.caneConfirmationFarmerList(dataLoadResponse,yearCode,villList,conn);
						}else
						{
							dataLoadResponse.setSuccess(false);
							ServerError error=new ServerError();
							error.setError(ConstantVeriables.ERROR_006);
							error.setMsg("Harvesting Year Not Found ");
							dataLoadResponse.setSe(error);
						}
						
						dataLoadResponse=mdataLoad.farmerList(dataLoadResponse,villList,chit_boy_id,conn);
					}
						
				}
				return dataLoadResponse;
			}
	}catch (Exception e) {
		mainresponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			mainresponse.setSe(error);
			e.printStackTrace();
		}
		return (MasterDataResponse) mainresponse;
	}

	@Override
	public CaneConfirmationFarmerResponse caneConfirmationFarmerList(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse confirnationList) {

		try(Connection conn=DBConnection.getConnection())
		{
			confirnationList=login.verifyUser(confirnationList,chit_boy_id,ramdomstring,imei,accessType,conn);
			CaneConfirmationFarmerResponse dataLoadResponse=(CaneConfirmationFarmerResponse) confirnationList;
			if(confirnationList.isSuccess())
			{
				String yearCode=reqObj.has("yearCode")?reqObj.getString("yearCode"):"";
				String farmerCode=reqObj.has("nentityUniId")?reqObj.getString("nentityUniId"):"";
				String rujtype=reqObj.has("rujtype")?reqObj.getString("rujtype"):"";
				String caller=reqObj.has("caller")?reqObj.getString("caller"):"1";
				if(yearCode.trim().isEmpty() || farmerCode.trim().isEmpty() || (caller.equals("1") && rujtype.trim().isEmpty()))
				{
					dataLoadResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter farmerCode Or yearCode Or Caller");
					confirnationList.setSe(error);
				}else
				{
					boolean byPassVill = caller.equals("3") || ((confirnationList.getNuserRoleId().equals("113") || confirnationList.getNuserRoleId().equals("114") || confirnationList.getNuserRoleId().equals("115")) && caller.equals("2"));
					MasterDataLoadDao mdataLoad=new MasterDataLoadDao();
					MasterDataResponse villeageLoad=new MasterDataResponse();
					String villList="0";
					if(!byPassVill) {
						villeageLoad=mdataLoad.village(villeageLoad,chit_boy_id,conn);
						ArrayList<VillageMaster> VillageList=villeageLoad.getVillageList();
						for(VillageMaster  vm:VillageList)
						{
							if(villList.equals("0"))
								villList=""+vm.getNvillageId();
							else
								villList=villList+","+vm.getNvillageId();
						}
					}
					if(!villList.equals("0") || byPassVill)
					{
						dataLoadResponse=mdataLoad.caneConfirmationFarmerList(farmerCode,yearCode,rujtype,caller,villList, byPassVill,dataLoadResponse,conn);
					}
					
				}
				return dataLoadResponse;
			}
	}catch (Exception e) {
			confirnationList.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			confirnationList.setSe(error);
			e.printStackTrace();
		}
		return (CaneConfirmationFarmerResponse) confirnationList;
	
	}

	@Override
	public BankBranchResponse bankBranchByBankCode(JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType, MainResponse bankBranchList) {
		try(Connection conn=DBConnection.getConnection())
		{
			bankBranchList=login.verifyUser(bankBranchList,chit_boy_id,ramdomstring,imei,accessType,conn);
			BankBranchResponse dataLoadResponse=(BankBranchResponse) bankBranchList;
			if(bankBranchList.isSuccess())
			{
				String bankCode=reqObj.has("bankCode")?reqObj.getString("bankCode"):"";
				if(bankCode.trim().isEmpty())
				{
					dataLoadResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter bankCode");
					bankBranchList.setSe(error);
				}else
				{
					dataLoadResponse=mdataLoad.bankBranchByBankCode(bankCode,dataLoadResponse,conn);
				}
				return dataLoadResponse;
			}
	}catch (Exception e) {
			bankBranchList.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			bankBranchList.setSe(error);
			e.printStackTrace();
		}
		return (BankBranchResponse) bankBranchList;
	}

	@Override
	public BranchResponse bankBranchByCode(JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType,
			MainResponse branchRes) {
		try(Connection conn=DBConnection.getConnection())
		{
			branchRes=login.verifyUser(branchRes,chit_boy_id,ramdomstring,imei,accessType,conn);
			BranchResponse branchResponse=(BranchResponse) branchRes;
			if(branchResponse.isSuccess())
			{
				String bankCode=reqObj.has("branchCode")?reqObj.getString("branchCode"):"";
				if(bankCode.trim().isEmpty())
				{
					branchResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Invalid Parameter bankCode");
					branchResponse.setSe(error);
				}else
				{
					branchResponse=mdataLoad.bankBranchByCode(bankCode,branchResponse,conn);
				}
				return branchResponse;
			}
	}catch (Exception e) {
		branchRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			branchRes.setSe(error);
			e.printStackTrace();
		}
		return (BranchResponse) branchRes;
	}


}
