package com.complaint.servlet;

import com.complaint.util.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // --- REQUIREMENT 14: SECURE ACCESS (SESSION CHECK) ---
        HttpSession session = request.getSession(false); // false = do not create new session
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Admin Dashboard</title><link rel='stylesheet' href='css/style.css'></head><body>");
        // Inline CSS for the table just for this page
        out.println("<style>table {width:100%; border-collapse: collapse;} th, td {padding: 8px; border: 1px solid #ddd;} th {background:#f4f4f4;}</style>");
        
        out.println("<div style='max-width:1000px; margin:auto; padding:20px;'>");
        out.println("<div style='display:flex; justify-content:space-between; align-items:center;'>");
        out.println("<h2>Admin Dashboard</h2>");
        out.println("<span>Welcome, " + session.getAttribute("adminUser") + " | <a href='LogoutServlet' style='color:red;'>Logout</a></span>");
        out.println("</div>");

        out.println("<table>");
        out.println("<tr><th>ID</th><th>User Email</th><th>Category</th><th>Description</th><th>Status</th><th>Action</th></tr>");

        try (Connection con = DBConnection.getConnection()) {
            // --- REQUIREMENT 7: RETRIEVE & DISPLAY COMPLAINTS ---
            String sql = "SELECT * FROM complaints ORDER BY id DESC"; // Newest first
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String id = rs.getString("complaint_id");
                String currentStatus = rs.getString("status");
                
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td>" + rs.getString("description") + "</td>");
                
                // Color code the status
                String color = "black";
                if("Pending".equals(currentStatus)) color = "red";
                else if("Resolved".equals(currentStatus)) color = "green";
                out.println("<td style='color:"+color+"'>" + currentStatus + "</td>");
                
                // --- REQUIREMENT 9: UPDATE STATUS FORM ---
                out.println("<td>");
                out.println("<form action='UpdateStatusServlet' method='post' style='display:flex; gap:5px;'>");
                out.println("<input type='hidden' name='cid' value='" + id + "'>");
                out.println("<select name='newStatus' style='padding:5px;'>");
                out.println("<option value='Pending' " + ("Pending".equals(currentStatus)?"selected":"") + ">Pending</option>");
                out.println("<option value='In Progress' " + ("In Progress".equals(currentStatus)?"selected":"") + ">In Progress</option>");
                out.println("<option value='Resolved' " + ("Resolved".equals(currentStatus)?"selected":"") + ">Resolved</option>");
                out.println("</select>");
                out.println("<input type='text' name='remarks' placeholder='Add Remarks' style='width:100px; padding:5px;'>");
                out.println("<button type='submit' style='padding:5px; width:auto; background:#007bff;'>Update</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<tr><td colspan='6'>Error loading data.</td></tr>");
        }
        
        out.println("</table>");
        out.println("</div></body></html>");
    }
}