package com.coffeeshop.factory;

import com.coffeeshop.model.CoffeeMachineItem;
import com.coffeeshop.model.MenuItem;

public class CoffeeMachineFactory extends  AbstractMenuItemFactory {
    @Override
    public MenuItem createMenuItem(int id, String name, String category, double price, String description, String icon, String status) {
        return new CoffeeMachineItem(id, name, "COFFEE_MACHINE", price, description, icon, status);
    }
}
