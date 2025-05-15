package com.example.lmslite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddVideoActivity extends AppCompatActivity {

    private TextInputEditText etVideoTitle, etVideoDescription, etVideoUrl, etVideoDuration;
    private MaterialButton btnSaveVideo;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appBarLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etVideoTitle = findViewById(R.id.etVideoTitle);
        etVideoDescription = findViewById(R.id.etVideoDescription);
        etVideoUrl = findViewById(R.id.etVideoUrl);
        etVideoDuration = findViewById(R.id.etVideoDuration);
        btnSaveVideo = findViewById(R.id.btnSaveVideo);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());
        btnSaveVideo.setOnClickListener(v -> saveVideo());
    }

    private void saveVideo() {
        String title = etVideoTitle.getText().toString().trim();
        String description = etVideoDescription.getText().toString().trim();
        String url = etVideoUrl.getText().toString().trim();
        String duration = etVideoDuration.getText().toString().trim();

        if (title.isEmpty()) {
            etVideoTitle.setError("Please enter video title");
            return;
        }

        if (description.isEmpty()) {
            etVideoDescription.setError("Please enter video description");
            return;
        }

        if (url.isEmpty()) {
            etVideoUrl.setError("Please enter video URL");
            return;
        }

        if (duration.isEmpty()) {
            etVideoDuration.setError("Please enter video duration");
            return;
        }

        if (!isValidUrl(url)) {
            etVideoUrl.setError("Please enter a valid URL");
            return;
        }

        Video video = new Video(title, description, url, Integer.parseInt(duration));
        Intent resultIntent = new Intent();
        resultIntent.putExtra("video", video);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean isValidUrl(String url) {
        // Basic URL validation
        return url.startsWith("http://") || url.startsWith("https://");
    }
} 