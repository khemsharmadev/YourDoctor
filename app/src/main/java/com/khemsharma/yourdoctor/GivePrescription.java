package com.khemsharma.yourdoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class GivePrescription extends AppCompatActivity {

    DatabaseReference databaseReference;
    String currentUserId;
    List<String> patientsList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ConnectedUsersAdapter connectedUsersAdapter;
    RecyclerView recyclerView;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_prescription);
        recyclerView = findViewById(R.id.user_list_cycle_view);
        databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.CHATS);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        openGallery();
    }




    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
        populateRecycleView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            populateRecycleView();

        }
    }

    private void populateRecycleView() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().startsWith(currentUserId)) {
                        String doctorId_patientId = snapshot.getKey();
                        patientsList.add(AppUttils.extractPatientId(doctorId_patientId));
                    }
                }
                linearLayoutManager = new LinearLayoutManager(GivePrescription.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                connectedUsersAdapter = new ConnectedUsersAdapter(GivePrescription.this, patientsList,imageUri);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(connectedUsersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
