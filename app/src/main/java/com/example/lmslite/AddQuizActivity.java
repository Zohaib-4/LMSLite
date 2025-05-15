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

public class AddQuizActivity extends AppCompatActivity {

    private TextInputEditText etQuizTitle, etQuizDescription;
    private MaterialButton btnAddQuestion, btnSaveQuiz;
    private ImageView ivBack;
    private RecyclerView rvQuestions;
    private QuestionAdapter questionAdapter;
    private List<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_quiz);
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
        etQuizTitle = findViewById(R.id.etQuizTitle);
        etQuizDescription = findViewById(R.id.etQuizDescription);
        btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnSaveQuiz = findViewById(R.id.btnSaveQuiz);
        ivBack = findViewById(R.id.ivBack);
        rvQuestions = findViewById(R.id.rvQuestions);
    }

    private void setupRecyclerView() {
        questions = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questions);
        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(questionAdapter);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());
        btnAddQuestion.setOnClickListener(v -> addQuestion());
        btnSaveQuiz.setOnClickListener(v -> saveQuiz());
    }

    private void addQuestion() {
        // Show dialog to add question
        QuestionDialog dialog = new QuestionDialog();
        dialog.setOnQuestionAddedListener(question -> {
            questions.add(question);
            questionAdapter.notifyItemInserted(questions.size() - 1);
        });
        dialog.show(getSupportFragmentManager(), "QuestionDialog");
    }

    private void saveQuiz() {
        String title = etQuizTitle.getText().toString().trim();
        String description = etQuizDescription.getText().toString().trim();

        if (title.isEmpty()) {
            etQuizTitle.setError("Please enter quiz title");
            return;
        }

        if (description.isEmpty()) {
            etQuizDescription.setError("Please enter quiz description");
            return;
        }

        if (questions.isEmpty()) {
            Toast.makeText(this, "Please add at least one question", Toast.LENGTH_SHORT).show();
            return;
        }

        Quiz quiz = new Quiz(title, description, questions);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("quiz", quiz);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
} 