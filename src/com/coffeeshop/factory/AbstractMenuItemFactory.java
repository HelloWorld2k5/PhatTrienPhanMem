package com.coffeeshop.factory;

import com.coffeeshop.model.MenuItem;

public abstract class AbstractMenuItemFactory {

    public abstract MenuItem createMenuItem(int id, String name, String category, 
                                            double price, String description, 
                                            String icon, String status);
    
    
}