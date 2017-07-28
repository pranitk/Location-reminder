package com.pranitkulkarni.remindbylocation;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.pranitkulkarni.remindbylocation.fragments.FragmentSchedules;
import com.pranitkulkarni.remindbylocation.fragments.FragmentHome;
import com.pranitkulkarni.remindbylocation.fragments.FragmentSettings;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    final static int FINE_LOCATION_REQUEST_CODE = 1;
    final static int SEND_SMS_REQUEST_CODE = 2;
    //final static int COARSE_LOCATION_REQUEST_CODE = 2;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new FragmentHome()).commit();
                    return true;

                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,new FragmentSchedules()).commit();
                    return true;

                case R.id.navigation_about:
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

        Fabric.with(this, new Crashlytics());

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_REQUEST_CODE);

        }
        else{
            Intent locationTracker = new Intent(this,LocationTracker.class);
            startService(locationTracker);
        }

        /*if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},SEND_SMS_REQUEST_CODE);

        }*/

        /*if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},COARSE_LOCATION_REQUEST_CODE);

        }*/

        getSupportFragmentManager().beginTransaction().replace(R.id.content,new FragmentHome()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case FINE_LOCATION_REQUEST_CODE :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Intent locationTracker = new Intent(this,LocationTracker.class);
                    startService(locationTracker);

                }
                else {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Alert!");
                    dialog.setMessage(getString(R.string.location_permission_note));
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_LOCATION_REQUEST_CODE);

                        }
                    });
                    dialog.show();
                }

                break;


            /*case SEND_SMS_REQUEST_CODE:

                //DO NOTHING RIGHT NOW

                break;*/

        }
    }
}
