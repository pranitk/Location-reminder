package com.pranitkulkarni.remindbylocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewReminder extends AppCompatActivity {

    TextView locationTv, contactTv, messageTv, sentAtTv,createdAtTv;
    int schedule_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);

        schedule_id = getIntent().getIntExtra("schedule_id",0);

        locationTv = (TextView)findViewById(R.id.location_text);
        contactTv = (TextView)findViewById(R.id.contact_name);
        messageTv = (TextView)findViewById(R.id.message);
        sentAtTv = (TextView)findViewById(R.id.sent_at);
        createdAtTv = (TextView)findViewById(R.id.created_at);



    }
}
