package com.example.sapbusinessuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Scores extends RealmObject {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public int getTrial() {
        return trial;
    }

    public void setTrial(int trial) {
        this.trial = trial;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @SerializedName("id")
    @Expose
    @PrimaryKey
    public String id ;
    @SerializedName("topic_id")
    @Expose
    public String topic_id;
    @SerializedName("less_id")
    @Expose
    public int trial;
    @Expose
    @SerializedName("score")
    public int score;

}
