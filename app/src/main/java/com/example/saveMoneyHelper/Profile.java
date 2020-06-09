package com.example.saveMoneyHelper;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Profile extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
/*

        try {


            Spinner spinner = view.findViewById(R.id.spinner2);
            String [] values =
                    {"Time at Residence","Under 6 months","6-12 months","1-2 years","2-4 years","4-8 years","8-15 years","Over 15 years"};
            ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.fragment_profile,values);

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);
        }catch (Exception ex){
            Log.d("profile: ",ex.getLocalizedMessage());
        }
    */

        final Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Apples", 6371664));
        data.add(new ValueDataEntry("Pears", 789622));
        data.add(new ValueDataEntry("Bananas", 7216301));
        data.add(new ValueDataEntry("Grapes", 1486621));
        data.add(new ValueDataEntry("Oranges", 1200000));

        pie.data(data);

        pie.title("Fruits imported in 2015 (in kg)");

        anyChartView.setChart(pie);


        final AnyChartView anyChartView1 = view.findViewById(R.id.any_chart_view1);
        APIlib.getInstance().setActiveAnyChartView(anyChartView1);

        final Pie pie1 = AnyChart.pie();

        pie1.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data1 = new ArrayList<>();
        data1.add(new ValueDataEntry("Apples", 6371664));
        data1.add(new ValueDataEntry("Pears", 789622));
        data1.add(new ValueDataEntry("Bananas", 7216301));
        data1.add(new ValueDataEntry("Grapes", 1486621));
        data1.add(new ValueDataEntry("Oranges", 1200000));

        pie1.data(data1);

        pie1.title("Fruits imported in 2015 (in kg)");

        anyChartView1.setChart(pie1);

        AnyChartView anyChartView2 = view.findViewById(R.id.any_chart_view2);
        APIlib.getInstance().setActiveAnyChartView(anyChartView2);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data2 = new ArrayList<>();
        data2.add(new ValueDataEntry("Rouge", 80540));
        data2.add(new ValueDataEntry("Foundation", 94190));
        data2.add(new ValueDataEntry("Mascara", 102610));
        data2.add(new ValueDataEntry("Lip gloss", 110430));
        data2.add(new ValueDataEntry("Lipstick", 128000));
        data2.add(new ValueDataEntry("Nail polish", 143760));
        data2.add(new ValueDataEntry("Eyebrow pencil", 170670));
        data2.add(new ValueDataEntry("Eyeliner", 213210));
        data2.add(new ValueDataEntry("Eyeshadows", 249980));

        Column column = cartesian.column(data2);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.title("Top 10 Cosmetic Products by Revenue");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Revenue");

        anyChartView2.setChart(cartesian);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = view.findViewById(R.id.spinner2);

        String [] values = {"Time at Residence","Under 6 months","6-12 months","1-2 years","2-4 years","4-8 years","8-15 years","Over 15 years"};
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,values);

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
