package com.example.saveMoneyHelper.transactions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.saveMoneyHelper.HomePage;
import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.categories.TopCategoriesAdapter;
import com.example.saveMoneyHelper.categories.TopCategoryListViewModel;
import com.example.saveMoneyHelper.firebase.FirebaseElement;
import com.example.saveMoneyHelper.firebase.FirebaseObserver;
import com.example.saveMoneyHelper.firebase.factories.TopWalletEntriesViewModelFactory;
import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.example.saveMoneyHelper.util.CategoriesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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