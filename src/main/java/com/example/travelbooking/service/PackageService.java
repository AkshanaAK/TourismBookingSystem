package com.example.travelbooking.service;

import com.example.travelbooking.dao.PackageDAO;
import com.example.travelbooking.model.Package;

import java.util.List;

public class PackageService {

    PackageDAO dao = new PackageDAO();

    public List<Package> getAllPackages() {
        return dao.getAllPackages();
    }

    public void addPackage(
            String packageName,
            String destination,
            String price,
            String duration,
            String description
    ) {
        String id = dao.generatePackageId();
        Package pkg = new Package(id, packageName, destination, price, duration, description);
        dao.savePackage(pkg);
    }

    public void deletePackage(String packageId) {
        dao.deletePackage(packageId);
    }
}