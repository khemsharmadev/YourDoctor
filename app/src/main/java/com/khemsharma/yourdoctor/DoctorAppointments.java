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
import com.khemsharma.yourdoctor.Models.Patient;

import java.util.ArrayList;
import java.util.List;

public class DoctorAppointments extends AppCompatActivity {

    TextView appointmentMsg;
    RecyclerView allAppointments;
    String patientId;

    List<Appointment> appointmentsList = new ArrayList<>();
    List<Patient> patientList = new ArrayList<>();

    DoctorAppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointments);appointmentMsg=findViewById(R.id.appointmentMsg);
        allAppointments=findViewById(R.id.allAppointments);


        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseRef.APPOINTMENTS).child("date_time").getParent();


        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {



                    Log.d(FirebaseRef.TAG+"d+p",dataSnapshot.getKey());

                    if (dataSnapshot.getKey().startsWith(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                        String patientId= AppUttils.extractPatientId(dataSnapshot.getKey());

                        for (DataSnapshot subSnapshot : dataSnapshot.getChildren()) {

                            Log.d(FirebaseRef.TAG+"Main",subSnapshot.getKey().toString());

                            Appointment appointment = subSnapshot.getValue(Appointment.class);

                            appointmentsList.add(appointment);

                            getDoctorsData(patientId);

                            //doctorsList.add(doctor);
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

    private void getDoctorsData(String patientId) {

        final Patient[] patients = new Patient[1];

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.PATIENTS).child(patientId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patients[0] = dataSnapshot.getValue(Patient.class);
                patientList.add(patients[0]);


                Log.d(FirebaseRef.TAG+"pDoc",dataSnapshot.toString());

                LinearLayoutManager llm = new LinearLayoutManager(DoctorAppointments.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                allAppointments.setLayoutManager(llm);

                adapter=new DoctorAppointmentsAdapter(DoctorAppointments.this,appointmentsList,patientList);
                allAppointments.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}