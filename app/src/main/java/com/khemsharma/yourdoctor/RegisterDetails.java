package com.khemsharma.yourdoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Doctor;
import com.khemsharma.yourdoctor.Models.Patient;
import com.khemsharma.yourdoctor.Models.Prescription;

import java.util.Date;

public class RegisterDetails extends AppCompatActivity {

    LinearLayout docView;
    RadioGroup userCat;
    RadioGroup genderCat;

    String gender,userType;

    Doctor doctor;
    Patient patient;

    TextInputEditText name,email,mobNum,age,bio,specialist,accNum,ifscCode;

    Button regSubmit,profileImagePicker;

    FirebaseDatabase database;
    DatabaseReference myRef,subRef;
    private FirebaseAuth mAuth;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    String userFirebasePath;
    String profileURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

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

        regSubmit = findViewById(R.id.regSubmit);

        genderCat = findViewById(R.id.gender);
        profileImagePicker = findViewById(R.id.pick_profile_image_btn);



        mAuth = FirebaseAuth.getInstance();

        userCat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkerId) {
                switch (checkerId) {
                    case R.id.radioD:
                        docView.setVisibility(View.VISIBLE);
                        userType = Doctor.DOCTOR;
                        break;

                    case R.id.radioP:
                        docView.setVisibility(View.GONE);
                        userType = Patient.PATIENT;

                        break;
                }
            }
        });

        genderCat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkerId) {
                switch (checkerId) {
                    case R.id.radioD:
                        gender = findViewById(R.id.radioD).toString();
                        break;

                    case R.id.radioP:
                        gender = findViewById(R.id.radioP).toString();
                        break;
                }
            }
        });


        database = FirebaseDatabase.getInstance();


        regSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadProfileImage();





            }
        });



        profileImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();


            }});



    }

    final DatabaseReference.CompletionListener dataSaveListner = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
            if (databaseError != null) {
                Toast.makeText(RegisterDetails.this, "Registration failed :" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Intent homeScreen;
                if (userType.equals(Doctor.DOCTOR)) {
                    AppUttils.saveUserInfo(RegisterDetails.this, doctor);
                    homeScreen = new Intent(RegisterDetails.this, DoctorsHome.class);


                } else {
                    AppUttils.saveUserInfo(RegisterDetails.this, patient);
                    homeScreen = new Intent(RegisterDetails.this, PatientsHome.class);
                }
                startActivity(homeScreen);


            }
        }
    };


    private void saveRegisteData() {

        if (userType.equals(Doctor.DOCTOR)) {
            myRef = database.getReference(FirebaseRef.DOCTORS);
            subRef = myRef.child(mAuth.getCurrentUser().getUid());
            doctor = new Doctor(name.getEditableText().toString(), email.getEditableText().toString(), profileURL, "OFFLINE", age.getEditableText().toString(), mobNum.getEditableText().toString(), gender, accNum.getEditableText().toString(), bio.getEditableText().toString(), ifscCode.getEditableText().toString(), specialist.getEditableText().toString(), "true");
            subRef.setValue(doctor, dataSaveListner);

        } else {

            myRef = database.getReference(FirebaseRef.PATIENTS);
            subRef = myRef.child(mAuth.getCurrentUser().getUid());
            patient = new Patient(name.getEditableText().toString(), email.getEditableText().toString(), profileURL, "OFFLINE", age.getEditableText().toString(), mobNum.getEditableText().toString(), gender);
            subRef.setValue(patient, dataSaveListner);
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


            }
        }


        private void uploadProfileImage(){

            if (userType!=null)
            {
                userFirebasePath = userType.equals(Doctor.DOCTOR) ?
                        FirebaseRef.PATIENTS : FirebaseRef.DOCTORS;

                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users/" + email.getEditableText().toString() + ".jpg");
                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                profileURL = uri.toString();
                                Toast.makeText(RegisterDetails.this, "Profile Image Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                                saveRegisteData();

                            }
                        });
                    }
                });
            }
            else {
                Toast.makeText(RegisterDetails.this, "First Choose User Type", Toast.LENGTH_SHORT).show();
            }

        }
}
