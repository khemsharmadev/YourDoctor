package com.khemsharma.yourdoctor;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.Models.Doctor;


public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    TextView logoText;
    ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logoText = findViewById(R.id.logoText);
        logoImage = findViewById(R.id.splash_logo);
        final Animation slide_up=AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_up);
        logoText.startAnimation(slide_up);
        logoImage.startAnimation(slide_up);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppUttils.isUserLogin(getApplicationContext())){

                    if (AppUttils.getUserType(SplashScreen.this).equals(Doctor.DOCTOR))
                    {
                        Intent intent = new Intent(SplashScreen.this,DoctorsHome.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashScreen.this,PatientsHome.class);
                        startActivity(intent);
                        finish();
                    }
                }
               else{
                    Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);

    }
}
