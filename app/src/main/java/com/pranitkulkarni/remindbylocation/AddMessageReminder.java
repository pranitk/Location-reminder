package com.pranitkulkarni.remindbylocation;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.MessagesModel;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

import java.util.Calendar;

public class AddMessageReminder extends AppCompatActivity {


    final int PLACE_PICKER_REQUEST = 1;
    //SharedPreferences sharedPreferences;
    Double latitude,longitude;
    String place_name="";
    TextView locationTv;
    EditText messageEt,phoneEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationTv = (TextView)findViewById(R.id.location_text);
        messageEt = (EditText) findViewById(R.id.message);
        phoneEt = (EditText)findViewById(R.id.phone_number);

        //sharedPreferences = getSharedPreferences("pranit",MODE_PRIVATE);

        findViewById(R.id.select_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Search for a location

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {

                    startActivityForResult(builder.build(AddMessageReminder.this), PLACE_PICKER_REQUEST);

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

                if (isValidated()){

                    DatabaseManager databaseManager = new DatabaseManager(AddMessageReminder.this);

                    ScheduleModel model = new ScheduleModel();
                    model.setLatitude(latitude);
                    model.setLongitude(longitude);
                    model.setPlace_name(place_name);
                    model.setCreated_at(String.valueOf(Calendar.getInstance().getTimeInMillis()));  //TODO check this out..
                    model.setAction_id(1);


                    MessagesModel messagesModel = new MessagesModel();
                    messagesModel.setMessage(messageEt.getText().toString());
                    messagesModel.setContact_number(Integer.parseInt(phoneEt.getText().toString()));
                    messagesModel.setContact_name("");

                    if (databaseManager.addSchedule(model))
                        finish();
                    else
                        Log.d("Add schedule","Something went wrong!");

                }



            }
        });
    }


    private Boolean isValidated(){

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        if (messageEt.getText().toString().isEmpty()){

            Snackbar.make(coordinatorLayout,"Enter your message to continue",Snackbar.LENGTH_LONG).show();

            return false;
        }


        if (phoneEt.getText().toString().isEmpty()){

            Snackbar.make(coordinatorLayout,"Enter contact number to continue",Snackbar.LENGTH_LONG).show();

            return false;
        }
        else if (phoneEt.getText().length() < 10){

            Snackbar.make(coordinatorLayout,"Invalid contact number",Snackbar.LENGTH_LONG).show();

            return false;
        }


        if (latitude == null){

            Snackbar.make(coordinatorLayout,"Select location to continue",Snackbar.LENGTH_LONG).show();

            return false;

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return true;
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

                locationTv.setText(place_name);


                Log.d("Selected Latitude",""+latitude);
                Log.d("Selected Longitude",""+longitude);

            }

        }

    }
}
