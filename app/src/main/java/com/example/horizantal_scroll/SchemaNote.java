package com.example.horizantal_scroll;

import android.provider.BaseColumns;

public class SchemaNote {

    private SchemaNote() {}

    /* Inner class that defines the table contents */
    public static class Note implements BaseColumns {
        public static final String TABLE_NAME = "NOTA";
        public static final String COLUMN_NAME_TITLE = "titolo";
        public static final String COLUMN_NAME_TEXT = "testo";
        public static final String COLUMN_NAME_IMAGE_PATH = "percorsoImmagini";
    }


}