package com.example.travelbooking.model;

public class Booking {

    private String bookingId;
    private String packageId;
    private String userId;
    private String packageName;
    private String destination;
    private String price;
    private String accommodation;
    private String days;
    private String dateTime;
    private String status; // "Confirmed" or "Cancelled"

    public Booking(
            String bookingId,
            String packageId,
            String userId,
            String packageName,
            String destination,
            String price,
            String accommodation,
            String days,
            String dateTime,
            String status
    ) {
        this.bookingId     = bookingId;
        this.packageId     = packageId;
        this.userId        = userId;
        this.packageName   = packageName;
        this.destination   = destination;
        this.price         = price;
        this.accommodation = accommodation;
        this.days          = days;
        this.dateTime      = dateTime;
        this.status        = status;
    }

    public String getBookingId()     { return bookingId; }
    public String getPackageId()     { return packageId; }
    public String getUserId()        { return userId; }
    public String getPackageName()   { return packageName; }
    public String getDestination()   { return destination; }
    public String getPrice()         { return price; }
    public String getAccommodation() { return accommodation; }
    public String getDays()          { return days; }
    public String getDateTime()      { return dateTime; }
    public String getStatus()        { return status; }
}