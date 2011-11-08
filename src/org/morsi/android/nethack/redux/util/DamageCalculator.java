package org.morsi.android.nethack.redux.util;

import org.morsi.android.nethack.redux.CalculatorActivity;
import org.morsi.android.nethack.redux.R;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.TableRow.LayoutParams;

public class DamageCalculator {
	  // reference to calculator activity
	  CalculatorActivity activity;	
	
	  // Store calculator values persistently
	  public static final String PREFS_NAME = "DamageCalcValues";
	  SharedPreferences settings;
	
	  // Damage calculator values
	  private int strength, min_weapon_damage, max_weapon_damage, weapon_enhancement, ring_increased_damage;
	  private boolean fighting_undead_with_blessed, poisoned_weapon, life_draining_weapon;
	  
	  public DamageCalculator(CalculatorActivity ractivity){
		  activity = ractivity;
	  }
	  
	  public void onCreate(){
	        // retrieve any stored preferences for damage calc
		    settings = activity.getSharedPreferences(PREFS_NAME, 0);
	        strength = settings.getInt("strength", 1);
	        min_weapon_damage   = settings.getInt("min_weapon_damage",   1);
	        max_weapon_damage   = settings.getInt("max_weapon_damage",   6);
	        weapon_enhancement  = settings.getInt("weapon_enhancement", 0);
	        ring_increased_damage        = settings.getInt("ring_increased_damage", 0);
	        fighting_undead_with_blessed = settings.getBoolean("fighting_undead_with_blessed", false);
	        poisoned_weapon              = settings.getBoolean("poisoned_weapon", false);
	        life_draining_weapon         = settings.getBoolean("life_draining_weapon", false);
	        
	        // setup damage feature toggle buttons
	        Button button = (Button) activity.findViewById(R.id.monster_size_button);
	        button.setText(activity.getResources().getStringArray(R.array.monster_size_array)[0]);
	        button = (Button) activity.findViewById(R.id.ring_increased_damage_button);
	        button.setText(activity.getResources().getStringArray(R.array.with_without_array)[0]);

	        // restore stored preferences and
	        // wire up listeners of damage calculator input events
	        DamageCalculatorInputChangedListener devent_listener = new DamageCalculatorInputChangedListener();

	        EditText edit_box = (EditText)activity.findViewById(R.id.strengthInput);
	        edit_box.setText(Integer.toString(strength));
	        edit_box.addTextChangedListener(devent_listener);

	        edit_box = (EditText)activity.findViewById(R.id.minDmgInput);
	        edit_box.setText(Integer.toString(min_weapon_damage));
	        edit_box.addTextChangedListener(devent_listener);

	        edit_box = (EditText)activity.findViewById(R.id.maxDmgInput);
	        edit_box.setText(Integer.toString(max_weapon_damage));
	        edit_box.addTextChangedListener(devent_listener);

	        edit_box = (EditText)activity.findViewById(R.id.weaponEnhancementInput);
	        edit_box.setText(Integer.toString(weapon_enhancement));
	        edit_box.addTextChangedListener(devent_listener);

	        edit_box = (EditText)activity.findViewById(R.id.ringIncreasedDamageInput);
	        edit_box.setText(Integer.toString(ring_increased_damage));
	        edit_box.addTextChangedListener(devent_listener);

	        ToggleButton tbutton = (ToggleButton)activity.findViewById(R.id.undeadBlessedWeaponInput);
	        tbutton.setChecked(fighting_undead_with_blessed);
	        tbutton.setOnClickListener(devent_listener);

	        tbutton = (ToggleButton)activity.findViewById(R.id.poisonWeaponInput);
	        tbutton.setChecked(poisoned_weapon);
	        tbutton.setOnClickListener(devent_listener);

	        tbutton = (ToggleButton)activity.findViewById(R.id.drainingLifeInput);
	        tbutton.setChecked(life_draining_weapon);
	        tbutton.setOnClickListener(devent_listener);
	  }
	  
	  // Handles damage calculator input changes
	  class DamageCalculatorInputChangedListener implements TextWatcher, OnClickListener {
	      public DamageCalculatorInputChangedListener(){}
	      
	      public void afterTextChanged(Editable s) {
	        updateView();
	      }
	      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	      public void onTextChanged(CharSequence s, int start, int before, int count) {}
	      
