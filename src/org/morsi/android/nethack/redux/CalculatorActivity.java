/**********************************
 * Nethack Encyclopedia - Calculator Activity
 *
 * Copyright (C) 2011: Mo Morsi <mo@morsi.org>
 * Distributed under the MIT License
 **********************************/

package org.morsi.android.nethack.redux;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.AndroidMenu;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

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


  // Store calculator values persistently
  public static final String PREFS_NAME = "NethackCalcValues";
  SharedPreferences settings;

  // Armor calculator values
  private int ac, monster_level, monster_damage, monster_attacks;

  // Damage calculator values
  private int strength, min_weapon_damage, max_weapon_damage, weapon_enhancement, ring_increased_damage;
  private boolean fighting_undead_with_blessed, poisoned_weapon, life_draining_weapon;

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

        // retrieve any stored preferences for armor calc
        settings = getSharedPreferences(PREFS_NAME, 0);
        ac = settings.getInt("ac", 10);
        monster_level   = settings.getInt("monster_level",   1);
        monster_damage  = settings.getInt("monster_damage", 5);
        monster_attacks = settings.getInt("monster_attacks", 1);

        // retrieve any stored preferences for damage calc
        strength = settings.getInt("strength", 1);
        min_weapon_damage   = settings.getInt("min_weapon_damage",   1);
        max_weapon_damage   = settings.getInt("max_weapon_damage",   6);
        weapon_enhancement  = settings.getInt("weapon_enhancement", 0);
        ring_increased_damage        = settings.getInt("ring_increased_damage", 0);
        fighting_undead_with_blessed = settings.getBoolean("fighting_undead_with_blessed", false);
        poisoned_weapon              = settings.getBoolean("poisoned_weapon", false);
        life_draining_weapon         = settings.getBoolean("life_draining_weapon", false);


        // restore stored preferences and
        // wire up listeners of armor calculator input events
        ArmorCalculatorInputChangedListener aevent_listener = new ArmorCalculatorInputChangedListener(this);

        EditText edit_box = (EditText)findViewById(R.id.acInput);
        edit_box.setText(Integer.toString(ac));
        edit_box.addTextChangedListener(aevent_listener);

        edit_box = (EditText)findViewById(R.id.mlevelInput);
        edit_box.setText(Integer.toString(monster_level));
        edit_box.addTextChangedListener(aevent_listener);

        edit_box = (EditText)findViewById(R.id.mdamageInput);
        edit_box.setText(Integer.toString(monster_damage));
        edit_box.addTextChangedListener(aevent_listener);

        edit_box = (EditText)findViewById(R.id.mattacksInput);
        edit_box.setText(Integer.toString(monster_attacks));
        edit_box.addTextChangedListener(aevent_listener);

        // restore stored preferences and
        // wire up listeners of damage calculator input events
        DamageCalculatorInputChangedListener devent_listener = new DamageCalculatorInputChangedListener(this);

        edit_box = (EditText)findViewById(R.id.strengthInput);
        edit_box.setText(Integer.toString(strength));
        edit_box.addTextChangedListener(devent_listener);

        edit_box = (EditText)findViewById(R.id.minDmgInput);
        edit_box.setText(Integer.toString(min_weapon_damage));
        edit_box.addTextChangedListener(devent_listener);

        edit_box = (EditText)findViewById(R.id.maxDmgInput);
        edit_box.setText(Integer.toString(max_weapon_damage));
        edit_box.addTextChangedListener(devent_listener);

        edit_box = (EditText)findViewById(R.id.weaponEnhancementInput);
        edit_box.setText(Integer.toString(weapon_enhancement));
        edit_box.addTextChangedListener(devent_listener);

        edit_box = (EditText)findViewById(R.id.ringIncreasedDamageInput);
        edit_box.setText(Integer.toString(ring_increased_damage));
        edit_box.addTextChangedListener(devent_listener);

        CheckBox check_box = (CheckBox)findViewById(R.id.undeadBlessedWeaponInput);
        check_box.setChecked(fighting_undead_with_blessed);
        check_box.setOnClickListener(devent_listener);

        check_box = (CheckBox)findViewById(R.id.poisonWeaponInput);
        check_box.setChecked(poisoned_weapon);
        check_box.setOnClickListener(devent_listener);

        check_box = (CheckBox)findViewById(R.id.drainingLifeInput);
        check_box.setChecked(life_draining_weapon);
        check_box.setOnClickListener(devent_listener);

        updateArmorCalculator();
        updateDamageCalculator();
    }

    // Calculate and return min_armor_damage
    private int minArmorDamage(){
      if(ac >= 0) return monster_damage;
      int dmg = monster_damage - (ac * -1);
      if(dmg < 1) return 1;
      return dmg;
    }

    // Calculate and return max_armor_damage
    private int maxArmorDamage(){
      if(ac >= 0) return monster_damage;
      int dmg = monster_damage - 1;
      if(dmg < 1) return 1;
      return dmg;
    }

    // Update calculated values based on armor calculator inputs
    public void updateArmorCalculator(){
    // grab values from text boxes and store
    SharedPreferences.Editor editor = settings.edit();

    // TOOD edit_text validation should be a numeric regex
    EditText edit_box = (EditText)findViewById(R.id.acInput);
    String edit_text  = edit_box.getText().toString();
    if(!edit_text.equals("") && !edit_text.equals("-")) ac = Integer.parseInt(edit_box.getText().toString());
    else ac = 0;
        editor.putInt("ac", ac);

        edit_box  = (EditText)findViewById(R.id.mlevelInput);
        edit_text = edit_box.getText().toString();
        if(!edit_text.equals(""))  monster_level = Integer.parseInt(edit_box.getText().toString());
        else monster_level = 0;
        if (monster_level < 0) monster_level *= -1;
        editor.putInt("monster_level", monster_level);

        edit_box  = (EditText)findViewById(R.id.mdamageInput);
        edit_text = edit_box.getText().toString();
        if(!edit_text.equals("")) monster_damage = Integer.parseInt(edit_box.getText().toString());
        else monster_damage = 0;
        if (monster_damage < 0) monster_damage *= -1;
        editor.putInt("monster_damage", monster_damage);

        edit_box  = (EditText)findViewById(R.id.mattacksInput);
        edit_text = edit_box.getText().toString();
        if(!edit_text.equals("")) monster_attacks = Integer.parseInt(edit_box.getText().toString());
        else monster_attacks = 0;
        if (monster_attacks < 0) monster_attacks *= -1;
        editor.putInt("monster_attacks", monster_attacks);

        // calculate and display damage
    TextView tv = (TextView)findViewById(R.id.armor_damage_taken);
    tv.setText(this.getString(R.string.armor_damage) + " " +
           Integer.toString(minArmorDamage()) + "/" +
           Integer.toString(maxArmorDamage()));

    // calculate hit probability
        float worst_target = 10 + ac + monster_level;
        if (worst_target <= 0) worst_target = 1;
        float best_target = 0;
        if(ac >= 0) best_target = 10 + ac + monster_level;
        else        best_target = 10 - 1  + monster_level;
        float max_total = 0, min_total = 0;
        float[] max_probabilities = new float[monster_attacks];
        float[] min_probabilities = new float[monster_attacks];

        // display hit probability table headers
    TableLayout table = (TableLayout) findViewById(R.id.armor_prob_table);
    table.removeAllViews();
    TableRow tr = new TableRow(this.getBaseContext());
    TextView labelTV = new TextView(this.getBaseContext());
        labelTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        labelTV.setText(this.getString(R.string.total_hit_prob));
        labelTV.setPadding(5, 5, 5, 5);
        tr.addView(labelTV);
        for(int i = 0; i < monster_attacks; i++){
          labelTV = new TextView(this.getBaseContext());
          labelTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
          labelTV.setText(this.getString(R.string.attack_number) + Integer.toString(i+1));
          labelTV.setPadding(5, 5, 5, 5);
            tr.addView(labelTV);
            max_probabilities[i] = best_target/(20+i);
            min_probabilities[i] = worst_target/(20+i);
            max_total += max_probabilities[i] / monster_attacks;
            min_total += min_probabilities[i] / monster_attacks;
        }
        table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        // display hit probability table body
        tr = new TableRow(this.getBaseContext());
    labelTV = new TextView(this.getBaseContext());
        labelTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        labelTV.setText(String.format("%.2f", min_total) + "/" +
                String.format("%.2f", max_total));
        labelTV.setPadding(5, 5, 5, 5);
        tr.addView(labelTV);
        for(int i = 0; i < monster_attacks; i++){
          labelTV = new TextView(this.getBaseContext());
            labelTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
            labelTV.setText(String.format("%.2f", min_probabilities[i]) + "/" +
                    String.format("%.2f", max_probabilities[i]));
            labelTV.setPadding(5, 5, 5, 5);
            tr.addView(labelTV);
        }
        table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    }

    // Calculate and return strength bonus
    public int strengthBonus(){
           if(strength > 5   && strength < 16)  return 0;
      else if(strength > 15  && strength < 18)  return 1;
      else if(strength == 18)                   return 2;
      else if(strength > 18  && strength < 94)  return 3;
      else if(strength > 93  && strength < 110) return 4;
      else if(strength > 109 && strength < 118) return 5;
      else if(strength > 117)                   return 6;
      return -1;
    }

    // Calculate and return min Weapon Damage
    public int minWeaponDamage(){
      return min_weapon_damage +
           weapon_enhancement +
           ring_increased_damage +
             (fighting_undead_with_blessed ? 1 : 0) +
             (poisoned_weapon ? 1 : 0) +
             (life_draining_weapon ? 1 : 0) +
             strengthBonus();
    }

    // Calculate and return max weapon damage
    public int maxWeaponDamage(){
      return max_weapon_damage +
       weapon_enhancement +
       ring_increased_damage +
         (fighting_undead_with_blessed ? 4 : 0) +
         (poisoned_weapon ? 6 : 0) +
         (life_draining_weapon ? 8 : 0) +
         strengthBonus();
    }

    // Update calculated values based on damage calculator inputs
    public void updateDamageCalculator(){
    // grab values from text boxes and store
    SharedPreferences.Editor editor = settings.edit();

    // TOOD edit_text validation should be a numeric regex
    EditText edit_box = (EditText)findViewById(R.id.strengthInput);
    String edit_text  = edit_box.getText().toString();
    if(!edit_text.equals("")) strength = Integer.parseInt(edit_box.getText().toString());
    else strength = 1;
        editor.putInt("strength", strength);

        edit_box  = (EditText)findViewById(R.id.minDmgInput);
        edit_text = edit_box.getText().toString();
        if(!edit_text.equals(""))  min_weapon_damage = Integer.parseInt(edit_box.getText().toString());
        else min_weapon_damage = 1;
        editor.putInt("min_weapon_damage", min_weapon_damage);

        edit_box  = (EditText)findViewById(R.id.maxDmgInput);
        edit_text = edit_box.getText().toString();
        if(!edit_text.equals(""))  max_weapon_damage = Integer.parseInt(edit_box.getText().toString());
        else max_weapon_damage = 1;
        editor.putInt("max_weapon_damage", max_weapon_damage);

        edit_box  = (EditText)findViewById(R.id.weaponEnhancementInput);
        edit_text = edit_box.getText().toString();
        if(!edit_text.equals(""))  weapon_enhancement = Integer.parseInt(edit_box.getText().toString());
        else weapon_enhancement = 0;
        editor.putInt("weapon_enhancement", weapon_enhancement);

        edit_box  = (EditText)findViewById(R.id.ringIncreasedDamageInput);
        edit_text = edit_box.getText().toString();
        if(!edit_text.equals(""))  ring_increased_damage = Integer.parseInt(edit_box.getText().toString());
        else ring_increased_damage = 0;
        editor.putInt("ring_increased_damage", ring_increased_damage);

        CheckBox check_box = (CheckBox)findViewById(R.id.undeadBlessedWeaponInput);
        fighting_undead_with_blessed = check_box.isChecked();
        editor.putBoolean("fighting_undead_with_blessed", fighting_undead_with_blessed);

        check_box = (CheckBox)findViewById(R.id.poisonWeaponInput);
        poisoned_weapon = check_box.isChecked();
        editor.putBoolean("poisoned_weapon", poisoned_weapon);

        check_box = (CheckBox)findViewById(R.id.drainingLifeInput);
        life_draining_weapon = check_box.isChecked();
        editor.putBoolean("life_draining_weapon", life_draining_weapon);

      // calculate and display damage
    TextView tv = (TextView)findViewById(R.id.weapon_damage);
    tv.setText(this.getString(R.string.weapon_damage) + " " +
           Integer.toString(minWeaponDamage()) + "/" +
           Integer.toString(maxWeaponDamage()));
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

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    // Handles armor calculator input changes
    class ArmorCalculatorInputChangedListener implements TextWatcher {
      private CalculatorActivity activity;

      public ArmorCalculatorInputChangedListener(CalculatorActivity ractivity){
        activity = ractivity;
      }
    public void afterTextChanged(Editable s) {
      activity.updateArmorCalculator();
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    // Handles damage calculator input changes
    class DamageCalculatorInputChangedListener implements TextWatcher, OnClickListener {
      private CalculatorActivity activity;

      public DamageCalculatorInputChangedListener(CalculatorActivity ractivity){
        activity = ractivity;
      }
    public void afterTextChanged(Editable s) {
      activity.updateDamageCalculator();
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    public void onClick(View v) {
      activity.updateDamageCalculator();
    }
    }
}
