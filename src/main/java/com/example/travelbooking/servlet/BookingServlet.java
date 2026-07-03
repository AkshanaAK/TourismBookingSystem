package com.example.travelbooking.servlet;

import com.example.travelbooking.dao.UserDAO;
import com.example.travelbooking.model.Booking;
import com.example.travelbooking.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "BookingServlet", value = "/bookings")
public class BookingServlet extends HttpServlet {

    BookingService service = new BookingService();

    // POST — book a package
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String packageId     = request.getParameter("packageId");
        String userId        = request.getParameter("userId");
        String accommodation = request.getParameter("accommodation");
        String days          = request.getParameter("days");

        String bookingId = service.bookPackage(packageId, userId, accommodation, days);

        if (bookingId != null) {
            response.sendRedirect("customer-dashboard.html?status=booked&bookingId=" + bookingId);
        } else {
            response.sendRedirect("customer-dashboard.html?status=bookingFailed");
        }
    }

    // GET — return all bookings as JSON (admin) or by userId (profile), includes status + userName
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        List<Booking> bookings = (userId != null && !userId.isEmpty())
                ? service.getBookingsByUserId(userId)
                : service.getAllBookings();

        // Build userId -> name map from users.txt (for admin view)
        java.util.Map<String, String> userNames = new java.util.HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(UserDAO.FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length >= 5) userNames.put(p[0].trim(), p[3].trim() + " " + p[4].trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();
        out.print("[");

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            String userName = userNames.getOrDefault(b.getUserId(), "Unknown");
            out.print("{");
            out.print("\"bookingId\":\""      + b.getBookingId()     + "\",");
            out.print("\"packageId\":\""      + b.getPackageId()     + "\",");
            out.print("\"userId\":\""         + b.getUserId()        + "\",");
            out.print("\"userName\":\""       + userName             + "\",");
            out.print("\"packageName\":\""    + b.getPackageName()   + "\",");
            out.print("\"destination\":\""    + b.getDestination()   + "\",");
            out.print("\"price\":\""          + b.getPrice()         + "\",");
            out.print("\"accommodation\":\"" + b.getAccommodation() + "\",");
            out.print("\"days\":\""           + b.getDays()          + "\",");
            out.print("\"dateTime\":\""       + b.getDateTime()      + "\",");
            out.print("\"status\":\""         + b.getStatus()        + "\"");
            out.print("}");
            if (i < bookings.size() - 1) out.print(",");
        }

        out.print("]");
        out.flush();
    }

    // PUT — update a booking (blocked if already cancelled)
    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String bookingId        = request.getParameter("bookingId");
        String newPrice         = request.getParameter("price");
        String newAccommodation = request.getParameter("accommodation");
        String newDays          = request.getParameter("days");

        // Check current status first
        boolean isCancelled = false;
        for (Booking b : service.getAllBookings()) {
            if (b.getBookingId().equals(bookingId) && "Cancelled".equalsIgnoreCase(b.getStatus())) {
                isCancelled = true;
                break;
            }
        }

        response.setContentType("application/json");

        if (isCancelled) {
            response.getWriter().write("{\"status\":\"failed\",\"reason\":\"cancelled\"}");
            return;
        }

        boolean success = service.updateBooking(bookingId, newPrice, newAccommodation, newDays);
        response.getWriter().write(success ? "{\"status\":\"updated\"}" : "{\"status\":\"failed\"}");
    }

    // DELETE — cancel a booking (marks status as Cancelled, does not delete the row)
    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String bookingId = request.getParameter("bookingId");
        boolean success  = service.cancelBooking(bookingId);

        response.setContentType("application/json");
        response.getWriter().write(success ? "{\"status\":\"cancelled\"}" : "{\"status\":\"failed\"}");
    }
}