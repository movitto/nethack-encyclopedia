package org.morsi.android.nethack.redux.calculators;

import org.morsi.android.nethack.redux.CalculatorActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.Input;

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
    CalculatorActivity activity;

    public DamageCalculator(CalculatorActivity ractivity) {
        activity = ractivity;
    }

    public void onCreate() {
        restoreUIPrefs();
        setListeners();
    }

    ///

    // Store calculator values persistently
    public static final String PREFS_NAME = "DamageCalcValues";

    SharedPreferences settings;

    // Damage calculator values
    private int strength, min_weapon_damage, max_weapon_damage, weapon_enhancement, ring_increased_damage;
    private boolean fighting_undead_with_blessed, poisoned_weapon, life_draining_weapon;

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private int strengthPref() {
        return settings.getInt("strength", 1);
    }

    private int minWeaponDamagePref() {
        return settings.getInt("min_weapon_damage", 1);
    }

    private int maxWeaponDamangePref() {
        return settings.getInt("max_weapon_damage", 6);
    }

    private int weaponEnhancementPref() {
        return settings.getInt("weapon_enhancement", 0);
    }

    private int ringIncreasedDamagePref() {
        return settings.getInt("ring_increased_damage", 0);
    }

    private boolean fightingUndeadWithBlessedPref() {
        return settings.getBoolean("fighting_undead_with_blessed", false);
    }

    private boolean poisonedWeaponPref() {
        return settings.getBoolean("poisoned_weapon", false);
    }

    private boolean lifeDrainingPref() {
        return settings.getBoolean("life_draining_weapon", false);
    }

    private void restorePrefs() {
        settings                     = sharedPrefs();
        strength                     = strengthPref();
        min_weapon_damage            = minWeaponDamagePref();
        max_weapon_damage            = maxWeaponDamangePref();
        weapon_enhancement           = weaponEnhancementPref();
        ring_increased_damage        = ringIncreasedDamagePref();
        fighting_undead_with_blessed = fightingUndeadWithBlessedPref();
        poisoned_weapon              = poisonedWeaponPref();
        life_draining_weapon         = lifeDrainingPref();
    }

    private String defaultMonsterSize() {
        return activity.getResources().getStringArray(R.array.monster_size_array)[0];
    }

    private String defaultWithWithoutRingIncreasedDamage() {
        return activity.getResources().getStringArray(R.array.with_without_array)[0];
    }

    private void restoreUIPrefs() {
        restorePrefs();
        monsterSizeButton().setText(defaultMonsterSize());
        ringIncreasedDamageButton().setText(defaultWithWithoutRingIncreasedDamage());
        strengthInput().setText(Integer.toString(strength));
        minDmgInput().setText(Integer.toString(min_weapon_damage));
        maxDmgInput().setText(Integer.toString(max_weapon_damage));
        weaponEnhancementInput().setText(Integer.toString(weapon_enhancement));
        ringIncreasedDamageInput().setText(Integer.toString(ring_increased_damage));
        undeadBlessedWeaponToggle().setChecked(fighting_undead_with_blessed);
        poisonWeaponToggle().setChecked(poisoned_weapon);
        lifeDrainingToggle().setChecked(life_draining_weapon);
    }

    ///

    private Button monsterSizeButton() {
        return (Button) activity.findViewById(R.id.monster_size_button);
    }

    private Button ringIncreasedDamageButton() {
        return (Button)activity.findViewById(R.id.ring_increased_damage_button);
    }

    private EditText strengthInput() {
        return (EditText) activity.findViewById(R.id.strengthInput);
    }

    private String strengthInputValueString(){
        return strengthInput().getText().toString();
    }

    private int strengthInputValue(){
        return Integer.parseInt(strengthInputValueString());
    }

    private EditText minDmgInput() {
        return (EditText) activity.findViewById(R.id.minDmgInput);
    }

    private String minDmgInputValueString(){
        return minDmgInput().getText().toString();
    }

    private int minDmgInputValue(){
        return Integer.parseInt(minDmgInputValueString());
    }

    private EditText maxDmgInput() {
        return (EditText) activity.findViewById(R.id.maxDmgInput);
    }

    private String maxDmgInputValueString(){
        return maxDmgInput().getText().toString();
    }

    private int maxDmgInputValue(){
        return Integer.parseInt(maxDmgInputValueString());
    }

    private EditText weaponEnhancementInput() {
        return (EditText) activity.findViewById(R.id.weaponEnhancementInput);
    }

    private String weaponEnhancementInputValueString(){
        return weaponEnhancementInput().getText().toString();
    }

    private int weaponEnhancementInputValue(){
        return Integer.parseInt(weaponEnhancementInputValueString());
    }

    private EditText ringIncreasedDamageInput() {
        return (EditText) activity.findViewById(R.id.ringIncreasedDamageInput);
    }

    private String ringIncreasedDamageInputValueString(){
        return ringIncreasedDamageInput().getText().toString();
    }

    private int ringIncreasedDamageInputValue(){
        return Integer.parseInt(ringIncreasedDamageInputValueString());
    }

    private ToggleButton undeadBlessedWeaponToggle() {
        return (ToggleButton) activity.findViewById(R.id.undeadBlessedWeaponInput);
    }

    private ToggleButton poisonWeaponToggle() {
        return (ToggleButton) activity.findViewById(R.id.poisonWeaponInput);
    }

    private ToggleButton lifeDrainingToggle() {
        return (ToggleButton) activity.findViewById(R.id.drainingLifeInput);
    }

    private TextView weaponDamageOutput(){
        return (TextView) activity.findViewById(R.id.weapon_damage);
    }

    private String weaponDamageOutputString(){
        return Integer.toString(minWeaponDamage()) + "/" + Integer.toString(maxWeaponDamage());
    }

    ///

    DamageCalculatorInputChangedListener event_listener;

    DamageCalculatorFeatureToggledListener feature_event_listener;

    private void setListeners() {
        event_listener = new DamageCalculatorInputChangedListener();
        feature_event_listener = new DamageCalculatorFeatureToggledListener();
        monsterSizeButton().setOnClickListener(feature_event_listener);
        ringIncreasedDamageButton().setOnClickListener(feature_event_listener);
        strengthInput().addTextChangedListener(event_listener);
        minDmgInput().addTextChangedListener(event_listener);
        maxDmgInput().addTextChangedListener(event_listener);
        weaponEnhancementInput().addTextChangedListener(event_listener);
        ringIncreasedDamageInput().addTextChangedListener(event_listener);
        undeadBlessedWeaponToggle().setOnClickListener(event_listener);
        poisonWeaponToggle().setOnClickListener(event_listener);
        lifeDrainingToggle().setOnClickListener(event_listener);
    }

    // Handles damage calculator input changes
    class DamageCalculatorInputChangedListener implements TextWatcher, OnClickListener {
        public DamageCalculatorInputChangedListener() {
        }

        public void afterTextChanged(Editable s) {
            updateView();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void onClick(View v) {
            updateView();
        }
    }

    // Handles damage feature toggles input changes
    class DamageCalculatorFeatureToggledListener implements OnClickListener {
        public void onClick(View v) {
            toggleDamageFeatures(v);
        }
    }

    ///

    // Calculate and return strength bonus
    public int strengthBonus() {
        if (strength > 5 && strength < 16) return 0;
        else if (strength > 15 && strength < 18) return 1;
        else if (strength == 18) return 2;
        else if (strength > 18 && strength < 94) return 3;
        else if (strength > 93 && strength < 110) return 4;
        else if (strength > 109 && strength < 118) return 5;
        else if (strength > 117) return 6;
        return -1;
    }

    // Calculate and return min Weapon Damage
    public int minWeaponDamage() {
        return min_weapon_damage +
                weapon_enhancement +
                ring_increased_damage +
                (fighting_undead_with_blessed ? 1 : 0) +
                (poisoned_weapon ? 1 : 0) +
                (life_draining_weapon ? 1 : 0) +
                strengthBonus();
    }

    // Calculate and return max weapon damage
    public int maxWeaponDamage() {
        return max_weapon_damage +
                weapon_enhancement +
                ring_increased_damage +
                (fighting_undead_with_blessed ? 4 : 0) +
                (poisoned_weapon ? 6 : 0) +
                (life_draining_weapon ? 8 : 0) +
                strengthBonus();
    }

    private void updateFields(){
        strength                     = Input.validInt(strengthInputValueString())            ? strengthInputValue()            : 1;
        min_weapon_damage            = Input.validInt(minDmgInputValueString())              ? minDmgInputValue()              : 1;
        max_weapon_damage            = Input.validInt(maxDmgInputValueString())              ? maxDmgInputValue()              : 1;
        weapon_enhancement           = Input.validInt(weaponEnhancementInputValueString())   ? weaponEnhancementInputValue()   : 0;
        ring_increased_damage        = Input.validInt(ringIncreasedDamageInputValueString()) ? ringIncreasedDamageInputValue() : 0;
        fighting_undead_with_blessed = undeadBlessedWeaponToggle().isChecked();
        poisoned_weapon              = poisonWeaponToggle().isChecked();
        life_draining_weapon         = lifeDrainingToggle().isChecked();
    }

    private void storeFields(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("strength", strength);
        editor.putInt("min_weapon_damage", min_weapon_damage);
        editor.putInt("max_weapon_damage", max_weapon_damage);
        editor.putInt("weapon_enhancement", weapon_enhancement);
        editor.putInt("ring_increased_damage", ring_increased_damage);
        editor.putBoolean("fighting_undead_with_blessed", fighting_undead_with_blessed);
        editor.putBoolean("poisoned_weapon", poisoned_weapon);
        editor.putBoolean("life_draining_weapon", life_draining_weapon);
        editor.commit();
    }

    private void updateOutput(){
        weaponDamageOutput().setText(weaponDamageOutputString());
    }

    // Update calculated values based on damage calculator inputs
    public void updateView() {
        updateFields();
        storeFields();

        updateOutput();
    }

    private void hideRingInput(){
        EditText input = ringIncreasedDamageInput();
        input.setVisibility(View.INVISIBLE);
        input.setLayoutParams(new LayoutParams(0, 0, 0));
        input.setText("0");
    }

    private void showRingInput(){
        EditText input = ringIncreasedDamageInput();
        input.setVisibility(View.VISIBLE);
        input.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.5f));
    }

    private String[] selectedFeature(View target){
        int id = target.getId();

        if (id == R.id.monster_size_button)
            id = R.array.monster_size_array;
        else if (id == R.id.ring_increased_damage_button)
            id = R.array.with_without_array;

        return activity.getResources().getStringArray(id);
    }

    private int nextFeatureValueIndex(Button feature_button) {
        String current = feature_button.getText().toString();

        int current_index = -1;
        String[] values = selectedFeature(feature_button);
        for (int i = 0; i < values.length; ++i) {
            if (values[i].equals(current)) {
                current_index = i;
                break;
            }
        }

        if (current_index == -1 || current_index == values.length - 1)
            current_index = 0;
        else
            current_index = current_index + 1;
        return current_index;
    }

    private String nextFeatureValue(Button feature_button){
        String[] values = selectedFeature(feature_button);
        return values[nextFeatureValueIndex(feature_button)];
    }


    // manipulates the ui based on current state
    public void toggleDamageFeatures(View target) {
        Button feature_button = (Button) target;
        feature_button.setText(nextFeatureValue(feature_button));

        if(ringIncreasedDamageButton().getText().equals("with"))
            showRingInput();
        else
            hideRingInput();
    }
}