package com.cristina.fao.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingList {

    private HashMap<String, String> content = new HashMap<>();
    private String name;

    public ShoppingList() {}

    public ShoppingList(String mName, HashMap<String, String> ingredients) {
        content = ingredients;
        name = mName;
    }

    public void setContent(HashMap<String, String> mIngredients) {
        content = mIngredients;
    }

    public void setName(String mName) {
        name = mName;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getmContent() {
        return content;
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

        public void setIngredient(String mIngredient) {
            this.mIngredient = mIngredient;
        }

        public void setQuantity(String mQuantity) {
            this.mQuantity = mQuantity;
        }

        public String getIngredient() {
            return mIngredient;
        }

        public String getQuantity() {
            return mQuantity;
        }
    }


}
