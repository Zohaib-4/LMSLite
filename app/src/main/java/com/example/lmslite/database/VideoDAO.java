package com.example.lmslite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class VideoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public VideoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertVideo(String videoId, String moduleId, String title, String url, int duration) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIDEO_ID, videoId);
        values.put(DatabaseHelper.COLUMN_MODULE_ID, moduleId);
        values.put(DatabaseHelper.COLUMN_VIDEO_TITLE, title);
        values.put(DatabaseHelper.COLUMN_VIDEO_URL, url);
        values.put(DatabaseHelper.COLUMN_VIDEO_DURATION, duration);

        return database.insert(DatabaseHelper.TABLE_VIDEOS, null, values);
    }

    public Cursor getVideo(String videoId) {
        return database.query(
                DatabaseHelper.TABLE_VIDEOS,
                null,
                DatabaseHelper.COLUMN_VIDEO_ID + " = ?",
                new String[]{videoId},
                null,
                null,
                null
        );
    }

    public Cursor getModuleVideos(String moduleId) {
        return database.query(
                DatabaseHelper.TABLE_VIDEOS,
                null,
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId},
                null,
                null,
                DatabaseHelper.COLUMN_CREATED_AT + " ASC"
        );
    }

    public int updateVideo(String videoId, String title, String url, int duration) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_VIDEO_TITLE, title);
        values.put(DatabaseHelper.COLUMN_VIDEO_URL, url);
        values.put(DatabaseHelper.COLUMN_VIDEO_DURATION, duration);

        return database.update(
                DatabaseHelper.TABLE_VIDEOS,
                values,
                DatabaseHelper.COLUMN_VIDEO_ID + " = ?",
                new String[]{videoId}
        );
    }

    public int deleteVideo(String videoId) {
        return database.delete(
                DatabaseHelper.TABLE_VIDEOS,
                DatabaseHelper.COLUMN_VIDEO_ID + " = ?",
                new String[]{videoId}
        );
    }

    public int getModuleVideoCount(String moduleId) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_VIDEOS,
                new String[]{"COUNT(*) as count"},
                DatabaseHelper.COLUMN_MODULE_ID + " = ?",
                new String[]{moduleId},
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
} 