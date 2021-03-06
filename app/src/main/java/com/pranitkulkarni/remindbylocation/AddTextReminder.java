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
import android.widget.TextView;

import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

import java.util.Calendar;

public class AddTextReminder extends AppCompatActivity {

    final int PLACE_PICKER_REQUEST = 1;
    //SharedPreferences sharedPreferences;
    Double latitude,longitude;
    String place_name="";
    TextView locationTv,contentTv;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_reminder);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Add");
        locationTv = (TextView)findViewById(R.id.location_text);
        contentTv = (TextView)findViewById(R.id.text);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        findViewById(R.id.select_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Search for a location

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {

                    startActivityForResult(builder.build(AddTextReminder.this), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


                Answers.getInstance().logCustom(new CustomEvent("Select location"));
                //Answers.getInstance().logAddToCart(new AddToCartEvent().putItemName("Search location").putItemType("Add reminder"));

            }
        });


        findViewById(R.id.schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (latitude != null && !contentTv.getText().toString().isEmpty()){

                    ScheduleModel model = new ScheduleModel();
                    model.setLatitude(latitude);
                    model.setLongitude(longitude);
                    model.setPlace_name(place_name);
                    model.setCreated_at(""+ Calendar.getInstance().getTime());
                    model.setLabel(contentTv.getText().toString());
                    model.setAction_type(0);

                    DatabaseManager databaseManager = new DatabaseManager(AddTextReminder.this);

                    if (databaseManager.addSchedule(model)) {

                        Answers.getInstance().logCustom(new CustomEvent("Add TEXT reminder"));
                        //Answers.getInstance().logAddToCart( new AddToCartEvent().putItemName("Text reminder").putItemType("Add reminder"));
                        finish();
                    }
                    else
                        Snackbar.make(coordinatorLayout,"Something went wrong! Please try again",Snackbar.LENGTH_LONG).show();

                }
                else {

                    if (latitude == null)
                        Snackbar.make(coordinatorLayout,"Select location to proceed",Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(coordinatorLayout,"Enter your reminder",Snackbar.LENGTH_LONG).show();

                }

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

                locationTv.setText(place_name);


                Log.d("Selected Latitude",""+latitude);
                Log.d("Selected Longitude",""+longitude);

            }

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return true;
    }
}
