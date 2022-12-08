package edu.northeastern.team11.slurp;

import android.graphics.Color;

import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;

// Represents a Restaurant shown on the map
public class MapRestaurant {
    private RestaurantDish restDish;
    private SymbolManager symbolManager;
    private Symbol symbol;

    public MapRestaurant(RestaurantDish restDish, SymbolManager symbolManager, Symbol symbol) {
        this.restDish = restDish;
        this.symbolManager = symbolManager;
        this.symbol = symbol;
    }


    public void setSymbolColor(Color color) {
        this.symbol.setIconColor(String.valueOf(color));
    }

    public void selectOnMap() {
        this.symbol.setTextField(this.restDish.getRestName());
        this.symbol.setIconColor("BF40BF"); // PURPLE
        this.symbolManager.update(this.symbol);
    }

    public void deselectOnMap() {
        this.symbol.setTextField("");
        this.symbol.setIconColor("FF0000"); //RED
        this.symbolManager.update(this.symbol);
    }

    // Remove a symbol from the map
    public void removeFromMap() {
        this.symbolManager.delete(this.symbol);
    }

    public RestaurantDish getRestDish() {
        return restDish;
    }

    public void setRestDish(RestaurantDish restDish) {
        this.restDish = restDish;
    }

    public SymbolManager getSymbolManager() {
        return symbolManager;
    }

    public void setSymbolManager(SymbolManager symbolManager) {
        this.symbolManager = symbolManager;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}