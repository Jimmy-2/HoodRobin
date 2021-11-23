package com.example.capstoneproject.fragments.portfolio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class databaseforbalance extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "databaseforportfoliobalance.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "id";
    private static final String BALANCE = "balance";


    public databaseforbalance(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BALANCE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    Cursor readAllData() {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db!=null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
    void updateData(String row_id, String newbalance){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BALANCE, newbalance);

        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
    }
    void addinitial(String balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BALANCE,balance);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteallbalance(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
