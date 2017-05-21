package com.pranitkulkarni.remindbylocation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranitkulkarni.remindbylocation.database.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranitkulkarni on 5/20/17.
 */

public class AdapterSchedules extends RecyclerView.Adapter<AdapterSchedules.myViewHolder> {

    private List<ScheduleModel> list;
    private Context context;

    public AdapterSchedules(List<ScheduleModel> list,Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.schedule_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        final ScheduleModel model = list.get(position);
        holder.placeName.setText(model.getPlace_name());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView placeName;

        public myViewHolder(View itemView){
            super(itemView);

            placeName = (TextView)itemView.findViewById(R.id.place_name);
        }

    }
}
