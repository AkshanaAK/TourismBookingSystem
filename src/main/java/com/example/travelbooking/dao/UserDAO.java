package com.example.travelbooking.dao;

import com.example.travelbooking.model.User;
import java.io.*;

public class UserDAO {

    public static final String FILE =
            "C:/Users/USER/IdeaProjects/TourismBookingSystem/src/main/resources/data/users.txt";

    public void saveUser(User user) {

        System.out.println("Saving user: " + user.getUsername());
        System.out.println("File path: " + FILE);

        try {
            File file = new File(FILE);
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(
                        user.getUserId() + "," +
                                user.getRole() + "," +
                                user.getTitle() + "," +
                                user.getFirstName() + "," +
                                user.getLastName() + "," +
                                user.getUsername() + "," +
                                user.getCountry() + "," +
                                user.getMobile() + "," +
                                user.getEmail() + "," +
                                user.getPassword()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}