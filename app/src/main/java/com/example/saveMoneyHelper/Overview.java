package com.example.saveMoneyHelper;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;

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


public class Overview extends Fragment{

    private BarChart chart, chartWants,chartNeeds;
    private LineChart lineChart;
    private UserSettings userSettings;
    private Calendar dateBegin;
    private Button btn_resetar,btn_simular;
    private Calendar dateEnd;
    private Spinner spinner;
    private CardView wantsCard,needsCard;
    private ArrayList<ILineDataSet> dataSets;
    private ArrayList<BarEntry> values, valuesNeeds, valuesWants;
    private ArrayList<Entry> valuesLine;
    private ArrayList<Integer> chartColors;
    private ListDataSet<WalletEntry> walletEntryListDataSet;
    private SeekBar seekBarX, seekBarY;
    private TextView editTextTempo, editTextValor, tempo,valor,valorTittle, tempoTittle,titulo;
    private int flag=-1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        editTextValor = view.findViewById(R.id.editTextValor);
        editTextTempo = view.findViewById(R.id.editTextTempo);

        tempo = view.findViewById(R.id.tvTempoMax);
        valor = view.findViewById(R.id.tvValorMax);

        titulo = view.findViewById(R.id.titulo);
        valorTittle = view.findViewById(R.id.valor);
        tempoTittle = view.findViewById(R.id.durante);

        seekBarX = view.findViewById(R.id.seekBar1);
        seekBarY = view.findViewById(R.id.seekBar2);

        chart = view.findViewById(R.id.chart1);
        btn_simular = view.findViewById(R.id.btn_simular);
        btn_resetar = view.findViewById(R.id.btn_resetar);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(100);

        chart.setDrawBarShadow(false);

        chartNeeds = view.findViewById(R.id.chartNeeds);
        chartWants = view.findViewById(R.id.chartWants);

        needsCard = view.findViewById(R.id.cardViewNeeds);
        wantsCard = view.findViewById(R.id.cardViewWants);

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
        spinner = view.findViewById(R.id.spinner2);
        values = new ArrayList<>();
        valuesNeeds = new ArrayList<>();
        valuesWants = new ArrayList<>();

        chartColors = new ArrayList<>();
        chartColors.clear();

        String [] names = {"Top Despesas","Top Ganhos", "Simulador do Tesouro poupança","Simulador de Certificados de Aforro (série e)","Overview"};
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
                        flag=1;
                        dataUpdated();
                        chart.setVisibility(View.VISIBLE);
                        lineChart.setVisibility(View.INVISIBLE);

                        editTextValor.setVisibility(View.INVISIBLE);
                        editTextTempo.setVisibility(View.INVISIBLE);

                        valorTittle.setVisibility(View.INVISIBLE);
                        tempoTittle.setVisibility(View.INVISIBLE);

                        seekBarX.setVisibility(View.INVISIBLE);
                        seekBarY.setVisibility(View.INVISIBLE);
                        btn_resetar.setVisibility(View.INVISIBLE);
                        btn_simular.setVisibility(View.INVISIBLE);


                        needsCard.setVisibility(View.INVISIBLE);
                        wantsCard.setVisibility(View.INVISIBLE);
                        chartWants.setVisibility(View.INVISIBLE);
                        chartNeeds.setVisibility(View.INVISIBLE);


                        tempo.setVisibility(View.INVISIBLE);
                        valor.setVisibility(View.INVISIBLE);
                        titulo.setVisibility(View.VISIBLE);
                        titulo.setText("Top Despesas");
                        chart.setClickable(false);
                        break;
                    case 1:
                        flag=2;
                        dataUpdated();
                        chart.setVisibility(View.VISIBLE);
                        lineChart.setVisibility(View.INVISIBLE);

                        editTextValor.setVisibility(View.INVISIBLE);
                        editTextTempo.setVisibility(View.INVISIBLE);

                        valorTittle.setVisibility(View.INVISIBLE);
                        tempoTittle.setVisibility(View.INVISIBLE);

                        seekBarX.setVisibility(View.INVISIBLE);
                        seekBarY.setVisibility(View.INVISIBLE);
                        btn_resetar.setVisibility(View.INVISIBLE);
                        btn_simular.setVisibility(View.INVISIBLE);

