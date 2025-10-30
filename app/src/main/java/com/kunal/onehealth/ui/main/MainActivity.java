package com.kunal.onehealth.ui.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kunal.onehealth.R;
import com.kunal.onehealth.data.model.Medicine;
import com.kunal.onehealth.data.repository.MedicineRepository;
import com.kunal.onehealth.ui.medicine.MedicineActivity;

import java.util.List;
import android.content.Intent;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNav, navController);


        MedicineRepository repo = new MedicineRepository(this);
        repo.insert(new Medicine("Paracetamol", "500mg", "10:00 AM"));

        List<Medicine> list = repo.getAll();
        Log.d("DB_TEST", "Medicines count: " + list.size());

        Button btnOpenMedicine = findViewById(R.id.btnOpenMedicine);
        btnOpenMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MedicineActivity.class);
            startActivity(intent);
        });



    }
}
