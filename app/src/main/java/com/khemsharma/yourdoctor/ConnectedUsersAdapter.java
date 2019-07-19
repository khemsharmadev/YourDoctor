package com.khemsharma.yourdoctor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Patient;
import com.khemsharma.yourdoctor.Models.Prescription;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class ConnectedUsersAdapter extends RecyclerView.Adapter<ConnectedUsersAdapter.ViewHolder> {

    Context context;
    List<String> patientsList;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    Uri imageUri;

    public ConnectedUsersAdapter() {
    }

    public ConnectedUsersAdapter(Context context, List<String> patientsList,Uri imageUri) {
        this.context = context;
        this.patientsList = patientsList;
        this.imageUri = imageUri;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        mAuth=FirebaseAuth.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final String patientId = patientsList.get(position);
        if (patientId!=null) {
            userRef = FirebaseDatabase.getInstance().getReference(FirebaseRef.PATIENTS).child(patientId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("profileURL")) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String profileURL = dataSnapshot.child("profileURL").getValue().toString();
                        Glide.with(context).load(profileURL).into(viewHolder.profileImage);
                        viewHolder.userName.setText(name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }


        viewHolder.mainLayoput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.PRESCRIPTIONS)
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"_"+patientId);
                String prescription_id=databaseReference.push().getKey();
                final DatabaseReference prescriptionRef= databaseReference.child(prescription_id);

                final StorageReference storageReference =  FirebaseStorage.getInstance().getReference().child("prescriptions/"+prescription_id+".jpg");
                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Prescription prescription= new Prescription(uri.toString(),Long.toString(new Date().getTime()));
                                prescriptionRef.setValue(prescription, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        context.startActivity(new Intent(context,DoctorsHome.class));

                                    }
                                });
                                Toast.makeText(context, "Prescription uploaded successfully", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
            }
        });

        }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView profileImage;
        TextView userName;
        CardView mainLayoput;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage= itemView.findViewById(R.id.user_profile_image);
            userName= itemView.findViewById(R.id.user_name);
            mainLayoput= itemView.findViewById(R.id.user_list_main_layout);
        }
    }
}
