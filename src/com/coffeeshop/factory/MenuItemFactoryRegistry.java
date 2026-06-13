package com.coffeeshop.factory;
import java.util.HashMap;
import java.util.Map;

public class MenuItemFactoryRegistry {
    private static final Map<String, AbstractMenuItemFactory> registry = new HashMap<>();

    static {
        registry.put("BAKERY", new BakeryItemFactory());
        registry.put("COFFEE_MACHINE", new CoffeeMachineFactory());
        registry.put("COFFEE_TRADITIONAL", new CoffeeTraditionalFactory());
        registry.put("COMBO", new ComboFactory());
    }

    public static AbstractMenuItemFactory getFactory(String category) {
        return registry.get(category);
    }
}