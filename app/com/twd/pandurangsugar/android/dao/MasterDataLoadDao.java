package com.twd.pandurangsugar.android.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.twd.convertismtouni.DemoConvert2;
import com.twd.pandurangsugar.android.bean.BankBranchMaster;
import com.twd.pandurangsugar.android.bean.BankBranchResponse;
import com.twd.pandurangsugar.android.bean.BankMaster;
import com.twd.pandurangsugar.android.bean.BranchResponse;
import com.twd.pandurangsugar.android.bean.CaneConfirmationFarmerResponse;
import com.twd.pandurangsugar.android.bean.CaneConfirmationRegistrationList;
import com.twd.pandurangsugar.android.bean.CaneTypeMaster;
import com.twd.pandurangsugar.android.bean.CaneYard;
import com.twd.pandurangsugar.android.bean.CropTypeMaster;
import com.twd.pandurangsugar.android.bean.CropWater;
import com.twd.pandurangsugar.android.bean.EntityMasterDetail;
import com.twd.pandurangsugar.android.bean.FarmerCategoryMaster;
import com.twd.pandurangsugar.android.bean.FarmerTypeMaster;
import com.twd.pandurangsugar.android.bean.FertSaleType;
import com.twd.pandurangsugar.android.bean.HangamMaster;
import com.twd.pandurangsugar.android.bean.HarvestingType;
import com.twd.pandurangsugar.android.bean.IrrigationTypeMaster;
import com.twd.pandurangsugar.android.bean.LaneType;
import com.twd.pandurangsugar.android.bean.MasterDataResponse;
import com.twd.pandurangsugar.android.bean.MenuBean;
import com.twd.pandurangsugar.android.bean.ReasonMaster;
import com.twd.pandurangsugar.android.bean.SectionMaster;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.VarietyMaster;
import com.twd.pandurangsugar.android.bean.VehicleGroupMaster;
import com.twd.pandurangsugar.android.bean.VehicleTypeMaster;
import com.twd.pandurangsugar.android.bean.VillageMaster;
import com.twd.pandurangsugar.both.constant.Constant;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;


public class MasterDataLoadDao {


	public MasterDataResponse loadBank(MasterDataResponse dataLoadResponse, Connection conn) {
				try
				{
					ArrayList<BankMaster> bankList = new ArrayList<>();
					try(PreparedStatement pst=conn.prepareStatement("select t.nbank_id,TO_NCHAR(t.vbank_name_local)as vbank_name_local from cb_m_bank_master t ORDER BY t.vbank_name_local ASC"))
					{
						try(ResultSet rs=pst.executeQuery())
						{
							while(rs.next())
							{
								BankMaster bank=new BankMaster();
								bank.setNbankId(rs.getInt("nbank_id"));
								bank.setVbankNameLocal(DemoConvert2.ism_to_uni(rs.getString("vbank_name_local")));
								bankList.add(bank);
							}
						}
					}
					dataLoadResponse.setBankList(bankList);
				}
					catch (SQLException e) {
						dataLoadResponse.setSuccess(false);
						ServerError error=new ServerError();
						error.setError(ConstantVeriables.ERROR_006);
						error.setMsg("Bank Issue "+e.getMessage());
						dataLoadResponse.setSe(error);
						e.printStackTrace();
					}
		return dataLoadResponse;
	}

	public MasterDataResponse CropType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<CropTypeMaster> CropTypeList = new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.ncrop_id,TO_NCHAR(t.vcrop_name)as vcrop_name from cr_m_crop_type_master t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						CropTypeMaster crop=new CropTypeMaster();
						crop.setNcropId(rs.getInt("ncrop_id"));
						crop.setVcropName(DemoConvert2.ism_to_uni(rs.getString("vcrop_name")));
						CropTypeList.add(crop);
					}
				}
			}
			dataLoadResponse.setCropTypeList(CropTypeList);
		}
			catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Crop Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
