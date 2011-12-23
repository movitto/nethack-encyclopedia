/**********************************
 * Nethack Encyclopedia - Encyclopedia Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/
package org.morsi.android.nethack.redux;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.Android;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.Encyclopedia;
import org.morsi.android.nethack.redux.util.NString;

// Provides access to view html based Nethack Encyclopedia articles
public class EncyclopediaActivity extends ListActivity {

    // Override menu / about dialog handlers
    @Override
    public boolean onSearchRequested() {return AndroidMenu.onEncyclopediaSearchRequested(this); }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return AndroidMenu.onCreateOptionsMenu(this, menu); }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { return AndroidMenu.onOptionsItemSelected(this, item); }
    @Override
    protected Dialog onCreateDialog(int id) { return AndroidMenu.onCreateDialog(this, id); }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !EncyclopediaActivity.in_alphabetical_mode) {
        	setListAdapter(new ArrayAdapter<String>(this, R.layout.encyclopedia, alphabet_sections));
        	EncyclopediaActivity.in_alphabetical_mode = true;
        	return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // divide encyclopedia into alphabetical sections
    public static boolean in_alphabetical_mode = true;
	public static String current_section;

	// current popup window being displayed
	public static String current_popup_topic = null;

    // alphabet sections
    public static final List<String> alphabet_sections =
      Arrays.asList(Encyclopedia.symbol_string, Encyclopedia.number_string, "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

    // Store a mapping of topic names to EncylopediaEntry objects
    public static Encyclopedia encyclopedia;
    
    // helper to load the encyclopedia in the background
    public static void load_encyclopedia(Context c){
        encyclopedia = new Encyclopedia();
        new LoadEncyclopediaTask().execute(c);

    }
    
    // Asynchronous task to load encyclopedia in the background
    //  need to pass in a context so as to retrieve encyclopedia from application's assets
    private static class LoadEncyclopediaTask extends AsyncTask<Context, Void, Void> {
        protected Void doInBackground(Context... c) {
        	// parse the list of topics and redirects from a registry file
            NString registry  = Android.assetToNString(c[0].getAssets(), "encyclopedia/registry");     
            NString redirects = Android.assetToNString(c[0].getAssets(), "encyclopedia/redirects");     
            encyclopedia.retrieveTopics(registry, redirects);
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
		// automatically wires the list of topic names to list items to display
		if(EncyclopediaActivity.in_alphabetical_mode)
      setListAdapter(new ArrayAdapter<String>(this, R.layout.encyclopedia, alphabet_sections));
		else
			setListAdapter(new ArrayAdapter<String>(this.getBaseContext(), R.layout.encyclopedia, encyclopedia.topicNames(EncyclopediaActivity.current_section)));

      // display the list, enable filtering when the user types
      //   characters and wire up item click listener
      ListView lv = getListView();
      lv.setTextFilterEnabled(true);
      lv.setOnItemClickListener(new EncyclopediaItemClickedListener());
    };


    // Handles encyclopedia article clicks
    private class EncyclopediaItemClickedListener implements OnItemClickListener {
      public EncyclopediaItemClickedListener(){
        super();
      }

      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	if(EncyclopediaActivity.in_alphabetical_mode){
				EncyclopediaActivity.current_section = ((TextView) view).getText().toString();
    		EncyclopediaActivity.in_alphabetical_mode = false;
				setListAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.encyclopedia, encyclopedia.topicNames(EncyclopediaActivity.current_section)));
    	}else{
				//create encyclopedia page activity when topic is picked
				EncyclopediaActivity.current_popup_topic = ((TextView) view).getText().toString();
				Intent EncPage = new Intent(EncyclopediaActivity.this, EncyclopediaPage.class);
				EncPage.putExtra("page", EncyclopediaActivity.current_popup_topic);
				startActivity(EncPage);
    	}
      }
    }
}
