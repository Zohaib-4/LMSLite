package com.example.lmslite;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class QuestionDialog extends DialogFragment {

    private OnQuestionAddedListener listener;
    private EditText etQuestionText, etOption1, etOption2, etOption3, etOption4;
    private EditText etCorrectOption;

    public interface OnQuestionAddedListener {
        void onQuestionAdded(Question question);
    }

    public void setOnQuestionAddedListener(OnQuestionAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_question, null);

        initializeViews(view);
        setupClickListeners(view);

        builder.setView(view)
                .setTitle("Add Question")
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());

        return builder.create();
    }

    private void initializeViews(View view) {
        etQuestionText = view.findViewById(R.id.etQuestionText);
        etOption1 = view.findViewById(R.id.etOption1);
        etOption2 = view.findViewById(R.id.etOption2);
        etOption3 = view.findViewById(R.id.etOption3);
        etOption4 = view.findViewById(R.id.etOption4);
        etCorrectOption = view.findViewById(R.id.etCorrectOption);
    }

    private void setupClickListeners(View view) {
        MaterialButton btnAddQuestion = view.findViewById(R.id.btnAddQuestion);
        btnAddQuestion.setOnClickListener(v -> addQuestion());
    }

    private void addQuestion() {
        String questionText = etQuestionText.getText().toString().trim();
        String option1 = etOption1.getText().toString().trim();
        String option2 = etOption2.getText().toString().trim();
        String option3 = etOption3.getText().toString().trim();
        String option4 = etOption4.getText().toString().trim();
        String correctOptionStr = etCorrectOption.getText().toString().trim();

        if (questionText.isEmpty()) {
            etQuestionText.setError("Please enter question text");
            return;
        }

        if (option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all options", Toast.LENGTH_SHORT).show();
            return;
        }

        if (correctOptionStr.isEmpty()) {
            etCorrectOption.setError("Please enter correct option number");
            return;
        }

        int correctOption = Integer.parseInt(correctOptionStr);
        if (correctOption < 1 || correctOption > 4) {
            etCorrectOption.setError("Correct option must be between 1 and 4");
            return;
        }

        List<String> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);

        Question question = new Question(questionText, options, correctOption - 1);
        if (listener != null) {
            listener.onQuestionAdded(question);
        }
        dismiss();
    }
} 