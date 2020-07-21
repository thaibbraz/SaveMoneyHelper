package com.example.saveMoneyHelper.budgets;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;



import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.firebase.FirebaseElement;
import com.example.saveMoneyHelper.firebase.FirebaseObserver;
import com.example.saveMoneyHelper.firebase.factories.BudgetEntriesViewModelFactory;

import com.example.saveMoneyHelper.firebase.factories.TopWalletEntriesViewModelFactory;
import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.example.saveMoneyHelper.util.CategoriesHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Budget extends Fragment {
    private ListDataSet<BudgetEntry> budgetEntryListDataSet;
    private ListDataSet<WalletEntry> walletEntryListDataSet;
    private BudgetAdapter adapter;
    private ArrayList<BudgetListViewModel> budgetModelsHome;
    private ListView budgetListView;
    private ImageView imageView;
    private ProgressBar progressbar_limit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetModelsHome = new ArrayList<>();
        progressbar_limit = view.findViewById(R.id.progress_bar);
        budgetListView = view.findViewById(R.id.budget_list_view);
        adapter = new BudgetAdapter(getContext(), budgetModelsHome);
        budgetListView.setAdapter(adapter);
        imageView = view.findViewById(R.id.icon_imageview);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        BudgetEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getActivity()).observe(this,
                new FirebaseObserver<FirebaseElement<ListDataSet<BudgetEntry>>>() {

                    @Override
                    public void onChanged(FirebaseElement<ListDataSet<BudgetEntry>> firebaseElement) {
                        if (firebaseElement.hasNoError()) {
                            Budget.this.budgetEntryListDataSet = firebaseElement.getElement();
                            dataUpdated();

                        }
                    }


                });

        TopWalletEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), getActivity()).
                observe(this, new FirebaseObserver<FirebaseElement<ListDataSet<WalletEntry>>>() {

                    @Override
                    public void onChanged(FirebaseElement<ListDataSet<WalletEntry>> firebaseElement) {
                        if (firebaseElement.hasNoError()) {
                            Budget.this.walletEntryListDataSet = firebaseElement.getElement();
                            dataUpdated();
                        }
                    }

                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddBudget.class);
                startActivity(i);
            }
        });

    }

    private void dataUpdated() {
        if (budgetEntryListDataSet != null && walletEntryListDataSet != null) {
            List<BudgetEntry> entryList = new ArrayList<>(budgetEntryListDataSet.getList());

            long expensesSumInDateRange = 0;
            List<WalletEntry> entryListWallet = new ArrayList<>(walletEntryListDataSet.getList());
            budgetModelsHome.clear();
            int count = 20;

            if (count>0){
                for (BudgetEntry budgetEntry : entryList) {
                    long limit = budgetEntry.limit;
                    for (WalletEntry walletEntry : entryListWallet) {
                        System.out.println("categoryID budgetEntry: "+budgetEntry.categoryID);
                        System.out.println("categoryID walletEntry: "+walletEntry.categoryID);
                        if (budgetEntry.categoryID.compareTo(walletEntry.categoryID)==0 && walletEntry.balanceDifference<0){
                            expensesSumInDateRange += walletEntry.balanceDifference;
                        }

                    }

                    Category category = CategoriesHelper.searchCategory(budgetEntry.categoryID);
                    String categoryID = budgetEntryListDataSet.getIDList().get(entryList.indexOf(budgetEntry));
                    budgetModelsHome.add(new BudgetListViewModel(-expensesSumInDateRange,category,limit,categoryID));
                    count--;
                }
            }

            adapter.refresh(budgetModelsHome);


        }
    }
}