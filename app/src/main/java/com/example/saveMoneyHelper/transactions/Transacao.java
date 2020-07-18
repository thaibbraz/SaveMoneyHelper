package com.example.saveMoneyHelper.transactions;


import android.content.DialogInterface;

import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.saveMoneyHelper.R;
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
import com.google.firebase.auth.FirebaseAuth;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Transacao extends Fragment {

    private ListDataSet<WalletEntry> walletEntryListDataSet;

    private TransactionsAdapter adapter;
    private Toolbar toolbarTransacoes;
    private TextView textViewToolBar;
    private ArrayList<TransactionsListViewModel> transactionsModelsHome;
    private ListView transactionsListView;
    private ImageView icon_calendar;
    private Calendar chosenDate;
    private Calendar dateBegin;
    private Calendar dateEnd;
    private UserSettings userSettings;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //MONTHLY BY DEFAULT
        userSettings = new UserSettings();

        if (PreferencesManager.getInstance().getSavedUserSettings(getContext()) != null) {

            dateBegin = CalendarHelper.getStartDate(PreferencesManager.getInstance().getSavedUserSettings(getContext()));
            dateEnd = CalendarHelper.getEndDate(PreferencesManager.getInstance().getSavedUserSettings(getContext()));


        } else {
            dateBegin = CalendarHelper.getStartDate(userSettings);
            dateEnd = CalendarHelper.getEndDate(userSettings);


        }
        return inflater.inflate(R.layout.fragment_transacoes, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        transactionsModelsHome = new ArrayList<>();
        transactionsListView = view.findViewById(R.id.transacoes_list_view);

         toolbarTransacoes = (Toolbar) view.findViewById(R.id.toolbarTransacoes);
         textViewToolBar = (TextView)toolbarTransacoes.findViewById(R.id.title);

        adapter = new TransactionsAdapter(transactionsModelsHome, getContext());
        transactionsListView.setAdapter(adapter);
        icon_calendar = view.findViewById(R.id.icon_calendar);

        TopWalletEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getActivity()).observe(this,
                new FirebaseObserver<FirebaseElement<ListDataSet<WalletEntry>>>() {

                    @Override
                    public void onChanged(FirebaseElement<ListDataSet<WalletEntry>> firebaseElement) {
                        if (firebaseElement.hasNoError()) {
                            Transacao.this.walletEntryListDataSet = firebaseElement.getElement();
                            dataUpdated();

                        }
                    }



                });
        icon_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDateRangeDialog();

            }
        });

    }
    private void showSelectDateRangeDialog() {
        SmoothDateRangePickerFragment datePicker = SmoothDateRangePickerFragment.newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
            @Override
            public void onDateRangeSet(SmoothDateRangePickerFragment view, int yearStart, int monthStart, int dayStart, int yearEnd, int monthEnd, int dayEnd) {
                dateBegin = Calendar.getInstance();
                dateBegin.set(yearStart, monthStart, dayStart);
                dateBegin.set(Calendar.HOUR_OF_DAY, 0);
                dateBegin.set(Calendar.MINUTE, 0);
                dateBegin.set(Calendar.SECOND, 0);

                dateEnd = Calendar.getInstance();
                dateEnd.set(yearEnd, monthEnd, dayEnd);
                dateEnd.set(Calendar.HOUR_OF_DAY, 23);
                dateEnd.set(Calendar.MINUTE, 59);
                dateEnd.set(Calendar.SECOND, 59);
                calendarUpdated();

            }
        });
        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dateBegin = null;
                dateEnd = null;
                calendarUpdated();

            }
        });
        datePicker.show(getActivity().getFragmentManager(), "TAG");
        //todo library doesn't respect other method than deprecated
    }
    private void calendarUpdated() {
        TopWalletEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getActivity()).setDateFilter(dateBegin, dateEnd);


    }
    private void dataUpdated() {
        if (walletEntryListDataSet != null) {
            List<WalletEntry> entryList = new ArrayList<>(walletEntryListDataSet.getList());

            transactionsModelsHome.clear();
            int count = 100;

            if (count>0){
                for (WalletEntry walletEntry : entryList) {
                    Category category = CategoriesHelper.searchCategory(walletEntry.categoryID);
                    Date date = new Date(-walletEntry.timestamp);
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String categoryID = walletEntryListDataSet.getIDList().get(entryList.indexOf(walletEntry));

                    transactionsModelsHome.add(new TransactionsListViewModel(walletEntry.balanceDifference,category,dateFormat.format(date),walletEntry.name,categoryID));
                    count--;
                }

                Collections.sort(transactionsModelsHome, new Comparator<TransactionsListViewModel>() {
                    @Override
                    public int compare(TransactionsListViewModel o1, TransactionsListViewModel o2) {

                        return o2.getDateTextView().compareTo(o1.getDateTextView());
                    }
                });
                adapter.refresh(transactionsModelsHome);
            }

        }


    }
}