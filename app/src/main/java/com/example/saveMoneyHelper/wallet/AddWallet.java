package com.example.saveMoneyHelper.wallet;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.categories.EntryCategoriesAdapter;
import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.util.CategoriesHelper;
import com.example.saveMoneyHelper.util.CurrencyHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddWallet extends Fragment {

    private Spinner selectCategorySpinner;
    private TextInputEditText selectNameEditText;
    private Calendar chosenDate;
    private TextInputEditText selectAmountEditText;
    private TextView chooseDayTextView;
    private Spinner selectTypeSpinner;
    private Button addEntryButton;
    private FirebaseUser user;
    private TextInputLayout selectAmountInputLayout;
    private TextInputLayout selectNameInputLayout;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private Switch typeSwitch;
    private ImageView imgCalendar;
    private String type="wants";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        selectCategorySpinner = view.findViewById(R.id.select_category_spinner);
        typeSwitch = view.findViewById(R.id.type);
        selectNameEditText = view.findViewById(R.id.select_name_edittext);
        selectNameInputLayout = view.findViewById(R.id.select_name_inputlayout);
        selectTypeSpinner = view.findViewById(R.id.select_type_spinner);
        addEntryButton = view.findViewById(R.id.add_entry_button);
        chooseDayTextView = view.findViewById(R.id.choose_day_textview);
        selectAmountEditText = view.findViewById(R.id.select_amount_edittext);
        selectAmountInputLayout = view.findViewById(R.id.select_amount_inputlayout);
        toolbar = view.findViewById(R.id.toolbarAddWallet);
        chosenDate = Calendar.getInstance();
        imgCalendar =  view.findViewById(R.id.icon_imageview);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        user = mAuth.getCurrentUser();

        if (user != null) {
            updateLists();
            updateDate();
        }

        chooseDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    addToWallet(((selectTypeSpinner.getSelectedItemPosition() * 2) - 1) *
                                    CurrencyHelper.convertAmountStringToLong(selectAmountEditText.getText().toString()),
                            chosenDate.getTime(),
                            ((Category) selectCategorySpinner.getSelectedItem()).getCategoryID(),
                            selectNameEditText.getText().toString(),type);
                    getActivity().onBackPressed();
                } catch (Exception e) {
                    selectNameInputLayout.setError(e.getMessage());


                }

            }

        });
    }

    private void updateLists() {
        final List<Category> categories = CategoriesHelper.getCategories();
        EntryCategoriesAdapter categoryAdapter = new EntryCategoriesAdapter(getContext(),
                R.layout.new_entry_type_spinner_row, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectCategorySpinner.setAdapter(categoryAdapter);

        EntryTypesAdapter typeAdapter = new EntryTypesAdapter(getContext(),
                R.layout.new_entry_type_spinner_row, Arrays.asList(
                new EntryTypeListViewModel("Despesas", Color.parseColor("#ef5350"),
                        R.drawable.money_icon),
                new EntryTypeListViewModel("Ganhos", Color.parseColor("#03DAC5"),
                        R.drawable.money_icon)));

        selectTypeSpinner.setAdapter(typeAdapter);
        typeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   // buttonView.setBackgroundColor(Color.GREEN);
                    buttonView.setHighlightColor(Color.GREEN);
                    type="needs";
                }
                else
                    type="wants";
            }
        });
        selectTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        addEntryButton.setBackgroundColor(Color.parseColor("#ef5350"));
                        toolbar.setBackgroundColor(Color.parseColor("#ef5350"));
                        imgCalendar.setBackgroundColor(Color.parseColor("#ef5350"));
                        typeSwitch.setVisibility(View.VISIBLE);

                        break;
                    case 1:
                        addEntryButton.setBackgroundColor(Color.parseColor("#03DAC5"));
                        toolbar.setBackgroundColor(Color.parseColor("#03DAC5"));
                        imgCalendar.setBackgroundColor(Color.parseColor("#03DAC5"));
                        typeSwitch.setVisibility(View.INVISIBLE);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateDate() {
        SimpleDateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd");
        chooseDayTextView.setText(dataFormatter.format(chosenDate.getTime()));

    }

    private void pickDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                chosenDate.set(year, monthOfYear, dayOfMonth);
                updateDate();

            }
        }, year, month, day).show();
    }

    public void addToWallet(long balanceDifference, Date entryDate, String entryCategory, String entryName, String type) throws Exception {
        if (balanceDifference == 0) {
            throw new Exception("O valor deverá ser diferente de 0");
        }
        if (entryName == null || entryName.length() == 0) {
            throw new Exception("O nome deverá ser > 0 caracteres");
        }
        FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(user.getUid())
                .child("default").push().setValue(new WalletEntry(entryCategory, entryName, entryDate.getTime(), balanceDifference,type));

    }

}
