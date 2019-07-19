package com.khemsharma.yourdoctor.AppUttils;

import android.content.Context;
import android.content.SharedPreferences;

import com.khemsharma.yourdoctor.Models.Doctor;
import com.khemsharma.yourdoctor.Models.Patient;

import java.util.ArrayList;
import java.util.List;

public class AppUttils {

    private static String NULL = "NA";
    public static final String CSV_SEPARATOR = ",";
    private static SharedPreferences sharedPreferences;

    public static void saveUserInfo(Context context, Patient user)
    {
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("UserID",user.getPatientId());
        editor.putString("name",user.getName());
        editor.putString("email",user.getEmail());
        editor.putString("userType",user.getUserType());
        editor.putString("age",user.getAge());
        editor.putString("gender",user.getGender());
        editor.putString("mobNum",user.getMobNum());
        editor.putString("profileURL",user.getProfileURL());


        editor.putBoolean("userLogin",true);
        editor.commit();
    }

    public static String getUserType(Context context){
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        return sharedPreferences.getString("userType",NULL);
    }

    public static void saveUserInfo(Context context, Doctor user)
    {
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        editor.putString("UserID",user.getDoctorID());
        editor.putString("name",user.getName());
        editor.putString("email",user.getEmail());
        editor.putString("userType",user.getUserType());
        editor.putString("age",user.getAge());
        editor.putString("gender",user.getGender());
        editor.putString("mobNum",user.getMobNum());
        editor.putString("profileURL",user.getProfileURL());

        editor.putString("specialist",user.getSpecialist());
        editor.putString("bio",user.getBio());
        editor.putString("ap_status",user.getAp_status());
        editor.putString("ifscCode",user.getIfscCode());
        editor.putString("accNum",user.getAccNum());


        editor.putBoolean("userLogin",true);
        editor.commit();
    }



    public static Boolean isUserLogin(Context context){
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("userLogin",false);
    }

    public static void logOut(Context context){
        if (sharedPreferences==null)
            sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }


    public static String extractDoctorId(String doctorId_patientId) {

        return doctorId_patientId=doctorId_patientId.substring(0,doctorId_patientId.indexOf('_'));
    }

    public static String extractPatientId(String doctorId_patientId) {

        return doctorId_patientId.substring(doctorId_patientId.indexOf('_')+1,doctorId_patientId.length());
    }
}
