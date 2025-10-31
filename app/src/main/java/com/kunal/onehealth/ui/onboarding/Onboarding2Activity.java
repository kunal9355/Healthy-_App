package com.kunal.onehealth.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.kunal.onehealth.R;
import android.widget.Button;

public class Onboarding2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_screen2);

        Button btnNext2 = findViewById(R.id.btnNext2);
        Button btnBack2 = findViewById(R.id.btnBack2);

        btnNext2.setOnClickListener(v -> {
            Intent intent = new Intent(this, Onboarding3Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnBack2.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}
