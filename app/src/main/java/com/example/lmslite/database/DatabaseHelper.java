package com.example.lmslite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lmslite.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_MODULES = "modules";
    public static final String TABLE_VIDEOS = "videos";
    public static final String TABLE_QUIZZES = "quizzes";
    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_ENROLLMENTS = "enrollments";

    // Common column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Users table columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ROLE = "role";

    // Courses table columns
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_TEACHER_ID = "teacher_id";

    // Modules table columns
    public static final String COLUMN_MODULE_ID = "module_id";
    public static final String COLUMN_MODULE_TITLE = "module_title";
    public static final String COLUMN_MODULE_DESCRIPTION = "module_description";
    public static final String COLUMN_MODULE_ORDER = "module_order";

    // Videos table columns
    public static final String COLUMN_VIDEO_ID = "video_id";
    public static final String COLUMN_VIDEO_TITLE = "video_title";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_VIDEO_DURATION = "video_duration";

    // Quizzes table columns
    public static final String COLUMN_QUIZ_ID = "quiz_id";
    public static final String COLUMN_QUIZ_TITLE = "quiz_title";
    public static final String COLUMN_QUIZ_DESCRIPTION = "quiz_description";

    // Questions table columns
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_QUESTION_TEXT = "question_text";
    public static final String COLUMN_OPTIONS = "options";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";

    // Enrollments table columns
    public static final String COLUMN_STUDENT_ID = "student_id";

    // Create table statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_ID + " TEXT UNIQUE,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT UNIQUE,"
            + COLUMN_ROLE + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_COURSE_ID + " TEXT UNIQUE,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_IMAGE_URL + " TEXT,"
            + COLUMN_DURATION + " INTEGER,"
            + COLUMN_TEACHER_ID + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_TEACHER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
            + ")";

    private static final String CREATE_TABLE_MODULES = "CREATE TABLE " + TABLE_MODULES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MODULE_ID + " TEXT UNIQUE,"
            + COLUMN_COURSE_ID + " TEXT,"
            + COLUMN_MODULE_TITLE + " TEXT,"
            + COLUMN_MODULE_DESCRIPTION + " TEXT,"
            + COLUMN_MODULE_ORDER + " INTEGER,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + ")"
            + ")";

    private static final String CREATE_TABLE_VIDEOS = "CREATE TABLE " + TABLE_VIDEOS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_VIDEO_ID + " TEXT UNIQUE,"
            + COLUMN_MODULE_ID + " TEXT,"
            + COLUMN_VIDEO_TITLE + " TEXT,"
            + COLUMN_VIDEO_URL + " TEXT,"
            + COLUMN_VIDEO_DURATION + " INTEGER,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULES + "(" + COLUMN_MODULE_ID + ")"
            + ")";

    private static final String CREATE_TABLE_QUIZZES = "CREATE TABLE " + TABLE_QUIZZES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_QUIZ_ID + " TEXT UNIQUE,"
            + COLUMN_MODULE_ID + " TEXT,"
            + COLUMN_QUIZ_TITLE + " TEXT,"
            + COLUMN_QUIZ_DESCRIPTION + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_MODULE_ID + ") REFERENCES " + TABLE_MODULES + "(" + COLUMN_MODULE_ID + ")"
            + ")";

    private static final String CREATE_TABLE_QUESTIONS = "CREATE TABLE " + TABLE_QUESTIONS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_QUESTION_ID + " TEXT UNIQUE,"
            + COLUMN_QUIZ_ID + " TEXT,"
            + COLUMN_QUESTION_TEXT + " TEXT,"
            + COLUMN_OPTIONS + " TEXT,"
            + COLUMN_CORRECT_ANSWER + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_QUIZ_ID + ") REFERENCES " + TABLE_QUIZZES + "(" + COLUMN_QUIZ_ID + ")"
            + ")";

    private static final String CREATE_TABLE_ENROLLMENTS = "CREATE TABLE " + TABLE_ENROLLMENTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_STUDENT_ID + " TEXT,"
            + COLUMN_COURSE_ID + " TEXT,"
            + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
            + "FOREIGN KEY(" + COLUMN_STUDENT_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
            + "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "),"
            + "UNIQUE(" + COLUMN_STUDENT_ID + "," + COLUMN_COURSE_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_MODULES);
        db.execSQL(CREATE_TABLE_VIDEOS);
        db.execSQL(CREATE_TABLE_QUIZZES);
        db.execSQL(CREATE_TABLE_QUESTIONS);
        db.execSQL(CREATE_TABLE_ENROLLMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }
} 