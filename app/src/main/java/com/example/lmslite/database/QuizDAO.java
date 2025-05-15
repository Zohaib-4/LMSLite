package com.example.lmslite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuizDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public QuizDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertQuiz(String quizId, String moduleId, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUIZ_ID, quizId);
        values.put(DatabaseHelper.COLUMN_MODULE_ID, moduleId);
        values.put(DatabaseHelper.COLUMN_QUIZ_TITLE, title);
        values.put(DatabaseHelper.COLUMN_QUIZ_DESCRIPTION, description);

        return database.insert(DatabaseHelper.TABLE_QUIZZES, null, values);
    }

    public Cursor getQuiz(String quizId) {
        return database.query(
                DatabaseHelper.TABLE_QUIZZES,
                null,
                DatabaseHelper.COLUMN_QUIZ_ID + " = ?",
                new String[]{quizId},
                null,
                null,
                null
        );
    }

    public Cursor getModuleQuizzes(String moduleId) {
        return database.query(
                DatabaseHelper.TABLE_QUIZZES,
                null,
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId},
                null,
                null,
                DatabaseHelper.COLUMN_CREATED_AT + " ASC"
        );
    }

    public int updateQuiz(String quizId, String title, String description) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUIZ_TITLE, title);
        values.put(DatabaseHelper.COLUMN_QUIZ_DESCRIPTION, description);

        return database.update(
                DatabaseHelper.TABLE_QUIZZES,
                values,
                DatabaseHelper.COLUMN_QUIZ_ID + " = ?",
                new String[]{quizId}
        );
    }

    public int deleteQuiz(String quizId) {
        // First delete all questions
        database.delete(
                DatabaseHelper.TABLE_QUESTIONS,
                DatabaseHelper.COLUMN_QUIZ_ID + " = ?",
                new String[]{quizId}
        );

        // Then delete the quiz
        return database.delete(
                DatabaseHelper.TABLE_QUIZZES,
                DatabaseHelper.COLUMN_QUIZ_ID + " = ?",
                new String[]{quizId}
        );
    }

    public long insertQuestion(String questionId, String quizId, String questionText,
                             List<String> options, String correctAnswer) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUESTION_ID, questionId);
        values.put(DatabaseHelper.COLUMN_QUIZ_ID, quizId);
        values.put(DatabaseHelper.COLUMN_QUESTION_TEXT, questionText);
        values.put(DatabaseHelper.COLUMN_OPTIONS, new JSONArray(options).toString());
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWER, correctAnswer);

        return database.insert(DatabaseHelper.TABLE_QUESTIONS, null, values);
    }

    public Cursor getQuizQuestions(String quizId) {
        return database.query(
                DatabaseHelper.TABLE_QUESTIONS,
                null,
                DatabaseHelper.COLUMN_QUIZ_ID + " = ?",
                new String[]{quizId},
                null,
                null,
                DatabaseHelper.COLUMN_CREATED_AT + " ASC"
        );
    }

    public int updateQuestion(String questionId, String questionText,
                            List<String> options, String correctAnswer) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUESTION_TEXT, questionText);
        values.put(DatabaseHelper.COLUMN_OPTIONS, new JSONArray(options).toString());
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWER, correctAnswer);

        return database.update(
                DatabaseHelper.TABLE_QUESTIONS,
                values,
                DatabaseHelper.COLUMN_QUESTION_ID + " = ?",
                new String[]{questionId}
        );
    }

    public int deleteQuestion(String questionId) {
        return database.delete(
                DatabaseHelper.TABLE_QUESTIONS,
                DatabaseHelper.COLUMN_QUESTION_ID + " = ?",
                new String[]{questionId}
        );
    }

    public int getQuizQuestionCount(String quizId) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_QUESTIONS,
                new String[]{"COUNT(*) as count"},
                DatabaseHelper.COLUMN_QUIZ_ID + " = ?",
                new String[]{quizId},
                null,
                null,
                null
        );

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<String> getQuestionOptions(String optionsJson) {
        List<String> options = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(optionsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                options.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return options;
    }
} 