package com.example.saveMoneyHelper;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.firebase.FirebaseElement;
import com.example.saveMoneyHelper.firebase.FirebaseObserver;
import com.example.saveMoneyHelper.firebase.factories.TopWalletEntriesViewModelFactory;
import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.example.saveMoneyHelper.settings.PreferencesManager;
import com.example.saveMoneyHelper.settings.UserSettings;
import com.example.saveMoneyHelper.util.CalendarHelper;
import com.example.saveMoneyHelper.util.CategoriesHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Overview extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private BarChart chart;
    private LineChart lineChart;
    private UserSettings userSettings;
    private Calendar dateBegin;
    private Calendar dateEnd;
    private ListDataSet<WalletEntry> walletEntryListDataSet;
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


        lineChart = view.findViewById(R.id.chartLine);
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawBorders(false);

        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setDrawGridLines(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        chart.getAxisLeft().setDrawGridLines(false);

        // setting data
        seekBarX.setProgress(10);
        seekBarY.setProgress(30);
        seekBarX.setMax(1000);


        // add a nice and smooth animation
        chart.animateY(1500);
        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);


        //MONTHLY BY DEFAULT
        userSettings = new UserSettings();

        if (PreferencesManager.getInstance().getSavedUserSettings(getContext()) != null) {

            dateBegin = CalendarHelper.getStartDate(PreferencesManager.getInstance().getSavedUserSettings(getContext()));
            dateEnd = CalendarHelper.getEndDate(PreferencesManager.getInstance().getSavedUserSettings(getContext()));


        } else {
            dateBegin = CalendarHelper.getStartDate(userSettings);
            dateEnd = CalendarHelper.getEndDate(userSettings);


        }



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = view.findViewById(R.id.spinner2);

        String [] names = {"Top Despesas","Balanço", "Top Ganhos"};
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,names);


        //Setting month filter for top 10 expenses
        TopWalletEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getActivity()).setDateFilter(dateBegin, dateEnd);
        //Observer for TopWalletEntries
        TopWalletEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getActivity()).observe(this,
                new FirebaseObserver<FirebaseElement<ListDataSet<WalletEntry>>>() {

                    @Override
                    public void onChanged(FirebaseElement<ListDataSet<WalletEntry>> firebaseElement) {
                        if (firebaseElement.hasNoError()) {
                            Overview.this.walletEntryListDataSet = firebaseElement.getElement();
                            dataUpdated();

                        }
                    }

                });

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        chart.setVisibility(View.VISIBLE);
                        lineChart.setVisibility(View.INVISIBLE);

                        break;
                    case 1:

                        chart.setVisibility(View.INVISIBLE);
                        lineChart.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        chart.setVisibility(View.INVISIBLE);
                        lineChart.setVisibility(View.INVISIBLE);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void dataUpdated() {
        if (walletEntryListDataSet != null) {
            List<WalletEntry> entryList = new ArrayList<>(walletEntryListDataSet.getList());

            long expensesSumInDateRange = 0;
            long incomesSumInDateRange = 0;

            HashMap<Category, Long> categoryModels = new HashMap<>();
            for (WalletEntry walletEntry : entryList) {

                if (walletEntry.balanceDifference > 0) {

                    incomesSumInDateRange += walletEntry.balanceDifference;
                    continue;
                }
                expensesSumInDateRange += walletEntry.balanceDifference;
                Category category = CategoriesHelper.searchCategory(walletEntry.categoryID);

                if (categoryModels.get(category) != null)
                    categoryModels.put(category, categoryModels.get(category) + walletEntry.balanceDifference);
                else
                    categoryModels.put(category, walletEntry.balanceDifference);

            }
            ArrayList<BarEntry> values = new ArrayList<>();
            ArrayList<Integer> chartColors = new ArrayList<>();
            int counter = 0;
            for (Map.Entry<Category, Long> categoryModel : categoryModels.entrySet()) {

                counter++;
                float percentage = categoryModel.getValue() / (float) expensesSumInDateRange;
                final float minPercentageToShowLabelOnChart = 0.01f;
                if(counter<11 && percentage>minPercentageToShowLabelOnChart){

                    Drawable drawable = getContext().getDrawable(categoryModel.getKey().getIconResourceID());
                    drawable.setTint(Color.parseColor("#000000"));
                    values.add(new BarEntry(counter,-categoryModel.getValue(),drawable));


                    chartColors.add(categoryModel.getKey().getIconColor());
                }

            }
            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    editTextValor.setText(String.valueOf(e.getY()));

                }

                @Override
                public void onNothingSelected() {

                }
            });

            BarDataSet set1;
            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values);

                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(values, "Data Set");
                set1.setColors(ColorTemplate.VORDIPLOM_COLORS);

                set1.setDrawValues(true);

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                chart.setDrawGridBackground(false);
                chart.setDrawBarShadow(false);
                chart.setHighlightFullBarEnabled(true);

                chart.setData(data);
                chart.setFitBars(true);

            }
            chart.invalidate();

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            for (int z = 0; z < 2; z++) {

                ArrayList<Entry> valuesLine = new ArrayList<>();

                for (int i = 0; i < 30; i++) {
                    double val = (Math.random() * seekBarY.getProgress()) + 3;
                    valuesLine.add(new Entry(i, (float) val));
                }

                LineDataSet d = new LineDataSet(valuesLine, " ");
                d.setLineWidth(3f);

                //d.setCircleColor(color);
                dataSets.add(d);
            }

            // make the first DataSet dashed

            ((LineDataSet) dataSets.get(1)).setColor(Color.RED);
            ((LineDataSet) dataSets.get(0)).setColor(Color.GREEN);

            LineData data = new LineData(dataSets);
            lineChart.setData(data);
            lineChart.animateXY(2000, 2000);
            lineChart.invalidate();

        }

    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



/*
           final int valorOutCome=seekBarX.getProgress();
        int valorIncome=seekBarY.getProgress();

        final ArrayList<BarEntry> values = new ArrayList<>();

        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        editTextValor.setText(String.valueOf(e.getY()));
                        e.setY(seekBar.getProgress());

                        values.add(new BarEntry(e.getX(), seekBarX.getProgress()));
                        chart.getData().notifyDataChanged();
                        chart.notifyDataSetChanged();
                        // values.add(new BarEntry(1, valorOutCome));
                    }

                    @Override
                    public void onNothingSelected() {

                    }
                });
            }
        });

        BarDataSet set1;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);

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
        */



    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
