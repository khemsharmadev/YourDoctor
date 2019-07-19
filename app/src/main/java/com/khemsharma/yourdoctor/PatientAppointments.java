package com.khemsharma.yourdoctor;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Appointment;
import com.khemsharma.yourdoctor.Models.Doctor;

import java.util.ArrayList;
import java.util.List;

public class PatientAppointments extends AppCompatActivity {

    TextView appointmentMsg;
    RecyclerView allAppointments;
    String patientId;
    
    List<Appointment> appointmentsList = new ArrayList<>();
    List<Doctor> doctorsList = new ArrayList<>();

    PatientAppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        appointmentMsg=findViewById(R.id.appointmentMsg);
        allAppointments=findViewById(R.id.allAppointments);


        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseRef.APPOINTMENTS).child("date_time").getParent();


        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().endsWith(patientId)){
                        String doctorId= AppUttils.extractDoctorId(dataSnapshot.getKey());
                        for (DataSnapshot subSnapshot : dataSnapshot.getChildren()) {
                            Appointment appointment = subSnapshot.getValue(Appointment.class);
                            appointmentsList.add(appointment);
                            getDoctorsData(doctorId);
                        }
                    }
                }
                if (snapshot.getChildrenCount()<=0)
                {
                    appointmentMsg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDoctorsData(String doctorId) {

        final Doctor[] doctor = new Doctor[1];

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.DOCTORS).child(doctorId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctor[0] = dataSnapshot.getValue(Doctor.class);
                doctorsList.add(doctor[0]);
                LinearLayoutManager llm = new LinearLayoutManager(PatientAppointments.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                allAppointments.setLayoutManager(llm);
                adapter=new PatientAppointmentsAdapter(PatientAppointments.this,appointmentsList,doctorsList);
                allAppointments.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
