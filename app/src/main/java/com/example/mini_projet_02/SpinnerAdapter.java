package com.example.mini_projet_02;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mini_projet_02.models.Color;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> colorNames;
    private ArrayList<String> colorCodes;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context context, ArrayList<String> colorNames, ArrayList<String> colorCodes) {
        super(context, R.layout.spinner_item, colorNames);
        this.colorNames = colorNames;
        this.colorCodes = colorCodes;
        inflater = LayoutInflater.from(context);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView tv_spinnerItemColorN = convertView.findViewById(R.id.tv_spinnerItemColorN);
        TextView tv_spinnerItemColorC = convertView.findViewById(R.id.tv_spinnerItemColorC);

        tv_spinnerItemColorN.setText(colorNames.get(position));
        tv_spinnerItemColorC.setText(colorCodes.get(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView tv_spinnerItemColorN = convertView.findViewById(R.id.tv_spinnerItemColorN);
        TextView tv_spinnerItemColorC = convertView.findViewById(R.id.tv_spinnerItemColorC);

        tv_spinnerItemColorN.setText(colorNames.get(position));
        tv_spinnerItemColorC.setText(colorCodes.get(position));

        tv_spinnerItemColorC.setBackgroundColor(android.graphics.Color.parseColor(colorCodes.get(position)));
        return convertView;
    }
}

