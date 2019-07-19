package com.khemsharma.yourdoctor;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khemsharma.yourdoctor.Models.Appointment;
import com.khemsharma.yourdoctor.Models.Doctor;

import java.util.List;

public class PatientAppointmentsAdapter extends RecyclerView.Adapter<PatientAppointmentsAdapter.ViewHolder> {

    Context context;
    List<Appointment> appointmentList;
    List<Doctor> doctorList;


    public PatientAppointmentsAdapter(Context context, List<Appointment> appointmentList, List<Doctor> doctorList) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.appointment_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int postion) {

        Doctor doctor=doctorList.get(postion);
        Appointment appointment=appointmentList.get(postion);
        viewHolder.name.setText("Name : "+doctor.getName());
        viewHolder.email.setText("Email : "+doctor.getEmail());
        viewHolder.mobile.setText("Mobile : "+doctor.getMobNum());
        viewHolder.date.setText("Date : "+appointment.getDate_time());
        viewHolder.time.setText("Time : "+appointment.getDate_time());
        viewHolder.subject.setText("Subject : "+appointment.getSubject());
        viewHolder.appointmentNumber.setText("Appointment : "+postion+1);

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,email,mobile,date,time,subject,appointmentNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.aName);
            email=itemView.findViewById(R.id.aEmail);
            mobile=itemView.findViewById(R.id.aMob);
            date=itemView.findViewById(R.id.aDate);
            time=itemView.findViewById(R.id.aTime);
            subject=itemView.findViewById(R.id.aSubject);
            appointmentNumber=itemView.findViewById(R.id.aNum);
        }
    }
}
