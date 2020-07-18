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

import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.example.saveMoneyHelper.util.CategoriesHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class Budget extends Fragment {
    private ListDataSet<BudgetEntry> budgetEntryListDataSet;
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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddBudget.class);
                startActivity(i);
            }
        });

    }

    private void dataUpdated() {
        if (budgetEntryListDataSet != null) {
            List<BudgetEntry> entryList = new ArrayList<>(budgetEntryListDataSet.getList());

            budgetModelsHome.clear();
            int count = 20;
            if (count>0){
                for (BudgetEntry budgetEntry : entryList) {

                    Category category = CategoriesHelper.searchCategory(budgetEntry.categoryID);
                    String categoryID = budgetEntryListDataSet.getIDList().get(entryList.indexOf(budgetEntry));
                    long limit = -budgetEntry.limit;
                    budgetModelsHome.add(new BudgetListViewModel(limit,category,1000,categoryID));
                    count--;

                }
            }

            adapter.refresh(budgetModelsHome);

       /*
            float progress = 100 * userSettings.getBudget().getEntry() / (float) (userSettings.getBudget().getLimit());

            float money = incomesSumInDateRange+expensesSumInDateRange;

            progressbar_income_expense.setMax((int) userSettings.getBudget().getLimit());
            progressbar_income_expense.setProgress((int) progress);

        */
        }
    }
}