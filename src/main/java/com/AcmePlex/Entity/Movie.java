package com.AcmePlex.Entity;
public class Movie {
    private int id;
    private String title;
    private String theaterLocation;

    public Movie(int id, String title, String theaterLocation) {
        this.id = id;
        this.title = title;
        this.theaterLocation = theaterLocation;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTheaterLocation() {
        return theaterLocation;
    }
}