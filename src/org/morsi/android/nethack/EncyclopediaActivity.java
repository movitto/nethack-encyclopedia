/**********************************
 * Nethack Encyclopedia - Encyclopedia Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/
package org.morsi.android.nethack;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
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

import org.morsi.android.nethack.R;
import org.morsi.android.nethack.util.Android;
import org.morsi.android.nethack.util.AndroidMenu;
import org.morsi.android.nethack.util.Encyclopedia;
import org.morsi.android.nethack.util.EncyclopediaEntry;

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


  // Store a mapping of topic names to EncylopediaEntry objects
    public static Encyclopedia encyclopedia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
      encyclopedia = new Encyclopedia();
      super.onCreate(savedInstanceState);

      // parse the list of topics from a registry file
      encyclopedia.parseTopics(Android.assetToString(getAssets(), "encyclopedia/registry"));

      // automatically wires the list of topic names to
      //  list items to display
      setListAdapter(new ArrayAdapter<String>(this, R.layout.encyclopedia, encyclopedia.topicNames()));

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
      String topic = ((TextView) view).getText().toString();
      EncyclopediaEntry entry = EncyclopediaActivity.encyclopedia.lookup(topic);
      entry.populateContent(Android.assetToString(getAssets(), "encyclopedia/" + Integer.toString(entry.catalog_number)));
          initiatePopupWindow(entry);
    }
    }

    // Show popup window when encyclopedia page is clicked
    private void initiatePopupWindow(EncyclopediaEntry entry) {
        try {
          // TODO display error if entry is null
            LayoutInflater inflater = (LayoutInflater) EncyclopediaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.encyclopedia_popup,
                    (ViewGroup) findViewById(R.id.encyclopedia_page_popup));
            PopupWindow pw = new PopupWindow(layout, 300, 470, true);

            TextView text = (TextView) layout.findViewById(R.id.encyclopedia_page_title);
            text.setText(entry.topic);
            WebView web_view = (WebView) layout.findViewById(R.id.encyclopedia_page_content);

            // intercept link clicks, redirect to our encyclopedia
            web_view.setWebViewClient(new WebViewClient() {
              @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                if(url.substring(0, 6).equals("/wiki/")){
                  String new_topic = url.substring(6, url.length());
                  initiatePopupWindow(EncyclopediaActivity.encyclopedia.lookup(new_topic));
                  return true;
                }
                return false;
                }
            });

      if(!entry.is_redirect){
        // if we are not redirecting, display page
        // http://code.google.com/p/android-rss/issues/detail?id=15
        web_view.loadData(entry.content,  "text/html", "utf-8");
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
      }else{
        initiatePopupWindow(EncyclopediaActivity.encyclopedia.lookup(entry.redirect_to));
      }

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
