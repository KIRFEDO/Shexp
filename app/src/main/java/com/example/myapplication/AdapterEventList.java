package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterEventList extends ArrayAdapter<EventListElement> {

    public AdapterEventList(Context context, ArrayList<EventListElement> eventArrayList){
        super(context, R.layout.row_event, eventArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        EventListElement event = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_event, parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.ELI_eventName);
        TextView ownerName = convertView.findViewById(R.id.ELI_ownerName);

        eventName.setText(event.eventName);
        ownerName.setText(event.ownerName);

        return convertView;
    }

}
