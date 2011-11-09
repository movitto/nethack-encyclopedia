/**********************************
 * Nethack Encyclopedia - Tutorial Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleExpandableListAdapter;

public class TutorialActivity extends ExpandableListActivity
{
	  // Override menu / about dialog handlers
    @Override
    public boolean onSearchRequested() { return AndroidMenu.onSearchRequested(this); }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return AndroidMenu.onCreateOptionsMenu(this, menu); }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { return AndroidMenu.onOptionsItemSelected(this, item); }
    @Override
    protected Dialog onCreateDialog(int id) { return AndroidMenu.onCreateDialog(this, id); }
    
    // hash of tutorial sections to items in those sections (use linked hash to preserve order)
    LinkedHashMap<String, ArrayList<String>> tutorial_content;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        tutorial_content = new LinkedHashMap<String, ArrayList<String>>();
        getTutorialFromXml();
        setContentView(R.layout.tutorial);
		SimpleExpandableListAdapter expListAdapter =
			new SimpleExpandableListAdapter(
				this,
				createGroupList(),	// groupData describes the first-level entries
				R.layout.tutorial_child_row,	// Layout for the first-level entries
				new String[] { "tutorial_section" },	// Key in the groupData maps to display
				new int[] { R.id.tutorial_item },		// Data under "colorName" key goes into this TextView
				createChildList(),	// childData describes second-level entries
				R.layout.tutorial_child_row,	// Layout for second-level entries
				new String[] { "content" },	// Keys in childData maps to display
				new int[] { R.id.tutorial_item }	// Data under the keys above go into these TextViews
			);
		setListAdapter( expListAdapter );
    }
    
    // Retrieve the content stored in the specified android tutorial resource
    private void getTutorialFromXml() {
        String current_section       = null;
        ArrayList<String> current_section_content = null;

        
        try{
          XmlResourceParser xpp = getResources().getXml(R.xml.tutorial);
          int eventType =  xpp.next(); String element_name = "";
          while (eventType != XmlPullParser.END_DOCUMENT){
        	  if(eventType == XmlPullParser.START_TAG){
        		  element_name =  xpp.getName();
        		  if(element_name.equals("section")){
        			  current_section_content = new ArrayList<String>();
        			  current_section = xpp.getAttributeValue(null, "name");
        			  // FIXME we miss the last row because of this
    				  tutorial_content.put(current_section, current_section_content);
        		  }
        	  }
        	  else if(eventType == XmlPullParser.TEXT && element_name.equals("content")) {
        		  current_section_content.add(xpp.getText());
        	  }
        	  eventType = xpp.next();
          }
        }catch(IOException e) {
        }catch(XmlPullParserException e) {  }
    }

    // return a list of hashes of the static string 'tutorial_section' to section names
	private List<Map<String, String>> createGroupList() {
		List<Map<String, String>> sections = new ArrayList<Map<String, String>>();
		for(String section : tutorial_content.keySet()){
			Map<String, String> m = new LinkedHashMap<String, String>();
			m.put( "tutorial_section", section);
			sections.add( m );
		}
		return sections;
	}

	// return a two level list, first level corresponding to the sections, the second level
	//   corresponding to a list of hash maps of key/attributes to use when rendering the items in that section
	private List<List<Map<String, String>>> createChildList() {
		List<List<Map<String, String>>> sections = new ArrayList<List<Map<String, String>>>();
		for( String section : tutorial_content.keySet() ) {
			ArrayList<Map<String, String>> section_content = new ArrayList<Map<String, String>>();
			for( String section_item : tutorial_content.get(section)) {
				Map<String, String> section_item_atts = new LinkedHashMap<String, String>();
				section_item_atts.put("content", section_item);
				section_content.add( section_item_atts );
			}
			sections.add( section_content );
		}
		return sections;
	}
}
