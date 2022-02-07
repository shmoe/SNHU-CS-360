package com.example.cs_360fitnessapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="accounts")
public class Account {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="username")
    private String mUsername = "";

    @NonNull
    @ColumnInfo(name="password")
    private String mPassword = "";

    @NonNull
    @ColumnInfo(name="salt", typeAffinity = ColumnInfo.BLOB)
    private byte [] mSalt = new byte [16];

    @ColumnInfo(name="goal_weight")
    private float mGoalWeight = 0.f;

    public Account(@NonNull String username, @NonNull String password, @NonNull byte [] salt){
        mUsername = username;
        mPassword = password;
        mSalt = salt;
    }

    @NonNull
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(@NonNull String username) {
        this.mUsername = username;
    }

    @NonNull
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(@NonNull String password) {
        this.mPassword = password;
    }

    @NonNull
    public byte [] getSalt() { return mSalt; }

    public void setSalt(@NonNull byte [] salt) { this.mSalt = salt; }

    public float getGoalWeight() { return mGoalWeight; }

    public void setGoalWeight(float goalWeight) { this.mGoalWeight = goalWeight; }
}
