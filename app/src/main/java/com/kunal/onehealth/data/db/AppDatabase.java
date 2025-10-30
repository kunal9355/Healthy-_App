package com.kunal.onehealth.data.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;


// Step 1: Keep it empty for now
@Database(entities = {TestEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

// Future DAOs will go here
}
