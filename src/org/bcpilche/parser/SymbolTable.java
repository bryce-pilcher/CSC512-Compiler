package org.bcpilche.parser;

import java.util.HashMap;

/**
 * Created by Bryce on 11/13/2015.
 */
public class SymbolTable {
    SymbolTable parent;
    HashMap<String, String> symbols;
    int localCount = 0;
    int globalCount = 0;
    int labelCount = 0;

    public SymbolTable(){
        this.parent = null;
        this.symbols = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
	public SymbolTable(SymbolTable parent){
        this.parent = parent;
        this.symbols = (HashMap<String, String>)parent.getAllSymbols().clone();
        this.localCount = parent.getLocalCount();
        this.labelCount = parent.getLabelCount();
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

    public int getGlobalCount(){
        return globalCount;
    }

    public void incGlobalCount(){
        this.globalCount++;
    }

    public int getLabelCount(){
        return labelCount;
    }

    public void incLabelCount() {
        this.labelCount++;
    	if(parent != null){
    		parent.incLabelCount();
    	}
    }
}
