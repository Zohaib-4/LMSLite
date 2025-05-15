package com.example.lmslite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ModuleDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public ModuleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertModule(String moduleId, String courseId, String title, String description, int order) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MODULE_ID, moduleId);
        values.put(DatabaseHelper.COLUMN_COURSE_ID, courseId);
        values.put(DatabaseHelper.COLUMN_MODULE_TITLE, title);
        values.put(DatabaseHelper.COLUMN_MODULE_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_MODULE_ORDER, order);

        return database.insert(DatabaseHelper.TABLE_MODULES, null, values);
    }

    public Cursor getModule(String moduleId) {
        return database.query(
                DatabaseHelper.TABLE_MODULES,
                null,
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId},
                null,
                null,
                null
        );
    }

    public Cursor getCourseModules(String courseId) {
        return database.query(
                DatabaseHelper.TABLE_MODULES,
                null,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId},
                null,
                null,
                DatabaseHelper.COLUMN_MODULE_ORDER + " ASC"
        );
    }

    public int updateModule(String moduleId, String title, String description, int order) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MODULE_TITLE, title);
        values.put(DatabaseHelper.COLUMN_MODULE_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_MODULE_ORDER, order);

        return database.update(
                DatabaseHelper.TABLE_MODULES,
                values,
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId}
        );
    }

    public int deleteModule(String moduleId) {
        // First delete all related videos and quizzes
        database.delete(
                DatabaseHelper.TABLE_VIDEOS,
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId}
        );

        database.delete(
                DatabaseHelper.TABLE_QUIZZES,
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId}
        );

        // Then delete the module
        return database.delete(
                DatabaseHelper.TABLE_MODULES,
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId}
        );
    }

    public int getNextModuleOrder(String courseId) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_MODULES,
                new String[]{"MAX(" + DatabaseHelper.COLUMN_MODULE_ORDER + ") as max_order"},
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId},
                null,
                null,
                null
        );

        int nextOrder = 1;
        if (cursor.moveToFirst()) {
            nextOrder = cursor.getInt(0) + 1;
        }
        cursor.close();
        return nextOrder;
    }

    public void reorderModules(String courseId, List<String> moduleIds) {
        database.beginTransaction();
        try {
            for (int i = 0; i < moduleIds.size(); i++) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_MODULE_ORDER, i + 1);

                database.update(
                        DatabaseHelper.TABLE_MODULES,
                        values,
                        DatabaseHelper.COLUMN_MODULE_ID + " = ? AND " + DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                        new String[]{moduleIds.get(i), courseId}
                );
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
} 