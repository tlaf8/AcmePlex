package com.AcmePlex.Entity;
public class Showtime {
    private int id;
    private String movieTitle;
    private String date;
    private String time;

    public Showtime(int id, String movieTitle, String date, String time) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}