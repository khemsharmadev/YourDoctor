package com.khemsharma.yourdoctor.Models;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Prescription {

    String fileURI;
    String issueDate;

    public Prescription() {
    }

    public Prescription(String fileURI,String issueDate) {
        this.fileURI = fileURI;
        this.issueDate=issueDate;
    }

    public String getFileURI() {
        return fileURI;
    }

    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}
