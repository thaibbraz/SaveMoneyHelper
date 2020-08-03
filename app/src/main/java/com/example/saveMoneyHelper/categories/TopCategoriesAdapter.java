package com.example.saveMoneyHelper.categories;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.DialogTitle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.auth.ProfileEditActivity;
import com.example.saveMoneyHelper.intro.DetailActivity;
import com.example.saveMoneyHelper.wallet.AddWallet;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class TopCategoriesAdapter extends ArrayAdapter<TopCategoryListViewModel> implements View.OnClickListener {
    private Dialog mydialog;
    private ArrayList<TopCategoryListViewModel> dataSet;
    private TopCategoryListViewModel dataModel;
    private Context context;
    private LayoutInflater layoutInflater;
    private  View listItem;


    public TopCategoriesAdapter(ArrayList<TopCategoryListViewModel> data, Context context) {
        super(context, R.layout.favorites_listview_row, data);

        this.dataSet = data;
        this.context = context;

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.favorites_listview_row, parent, false);

        dataModel = getItem(position);
        Category category = dataModel.getCategory();

        TextView categoryNameTextView = listItem.findViewById(R.id.item_category);
        TextView sumTextView = listItem.findViewById(R.id.item_sum);
        ImageView iconImageView = listItem.findViewById(R.id.icon_imageview);

        iconImageView.setImageResource(category.getIconResourceID());
        iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));

        categoryNameTextView.setText(dataModel.getCategoryName());
        sumTextView.setText(String.valueOf(dataModel.getMoney()));
        if (dataModel.getMoney() < 0)
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.outcome_color));
        else
            sumTextView.setTextColor(ContextCompat.getColor(context, R.color.income_color));

        listItem.setClickable(true);
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Intent intent = new Intent(view.getContext(), ProfileEditActivity.class);
              //  view.getContext().startActivity(intent);
              AlertDialog.Builder mAlert;
              View vpopup= (View) instantiateItem(parent,position);
              mAlert = new AlertDialog.Builder(getContext()).setView(vpopup);
              mAlert.create();

            }
        });

        return listItem;
    }
    public void refresh(ArrayList<TopCategoryListViewModel> top10List){
        this.dataSet = top10List;
        notifyDataSetChanged();

    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView;
        TextView categoriaPopUp, total;

        imageView = view.findViewById(R.id.img_logo_popup);
        categoriaPopUp = view.findViewById(R.id.categoriaPopUp);
        total = view.findViewById(R.id.total);

        imageView.setImageResource(dataSet.get(position).getCategory().getIconResourceID());
        categoriaPopUp.setText(dataSet.get(position).getCategoryName());
        total.setText((int) dataSet.get(position).getMoney());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

}

