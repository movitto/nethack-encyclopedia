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
import org.morsi.android.nethack.redux.util.TutorialItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;

public class TutorialActivity extends ExpandableListActivity  implements OnChildClickListener 
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
    LinkedHashMap<String, ArrayList<TutorialItem>> tutorial_content;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        tutorial_content = new LinkedHashMap<String, ArrayList<TutorialItem>>();
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
    
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ExpandableListAdapter mAdapter = parent.getExpandableListAdapter();
        mAdapter.getChild(groupPosition, childPosition);
        String tutorial_section = (String)tutorial_content.keySet().toArray()[groupPosition];
        TutorialItem item = tutorial_content.get(tutorial_section).get(childPosition);
        
        if(item.image != null){
	    	LayoutInflater inflater = (LayoutInflater) TutorialActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View layout = inflater.inflate(R.layout.tutorial_popup, (ViewGroup) findViewById(R.id.tutorial_popup));
	        PopupWindow pw = new PopupWindow(layout, 230, 400, true);
	        
	        ImageView img = (ImageView)layout.findViewById(R.id.tutorial_popup_image);
	        Resources res = getResources();
	        int resID = res.getIdentifier("tutorial_" + item.image , "drawable", getPackageName());
	        Drawable drawable = res.getDrawable(resID);
	        img.setImageDrawable(drawable );
	        
	        Button close = (Button)layout.findViewById(R.id.tutorial_close_button);
	        close.setOnClickListener(new TutorialPopupClickListener(pw));

	        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
	        
	    	return true;
	    }
        return false;
    }
    
    // Handles clicks to the closed button on the encyclopedia page
    private class TutorialPopupClickListener implements Button.OnClickListener {
	      private PopupWindow popup_window;
	
	      public TutorialPopupClickListener(PopupWindow lpw){
	        popup_window = lpw;
	      }
	      public void onClick(View v) {
	        popup_window.dismiss();
	      }
    };
    
    // Retrieve the content stored in the specified android tutorial resource
    private void getTutorialFromXml() {
        String current_section       = null;
        ArrayList<TutorialItem> current_section_content = null;
        TutorialItem current_item = null;
        
        try{
          XmlResourceParser xpp = getResources().getXml(R.xml.tutorial);
          String element_name = ""; int group_id = -1;;
          
          int eventType =  xpp.next(); 
          while (eventType != XmlPullParser.END_DOCUMENT){
        	  if(eventType == XmlPullParser.START_TAG){
        		  element_name =  xpp.getName();
        		  if(element_name.equals("section")){
        			  current_section_content = new ArrayList<TutorialItem>();
        			  current_section = xpp.getAttributeValue(null, "name");
            		  group_id = 0;
        			  
        		  }else if(element_name.equals("item")){
        			  current_item = new TutorialItem(group_id++);
        		  }
        		  
        	  }else if(eventType == XmlPullParser.TEXT) {
        		  if(element_name.equals("content")){
        			  String content = xpp.getText();
        			  current_item.content = content;
        			  
        		  }else if(element_name.equals("image"))
        			  current_item.image = xpp.getText();
        		  
        	  }else if(eventType == XmlPullParser.END_TAG){
        		  element_name =  xpp.getName();
        		  if(element_name.equals("section"))
				  	tutorial_content.put(current_section, current_section_content);
        		  else if(element_name.equals("item"))
        			  current_section_content.add(current_item);
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
			for( TutorialItem section_item : tutorial_content.get(section)) {
				Map<String, String> section_item_atts = new LinkedHashMap<String, String>();
				section_item_atts.put("content", section_item.content);
				section_content.add( section_item_atts );
			}
			sections.add( section_content );
		}
		return sections;
	}
}
