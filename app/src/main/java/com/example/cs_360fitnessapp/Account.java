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

    @ColumnInfo(name="goal_weight")
    private float mGoalWeight = 0.f;

    public Account(@NonNull String username, @NonNull String password){
        mUsername = username;
        mPassword = password;
    }

    @NonNull
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(@NonNull String username) {
        this.mUsername = mUsername;
    }

    @NonNull
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(@NonNull String password) {
        this.mPassword = password;
    }

    public float getGoalWeight() { return mGoalWeight; }

    public void setGoalWeight(float goalWeight) { this.mGoalWeight = goalWeight; }
}
