package com.example.saveMoneyHelper.budgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.auth.ProfileEditActivity;
import com.example.saveMoneyHelper.categories.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

public class BudgetAdapter extends ArrayAdapter<BudgetListViewModel> implements View.OnClickListener {
    private ArrayList<BudgetListViewModel> dataSet;
    private Context context;

    public BudgetAdapter(Context context, ArrayList<BudgetListViewModel> data) {
        super(context,  R.layout.budget_listview_row, data);
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.budget_listview_row, parent, false);
        final BudgetListViewModel dataModel = getItem(position);
        Category category = dataModel.getCategory();
        ImageView iconImageView = listItem.findViewById(R.id.icon_imageview);
        TextView moneyTextView = listItem.findViewById(R.id.money_textview);
        TextView categoryTextView = listItem.findViewById(R.id.category_textview);
        ProgressBar progressBar = listItem.findViewById(R.id.progress_bar);


       Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
       progressBar.setProgressDrawable(progressDrawable);

        float progress = 100 * dataModel.getMoney() / (float) (dataModel.getLimit());
        progressBar.setProgress((int) progress);
        if (progress>55 && progress<75)
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));

        if (progress>=75)
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        iconImageView.setImageResource(category.getIconResourceID());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));

        categoryTextView.setText(dataModel.getCategory().getCategoryVisibleName(getContext()));
        moneyTextView.setText(String.valueOf(dataModel.getLimit()+"€"));
        if (dataModel.getLimit() > 0)
            moneyTextView.setTextColor(ContextCompat.getColor(context, R.color.grey));

        listItem.setClickable(true);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileEditActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        listItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(),"category id: "+dataModel.getCategoryID()+" | userid"
                        + FirebaseAuth.getInstance().getCurrentUser().getUid().toString(), Toast.LENGTH_SHORT).show();
                createDeleteDialog(dataModel.getCategoryID(), FirebaseAuth.getInstance().getCurrentUser().getUid(), dataModel.getMoney(), view.getContext());
                return false;
            }
        });
        return listItem;

    }

    public void refresh(ArrayList<BudgetListViewModel> BudgetListViewModel){
        this.dataSet = BudgetListViewModel;
        notifyDataSetChanged();
    }

    private void createDeleteDialog(final String id, final String uid, long balanceDifference, Context context) {
        new AlertDialog.Builder(context).setMessage("Deseja eliminar?").setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                FirebaseDatabase.getInstance().getReference().child("budget-entries").child(uid).child(id).removeValue();

                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

    }


}
