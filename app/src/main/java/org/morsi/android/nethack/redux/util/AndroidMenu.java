package org.morsi.android.nethack.redux.util;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.EncyclopediaActivity;
import org.morsi.android.nethack.redux.dialogs.AboutDialog;
import org.morsi.android.nethack.redux.dialogs.ItemDialog;
import org.morsi.android.nethack.redux.dialogs.LevelDialog;
import org.morsi.android.nethack.redux.dialogs.PlayerDialog;
import org.morsi.android.nethack.redux.dialogs.NotesDialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

// Helper methods around the main android menu
public class AndroidMenu {

    // Override functionality of the search button to bring up encyclopedia
    public static boolean onSearchRequested(Activity activity) {
        // bring up the Encyclopedia
        activity.startActivity(new Intent(activity, EncyclopediaActivity.class));
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
    static public final int DIALOG_ABOUT_ID  = 0;
    static public final int DIALOG_PLAYER_ID = 1;
    static public final int DIALOG_LEVEL_ID  = 2;
    static public final int DIALOG_NOTES_ID  = 3;
    static public final int DIALOG_ITEM_ID   = 4;

    public static Dialog onCreateDialog(Activity activity, int id) {
        Dialog dialog = new Dialog(activity, R.style.About);

        switch (id) {
            case DIALOG_ABOUT_ID:
                dialog = new AboutDialog(activity);
                break;
            case DIALOG_PLAYER_ID:
                dialog = new PlayerDialog(activity);
                break;
            case DIALOG_LEVEL_ID:
                dialog = new LevelDialog(activity);
                break;
            case DIALOG_NOTES_ID:
                dialog = new NotesDialog(activity);
                break;
            case DIALOG_ITEM_ID:
                dialog = new ItemDialog(activity);
                break;
        }

        return dialog;
    }
}
