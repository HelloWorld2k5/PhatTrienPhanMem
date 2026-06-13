package com.coffeeshop.factory;

import com.coffeeshop.model.BakeryItem;
import com.coffeeshop.model.MenuItem;
public class BakeryItemFactory extends AbstractMenuItemFactory {
    @Override
    public  MenuItem createMenuItem(int id, String name, String category,double price,String description, String icon, String status) {
        return new BakeryItem(id, name, "BAKERY", price, description, icon, status);
    }
}
