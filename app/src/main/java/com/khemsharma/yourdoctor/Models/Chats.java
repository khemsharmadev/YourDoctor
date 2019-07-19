package com.khemsharma.yourdoctor.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Chats {

    String messageId,receiverId,senderId,messageText,messageType;

    String timestamp;

    public Chats() {
    }

    public Chats(String senderId, String receiverId, String messageText, String messageType,String timestamp) {
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.messageType = messageType;

        this.timestamp = timestamp;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
