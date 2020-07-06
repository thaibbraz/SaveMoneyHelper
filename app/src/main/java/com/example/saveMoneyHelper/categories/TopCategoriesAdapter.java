package com.example.saveMoneyHelper.categories;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.saveMoneyHelper.R;
import com.example.saveMoneyHelper.auth.ProfileEditActivity;
import com.example.saveMoneyHelper.wallet.AddWallet;

import java.util.ArrayList;

public class TopCategoriesAdapter extends ArrayAdapter<TopCategoryListViewModel> implements View.OnClickListener {

    private ArrayList<TopCategoryListViewModel> dataSet;
    Context context;


    public TopCategoriesAdapter(ArrayList<TopCategoryListViewModel> data, Context context) {
        super(context, R.layout.favorites_listview_row, data);
        this.dataSet = data;
        this.context = context;

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.favorites_listview_row, parent, false);

        TopCategoryListViewModel dataModel = getItem(position);
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
                Intent intent = new Intent(view.getContext(), ProfileEditActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return listItem;
    }
    public void refresh(ArrayList<TopCategoryListViewModel> top10List){
        this.dataSet = top10List;
        notifyDataSetChanged();

    }
}

