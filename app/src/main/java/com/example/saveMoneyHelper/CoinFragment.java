package com.example.saveMoneyHelper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.settings.PreferencesManager;
import com.example.saveMoneyHelper.settings.UserSettings;
import com.example.saveMoneyHelper.util.CalendarHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CoinFragment extends Fragment {
    private TextView necessidades, extras, poupanças;
    private TextInputEditText salarioEdit;
    private int salario;
    private Button btn_confirm, btn_delete;
    private UserSettings userSettings;
    private TextInputLayout valorinputlayout;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coin, container, false);
        salarioEdit = view.findViewById(R.id.valorEdit);
        necessidades = view.findViewById(R.id.gastosEssenciaisValor);
        extras = view.findViewById(R.id.gastosExtrasValor);
        poupanças = view.findViewById(R.id.savingsValor);
        btn_confirm = view.findViewById(R.id.add_entry_button);
        btn_delete = view.findViewById(R.id.cancel_entry_button);
        valorinputlayout = view.findViewById(R.id.valorinputlayout);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userSettings = PreferencesManager.getInstance().getSavedUserSettings(getContext());
        if (userSettings != null && userSettings.getXP()==1 && userSettings.getSalary()>0){
            salarioEdit.setText(String.valueOf(userSettings.getSalary()));

        }
        try {
            salarioEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    necessidades.setText("0€");
                    poupanças.setText("0€");
                    extras.setText("0€");

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {
                        salario = Integer.parseInt(s.toString());
                        necessidades.setText(String.valueOf((int)salario * 0.5) + "€");
                        extras.setText(String.valueOf((int)salario * 0.3) + "€");
                        poupanças.setText(String.valueOf((int)salario * 0.2) + "€");
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // necessidades.setText(String.valueOf(input));
                }
            });

        } catch (Exception e) {
            valorinputlayout.setError(e.getMessage());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!salarioEdit.getText().toString().isEmpty()) {
                    if (userSettings != null) {
                        userSettings.setXP(1);

                    }else {
                        userSettings = new UserSettings();
                        userSettings.setXP(1);

                    }
                    userSettings.setSalary(Integer.parseInt(salarioEdit.getText().toString()));
                    PreferencesManager.getInstance().setUserSettings(getContext(), userSettings);

                    FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(user.getUid())
                            .child("default").push().setValue(new WalletEntry(":salary","Salário", new Date().getTime(), Integer.parseInt(salarioEdit.getText().toString()),null));
                    Navigation.findNavController(getView()).popBackStack();
                } else {
                    valorinputlayout.setError("Não é possível adicionar a meta sem o valor do salário");

                }

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSettings != null) {
                    userSettings.setXP(0);
                    userSettings.setSalary(0);

                }else {
                    userSettings = new UserSettings();
                    userSettings.setXP(0);
                    userSettings.setSalary(0);

                }
                PreferencesManager.getInstance().setUserSettings(getContext(), userSettings);
                Navigation.findNavController(getView()).popBackStack();
            }
        });
    }
}