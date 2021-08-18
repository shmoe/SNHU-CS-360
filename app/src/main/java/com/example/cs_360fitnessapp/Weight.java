package com.example.cs_360fitnessapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "weights", foreignKeys = @ForeignKey(entity = Account.class, parentColumns = "username", childColumns = "username"))
@TypeConverters(DateConverter.class)
public class Weight {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int mId;

    @ColumnInfo(name = "weight")
    private float mWeight = 0.f;

    @NonNull
    @ColumnInfo(name = "datetime")
    private Date mDateTime;

    @NonNull
    @ColumnInfo(name = "username")
    private String mUsername;

    public Weight(float weight, @NonNull String username){
        mDateTime = new Date(System.currentTimeMillis());
        mWeight = weight;
        mUsername = username;
    }

    public int getId() { return mId; }

    public void setId(int mId) { this.mId = mId; }

    public float getWeight() {
        return mWeight;
    }

    public void setWeight(float weight) {
        this.mWeight = weight;
    }

    @NonNull
    public Date getDateTime() {
        return mDateTime;
    }

    public void setDateTime(@NonNull Date dateTime) {
        this.mDateTime = dateTime;
    }

    @NonNull
    public String getUsername() { return mUsername; }

    public void setUsername(@NonNull String mUsername) { this.mUsername = mUsername; }
}
