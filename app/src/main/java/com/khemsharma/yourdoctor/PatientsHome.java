package com.khemsharma.yourdoctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;

public class PatientsHome extends AppCompatActivity {

    GridLayout mainGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_home);
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
                        startActivity(new Intent(PatientsHome.this,AllChats.class));
                    } else if (finalI == 1) {
                        startActivity(new Intent(PatientsHome.this,AllDoctors.class));
                    } else if (finalI == 2) {
                        startActivity(new Intent(PatientsHome.this,PatientAppointments.class));
                    } else if (finalI == 3) {
                        startActivity(new Intent(PatientsHome.this,AllPrescriptions.class));

                    } else if (finalI == 4)
                        startActivity(new Intent(PatientsHome.this,UpdateProfile.class));

                    else if (finalI == 5)
                    {
                        AppUttils.logOut(PatientsHome.this);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(PatientsHome.this,LoginActivity.class));
                    }
                }
            });
        }
    }
}
