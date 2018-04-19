package com.cristina.fao.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingList {

    private HashMap<String, String> content = new HashMap<>();
    private String name;

    public void setmIngredients(HashMap<String, String> mIngredients) {
        content = mIngredients;
    }

    public void setmName(String mName) {
        name = mName;
    }

    public ShoppingList() {}

    public ShoppingList(String name, HashMap<String, String> ingredients) {
        content = ingredients;
        this.name = name;
    }

    public HashMap<String, Object> toMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("content", content);

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
        return name;
    }

    public HashMap<String, String> getmIngredients() {
        return content;
    }
}
