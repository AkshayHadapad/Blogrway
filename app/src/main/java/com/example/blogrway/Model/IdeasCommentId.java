package com.example.blogrway.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class IdeasCommentId {

    @Exclude
    public String ComentsId;

    public <T extends IdeasCommentId>  T withideasId (@NonNull final String id){
        this.ComentsId = id;
        return (T) this;
    }
}
