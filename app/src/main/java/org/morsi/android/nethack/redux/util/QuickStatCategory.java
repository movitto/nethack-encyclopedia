package org.morsi.android.nethack.redux.util;

import android.content.res.XmlResourceParser;

import org.morsi.android.nethack.redux.items.Item;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// category of quick stats to display
public class QuickStatCategory {
    // name of the quick stat category
	public String name;
	
	// list of column names
    public ArrayList<String> column_names;
   
    // list of column weights, total should equal 1
    public ArrayList<Double> column_weights;
   
    // list of actual quick stat rows
    public ArrayList<QuickStat> stats;
    
    public QuickStatCategory(String rname){
    	name = rname;
    	column_names   = new ArrayList<String>();
    	column_weights = new ArrayList<Double>();
    	stats = new ArrayList<QuickStat>();
    }
    
    private void add_column(String name, Double weight){
    	column_names.add(name);
    	column_weights.add(weight);
    }
    
    private void add_stats(ArrayList<String> rstats){
    	QuickStat stat = new QuickStat(rstats);
    	stats.add(stat);
    }
    
    // Variables used for table sorting
    private int sort_column = -1;
    private boolean reverse  = false;
    
    public void sort_stats(String by_column){
    	// find column we're sorting by
	    for(sort_column = 0; sort_column < column_names.size(); ++sort_column)
	        if(column_names.get(sort_column).equals(by_column))
	            break;
	
	    // actually perform the sort using a custom comparator
	    Collections.sort(stats, new Comparator<QuickStat>() {
	        public int compare(QuickStat s1, QuickStat s2) {
		        String item1 = sort_column < s1.num_values() ? s1.get_value(sort_column) : "";
		        String item2 = sort_column < s2.num_values() ? s2.get_value(sort_column) : "";
		
		         // first try to convert numeric columns to floats b4 comparing
		        try{
		          float f1 = Float.parseFloat(item1);
		          float f2 = Float.parseFloat(item2);
		          if (f1 == f2) return 0;
		          if ((reverse && f1 > f2) || (!reverse && f1 < f2)) return 1;
		          return -1;
		        } catch( NumberFormatException e ){}

		        return reverse ? item2.compareTo(item1) :item1.compareTo(item2);
		    }
	      });
	      reverse = !reverse;
    }
}
