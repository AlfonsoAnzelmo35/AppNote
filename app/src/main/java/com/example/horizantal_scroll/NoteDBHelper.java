package com.example.horizantal_scroll;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDBHelper  extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NoteDB.db";

    final static String[] columns = {
            SchemaNote.Note._ID,
            SchemaNote.Note.COLUMN_NAME_TITLE,
            SchemaNote.Note.COLUMN_NAME_TEXT,
            SchemaNote.Note.COLUMN_NAME_IMAGE_PATH
    };

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SchemaNote.Note.TABLE_NAME + " (" +
                    SchemaNote.Note._ID + " INTEGER PRIMARY KEY," +
                    SchemaNote.Note.COLUMN_NAME_TITLE + " TEXT, " +
                    SchemaNote.Note.COLUMN_NAME_TEXT + " TEXT, "  +
                    SchemaNote.Note.COLUMN_NAME_IMAGE_PATH + " TEXT) ";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SchemaNote.Note.TABLE_NAME;

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}