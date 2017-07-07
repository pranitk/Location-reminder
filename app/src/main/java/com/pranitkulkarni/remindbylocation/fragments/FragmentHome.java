package com.pranitkulkarni.remindbylocation.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranitkulkarni.remindbylocation.AddMessageReminder;
import com.pranitkulkarni.remindbylocation.AddTextReminder;
import com.pranitkulkarni.remindbylocation.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment {


    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        view.findViewById(R.id.add_text_reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getActivity(),AddMessageReminder.class));
                startActivity(new Intent(getActivity(),AddTextReminder.class));

            }
        });

        view.findViewById(R.id.add_message_reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(getActivity(),AddMessageReminder.class));
                startActivity(new Intent(getActivity(),AddMessageReminder.class));

            }
        });


        return view;
    }

}
