package com.example.android.lovemeter.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by noureldeen on 8/30/2017.
 */

public final class ResultsContract {

    public final static String CONTENT_AUTHORITY = "com.example.android.lovemeter";
    public static final String PATH_RESULT = "MyResult";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class ResultEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESULT;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESULT;
        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RESULT);
        public static final String TABLE_NAME = "ResultMember";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TESTER_NAME = "TesterName";
        public static final String COLUMN_TEST_NAME = "TestName";
        public static final String COLUMN_RESULT = "Result";
    }
}
