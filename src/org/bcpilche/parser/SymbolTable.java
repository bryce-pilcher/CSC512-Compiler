package org.bcpilche.parser;

import java.util.HashMap;

/**
 * Created by Bryce on 11/13/2015.
 */
public class SymbolTable {
    SymbolTable parent;
    HashMap<String, String> symbols;
    int localCount = 0;
    int labelCount = 0;

    public SymbolTable(){
        this.parent = null;
        this.symbols = new HashMap<>();
    }

    public SymbolTable(SymbolTable parent){
        this.parent = parent;
        this.symbols = parent.getAllSymbols();
    }

    public HashMap<String, String> getAllSymbols(){
        return symbols;
    }

    public SymbolTable getParent(){
        return parent;
    }

    public int getLocalCount(){
        return localCount;
    }

    public void incLocalCount(){
        this.localCount++;
    }

    public int getLabelCount(){
        return labelCount;
    }

    public void incLabelCount() {
        this.labelCount++;
    }
}
