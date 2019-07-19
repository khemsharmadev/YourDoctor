package com.khemsharma.yourdoctor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Doctor;
import com.khemsharma.yourdoctor.Models.Patient;

public class UpdateProfile extends AppCompatActivity {

    LinearLayout docView;
    RadioGroup userCat;
    String userFirebasePath;
    TextInputEditText name,email,mobNum,age,bio,specialist,accNum,ifscCode;
    Button updateProfileBtn,profileImagePicker;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ImageView prfoileImage;
    String userType,gender;
    Doctor doctor;
    Patient patient;
    String profileURL;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        userCat = findViewById(R.id.user);
        docView = findViewById(R.id.docView);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobNum = findViewById(R.id.mob);
        age = findViewById(R.id.age);
        bio = findViewById(R.id.bio);
        specialist = findViewById(R.id.specialist);
        accNum = findViewById(R.id.accNum);
        ifscCode = findViewById(R.id.ifscCode);
        prfoileImage = findViewById(R.id.profile_image);
        updateProfileBtn = findViewById(R.id.update_profile_submit);
        profileImagePicker = findViewById(R.id.pick_profile_image_btn);
        email.setEnabled(false);
        mAuth = FirebaseAuth.getInstance();
        if (AppUttils.getUserType(this).equals(Doctor.DOCTOR))
        {
            docView.setVisibility(View.VISIBLE);
            userType = Doctor.DOCTOR;
            userFirebasePath = FirebaseRef.DOCTORS;
        }else {
            docView.setVisibility(View.GONE);
            userType = Patient.PATIENT;
            userFirebasePath = FirebaseRef.PATIENTS;
        }
        String currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference(userFirebasePath).child(currentUserId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (userType.equals(Doctor.DOCTOR))
                {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    name.setText(doctor.getName());
                    email.setText(doctor.getEmail());
                    mobNum.setText(doctor.getMobNum());
                    age.setText(doctor.getAge());
                    bio.setText(doctor.getBio());
                    specialist.setText(doctor.getSpecialist());
                    accNum.setText(doctor.getAccNum());
                    ifscCode.setText(doctor.getIfscCode());
                    gender = doctor.getGender();
                }
                else {
                    Patient patient  = dataSnapshot.getValue(Patient.class);
                    name.setText(patient.getName());
                    email.setText(patient.getEmail());
                    mobNum.setText(patient.getMobNum());
                    age.setText(patient.getAge());
                    gender=patient.getGender();
                }

                try {
                    Glide.with(UpdateProfile.this)
                            .load(dataSnapshot.child("profileURL").getValue().toString())
                            .into(prfoileImage);
                }catch (Exception e)
                {
                    Log.d(FirebaseRef.TAG+"Prescription",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        profileImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProfileImage();
            }
        });
    }

    private void saveRegisteData() {
        if (userType.equals(Doctor.DOCTOR)) {
            doctor = new Doctor(name.getEditableText().toString(), email.getEditableText().toString(), profileURL, "OFFLINE", age.getEditableText().toString(), mobNum.getEditableText().toString(), gender, accNum.getEditableText().toString(), bio.getEditableText().toString(), ifscCode.getEditableText().toString(), specialist.getEditableText().toString(), "true");
            reference.setValue(doctor, dataSaveListner);

        } else {
            patient = new Patient(name.getEditableText().toString(), email.getEditableText().toString(), profileURL, "OFFLINE", age.getEditableText().toString(), mobNum.getEditableText().toString(), gender);
            reference.setValue(patient, dataSaveListner);
        }
    }


    private void openGallery() {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(prfoileImage);
        }
    }

    final DatabaseReference.CompletionListener dataSaveListner = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
            if (databaseError != null) {
                Toast.makeText(UpdateProfile.this, "Registration failed :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Intent homeScreen;
                if (userType.equals(Doctor.DOCTOR)) {
                    AppUttils.saveUserInfo(UpdateProfile.this, doctor);
                    homeScreen = new Intent(UpdateProfile.this, DoctorsHome.class);
                } else {
                    AppUttils.saveUserInfo(UpdateProfile.this, patient);
                    homeScreen = new Intent(UpdateProfile.this, PatientsHome.class);
                }
                startActivity(homeScreen);
            }
        }
    };





    private void uploadProfileImage(){
        if (userType!=null)
        {
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users/" + email.getEditableText().toString() + ".jpg");
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            profileURL = uri.toString();
                            Toast.makeText(UpdateProfile.this, "Profile Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                            saveRegisteData();

                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(UpdateProfile.this, "First Choose User Type", Toast.LENGTH_SHORT).show();
        }
    }
}
