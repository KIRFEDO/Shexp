package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class AdapterUserList extends ArrayAdapter<HelperUser> {

    public interface OnAddListener {
        public void onAdd(int position, String text);
    }
    private OnAddListener onAddListener;
    public void setOnAddListener(OnAddListener listener) {
        this.onAddListener = listener;
    }

    public AdapterUserList(Context context, ArrayList<HelperUser> eventArrayList){
        super(context, R.layout.row_user, eventArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        HelperUser user = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_user, parent, false);
        }

        ImageView iv_removeUser = convertView.findViewById(R.id.iv_removeUser);
        ImageView iv_avatar = convertView.findViewById(R.id.iv_avatar);
        switch (user.avatarId){
            case 1:
                iv_avatar.setImageResource(R.drawable.avatar_1);
                break;
            case 2:
                iv_avatar.setImageResource(R.drawable.avatar_2);
                break;
        }

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
