package com.example.saveMoneyHelper.firebase.factories;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.saveMoneyHelper.firebase.bases.BudgetEntriesBaseViewModel;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;

public class BudgetEntriesViewModelFactory implements ViewModelProvider.Factory{
    private String uid;

    BudgetEntriesViewModelFactory(String uid) {
        this.uid = uid;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        return (T)new BudgetEntriesViewModelFactory.Model(uid);
    }

    public static BudgetEntriesViewModelFactory.Model getModel(String uid, FragmentActivity activity) {
        return ViewModelProviders.of(activity, new BudgetEntriesViewModelFactory(uid)).get(BudgetEntriesViewModelFactory.Model.class);
    }

    public static class Model extends BudgetEntriesBaseViewModel {

        public Model(String uid) {
            super(uid, FirebaseDatabase.getInstance().getReference().child("budget-entries").child(uid).orderByChild("limit"));
        }


    }
}
