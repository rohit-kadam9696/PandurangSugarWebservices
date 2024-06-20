package com.twd.pandurangsugar.android.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twd.pandurangsugar.both.constant.ConstantVeriables;

@WebServlet("/fetchimage")
public class FetchImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//////// System.out.println("in get");
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//////// System.out.println("got call");
		String type = "addv";
		String image = "avtar1.png";

		if ((request.getParameter("image") != null) && (!request.getParameter("image").trim().equals(""))) {
			image = request.getParameter("image");
		}
		type = request.getParameter("type");
		
		String imagePath = null;
		if (type == null) {
			type = "";
		}

		if (type.equals("number"))
			imagePath = ConstantVeriables.baseFilePath + ConstantVeriables.numbertaker + image;
		else if (type.equals("sugar"))
			imagePath = ConstantVeriables.baseFilePath + ConstantVeriables.sugar + image;

		OutputStream out = response.getOutputStream();
		File f = new File(imagePath);
		if (!f.exists()) {
			imagePath = ConstantVeriables.baseFilePath + ConstantVeriables.numbertaker + "avtar1.png";
		}
		FileInputStream fin = null;
		BufferedInputStream fBuf = null;
		try {
			fin = new FileInputStream(imagePath);
			fBuf = new BufferedInputStream(fin);
			int BufferLen = 2048;
			byte[] buf = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = fBuf.read(buf, 0, buf.length))) {
				out.write(buf, 0, bytesRead);
			}

			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fBuf != null) {
				fBuf.close();
			}
			if (fin != null)
				fin.close();
		}

	}
}