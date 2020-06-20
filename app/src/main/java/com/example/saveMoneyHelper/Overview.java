package com.example.saveMoneyHelper;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


public class Overview extends Fragment {

    private BarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_overview, container, false);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = view.findViewById(R.id.spinner2);




        String [] values = {"Simulação financeira ","SaveMoneyHelper"};
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,values);

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
                    switch (position){
                        case 0:
                            //simulacao 1
                            break;
                        case 1:
                            //simulacao 2
                            break;


                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
