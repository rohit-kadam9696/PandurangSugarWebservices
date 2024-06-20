package com.twd.pandurangsugar.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.twd.pandurangsugar.web.bean.UserBean;
import com.twd.pandurangsugar.web.service.WebLoginService;
import com.twd.pandurangsugar.web.serviceInterface.WebLoginServiceInterface;

/**
 * Servlet implementation class WebLogin
 */
@WebServlet({"/weblogin","/logout"})
public class WebLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.invalidate();
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		WebLoginServiceInterface webLoginService=new WebLoginService();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		UserBean res=webLoginService.validateWebLogin(username,password);
		HttpSession session=request.getSession();
		if(res!=null && res.isSuccess())
		{
			JSONObject webPermission=webLoginService.getWebPermission(res.getNuserRoleId());
			System.out.println(webPermission);
			try {
				if(webPermission.getBoolean("success"))
				{
					webPermission.remove("success");
					webPermission.remove("msg");
					session.setAttribute("webPermission", webPermission);
					session.setAttribute("userName", res.getVfullName());
					session.setAttribute("userId", res.getNuserName());
					request.getRequestDispatcher("home.jsp").forward(request, response);
				}
				else
				{
					session.setAttribute("login_error", webPermission.getString("msg"));
					request.getRequestDispatcher("index.jsp").forward(request, response);
				}
			} catch (JSONException e) {
				session.setAttribute("login_error", "permission Error :"+e.getLocalizedMessage());
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}
			
			
		}else
		{
			session.setAttribute("login_error", res.getMsg());
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

}
