package com.khemsharma.yourdoctor.Models;

public class Appointment {
    String appointmentId,date_time,subject;

    public Appointment() {
    }
    public Appointment(String date_time, String subject) {
        this.date_time = date_time;
        this.subject = subject;
    }


    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
