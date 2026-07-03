package com.example.travelbooking.model;

public class Package {

    private String packageId;
    private String packageName;
    private String destination;
    private String price;
    private String duration;
    private String description;

    public Package(
            String packageId,
            String packageName,
            String destination,
            String price,
            String duration,
            String description
    ) {
        this.packageId   = packageId;
        this.packageName = packageName;
        this.destination = destination;
        this.price       = price;
        this.duration    = duration;
        this.description = description;
    }

    public String getPackageId()   { return packageId; }
    public String getPackageName() { return packageName; }
    public String getDestination() { return destination; }
    public String getPrice()       { return price; }
    public String getDuration()    { return duration; }
    public String getDescription() { return description; }
}