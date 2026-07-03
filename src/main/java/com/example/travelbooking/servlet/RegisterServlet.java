package com.example.travelbooking.servlet;

import com.example.travelbooking.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name="RegisterServlet", value="/register")
public class RegisterServlet extends HttpServlet {

    AuthenticationService service =
            new AuthenticationService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        System.out.println("REGISTER BUTTON CLICKED");

        String role = request.getParameter("role");
        String title = request.getParameter("title");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username");
        String country = request.getParameter("country");
        String mobile = request.getParameter("mobile");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String userId = service.register(
                role,
                title,
                firstName,
                lastName,
                username,
                country,
                mobile,
                email,
                password
        );

        if (userId == null) {
            response.sendRedirect(role + "-register.html?status=duplicate");
        } else {
            response.sendRedirect(role + "-login.html?status=success&userId=" + userId);
        }

        // ❌ DELETE THESE 3 LINES BELOW — this was the duplicate redirect causing the 500 error
        // response.sendRedirect(
        //         role + "-login.html?status=success&userId=" + userId
        // );

    }
}