/**********************************
 * Nethack Encyclopedia - Base Activity
 * 
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
 
// Provides common routines and functionality for 
// all NethackEncyclopedia activities
public class BaseNethackActivity  extends Activity  {
	
	// Override functionality of the search button
    @Override
    public boolean onSearchRequested() {
    	// bring up the Encyclopedia
    	Intent myIntent = new Intent(BaseNethackActivity.this, EncyclopediaActivity.class);
    	BaseNethackActivity.this.startActivity(myIntent);
    	return false;
    }

    // Override functionality of the menu button
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		// bring up our own menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    
	// Invoked whenever a menu item is selected
	//   (dispatch to appropriate menu item handler)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
        case R.id.menu_about:
        	showDialog(DIALOG_ABOUT_ID);
            return true;
        case R.id.menu_quit:
        	moveTaskToBack(true);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    // Handle the 'About' dialog creation
    static final int DIALOG_ABOUT_ID = 0;
    protected Dialog onCreateDialog(int id) {
    	Context mContext = this;
    	Dialog dialog = new Dialog(mContext);

        switch(id) {
        case DIALOG_ABOUT_ID:
        	dialog.setContentView(R.layout.about);
        	dialog.setTitle(this.getString(R.string.about_dialog_title));
        	Button close = (Button)dialog.findViewById(R.id.about_close);
        	close.setOnClickListener(new DialogCloseListener(dialog));
        }
        return dialog;
    }
    
    // Handles clicks to the 'About' dialog close button
    class DialogCloseListener implements Button.OnClickListener {
    	Dialog dialog_to_close;
    	DialogCloseListener(Dialog dialog){
    		dialog_to_close = dialog;
    	}
   		public void onClick(View v){
			dialog_to_close.dismiss();
		}
    }
}
