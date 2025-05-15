package com.example.lmslite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CreateCourseActivity extends AppCompatActivity {

    private TextInputEditText etCourseTitle, etCourseDescription, etCourseDuration;
    private MaterialButton btnAddModule, btnSaveCourse;
    private RecyclerView rvModules;
    private ImageView ivBack;
    private ModuleAdapter moduleAdapter;
    private List<Module> moduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        etCourseTitle = findViewById(R.id.etCourseTitle);
        etCourseDescription = findViewById(R.id.etCourseDescription);
        etCourseDuration = findViewById(R.id.etCourseDuration);
        btnAddModule = findViewById(R.id.btnAddModule);
        btnSaveCourse = findViewById(R.id.btnSaveCourse);
        rvModules = findViewById(R.id.rvModules);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setupRecyclerView() {
        moduleList = new ArrayList<>();
        moduleAdapter = new ModuleAdapter(moduleList);
        rvModules.setLayoutManager(new LinearLayoutManager(this));
        rvModules.setAdapter(moduleAdapter);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnAddModule.setOnClickListener(v -> {
            Intent intent = new Intent(CreateCourseActivity.this, AddModuleActivity.class);
            startActivityForResult(intent, 100);
        });

        btnSaveCourse.setOnClickListener(v -> saveCourse());
    }

    private void saveCourse() {
        String title = etCourseTitle.getText().toString().trim();
        String description = etCourseDescription.getText().toString().trim();
        String duration = etCourseDuration.getText().toString().trim();

        if (title.isEmpty()) {
            etCourseTitle.setError("Please enter course title");
            return;
        }

        if (description.isEmpty()) {
            etCourseDescription.setError("Please enter course description");
            return;
        }

        if (duration.isEmpty()) {
            etCourseDuration.setError("Please enter course duration");
            return;
        }

        if (moduleList.isEmpty()) {
            Toast.makeText(this, "Please add at least one module", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Save course to database
        Course course = new Course(title, description, Integer.parseInt(duration), moduleList);
        // Save course logic here

        Toast.makeText(this, "Course saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Module module = (Module) data.getSerializableExtra("module");
            if (module != null) {
                moduleList.add(module);
                moduleAdapter.notifyItemInserted(moduleList.size() - 1);
            }
        }
    }
} 