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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.Android;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.Encyclopedia;
import org.morsi.android.nethack.redux.util.EncyclopediaEntry;
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
        	// parse the list of topics from a registry file
            NString registry = Android.assetToNString(c[0].getAssets(), "encyclopedia/registry");     
            encyclopedia.retrieveTopics(registry);
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      // automatically wires the list of topic names to
      //  list items to display
      setListAdapter(new ArrayAdapter<String>(this, R.layout.encyclopedia, alphabet_sections));

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
    		String section = ((TextView) view).getText().toString();
    		EncyclopediaActivity.in_alphabetical_mode = false;
    		setListAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.encyclopedia, encyclopedia.topicNames(section)));
    	}else{
    		String topic = ((TextView) view).getText().toString();
    		initiatePopupWindow(EncyclopediaActivity.encyclopedia.get(topic).populate(view.getContext()));
    	}
      }
    }

    // Show popup window when encyclopedia page is clicked
    private void initiatePopupWindow(EncyclopediaEntry entry) {
        try {
          // TODO display error if entry is null
            LayoutInflater inflater = (LayoutInflater) EncyclopediaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.encyclopedia_popup,
                    (ViewGroup) findViewById(R.id.encyclopedia_page_popup));
            PopupWindow pw = new PopupWindow(layout, 230, 400, true);

            TextView text = (TextView) layout.findViewById(R.id.encyclopedia_page_title);
            text.setText(entry.topic);
            WebView web_view = (WebView) layout.findViewById(R.id.encyclopedia_page_content);

            // intercept link clicks, redirect to our encyclopedia
            web_view.setWebViewClient(new WebViewClient() {
              @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                if(url.substring(0, 22).equals("fake://morsi.org/wiki/")){
                  String new_topic = url.substring(22, url.length());
                  initiatePopupWindow(EncyclopediaActivity.encyclopedia.get(new_topic).populate(view.getContext()));
                  return true;
                }
                return false;
                }
            });

            // need to use loadDataWithBaseURL
            // http://code.google.com/p/android-rss/issues/detail?id=15
            web_view.loadDataWithBaseURL("fake://morsi.org", entry.content.toString(),  "text/html", "utf-8", null);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button cancelButton = (Button) layout.findViewById(R.id.encyclopedia_page_close);
            cancelButton.setOnClickListener(new EncyclopediaPageClosedListener(pw));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handles clicks to the closed button on the encyclopedia page
    private class EncyclopediaPageClosedListener implements Button.OnClickListener {
      private PopupWindow popup_window;

      public EncyclopediaPageClosedListener(PopupWindow lpw){
        popup_window = lpw;
      }
    public void onClick(View v) {
      popup_window.dismiss();
    }
    };

}
