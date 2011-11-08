/**********************************
 * Nethack Encyclopedia - Quick Stats Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.QuickStat;
import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
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

    // List of quick stat categories containing stats
    ArrayList<QuickStatCategory> stats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_stats);

        // retrieve stats from xml resources
        stats = new ArrayList<QuickStatCategory>();
        getStatsFromXml(this.getString(R.string.potions_quick_stat), R.xml.potions);
        getStatsFromXml(this.getString(R.string.gems_quick_stat),    R.xml.gems);
        getStatsFromXml(this.getString(R.string.scrolls_quick_stat), R.xml.scrolls);
        getStatsFromXml(this.getString(R.string.tools_quick_stat),   R.xml.tools);

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
             QuickStatCategory selected = selectedQuickStat(parent);
             if(pos != 0) displayStats(selected, (View) parent.getParent());
         }

         public void onNothingSelected(AdapterView<?> parent) {
         }
    }

    // Retrieve and return the stats stored in the specified android xml resource
    private void getStatsFromXml(String stat_to_parse, int xml_resource) {
      QuickStatCategory category = new QuickStatCategory(stat_to_parse);
      stats.add(category);

      ArrayList<String> current_row = null;

      try{
        XmlResourceParser xpp = getResources().getXml(xml_resource);
        int eventType =  xpp.next(); 
        String element_name = ""; String weight = "";
        while (eventType != XmlPullParser.END_DOCUMENT){
      	  if(eventType == XmlPullParser.START_TAG){
    		  element_name =  xpp.getName();
    		  if(element_name.equals("row"))
    			  current_row = new ArrayList<String>();
    		  else if(element_name.equals("column"))
    			  weight = xpp.getAttributeValue(null, "weight");
    		  
    	  }else if(eventType == XmlPullParser.TEXT){
    		  if(element_name.equals("item"))
    			  current_row.add(xpp.getText());
    		  else if(element_name.equals("column")){
    			  Float fweight = weight != null ? Float.parseFloat(weight) : -1;
    			  category.add_column(xpp.getText(), fweight);
    		  }
    		  
    	  }else if(eventType == XmlPullParser.END_TAG && xpp.getName().equals("row"))
    		  category.add_stats(current_row);
    		  
    	  eventType = xpp.next();
        }
      }catch(IOException e) {
      }catch(XmlPullParserException e) {  }
    }
    
    // Clears the quick stats table
    private void clearStats(View quick_stats_view){
      TableLayout table = (TableLayout) quick_stats_view.findViewById(R.id.quick_stats_table);
      table.removeAllViews();
    }

    // Display the specified stat on the table in the view
    // FIXME this method could use alot of work (remove specific styling here, theme it and/or pull from xml)
    private void displayStats(QuickStatCategory category, View quick_stats_view){
      TableLayout table = (TableLayout) quick_stats_view.findViewById(R.id.quick_stats_table);
 
      List<String> columns   = category.get_columns();
      List<Float> weights    = category.get_weights();
      List<QuickStat> rstats = category.get_stats();
      
      // add header row to table
	  TableRow tr = new TableRow(quick_stats_view.getContext());
      for(int i = 0; i < columns.size(); ++i){
    	  TextView tv = new TextView(quick_stats_view.getContext());
          tv.setText(columns.get(i));
          tv.setEllipsize(TruncateAt.MARQUEE);
          
          // wire up click listener to stort columns
          tv.setOnClickListener(new ColumnClickedListener());
          tv.setTextColor(getResources().getColor(R.color.light_blue));
          
          // if no weight specified, weigh all columns equally
          float weight = weights.get(i);
          if(weight == -1) weights.set(i, new Double(1.0/weights.size()).floatValue());
          
          tv.setGravity(Gravity.CENTER);
          tv.setLayoutParams(new LayoutParams(0, LayoutParams.FILL_PARENT, weights.get(i)));
          tr.addView(tv);
      }
	  tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	  table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	  
	  // add border row beneath header
	  TableRow border_row = new TableRow(quick_stats_view.getContext());
  	  LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
  	  lp.setMargins(1, 1, 1, 2);
  	  border_row.setPadding(1, 1, 1, 2);
  	  border_row.setLayoutParams(lp);
  	  border_row.setBackgroundResource(R.color.white);
  	  table.addView(border_row, lp);
	  
  	  boolean alternate_col;
  	  
  	  // add each quick stat to table
  	  for(QuickStat stat : rstats){
  		  tr = new TableRow(quick_stats_view.getContext());
          tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
          
          alternate_col = false;
          
  		  for(int i = 0; i < columns.size(); ++i){
  			String value = stat.get_value(i); 
  			if(value != null){
  	  			TextView tv = new TextView(quick_stats_view.getContext());
	            tv.setText(value);
	            tv.setEllipsize(TruncateAt.MARQUEE);
	            
	            // alternate background colors
	            tv.setBackgroundResource((alternate_col = !alternate_col) ? R.color.gray : R.color.light_gray);

	            tv.setGravity(Gravity.CENTER);
	            tv.setLayoutParams(new LayoutParams(0, LayoutParams.FILL_PARENT, weights.get(i)));
	            tr.addView(tv);
  			}
  		  }
  		  
          // fill in empty columns
          for(int i = stat.num_values(); i < columns.size(); ++i){
            TextView blank = new TextView(quick_stats_view.getContext());
            blank.setLayoutParams(new LayoutParams(0, LayoutParams.FILL_PARENT, weights.get(i)));
            blank.setBackgroundResource((alternate_col = !alternate_col) ? R.color.gray : R.color.light_gray);
        	tr.addView(blank);
          }
  		  
  		  table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
  	  }
  	  
          // set column display properties
          /*labelTV.setTextSize(11);
          labelTV.setPadding(4, 4, 4, 4);
    	  labelTV.setMaxWidth(40);*/
    }
    
    private String selectedQuickStatName(View parent){
    	return ((Spinner)parent.findViewById(R.id.quick_stats_spinner)).getSelectedItem().toString();
    }

    // Returns the current stat selected in the spinner
    private QuickStatCategory selectedQuickStat(View parent){
        String selected = selectedQuickStatName(parent);
        for(QuickStatCategory category : stats)
    	    if(category.name.equals(selected))
    	        return category;
        return null;
    }

    // Handles clicks to the column headers by sorting the table
    class ColumnClickedListener implements OnClickListener {
	    public void onClick(View v) {
	        View parent = (View)v.getParent().getParent().getParent().getParent().getParent();
	        QuickStatCategory selected = selectedQuickStat(parent); 
	        selected.sort_stats(((TextView)v).getText().toString());
	        clearStats(parent);
	        displayStats(selected, parent);
	    }
    }
}