return dataLoadResponse;
	}

	public MasterDataResponse farmerList(MasterDataResponse dataLoadResponse, String villList, String chit_boy_id, Connection conn) {
		try
		{
			ArrayList<EntityMasterDetail> farmerList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as ventity_name_local,"
					+ "t.vmobile_no,t.nbank_id,t.vbank_ac_no,t.nvillage_id,t.nfarmer_type_id from GM_M_ENTITY_MASTER_DETAIL t where t.nentity_category_id = 1 and t.nvillage_id IN("+villList+") and t.vblocked = 'N' order by t.ventity_name_local"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						EntityMasterDetail farmer=new EntityMasterDetail();
						farmer.setNbankId(rs.getInt("nbank_id"));
						farmer.setNentityUniId(rs.getString("nentity_uni_id"));
						farmer.setNfarmerTypeId(rs.getInt("nfarmer_type_id"));
						farmer.setNvillageId(rs.getInt("nvillage_id"));
						farmer.setVbankAcNo(rs.getString("vbank_ac_no"));
						farmer.setVentityNameLocal(rs.getString("ventity_name_local")!=null?DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")):"NO Name");
						farmer.setVmobileNo(rs.getString("vmobile_no"));
						farmerList.add(farmer);
					}
				}
			}
			
			try(PreparedStatement pst=conn.prepareStatement("select t.nentity_uni_id,TO_NCHAR(t.ventity_name_local)as ventity_name_local,t.vmobile_no,t.nbank_id,t.vbank_ac_no,t.nvillage_id,t.nfarmer_type_id from GM_M_ENTITY_MASTER_DETAIL t,cr_t_registration_approval t1 where t.nentity_uni_id=t1.nentity_uni_id and t.nentity_category_id = 1 and t.vblocked = 'N'  and t1.vstatus=2 and t1.nuser_id=? order by t.ventity_name_local"))
			{
				pst.setString(1, chit_boy_id);
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						EntityMasterDetail farmer=new EntityMasterDetail();
						farmer.setNbankId(rs.getInt("nbank_id"));
						farmer.setNentityUniId(rs.getString("nentity_uni_id"));
						farmer.setNfarmerTypeId(rs.getInt("nfarmer_type_id"));
						farmer.setNvillageId(rs.getInt("nvillage_id"));
						farmer.setVbankAcNo(rs.getString("vbank_ac_no"));
						farmer.setVentityNameLocal(rs.getString("ventity_name_local")!=null?DemoConvert2.ism_to_uni(rs.getString("ventity_name_local")):"NO Name");
						farmer.setVmobileNo(rs.getString("vmobile_no"));
						farmerList.add(farmer);
					}
				}
			}
			dataLoadResponse.setFarmerList(farmerList);
		}
			catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("farmer Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		
		return dataLoadResponse;
	}

	public MasterDataResponse FarmerCategory(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<FarmerCategoryMaster> FarmerCategoryList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nfarmer_cat_id,TO_NCHAR(t.vfarmer_cat_name_local) as vfarmer_cat_name_local from gm_m_farmer_category_master t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						FarmerCategoryMaster farmerCategory=new FarmerCategoryMaster();
						farmerCategory.setNfarmerCatId(rs.getInt("nfarmer_cat_id"));
						farmerCategory.setVfarmerCatNameLocal(DemoConvert2.ism_to_uni(rs.getString("vfarmer_cat_name_local")));
						FarmerCategoryList.add(farmerCategory);
					}
				}
			}
			dataLoadResponse.setFarmerCategoryList(FarmerCategoryList);
		}
			catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Farmer Category Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse FarmerType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<FarmerTypeMaster> FarmerTypeList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nfarmer_type_id,TO_NCHAR(t.vfarmer_type_name_local) as vfarmer_type_name_local,t.nfarmer_cat_id from gm_m_farmer_type_master t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						FarmerTypeMaster farmerType=new FarmerTypeMaster();
						farmerType.setNfarmerCatId(rs.getInt("nfarmer_cat_id"));
						farmerType.setNfarmerTypeId(rs.getInt("nfarmer_type_id"));
						farmerType.setVfarmerTypeNameLocal(DemoConvert2.ism_to_uni(rs.getString("vfarmer_type_name_local")));
						FarmerTypeList.add(farmerType);
					}
				}
			}
			dataLoadResponse.setFarmerTypeList(FarmerTypeList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("farmer Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse Hangam(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<HangamMaster> HangamList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nhangam_id,TO_NCHAR(t.vhangam_name)as "
					+ "vhangam_name,t.dhangam_start_date,t.dhangam_end_date from cr_m_hangam_master t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						HangamMaster hungam=new HangamMaster();
						hungam.setNhangamId(rs.getInt("nhangam_id"));
						hungam.setDhangamStartDate(rs.getString("dhangam_start_date"));
						hungam.setDhangamEndDate(rs.getString("dhangam_end_date"));
						hungam.setVhangamName(DemoConvert2.ism_to_uni(rs.getString("vhangam_name")));
						HangamList.add(hungam);
					}
				}
			}
			dataLoadResponse.setHangamList(HangamList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Hangam Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse HarvestingType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<HarvestingType> HarvestingTypeList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nharvest_type_id,TO_NCHAR(t.vharvest_type_name_local)as vharvest_type_name_local from cr_m_harvesting_type t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						HarvestingType harvestingtype=new HarvestingType();
						harvestingtype.setNharvestTypeId(rs.getInt("nharvest_type_id"));
						harvestingtype.setVharvestTypeNameLocal(DemoConvert2.ism_to_uni(rs.getString("vharvest_type_name_local")));
						HarvestingTypeList.add(harvestingtype);
					}
				}
			}
			dataLoadResponse.setHarvestingTypeList(HarvestingTypeList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Harvesting Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse IrrigationType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<IrrigationTypeMaster> IrrigationTypeList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nirrigation_id,TO_NCHAR(t.virrigation_name)as virrigation_name from cr_m_irrigation_type_master t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						IrrigationTypeMaster irrigationType=new IrrigationTypeMaster();
						irrigationType.setNirrigationId(rs.getInt("nirrigation_id"));
						irrigationType.setVirrigationName(DemoConvert2.ism_to_uni(rs.getString("virrigation_name")));
						IrrigationTypeList.add(irrigationType);
					}
				}
			}
			dataLoadResponse.setIrrigationTypeList(IrrigationTypeList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Irrigation Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse LagneType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<LaneType> LagneTypekList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nlane_type_id,TO_NCHAR(t.vlane_type_name_local)as vlane_type_name_local from cr_m_lane_type t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						LaneType ltype=new LaneType();
						ltype.setNlaneTypeId(rs.getInt("nlane_type_id"));
						ltype.setVlaneTypeNameLocal(DemoConvert2.ism_to_uni(rs.getString("vlane_type_name_local")));
						LagneTypekList.add(ltype);
					}
				}
			}
			dataLoadResponse.setLagneTypekList(LagneTypekList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Lagne Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse menu(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<MenuBean> menuList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.ngroupid,t.nandroid_order,TO_NCHAR(t.vgroupname)as vgroupname from APP_M_MENU t where t.vstatus_active=1  order by t.nandroid_order asc "))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						MenuBean menu=new MenuBean();
						menu.setGroupId(rs.getString("ngroupid"));
						menu.setGroupName(DemoConvert2.ism_to_uni(rs.getString("vgroupname")));
						menu.setNandroidOrder(rs.getString("nandroid_order"));
						menuList.add(menu);
					}
				}
			}
			dataLoadResponse.setMenuList(menuList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Menu Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse section(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<SectionMaster> SectionList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nsection_id,TO_NCHAR(t.vsection_name_local)as vsection_name_local from gm_m_section_master t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						SectionMaster section=new SectionMaster();
						section.setNsectionId(rs.getInt("nsection_id"));
						section.setVsectionNameLocal(DemoConvert2.ism_to_uni(rs.getString("vsection_name_local")));
						SectionList.add(section);
					}
				}
			}
			dataLoadResponse.setSectionList(SectionList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Section Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse variety(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<VarietyMaster> VarietyList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.navariety_id,TO_NCHAR(t.vvariety_name) as vvariety_name,t.nharvesting_seq from cr_m_variety_master t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						VarietyMaster varity=new VarietyMaster();
							varity.setNavarietyId(rs.getInt("navariety_id"));
							varity.setVvariety_name(DemoConvert2.ism_to_uni(rs.getString("vvariety_name")));
							varity.setNharvestingSeq(rs.getInt("nharvesting_seq"));
						VarietyList.add(varity);
					}
				}
			}
			dataLoadResponse.setVarietyList(VarietyList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Variety Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse cropWater(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<CropWater> cropList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.ncropwater_condition,TO_NCHAR(t.vcropwater_condition_local)as name from cr_m_cropwater t"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						CropWater crop=new CropWater();
						crop.setNcropwaterCondition(rs.getInt("ncropwater_condition"));
						crop.setVcropwaterCondition(DemoConvert2.ism_to_uni(rs.getString("name")));
						cropList.add(crop);
					}
				}
			}
			dataLoadResponse.setCropWaterList(cropList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("cropWater Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}
	
	public MasterDataResponse vehicleType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<VehicleTypeMaster> VehicleTypeList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.ndropdown_id,t.nvehicle_type_id,TO_NCHAR(t.vvehicle_type_name_local) as vvehicle_type_name_local,t.ndropdown_id from gm_m_vehicle_type_master t order by t.ndropdown_id ASC "))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						VehicleTypeMaster vehicleType=new VehicleTypeMaster();
						vehicleType.setNvehicleTypeId(rs.getInt("nvehicle_type_id"));
						vehicleType.setNdropdownId(rs.getInt("ndropdown_id"));
						vehicleType.setVvehicleTypeNameLocal(DemoConvert2.ism_to_uni(rs.getString("vvehicle_type_name_local")));
						VehicleTypeList.add(vehicleType);
					}
				}
			}
			dataLoadResponse.setVehicleTypeList(VehicleTypeList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("vehicle Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse village(MasterDataResponse dataLoadResponse, String chit_boy_id, Connection conn) {
		try
		{
			ArrayList<VillageMaster> VillageList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nvillage_id,TO_NCHAR(t.village_name_local)as village_name_local,t.nsection_id,t.nvillage_distance,t.vunder_area from gm_m_village_master t WHERE (t.napp_user_1=? OR t.napp_user_2=?)"))
			{
				pst.setString(1, chit_boy_id);
				pst.setString(2, chit_boy_id);
				try(ResultSet rs=pst.executeQuery())
				{
					
					while(rs.next())
					{
						VillageMaster village=new VillageMaster();
						village.setNvillageId(rs.getInt("nvillage_id"));
						village.setNvillageDistance(rs.getString("nvillage_distance"));
						village.setVunderArea(rs.getString("vunder_area"));
						village.setNsectionId(rs.getInt("nsection_id"));
						village.setVillageNameLocal(DemoConvert2.ism_to_uni(rs.getString("village_name_local")));
						VillageList.add(village);
					}
				}
			}
			dataLoadResponse.setVillageList(VillageList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("village Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}
	
	public MasterDataResponse plantationVehicleType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<VehicleTypeMaster> VehicleTypeList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nvehicle_type_id,to_NCHAR(t.vvehicle_type_name_local)as vvehicle_type_name_local from GM_M_VEHICLE_TYPE_MASTER_MAIN t order by  t.nvehicle_type_id ASC "))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						VehicleTypeMaster vehicleType=new VehicleTypeMaster();
						vehicleType.setNvehicleTypeId(rs.getInt("nvehicle_type_id"));
						vehicleType.setVvehicleTypeNameLocal(DemoConvert2.ism_to_uni(rs.getString("vvehicle_type_name_local")));
						VehicleTypeList.add(vehicleType);
					}
				}
			}
			dataLoadResponse.setPlantationVehicleTypeList(VehicleTypeList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("vehicle Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public CaneConfirmationFarmerResponse caneConfirmationFarmerList(String farmerCode, String yearCode,
			String rujtype,String caller, String villList, boolean byPassVill, CaneConfirmationFarmerResponse dataLoadResponse, Connection conn) {
		try
		{
			String sql="select TO_NCHAR(t1.village_name_local) as vill_name,t1.nsection_id, TO_NCHAR(t.ventity_name_local) as farmer_name from GM_M_ENTITY_MASTER_DETAIL t,GM_M_VILLAGE_MASTER t1 where t.nvillage_id=t1.nvillage_id and t.nentity_uni_id=?";
			if(!byPassVill)
					sql+= " and t.nvillage_id IN("+villList+")";
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				pst.setString(1, farmerCode);
				try(ResultSet rs=pst.executeQuery())
				{
					if(rs.next())
					{
						if(byPassVill)
							dataLoadResponse.setFarmerName(rs.getString("farmer_name")!=null?DemoConvert2.ism_to_uni(rs.getString("farmer_name")): null);
						dataLoadResponse.setFarmerVilleageName(rs.getString("vill_name")!=null?DemoConvert2.ism_to_uni(rs.getString("vill_name")):"No Village Name");
						dataLoadResponse.setSectionCode(rs.getString("nsection_id"));
					}
				}
			}
			DecimalFormat df=new DecimalFormat("#0.00");
			sql="select t.njune_flag,t.naugust_flag,t.nvillage_id,t.nplot_no,t.nhangam_id,t.narea,t.nvariety_id,t.dplantation_date,t.nexpected_yield, t.ntentative_area";
			if (byPassVill) 
				sql += ", TO_NCHAR(vm.village_name_local)as vill_name ";
			sql += " from " + ConstantVeriables.tabeConfirmAgriPlantaton + " t ";
			if (byPassVill)
				sql += ", GM_M_VILLAGE_MASTER vm where vm.nvillage_id=t.nvillage_id and";
			else
				sql += " Where ";
			sql += " t.nentity_uni_id=? and t.vyear_id=?";
			if(!byPassVill)
				sql+= " and t.nvillage_id IN("+villList+")";
			if(caller.equals("1"))
			{
				if(rujtype.equals("1"))
					sql+=" AND t.njune_flag=0 AND t.naugust_flag=0 ";
				else
					sql+=" AND t.naugust_flag=0";
			}
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				pst.setString(1, farmerCode);
				pst.setString(2, yearCode);
				try(ResultSet rs=pst.executeQuery())
				{
					List<CaneConfirmationRegistrationList> datalist=new ArrayList<>();
					while(rs.next())
					{
						CaneConfirmationRegistrationList data=new CaneConfirmationRegistrationList();
						
						data.setNcaneVarity(rs.getString("nvariety_id"));
						data.setNhungamCode(rs.getString("nhangam_id"));
						data.setPlotNo(rs.getString("nplot_no"));
						if(byPassVill)
							data.setVilleageCode(rs.getString("vill_name")!=null?DemoConvert2.ism_to_uni(rs.getString("vill_name")):"No Village Name");
						else 
							data.setVilleageCode(rs.getString("nvillage_id"));
						data.setNexpectedYield(Constant.decimalFormat(rs.getDouble("nexpected_yield"), "000"));
						data.setDplantationDate(Constant.DbDateToAppDate(rs.getDate("dplantation_date")));
						if(!caller.equals("1"))
						{
							data.setNarea(df.format(rs.getDouble("ntentative_area")));
							String vstatus="Registered";
							if(rs.getInt("naugust_flag")!=0)
								vstatus="Aug Confirm";
							else if(rs.getInt("njune_flag")!=0)
								vstatus="June Confirm";
							else {
								vstatus="Registered";
								data.setNarea(df.format(rs.getDouble("narea")));
							}
							data.setVstatus(vstatus);
						} else {
							data.setNarea(df.format(rs.getDouble("narea")));
						}
						datalist.add(data);
					}
					dataLoadResponse.setList(datalist);
				}
			}
			
		}catch (Exception e) {
			dataLoadResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Cane Confirmation Load Data Issue "+e.getMessage());
			dataLoadResponse.setSe(error);
			e.printStackTrace();
		}
		return dataLoadResponse;
	}
	
	public BankBranchResponse bankBranchByBankCode(String bankCode, BankBranchResponse dataLoadResponse,
			Connection conn) {
		try(PreparedStatement pst=conn.prepareStatement("select t.nbranch_id,TO_NCHAR(t.vbranch_name_local)as vbranch_name,t.ifsc_code from CB_M_BANK_BRANCH_MASTER t where t.nbank_id = ? order by t.vbranch_name_local ASC"))
		{
			pst.setString(1, bankCode);
			try(ResultSet rs=pst.executeQuery())
			{
				ArrayList<BankBranchMaster> list=new ArrayList<>();
				while(rs.next())
				{
					BankBranchMaster branch=new BankBranchMaster();
					branch.setIfscCode(rs.getString("ifsc_code"));
					branch.setNbranchId(rs.getInt("nbranch_id"));
					branch.setVbranchName(DemoConvert2.ism_to_uni(rs.getString("vbranch_name")));
					list.add(branch);
				}
				if(list.size()>0)
				{
					dataLoadResponse.setSuccess(true);
					dataLoadResponse.setBankBranchList(list);
				}else
				{
					dataLoadResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Bank Branch Information Not Found ");
					dataLoadResponse.setSe(error);
				}
				
			}
		} catch (SQLException e) {
			dataLoadResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Bank Branch Load Issue "+e.getMessage());
			dataLoadResponse.setSe(error);
			e.printStackTrace();
		}
		return dataLoadResponse;
	}

	public BranchResponse bankBranchByCode(String bankCode, BranchResponse branchResponse, Connection conn) {

		try(PreparedStatement pst=conn.prepareStatement("select t.nbranch_id,TO_NCHAR(t.vbranch_name_local)as vbranch_name,t.ifsc_code from CB_M_BANK_BRANCH_MASTER t where t.nbranch_id = ? order by t.vbranch_name_local"))
		{
			pst.setString(1, bankCode);
			try(ResultSet rs=pst.executeQuery())
			{
				if(rs.next())
				{
					branchResponse.setIfscCode(rs.getString("ifsc_code"));
					branchResponse.setNbranchId(rs.getInt("nbranch_id"));
					branchResponse.setVbranchName(DemoConvert2.ism_to_uni(rs.getString("vbranch_name")));
				}else
				{
					branchResponse.setSuccess(false);
					ServerError error=new ServerError();
					error.setError(ConstantVeriables.ERROR_006);
					error.setMsg("Bank Branch Information Not Found ");
					branchResponse.setSe(error);
				}
			}
		} catch (SQLException e) {
			branchResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Bank Branch Load Issue "+e.getMessage());
			branchResponse.setSe(error);
			e.printStackTrace();
		}
		return branchResponse;
	
	}

	public MasterDataResponse caneConfirmationFarmerList(MasterDataResponse dataLoadResponse, String yearCode,
			String villList, Connection conn) {
		try
		{
			DecimalFormat df=new DecimalFormat("#0.00");
			String sql="select t.nconfirm_flag, t.ntentative_area, t.nexpected_yield, t.ncropwater_condition, t.nentity_uni_id,t.njune_flag,t.naugust_flag,t.vsurve_no,t.nvillage_id,t.nplot_no,t.nhangam_id,t.narea,t.nvariety_id,t.dplantation_date,TO_NCHAR(t2.village_name_local)as vill_name,t2.nsection_id from  "+ConstantVeriables.tabeConfirmAgriPlantaton+" t,GM_M_ENTITY_MASTER_DETAIL t1,GM_M_VILLAGE_MASTER t2 where t1.nvillage_id=t2.nvillage_id and t.nentity_uni_id=t1.nentity_uni_id and t.nconfirm_flag<=3 and t.nvillage_id in("+villList+") AND t.vyear_id=? ";
			try(PreparedStatement pst=conn.prepareStatement(sql))
			{
				pst.setString(1, yearCode);
				try(ResultSet rs=pst.executeQuery())
				{
					List<CaneConfirmationRegistrationList> datalist=new ArrayList<>();
					while(rs.next())
					{
						CaneConfirmationRegistrationList data=new CaneConfirmationRegistrationList();
						data.setNexpectedYield(rs.getString("nexpected_yield"));
						data.setNcropwaterCondition(rs.getLong("ncropwater_condition"));
						data.setNtentativeArea(df.format(rs.getDouble("ntentative_area")));
						data.setYearCode(yearCode);
						data.setNentityUniId(rs.getString("nentity_uni_id"));
						data.setNarea(df.format(rs.getDouble("narea")));
						data.setNcaneVarity(rs.getString("nvariety_id"));
						data.setNhungamCode(rs.getString("nhangam_id"));
						data.setPlotNo(rs.getString("nplot_no"));
						data.setVilleageCode(rs.getString("nvillage_id"));
						data.setServeNo(rs.getString("vsurve_no"));
						data.setNconfirmFlag(rs.getString("nconfirm_flag"));
						data.setDplantationDate(Constant.DbDateToAppDate(rs.getDate("dplantation_date")));
						data.setNjuneFlag(rs.getString("njune_flag"));
						data.setNaugustFlag(rs.getString("naugust_flag"));
						data.setFarmerVilleageName(DemoConvert2.ism_to_uni(rs.getString("vill_name")));
						data.setSectionCode(rs.getString("nsection_id"));
						datalist.add(data);
					}
					dataLoadResponse.setCaneConfirmList(datalist);
				}
			}
			
		}catch (Exception e) {
			dataLoadResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Cane Confirmation Load Data Issue "+e.getMessage());
			dataLoadResponse.setSe(error);
			e.printStackTrace();
		}
		return dataLoadResponse;
	}

	public MasterDataResponse caneType(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<CaneTypeMaster> caneTypeList= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.ncrop_type_id,TO_NCHAR(t.vcroptype_name_local) as vcroptype_name_local from WB_M_CROP_TYPE_MASTER t order by t.ncrop_type_id asc"))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						CaneTypeMaster ltype=new CaneTypeMaster();
						ltype.setNcaneTypeId(rs.getInt("ncrop_type_id"));;
						ltype.setVcaneTypeName(DemoConvert2.ism_to_uni(rs.getString("vcroptype_name_local")));;
						caneTypeList.add(ltype);
					}
				}
			}
			dataLoadResponse.setCaneTypeMasters(caneTypeList);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("Lagne Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	
	}

	public MasterDataResponse remarkList(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<ReasonMaster> list= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nremark_id,t.vremark_name_local from WB_M_SLIP_REMARK t order by t.nremark_id "))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						ReasonMaster vehicleType=new ReasonMaster();
						vehicleType.setNremarkId(rs.getInt("nremark_id"));
						vehicleType.setVremarkNameLocal(DemoConvert2.ism_to_uni(rs.getString("vremark_name_local")));
						
						list.add(vehicleType);
					}
				}
			}
			dataLoadResponse.setReasonMasters(list);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("vehicle Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

	public MasterDataResponse fertSaleTypeList(MasterDataResponse dataLoadResponse, Connection conn) {
		try {
			ArrayList<FertSaleType> list = new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nsale_type_id, t.vsale_type_name, To_NCHAR(t.vsale_type_name_local) vsale_type_name_local from IN_M_FERTILIZER_SALE_TYPE t")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						FertSaleType vehicleType = new FertSaleType();
						vehicleType.setNsaleTypeId(rs.getInt("nsale_type_id"));
						vehicleType.setVsaleTypeNameMarathi(DemoConvert2.ism_to_uni(rs.getString("vsale_type_name_local")));
						vehicleType.setVsaleTypeName(rs.getString("vsale_type_name"));
						list.add(vehicleType);
					}
				}
			}
			dataLoadResponse.setFertSaleTypes(list);
		} catch (SQLException e) {
			dataLoadResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Fert Sale Type Issue " + e.getMessage());
			dataLoadResponse.setSe(error);
			e.printStackTrace();
		}
		return dataLoadResponse;
	}

	public MasterDataResponse caneYardList(MasterDataResponse dataLoadResponse, Connection conn) {
		try {
			ArrayList<CaneYard> list = new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nyard_id, To_NCHAR(t.vyard_name_mar) as vyard_name_mar, t.vtractor_truck, t.vbajat from APP_M_CANEYARD t")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						CaneYard caneYard = new CaneYard();
						caneYard.setNyardId(rs.getInt("nyard_id"));
						caneYard.setVyardName(DemoConvert2.ism_to_uni(rs.getString("vyard_name_mar")));
						caneYard.setVtruckTracktor(rs.getString("vtractor_truck"));
						caneYard.setVbajat(rs.getString("vbajat"));
						list.add(caneYard);
					}
				}
			}
			dataLoadResponse.setCaneYards(list);
		} catch (SQLException e) {
			dataLoadResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Fert Sale Type Issue " + e.getMessage());
			dataLoadResponse.setSe(error);
			e.printStackTrace();
		}
		return dataLoadResponse;
	}

	public MasterDataResponse vehicleGroupList(MasterDataResponse dataLoadResponse, Connection conn) {
		try {
			ArrayList<VehicleGroupMaster> list = new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nvehicle_group_id, TO_NCHAR(t.vvehicle_group_name_local) as vvehicle_group_name_local from APP_M_VEHICLE_GROUP t")) {
				try (ResultSet rs = pst.executeQuery()) {
					while (rs.next()) {
						VehicleGroupMaster groupMaster = new VehicleGroupMaster();
						groupMaster.setNvehicleGroupId(rs.getInt("nvehicle_group_id"));
						groupMaster.setVvehicleGroupName(DemoConvert2.ism_to_uni(rs.getString("vvehicle_group_name_local")));
						list.add(groupMaster);
					}
				}
			}
			dataLoadResponse.setVehicleGroupMasters(list);
		} catch (SQLException e) {
			dataLoadResponse.setSuccess(false);
			ServerError error = new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Fert Sale Type Issue " + e.getMessage());
			dataLoadResponse.setSe(error);
			e.printStackTrace();
		}
		return dataLoadResponse;
	}

	public MasterDataResponse resonList(MasterDataResponse dataLoadResponse, Connection conn) {
		try
		{
			ArrayList<ReasonMaster> list= new ArrayList<>();
			try(PreparedStatement pst=conn.prepareStatement("select t.nreason_id,t.vreason_name,t.nreason_group_id from APP_M_REASON t order by t.nreason_id "))
			{
				try(ResultSet rs=pst.executeQuery())
				{
					while(rs.next())
					{
						ReasonMaster resondata=new ReasonMaster();
						resondata.setNremarkId(rs.getInt("nreason_id"));
						resondata.setVremarkNameLocal(DemoConvert2.ism_to_uni(rs.getString("vreason_name")));
						resondata.setNremarkGroupId(rs.getInt("nreason_group_id"));
						list.add(resondata);
					}
				}
			}
			dataLoadResponse.setReasonAllMasters(list);
		}catch (SQLException e) {
				dataLoadResponse.setSuccess(false);
				ServerError error=new ServerError();
				error.setError(ConstantVeriables.ERROR_006);
				error.setMsg("vehicle Type Issue "+e.getMessage());
				dataLoadResponse.setSe(error);
				e.printStackTrace();
			}
		return dataLoadResponse;
	}

}
