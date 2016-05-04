package org.morsi.android.nethack.redux.calculators;

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
    CalculatorActivity activity;

    public ArmorCalculator(CalculatorActivity ractivity) {
        activity = ractivity;
    }

    public void onCreate() {
        restoreUIPrefs();
        setListeners();
    }

    ///

    // Store calculator values persistently
    public static final String PREFS_NAME = "ArmorCalcValues";

    SharedPreferences settings;

    // Armor calculator values
    private int ac, monster_level, monster_damage, monster_attacks;

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private int acPref(){
        return settings.getInt("ac", 10);
    }

    private int monsterLevelPref(){
        return settings.getInt("monster_level", 1);
    }

    private int monsterDamagePref(){ return settings.getInt("monster_damage", 5); }

    private int monsterAttacksPref(){ return settings.getInt("monster_attacks", 1); }

    private void restorePrefs() {
        // retrieve any stored preferences for armor calc
        settings        = sharedPrefs();
        ac              = acPref();
        monster_level   = monsterLevelPref();
        monster_damage  = monsterDamagePref();
        monster_attacks = monsterAttacksPref();
    }

    private void restoreUIPrefs(){
        restorePrefs();
        acInput().setText(Integer.toString(ac));
        monsterLevelInput().setText(Integer.toString(monster_level));
        monsterDamageInput().setText(Integer.toString(monster_damage));
        monsterAttacksInput().setText(Integer.toString(monster_attacks));
    }

    ///

    private boolean validInput(String test){
        // TODO edit_text validation should be a numeric regex
        return !test.equals("") && !test.equals("-");
    }

    private int sanitizeInput(int input){
        return input < 0 ? -input : input;
    }

    private EditText acInput(){
        return (EditText) activity.findViewById(R.id.acInput);
    }

    private String acInputValueString(){
        return acInput().getText().toString();
    }

    private int acInputValue(){
        return Integer.parseInt(acInputValueString());
    }

    private EditText monsterLevelInput(){
        return (EditText) activity.findViewById(R.id.mlevelInput);
    }

    private String monsterLevelInputString(){
        return monsterLevelInput().getText().toString();
    }

    private int monsterLevelInputValue(){
        return Integer.parseInt(monsterLevelInputString());
    }

    private EditText monsterDamageInput(){
        return (EditText) activity.findViewById(R.id.mdamageInput);
    }

    private String monsterDamageInputString(){
        return monsterDamageInput().getText().toString();
    }

    private int monsterDamageInputValue(){
        return Integer.parseInt(monsterDamageInputString());
    }

    private EditText monsterAttacksInput(){
        return (EditText) activity.findViewById(R.id.mattacksInput);
    }

    private String monsterAttacksInputString(){
        return monsterAttacksInput().getText().toString();
    }

    private int monsterAttacksInputValue(){
        return Integer.parseInt(monsterAttacksInputString());
    }

    ///

    private TextView armorDamageOutput() {
        return (TextView) activity.findViewById(R.id.armor_damage_taken);
    }

    private String armorDamageOutputString(){
        return Integer.toString(minArmorDamage()) + "/" + Integer.toString(maxArmorDamage());
    }

    private TextView hitProbabilityOutput(){
        return (TextView) activity.findViewById(R.id.total_armor_hit_probability);
    }

    private float worstTarget(){
        float worst_target = 10 + ac + monster_level;
        if (worst_target <= 0) worst_target = 1;
        return worst_target;
    }

    private float bestTarget(){
        float best_target = 0;
        if (ac >= 0) best_target = 10 + ac + monster_level;
        else best_target = 10 - 1 + monster_level;
        return best_target;

    }

    private float[] maxProbabilities(){
        float best_target = bestTarget();
        float[] max_probabilities = new float[monster_attacks];
        for (int i = 0; i < monster_attacks; i++)
            max_probabilities[i] = best_target / (20 + i);
        return max_probabilities;
    }

    private float[] minProbabilities(){
        float worst_target = worstTarget();
        float[] min_probabilities = new float[monster_attacks];
        for (int i = 0; i < monster_attacks; i++)
            min_probabilities[i] = worst_target / (20 + i);
        return min_probabilities;
    }

    private float maxTotal(){
        float max_total = 0;
        for(float p : maxProbabilities())
            max_total += p / monster_attacks;
        return max_total;
    }

    private float minTotal(){
        float min_total = 0;
        for(float p : minProbabilities())
            min_total += p / monster_attacks;
        return min_total;
    }

    private String hitProbabilityOutputString(){
        return String.format("%.2f", minTotal()) + "/" + String.format("%.2f", maxTotal());
    }

    private TableRow hitProbabilityTable(){
        return (TableRow) activity.findViewById(R.id.armor_prob_table_row);
    }

    private TextView newProbabilityTableRow(){
        TextView row = new TextView(activity.getBaseContext());
        row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        row.setGravity(Gravity.CENTER_HORIZONTAL);
        row.setTypeface(null, Typeface.BOLD);
        row.setPadding(5, 5, 5, 5);
        return row;
    }

    private String probabilityTableRowString(int row_num, float min, float max){
        return "#" + Integer.toString(row_num + 1) + ": " +
                String.format("%.2f", min) + "/" + String.format("%.2f", max);
    }

    ///

    ArmorCalculatorInputChangedListener event_listener;

    private void setListeners(){
        event_listener = new ArmorCalculatorInputChangedListener();
        acInput().addTextChangedListener(event_listener);
        monsterLevelInput().addTextChangedListener(event_listener);
        monsterDamageInput().addTextChangedListener(event_listener);
        monsterAttacksInput().addTextChangedListener(event_listener);
    }

    // Handles armor calculator input changes
    class ArmorCalculatorInputChangedListener implements TextWatcher {
        public ArmorCalculatorInputChangedListener() {}

        public void afterTextChanged(Editable s) {
            updateView();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

    ///

    // Calculate and return min_armor_damage
    private int minArmorDamage() {
        if (ac >= 0) return monster_damage;
        int dmg = monster_damage - (ac * -1);
        if (dmg < 1) return 1;
        return dmg;
    }

    // Calculate and return max_armor_damage
    private int maxArmorDamage() {
        if (ac >= 0) return monster_damage;
        int dmg = monster_damage - 1;
        if (dmg < 1) return 1;
        return dmg;
    }

    private void updateFields(){
        ac              =               validInput(acInputValueString())        ? acInputValue()              : 0;
        monster_level   = sanitizeInput(validInput(monsterLevelInputString())   ? monsterLevelInputValue()    : 0);
        monster_damage  = sanitizeInput(validInput(monsterDamageInputString())  ? monsterDamageInputValue()   : 0);
        monster_attacks = sanitizeInput(validInput(monsterAttacksInputString()) ? monsterAttacksInputValue()  : 0);
    }

    private void storeFields(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("ac", ac);
        editor.putInt("monster_level", monster_level);
        editor.putInt("monster_damage", monster_damage);
        editor.putInt("monster_attacks", monster_attacks);
        editor.commit();
    }

    private void updateOutput(){
        // calculate and display damage
        armorDamageOutput().setText(armorDamageOutputString());

        // display total hit probability
        hitProbabilityOutput().setText(hitProbabilityOutputString());

        // display hit probability table
        float[]  max = maxProbabilities();
        float[]  min = minProbabilities();
        TableRow hpt = hitProbabilityTable();
        hpt.removeAllViews();
        for (int i = 0; i < monster_attacks; i++) {
            TextView row = newProbabilityTableRow();
            row.setText(probabilityTableRowString(i, min[i], max[i]));
            hitProbabilityTable().addView(row);
        }
    }

    // Update calculated values based on armor calculator inputs
    public void updateView() {
        // grab values from text boxes and store
        updateFields();
        storeFields();

        updateOutput();
    }
}