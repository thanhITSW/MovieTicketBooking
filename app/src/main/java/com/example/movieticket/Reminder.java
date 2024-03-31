package com.example.movieticket;

public class Reminder {
    public String id;
    public String movieName;
    public String date;

    public Reminder() {
    }

    public Reminder(String id, String movieName, String date) {
        this.id = id;
        this.movieName = movieName;
        this.date = date;
    }
}
