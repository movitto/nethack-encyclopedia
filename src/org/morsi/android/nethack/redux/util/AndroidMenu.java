package org.morsi.android.nethack.redux.util;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.EncyclopediaActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

// Helper methods around the main android menu
public class AndroidMenu {

  // Override functionality of the search button to bring up encyclopedia
    public static boolean onSearchRequested(Activity activity) {
      // bring up the Encyclopedia
      Intent myIntent = new Intent(activity, EncyclopediaActivity.class);
      activity.startActivity(myIntent);
      return false;
    }

    // Override functionality of the search button to search encyclopedia
    // Simply bring up the Android keyboard (typing text in this listview
    //   will filter the list automatically)
    public static boolean onEncyclopediaSearchRequested(ListActivity activity) {
      // bring up the Encyclopedia
      InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      mgr.showSoftInput(activity.getListView(), 0); // InputMethodManager.SHOW_IMPLICIT to only display if no keyboard is open
      return false;
    }

    // Override functionality of the menu button
    public static boolean onCreateOptionsMenu(Activity activity, Menu menu) {
    // bring up our own menu
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

  // Invoked whenever a menu item is selected
  //   (dispatch to appropriate menu item handler)
    public static boolean onOptionsItemSelected(Activity activity, MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
        case R.id.menu_about:
          activity.showDialog(DIALOG_ABOUT_ID);
            return true;
        case R.id.menu_quit:
          activity.moveTaskToBack(true);
            return true;
        default:
            return activity.onOptionsItemSelected(item);
        }
    }

    // Handle the 'About' dialog creation
    static public final int DIALOG_ABOUT_ID = 0;
    public static Dialog onCreateDialog(Activity activity, int id) {
      Dialog dialog = new Dialog(activity, R.style.About);

        switch(id) {
        case DIALOG_ABOUT_ID:
          dialog.setContentView(R.layout.about);
          dialog.setTitle(activity.getString(R.string.about_dialog_title));

          // wire up close button
          Button close = (Button)dialog.findViewById(R.id.about_close);
          close.setOnClickListener(new DialogCloseListener(dialog));

          // wire up project homepage link
          TransformFilter filter = new TransformFilter() {
              public final String transformUrl(final Matcher match, String url) {
                  return "";
              }
          };
          Pattern pattern = Pattern.compile(activity.getString(R.string.app_name));
          String  scheme =    activity.getString(R.string.project_url);
          TextView tv = (TextView) dialog.findViewById(R.id.about_title);
          Linkify.addLinks(tv, pattern, scheme, null, filter);
          
          // wire up author url
          pattern = Pattern.compile(activity.getString(R.string.project_author));
          scheme =    activity.getString(R.string.project_author_url);
          tv = (TextView) dialog.findViewById(R.id.about_authors);
          Linkify.addLinks(tv, pattern, scheme, null, filter);
          
          // wire up license url
          pattern = Pattern.compile(activity.getString(R.string.project_license));
          scheme =    activity.getString(R.string.project_license_url);
          tv = (TextView) dialog.findViewById(R.id.about_license);
          Linkify.addLinks(tv, pattern, scheme, null, filter);
        }
        return dialog;
    }

    // Handles clicks to the 'About' dialog close button
    static class DialogCloseListener implements Button.OnClickListener {
      Dialog dialog_to_close;
      DialogCloseListener(Dialog dialog){
        dialog_to_close = dialog;
      }
      public void onClick(View v){
      dialog_to_close.dismiss();
    }
    }
}
