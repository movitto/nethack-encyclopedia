/**********************************
 * Nethack Encyclopedia - Calculator Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.calculators.ArmorCalculator;
import org.morsi.android.nethack.redux.calculators.DamageCalculator;
import org.morsi.android.nethack.redux.calculators.SpellsCalculator;
import org.morsi.android.nethack.redux.util.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

// Tools to calculate Nethack stats based on user input
public class CalculatorActivity extends Activity {
    // actual calculators
    private ArmorCalculator  armor;
    private DamageCalculator damage;
    private SpellsCalculator spells;

    private Spinner calculatorSpinner(){
        return (Spinner) findViewById(R.id.calculator_spinner);
    }

    private ArrayAdapter<CharSequence> spinnerAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.calculator_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private String selectedCalculator(){
        return calculatorSpinner().getSelectedItem().toString();
    }

    // Override menu / about_dialog dialog handlers
    @Override
    public boolean onSearchRequested() {
        return AndroidMenu.onSearchRequested(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return AndroidMenu.onCreateOptionsMenu(this, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return AndroidMenu.onOptionsItemSelected(this, item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return AndroidMenu.onCreateDialog(this, id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        // hide calculator tools until one is selected by user
        findViewById(R.id.armor_calculator).setVisibility(View.INVISIBLE);

        // setup calculator calculatorSpinner
        calculatorSpinner().setAdapter(spinnerAdapter());
        handleSpinnerSelection();
        
        armor  = new ArmorCalculator(this);
        damage = new DamageCalculator(this);
        spells = new SpellsCalculator(this);
        
        armor.onCreate();
        damage.onCreate();
        spells.onCreate();
        
        armor.updateView();
        damage.updateView();
        spells.updateView();
    }

    private boolean armorSelected(){
        return selectedCalculator().equals("Armor");
    }

    private boolean damageSelected(){
        return selectedCalculator().equals("Damage");
    }

    private boolean spellsSelected(){
        return selectedCalculator().equals("Spells");
    }

    private void hideAll(){
        UI.hideView(this, R.id.armor_calculator);
        UI.hideView(this, R.id.damage_calculator);
        UI.hideView(this, R.id.spells_calculator);
    }

    private void handleSpinnerSelection(){
        final Activity activity = this;

        calculatorSpinner().setOnItemSelectedListener(new OnItemSelectedListener() {
            // Handles calculator calculatorSpinner changes
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                hideAll();

                if(armorSelected()){
                    UI.showView(activity, R.id.armor_calculator);

                }else if(damageSelected()){
                    UI.showView(activity, R.id.damage_calculator);

                }else if(spellsSelected()){
                    UI.showView(activity, R.id.spells_calculator);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
