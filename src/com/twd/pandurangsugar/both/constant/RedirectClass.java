package com.twd.pandurangsugar.both.constant;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RedirectClass {
	public static int MyRedirect(HttpServletRequest request,
			HttpServletResponse response) {
		int userid = 0;
		HttpSession session = request.getSession(true);
		if (session == null || session.isNew()
				|| session.getAttribute("webPermission")==null || session.getAttribute("userId")==null) {
			try {
				response.sendRedirect("logout");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			userid = Integer.parseInt(String.valueOf(session.getAttribute("userId")));
		}
		return userid;
	}
}
