package com.example.blogrway.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class IdeasId {
    @Exclude
    public  String IdeasId;
    public <T extends IdeasId> T withideasId(@NonNull final String id){
        this.IdeasId=id;
        return  (T) this;
    }
}
