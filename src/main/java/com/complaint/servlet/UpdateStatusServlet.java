package com.complaint.servlet;

import com.complaint.util.DBConnection;
import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/UpdateStatusServlet")
public class UpdateStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Security check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String cid = request.getParameter("cid");
        String status = request.getParameter("newStatus");
        String remarks = request.getParameter("remarks");

        try (Connection con = DBConnection.getConnection()) {
            // --- REQUIREMENT 10: STORE UPDATES SECURELY ---
            // We update status and append remarks if provided
            String sql = "UPDATE complaints SET status=?, remarks=? WHERE complaint_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setString(3, cid);
            
            ps.executeUpdate();
            
            // Redirect back to dashboard to see changes
            response.sendRedirect("AdminDashboardServlet");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminDashboardServlet?error=update_failed");
        }
    }
}