/**********************************
 * Nethack Encyclopedia - Nethack Encyclopedia Activity
 * 
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack;

import org.morsi.android.nethack.R;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

// Main NethackEncyclopedia entry point activity,
//   simply provide means which to access sub-activities
public class NethackEncyclopedia extends BaseNethackActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
    //public void onClickTutorialButton(View target) {
    //}
    public void onClickAboutButton(View target) {
    	showDialog(DIALOG_ABOUT_ID);
    }
    
    
}