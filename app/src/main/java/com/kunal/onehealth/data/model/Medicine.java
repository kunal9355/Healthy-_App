package com.kunal.onehealth.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine_table")
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String dosage;
    private String time;

    public Medicine(String name, String dosage, String time) {
        this.name = name;
        this.dosage = dosage;
        this.time = time;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public String getDosage() { return dosage; }
    public String getTime() { return time; }
}
