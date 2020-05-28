package com.example.saveMoneyHelper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HomePage extends Fragment {
    AnyChartView chartView;
    int day,month,year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        chartView = view.findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Rouge", 80540));
        data.add(new ValueDataEntry("Foundation", 94190));
        data.add(new ValueDataEntry("Mascara", 102610));
        data.add(new ValueDataEntry("Lip gloss", 110430));
        data.add(new ValueDataEntry("Lipstick", 128000));
        data.add(new ValueDataEntry("Nail polish", 143760));
        data.add(new ValueDataEntry("Eyebrow pencil", 170670));
        data.add(new ValueDataEntry("Eyeliner", 213210));
        data.add(new ValueDataEntry("Eyeshadows", 249980));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Top 10 Cosmetic Products by Revenue");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Revenue");

        AnyChartView anyChartView = (AnyChartView) chartView;
        anyChartView.setChart(cartesian);



        return view;
    }
/*
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH+1);
        year = Calendar.getInstance().get(Calendar.YEAR);

        String date = String.format(Locale.getDefault(),"%d/%d/%d", day, month, year);

        List<DataEntry> dataEntries = new ArrayList<>();
        dataEntries.add(new CustomEntry( String.format(Locale.getDefault(),"%d/%d/%d", 1, 5, 2020),3,2));
        dataEntries.add(new CustomEntry( String.format(Locale.getDefault(),"%d/%d/%d", 5, 5, 2020),5,7));
        dataEntries.add(new CustomEntry( String.format(Locale.getDefault(),"%d/%d/%d", 10, 5, 2020),8,2));


        chartView.setChart(makeCartesianManagement(dataEntries));
    }

    private Cartesian makeCartesianManagement(List<DataEntry> dataEntries) {
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.crosshair().enabled(false);
        cartesian.title(getResources().getString(R.string.welcome));


        Set set = Set.instantiate();
        set.data(dataEntries);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'cases' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'death' }");

        Line series1 = cartesian.line(series2Mapping);
        series1.name("Teste");


        series1.hovered().markers().enabled(true);
        series1.color("#6AB09A");
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series1Mapping);
        series2.name("Teste2");

        series2.hovered().markers().enabled(true);
        series2.color("#DF3C58");
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.RIGHT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        return cartesian;
    }

    private class CustomEntry extends ValueDataEntry {

        public CustomEntry(String x, Number valor1, Number valor2) {
            super(x,valor1);

            setValue("valor1", valor1);
            setValue("valor2", valor2);

        }
        */

}

