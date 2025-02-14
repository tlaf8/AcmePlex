package com.AcmePlex.Entity;

public class User {
    private String email;
    private String password;
    private String fullName;
    private String address;
    private String cardNumber;
    private String csv;
    private String expiryDate;

    public User(String email, String password, String fullName, String address, String cardNumber, String csv, String expiryDate) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.address = address;
        this.cardNumber = cardNumber;
        this.csv = csv;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}