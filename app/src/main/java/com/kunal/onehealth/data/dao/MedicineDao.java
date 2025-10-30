package com.kunal.onehealth.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.kunal.onehealth.data.model.Medicine;
import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    void insert(Medicine medicine);

    @Delete
    void delete(Medicine medicine);

    @Query("SELECT * FROM medicine_table ORDER BY time ASC")
    List<Medicine> getAllMedicines();
}
