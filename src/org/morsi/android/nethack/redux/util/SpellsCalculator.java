package org.morsi.android.nethack.redux.util;

import java.util.Arrays;

import org.morsi.android.nethack.redux.CalculatorActivity;
import org.morsi.android.nethack.redux.R;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SpellsCalculator {
	  // reference to calculator activity
	  CalculatorActivity activity;	
	
	  // Store calculator values persistently
	  public static final String PREFS_NAME = "SpellsCalcValues";
	  SharedPreferences settings;
	  
	  // spells calculator values
	  private int character_level, character_int_wis, spell_level, skill_level;
	  private String character_class, spell_cast;
	  private boolean small_shield, other_shield, robe, metal_armor, other_armor, metal_helmet, metal_gloves, metal_boots;
	  
	  // static values
	  private static final String[] int_classes = {"Archeologist", "Barbarian", "Caveman", "Ranger", "Rogue", "Samurai", "Tourist", "Wizard"};
	  private static final String[] wis_classes = {"Healer", "Knight", "Monk", "Priest", "Valkyrie" };
	  private static final String[] special_spells    = {"Magic mapping", "Haste self", "Dig", "Invsisibility", "Detect treasure", "Clairvoyance", "Charm moster", "Magic missile", "Cure sickness", "Turn undead", "Restore ability", "Remove curse",    "Cone of cold"};
	  private static final String[] emergancy_spells  = {"Remove curse", "Healing", "Cure Blindness", "Cure sickness", "Extra Healing", "Restore ability"};
	  
	  private static final Integer[] base_values   = {5, 14, 12, 9, 8, 10, 5, 1, 3, 8, 8, 3, 10};
	  private static final Integer[] emerg_values  = {0, 0, 0, 2, 0, 0, 1, 0, -3, -2, -2, -2, -2};
	  private static final Integer[] shield_values = {2, 0, 1, 1, 1, 0, 2, 3, 2, 0, 2, 2, 0};
	  private static final Integer[] suit_values   = {10, 8, 8, 10, 9, 8, 10, 10, 10, 9, 20, 10, 9};
	  
	  public SpellsCalculator(CalculatorActivity ractivity){
		  activity = ractivity;
	  }
	  
	  public void onCreate(){
		  // populate character classes
		  Spinner spinner = (Spinner)activity.findViewById(R.id.characterClassInput);
		  String[] character_classes= new String[int_classes.length+wis_classes.length+1];
		  character_classes[0] = "Please select";
		  System.arraycopy(int_classes, 0, character_classes, 1, int_classes.length);
		  System.arraycopy(wis_classes, 0, character_classes, int_classes.length+1, wis_classes.length);
		  ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, character_classes);
		  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  spinner.setAdapter(adapter);
		  
		  // populate skills that are important
		  spinner = (Spinner)activity.findViewById(R.id.spellCastInput);
		  String[] spells= new String[special_spells.length+emergancy_spells.length+1];
		  spells[0] = "Other";
		  System.arraycopy(special_spells, 0, spells, 1, special_spells.length);
		  System.arraycopy(emergancy_spells, 0, spells, special_spells.length+1, emergancy_spells.length);
		  // FIXME filter duplicate elements
		  adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, spells);
		  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  spinner.setAdapter(adapter);
		  
	      // retrieve any stored preferences for damage calc
		  settings = activity.getSharedPreferences(PREFS_NAME, 0);
	      character_level   = settings.getInt("character_level", 1);
	      character_int_wis = settings.getInt("character_int_wis", 1);
	      spell_level = settings.getInt("spell_level", 1);
	      skill_level = settings.getInt("skill_level", 1);
	      character_class = settings.getString("character_class", "");
	      spell_cast = settings.getString("spell_cast", "");
	      small_shield = settings.getBoolean("small_shield", false);
	      other_shield = settings.getBoolean("other_shield", false);
	      robe = settings.getBoolean("robe", false);
	      metal_armor = settings.getBoolean("metal_armor", false);
	      other_armor = settings.getBoolean("other_armor", false);
	      metal_helmet = settings.getBoolean("metal_helmet", false);
	      metal_gloves = settings.getBoolean("metal_gloves", false);
	      metal_boots = settings.getBoolean("metal_boots", false);
	      
  	      // restore stored preferences and
	      // wire up listeners of armor calculator input events
	      SpellsCalculatorInputChangedListener aevent_listener = new SpellsCalculatorInputChangedListener();

	      EditText edit_box = (EditText)activity.findViewById(R.id.characterLevelInput);
	      edit_box.setText(Integer.toString(character_level));
	      edit_box.addTextChangedListener(aevent_listener);
	      
	      edit_box = (EditText)activity.findViewById(R.id.characterIntWisInput);
	      edit_box.setText(Integer.toString(character_int_wis));
	      edit_box.addTextChangedListener(aevent_listener);
	      
	      edit_box = (EditText)activity.findViewById(R.id.spellLevelInput);
	      edit_box.setText(Integer.toString(spell_level));
	      edit_box.addTextChangedListener(aevent_listener);
	      
	      edit_box = (EditText)activity.findViewById(R.id.skillLevelInput);
	      edit_box.setText(Integer.toString(skill_level));
	      edit_box.addTextChangedListener(aevent_listener);
	      
	      spinner = (Spinner)activity.findViewById(R.id.characterClassInput);
	      for(int i = 0; i < spinner.getChildCount(); ++i){
	    	  if(spinner.getChildAt(i).toString().equals(character_class)){
	    		  spinner.setSelection(i);
	    		  break;
	    	  }
	      }
	      spinner.setOnItemSelectedListener(aevent_listener);
	      
	      spinner = (Spinner)activity.findViewById(R.id.spellCastInput);
	      for(int i = 0; i < spinner.getChildCount(); ++i){
	    	  if(spinner.getChildAt(i).toString().equals(spell_cast)){
	    		  spinner.setSelection(i);
	    		  break;
	    	  }
	      }
	      spinner.setOnItemSelectedListener(aevent_listener);
	      
	      ToggleButton tbutton = (ToggleButton)activity.findViewById(R.id.smallShieldInput);
	      tbutton.setChecked(small_shield);
	      tbutton.setOnClickListener(aevent_listener);
	      
	      tbutton = (ToggleButton)activity.findViewById(R.id.otherShieldInput);
	      tbutton.setChecked(other_shield);
	      tbutton.setOnClickListener(aevent_listener);
	      
	      tbutton = (ToggleButton)activity.findViewById(R.id.robeInput);
	      tbutton.setChecked(robe);
	      tbutton.setOnClickListener(aevent_listener);
	      
	      tbutton = (ToggleButton)activity.findViewById(R.id.metalArmorInput);
	      tbutton.setChecked(metal_armor);
	      tbutton.setOnClickListener(aevent_listener);
	      
	      tbutton = (ToggleButton)activity.findViewById(R.id.otherArmorInput);
	      tbutton.setChecked(other_armor);
	      tbutton.setOnClickListener(aevent_listener);
	      
	      tbutton = (ToggleButton)activity.findViewById(R.id.metalHelmetInput);
	      tbutton.setChecked(metal_helmet);
	      tbutton.setOnClickListener(aevent_listener);
	      
	      tbutton = (ToggleButton)activity.findViewById(R.id.metalGlovesInput);
	      tbutton.setChecked(metal_gloves);
	      tbutton.setOnClickListener(aevent_listener);
	      
	      tbutton = (ToggleButton)activity.findViewById(R.id.metalBootsInput);
	      tbutton.setChecked(metal_boots);
	      tbutton.setOnClickListener(aevent_listener);
	  }
	  
	  // Handles spells calculator input changes
	  class SpellsCalculatorInputChangedListener implements TextWatcher, OnClickListener, OnItemSelectedListener {
	      public SpellsCalculatorInputChangedListener(){}
	      
	      public void afterTextChanged(Editable s) {
	        updateView();
	      }
	      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	      public void onTextChanged(CharSequence s, int start, int before, int count) {}
	      
	      public void onClick(View v) {
	    	  updateView();
	      }

		  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			  if(parent == activity.findViewById(R.id.characterClassInput)){
				  String selected_class = ((Spinner)parent).getSelectedItem().toString();
			      toggleIntWisLabel(selected_class);
			  }
			  updateView();
		  }
	
		  public void onNothingSelected(AdapterView<?> arg0) {}
	  }
	  
	  // Update calculated values based on damage calculator inputs
	  public void updateView(){
  		  // grab values from text boxes and store
  	      SharedPreferences.Editor editor = settings.edit();
  	      
  	      // TOOD edit_text validation should be a numeric regex
	      EditText edit_box = (EditText)activity.findViewById(R.id.characterLevelInput);
	      String edit_text  = edit_box.getText().toString();
	      if(!edit_text.equals("")) character_level = Integer.parseInt(edit_text);
	      else character_level = 1;
          editor.putInt("character_level", character_level);
          
          edit_box = (EditText)activity.findViewById(R.id.characterIntWisInput);
	      edit_text  = edit_box.getText().toString();
	      if(!edit_text.equals("")) character_int_wis = Integer.parseInt(edit_text);
	      else character_int_wis = 1;
          editor.putInt("character_int_wis", character_int_wis);
          
          edit_box = (EditText)activity.findViewById(R.id.spellLevelInput);
	      edit_text  = edit_box.getText().toString();
	      if(!edit_text.equals("")) spell_level = Integer.parseInt(edit_text);
	      else spell_level = 1;
          editor.putInt("spell_level", spell_level);
          
          edit_box = (EditText)activity.findViewById(R.id.skillLevelInput);
	      edit_text  = edit_box.getText().toString();
	      if(!edit_text.equals("")) skill_level = Integer.parseInt(edit_text);
	      else skill_level = 1;
          editor.putInt("skill_level", skill_level);
          
          Spinner spinner_box = (Spinner)activity.findViewById(R.id.characterClassInput);
	      String spinner_text  = spinner_box.getSelectedItem().toString();
	      if(!spinner_text.equals("")) character_class = spinner_text;
	      else character_class = "";
          editor.putString("character_class", character_class);
          
          spinner_box = (Spinner)activity.findViewById(R.id.spellCastInput);
	      spinner_text  = spinner_box.getSelectedItem().toString();
	      if(!spinner_text.equals("")) spell_cast = spinner_text;
	      else spell_cast = "";
          editor.putString("spell_cast", spell_cast);
          
          ToggleButton tbutton = (ToggleButton)activity.findViewById(R.id.smallShieldInput);
          small_shield = tbutton.isChecked();
          editor.putBoolean("small_shield", small_shield);
          
          tbutton = (ToggleButton)activity.findViewById(R.id.otherShieldInput);
          other_shield = tbutton.isChecked();
          editor.putBoolean("other_shield", other_shield);
          
          tbutton = (ToggleButton)activity.findViewById(R.id.robeInput);
          robe = tbutton.isChecked();
          editor.putBoolean("robe", robe);
          
          tbutton = (ToggleButton)activity.findViewById(R.id.metalArmorInput);
          small_shield = tbutton.isChecked();
          editor.putBoolean("metal_armor", metal_armor);
          
          tbutton = (ToggleButton)activity.findViewById(R.id.otherArmorInput);
          other_armor = tbutton.isChecked();
          editor.putBoolean("other_armor", other_armor);
          
          tbutton = (ToggleButton)activity.findViewById(R.id.metalHelmetInput);
          metal_helmet = tbutton.isChecked();
          editor.putBoolean("metal_helmet", metal_helmet);
          
          tbutton = (ToggleButton)activity.findViewById(R.id.metalGlovesInput);
          metal_gloves = tbutton.isChecked();
          editor.putBoolean("metal_gloves", metal_gloves);
          
          tbutton = (ToggleButton)activity.findViewById(R.id.metalBootsInput);
          metal_boots = tbutton.isChecked();
          editor.putBoolean("metal_boots", metal_boots);
  	      
  	      // TODO calculate spell success / failure rate and display
          TextView tv = (TextView)activity.findViewById(R.id.successRateValue);
          tv.setText(String.format("%.2f", spellSuccessRate()));
	  }
	  
	  public float spellSuccessRate(){
 		  // http://nethackwiki.com/wiki/Spellcasting#Calculating_spell_success_rate
		  int selected_class_index = -1;
		  for(int i = 0; i < int_classes.length; ++i){
			  if(character_class.equals(int_classes[i])){
				  selected_class_index = i;
				  break;
			  }
		  }
		  for(int i = 0; i < wis_classes.length; ++i){
			  if(character_class.equals(wis_classes[i])){
				  selected_class_index = int_classes.length + i;
				  break;
			  }
		  }
		  
		  // we didn't select a class
		  if(selected_class_index == -1) return (float) 0.0;

		  boolean is_special_spell = spell_cast.equals(special_spells[selected_class_index]);
		  boolean is_emergancy_spell = false;
		  
		  for(int i = 0; i < emergancy_spells.length; ++i){
			  if(spell_cast.equals(emergancy_spells[i])){
				  is_emergancy_spell = true;
				  break;
			  }
		  }
		  
		  int base   = base_values[selected_class_index];
		  int emerg  = emerg_values[selected_class_index];
		  int shield = shield_values[selected_class_index];
		  int suit   = suit_values[selected_class_index];
		  boolean wearing_shield =  small_shield || other_shield;
		  
		  int armor_robe_penalty = metal_armor ? (robe ? (suit / 2) : suit) : (robe ? (-1 * suit) : 0);
		  int penalty = base + (!is_emergancy_spell ? emerg : 0) + (wearing_shield ? shield : 0) + armor_robe_penalty +
		                       (metal_helmet ? 4 : 0) + (metal_gloves ? 6 : 0) + (metal_boots ? 2 : 0) +
		                       (is_special_spell ? -4 : 0);
		  float chance = (float) (5.5 * character_int_wis);
		  int difficulty = spell_level * 4 - skill_level * 6 - character_level / 2 - 5;
		  if(difficulty > 0){
			  chance = (float) (chance - Math.sqrt(900 * difficulty + 2000));
		  }else if(difficulty < 0){
			  difficulty = Math.abs(difficulty) * 15 / spell_level;
			  if(difficulty > 20) difficulty = 20;
			  chance = chance + difficulty;
		  }
		  if(chance < 0) chance = 0;
		  else if(chance > 120) chance = 120;
		  
		  if(wearing_shield && !small_shield){
			  if(is_special_spell)
				  chance /= 2;
			  else
				  chance /= 4;
		  }
		  
		  chance = chance * (20 - penalty)/15 - penalty;
		  if(chance < 0) chance = 0;
		  else if(chance > 100) chance = 100;
		  
		  return chance;
	  }
	  
	  // toggles the intelligence/wisdom label depending on the selected character classes
	  private void toggleIntWisLabel(String selected_class){
		  TextView tv = (TextView)activity.findViewById(R.id.characterClassIntWisLabel);
		  for(String int_class : int_classes){
			  if(int_class.equals(selected_class)){
				  tv.setText(R.string.classes_calc_int_label);
				  return;
			  }
		  }
		  for(String wis_class : wis_classes){
			  if(wis_class.equals(selected_class)){
				  tv.setText(R.string.classes_calc_wis_label);
				  return;
			  }
		  }
		  tv.setText(R.string.classes_calc_int_wis_label);
	  }
}
