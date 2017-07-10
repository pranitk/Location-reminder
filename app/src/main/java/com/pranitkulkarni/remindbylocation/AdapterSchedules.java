package com.pranitkulkarni.remindbylocation;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.schedule_list_item_new,parent,false));
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {

        final ScheduleModel model = list.get(position);
        holder.placeName.setText(model.getPlace_name());
        holder.reminderText.setText(model.getLabel());
        Log.d(""+model.getPlace_name()," Created at - "+model.getCreated_at());

        if (model.getAction_type() == 1)
            holder.logo.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_sms_dark));


        if (model.getNotified()){
            holder.placeName.setTextColor(ContextCompat.getColor(context,R.color.grey_600));
            holder.reminderText.setTypeface(Typeface.DEFAULT);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView placeName,reminderText,reminderSubText;
        ImageView logo;

        public myViewHolder(View itemView){
            super(itemView);

            placeName = (TextView)itemView.findViewById(R.id.location_text);
            reminderText = (TextView)itemView.findViewById(R.id.reminder_text);
            reminderSubText = (TextView)itemView.findViewById(R.id.reminder_sub_text);
            logo = (ImageView)itemView.findViewById(R.id.reminder_type_logo);

        }

    }
}
