package org.morsi.android.nethack.redux.util;

import org.morsi.android.nethack.redux.CalculatorActivity;
import org.morsi.android.nethack.redux.R;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ArmorCalculator {
	  // reference to calculator activity
	  CalculatorActivity activity;
	
	  // Store calculator values persistently
	  public static final String PREFS_NAME = "ArmorCalcValues";
	  SharedPreferences settings;
	
	  // Armor calculator values
	  private int ac, monster_level, monster_damage, monster_attacks;
	  
	  public ArmorCalculator(CalculatorActivity ractivity){
		  activity = ractivity;
	  }
	  
	  public void onCreate(){
	        // retrieve any stored preferences for armor calc
	        settings = activity.getSharedPreferences(PREFS_NAME, 0);
	        ac = settings.getInt("ac", 10);
	        monster_level   = settings.getInt("monster_level",   1);
	        monster_damage  = settings.getInt("monster_damage",  5);
	        monster_attacks = settings.getInt("monster_attacks", 1);
	        
	        // restore stored preferences and
	        // wire up listeners of armor calculator input events
	        ArmorCalculatorInputChangedListener aevent_listener = new ArmorCalculatorInputChangedListener();

	        EditText edit_box = (EditText)activity.findViewById(R.id.acInput);
	        edit_box.setText(Integer.toString(ac));
	        edit_box.addTextChangedListener(aevent_listener);

	        edit_box = (EditText)activity.findViewById(R.id.mlevelInput);
	        edit_box.setText(Integer.toString(monster_level));
	        edit_box.addTextChangedListener(aevent_listener);

	        edit_box = (EditText)activity.findViewById(R.id.mdamageInput);
	        edit_box.setText(Integer.toString(monster_damage));
	        edit_box.addTextChangedListener(aevent_listener);

	        edit_box = (EditText)activity.findViewById(R.id.mattacksInput);
	        edit_box.setText(Integer.toString(monster_attacks));
	        edit_box.addTextChangedListener(aevent_listener);
	  }
	  
	  // Handles armor calculator input changes
	  class ArmorCalculatorInputChangedListener implements TextWatcher {
	      public ArmorCalculatorInputChangedListener(){}
	      
	      public void afterTextChanged(Editable s) {
	        updateView();
	      }
	      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	      public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
      public void updateView(){
          // grab values from text boxes and store
          SharedPreferences.Editor editor = settings.edit();

          // TOOD edit_text validation should be a numeric regex
          EditText edit_box = (EditText)activity.findViewById(R.id.acInput);
          String edit_text  = edit_box.getText().toString();
          if(!edit_text.equals("") && !edit_text.equals("-")) ac = Integer.parseInt(edit_box.getText().toString());
          else ac = 0;
          editor.putInt("ac", ac);

          edit_box  = (EditText)activity.findViewById(R.id.mlevelInput);
          edit_text = edit_box.getText().toString();
          if(!edit_text.equals(""))  monster_level = Integer.parseInt(edit_box.getText().toString());
          else monster_level = 0;
          if (monster_level < 0) monster_level *= -1;
          editor.putInt("monster_level", monster_level);

          edit_box  = (EditText)activity.findViewById(R.id.mdamageInput);
          edit_text = edit_box.getText().toString();
          if(!edit_text.equals("")) monster_damage = Integer.parseInt(edit_box.getText().toString());
          else monster_damage = 0;
          if (monster_damage < 0) monster_damage *= -1;
          editor.putInt("monster_damage", monster_damage);

          edit_box  = (EditText)activity.findViewById(R.id.mattacksInput);
          edit_text = edit_box.getText().toString();
          if(!edit_text.equals("")) monster_attacks = Integer.parseInt(edit_box.getText().toString());
          else monster_attacks = 0;
          if (monster_attacks < 0) monster_attacks *= -1;
          editor.putInt("monster_attacks", monster_attacks);

          // calculate and display damage
          TextView tv = (TextView)activity.findViewById(R.id.armor_damage_taken);
          tv.setText(Integer.toString(minArmorDamage()) + "/" + Integer.toString(maxArmorDamage()));

          // calculate hit probability
          float worst_target = 10 + ac + monster_level;
          if (worst_target <= 0) worst_target = 1;
          float best_target = 0;
          if(ac >= 0) best_target = 10 + ac + monster_level;
          else        best_target = 10 - 1  + monster_level;
          float max_total = 0, min_total = 0;
          float[] max_probabilities = new float[monster_attacks];
          float[] min_probabilities = new float[monster_attacks];
          for(int i = 0; i < monster_attacks; i++){
              max_probabilities[i] = best_target/(20+i);
              min_probabilities[i] = worst_target/(20+i);
              max_total += max_probabilities[i] / monster_attacks;
              min_total += min_probabilities[i] / monster_attacks;
          }
        
          // display total hit probability
          TextView labelTV = (TextView)activity.findViewById(R.id.total_armor_hit_probability);
          labelTV.setText(String.format("%.2f", min_total) + "/" + String.format("%.2f", max_total));

          // display hit probability table
          TableRow tr = (TableRow)activity.findViewById(R.id.armor_prob_table_row);
          tr.removeAllViews();        
          for(int i = 0; i < monster_attacks; i++){
              labelTV = new TextView(activity.getBaseContext());
              labelTV.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
              labelTV.setGravity(Gravity.CENTER_HORIZONTAL);
              labelTV.setTypeface(null, Typeface.BOLD);
              labelTV.setText("#" + Integer.toString(i+1) + ": " +
              		String.format("%.2f", min_probabilities[i]) + "/" + String.format("%.2f", max_probabilities[i]));
              labelTV.setPadding(5, 5, 5, 5);
              tr.addView(labelTV);
          }
      }
}