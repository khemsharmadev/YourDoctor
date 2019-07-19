package com.khemsharma.yourdoctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;

import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;

public class DoctorsHome extends AppCompatActivity {


    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_home);

        mainGrid =  findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);

    }


    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (finalI == 0) {
                        startActivity(new Intent(DoctorsHome.this,AllChats.class));
                    } else if (finalI == 1) {
                        startActivity(new Intent(DoctorsHome.this,DoctorAppointments.class));
                    } else if (finalI == 2) {
                        startActivity(new Intent(DoctorsHome.this,GivePrescription.class));
                    } else if (finalI == 3) {
                        startActivity(new Intent(DoctorsHome.this,AllPrescriptions.class));

                    } else if (finalI == 4)
                        startActivity(new Intent(DoctorsHome.this,UpdateProfile.class));

                    else if (finalI == 5)
                    {
                        AppUttils.logOut(DoctorsHome.this);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(DoctorsHome.this,LoginActivity.class));
                    }
                }
            });
        }
    }
}
