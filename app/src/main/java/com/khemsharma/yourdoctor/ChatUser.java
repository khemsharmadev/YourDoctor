package com.khemsharma.yourdoctor;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Chats;
import com.khemsharma.yourdoctor.Models.Doctor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatUser extends AppCompatActivity {

    String userId;
    String receiverId,senderId,doctorId,patientId;
    FloatingActionButton sendMessageBtn;
    List<Chats> chatsList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RecyclerView userMessageList;
    ChatAdapter chatAdapter;
    EditText messageTextBox;


    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(FirebaseRef.CHATS).child(doctorId+"_"+patientId);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chats chats = dataSnapshot.getValue(Chats.class);
                chatsList.add(chats);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverId = getIntent().getStringExtra("fromUserId");
        userMessageList=findViewById(R.id.chatRecycleView);
        messageTextBox=findViewById(R.id.message_text_box);
        chatAdapter=new ChatAdapter(this,chatsList);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(chatAdapter);
        sendMessageBtn = findViewById(R.id.send_message_btn);

        if (AppUttils.getUserType(this).equals(Doctor.DOCTOR)){
            doctorId=userId;
            patientId=receiverId;
        }else {
            doctorId=receiverId;
            patientId=userId;
        }

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Chats chats = new Chats(userId,
                        receiverId,
                        messageTextBox.getText().toString(),
                        "text",Long.toString(new Date().getTime()));

                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                        .getReference(FirebaseRef.CHATS).child(doctorId+"_"+patientId);
                String appointment_key = databaseReference.push().getKey();
                databaseReference.child(appointment_key).setValue(chats);
            }
        });
        }
}
