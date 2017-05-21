package com.pranitkulkarni.remindbylocation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

public class AddReminder extends AppCompatActivity {


    final int PLACE_PICKER_REQUEST = 1;
    SharedPreferences sharedPreferences;
    Double latitude,longitude;
    String place_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("pranit",MODE_PRIVATE);

        findViewById(R.id.select_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Search for a location

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {

                    startActivityForResult(builder.build(AddReminder.this), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });


        findViewById(R.id.schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseManager databaseManager = new DatabaseManager(AddReminder.this);

                ScheduleModel model = new ScheduleModel();
                model.setLatitude(String.valueOf(latitude));
                model.setLongitude(String.valueOf(longitude));
                model.setPlace_name(place_name);

                // --- TEMPORARY ----
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("latitude",String.valueOf(latitude));
                editor.putString("longitude",String.valueOf(longitude));

                editor.apply();

                if (databaseManager.addSchedule(model))
                    finish();
                else
                    Log.d("Add schedule","Something went wrong!");

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PLACE_PICKER_REQUEST){

            if (resultCode == RESULT_OK){

                Place place = PlacePicker.getPlace(data,this);
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                place_name = String.format("%s",place.getName());

                /*

                // --- TEMPORARY ----
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("latitude",String.valueOf(latitude));
                editor.putString("longitude",String.valueOf(longitude));

                editor.apply();
                //

                */

                Log.d("Selected Latitude",""+latitude);
                Log.d("Selected Longitude",""+longitude);

            }

        }

    }
}
