package com.khemsharma.yourdoctor.Models;

public class BookedAppointments {
    String date_time;

    public BookedAppointments() {
    }

    public BookedAppointments(String date_time) {
        this.date_time = date_time;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
