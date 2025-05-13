package com.example.lmslite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private RadioGroup rgRole;
    private MaterialButton btnSignup;
    private TextView tvLogin;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        rgRole = findViewById(R.id.rgRole);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());
        
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        btnSignup.setOnClickListener(v -> signup());
    }

    private void signup() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        boolean isTeacher = rgRole.getCheckedRadioButtonId() == R.id.rbTeacher;

        if (name.isEmpty()) {
            etName.setError("Please enter your name");
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Please enter password");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        // TODO: Implement actual signup logic with Firebase or your backend
        // For now, we'll just show a success message and navigate to the appropriate screen
        Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
        
        // Navigate to appropriate screen based on role
        Intent intent = new Intent(SignupActivity.this, isTeacher ? TeacherDashboardActivity.class : StudentDashboardActivity.class);
        startActivity(intent);
        finish();
    }
}