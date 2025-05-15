package com.example.lmslite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.lmslite.database.UserDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    
    private CircleImageView profileImage;
    private TextView btnChangePhoto;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private Button btnChangePassword;
    private SwitchMaterial switchDarkMode;
    private Button btnSaveChanges;
    private Button btnLogout;
    private BottomNavigationView bottomNavigation;
    
    private UserDAO userDAO;
    private String userId;
    private String userName;
    private Uri selectedImageUri;
    private SharedPreferences sharedPreferences;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                selectedImageUri = result.getData().getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    profileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get user information from intent
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Initialize views
        initializeViews();

        // Initialize database
        userDAO = new UserDAO(this);
        userDAO.open();

        // Load user data
        loadUserData();

        // Setup click listeners
        setupClickListeners();

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profileImage);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnLogout = findViewById(R.id.btnLogout);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void loadUserData() {
        // Load user data from database
        String[] userData = userDAO.getUserData(userId);
        if (userData != null) {
            etName.setText(userData[0]); // Name
            etEmail.setText(userData[1]); // Email
        }

        // Load dark mode preference
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);
    }

    private void setupClickListeners() {
        btnChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnChangePassword.setOnClickListener(v -> {
            // TODO: Show change password dialog
            Toast.makeText(this, "Change password feature coming soon", Toast.LENGTH_SHORT).show();
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save dark mode preference
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();

            // Apply dark mode
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        btnSaveChanges.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update user data in database
            boolean success = userDAO.updateUserData(userId, newName, newEmail);
            if (success) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(v -> {
            // Clear user session
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Navigate to login screen
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.navigation_home) {
                // Navigate to Home
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                // Navigate to appropriate dashboard based on user role
                Intent intent;
                if (userDAO.isTeacher(userId)) {
                    intent = new Intent(this, TeacherDashboardActivity.class);
                } else {
                    intent = new Intent(this, StudentDashboardActivity.class);
                }
                intent.putExtra("userId", userId);
                intent.putExtra("userName", userName);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // Already on profile, do nothing
                return true;
            }
            
            return false;
        });

        // Set the profile item as selected
        bottomNavigation.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDAO != null) {
            userDAO.close();
        }
    }
} 