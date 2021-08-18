package com.example.cs_360fitnessapp;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Account.class, Weight.class}, version = 1)
public abstract class FitnessAppDatabase extends RoomDatabase {
    private final static String FILE_NAME = "fitness_app_database.db";

    private static FitnessAppDatabase mFitnessDatabase;

    public static FitnessAppDatabase getInstance(Context context) {
        if (mFitnessDatabase == null) {
            mFitnessDatabase = Room.databaseBuilder(context, FitnessAppDatabase.class,
                    FILE_NAME).allowMainThreadQueries().build();
        }
        return mFitnessDatabase;
    }

    public abstract AccountDao accountDao();

    public abstract WeightDao weightDao();
}
