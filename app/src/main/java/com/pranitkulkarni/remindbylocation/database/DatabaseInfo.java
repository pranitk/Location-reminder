package com.pranitkulkarni.remindbylocation.database;

import android.provider.BaseColumns;

/**
 * Created by pranitkulkarni on 5/19/17.
 */

public class DatabaseInfo {

    private DatabaseInfo(){}

    public static class Schedules implements BaseColumns{

        public static final String TABLE_NAME = "schedules";

        public static final String ID = "id";
        public static final String LATITUDE = "longitude";
        public static final String LONGITUDE = "latitude";
        public static final String PLACE_NAME = "place_name";
        public static final String LABEL = "label";
        public static final String IS_COMPLETED = "is_completed";

    }
}
