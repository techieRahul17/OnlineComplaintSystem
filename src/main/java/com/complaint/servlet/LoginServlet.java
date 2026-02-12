package com.complaint.servlet;

import com.complaint.util.DBConnection;
import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String u = request.getParameter("username");
        String p = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {
            // --- REQUIREMENT 6: ADMIN LOGIN ---
            String sql = "SELECT * FROM admins WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u);
            ps.setString(2, p);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // --- REQUIREMENT 14: SESSION HANDLING ---
                HttpSession session = request.getSession();
                session.setAttribute("adminUser", u);
                // Redirect to the protected dashboard
                response.sendRedirect("AdminDashboardServlet");
            } else {
                // Login Failed
                response.sendRedirect("login.html?error=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error=server");
        }
    }
}