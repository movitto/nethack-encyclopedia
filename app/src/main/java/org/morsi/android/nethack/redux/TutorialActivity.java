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
import org.morsi.android.nethack.redux.util.Tutorial;
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
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

public class TutorialActivity extends ExpandableListActivity  implements OnChildClickListener 
{
	  // Override menu / about_dialog dialog handlers
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
        tutorial_content = Tutorial.getTutorialFromXml(this);
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
			){
				@Override
				public View getChildView(int groupPosition, int childPosition,
										 boolean isLastChild, View convertView, ViewGroup parent) {
					View r = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
					LinearLayout layout = (LinearLayout) r;
					for (int i = 0; i <=  layout.getChildCount(); i++) {
						View v = layout.getChildAt(i);
						if (v instanceof TextView)
							((TextView)(v)).setText(Html.fromHtml(((TextView)(v)).getText().toString()));
					}

					return r;
				}
			};
		setListAdapter( expListAdapter );
    }
    
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ExpandableListAdapter mAdapter = parent.getExpandableListAdapter();
        mAdapter.getChild(groupPosition, childPosition);
        String tutorial_section = (String)tutorial_content.keySet().toArray()[groupPosition];
        TutorialItem item = tutorial_content.get(tutorial_section).get(childPosition);
        
        if(item.image != null){
            TutorialPopup.showPopup(this, item.image);
	    	return true;
	    }
        return false;
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
