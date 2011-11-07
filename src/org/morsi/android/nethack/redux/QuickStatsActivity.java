/**********************************
 * Nethack Encyclopedia - Quick Stats Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;
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
        getStatsFromXml(this.getString(R.string.tools_quick_stat), R.xml.tools);

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
          if(pos != 0) displayStats(selected, (View) parent.getParent());
         }

        public void onNothingSelected(AdapterView<?> parent) {
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
    // FIXME this method could use alot of work (remove specific styling here, theme it and/or pull from xml)
    private void displayStats(String stat_to_display, View quick_stats_view){
      TableLayout table = (TableLayout) quick_stats_view.findViewById(R.id.quick_stats_table);
      List<List<String>> qstats = stats.get(stat_to_display);
      boolean header_row = true; int cols = -1;

      // iterate over each statistic row
      for(List<String> qstat : qstats){
    	boolean alternate_col = false;
        TableRow tr = new TableRow(quick_stats_view.getContext());
        tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        // iterate over each column in row
        for(String qstat_item : qstat){

          // set column display properties
          TextView labelTV = new TextView(quick_stats_view.getContext());
          labelTV.setText(qstat_item);
          labelTV.setEllipsize(TruncateAt.MARQUEE);
          labelTV.setTextSize(11);
          labelTV.setPadding(4, 4, 4, 4);
          labelTV.setGravity(Gravity.CENTER);
    	  labelTV.setMaxWidth(40);
          labelTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

          // wire up clicks to the header row to sort the table
          if(header_row){
            labelTV.setOnClickListener(new ColumnClickedListener());
            labelTV.setTextColor(getResources().getColor(R.color.light_blue));

          // alternate colors on columns
          }else{
        	  labelTV.setBackgroundResource((alternate_col = !alternate_col) ?
        			                         R.color.gray : R.color.light_gray);
          }

          // add column to row
          tr.addView(labelTV);
        }
        
        // fill in blank columns
        if(qstat.size() < cols){
        	for(int i = 0; i < cols - qstat.size(); ++i){
        		TextView blank = new TextView(quick_stats_view.getContext());
            	blank.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            	blank.setBackgroundResource((alternate_col = !alternate_col) ?
                        R.color.gray : R.color.light_gray);
        		tr.addView(blank);
        	}
        	//((LayoutParams)tr.getChildAt(tr.getChildCount() - 1).getLayoutParams()).span =
        	//	cols - qstat.size() + 1;
        }

        // add row to table
        table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
    	// add a border beneath header row
        if(header_row){
        	cols = qstat.size();
        	TableRow border_row = new TableRow(quick_stats_view.getContext());
        	LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        	lp.setMargins(1, 1, 1, 2);
        	border_row.setPadding(1, 1, 1, 2);
        	border_row.setLayoutParams(lp);
        	border_row.setBackgroundResource(R.color.white);
        	table.addView(border_row, lp);
            header_row = false;
        }

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
         String item1 = sort_column < s1.size() ? s1.get(sort_column) : "";
         String item2 = sort_column < s2.size() ? s2.get(sort_column) : "";

         // first try to convert numeric columns to floats b4 comparing
        try{
          float f1 = Float.parseFloat(item1);
          float f2 = Float.parseFloat(item2);
          if (f1 == f2) return 0;
          if ((reverse && f1 > f2) || (!reverse && f1 < f2)) return 1;
          return -1;
        } catch( Exception e ){}
          if (!reverse)
            return item1.compareTo(item2);
          return item2.compareTo(item1);
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
