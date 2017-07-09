package com.example.arshdeep.stock;

/**
 * Created by Arshdeep on 7/9/2017.
 */

public class Stock {
    String Symbol;
    String Name;
    String Change;
    String ChangeinPercent;

    public Stock() {}

    public Stock(String symbol, String name, String change, String changeinPercent) {
        Symbol = symbol;
        Name = name;
        Change = change;
        ChangeinPercent = changeinPercent;
    }
}
