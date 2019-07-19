package com.khemsharma.yourdoctor;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorDetails extends AppCompatActivity {

    String doctorId;

    TextView name,specialist,email,bio,mobile,age,status,apointment_status;

    Button chatDoc,bookDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        doctorId = getIntent().getStringExtra("doctorId");

        name=findViewById(R.id.dName);
        specialist=findViewById(R.id.dSpecialist);
        email=findViewById(R.id.dEmail);
        bio=findViewById(R.id.dBio);
        age=findViewById(R.id.dAge);
        mobile=findViewById(R.id.dMob);
        status=findViewById(R.id.dStatus);
        apointment_status=findViewById(R.id.d_ap_status);

        bookDoc=findViewById(R.id.bookDoc);


        chatDoc=findViewById(R.id.chatDoc);

        if (doctorId!=null){
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference(FirebaseRef.DOCTORS).child(doctorId);

            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Doctor doctor = dataSnapshot.getValue(Doctor.class);

                    name.setText(doctor.getName());
                    specialist.setText(doctor.getSpecialist());
                    email.setText(doctor.getEmail());
                    bio.setText(doctor.getBio());
                    age.setText(doctor.getAge());
                    mobile.setText(doctor.getMobNum());
                    status.setText(doctor.getStatus());
                    apointment_status.setText(doctor.getAp_status());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        bookDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookAppointments = new Intent(DoctorDetails.this,BookAppointments.class);
                bookAppointments.putExtra("doctorId",doctorId);
                startActivity(bookAppointments);
            }
        });


        chatDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatUser = new Intent(DoctorDetails.this,ChatUser.class);
                chatUser.putExtra("fromUserId",doctorId);

                startActivity(chatUser);
            }
        });



    }
}
