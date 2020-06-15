package com.example.saveMoneyHelper.models;

import android.graphics.Color;

import com.example.saveMoneyHelper.R;

public  class DefaultCategories {
    private static Category[] categories = new Category[]{
            new Category(":others", "Outros", R.drawable.category_default, Color.parseColor("#455a64")),
            new Category(":clothing", "Roupas", R.drawable.category_clothing, Color.parseColor("#d32f2f")),
            new Category(":food", "Alimentação", R.drawable.category_food, Color.parseColor("#c2185b")),
            new Category(":gas_station", "Combustível", R.drawable.category_gas_station, Color.parseColor("#7b1fa2")),
            new Category(":gaming", "Jogos", R.drawable.category_gaming, Color.parseColor("#512da8")),
            new Category(":gift", "Prendas", R.drawable.category_gift, Color.parseColor("#303f9f")),
            new Category(":holidays", "Férias", R.drawable.category_holidays, Color.parseColor("#1976d2")),
            new Category(":home", "Casa", R.drawable.category_home, Color.parseColor("#0288d1")),
            new Category(":kids", "Crianças", R.drawable.category_kids, Color.parseColor("#0097a7")),
            new Category(":pharmacy", "Farmácia", R.drawable.category_pharmacy, Color.parseColor("#00796b")),
            new Category(":repair", "Reparação", R.drawable.category_repair, Color.parseColor("#388e3c")),
            new Category(":shopping", "Compras", R.drawable.category_shopping, Color.parseColor("#689f38")),
            new Category(":sport", "Desporto", R.drawable.category_sport, Color.parseColor("#afb42b")),
            new Category(":transfer", "Transferência", R.drawable.category_transfer, Color.parseColor("#fbc02d")),
            new Category(":transport", "Transporte", R.drawable.category_transport, Color.parseColor("#ffa000")),
            new Category(":work", "Trabalho", R.drawable.category_briefcase, Color.parseColor("#f57c00")),


    };

    public static Category createDefaultCategoryModel(String visibleName) {
        return new Category("default", visibleName, R.drawable.category_default,
                Color.parseColor("#26a69a"));
    }


    public static Category[] getDefaultCategories() {
        return categories;
    }
}
