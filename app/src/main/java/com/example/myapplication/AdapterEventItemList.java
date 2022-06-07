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

public class AdapterEventItemList extends ArrayAdapter<EventItemListElement> {

    public AdapterEventItemList(Context context, ArrayList<EventItemListElement> eventArrayList){
        super(context, R.layout.row_event_item, eventArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        EventItemListElement item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_event_item, parent, false);
        }

        TextView tv_itemName = convertView.findViewById(R.id.tv_itemName);
        TextView ownerName = convertView.findViewById(R.id.EILI_ownerName);
        TextView itemCost = convertView.findViewById(R.id.EILI_itemCost);

        tv_itemName.setText(item.itemName);
        ownerName.setText(item.ownerName);
        itemCost.setText(Float.toString(item.itemCost));

        return convertView;
    }

}
