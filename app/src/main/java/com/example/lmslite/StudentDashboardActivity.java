package com.example.lmslite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lmslite.database.CourseDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class StudentDashboardActivity extends AppCompatActivity {
    private static final int REQUEST_COURSE_DETAILS = 1;
    
    private TextView welcomeText;
    private TextView tvCurrentCourse;
    private TextView tvProgress;
    private TextView tvPercentage;
    private ProgressBar progressBar;
    private Button btnContinueLearning;
    private RecyclerView coursesRecyclerView;
    private RecyclerView recommendedCoursesRecyclerView;
    private TabLayout tabLayout;
    private CourseDAO courseDAO;
    private String studentId;
    private ImageView ivNotifications;
    private ImageView ivProfile;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Get student ID from intent
        studentId = getIntent().getStringExtra("userId");
        String studentName = getIntent().getStringExtra("userName");

        // Initialize views
        initializeViews();
        
        // Set welcome message
        welcomeText.setText("Welcome back, " + studentName + "!");

        // Initialize database
        courseDAO = new CourseDAO(this);
        courseDAO.open();

        // Setup RecyclerViews
        setupRecyclerViews();

        // Setup click listeners
        setupClickListeners();

        // Load initial data
        loadDashboardData();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcomeText);
        tvCurrentCourse = findViewById(R.id.tvCurrentCourse);
        tvProgress = findViewById(R.id.tvProgress);
        tvPercentage = findViewById(R.id.tvPercentage);
        progressBar = findViewById(R.id.progressBar);
        btnContinueLearning = findViewById(R.id.btnContinueLearning);
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        recommendedCoursesRecyclerView = findViewById(R.id.recommendedCoursesRecyclerView);
        ivNotifications = findViewById(R.id.ivNotifications);
        ivProfile = findViewById(R.id.ivProfile);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void setupRecyclerViews() {
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendedCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set adapters for both RecyclerViews
    }

    private void setupClickListeners() {
        btnContinueLearning.setOnClickListener(v -> {
            // TODO: Navigate to the current course's next lesson
            Toast.makeText(this, "Continue learning feature coming soon", Toast.LENGTH_SHORT).show();
        });

        ivNotifications.setOnClickListener(v -> {
            // TODO: Show notifications
            Toast.makeText(this, "Notifications feature coming soon", Toast.LENGTH_SHORT).show();
        });

        ivProfile.setOnClickListener(v -> {
            // TODO: Show profile options
            Toast.makeText(this, "Profile feature coming soon", Toast.LENGTH_SHORT).show();
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.navigation_home) {
                // Navigate to Home
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("userId", studentId);
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                // Already on dashboard, do nothing
                return true;
            } else if (itemId == R.id.navigation_profile) {
                // Navigate to Profile
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("userId", studentId);
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(intent);
                return true;
            }
            
            return false;
        });

        // Set the dashboard item as selected by default
        bottomNavigation.setSelectedItemId(R.id.navigation_dashboard);
    }

    private void loadDashboardData() {
        // Load current course progress
        Cursor currentCourseCursor = courseDAO.getCurrentCourse(studentId);
        if (currentCourseCursor != null && currentCourseCursor.moveToFirst()) {
            String courseName = currentCourseCursor.getString(currentCourseCursor.getColumnIndex("course_name"));
            int completedLessons = currentCourseCursor.getInt(currentCourseCursor.getColumnIndex("completed_lessons"));
            int totalLessons = currentCourseCursor.getInt(currentCourseCursor.getColumnIndex("total_lessons"));
            
            tvCurrentCourse.setText(courseName);
            tvProgress.setText(completedLessons + " of " + totalLessons + " lessons completed");
            
            int percentage = (completedLessons * 100) / totalLessons;
            tvPercentage.setText(percentage + "%");
            progressBar.setProgress(percentage);
            
            currentCourseCursor.close();
        }

        // Load enrolled courses
        Cursor enrolledCoursesCursor = courseDAO.getStudentEnrolledCourses(studentId);
        // TODO: Set adapter with enrolled courses data
        if (enrolledCoursesCursor != null) {
            enrolledCoursesCursor.close();
        }

        // Load recommended courses
        Cursor recommendedCoursesCursor = courseDAO.getRecommendedCourses(studentId);
        // TODO: Set adapter with recommended courses data
        if (recommendedCoursesCursor != null) {
            recommendedCoursesCursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_COURSE_DETAILS && resultCode == RESULT_OK) {
            // Refresh the dashboard data
            loadDashboardData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.action_profile) {
            // TODO: Implement profile view/edit
            Toast.makeText(this, "Profile feature coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_progress) {
            // TODO: Implement progress tracking
            Toast.makeText(this, "Progress tracking coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_settings) {
            // TODO: Implement settings
            Toast.makeText(this, "Settings feature coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_logout) {
            // Clear user session and return to login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (courseDAO != null) {
            courseDAO.close();
        }
    }
} 