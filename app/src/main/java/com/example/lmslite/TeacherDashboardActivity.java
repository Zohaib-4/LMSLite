package com.example.lmslite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lmslite.database.CourseDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TeacherDashboardActivity extends AppCompatActivity {
    private static final int REQUEST_CREATE_COURSE = 1;
    
    private TextView welcomeText;
    private TextView tvTotalCourses;
    private TextView tvTotalStudents;
    private TextView tvCourseCount;
    private Button btnCreateCourse;
    private RecyclerView coursesRecyclerView;
    private RecyclerView activityRecyclerView;
    private CourseDAO courseDAO;
    private String teacherId;
    private ImageView ivNotifications;
    private ImageView ivProfile;
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton createCourseFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Get teacher ID from intent
        teacherId = getIntent().getStringExtra("userId");
        String teacherName = getIntent().getStringExtra("userName");

        // Initialize views
        initializeViews();
        
        // Set welcome message
        welcomeText.setText("Welcome back, " + teacherName + "!");

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
        tvTotalCourses = findViewById(R.id.tvTotalCourses);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvCourseCount = findViewById(R.id.tvCourseCount);
        btnCreateCourse = findViewById(R.id.btnCreateCourse);
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        activityRecyclerView = findViewById(R.id.activityRecyclerView);
        ivNotifications = findViewById(R.id.ivNotifications);
        ivProfile = findViewById(R.id.ivProfile);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        createCourseFab = findViewById(R.id.createCourseFab);
    }

    private void setupRecyclerViews() {
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Set adapters for both RecyclerViews
    }

    private void setupClickListeners() {
        btnCreateCourse.setOnClickListener(v -> {
            startCreateCourseActivity();
        });

        createCourseFab.setOnClickListener(v -> {
            startCreateCourseActivity();
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
                intent.putExtra("userId", teacherId);
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
                intent.putExtra("userId", teacherId);
                intent.putExtra("userName", getIntent().getStringExtra("userName"));
                startActivity(intent);
                return true;
            }
            
            return false;
        });

        // Set the dashboard item as selected by default
        bottomNavigation.setSelectedItemId(R.id.navigation_dashboard);
    }

    private void startCreateCourseActivity() {
        Intent intent = new Intent(this, CreateCourseActivity.class);
        intent.putExtra("teacherId", teacherId);
        startActivityForResult(intent, REQUEST_CREATE_COURSE);
    }

    private void loadDashboardData() {
        // Load course statistics
        Cursor statsCursor = courseDAO.getTeacherCourseStats(teacherId);
        if (statsCursor != null && statsCursor.moveToFirst()) {
            int totalCourses = statsCursor.getInt(statsCursor.getColumnIndex("total_courses"));
            int totalStudents = statsCursor.getInt(statsCursor.getColumnIndex("total_students"));
            
            tvCourseCount.setText(String.valueOf(totalCourses));
            tvTotalStudents.setText(totalStudents + " students enrolled");
            
            statsCursor.close();
        }

        // Load teacher's courses
        Cursor coursesCursor = courseDAO.getTeacherCourses(teacherId);
        // TODO: Set adapter with courses data
        if (coursesCursor != null) {
            coursesCursor.close();
        }

        // Load recent activity
        Cursor activityCursor = courseDAO.getTeacherRecentActivity(teacherId);
        // TODO: Set adapter with activity data
        if (activityCursor != null) {
            activityCursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_COURSE && resultCode == RESULT_OK) {
            // Refresh the dashboard data
            loadDashboardData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.action_profile) {
            // TODO: Implement profile view/edit
            Toast.makeText(this, "Profile feature coming soon", Toast.LENGTH_SHORT).show();
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