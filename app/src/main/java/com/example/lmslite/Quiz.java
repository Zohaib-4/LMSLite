package com.example.lmslite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {
    private String title;
    private String description;
    private List<Question> questions;

    public Quiz(String title, String description, List<Question> questions) {
        this.title = title;
        this.description = description;
        this.questions = new ArrayList<>(questions);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = new ArrayList<>(questions);
    }
} 