package com.twd.pandurangsugar.android.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.MainResponse;
import com.twd.pandurangsugar.android.bean.SavePrintResponse;
import com.twd.pandurangsugar.android.bean.ServerError;
import com.twd.pandurangsugar.android.bean.SugarSaleSavePrintResponse;
import com.twd.pandurangsugar.android.service.LoginService;
import com.twd.pandurangsugar.android.service.NumberSystemService;
import com.twd.pandurangsugar.android.serviceInterface.LoginServiceInterface;
import com.twd.pandurangsugar.android.serviceInterface.NumberSystemInterface;
import com.twd.pandurangsugar.both.constant.ConstantVeriables;

/**
 * Servlet implementation class SaveSugarSale
 */
@WebServlet("/numbersyssave")
public class Numbersyssave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Numbersyssave() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		LoginServiceInterface login = new LoginService();
		NumberSystemInterface numbersys=new NumberSystemService();
		MainResponse mainresponse= new SugarSaleSavePrintResponse();
		String json = null;
		String action = null;
		String imei = null;
		String chit_boy_id = null;
		String ramdomstring = null;
		String versionId = null;
		String accessType = null;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<?> items1 = null;
		try {
			items1 = upload.parseRequest(request); // upload.parseRequest(request);
			Iterator<?> itr1 = (Iterator<?>) items1.iterator();
			while (itr1.hasNext()) {
				FileItem item = (FileItem) itr1.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					if (name.equals("action")) {
						action = value;
					} else if (name.equals("json")) {
						json = value;
					} else if (name.equals("imei")) {
						imei = value;
					} else if (name.equals("chit_boy_id")) {
						chit_boy_id = value;
					} else if (name.equals("randomstring")) {
						ramdomstring = value;
					} else if (name.equals("versionId")) {
						versionId = value;
					} else if (name.equals("accessType")) {
						accessType = value;
					} 
				} else {
					String name = item.getFieldName();
					String itemName = item.getName();
					if ((itemName != null) && (!itemName.trim().equals(""))) {
						File directory = new File(ConstantVeriables.baseFilePath+ConstantVeriables.numbertaker);
						if(!directory.exists()) {
							directory.mkdir();
						}
						File savedFile = new File(directory, itemName);
						item.write(savedFile);
					}
				}
			}
			try(PrintWriter out=response.getWriter())
			{
				JSONObject result=null;
				if(accessType==null || accessType.trim().isEmpty())
					accessType="1";
				mainresponse=login.checkAppUpdate(versionId,accessType,mainresponse);
				if(mainresponse.getUpdateResponse()!=null && mainresponse.getUpdateResponse().isForceUpdate())
				{
					result=new JSONObject(mainresponse);
					out.print(result);
					return;
				}
				if(action.equalsIgnoreCase("savenumber"))
				{
					JSONObject reqObj=new JSONObject(json);
					MainResponse saveNumber= new SavePrintResponse();
					saveNumber=numbersys.saveNumber(reqObj, imei,ramdomstring,chit_boy_id,accessType,saveNumber);
					result=new JSONObject(saveNumber);
				}
				out.print(result);
				out.flush();
			}
			
		} catch (Exception e) {
			mainresponse=login.verifyUser(mainresponse,chit_boy_id,ramdomstring,imei,accessType);
			mainresponse.setSuccess(true);
			ServerError error=new ServerError();
			error.setError(ConstantVeriables.ERROR_006);
			error.setMsg("controller Issue "+e.getMessage());
			mainresponse.setSe(error);
			e.printStackTrace();
		}
	}

}
