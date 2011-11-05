/**********************************
 * Nethack Encyclopedia - Nethack Encyclopedia Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

// Main NethackEncyclopedia entry point activity,
//   simply provide means which to access sub-activities
public class NethackEncyclopedia extends Activity {
  // Override menu / about dialog handlers
    @Override
    public boolean onSearchRequested() { return AndroidMenu.onSearchRequested(this); }
  @Override
    public boolean onCreateOptionsMenu(Menu menu) { return AndroidMenu.onCreateOptionsMenu(this, menu); }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { return AndroidMenu.onOptionsItemSelected(this, item); }
    @Override
    protected Dialog onCreateDialog(int id) { return AndroidMenu.onCreateDialog(this, id); }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // loading the encyclopedia might take a while due to a lot of
        // content so launch the worker thread immediately
        EncyclopediaActivity.load_encyclopedia(this);
    }
    
    public void onClickQuickStatsButton(View target) {
      Intent myIntent = new Intent(NethackEncyclopedia.this, QuickStatsActivity.class);
      NethackEncyclopedia.this.startActivity(myIntent);
    }
    public void onClickEncyclopediaButton(View target) {
      Intent myIntent = new Intent(NethackEncyclopedia.this, EncyclopediaActivity.class);
      NethackEncyclopedia.this.startActivity(myIntent);
    }
    public void onClickCalculatorButton(View target) {
      Intent myIntent = new Intent(NethackEncyclopedia.this, CalculatorActivity.class);
      NethackEncyclopedia.this.startActivity(myIntent);
    }
    public void onClickTutorialButton(View target) {
      Intent myIntent = new Intent(NethackEncyclopedia.this,TutorialActivity.class);
      NethackEncyclopedia.this.startActivity(myIntent);
    }
    public void onClickAboutButton(View target) {
      showDialog(AndroidMenu.DIALOG_ABOUT_ID);
    }


}
