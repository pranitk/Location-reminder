package com.pranitkulkarni.remindbylocation;


import java.text.SimpleDateFormat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.MessagesModel;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

public class ViewReminder extends AppCompatActivity {

    TextView locationTv, contactTv, reminderTv, sentAtTv,createdAtTv;
    int schedule_id = 0;
    ScheduleModel scheduleModel;
    LinearLayout markAsDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Details");

        schedule_id = getIntent().getIntExtra("schedule_id",0);
        Log.d("ID passed",""+schedule_id);

        scheduleModel = new DatabaseManager(ViewReminder.this).getSchedule(schedule_id);

        locationTv = (TextView)findViewById(R.id.location_text);
        contactTv = (TextView)findViewById(R.id.contact_name);
        reminderTv = (TextView)findViewById(R.id.reminder_text);
        sentAtTv = (TextView)findViewById(R.id.sent_at);
        createdAtTv = (TextView)findViewById(R.id.created_at);
        markAsDone = (LinearLayout)findViewById(R.id.mark_as_done);

        Log.d("ID retreived",""+scheduleModel.getId());
        locationTv.setText(scheduleModel.getPlace_name());

        markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Add some animation

               if(new DatabaseManager(ViewReminder.this).setCompleted(schedule_id))
                   finish();


            }
        });

        SimpleDateFormat systemDateFormat = new SimpleDateFormat(getString(R.string.system_date_format));
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM");

        if (scheduleModel.getAction_type() == 1){   // SMS reminder...

            if (scheduleModel.getCompleted())
                markAsDone.setVisibility(View.GONE);

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

            if (scheduleModel.getNotified())    // Not worked on 'COMPLETED' flag yet..
                markAsDone.setVisibility(View.GONE);

            findViewById(R.id.contact_name_layout).setVisibility(View.GONE);

            ImageView logo = (ImageView)findViewById(R.id.reminder_type_logo);
            logo.setImageDrawable(ContextCompat.getDrawable(ViewReminder.this, R.drawable.ic_text_reminder_red));

            reminderTv.setText(scheduleModel.getLabel());

            ImageView notificationIcon = (ImageView)findViewById(R.id.notified_icon);
            notificationIcon.setImageDrawable(ContextCompat.getDrawable(ViewReminder.this, R.drawable.ic_notifications_dark));

            // TODO: Add NOTIFIED_AT column ..
            sentAtTv.setText("Notified at ");
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return true;
    }
}
