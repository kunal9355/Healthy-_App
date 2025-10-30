package com.kunal.onehealth.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TestEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
}
