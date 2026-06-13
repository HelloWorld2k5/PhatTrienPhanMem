package com.coffeeshop.factory;

import com.coffeeshop.model.CoffeeCombo;
import com.coffeeshop.model.MenuItem;

public class ComboFactory extends  AbstractMenuItemFactory{
    @Override
    public MenuItem createMenuItem(int id, String name, String category, double price, String description, String icon, String status) {
        return new CoffeeCombo (id, name, "COMBO", price, description, icon, status);
    }
}
