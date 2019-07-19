package com.khemsharma.yourdoctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Chats;
import com.khemsharma.yourdoctor.Models.Doctor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AllChatAdapter extends RecyclerView.Adapter<AllChatAdapter.ViewHolder> {

    Context context;
    List<Chats> chatsList;
    FirebaseAuth mAuth;
    String userFirebasePath;
    DatabaseReference userRef;
    String fromUserId;


    public AllChatAdapter() {
    }

    public AllChatAdapter(Context context, List<Chats> chatsList) {
        this.context = context;
        this.chatsList = chatsList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_chats_custom_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        mAuth=FirebaseAuth.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        String currentUser = mAuth.getCurrentUser().getUid();

        Chats chats = chatsList.get(position);

        if (chats!=null){

            userFirebasePath = AppUttils.getUserType(context).equals(Doctor.DOCTOR)?
                    FirebaseRef.PATIENTS:FirebaseRef.DOCTORS;

            fromUserId = currentUser.equals(chats.getSenderId()) ? chats.getReceiverId():chats.getSenderId();
            String messageType=chats.getMessageType();
            userRef=FirebaseDatabase.getInstance().getReference().child(userFirebasePath).child(fromUserId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(FirebaseRef.TAG+userFirebasePath,dataSnapshot.toString());

                    if (dataSnapshot.hasChild("profileURL"))
                    {

                        String name=dataSnapshot.child("name").getValue().toString();
                        viewHolder.userName.setText(name);
                        //Log.d(FirebaseRef.TAG+"profile",profileImageURL);
                        Glide.with(context).load(dataSnapshot.child("profileURL").getValue().toString())
                                .into(viewHolder.profileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (messageType.equals("text"))
            {
                viewHolder.lastMessage.setText(chats.getMessageText());


                Date date = new Date(Long.parseLong(chats.getTimestamp()));
                String timestamp = date.toString().substring(0,date.toString().length()-5);
                viewHolder.lastMessageTime.setText(timestamp);
            }
        }

        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatUser = new Intent(context,ChatUser.class);
                chatUser.putExtra("fromUserId",fromUserId);
                context.startActivity(chatUser);
            }
        });



    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lastMessageTime,lastMessage,userName;
        ImageView profileImage;
        CardView mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lastMessage=itemView.findViewById(R.id.user_chat_message);
            lastMessageTime=itemView.findViewById(R.id.last_chat_timestamp);
            userName=itemView.findViewById(R.id.user_name);
            mainLayout=itemView.findViewById(R.id.all_chat_main_layout);
            profileImage=itemView.findViewById(R.id.all_chat_profile_image);


        }
    }
}
