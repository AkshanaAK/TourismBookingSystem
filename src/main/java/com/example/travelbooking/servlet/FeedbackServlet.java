package com.example.travelbooking.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FeedbackServlet", value = "/feedback")
public class FeedbackServlet extends HttpServlet {

    public static final String FILE =
            "C:/Users/USER/IdeaProjects/TourismBookingSystem/src/main/resources/data/feedback.txt";

    // Format: feedbackId,userId,username,rating,comment,dateTime,adminReply
    // adminReply is empty string "" if no reply yet

    // POST — save new feedback OR save admin reply (action=reply)
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("reply".equals(action)) {
            saveReply(request, response);
            return;
        }

        // New feedback submission
        String userId   = request.getParameter("userId");
        String username = request.getParameter("username");
        String rating   = request.getParameter("rating");
        String comment  = request.getParameter("comment");
        String dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (comment != null) comment = comment.replace(",", ";");

        String feedbackId = generateFeedbackId();

        try {
            File file = new File(FILE);
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(feedbackId + "," + userId + "," + username + "," + rating + "," + comment + "," + dateTime + ",");
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        response.sendRedirect("feedback.html?status=submitted");
    }

    // Save an admin reply onto an existing feedback entry
    private void saveReply(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String feedbackId = request.getParameter("feedbackId");
        String replyText  = request.getParameter("replyText");

        if (replyText != null) replyText = replyText.replace(",", ";").replace("\n", " ");

        File file = new File(FILE);
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) { lines.add(line); continue; }
                String[] p = line.split(",", 7);
                if (p.length >= 6 && p[0].trim().equals(feedbackId)) {
                    String existing = p.length >= 7 ? p[6] : "";
                    String updatedLine =
                            p[0].trim() + "," + p[1].trim() + "," + p[2].trim() + "," +
                                    p[3].trim() + "," + p[4].trim() + "," + p[5].trim() + "," + replyText;
                    lines.add(updatedLine);
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                for (String l : lines) { writer.write(l); writer.newLine(); }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(updated ? "{\"status\":\"replied\"}" : "{\"status\":\"failed\"}");
    }

    // GET — return all feedback as JSON (public wall + admin)
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("[");

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", 7);
                if (p.length >= 6) {
                    String reply = p.length >= 7 ? p[6].trim() : "";
                    if (!first) out.print(",");
                    out.print("{");
                    out.print("\"feedbackId\":\"" + p[0].trim() + "\",");
                    out.print("\"userId\":\""     + p[1].trim() + "\",");
                    out.print("\"username\":\""   + p[2].trim() + "\",");
                    out.print("\"rating\":\""     + p[3].trim() + "\",");
                    out.print("\"comment\":\""    + p[4].trim() + "\",");
                    out.print("\"dateTime\":\""   + p[5].trim() + "\",");
                    out.print("\"adminReply\":\"" + reply       + "\"");
                    out.print("}");
                    first = false;
                }
            }

        } catch (FileNotFoundException e) {
            // no feedback yet
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.print("]");
        out.flush();
    }

    // Generate unique feedback ID (F1000, F1001...)
    private String generateFeedbackId() {
        int max = 1000;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 1) {
                    try {
                        String id = p[0].trim();
                        if (id.startsWith("F")) {
                            int num = Integer.parseInt(id.substring(1));
                            if (num >= max) max = num + 1;
                        }
                    } catch (NumberFormatException e) { }
                }
            }
        } catch (FileNotFoundException e) {
            // first feedback
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "F" + max;
    }
}