package com.example.travelbooking.servlet;

import com.example.travelbooking.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.util.*;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends HttpServlet {

    // GET — fetch user data as JSON by username
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String username = request.getParameter("username");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try (BufferedReader reader = new BufferedReader(new FileReader(UserDAO.FILE))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 10 && p[5].trim().equals(username)) {
                    out.print("{");
                    out.print("\"userId\":\"" + p[0].trim() + "\",");
                    out.print("\"role\":\"" + p[1].trim() + "\",");
                    out.print("\"title\":\"" + p[2].trim() + "\",");
                    out.print("\"firstName\":\"" + p[3].trim() + "\",");
                    out.print("\"lastName\":\"" + p[4].trim() + "\",");
                    out.print("\"username\":\"" + p[5].trim() + "\",");
                    out.print("\"country\":\"" + p[6].trim() + "\",");
                    out.print("\"mobile\":\"" + p[7].trim() + "\",");
                    out.print("\"email\":\"" + p[8].trim() + "\"");
                    out.print("}");
                    out.flush();
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print("{\"error\":\"User not found\"}");
        out.flush();
    }

    // POST — update username and/or password after verification
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String currentUsername = request.getParameter("currentUsername");
        String currentPassword = request.getParameter("currentPassword");
        String newUsername = request.getParameter("newUsername");
        String newPassword = request.getParameter("newPassword");

        File file = new File(UserDAO.FILE);
        List<String> lines = new ArrayList<>();
        boolean verified = false;
        boolean updated = false;

        // Read all lines and find the user
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    lines.add(line);
                    continue;
                }

                String[] p = line.split(",");

                if (p.length >= 10 &&
                        p[5].trim().equals(currentUsername) &&
                        p[9].trim().equals(currentPassword)) {

                    verified = true;

                    // Update username if provided and different
                    String updatedUsername = (newUsername != null && !newUsername.trim().isEmpty())
                            ? newUsername.trim() : p[5].trim();

                    // Update password if provided and different
                    String updatedPassword = (newPassword != null && !newPassword.trim().isEmpty())
                            ? newPassword.trim() : p[9].trim();

                    // Rebuild the line
                    String updatedLine =
                            p[0].trim() + "," +
                                    p[1].trim() + "," +
                                    p[2].trim() + "," +
                                    p[3].trim() + "," +
                                    p[4].trim() + "," +
                                    updatedUsername + "," +
                                    p[6].trim() + "," +
                                    p[7].trim() + "," +
                                    p[8].trim() + "," +
                                    updatedPassword;

                    lines.add(updatedLine);
                    updated = true;

                } else {
                    lines.add(line);
                }
            }
        }

        if (!verified) {
            response.sendRedirect("Profile.html?status=wrongCredentials");
            return;
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

            String redirectUsername = (newUsername != null && !newUsername.trim().isEmpty())
                    ? newUsername.trim() : currentUsername;

            response.sendRedirect("Profile.html?status=updated&username=" + redirectUsername);
        } else {
            response.sendRedirect("Profile.html?status=wrongCredentials");
        }
    }
}