package com.example.travelbooking.dao;

import com.example.travelbooking.model.Package;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PackageDAO {

    public static final String FILE =
            "C:/Users/USER/IdeaProjects/TourismBookingSystem/src/main/resources/data/packages.txt";

    // Save a new package
    public void savePackage(Package pkg) {

        try {
            File file = new File(FILE);
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(
                        pkg.getPackageId()   + "," +
                                pkg.getPackageName() + "," +
                                pkg.getDestination() + "," +
                                pkg.getPrice()       + "," +
                                pkg.getDuration()    + "," +
                                pkg.getDescription()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all packages
    public List<Package> getAllPackages() {

        List<Package> packages = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", 6);
                if (parts.length >= 6) {
                    packages.add(new Package(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            parts[4].trim(),
                            parts[5].trim()
                    ));
                }
            }

        } catch (FileNotFoundException e) {
            // no packages yet
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packages;
    }

    // Delete a package by ID
    public void deletePackage(String packageId) {

        File file = new File(FILE);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(packageId + ",")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate unique package ID
    public String generatePackageId() {

        int max = 1000;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1) {
                    try {
                        String id = parts[0].trim();
                        if (id.startsWith("P")) {
                            int num = Integer.parseInt(id.substring(1));
                            if (num >= max) max = num + 1;
                        }
                    } catch (NumberFormatException e) { }
                }
            }
        } catch (FileNotFoundException e) {
            // first package
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "P" + max;
    }
}