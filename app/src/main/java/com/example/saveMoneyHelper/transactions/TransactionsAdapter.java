package com.example.saveMoneyHelper.transactions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.auth.ProfileEditActivity;
import com.example.saveMoneyHelper.categories.Category;
import com.example.saveMoneyHelper.firebase.models.WalletEntry;
import com.example.saveMoneyHelper.firebase.utils.ListDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


public class TransactionsAdapter extends ArrayAdapter<TransactionsListViewModel> implements View.OnClickListener {
    private ArrayList<TransactionsListViewModel> dataSet;

    private Context context;
    public TransactionsAdapter(ArrayList<TransactionsListViewModel> data, Context context) {
        super(context, R.layout.transaction_listview_row, data);
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
            listItem = LayoutInflater.from(context).inflate(R.layout.transaction_listview_row, parent, false);
        final TransactionsListViewModel dataModel = getItem(position);
        Category category = dataModel.getCategory();

        TextView moneyTextView = listItem.findViewById(R.id.money_textview);
        TextView categoryTextView = listItem.findViewById(R.id.category_textview);
        TextView nameTextView = listItem.findViewById(R.id.name_textview);
        TextView dateTextView = listItem.findViewById(R.id.date_textview);
        ImageView iconImageView = listItem.findViewById(R.id.icon_imageview);


        nameTextView.setText(dataModel.getNameTextView());
        iconImageView.setImageResource(category.getIconResourceID());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));
        dateTextView.setText(dataModel.getDateTextView());

        categoryTextView.setText(dataModel.getCategory().getCategoryVisibleName(getContext()));
        moneyTextView.setText(String.valueOf(dataModel.getMoney()));
        if (dataModel.getMoney() < 0)
            moneyTextView.setTextColor(ContextCompat.getColor(context, R.color.outcome_color));
        else
            moneyTextView.setTextColor(ContextCompat.getColor(context, R.color.income_color));

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
                createDeleteDialog(dataModel.getCategoryID(), FirebaseAuth.getInstance().getCurrentUser().getUid(), dataModel.getMoney(), view.getContext());
                return false;
            }
        });
        return listItem;
    }

    public void refresh(ArrayList<TransactionsListViewModel> transactions){
        this.dataSet = transactions;
        notifyDataSetChanged();

    }


    private void createDeleteDialog(final String id, final String uid, long balanceDifference, Context context) {
        new AlertDialog.Builder(context).setMessage("Deseja eliminar?").setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                FirebaseDatabase.getInstance().getReference().child("wallet-entries").child(uid).child("default").child(id).removeValue();

                dialog.dismiss();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

    }

}
