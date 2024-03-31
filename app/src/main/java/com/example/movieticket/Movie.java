package com.example.movieticket;

import java.util.List;

public class Movie {
    public String id;
    public String name;
    public List<String> category;
    public List<String> actor;
    public String date;
    public String description;
    public double price;
    public String url_avatar;
    public String url_trailer;

    public Movie() {
    }

    public Movie(String id, String name, List<String> category, List<String> actor, String date, String description, double price, String url_avatar, String url_trailer) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.actor = actor;
        this.date = date;
        this.description = description;
        this.price = price;
        this.url_avatar = url_avatar;
        this.url_trailer = url_trailer;
    }
}
