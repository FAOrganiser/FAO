package com.cristina.fao.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingList {

    private List<Ingredient> ingredients;
    private String name;

    public ShoppingList() {}

    public ShoppingList(String name, List<Ingredient> ingr) {
        this.ingredients = ingr;
        this.name = name;
    }

    public void setContent(List<Ingredient> ingr) {
        ingredients = ingr;
    }

    public void setName(String mName) {
        name = mName;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public HashMap<String, Object> toMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("ingredients", ingredients);

        return map;
    }

    public static class Ingredient {

        private String name;
        private String quantity;

        public Ingredient() {}

        public Ingredient(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public String getQuantity() {
            return quantity;
        }
    }


}
