package com.example.cs_360fitnessapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface WeightDao {
    @Query("SELECT * from weights WHERE username = :username ORDER BY datetime DESC")
    public List<Weight> getWeights(String username);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertWeight(Weight weight);

    @Update
    public void updateWeight(Weight weight);

    @Delete
    void deleteWeight(Weight weight);
}
