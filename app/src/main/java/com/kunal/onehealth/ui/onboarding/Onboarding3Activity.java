package com.kunal.onehealth.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.kunal.onehealth.R;
import com.kunal.onehealth.auth.LoginActivity;
import android.widget.Button;

public class Onboarding3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_screen3);

        Button btnBack3 = findViewById(R.id.btnBack3);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        btnBack3.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }
}
