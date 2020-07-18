package com.example.saveMoneyHelper.budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.Date;
import java.util.List;

public class AddBudget extends AppCompatActivity {
    private ImageView imageClose;
    private Spinner selectCategorySpinner;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button addEntryButton;
    private TextInputEditText selectNameEditText;
    private TextInputEditText selectAmountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        imageClose = findViewById(R.id.icon_close);
        addEntryButton = findViewById(R.id.add_entry_button);
        selectNameEditText = findViewById(R.id.select_name_edittext);
        selectAmountEditText = findViewById(R.id.select_amount_edittext);
        selectCategorySpinner = findViewById(R.id.select_category_spinner);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Update category list
        updateList();
        imageClose.setOnClickListener(new View.OnClickListener() {
            //Closing this screen
            @Override
            public void onClick(View view) {
             try {
                    Intent i = new Intent(view.getContext(), Budget.class);
                    startActivity(i);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


            }
        });


        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    addBudget(Long.valueOf(selectAmountEditText.getText().toString()),
                            ((Category) selectCategorySpinner.getSelectedItem()).getCategoryID(),
                            selectNameEditText.getText().toString());

                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG);

                }

            }

        });

    }
    private void updateList() {
        final List<Category> categories = CategoriesHelper.getCategories();
        EntryCategoriesAdapter categoryAdapter = new EntryCategoriesAdapter(this.getApplicationContext(),
                R.layout.new_entry_type_spinner_row, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectCategorySpinner.setAdapter(categoryAdapter);
    }

    public void addBudget(long limit, String entryCategory, String entryName) throws Exception {
        if (limit == 0) {
            throw new Exception("O valor deverá ser diferente de 0");
        }
        if (entryName == null || entryName.length() == 0) {
            throw new Exception("O nome deverá ser > 0 caracteres");
        }
        FirebaseDatabase.getInstance().getReference().child("budget-entries").child(user.getUid())
                .push().setValue(new BudgetEntry(entryCategory, entryName, limit));

    }

}