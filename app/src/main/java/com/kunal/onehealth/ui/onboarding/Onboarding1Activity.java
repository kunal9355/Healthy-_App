package com.kunal.onehealth.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.kunal.onehealth.R;
import android.widget.Button;

public class Onboarding1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_screen1);

        Button btnNext1 = findViewById(R.id.btnNext1);
        btnNext1.setOnClickListener(v -> {
            Intent intent = new Intent(this, Onboarding2Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}
