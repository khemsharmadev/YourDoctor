package com.khemsharma.yourdoctor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Appointment;
import com.khemsharma.yourdoctor.Models.Appointments;
import com.khemsharma.yourdoctor.Models.BookedAppointments;
import com.khemsharma.yourdoctor.Models.Doctor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookAppointments extends AppCompatActivity implements View.OnClickListener{

    String doctorId,date_time,patientId;

    Button btnDatePicker, btnTimePicker,aCheckBtn;
    EditText txtDate, txtTime,aSub;
    private int mYear, mMonth, mDay, mHour, mMinute;

    Boolean appointmentFound=false;

    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointments);

        doctorId=getIntent().getStringExtra("doctorId");

        btnDatePicker=findViewById(R.id.btn_date);
        btnTimePicker=findViewById(R.id.btn_time);
        txtDate=findViewById(R.id.in_date);

        txtTime=findViewById(R.id.in_time);

        aSub=findViewById(R.id.aSub);
        aCheckBtn=findViewById(R.id.aCheckBtn);



        patientId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        aCheckBtn.setOnClickListener(this);


    }

    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            date_time=dayOfMonth +(monthOfYear + 1) + Integer.toString(year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            //mMinute = c.get(Calendar.MINUTE);
            mMinute = 0;

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (minute!=0)
                            {
                                Toast.makeText(BookAppointments.this, "Time can only booked on hourly bases", Toast.LENGTH_SHORT).show();
                            }

                            mHour = hourOfDay;
                            mMinute = minute;
                            String timeSet = "";
                            if (mHour > 12) {
                                mHour -= 12;
                                timeSet = "PM";
                            } else if (mHour == 0) {
                                mHour += 12;
                                timeSet = "AM";
                            } else if (mHour == 12){
                                timeSet = "PM";
                            }else{
                                timeSet = "AM";
                            }

                            txtTime.setText(mHour + ":" +"00 "+timeSet);
                            date_time=date_time+"_"+Integer.toString(mHour);
                        }
                    }, mHour, mMinute, false);

            timePickerDialog.show();
        }

        if (v == aCheckBtn){

            if (txtDate!=null||txtTime!=null)
            {
                databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.BOOKED_APPOINTMENTS).child(doctorId);

                databaseReference.addListenerForSingleValueEvent(saveInfo);

                txtDate.setText(null);
                txtTime.setText(null);
            }else Toast.makeText(this, "Enter date and time.", Toast.LENGTH_SHORT).show();



        }
    }



    ValueEventListener saveInfo =new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {




            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                Log.d("AppointmentLog",dataSnapshot.toString());

                BookedAppointments bookedAppointments = dataSnapshot.getValue(BookedAppointments.class);

                if (bookedAppointments!=null){
                    if (bookedAppointments.getDate_time().equals(date_time)){
                        appointmentFound=true;
                        databaseReference.removeEventListener(saveInfo);
                        Toast.makeText(BookAppointments.this, "Already Booked. Choose other time.", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(BookAppointments.this, "No Booking Found", Toast.LENGTH_SHORT).show();
                        bookAppointmentNow(date_time);
                    }
                }else {
                    Toast.makeText(BookAppointments.this, "No Booking Found", Toast.LENGTH_SHORT).show();
                    bookAppointmentNow(date_time);
                }
            }

            if (!snapshot.hasChildren()){
                Toast.makeText(BookAppointments.this, "No Booking Found", Toast.LENGTH_SHORT).show();
                bookAppointmentNow(date_time);
            }




        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void bookAppointmentNow(String date_time) {
        String subject = (aSub.getEditableText() == null) ? "NA" : aSub.getEditableText().toString();
        databaseReference=databaseReference.getDatabase().getReference(FirebaseRef.APPOINTMENTS).child(doctorId+"_"+patientId);
        String appointmentId = databaseReference.child(doctorId+"_"+patientId).push().getKey();
        DatabaseReference subRef = databaseReference.child(appointmentId);

        subRef.setValue(new Appointment(date_time,subject));

        databaseReference=databaseReference.getDatabase().getReference(FirebaseRef.BOOKED_APPOINTMENTS);
        subRef=databaseReference.child(doctorId).child(appointmentId);
        subRef.setValue(new BookedAppointments(date_time), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                /*Log.d("AppointmentLog",databaseError.getMessage());*/
            }
        });

        Toast.makeText(this, "Booked", Toast.LENGTH_SHORT).show();



        databaseReference.removeEventListener(saveInfo);

    }
}
