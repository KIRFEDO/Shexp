package com.example.myapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageView iv_avatar = convertView.findViewById(R.id.iv_avatar);

        eventName.setText(event.eventName);
        ownerName.setText(event.ownerName);
        switch (event.ownerAvatarId){
            case 1:
                iv_avatar.setImageResource(R.drawable.avatar_1);
                break;
            case 2:
                iv_avatar.setImageResource(R.drawable.avatar_2);
                break;
        }

        return convertView;
    }

}
