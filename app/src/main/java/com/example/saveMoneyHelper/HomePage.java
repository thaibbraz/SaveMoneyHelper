package com.example.saveMoneyHelper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.example.saveMoneyHelper.auth.ProfileEditActivity;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.categories.TopCategoriesAdapter;
import com.example.saveMoneyHelper.categories.TopCategoryListViewModel;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;


public class HomePage extends Fragment {

    private PieChart pieChart;
    private UserSettings userSettings;
    private ImageButton btnFloattingProfile;
    private ProgressBar progressbar_income_expense, background_progress_bar;
    private TextView balance;
    private Calendar dateBegin;
    private Calendar dateEnd;
    private ImageButton img_btn,reward_btn;
    private int progress;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private ListView favoriteListView;

    private ListDataSet<WalletEntry> walletEntryListDataSet;
    private TopCategoriesAdapter adapter;
    private ArrayList<TopCategoryListViewModel> categoryModelsHome;
    private ArrayList<Integer> colors = new ArrayList<>();
    private boolean currentday;
    private static final int[] PIE_COLORS = {
            rgb("#02c39a"), rgb("#028090"), rgb("#b7e4c7")
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        //MONTHLY BY DEFAULT
        userSettings = new UserSettings();

        if (PreferencesManager.getInstance().getSavedUserSettings(getContext()) != null) {


            dateBegin = CalendarHelper.getStartDate(PreferencesManager.getInstance().getSavedUserSettings(getContext()));
            dateEnd = CalendarHelper.getEndDate(PreferencesManager.getInstance().getSavedUserSettings(getContext()));



        } else {
            dateBegin = CalendarHelper.getStartDate(userSettings);
            dateEnd = CalendarHelper.getEndDate(userSettings);
        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        categoryModelsHome = new ArrayList<>();
        btnFloattingProfile = view.findViewById(R.id.img_btn);
        reward_btn = view.findViewById(R.id.img_xp);
        pieChart = view.findViewById(R.id.pie_chart);
        favoriteListView = view.findViewById(R.id.favourite_categories_list_view);
        progressbar_income_expense = view.findViewById(R.id.progress_bar);
        background_progress_bar = view.findViewById(R.id.background_progress_bar);
        balance = view.findViewById(R.id.balance);

        adapter = new TopCategoriesAdapter(categoryModelsHome, getContext());
        favoriteListView.setAdapter(adapter);

        //Profile button

        btnFloattingProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Navigation.findNavController(getView()).navigate(R.id.coinFragment);

            }
        });

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
                            HomePage.this.walletEntryListDataSet = firebaseElement.getElement();
                            dataUpdated();

                        }
                    }

                });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        final String todaystring = year + "" + month + "" + day + "";
        final SharedPreferences timepref = getContext().getSharedPreferences("REWARD", 0);
        currentday = timepref.getBoolean(todaystring, false);

        if (!currentday)
            reward_btn.setImageResource(R.drawable.ic_diamond);
        else
            reward_btn.setImageResource(R.drawable.ic_diamond_done);

        reward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reward_btn.setImageResource(R.drawable.ic_diamond_done);
                System.out.println("XP: "+PreferencesManager.getInstance().getSavedUserSettings(getContext()).getXP());
                System.out.println("getCountXP: "+PreferencesManager.getInstance().getSavedUserSettings(getContext()).getCountXP());
                if (currentday && PreferencesManager.getInstance().getSavedUserSettings(getContext()).getXP() == 1) {
                    if (userSettings != null) {
                        userSettings.setCountXP(PreferencesManager.getInstance().getSavedUserSettings(getContext()).getCountXP()+progress);

                    }else {
                        userSettings = new UserSettings();
                        userSettings.setCountXP(PreferencesManager.getInstance().getSavedUserSettings(getContext()).getCountXP()+progress);

                    }
                    PreferencesManager.getInstance().setUserSettings(getContext(), userSettings);

                    Toast.makeText(getContext(), "Daily reward granted!", Toast.LENGTH_LONG).show();
                    //Daily reward granted
                    SharedPreferences.Editor timedaily = timepref.edit();
                    timedaily.putBoolean(todaystring, true);
                    timedaily.apply();

                    if (Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)==Calendar.getInstance().DAY_OF_MONTH){
                        FirebaseDatabase.getInstance().getReference().child("XP").child(user.getUid()).push().setValue(userSettings.getCountXP());
                    }


                }else{

                    Toast.makeText(getContext(), "Daily reward NOT granted!", Toast.LENGTH_LONG).show();
                }

            }

        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    private void dataUpdated() {

        if (walletEntryListDataSet != null) {
            //list of entries
            List<WalletEntry> entryList = new ArrayList<>(walletEntryListDataSet.getList());

            long expensesSumInDateRange = 0;
            long incomesSumInDateRange = 0;
            long necessidades = 0;
            long extras = 0;
            long poupanças = 0;

            HashMap<String, Long> categoryModels = new HashMap<>();
            for (WalletEntry walletEntry : entryList) {
                if (walletEntry.balanceDifference > 0 && !(walletEntry.categoryID.contains("savings"))) {
                    incomesSumInDateRange += walletEntry.balanceDifference;
                }

                if (walletEntry.categoryID.contains("savings") && walletEntry.balanceDifference > 0) {
                    poupanças += walletEntry.balanceDifference;
                    if (categoryModels.get("poupanças") != null)
                        categoryModels.put("poupanças", categoryModels.get("poupanças") + walletEntry.balanceDifference);
                    else
                        categoryModels.put("poupanças", walletEntry.balanceDifference);

                }

                if (walletEntry.balanceDifference < 0 && walletEntry.type != null) {
                    expensesSumInDateRange += walletEntry.balanceDifference;
                    switch (walletEntry.type) {
                        case "wants":
                            extras += walletEntry.balanceDifference;

                            if (categoryModels.get("extras") != null)
                                categoryModels.put("extras", categoryModels.get("extras") + walletEntry.balanceDifference);
                            else
                                categoryModels.put("extras", walletEntry.balanceDifference);
                            break;
                        case "needs":
                            necessidades += walletEntry.balanceDifference;
                            if (categoryModels.get("necessidades") != null)
                                categoryModels.put("necessidades", categoryModels.get("necessidades") + walletEntry.balanceDifference);
                            else
                                categoryModels.put("necessidades", walletEntry.balanceDifference);
                            break;
                    }


                }

            }

            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            categoryModelsHome.clear();

            int count = 10;
            for (Map.Entry<String, Long> categoryModel : categoryModels.entrySet()) {

                //Populating arrayList<TopCategoryListViewModel>

                if (categoryModel.getValue() < 0) {
                    pieEntries.add(new PieEntry(-categoryModel.getValue(), categoryModel.getKey()));
                } else {
                    pieEntries.add(new PieEntry(categoryModel.getValue(), categoryModel.getKey()));
                }


            }

            Collections.sort(categoryModelsHome, new Comparator<TopCategoryListViewModel>() {
                @Override
                public int compare(TopCategoryListViewModel o1, TopCategoryListViewModel o2) {
                    return Long.compare(o1.getMoney(), o2.getMoney());
                }
            });
            for (int c : PIE_COLORS)
                colors.add(c);

            //Pie chart
            PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setDrawValues(false);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            pieDataSet.setValueLinePart1OffsetPercentage(90.f);
            pieDataSet.setValueLinePart1Length(0.5f);
            pieDataSet.setValueLinePart2Length(0.8f);


            pieDataSet.setSliceSpace(2f);
            pieDataSet.setColors(colors);
            adapter.refresh(categoryModelsHome);


            PieData data = new PieData(pieDataSet);

            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(18f);
            data.setValueTextColor(Color.DKGRAY);

            data.setDrawValues(true);
            pieChart.setDrawEntryLabels(false);
            pieChart.setData(data);
            pieChart.setTouchEnabled(true);
            pieChart.getDescription().setEnabled(false);

            pieChart.setUsePercentValues(true);

            pieChart.setDragDecelerationFrictionCoef(0.95f);
            pieChart.setDrawHoleEnabled(false);

            pieChart.setRotationAngle(270);
            pieChart.setRotationEnabled(true);
            pieChart.setHighlightPerTapEnabled(true);

            pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

            pieChart.animateY(1200, Easing.EaseInOutQuad);


            pieChart.invalidate();

            float money = incomesSumInDateRange + expensesSumInDateRange;
            int totalnecessidades = 0;
            int totalExtras = 0;
            int totalPoupanças = 0;
            if (incomesSumInDateRange > 0) {
                //50%
                totalnecessidades = (int) ((-necessidades * 100) / (incomesSumInDateRange * 0.5));
                //30%
                totalExtras = (int) ((-extras * 100) / (incomesSumInDateRange * 0.3));
                //20%
                totalPoupanças = (int) ((poupanças * 100) / (incomesSumInDateRange * 0.2));

            }

            if (totalnecessidades > 100)
                totalnecessidades = (int) (totalnecessidades - ((totalnecessidades - 100) * 2));
            if (totalPoupanças > 100)
                totalPoupanças = (int) (totalPoupanças - ((totalPoupanças - 100) * 2));
            if (totalExtras > 100)
                totalExtras = (int) (totalExtras - ((totalExtras - 100) * 2));

             progress = (int) ((totalnecessidades + totalExtras + totalPoupanças) / 3);

            System.out.println("totalnecessidades: " + totalnecessidades);
            System.out.println("incomesSumInDateRange: " + incomesSumInDateRange);
            System.out.println("totalExtras: " + totalExtras);
            System.out.println("totalPoupanças: " + totalPoupanças);
            System.out.println("progress: " + progress);


            if (PreferencesManager.getInstance().getSavedUserSettings(getContext()).getXP() == 0) {
                System.out.println("XP: " + PreferencesManager.getInstance().getSavedUserSettings(getContext()).getXP());
                progressbar_income_expense.setVisibility(View.INVISIBLE);
                background_progress_bar.setVisibility(View.INVISIBLE);
            } else {
                progressbar_income_expense.setVisibility(View.VISIBLE);
                background_progress_bar.setVisibility(View.VISIBLE);
                progressbar_income_expense.setMax(100);
                progressbar_income_expense.setProgress(progress);
            }


            balance.setText(String.valueOf(money + "€"));
            if (money > 0)
                balance.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            else if (money == 0)
                balance.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
            else
                balance.setTextColor(ContextCompat.getColor(getContext(), R.color.outcome_color));

        }


    }
}