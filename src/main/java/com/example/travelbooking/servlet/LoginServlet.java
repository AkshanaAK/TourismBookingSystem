package com.example.travelbooking.servlet;
import com.example.travelbooking.dao.UserDAO;
import java.io.BufferedReader;
import java.io.FileReader;

import com.example.travelbooking.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    AuthenticationService service =
            new AuthenticationService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        System.out.println("LOGIN BUTTON CLICKED");

        String role     = request.getParameter("role");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean success = service.login(username, password, role);

        if (success) {
            // Get the userId from users.txt
            String userId = getUserId(username, role);
            response.sendRedirect("home.html?username=" + username + "&role=" + role + "&userId=" + userId);
        } else {
            response.sendRedirect(role + "-login.html?status=failed");
        }
    }

    // Helper method to find userId from users.txt
    private String getUserId(String username, String role) {
        try (BufferedReader reader = new BufferedReader(new FileReader(UserDAO.FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 10 &&
                        p[5].trim().equals(username) &&
                        p[1].trim().equals(role)) {
                    return p[0].trim(); // return userId
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}