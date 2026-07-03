package com.example.travelbooking.service;

import com.example.travelbooking.dao.UserDAO;
import com.example.travelbooking.model.User;
import com.example.travelbooking.utils.UserIdGenerator;
import java.io.*;

public class AuthenticationService {

    UserDAO dao = new UserDAO();

    public boolean login(String username, String password, String role) {

        try (BufferedReader reader = new BufferedReader(new FileReader(UserDAO.FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length >= 10) {

                    String fileRole     = parts[1].trim();
                    String fileUsername = parts[5].trim();
                    String filePassword = parts[9].trim();

                    System.out.println("Checking: " + fileUsername + " / " + fileRole);

                    if (fileUsername.equals(username) &&
                            filePassword.equals(password) &&
                            fileRole.equals(role)) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean emailExists(String email) {

        try (BufferedReader reader = new BufferedReader(new FileReader(UserDAO.FILE))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length >= 9) {

                    String fileEmail = parts[8].trim();

                    if (fileEmail.equalsIgnoreCase(email)) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String register(
            String role,
            String title,
            String firstName,
            String lastName,
            String username,
            String country,
            String mobile,
            String email,
            String password
    ) {
        if (emailExists(email)) {
            return null; // duplicate email
        }

        String id = UserIdGenerator.generateId(role);

        User user = new User(
                id, role, title, firstName,
                lastName, username, country,
                mobile, email, password
        );

        dao.saveUser(user);

        return id;
    }
}