package com.complaint.servlet;

import com.complaint.util.DBConnection;
import java.io.IOException;
import java.sql.*;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String category = request.getParameter("category");
        String description = request.getParameter("description");

        try (Connection con = DBConnection.getConnection()) {
            
            // --- REQUIREMENT 13: DUPLICATE CHECK ---
            // Check if a pending complaint with same email & description exists
            String checkSql = "SELECT complaint_id FROM complaints WHERE email = ? AND description = ? AND status != 'Resolved'";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, email);
            checkPs.setString(2, description);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // Duplicate found
                response.sendRedirect("index.html?status=duplicate");
                return;
            }

            // --- REQUIREMENT 4: GENERATE UNIQUE ID ---
            // Format: CMP-1000 to CMP-9999
            String complaintId = "CMP-" + (1000 + new Random().nextInt(9000));

            // --- REQUIREMENT 3: INSERT INTO DB ---
            String sql = "INSERT INTO complaints (complaint_id, name, email, category, description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, complaintId);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, category);
            ps.setString(5, description);
            
            int result = ps.executeUpdate();
            
            if(result > 0) {
                // Success: Redirect with ID
                response.sendRedirect("index.html?status=success&id=" + complaintId);
            } else {
                response.sendRedirect("index.html?status=error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.html?status=error");
        }
    }
}