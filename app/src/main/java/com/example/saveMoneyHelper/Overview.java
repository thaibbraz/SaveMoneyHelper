package com.example.saveMoneyHelper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class Overview extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private BarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView editTextTempo, editTextValor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        editTextValor = view.findViewById(R.id.editTextValor);
        editTextTempo = view.findViewById(R.id.editTextTempo);

        seekBarX = view.findViewById(R.id.seekBar1);
        seekBarX.setOnSeekBarChangeListener(this);

        seekBarY = view.findViewById(R.id.seekBar2);
        seekBarY.setOnSeekBarChangeListener(this);

        chart = view.findViewById(R.id.chart1);

        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(100);

        chart.setDrawBarShadow(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        chart.getAxisLeft().setDrawGridLines(false);

        // setting data
        seekBarX.setProgress(4);
        seekBarY.setProgress(0);

        seekBarX.setMax(48);

        // add a nice and smooth animation
        chart.animateY(1500);
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = view.findViewById(R.id.spinner2);

        String [] names = {"Simulação financeira ","SaveMoneyHelper"};
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,names);

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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int tempo=seekBarX.getProgress();
        int valor=seekBarY.getProgress();
        ArrayList<BarEntry> values = new ArrayList<>();
        float ganho=0;
        for (int i = 0; i < 3; i++){
            //y = ganhos | x = meses
            int ganhoMensal=seekBarY.getProgress();
            ganho = ganho + ganhoMensal;
           // float multi = (seekBarY.getProgress() * 12 );
            //float val = multi;
            values.add(new BarEntry(i, ganho));
        }
        editTextTempo.setText(String.valueOf(tempo));
        editTextValor.setText(String.valueOf(valor));
        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            chart.setData(data);
            chart.setFitBars(true);
        }

        chart.invalidate();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
