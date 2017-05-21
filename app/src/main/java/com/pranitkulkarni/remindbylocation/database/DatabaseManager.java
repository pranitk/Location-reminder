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
                    DatabaseInfo.Schedules.IS_COMPLETED + " BOOLEAN "+ ")";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_SCHEDULE_TABLE);
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

            database.insert(DatabaseInfo.Schedules.TABLE_NAME,null,contentValues);

            database.close();


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


        return true;
    }



    // Optimize this...
    public List<ScheduleModel> getLocationsToBeSearched(Location currentLocation){

        ArrayList<ScheduleModel> reminders = new ArrayList<>();

        try {

            String query = "Select * from "+ DatabaseInfo.Schedules.TABLE_NAME; // TODO: Only incomplete ones...

            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(query,null);

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

                Location location = new Location("test");
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(DatabaseInfo.Schedules.LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(DatabaseInfo.Schedules.LONGITUDE)));

                ScheduleModel model = new ScheduleModel();
                model.setId(cursor.getInt(cursor.getColumnIndex(DatabaseInfo.Schedules.ID)));
                model.setPlace_name(cursor.getString(cursor.getColumnIndex(DatabaseInfo.Schedules.PLACE_NAME)));
                model.setLatitude(location.getLatitude());
                model.setLongitude(location.getLongitude());

                Log.d("Reminder "+model.getId()," at "+model.getPlace_name());

                Float distance = currentLocation.distanceTo(location);
                if (distance < 100){    // Check if it's nearby..

                    reminders.add(model);
                    Log.d("Nearby - "+model.getPlace_name(),"by meters "+distance);

                }

            }



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

                reminders.add(model);


            }



        }catch (Exception e){
            e.printStackTrace();
        }


        return reminders;

    }

}
