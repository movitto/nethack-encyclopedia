package org.morsi.android.nethack.redux.util;

import java.util.ArrayList;

// actual quick stat to display
public class QuickStat {
	
	// actual values to display
    public ArrayList<String> values;
    
    public QuickStat(ArrayList<String> rvalues){
    	values = new ArrayList<String>();
    	for(String value : rvalues)
    		values.add(value);
    }
    
    public int num_values(){ return values.size(); }
    public String get_value(int index){
        if(index > values.size() - 1) return "";
        return values.get(index);
    }
}
