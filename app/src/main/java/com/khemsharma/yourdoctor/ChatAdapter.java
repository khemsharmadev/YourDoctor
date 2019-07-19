package com.khemsharma.yourdoctor;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
import com.khemsharma.yourdoctor.AppUttils.AppUttils;
import com.khemsharma.yourdoctor.AppUttils.FirebaseRef;
import com.khemsharma.yourdoctor.Models.Chats;
import com.khemsharma.yourdoctor.Models.Doctor;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    Context context;
    List<Chats> chatsList;
    String userFirebasePath;
    DatabaseReference userRef;
    FirebaseAuth mAuth;
    String profileImageURL;


    public ChatAdapter(Context context, List<Chats> chatsList) {
        this.context = context;
        this.chatsList = chatsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_chat_list,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        mAuth=FirebaseAuth.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();



        Chats chats = chatsList.get(position);

        if (chats!=null){

            String chatUserId =messageSenderId.equals(chats.getSenderId())? chats.getReceiverId(): chats.getSenderId();
            String fromUserId=chats.getSenderId();
            String messageType=chats.getMessageType();

            userFirebasePath = AppUttils.getUserType(context).equals(Doctor.DOCTOR)?
                    FirebaseRef.PATIENTS:FirebaseRef.DOCTORS;

            userRef=FirebaseDatabase.getInstance().getReference().child(userFirebasePath).child(chatUserId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("profileURL"))
                    {
                        profileImageURL=dataSnapshot.child("profileURL").getValue().toString();
                        Log.d(FirebaseRef.TAG+"profile",profileImageURL);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (messageType.equals("text"))
            {
                viewHolder.receiverMessageText.setVisibility(View.INVISIBLE);
                viewHolder.senderMessageText.setVisibility(View.INVISIBLE);

                if (fromUserId.equals(messageSenderId)){
                    viewHolder.senderMessageText.setBackgroundResource(R.drawable.sender_message_bg);
                    viewHolder.senderMessageText.setTextColor(Color.BLACK);
                    viewHolder.senderMessageText.setText(chats.getMessageText());

                    viewHolder.senderMessageText.setVisibility(View.VISIBLE);
                    viewHolder.profileImage.setVisibility(View.INVISIBLE);



                }
                else {
                    viewHolder.senderMessageText.setVisibility(View.INVISIBLE);
                    viewHolder.receiverMessageText.setVisibility(View.VISIBLE);

                    viewHolder.receiverMessageText.setBackgroundResource(R.drawable.receiver_message_bg);
                    viewHolder.receiverMessageText.setTextColor(Color.BLACK);
                    viewHolder.receiverMessageText.setText(chats.getMessageText());
                    Glide.with(context).load(profileImageURL).into(viewHolder.profileImage);
                    viewHolder.profileImage.setVisibility(View.VISIBLE);




                }
            }






        }





    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView senderMessageText,receiverMessageText;
        ImageView profileImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_message);
            receiverMessageText = itemView.findViewById(R.id.receiver_message);
            profileImage = itemView.findViewById(R.id.message_profile_image);




        }
    }
}
