package com.todoapplication.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.todoapplication.models.TaskBlock;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todolist.db";
    public static final String TABLE_NAME = "todolist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "Task";
    public static final String COL3 = "Status";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " BOOL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(TaskBlock taskBlock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, taskBlock.getTask());
        contentValues.put(COL3, 0);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public boolean update(TaskBlock taskBlock, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL3, status);

        db.update(TABLE_NAME, contentValues, COL2 + "= ?", new String[]{taskBlock.getTask()});
        return true;
    }

    public boolean delete(TaskBlock taskBlock){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, taskBlock.getTask());
        contentValues.put(COL3, taskBlock.getStatus());

        db.delete(TABLE_NAME, COL2 + "= ?", new String[]{taskBlock.getTask()});
        return true;
    }
}
