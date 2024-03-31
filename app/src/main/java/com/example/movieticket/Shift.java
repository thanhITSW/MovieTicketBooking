package com.example.movieticket;

import java.util.ArrayList;
import java.util.List;

public class Shift {
    public String id;
    public String movieId;
    public String time;
    public List<String> selected;

    public Shift() {
    }

    public Shift(String id, String movieId, String time, List<String> selected) {
        this.id = id;
        this.movieId = movieId;
        this.time = time;
        this.selected = selected;
    }
}
