package com.example.saveMoneyHelper.transactions;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.saveMoneyHelper.HomePage;
import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.categories.TopCategoriesAdapter;
import com.example.saveMoneyHelper.categories.TopCategoryListViewModel;
import com.example.saveMoneyHelper.firebase.FirebaseElement;
import com.example.saveMoneyHelper.firebase.FirebaseObserver;
import com.example.saveMoneyHelper.firebase.factories.TopWalletEntriesViewModelFactory;
import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Transacao extends Fragment {

    private ListDataSet<WalletEntry> walletEntryListDataSet;
    private TransactionsAdapter adapter;

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

        adapter = new TransactionsAdapter(transactionsModelsHome, getContext());
        transactionsListView.setAdapter(adapter);

        TopWalletEntriesViewModelFactory.getModel(FirebaseAuth.getInstance().getCurrentUser().getUid(), getActivity()).observe(this,
                new FirebaseObserver<FirebaseElement<ListDataSet<WalletEntry>>>() {

                    @Override
                    public void onChanged(FirebaseElement<ListDataSet<WalletEntry>> firebaseElement) {
                        if (firebaseElement.hasNoError()) {
                            Transacao.this.walletEntryListDataSet = firebaseElement.getElement();
                            dataUpdated();

                        }
                    }

                    private void dataUpdated() {
                    }

                });

    }
}