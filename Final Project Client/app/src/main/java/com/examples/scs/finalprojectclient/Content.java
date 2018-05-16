package com.examples.scs.finalprojectclient;

public class Content {

    private String username;
    private String password;
    private String message;
    private String firstName;
    private String lastName;
    private double latitude;
    private double longitude;

    public Content(){}

    public Content(String username, String password, String message, String firstName, String lastName, double latitude, double longitude) {
        this.username = username;
        this.password = password;
        this.message = message;
        this.firstName = firstName;
        this.lastName = lastName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("username:" + username + ",")
                .append("password:" + password + ",")
                .append("message:" + message + ",")
                .append("firstName:" + firstName + ",")
                .append("lastName:" + lastName + ",")
                .append("latitude:" + latitude + ",")
                .append("longitude:" + longitude)
                .toString();
    }
}
