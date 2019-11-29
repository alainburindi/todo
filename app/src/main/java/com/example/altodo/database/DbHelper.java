package com.example.altodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todo";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IS_DONE = "done";


    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT, "+ KEY_DATE + " DATE, "
                + KEY_TIME + " TIME, "+ KEY_DESCRIPTION + " TEXT, "
                + KEY_IS_DONE + " INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TABLE_TASKS);
        onCreate(db);
    }

    public void addTask(String name, String date, String time, String description){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("date", date);
        contentValues.put("description", description);
        contentValues.put("time", time);
        db.insert(TABLE_TASKS, null, contentValues);
    }

    public Cursor getTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.query(TABLE_TASKS, null, KEY_ID,
                new String[]{String.valueOf(id)}, null,
                null, null, null);
    }

    public ArrayList<HashMap<String, String>> getAllTasks(){
        ArrayList<HashMap<String, String>> tasks_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+ TABLE_TASKS, null);
        result.moveToFirst();
        while (!result.isAfterLast()){
            HashMap<String, String> task = new HashMap<>();
            task.put("name", result.getString(result.getColumnIndex(KEY_NAME)));
            task.put("date", result.getString(result.getColumnIndex(KEY_DATE)));
            task.put("description", result.getString(result.getColumnIndex(KEY_DESCRIPTION)));
            task.put("time", result.getString(result.getColumnIndex(KEY_TIME)));
            tasks_list.add(task);
            result.moveToNext();
        }
        result.close();
        return tasks_list;
    }

    int updateTask(int id, String name, String date, String time, String description){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("name", name);
        contentValues.put("description", description);
        return db.update(TABLE_TASKS, contentValues, "id = ? ",
                new String[]{String.valueOf(id)});
    }

    void deleteTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_TASKS, KEY_ID+" =? ", new String[]{String.valueOf(id)});
    }

    int markAsDone(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("done", 1);

        return db.update(TABLE_TASKS, contentValues, "id = ? ",
                new String[]{String.valueOf(id)});
    }
}
