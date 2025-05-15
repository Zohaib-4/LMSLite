package com.example.lmslite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertUser(String userId, String name, String email, String role) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_ROLE, role);

        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public Cursor getUser(String userId) {
        return database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{userId},
                null,
                null,
                null
        );
    }

    public Cursor getUserByEmail(String email) {
        return database.query(
                DatabaseHelper.TABLE_USERS,
                null,
                DatabaseHelper.COLUMN_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );
    }

    public int updateUser(String userId, String name, String email, String role) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_ROLE, role);

        return database.update(
                DatabaseHelper.TABLE_USERS,
                values,
                DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{userId}
        );
    }

    public int deleteUser(String userId) {
        return database.delete(
                DatabaseHelper.TABLE_USERS,
                DatabaseHelper.COLUMN_USER_ID + " = ?",
                new String[]{userId}
        );
    }

    public boolean isUserExists(String userId) {
        Cursor cursor = getUser(userId);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean isEmailExists(String email) {
        Cursor cursor = getUserByEmail(email);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
} 