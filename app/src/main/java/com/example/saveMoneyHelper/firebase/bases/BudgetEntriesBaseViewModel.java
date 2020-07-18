package com.example.saveMoneyHelper.firebase.bases;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.saveMoneyHelper.budgets.BudgetEntry;
import com.example.saveMoneyHelper.firebase.FirebaseElement;
import com.example.saveMoneyHelper.firebase.FirebaseObserver;
import com.example.saveMoneyHelper.firebase.queries.FirebaseQueryLiveDataSet;
import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.google.firebase.database.Query;

public class BudgetEntriesBaseViewModel extends ViewModel{
    protected final FirebaseQueryLiveDataSet<BudgetEntry> liveData;
    protected final String uid;

    public BudgetEntriesBaseViewModel(String uid, Query query) {
        this.uid=uid;
        liveData = new FirebaseQueryLiveDataSet<>(BudgetEntry.class, query);
    }

    public void observe(LifecycleOwner owner, final FirebaseObserver<FirebaseElement<ListDataSet<BudgetEntry>>> observer) {
        observer.onChanged(liveData.getValue());
        liveData.observe(owner, new Observer<FirebaseElement<ListDataSet<BudgetEntry>>>() {
            @Override
            public void onChanged(@Nullable FirebaseElement<ListDataSet<BudgetEntry>> element) {
                if(element != null)
                    observer.onChanged(element);
            }
        });
    }

    public void removeObserver(Observer<FirebaseElement<ListDataSet<BudgetEntry>>> observer) {
        liveData.removeObserver(observer);
    }

}
