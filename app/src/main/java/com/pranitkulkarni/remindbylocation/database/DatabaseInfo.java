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
        public static final String IS_NOTIFIED = "is_notified";
        public static final String ACTION_ID = "action_id";
        public static final String ACTION_TYPE = "action_type";
        public static final String CREATED_AT = "created_at";
        public static final String NEEDS_CONFIRMATION = "needs_confirmation";
        public static final String NOTIFIED_AT = "notified_at";


    }


    public static class Messages implements BaseColumns{

        public static final String TABLE_NAME = "messages";

        public static final String ID = "id";
        public static final String SCHEDULE_ID = "schedule_id";
        public static final String MESSAGE = "message";
        public static final String CONTACT_NUMBER = "contact_number";
        public static final String CONTACT_NAME = "contact_name";
        public static final String SENT_AT = "sent_at";

    }
}