                        tempo.setVisibility(View.INVISIBLE);
                        valor.setVisibility(View.INVISIBLE);
                        titulo.setVisibility(View.VISIBLE);


                        needsCard.setVisibility(View.INVISIBLE);
                        wantsCard.setVisibility(View.INVISIBLE);
                        chartWants.setVisibility(View.INVISIBLE);
                        chartNeeds.setVisibility(View.INVISIBLE);

                        titulo.setText("Top Ganhos");
                        chart.setClickable(false);

                        break;
                    case 2:
                        flag=3;
                        dataUpdated();
                        chart.setVisibility(View.VISIBLE);
                        lineChart.setVisibility(View.INVISIBLE);

                        valorTittle.setVisibility(View.VISIBLE);
                        tempoTittle.setVisibility(View.VISIBLE);

                        editTextValor.setVisibility(View.VISIBLE);
                        editTextTempo.setVisibility(View.VISIBLE);

                        seekBarX.setVisibility(View.VISIBLE);
                        seekBarY.setVisibility(View.VISIBLE);
                        btn_resetar.setVisibility(View.VISIBLE);
                        btn_simular.setVisibility(View.VISIBLE);

                        needsCard.setVisibility(View.INVISIBLE);
                        wantsCard.setVisibility(View.INVISIBLE);
                        chartWants.setVisibility(View.INVISIBLE);
                        chartNeeds.setVisibility(View.INVISIBLE);


                        tempo.setVisibility(View.VISIBLE);
                        valor.setVisibility(View.VISIBLE);
                        titulo.setVisibility(View.INVISIBLE);
                        chart.setClickable(false);
                        break;
                    case 3:
                        flag=4;
                        dataUpdated();
                        chart.setVisibility(View.VISIBLE);
                        lineChart.setVisibility(View.INVISIBLE);

                        valorTittle.setVisibility(View.VISIBLE);
                        tempoTittle.setVisibility(View.VISIBLE);

                        editTextValor.setVisibility(View.VISIBLE);
                        editTextTempo.setVisibility(View.VISIBLE);

                        seekBarX.setVisibility(View.VISIBLE);
                        seekBarY.setVisibility(View.VISIBLE);
                        btn_resetar.setVisibility(View.VISIBLE);
                        btn_simular.setVisibility(View.VISIBLE);

                        needsCard.setVisibility(View.INVISIBLE);
                        wantsCard.setVisibility(View.INVISIBLE);
                        chartWants.setVisibility(View.INVISIBLE);
                        chartNeeds.setVisibility(View.INVISIBLE);


                        tempo.setVisibility(View.VISIBLE);
                        valor.setVisibility(View.VISIBLE);
                        titulo.setVisibility(View.INVISIBLE);
                        chart.setClickable(false);
                        break;
                    case 4:
                        flag=5;
                        dataUpdated();
                        chart.setVisibility(View.INVISIBLE);
                        lineChart.setVisibility(View.INVISIBLE);

                        editTextValor.setVisibility(View.INVISIBLE);
                        editTextTempo.setVisibility(View.INVISIBLE);

                        valorTittle.setVisibility(View.INVISIBLE);
                        tempoTittle.setVisibility(View.INVISIBLE);

                        seekBarX.setVisibility(View.INVISIBLE);
                        seekBarY.setVisibility(View.INVISIBLE);
                        btn_resetar.setVisibility(View.INVISIBLE);
                        btn_simular.setVisibility(View.INVISIBLE);

                        needsCard.setVisibility(View.VISIBLE);
                        wantsCard.setVisibility(View.VISIBLE);
                        chartWants.setVisibility(View.VISIBLE);
                        chartNeeds.setVisibility(View.VISIBLE);

