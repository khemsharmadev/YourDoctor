package com.khemsharma.yourdoctor;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Chats;
import com.khemsharma.yourdoctor.Models.Doctor;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class AllChats extends AppCompatActivity {

    RecyclerView allChatRecycleView;
    DatabaseReference databaseReference;
    String currentUserId;
    List<Chats> chatList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    AllChatAdapter allChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        allChatRecycleView = findViewById(R.id.allChatsRecycleView);
        databaseReference= FirebaseDatabase.getInstance().getReference(FirebaseRef.CHATS);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot :dataSnapshot.getChildren()) {

                    Log.d(FirebaseRef.TAG+"top",snapshot.toString());


                    if (AppUttils.getUserType(AllChats.this).equals(Doctor.DOCTOR)) {
                        if (snapshot.getKey().startsWith(currentUserId)) {
                            String doctorId_patientId=snapshot.getKey();
                            //String chatId = snapshot.getValue(String.class);

                        Query lastMessageQuery = databaseReference.child(doctorId_patientId).orderByChild("timestamp").limitToLast(1);
                        lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot subSnapshot:dataSnapshot.getChildren())
                                {
                                    Chats chat = subSnapshot.getValue(Chats.class);
                                    chatList.add(chat);
                                    allChatAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        }
                    } else {
                        if (snapshot.getKey().endsWith(currentUserId)) {
                            String doctorId_patientId=snapshot.getKey();
                            Log.d(FirebaseRef.TAG+"in",snapshot.toString());
                            Query lastMessageQuery = databaseReference.child(doctorId_patientId).orderByChild("timestamp").limitToLast(1);
                            lastMessageQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot subSnapshot:dataSnapshot.getChildren())
                                    {
                                        Log.d(FirebaseRef.TAG+"in",subSnapshot.toString());
                                        Chats chat = subSnapshot.getValue(Chats.class);
                                        chatList.add(chat);
                                        allChatAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                }
                linearLayoutManager = new LinearLayoutManager(AllChats.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                allChatAdapter=new AllChatAdapter(AllChats.this,chatList);
                allChatRecycleView.setLayoutManager(linearLayoutManager);
                allChatRecycleView.setAdapter(allChatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
