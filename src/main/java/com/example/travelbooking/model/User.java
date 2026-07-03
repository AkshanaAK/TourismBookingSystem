package com.example.travelbooking.model;

public class User {

    private String userId;
    private String role;
    private String title;
    private String firstName;
    private String lastName;
    private String username;
    private String country;
    private String mobile;
    private String email;
    private String password;


    public User(
            String userId,
            String role,
            String title,
            String firstName,
            String lastName,
            String username,
            String country,
            String mobile,
            String email,
            String password
    ){

        this.userId=userId;
        this.role=role;
        this.title=title;
        this.firstName=firstName;
        this.lastName=lastName;
        this.username=username;
        this.country=country;
        this.mobile=mobile;
        this.email=email;
        this.password=password;

    }

    public String getUserId(){

        return userId;

    }

    public String getRole(){

        return role;

    }

    public String getTitle(){

        return title;

    }

    public String getFirstName(){

        return firstName;

    }

    public String getLastName(){

        return lastName;

    }

    public String getUsername(){

        return username;

    }

    public String getCountry(){

        return country;

    }

    public String getMobile(){

        return mobile;

    }

    public String getEmail(){

        return email;

    }

    public String getPassword(){

        return password;

    }

}