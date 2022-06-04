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

public class AdapterTransactionList extends ArrayAdapter<TransactionListElement> {

    public AdapterTransactionList(Context context, ArrayList<TransactionListElement> transactionList){
        super(context, R.layout.row_transaction, transactionList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        TransactionListElement transaction = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_transaction, parent, false);
        }

        TextView tv_from = convertView.findViewById(R.id.tv_from);
        TextView tv_to = convertView.findViewById(R.id.tv_to);
        TextView tv_amount = convertView.findViewById(R.id.tv_amount);

        tv_from.setText("From: "+transaction.from);
        tv_to.setText("To: "+transaction.to);
        tv_amount.setText(Float.toString(transaction.amount));

        return convertView;
    }

}
