package com.pranitkulkarni.remindbylocation.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class FragmentSchedules extends Fragment {


    public FragmentSchedules() {
        // Required empty public constructor
    }

    AdapterSchedules adapterSchedules;
    List<ScheduleModel> schedules;
    RecyclerView recyclerView;


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

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        schedules = new DatabaseManager(getActivity()).getAllSchedules();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterSchedules = new AdapterSchedules(schedules,getContext(),this);
        recyclerView.setAdapter(adapterSchedules);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult"+requestCode," Req,Result code"+resultCode);
        if (requestCode == 1){

            if (resultCode == 1){   // Mark as done

                schedules = new DatabaseManager(getActivity()).getAllSchedules();
                adapterSchedules = new AdapterSchedules(schedules,getContext(),this);
                recyclerView.setAdapter(adapterSchedules);

            }

            if (resultCode == 2){   // DELETE

                Log.d("Position removed ",""+data.getIntExtra("position",-1));

                adapterSchedules.notifyItemRemoved(data.getIntExtra("position",-1));
                //recyclerView.setAdapter(adapterSchedules);

            }

        }
    }
}
