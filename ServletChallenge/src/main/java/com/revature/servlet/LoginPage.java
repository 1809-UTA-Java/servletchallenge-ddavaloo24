package com.revature.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.util.ConnectionUtil;


@SuppressWarnings("serial")
@WebServlet("/logging")
public class LoginPage extends HttpServlet {
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		int existance = 1000;

		PrintWriter pw = resp.getWriter();

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		PreparedStatement ps = null;
		String sql;
		
		try(Connection conn = ConnectionUtil.getConnection()) {
			
			sql = "SELECT Count(U_ID) FROM ERS_USERS WHERE u_username=? AND u_password=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				existance = rs.getInt("COUNT(U_ID)");
			}
			
			ps.close();
			rs.close();
			
			
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		

		if(existance == 1) {
			pw.println("You are now logged in! You will now be redirected to the main menu");
			HttpSession session = req.getSession();
			session.setAttribute("username", username);			
			resp.setHeader("Refresh", "2; URL=main-menu");
		}
		else if(existance == 0) {
			pw.println("Failed to create the account. You will not be redirected to the home page");
			resp.setHeader("Refresh", "2; URL=home");
		}
		
	}

}
