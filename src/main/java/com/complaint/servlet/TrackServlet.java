package com.complaint.servlet;

import com.complaint.util.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/TrackServlet")
public class TrackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("complaintId");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Status</title><link rel='stylesheet' href='css/style.css'></head><body>");
        out.println("<div class='container'><h2>Complaint Status</h2>");

        try (Connection con = DBConnection.getConnection()) {
            // --- REQUIREMENT 5: TRACK STATUS ---
            String sql = "SELECT * FROM complaints WHERE complaint_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                String color = "orange";
                if("Resolved".equalsIgnoreCase(status)) color = "green";
                if("In Progress".equalsIgnoreCase(status)) color = "blue";

                out.println("<div style='border:1px solid #ddd; padding:15px; border-radius:5px;'>");
                out.println("<p><strong>Complaint ID:</strong> " + rs.getString("complaint_id") + "</p>");
                out.println("<p><strong>Category:</strong> " + rs.getString("category") + "</p>");
                out.println("<p><strong>Description:</strong> " + rs.getString("description") + "</p>");
                out.println("<p><strong>Current Status:</strong> <span style='color:"+color+"; font-weight:bold;'>" + status + "</span></p>");
                
                String remarks = rs.getString("remarks");
                out.println("<p><strong>Admin Remarks:</strong> " + (remarks != null ? remarks : "<em>No remarks yet.</em>") + "</p>");
                out.println("</div>");
            } else {
                out.println("<h3 style='color:red;'>Complaint ID Not Found</h3>");
                out.println("<p>Please check your ID and try again.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error connecting to database.</p>");
        }
        
        out.println("<div class='links'><br><a href='track.html'>Track Another</a> | <a href='index.html'>Home</a></div>");
        out.println("</div></body></html>");
    }
}