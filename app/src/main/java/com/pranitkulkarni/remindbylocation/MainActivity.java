package com.pranitkulkarni.remindbylocation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.pranitkulkarni.remindbylocation.fragments.FragmentDashboard;
import com.pranitkulkarni.remindbylocation.fragments.FragmentHome;
import com.pranitkulkarni.remindbylocation.fragments.FragmentSettings;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new FragmentHome()).commit();
                    return true;

                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new FragmentDashboard()).commit();
                    return true;

                case R.id.navigation_notifications:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new FragmentSettings()).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.content,new FragmentHome()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Intent locationTracker = new Intent(this,LocationTracker.class);
        startService(locationTracker);


        findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }



}
