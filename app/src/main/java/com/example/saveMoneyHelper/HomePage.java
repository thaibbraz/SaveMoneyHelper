package com.example.saveMoneyHelper;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.example.saveMoneyHelper.auth.EditProfileActivity;

import com.example.saveMoneyHelper.firebase.models.FirebaseElement;
import com.example.saveMoneyHelper.firebase.models.FirebaseObserver;
import com.example.saveMoneyHelper.firebase.models.TopWalletEntriesViewModelFactory;
import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.example.saveMoneyHelper.models.Category;
import com.example.saveMoneyHelper.util.CategoriesHelper;
import com.example.saveMoneyHelper.util.CurrencyHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomePage extends Fragment {

    private PieChart pieChart;
    private FloatingActionButton btnOverview;
    private ProgressBar progressbar_income_expense;
    private TextView incomesTextView;
    private TextView balance;
    private ListDataSet<WalletEntry> walletEntryListDataSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        btnOverview = view.findViewById(R.id.btn_floatingProfile);
        pieChart = view.findViewById(R.id.pie_chart);

        progressbar_income_expense = view.findViewById(R.id.progress_bar);
        balance = view.findViewById(R.id.balance);


        TopWalletEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), getActivity()).observe(this,
                new FirebaseObserver<FirebaseElement<ListDataSet<WalletEntry>>>() {

            @Override
            public void onChanged(FirebaseElement<ListDataSet<WalletEntry>> firebaseElement) {
                if (firebaseElement.hasNoError()) {
                    HomePage.this.walletEntryListDataSet = firebaseElement.getElement();
                    dataUpdated();
                }
            }

        });


        btnOverview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), EditProfileActivity.class);
                startActivity(i);

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

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            ArrayList<Integer> pieColors = new ArrayList<>();

            for (Map.Entry<Category, Long> categoryModel : categoryModels.entrySet()) {
                float percentage = categoryModel.getValue() / (float) expensesSumInDateRange;
                final float minPercentageToShowLabelOnChart = 0.1f;

                if (percentage > minPercentageToShowLabelOnChart) {
                    Drawable drawable = getContext().getDrawable(categoryModel.getKey().getIconResourceID());
                    drawable.setTint(Color.parseColor("#FFFFFF"));
                    pieEntries.add(new PieEntry(-categoryModel.getValue(), drawable));

                } else {
                    pieEntries.add(new PieEntry(-categoryModel.getValue()));
                }
                pieColors.add(categoryModel.getKey().getIconColor());
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setDrawValues(false);
            pieDataSet.setColors(pieColors);
            pieDataSet.setSliceSpace(2f);

            PieData data = new PieData(pieDataSet);
            pieChart.setData(data);
            pieChart.setTouchEnabled(false);
            pieChart.getLegend().setEnabled(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setUsePercentValues(true);

            pieChart.setDragDecelerationFrictionCoef(0.95f);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(ContextCompat.getColor(getContext(), R.color.design_default_color_background));

            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);

            pieChart.setHoleRadius(58f);
            pieChart.setTransparentCircleRadius(61f);
            pieChart.setDrawCenterText(true);
            pieChart.setRotationAngle(270);
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);

            pieChart.animateY(1400, Easing.EaseInOutQuad);



            pieChart.invalidate();
            System.out.println("income: "+incomesSumInDateRange+ " outcome "+expensesSumInDateRange);
            float progress = 100 * incomesSumInDateRange / (float) (incomesSumInDateRange - expensesSumInDateRange);
            progressbar_income_expense.setProgress((int) progress);

            balance.setText(String.valueOf(progress));

        }


    }
}