                        tempo.setVisibility(View.INVISIBLE);
                        valor.setVisibility(View.INVISIBLE);
                        titulo.setVisibility(View.VISIBLE);
                        titulo.setText("Overview");
                        chart.setClickable(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarX.setMin(1000);
                seekBarX.setMax(10000);
                editTextValor.setText(String.valueOf(seekBarX.getProgress()+"€"));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarY.setMin(1);
                seekBarY.setMax(10);

                if (seekBarY.getProgress()>1)
                    editTextTempo.setText(String.valueOf(seekBarY.getProgress()+" anos"));
                else
                    editTextTempo.setText(String.valueOf(seekBarY.getProgress()+" ano"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_simular.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    values.clear();
                    for(int i = 0; i<=seekBarY.getProgress();i++){

                      // values.add(new BarEntry((int)i,seekBarX.getProgress()*seekBarY.getProgress()*12));
                        if (flag==3){
                          values.add(new BarEntry((int)i, (float) (seekBarX.getProgress()+ (seekBarX.getProgress()*(0.0125+0.005*i))*0.72)));

                        }
                       //values.add(new BarEntry((int)i, (seekBarX.getProgress())));
                        if (flag==4){
                          values.add(new BarEntry((int)i, (float) (seekBarX.getProgress()+ (seekBarX.getProgress()*(0.0125+0.0044*i))*0.72)));
                        }
                    }

                    BarDataSet set1;
                    if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                        set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                        set1.setValues(values);

                        chart.getData().notifyDataChanged();
                        chart.notifyDataSetChanged();
                    } else {
                        set1 = new BarDataSet(values, "");

                        set1.setDrawValues(false);
                        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);

                        BarData data = new BarData(dataSets);
                        set1.setValueTextColor(Color.WHITE);
                        chart.setData(data);

                    }
                    set1.setColors(ColorTemplate.VORDIPLOM_COLORS);

                chart.setDrawBarShadow(false);
                chart.setDoubleTapToZoomEnabled(false);
                chart.setHighlightFullBarEnabled(false);
                chart.setFitBars(true);


                    chart.invalidate();



            }
        });


    }


    private void dataUpdated() {
        if (walletEntryListDataSet != null) {
            List<WalletEntry> entryList = new ArrayList<>(walletEntryListDataSet.getList());

            long expensesSumInDateRange = 0;
            long incomesSumInDateRange = 0;
            long needs = 0;
            long wants = 0;
            HashMap<Category, Long> categoryModels = new HashMap<>();
            HashMap<Category, Long> categoryModelsNeeds = new HashMap<>();
            HashMap<Category, Long> categoryModelsWants = new HashMap<>();
            for (WalletEntry walletEntry : entryList) {

                if (walletEntry.balanceDifference > 0) {
                    incomesSumInDateRange += walletEntry.balanceDifference;

                }
                expensesSumInDateRange += walletEntry.balanceDifference;
                Category category = CategoriesHelper.searchCategory(walletEntry.categoryID);

                if (walletEntry.type != null){
                    if (walletEntry.type.contains("needs")){
                        needs += walletEntry.balanceDifference;
                        if (categoryModelsNeeds.get(category) != null)
                            categoryModelsNeeds.put(category, categoryModels.get(category) + walletEntry.balanceDifference);
                        else
                            categoryModelsNeeds.put(category, walletEntry.balanceDifference);
                    }else{
                        wants += walletEntry.balanceDifference;
                        if (categoryModelsWants.get(category) != null)
                            categoryModelsWants.put(category, categoryModels.get(category) + walletEntry.balanceDifference);
                        else
                            categoryModelsWants.put(category, walletEntry.balanceDifference);
                    }

                }


                if (categoryModels.get(category) != null)
                    categoryModels.put(category, categoryModels.get(category) + walletEntry.balanceDifference);
                else
                    categoryModels.put(category, walletEntry.balanceDifference);

            }
            Drawable drawable = null;

            int counter = 0;
            values.clear();
            chartColors.clear();
            for (Map.Entry<Category, Long> categoryModel : categoryModels.entrySet()) {
                drawable = getContext().getDrawable(categoryModel.getKey().getIconResourceID());
                drawable.setTint(Color.parseColor("#000000"));

                if (flag == 1 && categoryModel.getValue()<=0) {

                    values.add(new BarEntry(counter, -categoryModel.getValue(), drawable));
                    chartColors.add(categoryModel.getKey().getIconColor());

                }
                if (flag == 2 && categoryModel.getValue()>0){

                    values.add(new BarEntry(counter,categoryModel.getValue(),drawable));
                    chartColors.add(categoryModel.getKey().getIconColor());
                }

                counter++;

            }


            for (Map.Entry<Category, Long> categoryModel : categoryModelsNeeds.entrySet()) {
                drawable = getContext().getDrawable(categoryModel.getKey().getIconResourceID());
                drawable.setTint(Color.parseColor("#000000"));

                if (flag == 5) {

                    valuesNeeds.add(new BarEntry(counter, -categoryModel.getValue(), drawable));
                    chartColors.add(categoryModel.getKey().getIconColor());

                }

                counter++;

            }
            for (Map.Entry<Category, Long> categoryModel : categoryModelsWants.entrySet()) {
                drawable = getContext().getDrawable(categoryModel.getKey().getIconResourceID());
                drawable.setTint(Color.parseColor("#000000"));

                if (flag == 5) {

                    valuesWants.add(new BarEntry(counter, -categoryModel.getValue(), drawable));
                    chartColors.add(categoryModel.getKey().getIconColor());

                }

                counter++;

            }



            BarDataSet setNeeds,setWants;
            if (chartNeeds.getData() != null && chartNeeds.getData().getDataSetCount() > 0) {
                setNeeds = (BarDataSet) chartNeeds.getData().getDataSetByIndex(0);
                setNeeds.setValues(valuesNeeds);

                chartNeeds.getData().notifyDataChanged();
                chartNeeds.notifyDataSetChanged();
            } else {
                setNeeds = new BarDataSet(valuesNeeds, "");

                setNeeds.setDrawValues(false);
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(setNeeds);

                BarData data = new BarData(dataSets);
                setNeeds.setValueTextColor(Color.WHITE);
                chartNeeds.setData(data);

            }
            chartNeeds.setDrawBarShadow(false);
            chartNeeds.setDoubleTapToZoomEnabled(false);
            chartNeeds.setHighlightFullBarEnabled(false);
            chartNeeds.setFitBars(true);

            setNeeds.setColors(ColorTemplate.VORDIPLOM_COLORS);
            chartNeeds.invalidate();


            if (chartWants.getData() != null && chartWants.getData().getDataSetCount() > 0) {
                setWants = (BarDataSet) chartWants.getData().getDataSetByIndex(0);
                setWants.setValues(valuesWants);

                chartWants.getData().notifyDataChanged();
                chartWants.notifyDataSetChanged();
            } else {
                setWants = new BarDataSet(valuesWants, "");

                setWants.setDrawValues(false);
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(setWants);

                BarData data = new BarData(dataSets);
                setWants.setValueTextColor(Color.WHITE);
                chartWants.setData(data);

            }
            chartWants.setDrawBarShadow(false);
            chartWants.setDoubleTapToZoomEnabled(false);
            chartWants.setHighlightFullBarEnabled(false);
            chartWants.setFitBars(true);

            setWants.setColors(ColorTemplate.VORDIPLOM_COLORS);
            chartWants.invalidate();


            btn_resetar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    values.clear();
                    seekBarX.setProgress(100);
                    seekBarY.setProgress(1);
                    chart.invalidate();

                }
            });

            BarDataSet set1;
            if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values);

                set1.setColors(chartColors);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(values, "");

                set1.setDrawValues(false);
                set1.setColors(chartColors);

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                set1.setValueTextColor(Color.WHITE);


              //  set1.setFormLineDashEffect(new DashPathEffect(1f,1f));
                chart.setDrawBarShadow(false);
                chart.setDoubleTapToZoomEnabled(false);
                chart.setHighlightFullBarEnabled(false);
                chart.setData(data);
                chart.setFitBars(true);




            }
            chart.invalidate();
/*
            dataSets = new ArrayList<>();

            for (int z = 0; z <2; z++) {
                valuesLine = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    for (Map.Entry<Category, Long> categoryModel : categoryModels.entrySet()) {
                        double val = i * seekBarX.getProgress();
                        valuesLine.add(new Entry(i, categoryModel.getValue()));
                    }
                }
                LineDataSet d = new LineDataSet(valuesLine, " ");
                d.setLineWidth(5f);

                //d.setCircleColor(color);
                dataSets.add(d);
            }
              // make the first DataSet dashed
            ((LineDataSet) dataSets.get(1)).setColor(Color.GREEN);
            ((LineDataSet) dataSets.get(0)).setColor(Color.GREEN);

            LineData data = new LineData(dataSets);
            lineChart.setData(data);
            lineChart.animateXY(2000, 2000);
            lineChart.invalidate();
            */

        }


    }




}
