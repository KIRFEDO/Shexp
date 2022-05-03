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

public class EventItemListAdapter extends ArrayAdapter<EventItemListElement> {

    public EventItemListAdapter(Context context, ArrayList<EventItemListElement> eventArrayList){
        super(context, R.layout.event_item_list_item, eventArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        EventItemListElement item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item_list_item, parent, false);
        }

        TextView eventName = convertView.findViewById(R.id.EILI_eventName);
        TextView ownerName = convertView.findViewById(R.id.EILI_ownerName);
        TextView itemCost = convertView.findViewById(R.id.EILI_itemCost);

        eventName.setText(item.eventName);
        ownerName.setText(item.ownerName);
        itemCost.setText(Integer.toString(item.itemCost));

        return convertView;
    }

}
