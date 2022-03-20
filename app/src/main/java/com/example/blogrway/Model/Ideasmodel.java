package com.example.blogrway.Model;

import java.util.Date;

public class Ideasmodel extends IdeasId {

    private String topic,headline,story, user ;
    private Date time;

    public String getTopic() {
        return topic;
    }

    public String getHeadline() {
        return headline;
    }

    public String getStory() {
        return story;
    }

    public String getUser() {
        return user;
    }

    public Date getTime() {
        return time;
    }
}
