package com.pranitkulkarni.remindbylocation;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.MessagesModel;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

public class ViewReminder extends AppCompatActivity {

    TextView locationTv, contactTv, reminderTv, sentAtTv,createdAtTv;
    int schedule_id = 0;
    ScheduleModel scheduleModel;

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

        Log.d("ID retreived",""+scheduleModel.getId());
        locationTv.setText(scheduleModel.getPlace_name());

        if (scheduleModel.getAction_type() == 1){   // SMS reminder...

            MessagesModel messageModel = scheduleModel.getMessagesModel();
            contactTv.setText(messageModel.getContact_name());
            reminderTv.setText(messageModel.getMessage());
            sentAtTv.setText(messageModel.getSent_at());


        }
        else {

            findViewById(R.id.contact_name_layout).setVisibility(View.GONE);

            ImageView logo = (ImageView)findViewById(R.id.reminder_type_logo);
            logo.setImageDrawable(ContextCompat.getDrawable(ViewReminder.this, R.drawable.ic_text_reminder_red));

            reminderTv.setText(scheduleModel.getLabel());

            ImageView notificationIcon = (ImageView)findViewById(R.id.notified_icon);
            notificationIcon.setImageDrawable(ContextCompat.getDrawable(ViewReminder.this, R.drawable.ic_notifications_dark));
            sentAtTv.setText("Notified at");

        }



        createdAtTv.setText("Created on "+scheduleModel.getCreated_at());




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return true;
    }
}
