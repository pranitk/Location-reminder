package com.pranitkulkarni.remindbylocation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranitkulkarni.remindbylocation.AdapterSchedules;
import com.pranitkulkarni.remindbylocation.R;
import com.pranitkulkarni.remindbylocation.database.DatabaseManager;
import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends Fragment {


    public FragmentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        List<ScheduleModel> reminders = new DatabaseManager(getActivity()).getAllSchedules();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new AdapterSchedules(reminders,getContext()));

        return view;
    }

}
