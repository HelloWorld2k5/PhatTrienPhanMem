
package com.coffeeshop.factory;
import com.coffeeshop.model.CoffeeTraditionalItem;
import com.coffeeshop.model.MenuItem;

public class CoffeeTraditionalFactory extends  AbstractMenuItemFactory {
    @Override
    public MenuItem createMenuItem(int id, String name, String category, double price, String description, String icon, String status) {
        return new CoffeeTraditionalItem(id, name, "COFFEE_TRADITIONAL", price, description, icon, status);
    }
}
