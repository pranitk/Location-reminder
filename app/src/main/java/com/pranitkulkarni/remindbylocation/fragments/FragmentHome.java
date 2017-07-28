package com.pranitkulkarni.remindbylocation.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranitkulkarni.remindbylocation.AddMessageReminder;
import com.pranitkulkarni.remindbylocation.AddTextReminder;
import com.pranitkulkarni.remindbylocation.MainActivity;
import com.pranitkulkarni.remindbylocation.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {

    AppCompatButton addMessageReminderBtn;

    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        addMessageReminderBtn = (AppCompatButton)view.findViewById(R.id.add_message_reminder);

        view.findViewById(R.id.add_text_reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),AddTextReminder.class));

            }
        });

        addMessageReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setTitle("Alert");
                    dialog.setMessage(getString(R.string.sms_permission_note));
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.SEND_SMS},1);

                        }
                    });
                    dialog.show();

                }
                else
                    startActivity(new Intent(getActivity(), AddMessageReminder.class));



            }
        });






        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                startActivity(new Intent(getActivity(), AddMessageReminder.class));

            }

        }
    }
}
