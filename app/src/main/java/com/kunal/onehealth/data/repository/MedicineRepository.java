package com.kunal.onehealth.data.repository;

import android.content.Context;
import androidx.room.Room;
import com.kunal.onehealth.data.dao.MedicineDao;
import com.kunal.onehealth.data.db.AppDatabase;
import com.kunal.onehealth.data.model.Medicine;
import java.util.List;

public class MedicineRepository {

    private MedicineDao dao;

    public MedicineRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "medicine_db")
                .allowMainThreadQueries() // Later: use background thread
                .build();
        dao = db.medicineDao();
    }

    public void insert(Medicine m) { dao.insert(m); }
    public void delete(Medicine m) { dao.delete(m); }
    public List<Medicine> getAll() { return dao.getAllMedicines(); }
}
