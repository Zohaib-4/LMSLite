package com.example.lmslite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public CourseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertCourse(String courseId, String title, String description, String imageUrl,
                           int duration, String teacherId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COURSE_ID, courseId);
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, imageUrl);
        values.put(DatabaseHelper.COLUMN_DURATION, duration);
        values.put(DatabaseHelper.COLUMN_TEACHER_ID, teacherId);

        return database.insert(DatabaseHelper.TABLE_COURSES, null, values);
    }

    public Cursor getCourse(String courseId) {
        return database.query(
                DatabaseHelper.TABLE_COURSES,
                null,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId},
                null,
                null,
                null
        );
    }

    public Cursor getTeacherCourses(String teacherId) {
        return database.query(
                DatabaseHelper.TABLE_COURSES,
                null,
                DatabaseHelper.COLUMN_TEACHER_ID + " = ?",
                new String[]{teacherId},
                null,
                null,
                DatabaseHelper.COLUMN_CREATED_AT + " DESC"
        );
    }

    public Cursor getStudentEnrolledCourses(String studentId) {
        String query = "SELECT c.* FROM " + DatabaseHelper.TABLE_COURSES + " c " +
                "INNER JOIN " + DatabaseHelper.TABLE_ENROLLMENTS + " e " +
                "ON c." + DatabaseHelper.COLUMN_COURSE_ID + " = e." + DatabaseHelper.COLUMN_COURSE_ID + " " +
                "WHERE e." + DatabaseHelper.COLUMN_STUDENT_ID + " = ? " +
                "ORDER BY e." + DatabaseHelper.COLUMN_CREATED_AT + " DESC";

        return database.rawQuery(query, new String[]{studentId});
    }

    public Cursor getAvailableCourses(String studentId) {
        String query = "SELECT c.* FROM " + DatabaseHelper.TABLE_COURSES + " c " +
                "WHERE c." + DatabaseHelper.COLUMN_COURSE_ID + " NOT IN " +
                "(SELECT " + DatabaseHelper.COLUMN_COURSE_ID + " FROM " + DatabaseHelper.TABLE_ENROLLMENTS + " " +
                "WHERE " + DatabaseHelper.COLUMN_STUDENT_ID + " = ?) " +
                "ORDER BY c." + DatabaseHelper.COLUMN_CREATED_AT + " DESC";

        return database.rawQuery(query, new String[]{studentId});
    }

    public int updateCourse(String courseId, String title, String description, String imageUrl,
                          int duration) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, title);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, imageUrl);
        values.put(DatabaseHelper.COLUMN_DURATION, duration);

        return database.update(
                DatabaseHelper.TABLE_COURSES,
                values,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId}
        );
    }

    public int deleteCourse(String courseId) {
        // First delete all related records
        database.delete(
                DatabaseHelper.TABLE_ENROLLMENTS,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId}
        );

        // Then delete the course
        return database.delete(
                DatabaseHelper.TABLE_COURSES,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId}
        );
    }

    public boolean isEnrolled(String studentId, String courseId) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ENROLLMENTS,
                null,
                DatabaseHelper.COLUMN_STUDENT_ID + " = ? AND " + DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{studentId, courseId},
                null,
                null,
                null
        );
        boolean isEnrolled = cursor.getCount() > 0;
        cursor.close();
        return isEnrolled;
    }

    public long enrollStudent(String studentId, String courseId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_ID, studentId);
        values.put(DatabaseHelper.COLUMN_COURSE_ID, courseId);

        return database.insert(DatabaseHelper.TABLE_ENROLLMENTS, null, values);
    }

    public int unenrollStudent(String studentId, String courseId) {
        return database.delete(
                DatabaseHelper.TABLE_ENROLLMENTS,
                DatabaseHelper.COLUMN_STUDENT_ID + " = ? AND " + DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{studentId, courseId}
        );
    }
} 