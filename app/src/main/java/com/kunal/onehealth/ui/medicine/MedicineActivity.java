package com.kunal.onehealth.ui.medicine;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.kunal.onehealth.R;
import com.kunal.onehealth.data.model.Medicine;
import com.kunal.onehealth.data.repository.MedicineRepository;
import java.util.List;
import java.util.ArrayList;
import android.widget.ArrayAdapter;

public class MedicineActivity extends AppCompatActivity {

    private EditText etName, etDosage, etTime;
    private Button btnAdd;
    private ListView listView;
    private MedicineRepository repo;
    private ArrayAdapter<String> adapter;
    private List<String> medicineNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        etName = findViewById(R.id.etName);
        etDosage = findViewById(R.id.etDosage);
        etTime = findViewById(R.id.etTime);
        btnAdd = findViewById(R.id.btnAdd);
        listView = findViewById(R.id.listView);

        repo = new MedicineRepository(this);
        loadMedicines();

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String dosage = etDosage.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (name.isEmpty() || dosage.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            repo.insert(new Medicine(name, dosage, time));
            Toast.makeText(this, "Medicine Added ✅", Toast.LENGTH_SHORT).show();

            etName.setText("");
            etDosage.setText("");
            etTime.setText("");

            loadMedicines();
        });
    }

    private void loadMedicines() {
        List<Medicine> list = repo.getAll();
        medicineNames.clear();
        for (Medicine m : list) {
            medicineNames.add(m.getName() + " (" + m.getDosage() + ") at " + m.getTime());
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicineNames);
        listView.setAdapter(adapter);
    }
}
