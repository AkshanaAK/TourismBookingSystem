package com.example.travelbooking.dao;

import com.example.travelbooking.model.Booking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public static final String FILE =
            "C:/Users/USER/IdeaProjects/TourismBookingSystem/src/main/resources/data/bookings.txt";

    // Format: bookingId,packageId,userId,packageName,destination,price,accommodation,days,dateTime,status
    public void saveBooking(Booking booking) {

        try {
            File file = new File(FILE);
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(
                        booking.getBookingId()     + "," +
                                booking.getPackageId()     + "," +
                                booking.getUserId()        + "," +
                                booking.getPackageName()   + "," +
                                booking.getDestination()   + "," +
                                booking.getPrice()         + "," +
                                booking.getAccommodation() + "," +
                                booking.getDays()          + "," +
                                booking.getDateTime()      + "," +
                                booking.getStatus()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> getAllBookings() {

        List<Booking> bookings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", 10);
                if (parts.length >= 9) {
                    // status may be missing on older rows -> default to Confirmed
                    String status = parts.length >= 10 ? parts[9].trim() : "Confirmed";
                    bookings.add(new Booking(
                            parts[0].trim(), parts[1].trim(), parts[2].trim(),
                            parts[3].trim(), parts[4].trim(), parts[5].trim(),
                            parts[6].trim(), parts[7].trim(), parts[8].trim(),
                            status
                    ));
                }
            }

        } catch (FileNotFoundException e) {
            // no bookings yet
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public List<Booking> getBookingsByUserId(String userId) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : getAllBookings()) {
            if (b.getUserId().equals(userId)) result.add(b);
        }
        return result;
    }

    // Update a booking (price, accommodation, days) - only allowed if not cancelled
    public boolean updateBooking(String bookingId, String newPrice, String newAccommodation, String newDays) {

        File file = new File(FILE);
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) { lines.add(line); continue; }
                String[] p = line.split(",", 10);
                if (p.length >= 9 && p[0].trim().equals(bookingId)) {
                    String status = p.length >= 10 ? p[9].trim() : "Confirmed";
                    String updatedLine =
                            p[0].trim() + "," + p[1].trim() + "," + p[2].trim() + "," +
                                    p[3].trim() + "," + p[4].trim() + "," +
                                    newPrice + "," + newAccommodation + "," + newDays + "," +
                                    p[8].trim() + "," + status;
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

        return updated;
    }

    // Mark a booking as Cancelled instead of deleting it
    public boolean cancelBooking(String bookingId) {

        File file = new File(FILE);
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) { lines.add(line); continue; }
                String[] p = line.split(",", 10);
                if (p.length >= 9 && p[0].trim().equals(bookingId)) {
                    String updatedLine =
                            p[0].trim() + "," + p[1].trim() + "," + p[2].trim() + "," +
                                    p[3].trim() + "," + p[4].trim() + "," + p[5].trim() + "," +
                                    p[6].trim() + "," + p[7].trim() + "," + p[8].trim() + "," +
                                    "Cancelled";
                    lines.add(updatedLine);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                for (String l : lines) { writer.write(l); writer.newLine(); }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return found;
    }

    public String generateBookingId() {
        int max = 1000;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    try {
                        String id = parts[0].trim();
                        if (id.startsWith("B")) {
                            int num = Integer.parseInt(id.substring(1));
                            if (num >= max) max = num + 1;
                        }
                    } catch (NumberFormatException e) { }
                }
            }
        } catch (FileNotFoundException e) {
            // first booking
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "B" + max;
    }
}