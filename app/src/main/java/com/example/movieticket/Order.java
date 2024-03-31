package com.example.movieticket;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Order implements Serializable {
    public String id;
    public String creation_date;
    public String username;
    public String movieId;
    public String movieName;
    public String date;
    public String url_avatar;
    public String shift;
    public String cinema;
    public int quantity;
    public String selected;
    public String method;
    public double totalPrice;

    public Order() {
    }

    public Order(String id, String creation_date, String username, String movieId, String movieName, String date, String url_avatar, String shift, String cinema, int quantity, String selected, String method, double totalPrice) {
        this.id = id;
        this.creation_date = creation_date;
        this.username = username;
        this.movieId = movieId;
        this.movieName = movieName;
        this.date = date;
        this.url_avatar = url_avatar;
        this.shift = shift;
        this.cinema = cinema;
        this.quantity = quantity;
        this.selected = selected;
        this.method = method;
        this.totalPrice = totalPrice;
    }

    public String displayDataQRCode() {
        DecimalFormat formatter = new DecimalFormat("#,### VND");
        return  "id: '" + id + '\'' +
                ", username: '" + username + '\'' +
                ", movie name: '" + movieName + '\'' +
                ", number of tickets: " + quantity +
                ", method payment: '" + method + '\'' +
                ", total price: " + formatter.format(totalPrice);
    }
}
