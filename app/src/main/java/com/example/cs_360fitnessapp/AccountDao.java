package com.example.cs_360fitnessapp;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AccountDao {
    @Query("SELECT password FROM accounts WHERE username = :username")
    public @Nullable String getPassword(String username);

    @Query("SELECT salt FROM accounts WHERE username = :username")
    public @Nullable byte [] getSalt(String username);

    @Query("SELECT goal_weight FROM accounts WHERE username = :username")
    public float getGoalWeight(String username);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertAccount(Account account);

    @Query("UPDATE accounts SET goal_weight = :goalWeight WHERE username = :username")
    public void setGoalWeight(float goalWeight, String username);
}
