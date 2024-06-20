package com.twd.pandurangsugar.both.constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SendSMS {
	public static void sendTxtSMS(String m, String text) throws IOException{
		HttpsURLConnection connection = null;
		String msg=java.net.URLEncoder.encode(text, "UTF-8");
		//https://www.businesssms.co.in
String targetURL="https://messaging.charteredinfo.com/smsaspx?ID=tanaji_bhosale@yahoo.co.in&Pwd=F@ntasy@12345&PhNo="+m+"&Text="+msg+"&TemplateID=1007111074714711909";
		URL url = new URL(targetURL);
		    connection = (HttpsURLConnection)url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
		  BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
}
	
	public static void main(String[] args) throws Exception {
		try {
		   
			/*String Str2="Dear "+"Surendra Hanumant Thorat"+", Cane Weight Info"
					+ " \n Code_No :"+11
					+ "\n Slip_No :"+100
					+ " \n Date :"+23/11/2015
					+ " \n Net Weight :"+2.050;*/
			String str1="10155 is your otp for SPSSK Mobile App";
			sendTxtSMS("9561800885",str1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
