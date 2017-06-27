package com.pranitkulkarni.remindbylocation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranitkulkarni on 5/19/17.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private Context context;

    // Database Name
    private static final String DATABASE_NAME = "p_location_reminder.db";

    public DatabaseManager(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }


    private static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE "+ DatabaseInfo.Schedules.TABLE_NAME+
            " ( " + DatabaseInfo.Schedules.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    DatabaseInfo.Schedules.LABEL + " TEXT, "+
                    DatabaseInfo.Schedules.LATITUDE + " DOUBLE, "+
                    DatabaseInfo.Schedules.LONGITUDE + " DOUBLE, "+
                    DatabaseInfo.Schedules.PLACE_NAME + " TEXT, "+
                    DatabaseInfo.Schedules.IS_NOTIFIED + " BOOLEAN, "+
                    DatabaseInfo.Schedules.ACTION_ID + " INTEGER, "+
                    DatabaseInfo.Schedules.ACTION_TYPE + " INTEGER, "+
                    DatabaseInfo.Schedules.CREATED_AT + " TEXT, "+
                    DatabaseInfo.Schedules.NEEDS_CONFIRMATION + " BOOLEAN, "+
                    DatabaseInfo.Schedules.IS_COMPLETED + " BOOLEAN, "+
                    "FOREIGN KEY ("+ DatabaseInfo.Schedules.ACTION_ID+") REFERENCES "+ DatabaseInfo.Messages.TABLE_NAME + " ("+ DatabaseInfo.Messages.ID+"))";

    private static final String CREATE_MESSAGES_TABLE =
            "CREATE TABLE "+ DatabaseInfo.Messages.TABLE_NAME +
                    " ( " + DatabaseInfo.Messages.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    DatabaseInfo.Messages.MESSAGE + " TEXT, "+
                    DatabaseInfo.Messages.CONTACT_NAME + " TEXT, "+
                    DatabaseInfo.Messages.CONTACT_NUMBER + " INTEGER, "+
                    DatabaseInfo.Messages.SCHEDULE_ID + " INTEGER, "+
                    DatabaseInfo.Messages.SENT_AT+" TEXT, "+
                    "FOREIGN KEY ("+ DatabaseInfo.Messages.SCHEDULE_ID+") REFERENCES "+ DatabaseInfo.Schedules.TABLE_NAME + " (" + DatabaseInfo.Schedules.ID + "))";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_SCHEDULE_TABLE);
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Boolean addSchedule(ScheduleModel model){


        try {

            SQLiteDatabase database = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseInfo.Schedules.LATITUDE,model.getLatitude());
            contentValues.put(DatabaseInfo.Schedules.LONGITUDE,model.getLongitude());
            contentValues.put(DatabaseInfo.Schedules.PLACE_NAME,model.getPlace_name());
            contentValues.put(DatabaseInfo.Schedules.IS_NOTIFIED,false);
            contentValues.put(DatabaseInfo.Schedules.IS_COMPLETED,false);
            contentValues.put(DatabaseInfo.Schedules.LABEL,model.getLabel());
            contentValues.put(DatabaseInfo.Schedules.CREATED_AT,model.getCreated_at());
            contentValues.put(DatabaseInfo.Schedules.ACTION_TYPE,model.getAction_type());

            int id = (int)database.insert(DatabaseInfo.Schedules.TABLE_NAME,null,contentValues);

            if (model.getAction_type() == 1)    // Send SMS
            {
                MessagesModel messagesModel = model.getMessagesModel();

                contentValues = new ContentValues();
                contentValues.put(DatabaseInfo.Messages.SCHEDULE_ID,id);
                contentValues.put(DatabaseInfo.Messages.CONTACT_NAME,messagesModel.getContact_name());
                contentValues.put(DatabaseInfo.Messages.CONTACT_NUMBER,messagesModel.getContact_number());
                contentValues.put(DatabaseInfo.Messages.MESSAGE,messagesModel.getMessage());
                contentValues.put(DatabaseInfo.Messages.SENT_AT,messagesModel.getSent_at());

                int message_id = (int)database.insert(DatabaseInfo.Messages.TABLE_NAME,null,contentValues);

                String updateQuery = "UPDATE "+ DatabaseInfo.Schedules.TABLE_NAME + " SET " + DatabaseInfo.Schedules.ACTION_ID + " = "
                        + message_id + " WHERE " + DatabaseInfo.Schedules.ID + " == "+ id;

                database.execSQL(updateQuery);
            }




            database.close();


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


        return true;
    }


    public void setNotified(int id){

        SQLiteDatabase database = this.getWritableDatabase();

        String query = "UPDATE " + DatabaseInfo.Schedules.TABLE_NAME + " SET " + DatabaseInfo.Schedules.IS_NOTIFIED + " = 1" +
                " WHERE " + DatabaseInfo.Schedules.ID + " == " + id;

        database.execSQL(query);

        database.close();
    }

    // Optimize this...
    public List<ScheduleModel> getLocationsToBeSearched(Location currentLocation){

        ArrayList<ScheduleModel> reminders = new ArrayList<>();

        try {

            String query = "Select * from "+ DatabaseInfo.Schedules.TABLE_NAME + " WHERE " + DatabaseInfo.Schedules.IS_NOTIFIED + " == 0";

            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(query,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

                Location location = new Location("test");
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(DatabaseInfo.Schedules.LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(DatabaseInfo.Schedules.LONGITUDE)));

                ScheduleModel model = new ScheduleModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Schedules.ID)));
                model.setPlace_name(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Schedules.PLACE_NAME)));
                model.setLabel(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Schedules.LABEL)));
                model.setLatitude(location.getLatitude());
                model.setLongitude(location.getLongitude());

                Log.d("Reminder "+model.getId()," at "+model.getPlace_name());

                Float distance = currentLocation.distanceTo(location);
                if (distance < 100){    // Check if it's nearby..

                    reminders.add(model);
                    Log.d("Nearby - "+model.getPlace_name(),"by meters "+distance);

                }

            }

            cursor.close();
            database.close();


        }catch (Exception e){
            e.printStackTrace();
        }


        return reminders;
    }

    public List<ScheduleModel> getAllSchedules(){

        ArrayList<ScheduleModel> reminders = new ArrayList<>();

        try {

            String query = "Select * from "+ DatabaseInfo.Schedules.TABLE_NAME;

            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(query,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){


                ScheduleModel model = new ScheduleModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Schedules.ID)));
                model.setPlace_name(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Schedules.PLACE_NAME)));
                model.setLatitude(cursor.getDouble(cursor.getColumnIndex(DatabaseInfo.Schedules.LATITUDE)));
                model.setLongitude(cursor.getDouble(cursor.getColumnIndex(DatabaseInfo.Schedules.LONGITUDE)));
                model.setLabel(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Schedules.LABEL)));

                reminders.add(model);


            }



        }catch (Exception e){
            e.printStackTrace();
        }


        return reminders;

    }

}
