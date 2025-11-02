package com.kunal.onehealth.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.kunal.onehealth.R;
import com.kunal.onehealth.ui.home.HomeFragment;
import com.kunal.onehealth.ui.main.HomeDashboardActivity;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email, password;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 🔹 Step 1: Initialize FirebaseAuth FIRST
        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            // User already logged in → go directly to MainActivity
            startActivity(new Intent(LoginActivity.this, HomeDashboardActivity.class));
            finish();
            return;
        }

        // 🔹 Step 3: Initialize views after Firebase check



        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        TextView tvSignupLink = findViewById(R.id.tvSignupLink);



        // 🔹 Step 4: Button listeners
        btnLogin.setOnClickListener(v -> {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();


            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }


            auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Login Successful ✅", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, HomeFragment.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Login Failed ❌", Toast.LENGTH_SHORT).show();
                        }

                    });
        });

        // 🔹 Step 5: Signup navigation

        tvSignupLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }
}
