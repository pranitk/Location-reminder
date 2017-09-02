package com.pranitkulkarni.remindbylocation;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
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
    String place_name="",contact_name="";
    TextView locationTv,contactChipName,contactChipInitial;
    EditText messageEt,phoneEt;
    private static final int RESULT_PICK_CONTACT = 24;
    RelativeLayout contactChip;

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
        contactChip = (RelativeLayout) findViewById(R.id.contact_chip);
        contactChipName = (TextView)findViewById(R.id.name);
        contactChipInitial = (TextView)findViewById(R.id.initial);

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

                Answers.getInstance().logCustom(new CustomEvent("Select location"));
                //Answers.getInstance().logAddToCart(new AddToCartEvent().putItemName("Search location").putItemType("Add reminder"));

            }
        });

        findViewById(R.id.select_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Answers.getInstance().logCustom(new CustomEvent("Select contact"));

                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);

            }
        });


        findViewById(R.id.schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isValidated()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMessageReminder.this);
                    builder.setTitle("Save ?");
                    builder.setMessage("Note that you will be charged for the SMS as per your plan by the network provider.");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DatabaseManager databaseManager = new DatabaseManager(AddMessageReminder.this);

                            ScheduleModel model = new ScheduleModel();
                            model.setLatitude(latitude);
                            model.setLongitude(longitude);
                            model.setPlace_name(place_name);
                            model.setCreated_at(""+ Calendar.getInstance().getTime());
                            model.setAction_type(1);


                            MessagesModel messagesModel = new MessagesModel();
                            messagesModel.setMessage(messageEt.getText().toString());
                            messagesModel.setContact_number(phoneEt.getText().toString());

                            if (contact_name.isEmpty())
                                messagesModel.setContact_name(phoneEt.getText().toString());
                            else
                                messagesModel.setContact_name(contact_name);

                            model.setMessagesModel(messagesModel);
                            model.setLabel("Send SMS to "+messagesModel.getContact_name());

                            if (databaseManager.addSchedule(model)) {

                                Answers.getInstance().logCustom(new CustomEvent("Add SMS reminder"));
                                //Answers.getInstance().logAddToCart(new AddToCartEvent().putItemName("Message reminder").putItemType("Add reminder"));
                                finish();
                            }
                            else
                                Log.d("Add schedule","Something went wrong!");
                        }
                    });

                    builder.setNegativeButton("Cancel",null);
                    builder.show();


                }



            }
        });

        findViewById(R.id.remove_selected_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contact_name = "";
                phoneEt.setEnabled(true);
                phoneEt.setText(null);
                contactChip.setVisibility(View.GONE);

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


    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);

            if(cursor.moveToFirst()){

                contact_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                phoneEt.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                phoneEt.setEnabled(false);
                contactChipInitial.setText(contact_name.substring(0,1));
                contactChipName.setText(contact_name);

            }


            cursor.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if (requestCode == RESULT_PICK_CONTACT){

            if (resultCode == RESULT_OK) {
                contactChip.setVisibility(View.VISIBLE);
                contactPicked(data);
            }

        }

    }
}
