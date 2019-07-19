package com.khemsharma.yourdoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Doctor;
import com.khemsharma.yourdoctor.Models.Prescription;
import java.util.ArrayList;
import java.util.List;

public class AllPrescriptions extends AppCompatActivity {

    RecyclerView allPrescriptionList;
    List<Prescription> prescriptionList=new ArrayList<>();
    DatabaseReference databaseReference;
    String currentUserId;
    LinearLayoutManager linearLayoutManager;
    List<String> userList=new ArrayList<>();
    AllPrescriptionsAdapter allPrescriptionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_prescriptions);
        allPrescriptionList= findViewById(R.id.all_prescription_cycle);
        currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference(FirebaseRef.PRESCRIPTIONS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    if (AppUttils.getUserType(AllPrescriptions.this).equals(Doctor.DOCTOR)) {
                        if (snapshot.getKey().startsWith(currentUserId)) {
                            final String doctorId_patientId=snapshot.getKey();
                            Query lastMessageQuery = databaseReference.child(doctorId_patientId).orderByChild("issueDate");
                            lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot subSnapshot:dataSnapshot.getChildren())
                                    {
                                        Prescription prescription = subSnapshot.getValue(Prescription.class);
                                        prescriptionList.add(prescription);
                                        userList.add(AppUttils.extractPatientId(doctorId_patientId));
                                        allPrescriptionsAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    } else {
                        if (snapshot.getKey().endsWith(currentUserId)) {
                            final String doctorId_patientId=snapshot.getKey();
                            Query lastMessageQuery = databaseReference.child(doctorId_patientId).orderByChild("issueDate");
                            lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot subSnapshot:dataSnapshot.getChildren())
                                    {
                                        Prescription prescription = subSnapshot.getValue(Prescription.class);
                                        prescriptionList.add(prescription);

                                        userList.add(AppUttils.extractDoctorId(doctorId_patientId));
                                        allPrescriptionsAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                }
                linearLayoutManager = new LinearLayoutManager(AllPrescriptions.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                allPrescriptionsAdapter=new AllPrescriptionsAdapter(AllPrescriptions.this,prescriptionList,userList);
                allPrescriptionList.setLayoutManager(linearLayoutManager);
                allPrescriptionList.setAdapter(allPrescriptionsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
