package com.pranitkulkarni.remindbylocation;


import java.text.SimpleDateFormat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.MessagesModel;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

public class ViewReminder extends AppCompatActivity {

    TextView locationTv, contactTv, reminderTv, sentAtTv,createdAtTv;
    int schedule_id = 0,position = -1;
    ScheduleModel scheduleModel;
    LinearLayout markAsDone,repeat;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Details");

        schedule_id = getIntent().getIntExtra("schedule_id",0);
        position = getIntent().getIntExtra("position",-1);
        Log.d("ID passed",""+schedule_id);

        scheduleModel = new DatabaseManager(ViewReminder.this).getSchedule(schedule_id);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        locationTv = (TextView)findViewById(R.id.location_text);
        contactTv = (TextView)findViewById(R.id.contact_name);
        reminderTv = (TextView)findViewById(R.id.reminder_text);
        sentAtTv = (TextView)findViewById(R.id.sent_at);
        createdAtTv = (TextView)findViewById(R.id.created_at);
        markAsDone = (LinearLayout)findViewById(R.id.mark_as_done);
        repeat = (LinearLayout)findViewById(R.id.repeat);

        Log.d("ID retreived",""+scheduleModel.getId());
        locationTv.setText(scheduleModel.getPlace_name());

        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Add some animation

               if(new DatabaseManager(ViewReminder.this).updateCompleted(schedule_id,1)) {
                   Toast.makeText(ViewReminder.this,"Marked as done!",Toast.LENGTH_LONG).show();
                   setResult(1);
                   finish();
               }
               else
                Snackbar.make(coordinatorLayout,"Something went wrong! Please try again",Snackbar.LENGTH_LONG).show();


            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewReminder.this);
                dialog.setTitle("Are you sure to delete the reminder ?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (new DatabaseManager(ViewReminder.this).deleteSchedule(schedule_id,scheduleModel.getAction_type())) {
                            Toast.makeText(ViewReminder.this,"Reminder deleted",Toast.LENGTH_LONG).show();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("position",position);
                            setResult(2,returnIntent);
                            finish();
                        }
                        else
                            Snackbar.make(coordinatorLayout,"Something went wrong! Please try again",Snackbar.LENGTH_LONG).show();


                    }
                });

                dialog.setNegativeButton("No",null);
                dialog.show();



            }
        });


        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(new DatabaseManager(ViewReminder.this).updateCompleted(schedule_id,0)) {

                    Toast.makeText(ViewReminder.this,"Done! You will be reminded next time you are near "+scheduleModel.getPlace_name(),Toast.LENGTH_LONG).show();
                    setResult(1);
                    finish();
                }
                else
                    Snackbar.make(coordinatorLayout,"Something went wrong! Please try again",Snackbar.LENGTH_LONG).show();

            }
        });

        SimpleDateFormat systemDateFormat = new SimpleDateFormat(getString(R.string.system_date_format));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM");

        if (scheduleModel.getAction_type() == 1){   // SMS reminder...

            if (scheduleModel.getCompleted())
                markAsDone.setVisibility(View.GONE);
            else if (!scheduleModel.getNotified())
                sentAtTv.setText("Yet to be sent");
            else
                sentAtTv.setText("SMS sending failed. Check your messages app.");

            MessagesModel messageModel = scheduleModel.getMessagesModel();
            contactTv.setText(messageModel.getContact_name());
            reminderTv.setText(messageModel.getMessage());
            //sentAtTv.setText(messageModel.getSent_at());

            try{

                String time = timeFormat.format(systemDateFormat.parse(messageModel.getSent_at()));
                String day = dateFormat.format(systemDateFormat.parse(messageModel.getSent_at()));

                sentAtTv.setText("Sent on "+day+" at "+time);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        else {

            if (scheduleModel.getNotified()) {    // Not worked on 'COMPLETED' flag yet..
                markAsDone.setVisibility(View.GONE);
                repeat.setVisibility(View.VISIBLE);

            }

            findViewById(R.id.contact_name_layout).setVisibility(View.GONE);

            ImageView logo = (ImageView)findViewById(R.id.reminder_type_logo);
            logo.setImageDrawable(ContextCompat.getDrawable(ViewReminder.this, R.drawable.ic_text_reminder_red));

            reminderTv.setText(scheduleModel.getLabel());

            findViewById(R.id.sent_at_layout).setVisibility(View.GONE);

            /*ImageView notificationIcon = (ImageView)findViewById(R.id.notified_icon);
            notificationIcon.setImageDrawable(ContextCompat.getDrawable(ViewReminder.this, R.drawable.ic_notifications_dark));

            // TODO: Add NOTIFIED_AT column ..
            sentAtTv.setText("Notified at ");*/

            /*try{

                String time = timeFormat.format(systemDateFormat.parse());
                String day = dateFormat.format(systemDateFormat.parse());

                sentAtTv.setText("Notified at "+day+" at "+time);

            }catch (Exception e){
                e.printStackTrace();
            }*/


        }




        try{

            String time = timeFormat.format(systemDateFormat.parse(scheduleModel.getCreated_at()));
            String day = dateFormat.format(systemDateFormat.parse(scheduleModel.getCreated_at()));

            createdAtTv.setText("Created on "+day+" at "+time);

        }catch (Exception e){
            e.printStackTrace();
        }

        findViewById(R.id.location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String coordinates = "geo:"+scheduleModel.getLatitude()+","+scheduleModel.getLongitude();
                Uri gmmIntentUri = Uri.parse(coordinates);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return true;
    }
}