	      public void onClick(View v) {
	    	  updateView();
	      }
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
	  public void updateView(){
	      // grab values from text boxes and store
  	      SharedPreferences.Editor editor = settings.edit();

	      // TOOD edit_text validation should be a numeric regex
	      EditText edit_box = (EditText)activity.findViewById(R.id.strengthInput);
	      String edit_text  = edit_box.getText().toString();
	      if(!edit_text.equals("")) strength = Integer.parseInt(edit_box.getText().toString());
	      else strength = 1;
          editor.putInt("strength", strength);

          edit_box  = (EditText)activity.findViewById(R.id.minDmgInput);
          edit_text = edit_box.getText().toString();
          if(!edit_text.equals(""))  min_weapon_damage = Integer.parseInt(edit_box.getText().toString());
          else min_weapon_damage = 1;
          editor.putInt("min_weapon_damage", min_weapon_damage);

          edit_box  = (EditText)activity.findViewById(R.id.maxDmgInput);
          edit_text = edit_box.getText().toString();
          if(!edit_text.equals(""))  max_weapon_damage = Integer.parseInt(edit_box.getText().toString());
          else max_weapon_damage = 1;
          editor.putInt("max_weapon_damage", max_weapon_damage);

          edit_box  = (EditText)activity.findViewById(R.id.weaponEnhancementInput);
          edit_text = edit_box.getText().toString();
          if(!edit_text.equals(""))  weapon_enhancement = Integer.parseInt(edit_box.getText().toString());
          else weapon_enhancement = 0;
          editor.putInt("weapon_enhancement", weapon_enhancement);

          edit_box  = (EditText)activity.findViewById(R.id.ringIncreasedDamageInput);
          edit_text = edit_box.getText().toString();
          if(!edit_text.equals(""))  ring_increased_damage = Integer.parseInt(edit_box.getText().toString());
          else ring_increased_damage = 0;
          editor.putInt("ring_increased_damage", ring_increased_damage);

          ToggleButton tbutton = (ToggleButton)activity.findViewById(R.id.undeadBlessedWeaponInput);
          fighting_undead_with_blessed = tbutton.isChecked();
          editor.putBoolean("fighting_undead_with_blessed", fighting_undead_with_blessed);

          tbutton = (ToggleButton)activity.findViewById(R.id.poisonWeaponInput);
          poisoned_weapon = tbutton.isChecked();
          editor.putBoolean("poisoned_weapon", poisoned_weapon);

          tbutton = (ToggleButton)activity.findViewById(R.id.drainingLifeInput);
          life_draining_weapon = tbutton.isChecked();
          editor.putBoolean("life_draining_weapon", life_draining_weapon);

          // calculate and display damage
          TextView tv = (TextView)activity.findViewById(R.id.weapon_damage);
          tv.setText(Integer.toString(minWeaponDamage()) + "/" + Integer.toString(maxWeaponDamage()));
      }
	  
	  // manipulates the ui based on current state
      public void toggleDamageFeatures(View target) {
        	int res_id = target.getId(); int input_id = -1;
        	if(res_id == R.id.monster_size_button)
        		res_id = R.array.monster_size_array;
        	else if(res_id == R.id.ring_increased_damage_button){
        		res_id = R.array.with_without_array;
        		input_id = R.id.ringIncreasedDamageInput;
        	}
        	
        	Button button = (Button) target;
        	String current_text = button.getText().toString();
        	int current_index = -1;
        	String[] values = activity.getResources().getStringArray(res_id);
        	for(int i = 0; i < values.length; ++i){
        		if(values[i].equals(current_text)){
        			current_index = i;
        			break;
        		}
        	}
        	if(current_index == -1 || current_index == values.length - 1) current_index = 0;
        	else current_index = current_index+1;
        	
          ((Button)target).setText(values[current_index]);
            
          if(input_id != -1){
          	EditText edit = ((EditText)activity.findViewById(input_id));
            	if(current_index == values.length - 1){
            		edit.setVisibility(View.INVISIBLE);
            		edit.setLayoutParams(new LayoutParams(0, 0, 0));
            		edit.setText("0");
            	}else{
            		edit.setVisibility(View.VISIBLE);
            		edit.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.5f));
            	}
            	updateView();
          }
      }
}