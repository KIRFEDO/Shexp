package com.example.myapplication;

import android.content.Context;
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


public class AdapterUserList extends ArrayAdapter<UserListItem> {

    public interface OnAddListener {
        public void onAdd(int position, String text);
    }
    private OnAddListener onAddListener;
    public void setOnAddListener(OnAddListener listener) {
        this.onAddListener = listener;
    }

    public AdapterUserList(Context context, ArrayList<UserListItem> eventArrayList){
        super(context, R.layout.row_user, eventArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        UserListItem user = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_user, parent, false);
        }

        ImageView iv_removeUser = convertView.findViewById(R.id.iv_removeUser);
        TextView tv_userName = convertView.findViewById(R.id.tv_userName);

        tv_userName.setText(user.name);
        if(!user.remove_option) {
            iv_removeUser.setVisibility(View.INVISIBLE);
        } else {
            iv_removeUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAddListener.onAdd(position, user.name);
                }
            });
        }

        return convertView;
    }

}