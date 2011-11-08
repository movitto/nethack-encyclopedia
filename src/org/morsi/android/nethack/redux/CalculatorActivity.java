/**********************************
 * Nethack Encyclopedia - Calculator Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.ArmorCalculator;
import org.morsi.android.nethack.redux.util.DamageCalculator;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

// Tools to calculate Nethack stats based on user input
public class CalculatorActivity extends Activity {

    // Override menu / about dialog handlers
    @Override
    public boolean onSearchRequested() { return AndroidMenu.onSearchRequested(this); }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return AndroidMenu.onCreateOptionsMenu(this, menu); }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { return AndroidMenu.onOptionsItemSelected(this, item); }
    @Override
    protected Dialog onCreateDialog(int id) { return AndroidMenu.onCreateDialog(this, id); }

    // actual calculators
    private ArmorCalculator  armor;
    private DamageCalculator damage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        // hide calculator tools until one is selected by user
        findViewById(R.id.armor_calculator).setVisibility(View.INVISIBLE);

        // setup calculator spinner
        Spinner spinner = (Spinner) findViewById(R.id.calculator_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
               this, R.array.calculator_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new CalculatorSelectedListener());
        
        armor  = new ArmorCalculator(this);
        damage = new DamageCalculator(this);
        
        armor.onCreate();
        damage.onCreate();
        
        armor.updateView();
        damage.updateView();
    }

    // Handles calculator spinner changes,
    // shows/hides appropriate calculator sections
    class CalculatorSelectedListener implements OnItemSelectedListener {
         // Show view specified by id on the calculator
         private void showView(int view_id){
           View v = findViewById(view_id);
           v.setVisibility(View.VISIBLE);
           ViewGroup.LayoutParams lp = v.getLayoutParams();
           lp.height = RelativeLayout.LayoutParams.FILL_PARENT;
           v.setLayoutParams(lp);
         }

         // Hide view specified by id on the calculator
         private void hideView(int view_id){
           View v = findViewById(view_id);
           v.setVisibility(View.INVISIBLE);
           ViewGroup.LayoutParams params = v.getLayoutParams();
           params.height = 0;
           v.setLayoutParams(params);
         }

         // Handles calculator spinner changes
         public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        String selected = parent.getItemAtPosition(pos).toString();
	        if(selected.equals(view.getContext().getString(R.string.armor_calculator))){
	          showView(R.id.armor_calculator);
	          hideView(R.id.damage_calculator);
	        }else if(selected.equals(view.getContext().getString(R.string.damage_calculator))){
	          showView(R.id.damage_calculator);
	          hideView(R.id.armor_calculator);
	        }else{
	          hideView(R.id.armor_calculator);
	          hideView(R.id.damage_calculator);
	        }
         }

        public void onNothingSelected(AdapterView<?> parent) {}
    }
    
    public void onClickDamageFeatureToggleButton(View target) {
    	damage.toggleDamageFeatures(target);
    }
}
