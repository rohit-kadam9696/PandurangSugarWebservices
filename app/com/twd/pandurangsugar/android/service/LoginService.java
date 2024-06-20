package com.twd.pandurangsugar.android.service;

import java.sql.Connection;

import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.LoginResponse;
import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.dao.LoginDao;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.both.connection.DBConnection;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;
import com.twd.pandurangsugar.both.constant.RandomString;

public class LoginService implements LoginServiceInterface{
	LoginDao login=new LoginDao();
	
	@Override
	public LoginResponse appLogin(JSONObject reqObj,String imei,String accessType, LoginResponse res) {
		try(Connection conn=DBConnection.getConnection())
		{
			String mobileno=reqObj.getString("mobileno");
			res=login.verifyLogin(mobileno,accessType,conn);
			if(res.isSuccess())
			{
				String randamstring=RandomString.generateRandomString();
				res.setUniquestring(randamstring);
				res=login.checkUserHistory(accessType,res,conn);
				if(res.isSuccess())
				{
					res=login.updateImeiAndRandamString(accessType,res,imei,conn);
				}else
				{
					res=login.saveimeiAndRandamString(accessType,res,imei,conn);
				}
				if(res.isSuccess())
				{
					res=login.generateOTP(accessType,res,conn);
				}
			}
		} catch (Exception e) {
			res.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_001);
			error.setMsg("Connection Issue "+e.getMessage());
			res.setSe(error);
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public MainResponse verifyOTP(MainResponse otpRes,JSONObject reqObj, String imei, String ramdomstring, String chit_boy_id,String accessType) {
		try(Connection conn=DBConnection.getConnection())
		{
			otpRes=login.verifyUser(otpRes,chit_boy_id,ramdomstring,imei,accessType,conn);
			if(otpRes.isSuccess())
			{
				otpRes=login.verifyOTP(otpRes,reqObj.getString("otp"),conn);
			}
	}catch (Exception e) {
			otpRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			otpRes.setSe(error);
			e.printStackTrace();
		}
		return otpRes;
	}

	@Override
	public MainResponse resendOTP(MainResponse resendOTPRes, JSONObject reqObj, String imei, String ramdomstring,
			String chit_boy_id,String accessType) {
		try(Connection conn=DBConnection.getConnection())
		{
			resendOTPRes=login.verifyUser(resendOTPRes,chit_boy_id,ramdomstring,imei,accessType,conn);
			if(resendOTPRes.isSuccess())
			{
				String mobileno=reqObj.getString("mobileno");
				LoginResponse loginrRes=new LoginResponse();
				loginrRes.setMobileno(mobileno);
				loginrRes=login.generateOTP(accessType,loginrRes, conn);
				if(!loginrRes.isSuccess())
				{
					resendOTPRes.setSe(loginrRes.getSe());
					resendOTPRes.setSuccess(false);
				}
			}
	}catch (Exception e) {
		resendOTPRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			resendOTPRes.setSe(error);
			e.printStackTrace();
		}
		return resendOTPRes;
	}

	@Override
	public MainResponse checkAppUpdate(String versionId,String accessType,MainResponse appversionRes) {
		try(Connection conn=DBConnection.getConnection())
		{
			appversionRes=login.checkAppUpdate(appversionRes,versionId,accessType,conn);
		}catch (Exception e) {
			appversionRes.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			appversionRes.setSe(error);
			e.printStackTrace();
		}
		return appversionRes;
	}

	@Override
	public MainResponse verifyUser(MainResponse actionResponse, String chit_boy_id, String ramdomstring, String imei,String accessType) {
		try(Connection conn=DBConnection.getConnection())
		{
			actionResponse=login.verifyUser(actionResponse,chit_boy_id,ramdomstring,imei,accessType,conn);
		}catch (Exception e) {
			actionResponse.setSuccess(false);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("Connection Issue "+e.getMessage());
			actionResponse.setSe(error);
			e.printStackTrace();
		}
		return actionResponse;
	}
}
