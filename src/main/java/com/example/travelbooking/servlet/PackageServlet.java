package com.example.travelbooking.servlet;

import com.example.travelbooking.model.Package;
import com.example.travelbooking.service.PackageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "PackageServlet", value = "/packages")
public class PackageServlet extends HttpServlet {

    PackageService service = new PackageService();

    // GET — return all packages as JSON
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Package> packages = service.getAllPackages();

        PrintWriter out = response.getWriter();
        out.print("[");

        for (int i = 0; i < packages.size(); i++) {
            Package pkg = packages.get(i);
            out.print("{");
            out.print("\"packageId\":\""   + pkg.getPackageId()   + "\",");
            out.print("\"packageName\":\"" + pkg.getPackageName() + "\",");
            out.print("\"destination\":\"" + pkg.getDestination() + "\",");
            out.print("\"price\":\""       + pkg.getPrice()       + "\",");
            out.print("\"duration\":\""    + pkg.getDuration()    + "\",");
            out.print("\"description\":\"" + pkg.getDescription() + "\"");
            out.print("}");
            if (i < packages.size() - 1) out.print(",");
        }

        out.print("]");
        out.flush();
    }

    // POST — add new package
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String packageName = request.getParameter("packageName");
        String destination = request.getParameter("destination");
        String price       = request.getParameter("price");
        String duration    = request.getParameter("duration");
        String description = request.getParameter("description");

        service.addPackage(packageName, destination, price, duration, description);

        response.sendRedirect("admin-dashboard.html?status=packageAdded");
    }

    // DELETE — remove a package
    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String packageId = request.getParameter("packageId");
        service.deletePackage(packageId);

        response.setStatus(200);
        response.getWriter().write("deleted");
    }
}