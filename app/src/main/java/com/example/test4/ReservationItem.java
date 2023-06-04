package com.example.test4;

public class ReservationItem {

    String time;
    String start; // 정류장

    public ReservationItem(String time, String start) {
        this.time = time;
        this.start = start;
    }

    public String getTime() {
        return time;
    }

    public void setName(String time) {
        this.time = time;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }



}
