package com.example.travelbooking.servlet;

import com.example.travelbooking.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet(name = "UserListServlet", value = "/users-list")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("[");

        try (BufferedReader reader = new BufferedReader(new FileReader(UserDAO.FILE))) {

            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 10) {
                    if (!first) out.print(",");
                    out.print("{");
                    out.print("\"userId\":\""    + p[0].trim() + "\",");
                    out.print("\"role\":\""      + p[1].trim() + "\",");
                    out.print("\"firstName\":\"" + p[3].trim() + "\",");
                    out.print("\"lastName\":\""  + p[4].trim() + "\",");
                    out.print("\"username\":\"" + p[5].trim() + "\",");
                    out.print("\"country\":\""   + p[6].trim() + "\",");
                    out.print("\"mobile\":\""    + p[7].trim() + "\",");
                    out.print("\"email\":\""     + p[8].trim() + "\"");
                    out.print("}");
                    first = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print("]");
        out.flush();
    }
}