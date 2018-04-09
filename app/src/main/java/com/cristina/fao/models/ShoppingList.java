package com.cristina.fao.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingList {

    private HashMap<String, String> mIngredients = new HashMap<>();
    private String mName;

    public ShoppingList() {}

    public ShoppingList(String name, HashMap<String, String> ingredients) {
        mIngredients = ingredients;
        mName = name;
    }

    public HashMap<String, Object> toMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", mName);
        map.put("content", mIngredients);

        return map;
    }

    public static class Ingredient {
        private String mIngredient;
        private String mQuantity;

        public Ingredient(String mIngredient, String mQuantity) {
            this.mIngredient = mIngredient;
            this.mQuantity = mQuantity;
        }
    }

    public String getName() {
        return mName;
    }
}
