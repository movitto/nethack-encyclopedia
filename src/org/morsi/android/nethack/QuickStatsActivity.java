/**********************************
 * Nethack Encyclopedia - Quick Stats Activity
 * 
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.morsi.android.nethack.R;
import org.morsi.android.nethack.util.AndroidMenu;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;

// User friendly view to various lists of builtin Nethack stats
public class QuickStatsActivity extends Activity {
	// Override menu / about dialog handlers
    @Override
    public boolean onSearchRequested() { return AndroidMenu.onSearchRequested(this); }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) { return AndroidMenu.onCreateOptionsMenu(this, menu); }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { return AndroidMenu.onOptionsItemSelected(this, item); }
    @Override
    protected Dialog onCreateDialog(int id) { return AndroidMenu.onCreateDialog(this, id); }
	
	// Store quick stats as a mapping between object names and a list
	//   of those objects. Each value contains a list of attributes
	//   representing the values of the stats on that object
	Map<String, List<List<String>>> stats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_stats);
        
        // retrieve stats from xml resources
        stats = new HashMap<String, List<List<String>>>();
        getStatsFromXml(this.getString(R.string.potions_quick_stat), R.xml.potions);
        getStatsFromXml(this.getString(R.string.gems_quick_stat),    R.xml.gems);
        getStatsFromXml(this.getString(R.string.scrolls_quick_stat), R.xml.scrolls);

        // populate spinner and wire up changes
        Spinner spinner = (Spinner) findViewById(R.id.quick_stats_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
               this, R.array.quick_stats_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new QuickStatsSelectedListener());
    }
    

    // Handles quick stats spinner changes
    class QuickStatsSelectedListener implements OnItemSelectedListener {
         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	clearStats((View) parent.getParent());
        	String selected = parent.getItemAtPosition(pos).toString();
        	if(!selected.equals("")) displayStats(selected, (View) parent.getParent());
         }

        public void onNothingSelected(AdapterView parent) {
        }
    }
    
    // Clears the quick stats table
    private void clearStats(View quick_stats_view){
    	TableLayout table = (TableLayout) quick_stats_view.findViewById(R.id.quick_stats_table);
	 	table.removeAllViews();
    }
    
    // Retrieve and return the stats stored in the specified android
    //  xml resource
    private void getStatsFromXml(String stat_to_parse, int xml_resource) {
		stats.put(stat_to_parse, new ArrayList<List<String>>());
		List<List<String>> qstats = stats.get(stat_to_parse);
		List<String> qstat = new ArrayList<String>();
		
        XmlResourceParser xpp = getResources().getXml(xml_resource);
        String element_name = "";
        try{
	        xpp.next();
	        int eventType = xpp.getEventType();
	        while (eventType != XmlPullParser.END_DOCUMENT){
		      if(eventType == XmlPullParser.START_TAG){
		    	element_name =  xpp.getName();
		    	if(element_name.equals("row")){
		    		qstat = new ArrayList<String>();
		    		qstats.add(qstat);
		    	}
		      }
		      else if(eventType == XmlPullParser.TEXT && element_name.equals("item")) {
		    	qstat.add(xpp.getText());
		      }
		      eventType = xpp.next();
	         }
        }catch(IOException e) {
        }catch(XmlPullParserException e) {}
    }
    
    // Display the specified stat on the table in the view
    private void displayStats(String stat_to_display, View quick_stats_view){
    	TableLayout table = (TableLayout) quick_stats_view.findViewById(R.id.quick_stats_table);
    	List<List<String>> qstats = stats.get(stat_to_display);
    	boolean header_row = true;
    	for(List<String> qstat : qstats){
    		TableRow tr = new TableRow(quick_stats_view.getContext());
    		tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    		for(String qstat_item : qstat){
    			TextView labelTV = new TextView(quick_stats_view.getContext());
		        labelTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		        labelTV.setText(qstat_item);
		        labelTV.setTextSize(12);
		        labelTV.setPadding(1, 1, 1, 1);
		        labelTV.setMaxWidth(100);
		    	// handle the first row specially
		        if(header_row){
		        	labelTV.setOnClickListener(new ColumnClickedListener());
		        	labelTV.setTextColor(getResources().getColor(R.color.light_blue));
		        }
	            tr.addView(labelTV);
    		}
    		table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    		header_row = false;
    	}
    }
    
    // Returns the current stat selected in the spinner
    private String selectedStat(View parent){
    	return ((Spinner)parent.findViewById(R.id.quick_stats_spinner)).getSelectedItem().toString();
    }
    
    // Variables used for table sorting
    private int sort_column = -1;
    private boolean reverse  = false;
    
    // Sorts the specified stat based on the specified column
    private void sortStats(String stat, String column){
    	List<List<String>> qstats = stats.get(stat);
    	List<String> headers = qstats.remove(0); // pop headers of stats list
    	
    	// find column we're sorting by
    	for(sort_column = 0; sort_column < headers.size(); ++sort_column) 
    		if(headers.get(sort_column).equals(column))
    			break;
    	
    	// actually perform the sort using a custom comparator
    	Collections.sort(qstats, new Comparator<List<String>>() {
    		public int compare(List<String> s1, List<String> s2) {
    			// first try to convert numeric columns to floats b4 comparing
				try{  
				  float f1 = Float.parseFloat(s1.get(sort_column) );
				  float f2 = Float.parseFloat(s2.get(sort_column));
				  if (f1 == f2) return 0; 
				  if ((reverse && f1 > f2) || (!reverse && f1 < f2)) return 1;
				  return -1; 
				} catch( Exception e ){}  
    			if (!reverse)
    				return s1.get(sort_column).compareTo(s2.get(sort_column));
    			return s2.get(sort_column).compareTo(s1.get(sort_column));
    		}
    	});
		reverse = !reverse;
    	qstats.add(0, headers);
    }
    
    // Handles clicks to the column headers by sorting the table
    class ColumnClickedListener implements OnClickListener {
		public void onClick(View v) {
			View parent = (View)v.getParent().getParent().getParent().getParent().getParent();
			String selected = selectedStat(parent);
			sortStats(selected, ((TextView)v).getText().toString());
			clearStats(parent);
			displayStats(selected, parent);
		}
    }
}
