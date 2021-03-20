package com.example.masterdetail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DETAILS = "details";
    private static final String TABLE_NAME = "notes";

    private static DBHelper sInstance = null;

    public static DBHelper getsInstance(Context contex) {
        if (sInstance == null){
            sInstance = new DBHelper(contex);
        }
        return sInstance;
    }

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    // Create table SQL query
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_DETAILS + " TEXT "
                    + ")";


    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);

    }

    public long insertorUpdate(Note note) {
        // get writable database as we want to write data
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_DETAILS, note.getDetail());

        // insert row
        if(note.getId() == 0)
            id = db.insert(TABLE_NAME, null, values);
        else{
            //values.put(COLUMN_ID,id);
            id = db.update(TABLE_NAME, values,COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getId())});

        }

        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }


    public List<Note> getAllNotes() {
        List<Note> dbNotes = new ArrayList<>();

        // Select All Query

        String selectQuery = "SELECT  * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                note.setDetail(cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS)));

                dbNotes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return dbNotes;
    }

    public void deleteById(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    public Note getNoteByid(long id ){
        SQLiteDatabase db = this.getReadableDatabase();
        String rawQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(rawQuery, null);
        Note note = new Note();
        cursor.moveToFirst();
        note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        note.setDetail(cursor.getString(cursor.getColumnIndex(COLUMN_DETAILS)));
        return note;
    }

    public void deleteByNode(Note note){
        deleteById(note.getId());
    }

}
