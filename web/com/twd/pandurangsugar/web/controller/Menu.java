package com.twd.pandurangsugar.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.twd.pandurangsugar.android.bean.KeyPairBoolData;
import com.twd.pandurangsugar.both.constant.RedirectClass;
import com.twd.pandurangsugar.web.bean.Department;
import com.twd.pandurangsugar.web.bean.Permission;
import com.twd.pandurangsugar.web.bean.UserBean;
import com.twd.pandurangsugar.web.service.ReportService;
import com.twd.pandurangsugar.web.service.UserService;
import com.twd.pandurangsugar.web.serviceInterface.ReportServiceInterface;
import com.twd.pandurangsugar.web.serviceInterface.UserServiceInterface;

/**
 * Servlet implementation class Menu
 */
@WebServlet("/menu")
public class Menu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Menu() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url=request.getParameter("url");
		int userId=RedirectClass.MyRedirect(request, response);
		if(userId!=0)
		{
			UserServiceInterface user=new UserService();
			ReportServiceInterface report=new ReportService();
			switch(url)
			{
			case "systemuser":
				request.getRequestDispatcher("adduser.jsp").forward(request, response);
				break;
			case "viewUserList":
				ArrayList<UserBean> viewUserList=new ArrayList<>();
				viewUserList=user.viewUserList(viewUserList);
				request.setAttribute("viewUserList", viewUserList);
				request.getRequestDispatcher("list/systemuser.jsp").forward(request, response);
				break;
			case "plantationMapReport":
				ArrayList<KeyPairBoolData> sectionList=new ArrayList<>();
				sectionList=report.viewSectionList(sectionList);
				request.setAttribute("sectionList", sectionList);
				ArrayList<String> yearList=new ArrayList<>();
				yearList=report.viewYearList(yearList);
				request.setAttribute("yearList", yearList);
				request.setAttribute("currentYear", yearList.size()>0?yearList.get(0):"");
				request.getRequestDispatcher("report/plantationmap.jsp").forward(request, response);
				break;
			case "loadVillageBySection":
				String nsectionId=request.getParameter("nsectionId");
				ArrayList<KeyPairBoolData> villageList=new ArrayList<>();
				villageList=report.viewVillageList(nsectionId,villageList);
				JSONArray list=new JSONArray(villageList);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				try(PrintWriter out=response.getWriter())
				{
					out.print(list);
					out.flush();
				}
				break;
			case "loadFarmerByVillege":
				String nvillageId=request.getParameter("nvillageId");
				String yearId=request.getParameter("yearId");
				ArrayList<KeyPairBoolData> farmerList=new ArrayList<>();
				farmerList=report.loadFarmerByVillege(nvillageId,yearId,farmerList);
				list=new JSONArray(farmerList);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				try(PrintWriter out=response.getWriter())
				{
					out.print(list);
					out.flush();
				}
				break;
			case "viewPlantationMapReport":
				nsectionId=request.getParameter("nsectionId");
				nvillageId=request.getParameter("nvillageId");
				yearId=request.getParameter("yearId");
				String nfarmerCode=request.getParameter("nfarmerCode");
				JSONObject plantationMapReport=new JSONObject();
				plantationMapReport=report.viewPlantationMapReport(yearId,nvillageId,nfarmerCode,plantationMapReport);
				request.setAttribute("plantationMapReport", plantationMapReport);
				sectionList=new ArrayList<>();
				sectionList=report.viewSectionList(sectionList);
				request.setAttribute("sectionList", sectionList);
				yearList=new ArrayList<>();
				yearList=report.viewYearList(yearList);
				request.setAttribute("yearList", yearList);
				request.setAttribute("currentYear", yearId);
				request.setAttribute("nsectionId",nsectionId);
				request.setAttribute("nfarmerCode",nfarmerCode);
				request.setAttribute("nvillageId",nvillageId);
				request.getRequestDispatcher("report/plantationmap.jsp").forward(request, response);
				break;
				default:
				request.getRequestDispatcher("404.jsp").forward(request, response);
				break;
			}
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url=request.getParameter("url");
		int userId=RedirectClass.MyRedirect(request, response);
		if(userId!=0)
		{
			UserServiceInterface user=new UserService();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			try(PrintWriter out=response.getWriter())
			{
				JSONObject result=new JSONObject();
				switch(url)
				{
				case "loadData":
					try
					{
						ArrayList<Permission> roleList=user.getRoleList();
						JSONArray role=new JSONArray(roleList);
						result.put("roleList", role);
						ArrayList<Department> deptList=user.getDepartmentList();
						JSONArray dept=new JSONArray(deptList);
						result.put("deptList", dept);
					}catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case "getSugarTypeAndLocationList":
					String nuserRoleId=request.getParameter("nuserRoleId");
					result=user.getSugarTypeAndLocationList(result,nuserRoleId);
					break;
				case "checkMobileNoExitOrNot":
					String mobileNo=request.getParameter("fieldvalue");
					String nuserName=request.getParameter("nuserName");
					result=user.checkMobileNoExitOrNot(result,mobileNo,nuserName);
					break;
				case "checkUserCodeExitOrNot":
					String userCode=request.getParameter("fieldvalue");
					nuserName=request.getParameter("nuserName");
					result=user.checkUserCodeExitOrNot(result,userCode,nuserName);
					break;
				case "checkUserNameExitOrNot":
					String userName=request.getParameter("fieldvalue");
					nuserName=request.getParameter("nuserName");
					result=user.checkUserNameExitOrNot(result,userName,nuserName);
					break;
				case "saveSystemUser":
					String vfullName=request.getParameter("vfullName");
					String vfullNameLocal=request.getParameter("vfullNameLocal");
					nuserName=request.getParameter("nuserName");
					String vuserName=request.getParameter("vuserName");
					String ndeptId=request.getParameter("ndeptId");
					nuserRoleId=request.getParameter("nuserRoleId");
					String nsugTypeId=request.getParameter("nsugTypeId");
					String nlocationId=request.getParameter("nlocationId");
					String nmobileNo=request.getParameter("nmobileNo");
					String npassword=request.getParameter("npassword");
					String pkid=request.getParameter("pkid");
					UserBean ubean=new UserBean();
					ubean.setNdeptId(ndeptId);
					ubean.setNlocationId(nlocationId);
					ubean.setNmobileNo(nmobileNo);
					ubean.setNpassword(npassword==null?"":npassword);
					ubean.setNsugTypeId(nsugTypeId==null?"":nsugTypeId);
					ubean.setNuserName(Integer.parseInt(nuserName));
					ubean.setNuserRoleId(nuserRoleId);
					ubean.setVactive("Y");
					ubean.setVfullName(vfullName);
					ubean.setVfullNameLocal(vfullNameLocal);
					ubean.setVuserName(vuserName);
					if(pkid.equals("1"))
						result=user.saveSystemUser(result,ubean);
					else
						result=user.updateSystemUser(result,ubean);
					break;
				case "getInfoById":
					nuserName=request.getParameter("nuserName");
					result=user.getInfoById(result,nuserName);
					break;
				case "activedeactiveUserById":
					String uid=request.getParameter("id");
					String status=request.getParameter("status");
					result=user.activeDeactiveById(result,uid,status);
					break;
					default:
					request.getRequestDispatcher("404.jsp").forward(request, response);
					break;
				}
				out.print(result);
			}
			
		}
	}

}
