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

        /*RecyclerView recyclerViewPending = (RecyclerView)view.findViewById(R.id.recyclerView1);
        RecyclerView recyclerViewCompleted = (RecyclerView)view.findViewById(R.id.recyclerView2);

        List<ScheduleModel> pendingSchedules = new DatabaseManager(getActivity()).getPendingSchedules();
        List<ScheduleModel> completedSchedules = new DatabaseManager(getActivity()).getCompletedSchedules();

        recyclerViewPending.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPending.setAdapter(new AdapterSchedules(pendingSchedules,getContext()));

        recyclerViewCompleted.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCompleted.setAdapter(new AdapterSchedules(completedSchedules,getContext()));*/

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        List<ScheduleModel> schedules = new DatabaseManager(getActivity()).getAllSchedules();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new AdapterSchedules(schedules,getContext()));


        return view;
    }

}
