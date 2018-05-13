package com.example.android.lovemeter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by noureldeen on 8/30/2017.
 */

public class ResultHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyResults";
    private static final int DATABASE_VERSION = 1;

    public ResultHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE "
                + ResultsContract.ResultEntry.TABLE_NAME + " (  "
                + ResultsContract.ResultEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + ResultsContract.ResultEntry.COLUMN_TESTER_NAME + " TEXT NOT NULL , "
                + ResultsContract.ResultEntry.COLUMN_TEST_NAME + " TEXT NOT NULL , "
                + ResultsContract.ResultEntry.COLUMN_RESULT + " INTEGER NOT NULL ); ";
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
