package com.example.saveMoneyHelper;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;

import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.saveMoneyHelper.auth.EditProfileActivity;
import com.example.saveMoneyHelper.auth.RegisterActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;



public class HomePage extends Fragment {
    AnyChartView chartView;
    FloatingActionButton btnProfile;
    ProgressBar progressBar;
    int day,month,year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        chartView = view.findViewById(R.id.any_chart_view);
        chartView.setProgressBar(view.findViewById(R.id.progress_bar));
        btnProfile = view.findViewById(R.id.btn_floatingProfile);
        progressBar = view.findViewById(R.id.progress_bar);
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> lazer = new ArrayList<>();
        lazer.add(new ValueDataEntry("o", 150));
        Column columnLazer = cartesian.column(lazer);
        columnLazer.color("#6AB09A");

        columnLazer.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(4d)
                .format("${%Value}{groupsSeparator: }");


        List<DataEntry> roupas = new ArrayList<>();
        roupas.add(new ValueDataEntry("u", 140));
        roupas.add(new ValueDataEntry("t", 70));
        Column columnRoupas = cartesian.column(roupas);
        columnRoupas.color("#1a1aff");

        columnRoupas.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(4d)
                .format("${%Value}{groupsSeparator: }");

        List<DataEntry> alimentacao = new ArrayList<>();
        alimentacao.add(new ValueDataEntry("x", 40));
        alimentacao.add(new ValueDataEntry("y", 70));
        alimentacao.add(new ValueDataEntry("z", 50));
        Column columnAlimentacao = cartesian.column(alimentacao);
        columnAlimentacao.color("#e066ff");

        columnAlimentacao.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(4d)
                .format("${%Value}{groupsSeparator: }");

        List<DataEntry> despesas = new ArrayList<>();
        despesas.add(new ValueDataEntry("e", 50));
        Column columnDespesas = cartesian.column(despesas);
        columnDespesas.color("#4dd2ff");


        columnDespesas.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(4d)
                .format("${%Value}{groupsSeparator: }");


        cartesian.animation(true);


        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Categoria");
      //  cartesian.yAxis(0).title("Valor");

        cartesian.barGroupsPadding(0);
        chartView.setChart(cartesian);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditProfileActivity.class);
                startActivity(i);

            }
        });


        return view;
    }

}

