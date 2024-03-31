package com.example.movieticket;

public class Review {
    public String id;
    public String movieName;
    public String username;
    public String date;
    public String comment;
    public Float rating;

    public Review() {
    }

    public Review(String id, String movieName, String username, String date, String comment, Float rating) {
        this.id = id;
        this.movieName = movieName;
        this.username = username;
        this.date = date;
        this.comment = comment;
        this.rating = rating;
    }
}
