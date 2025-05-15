package com.example.lmslite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class CourseDetailsActivity extends AppCompatActivity {

    private TextView tvCourseTitle, tvCourseDescription, tvDuration, tvModules;
    private ImageView ivCourseImage, ivBack;
    private RecyclerView rvModules;
    private MaterialButton btnEnroll;
    private Course course;
    private boolean isStudent;
    private boolean isEnrolled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get course data from intent
        course = (Course) getIntent().getSerializableExtra("course");
        isStudent = getIntent().getBooleanExtra("isStudent", false);
        isEnrolled = getIntent().getBooleanExtra("isEnrolled", false);

        if (course == null) {
            Toast.makeText(this, "Error loading course", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        displayCourseDetails();
    }

    private void initializeViews() {
        tvCourseTitle = findViewById(R.id.tvCourseTitle);
        tvCourseDescription = findViewById(R.id.tvCourseDescription);
        tvDuration = findViewById(R.id.tvDuration);
        tvModules = findViewById(R.id.tvModules);
        ivCourseImage = findViewById(R.id.ivCourseImage);
        ivBack = findViewById(R.id.ivBack);
        rvModules = findViewById(R.id.rvModules);
        btnEnroll = findViewById(R.id.btnEnroll);
    }

    private void setupRecyclerView() {
        ModuleAdapter moduleAdapter = new ModuleAdapter(course.getModules());
        rvModules.setLayoutManager(new LinearLayoutManager(this));
        rvModules.setAdapter(moduleAdapter);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnEnroll.setOnClickListener(v -> enrollInCourse());
    }

    private void displayCourseDetails() {
        tvCourseTitle.setText(course.getTitle());
        tvCourseDescription.setText(course.getDescription());
        tvDuration.setText("Duration: " + course.getDuration() + " hours");
        tvModules.setText("Modules: " + course.getModules().size());

        // Show enroll button only for students who haven't enrolled
        if (isStudent && !isEnrolled) {
            btnEnroll.setVisibility(View.VISIBLE);
        } else {
            btnEnroll.setVisibility(View.GONE);
        }
    }

    private void enrollInCourse() {
        // TODO: Implement actual enrollment logic with your backend
        // For now, we'll just show a success message
        Toast.makeText(this, "Successfully enrolled in course!", Toast.LENGTH_SHORT).show();
        
        // Update enrollment status
        isEnrolled = true;
        btnEnroll.setVisibility(View.GONE);
        
        // Notify the dashboard to refresh the enrolled courses
        Intent resultIntent = new Intent();
        resultIntent.putExtra("courseId", course.getId());
        setResult(RESULT_OK, resultIntent);
    }
} 