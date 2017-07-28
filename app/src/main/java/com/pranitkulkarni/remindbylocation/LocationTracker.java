package com.pranitkulkarni.remindbylocation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.MessagesModel;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by pranitkulkarni on 5/17/17.
 */

public class LocationTracker extends Service {

    private static final String TAG = LocationTracker.class.getSimpleName();
    private LocationManager myLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;
    SharedPreferences sharedPreferences;
    //int random_number=0;    // Use for unique notification id

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class LocationListener implements android.location.LocationListener{

        Location myLastLocation;


        public LocationListener(String provider){

            myLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG,"onLocationChanged");
            Log.d(TAG,"Latitude - "+location.getLatitude());
            Log.d(TAG,"Longitude - "+location.getLongitude());
            myLastLocation.set(location);

            try{


                List<ScheduleModel> reminders = new DatabaseManager(getApplicationContext()).getLocationsToBeSearched(location);

                if (reminders.size() > 0){

                    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    // Notify about each reminder...
                    for (int i =0; i < reminders.size(); i++){

                        ScheduleModel reminder = reminders.get(i);

                        Intent openApp = new Intent(getApplicationContext(), ViewReminder.class);
                        openApp.putExtra("schedule_id",reminder.getId());
                        openApp.putExtra("position",i);
                        openApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,openApp,PendingIntent.FLAG_UPDATE_CURRENT);

                        if (reminder.getAction_type() == 1) // Send SMS
                        {

                            DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
                            String notification_title = "SMS sent to "+reminder.getMessagesModel().getContact_name();

                            if(sendSMS(reminder.getMessagesModel()))
                                databaseManager.updateSentAt(String.valueOf(Calendar.getInstance().getTime()),reminder.getAction_id());
                            else    //.. if failed
                                notification_title = "SMS sending failed";

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                            Notification notification = builder.setContentTitle(notification_title)
                                    .setContentText(getString(R.string.notification_title)+" "+reminder.getPlace_name())
                                    .setColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))    // Primary color will give some consistency to the user
                                    .setSmallIcon(R.drawable.ic_notification)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_title)+" "+reminder.getPlace_name()))
                                    .build();


                            //notificationManager.notify(random_number,notification);
                            notificationManager.notify(reminder.getId(),notification);


                            databaseManager.setNotified(reminder.getId());



                        }
                        else {


                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                            Notification notification = builder.setContentTitle(reminder.getLabel())
                                    .setContentText(getString(R.string.notification_title)+" "+reminder.getPlace_name())
                                    .setColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))    // Primary color will give some consistency to the user
                                    .setSmallIcon(R.drawable.ic_notification)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    //.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_title)+" "+reminder.getPlace_name()))
                                    .build();


                            //notificationManager.notify(random_number,notification);
                            notificationManager.notify(reminder.getId(),notification);

                            new DatabaseManager(getApplicationContext()).setNotified(reminder.getId());

                        }




                    }

                }


            }catch (Exception e){
                e.printStackTrace();
            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    LocationListener[] myLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };



    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getApplicationContext().getSharedPreferences("pranit",MODE_PRIVATE);

        initializeLocationManager();

        //random_number = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        try {

            myLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    myLocationListeners[0]);


        }catch (SecurityException e){
            e.printStackTrace();
        }

    }

    private void initializeLocationManager() {

        Log.e("", "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);

        if (myLocationManager == null)
            myLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


    }

    public Boolean sendSMS(MessagesModel message){

        try{

            Log.d("Sending sms ",message.getMessage());
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(message.getContact_number(),null,message.getMessage(),null,null);

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


        return true;
    }


    @Override
    public void onDestroy() {

        Log.e("LocationTracker service", "onDestroy");
        super.onDestroy();
        if (myLocationManager != null) {
            for (int i = 0; i < myLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    myLocationManager.removeUpdates(myLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("", "fail to remove location listener, ignore", ex);
                }
            }
        }
    }
}
