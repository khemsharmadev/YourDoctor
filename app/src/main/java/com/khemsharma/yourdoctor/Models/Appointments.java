package com.khemsharma.yourdoctor.Models;

public class Appointments {
    String doctorId_patientId,doctorId,patientId;
    Appointment appointment;
    public Appointments() {
    }




    public Appointments(String doctorId_patientId, Appointment appointment) {
        this.doctorId_patientId = doctorId_patientId;
        this.appointment = appointment;
    }

    public String getDoctorId_patientId() {
        return doctorId_patientId;
    }

    public void setDoctorId_patientId(String doctorId_patientId) {
        this.doctorId_patientId = doctorId_patientId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getDoctorId() {
        if (doctorId==null)
            doctorId=doctorId_patientId.substring(0,doctorId_patientId.indexOf('_'));

        return doctorId;
    }

    public String getPatientId() {
        if (patientId==null)
            patientId=doctorId_patientId.substring(doctorId_patientId.indexOf('_'),doctorId_patientId.length());

        return patientId;
    }
}
