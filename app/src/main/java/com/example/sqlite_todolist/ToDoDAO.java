package com.example.sqlite_todolist;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class ToDoDAO {
    private final DbHelper dbHelper;

    public  ToDoDAO(Context context){
        dbHelper = new DbHelper(context);
    }

    public ArrayList<ToDo> getListToDo(){
        ArrayList<ToDo> list = new ArrayList<>();
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        database.beginTransaction();
        try{
            Cursor cursor = database.rawQuery("SELECT * FROM TODO", null);
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    list.add(new ToDo(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getInt(5)));
                }while (cursor.moveToNext());
                database.setTransactionSuccessful();;
            }

        }catch (Exception e){
            Log.e(TAG, "getListTodo" + e);
        }finally {
            database.endTransaction();
        }
        return list;
    }

    public boolean addToDo(ToDo todo){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        ContentValues values = new ContentValues();

        values.put("TITLE", todo.getTitle());
        values.put("CONTENT", todo.getContent());
        values.put("DATE", todo.getDate());
        values.put("TYPE", todo.getType());
        values.put("STATUS", todo.getStatus());

        long check = database.insert("TODO", null, values);
        return check != -1;

    }
}
