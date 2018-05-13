package com.example.android.lovemeter.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.lovemeter.data.ResultsContract.CONTENT_AUTHORITY;
import static com.example.android.lovemeter.data.ResultsContract.PATH_RESULT;

/**
 * Created by noureldeen on 9/3/2017.
 */

public class ResultsProvider extends ContentProvider {
    public final String LOG_TAG = ResultsProvider.class.getSimpleName();
    public static final int RESULT = 100, RESULT_ID = 101;
    ResultHelper resultHelper;
    UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        resultHelper = new ResultHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_RESULT, RESULT);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_RESULT + "/#", RESULT_ID);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String s1) {
        final int match = uriMatcher.match(uri);
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase = resultHelper.getReadableDatabase();
        switch (match) {
            case RESULT:
                cursor = sqLiteDatabase.query(ResultsContract.ResultEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            case RESULT_ID:
                cursor = sqLiteDatabase.query(ResultsContract.ResultEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case RESULT:
                return ResultsContract.ResultEntry.CONTENT_LIST_TYPE;
            case RESULT_ID:
                return ResultsContract.ResultEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case RESULT:
                return insertResult(uri, contentValues);
            default:
                throw new IllegalArgumentException("inavlid uri " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = resultHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri, null);
        int noOfDeletedRows = db.delete(ResultsContract.ResultEntry.TABLE_NAME, s, strings);
        return noOfDeletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case RESULT:
                return updateFamilyMember(uri, contentValues, s, strings);
            case RESULT_ID:
                return updateFamilyMember(uri, contentValues, s, strings);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    public Uri insertResult(Uri uri, ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = resultHelper.getWritableDatabase();
        String testerName = contentValues.getAsString(ResultsContract.ResultEntry.COLUMN_TESTER_NAME);
        String testName = contentValues.getAsString(ResultsContract.ResultEntry.COLUMN_TEST_NAME);
        String testResult = contentValues.getAsString(ResultsContract.ResultEntry.COLUMN_RESULT);
        if (testerName.isEmpty()) {
            throw new IllegalArgumentException("Tester Name is required");
        }
        if (testName.isEmpty()) {
            throw new IllegalArgumentException("Test Name is required");
        }
        if (testResult.isEmpty()) {
            throw new IllegalArgumentException("Test Result is required");
        }
        long _id = sqLiteDatabase.insert(ResultsContract.ResultEntry.TABLE_NAME, null, contentValues);
        if (_id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Uri result = ContentUris.withAppendedId(uri, _id);
        Log.v(LOG_TAG, result.toString());
        return result;
    }

    public Integer updateFamilyMember(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = resultHelper.getWritableDatabase();
        String testerName = contentValues.getAsString(ResultsContract.ResultEntry.COLUMN_TESTER_NAME);
        String testName = contentValues.getAsString(ResultsContract.ResultEntry.COLUMN_TEST_NAME);
        String testResult = contentValues.getAsString(ResultsContract.ResultEntry.COLUMN_RESULT);
        if (testerName.isEmpty()) {
            throw new IllegalArgumentException("Tester Name is required");
        }
        if (testName.isEmpty()) {
            throw new IllegalArgumentException("Test Name is required");
        }
        if (testResult.isEmpty()) {
            throw new IllegalArgumentException("Test Result is required");
        }
        int numberOfUpdatedRows = sqLiteDatabase.update(ResultsContract.ResultEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (numberOfUpdatedRows == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numberOfUpdatedRows;
    }
}
