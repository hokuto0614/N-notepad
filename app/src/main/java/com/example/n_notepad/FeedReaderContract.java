package com.example.n_notepad;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    private FeedReaderContract() {}

    public static class FeedEntry implements BaseColumns {
        public static final String DATABASE_NAME = "NEdit_DB";
        public static final String TABLE_NAME = "MEMO_TABLE";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BODY = "body";
    }
}
