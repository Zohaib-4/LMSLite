package com.example.lmslite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questions;

    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.tvQuestionText.setText(question.getQuestionText());
        
        StringBuilder optionsText = new StringBuilder();
        List<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            optionsText.append(i + 1).append(". ").append(options.get(i)).append("\n");
        }
        holder.tvOptions.setText(optionsText.toString());
        
        holder.tvCorrectAnswer.setText("Correct Answer: " + (question.getCorrectOptionIndex() + 1));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionText, tvOptions, tvCorrectAnswer;

        QuestionViewHolder(View itemView) {
            super(itemView);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            tvOptions = itemView.findViewById(R.id.tvOptions);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
        }
    }
} 