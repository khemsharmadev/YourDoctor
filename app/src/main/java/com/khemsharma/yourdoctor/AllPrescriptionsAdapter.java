package com.khemsharma.yourdoctor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Chats;
import com.khemsharma.yourdoctor.Models.Doctor;
import com.khemsharma.yourdoctor.Models.Patient;
import com.khemsharma.yourdoctor.Models.Prescription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllPrescriptionsAdapter extends RecyclerView.Adapter<AllPrescriptionsAdapter.ViewHolder> {


    Context context;
    List<Prescription> prescriptionList=new ArrayList<>();
    FirebaseAuth mAuth;
    String userFirebasePath;
    DatabaseReference userRef;
    String fromUserId;
    List<String> userList= new ArrayList<>();

    public AllPrescriptionsAdapter(Context context, List<Prescription> prescriptionList,List<String> userList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.prescriptions_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        mAuth=FirebaseAuth.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Prescription prescription = prescriptionList.get(position);

        if (prescription!=null){

            userFirebasePath = (AppUttils.getUserType(context).equals(Doctor.DOCTOR))? FirebaseRef.PATIENTS:
                    FirebaseRef.DOCTORS;
            userRef=FirebaseDatabase.getInstance().getReference().child(userFirebasePath).child(userList.get(position));

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(FirebaseRef.TAG+userFirebasePath,dataSnapshot.toString());

                    if (dataSnapshot.hasChild("profileURL"))
                    {
                        String name=dataSnapshot.child("name").getValue().toString();
                        holder.userName.setText("Name : "+name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            Date issueDate = new Date(Long.parseLong(prescription.getIssueDate()));
            holder.issueDate.setText("Issue Date : "+issueDate.toString());

            try {
                Glide.with(context)
                        .load(prescription.getFileURI())
                        .into(holder.prescriptionFile);
            }catch (Exception e)
            {
                Log.d(FirebaseRef.TAG+"Prescription",e.getMessage());
            }

            holder.prescriptionNumber.setText("Prescription : "+(position+1));
        }

    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView prescriptionFile;
        TextView issueDate,userName,prescriptionNumber;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prescriptionFile=itemView.findViewById(R.id.prescription_file);
            issueDate=itemView.findViewById(R.id.prescription_issue_date);
            userName=itemView.findViewById(R.id.prescription_user_name);
            prescriptionNumber=itemView.findViewById(R.id.prescription_number);


        }
    }
}
