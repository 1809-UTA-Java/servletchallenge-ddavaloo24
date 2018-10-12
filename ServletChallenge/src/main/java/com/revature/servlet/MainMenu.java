package com.revature.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.util.ConnectionUtil;

@SuppressWarnings("serial")
@WebServlet("/main-menu")
public class MainMenu extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false);
		PrintWriter pw = resp.getWriter();
		PreparedStatement ps = null;
		String sql;
		String name = "";
		
		if(session != null) {
			
			String uname = (String) session.getAttribute("username");
		
			
			try(Connection conn = ConnectionUtil.getConnection()) {
				
				sql = "SELECT * FROM ERS_USERS WHERE U_USERNAME=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, uname);
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					name = rs.getString("U_FIRSTNAME") + " " + rs.getString("U_LASTNAME");
				}
				
				
			} catch(SQLException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			req.setAttribute("name", name);
			RequestDispatcher rd = req.getRequestDispatcher("mainmenu.jsp");
			rd.forward(req, resp);
		}
		else {
			pw.println("BRO YOU GOTTA LOGIN FIRST!! WE ARE TAKING YOU HOME TO LOGIN MY DUDE");
			resp.setHeader("Refresh", "3; URL=home");
		}
		
	}
}
