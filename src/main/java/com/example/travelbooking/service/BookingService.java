package com.example.travelbooking.service;

import com.example.travelbooking.dao.BookingDAO;
import com.example.travelbooking.dao.PackageDAO;
import com.example.travelbooking.model.Booking;
import com.example.travelbooking.model.Package;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingService {

    BookingDAO bookingDAO = new BookingDAO();
    PackageDAO packageDAO = new PackageDAO();

    public String bookPackage(String packageId, String userId, String accommodation, String days) {

        List<Package> packages = packageDAO.getAllPackages();
        Package selected = null;

        for (Package pkg : packages) {
            if (pkg.getPackageId().equals(packageId)) { selected = pkg; break; }
        }

        if (selected == null) return null;

        String bookingId = bookingDAO.generateBookingId();
        String dateTime  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Booking booking = new Booking(
                bookingId, packageId, userId,
                selected.getPackageName(), selected.getDestination(), selected.getPrice(),
                accommodation, days, dateTime,
                "Confirmed"
        );

        bookingDAO.saveBooking(booking);
        return bookingId;
    }

    public List<Booking> getAllBookings() { return bookingDAO.getAllBookings(); }

    public List<Booking> getBookingsByUserId(String userId) { return bookingDAO.getBookingsByUserId(userId); }

    public boolean updateBooking(String bookingId, String newPrice, String newAccommodation, String newDays) {
        return bookingDAO.updateBooking(bookingId, newPrice, newAccommodation, newDays);
    }

    public boolean cancelBooking(String bookingId) {
        return bookingDAO.cancelBooking(bookingId);
    }
}