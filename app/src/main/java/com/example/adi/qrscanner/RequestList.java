package com.example.adi.qrscanner;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class RequestList extends ArrayAdapter<Request> {
    Activity context;
    List<Request> requestList;


    public RequestList(Activity context, List<Request> requestList) {
        super(context, R.layout.request_layout , requestList);
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.request_layout, null, true);
        TextView name = listViewItem.findViewById(R.id.name);
        TextView type = listViewItem.findViewById(R.id.type);

        Request request = requestList.get(position);

        name.setText(request.getEmail());
        type.setText(!request.isType() ? "User" : "Admin");

        return listViewItem;
    }
}
