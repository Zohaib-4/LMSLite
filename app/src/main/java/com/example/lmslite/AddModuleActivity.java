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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddModuleActivity extends AppCompatActivity {

    private TextInputEditText etModuleTitle, etModuleDescription;
    private MaterialButton btnAddVideo, btnAddQuiz, btnSaveModule;
    private RecyclerView rvContent;
    private ImageView ivBack;
    private ContentAdapter contentAdapter;
    private List<ModuleContent> contentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_module);
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
        etModuleTitle = findViewById(R.id.etModuleTitle);
        etModuleDescription = findViewById(R.id.etModuleDescription);
        btnAddVideo = findViewById(R.id.btnAddVideo);
        btnAddQuiz = findViewById(R.id.btnAddQuiz);
        btnSaveModule = findViewById(R.id.btnSaveModule);
        rvContent = findViewById(R.id.rvContent);
        ivBack = findViewById(R.id.ivBack);
    }

    private void setupRecyclerView() {
        contentList = new ArrayList<>();
        contentAdapter = new ContentAdapter(contentList);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(contentAdapter);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        btnAddVideo.setOnClickListener(v -> {
            Intent intent = new Intent(AddModuleActivity.this, AddVideoActivity.class);
            startActivityForResult(intent, 101);
        });

        btnAddQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(AddModuleActivity.this, AddQuizActivity.class);
            startActivityForResult(intent, 102);
        });

        btnSaveModule.setOnClickListener(v -> saveModule());
    }

    private void saveModule() {
        String title = etModuleTitle.getText().toString().trim();
        String description = etModuleDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etModuleTitle.setError("Please enter module title");
            return;
        }

        if (description.isEmpty()) {
            etModuleDescription.setError("Please enter module description");
            return;
        }

        if (contentList.isEmpty()) {
            Toast.makeText(this, "Please add at least one video or quiz", Toast.LENGTH_SHORT).show();
            return;
        }

        Module module = new Module(title, description, contentList);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("module", module);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 101) {
                Video video = (Video) data.getSerializableExtra("video");
                if (video != null) {
                    contentList.add(new ModuleContent(ModuleContent.TYPE_VIDEO, video));
                    contentAdapter.notifyItemInserted(contentList.size() - 1);
                }
            } else if (requestCode == 102) {
                Quiz quiz = (Quiz) data.getSerializableExtra("quiz");
                if (quiz != null) {
                    contentList.add(new ModuleContent(ModuleContent.TYPE_QUIZ, quiz));
                    contentAdapter.notifyItemInserted(contentList.size() - 1);
                }
            }
        }
    }
} 