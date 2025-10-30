package com.kunal.onehealth.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.kunal.onehealth.data.dao.MedicineDao;
import com.kunal.onehealth.data.model.Medicine;

@Database(entities = {Medicine.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();
}
