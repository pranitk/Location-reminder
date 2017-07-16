package com.pranitkulkarni.remindbylocation;

import android.content.Context;
import android.content.Intent;
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
        Log.d("------","-------");
        Log.d(""+model.getPlace_name()," Created at - "+model.getCreated_at());
        Log.d(""+model.getLabel()," type - "+model.getAction_type());
        Log.d("Notified ",""+model.getNotified());
        Log.d("------","-------");

        if (model.getAction_type() == 1) {
            holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_sms_green));
            holder.reminderSubText.setText(model.getMessagesModel().getMessage());
            holder.reminderSubText.setVisibility(View.VISIBLE);
        }
        else {
            holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_text_reminder_red));
            holder.reminderSubText.setVisibility(View.GONE);
        }


        if (model.getNotified()){
            holder.placeName.setTextColor(ContextCompat.getColor(context,R.color.grey_600));
            holder.reminderText.setTypeface(Typeface.DEFAULT);
        }
        else {
            holder.reminderText.setTypeface(Typeface.DEFAULT_BOLD);
            holder.placeName.setTextColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        }

        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,ViewReminder.class);
                intent.putExtra("schedule_id",model.getId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView placeName,reminderText,reminderSubText;
        ImageView logo;
        View clickLayout;

        public myViewHolder(View itemView){
            super(itemView);

            clickLayout = itemView.findViewById(R.id.click_layout);
            placeName = (TextView)itemView.findViewById(R.id.location_text);
            reminderText = (TextView)itemView.findViewById(R.id.reminder_text);
            reminderSubText = (TextView)itemView.findViewById(R.id.reminder_sub_text);
            logo = (ImageView)itemView.findViewById(R.id.reminder_type_logo);

        }

    }
}
