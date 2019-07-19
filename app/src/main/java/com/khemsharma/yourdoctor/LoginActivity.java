package com.khemsharma.yourdoctor;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Doctor;
import com.khemsharma.yourdoctor.Models.Patient;



public class LoginActivity extends AppCompatActivity {

    Button registerBtn,loginWithProvider,loginBtn;

    EditText username,password;

    //Sign in firebase
    private FirebaseAuth mAuth;

    private int RC_SIGN_IN= 1;

    private String userType;

    DatabaseReference database;

    GoogleSignInClient mGoogleSignInClient;

    String firebasePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Google plus
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




        registerBtn=findViewById(R.id.registerNowBtn);
        loginWithProvider=findViewById(R.id.loginWithBtn);
        loginBtn=findViewById(R.id.loginBtn);


        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(register);
            }
        });




        loginWithProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.login_provider_popup, null);



                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);



                popupWindow.setBackgroundDrawable(LoginActivity.this.getDrawable(R.drawable.pop_bg));
                popupWindow.setElevation(43);


                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });


                Button loginWithGoogle = popupView.findViewById(R.id.loginG);

                loginWithGoogle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        signInWithGoogle();
                    }
                });


            }
        });




        //Launch user chooser in end
        findViewById(R.id.loginMainLayout).post(new Runnable() {
            @Override
            public void run() {
                chooseUserTypePopup();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    firebaseAuthWithEmail(username.getText().toString(),password.getText().toString());
            }
        });





    }

    private void chooseUserTypePopup() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.user_type_list, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);





        popupWindow.setBackgroundDrawable(LoginActivity.this.getDrawable(R.drawable.pop_bg));
        popupWindow.setElevation(43);


        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(LoginActivity.this.findViewById(R.id.loginMainLayout), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        popupView.findViewById(R.id.selPatient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebasePath=FirebaseRef.PATIENTS;
                userType=Patient.PATIENT;
                popupWindow.dismiss();
            }
        });

        popupView.findViewById(R.id.selDoctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebasePath=FirebaseRef.DOCTORS;
                userType=Doctor.DOCTOR;
                popupWindow.dismiss();
            }
        });

    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(FirebaseRef.TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser!=null&&userType!=null&&AppUttils.getUserType(LoginActivity.this)!=null)
        {
            Intent homeScreen=userType.equals(Doctor.DOCTOR) ?
                    new Intent(LoginActivity.this,DoctorsHome.class):
                    new Intent(LoginActivity.this,PatientsHome.class);

            startActivity(homeScreen);

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(FirebaseRef.TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FirebaseRef.TAG, "signInWithCredential:success");

                            saveUsersData();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FirebaseRef.TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void firebaseAuthWithEmail(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FirebaseRef.TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            createUserWithEmail(email,password);
                            Toast.makeText(LoginActivity.this, "User not found creating", Toast.LENGTH_SHORT).show();
                            Log.d(FirebaseRef.TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private void saveUsersData() {


        database = FirebaseDatabase.getInstance().getReference(firebasePath)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        database.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                Log.d(FirebaseRef.TAG+"LoginData",dataSnapshot.toString());

                Intent homeScreen;
                if (userType.equals(Doctor.DOCTOR))
                {

                    Doctor doctor = dataSnapshot.getValue(Doctor.class);

                    homeScreen = new Intent(LoginActivity.this,DoctorsHome.class);
                    AppUttils.saveUserInfo(LoginActivity.this,doctor);


                }else {
                    Patient patient = dataSnapshot.getValue(Patient.class);

                    homeScreen = new Intent(LoginActivity.this,PatientsHome.class);
                    AppUttils.saveUserInfo(LoginActivity.this,patient);

                }
                startActivity(homeScreen);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void createUserWithEmail(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FirebaseRef.TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,RegisterDetails.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(FirebaseRef.TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
