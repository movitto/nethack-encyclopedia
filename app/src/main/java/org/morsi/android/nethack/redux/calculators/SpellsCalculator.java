package org.morsi.android.nethack.redux.calculators;

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

    public SpellsCalculator(CalculatorActivity ractivity) {
        activity = ractivity;
    }

    public void onCreate() {
        restoreUIPrefs();
        initializeSpinners();
        setListeners();
    }

    ///

    // Store calculator values persistently
    public static final String PREFS_NAME = "SpellsCalcValues";

    SharedPreferences settings;

    // spells calculator values
    private int character_level, character_int_wis, spell_level, skill_level;
    private String character_class, spell_cast;
    private boolean small_shield, other_shield, robe, metal_armor, other_armor, metal_helmet, metal_gloves, metal_boots;


    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private void restorePrefs() {
        // retrieve any stored preferences for armor calc
        settings = sharedPrefs();

        character_level   = settings.getInt("character_level",1);
        character_int_wis = settings.getInt("character_int_wis",1);
        spell_level       = settings.getInt("spell_level",1);
        skill_level       = settings.getInt("skill_level",1);
        character_class   = settings.getString("character_class","");
        spell_cast        = settings.getString("spell_cast","");
        small_shield      = settings.getBoolean("small_shield",false);
        other_shield      = settings.getBoolean("other_shield",false);
        robe              = settings.getBoolean("robe",false);
        metal_armor       = settings.getBoolean("metal_armor",false);
        other_armor       = settings.getBoolean("other_armor",false);
        metal_helmet      = settings.getBoolean("metal_helmet",false);
        metal_gloves      = settings.getBoolean("metal_gloves",false);
        metal_boots       = settings.getBoolean("metal_boots",false);
    }

    private void restoreUIPrefs() {
        restorePrefs();
        characterLevelInput().setText(Integer.toString(character_level));
        characterIntWisInput().setText(Integer.toString(character_int_wis));
        spellLevelInput().setText(Integer.toString(spell_level));
        skillLevelInput().setText(Integer.toString(skill_level));
        smallShieldInput().setChecked(small_shield);
        otherShieldInput().setChecked(other_shield);
        robeInput().setChecked(robe);
        metalArmorInput().setChecked(metal_armor);
        otherArmorInput().setChecked(other_armor);
        metalHelmetInput().setChecked(metal_helmet);
        metalGlovesInput().setChecked(metal_gloves);
        metalBootsInput().setChecked(metal_boots);
    }

    ///

    // static values
    private static final String[] int_classes      = {"Archeologist", "Barbarian", "Caveman", "Ranger", "Rogue", "Samurai", "Tourist", "Wizard"};
    private static final String[] wis_classes      = {"Healer", "Knight", "Monk", "Priest", "Valkyrie"};
    private static final String[] special_spells   = {"Magic mapping", "Haste self", "Dig", "Invsisibility", "Detect treasure", "Clairvoyance", "Charm moster",
                                                      "Magic missile", "Cure sickness", "Turn undead", "Restore ability", "Remove curse", "Cone of cold"};
    private static final String[] emergancy_spells = {"Remove curse", "Healing", "Cure Blindness", "Cure sickness", "Extra Healing", "Restore ability"};

    private static final Integer[] base_values     = {5, 14, 12,  9, 8, 10,  5,  1,  3,  8,  8,  3, 10};
    private static final Integer[] emerg_values    = {0,  0,  0,  2, 0,  0,  1,  0, -3, -2, -2, -2, -2};
    private static final Integer[] shield_values   = {2,  0,  1,  1, 1,  0,  2,  3,  2,  0,  2,  2,  0};
    private static final Integer[] suit_values     = {10, 8,  8, 10, 9,  8, 10, 10, 10,  9, 20, 10,  9};

    private String[] characterClasses(){
        String[] character_classes = new String[int_classes.length + wis_classes.length + 1];

        character_classes[0]="Please select";

        System.arraycopy(int_classes, 0, character_classes, 1, int_classes.length);
        System.arraycopy(wis_classes, 0, character_classes, int_classes.length+1, wis_classes.length);

        return character_classes;
    }

    private ArrayAdapter<String> characterClassesAdapter(){
        ArrayAdapter<String> aa = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, characterClasses());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return aa;
    }

    private String[] spellsCast(){
        String[] spells = new String[special_spells.length + emergancy_spells.length + 1];

        spells[0]="Other";

        System.arraycopy(special_spells,   0, spells, 1, special_spells.length);
        System.arraycopy(emergancy_spells, 0, spells, special_spells.length+1, emergancy_spells.length);

        return spells;
    }

    private ArrayAdapter<String> spellCastAdapter(){
        // FIXME filter duplicate elements
        ArrayAdapter<String> aa =new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, spellsCast());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return aa;
    }

    ///

    private boolean validInput(String test){
        // TODO edit_text validation should be a numeric regex
        return !test.equals("") && !test.equals("-");
    }

    private Spinner characterClassInput() {
        return (Spinner) activity.findViewById(R.id.characterClassInput);
    }

    private String characterClassInputValue() {
        return characterClassInput().getSelectedItem().toString();
    }

    private TextView characterClassInputLabel(){
        return (TextView) activity.findViewById(R.id.characterClassIntWisLabel);
    }

    private Spinner spellCastInput() {
        return (Spinner) activity.findViewById(R.id.spellCastInput);
    }

    private String spellCastInputValue(){
        return spellCastInput().getSelectedItem().toString();
    }

    private EditText characterLevelInput() {
        return (EditText) activity.findViewById(R.id.characterLevelInput);
    }

    private String characterLevelInputValueString() {
        return characterLevelInput().getText().toString();
    }

    private int characterLevelInputValue() {
        return Integer.parseInt(characterLevelInputValueString());
    }

    private EditText characterIntWisInput() {
        return (EditText)activity.findViewById(R.id.characterIntWisInput);
    }

    private String characterIntWisInputValueString() {
        return characterIntWisInput().getText().toString();
    }

    private int characterIntWisInputValue() {
        return Integer.parseInt(characterIntWisInputValueString());
    }

    private EditText spellLevelInput() {
        return (EditText)activity.findViewById(R.id.spellLevelInput);
    }

    private String spellLevelInputValueString() {
        return spellLevelInput().getText().toString();
    }

    private int spellLevelInputValue() {
        return Integer.parseInt(spellLevelInputValueString());
    }

    private EditText skillLevelInput() {
        return (EditText)activity.findViewById(R.id.skillLevelInput);
    }

    private String skillLevelInputValueString() {
        return skillLevelInput().getText().toString();
    }

    private int skillLevelInputValue() {
        return Integer.parseInt(skillLevelInputValueString());
    }

    private ToggleButton smallShieldInput() {
        return (ToggleButton) activity.findViewById(R.id.smallShieldInput);
    }

    private boolean smallShieldInputValue() {
        return smallShieldInput().isChecked();
    }

    private ToggleButton otherShieldInput() {
        return (ToggleButton)activity.findViewById(R.id.otherShieldInput);
    }

    private boolean otherShieldInputValue(){
        return otherShieldInput().isChecked();
    }

    private ToggleButton robeInput() {
        return (ToggleButton)activity.findViewById(R.id.robeInput);
    }

    private boolean robeInputValue(){
        return robeInput().isChecked();
    }

    private ToggleButton metalArmorInput() {
        return (ToggleButton)activity.findViewById(R.id.metalArmorInput);
    }

    private boolean metalArmorInputValue(){
        return metalArmorInput().isChecked();
    }

    private ToggleButton otherArmorInput() {
        return (ToggleButton)activity.findViewById(R.id.otherArmorInput);
    }

    private boolean otherArmorInputValue(){
        return otherArmorInput().isChecked();
    }

    private ToggleButton metalHelmetInput() {
        return (ToggleButton)activity.findViewById(R.id.metalHelmetInput);
    }

    private boolean metalHelmetInputValue(){
        return metalHelmetInput().isChecked();
    }

    private ToggleButton metalGlovesInput() {
        return (ToggleButton)activity.findViewById(R.id.metalGlovesInput);
    }

    private boolean metalGlovesInputValue(){
        return metalGlovesInput().isChecked();
    }

    private ToggleButton metalBootsInput() {
        return (ToggleButton)activity.findViewById(R.id.metalBootsInput);
    }

    private boolean metalBootsInputValue(){
        return metalBootsInput().isChecked();
    }

    private TextView successRateOutput(){
        return (TextView) activity.findViewById(R.id.successRateValue);
    }

    private void initializeSpinners() {
        // populate character classes
        Spinner cci = characterClassInput();
        cci.setAdapter(characterClassesAdapter());

        for(int i = 0; i < cci.getChildCount(); ++i){
            if (cci.getChildAt(i).toString().equals(character_class)) {
                cci.setSelection(i);
                break;
            }
        }

        // populate skills
        Spinner sci = spellCastInput();
        cci.setAdapter(spellCastAdapter());
        for(int i = 0; i<sci.getChildCount(); ++i){
            if (sci.getChildAt(i).toString().equals(spell_cast)) {
                sci.setSelection(i);
                break;
            }
        }
    }

    ///

    SpellsCalculatorInputChangedListener event_listener;

    private void setListeners() {
        event_listener = new SpellsCalculatorInputChangedListener();
        characterLevelInput().addTextChangedListener(event_listener);
        characterIntWisInput().addTextChangedListener(event_listener);
        spellLevelInput().addTextChangedListener(event_listener);
        skillLevelInput().addTextChangedListener(event_listener);
        smallShieldInput().setOnClickListener(event_listener);
        otherShieldInput().setOnClickListener(event_listener);
        robeInput().setOnClickListener(event_listener);
        metalArmorInput().setOnClickListener(event_listener);
        otherArmorInput().setOnClickListener(event_listener);
        metalHelmetInput().setOnClickListener(event_listener);
        metalGlovesInput().setOnClickListener(event_listener);
        metalBootsInput().setOnClickListener(event_listener);

        characterClassInput().setOnItemSelectedListener(event_listener);
        spellCastInput().setOnItemSelectedListener(event_listener);
    }

    // Handles spells calculator input changes
    class SpellsCalculatorInputChangedListener implements TextWatcher, OnClickListener, OnItemSelectedListener {
        public SpellsCalculatorInputChangedListener() {
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

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (parent == activity.findViewById(R.id.characterClassInput)) {
                String selected_class = ((Spinner) parent).getSelectedItem().toString();
                toggleIntWisLabel(selected_class);
            }
            updateView();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    ///

    private void updateFields(){
        character_level   = validInput(characterLevelInputValueString())  ? characterLevelInputValue()  : 1;
        character_int_wis = validInput(characterIntWisInputValueString()) ? characterIntWisInputValue() : 1;
        spell_level       = validInput(spellLevelInputValueString())      ? spellLevelInputValue()      : 1;
        skill_level       = validInput(skillLevelInputValueString())      ? skillLevelInputValue()      : 1;
        character_class   = validInput(characterClassInputValue())        ? characterClassInputValue()  : "";
        spell_cast        = validInput(spellCastInputValue())             ? spellCastInputValue()       : "";
        small_shield      = smallShieldInputValue();
        other_shield      = otherShieldInputValue();
        robe              = robeInputValue();
        metal_armor       = metalArmorInputValue();
        other_armor       = otherArmorInputValue();
        metal_gloves      = metalGlovesInputValue();
        metal_helmet      = metalHelmetInputValue();
        metal_boots       = metalBootsInputValue();
    }

    private void storeFields(){
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("character_level",    character_level);
        editor.putInt("character_int_wis",  character_int_wis);
        editor.putInt("spell_level",        spell_level);
        editor.putInt("skill_level",        skill_level);
        editor.putString("character_class", character_class);
        editor.putString("spell_cast",      spell_cast);
        editor.putBoolean("small_shield",   small_shield);
        editor.putBoolean("other_shield",   other_shield);
        editor.putBoolean("robe",           robe);
        editor.putBoolean("metal_armor",    metal_armor);
        editor.putBoolean("other_armor",    other_armor);
        editor.putBoolean("metal_helmet",   metal_helmet);
        editor.putBoolean("metal_gloves",   metal_gloves);
        editor.putBoolean("metal_boots",    metal_boots);
    }

    private void updateOutput() {
        successRateOutput().setText(String.format("%.2f", spellSuccessRate()));
    }

    // Update calculated values based on damage calculator inputs
    public void updateView() {
        updateFields();
        storeFields();

        updateOutput();
    }

    ///

    public float spellSuccessRate() {
        // http://nethackwiki.com/wiki/Spellcasting#Calculating_spell_success_rate
        int selected_class_index = -1;
        for (int i = 0; i < int_classes.length; ++i) {
            if (character_class.equals(int_classes[i])) {
                selected_class_index = i;
                break;
            }
        }
        for (int i = 0; i < wis_classes.length; ++i) {
            if (character_class.equals(wis_classes[i])) {
                selected_class_index = int_classes.length + i;
                break;
            }
        }

        // we didn't select a class
        if (selected_class_index == -1) return (float) 0.0;

        boolean is_special_spell = spell_cast.equals(special_spells[selected_class_index]);
        boolean is_emergancy_spell = false;

        for (int i = 0; i < emergancy_spells.length; ++i) {
            if (spell_cast.equals(emergancy_spells[i])) {
                is_emergancy_spell = true;
                break;
            }
        }

        int base = base_values[selected_class_index];
        int emerg = emerg_values[selected_class_index];
        int shield = shield_values[selected_class_index];
        int suit = suit_values[selected_class_index];
        boolean wearing_shield = small_shield || other_shield;

        int armor_robe_penalty = metal_armor ? (robe ? (suit / 2) : suit) : (robe ? (-1 * suit) : 0);
        int penalty = base + (!is_emergancy_spell ? emerg : 0) + (wearing_shield ? shield : 0) + armor_robe_penalty +
                (metal_helmet ? 4 : 0) + (metal_gloves ? 6 : 0) + (metal_boots ? 2 : 0) +
                (is_special_spell ? -4 : 0);
        float chance = (float) (5.5 * character_int_wis);
        int difficulty = spell_level * 4 - skill_level * 6 - character_level / 2 - 5;
        if (difficulty > 0) {
            chance = (float) (chance - Math.sqrt(900 * difficulty + 2000));
        } else if (difficulty < 0) {
            difficulty = Math.abs(difficulty) * 15 / spell_level;
            if (difficulty > 20) difficulty = 20;
            chance = chance + difficulty;
        }
        if (chance < 0) chance = 0;
        else if (chance > 120) chance = 120;

        if (wearing_shield && !small_shield) {
            if (is_special_spell)
                chance /= 2;
            else
                chance /= 4;
        }

        chance = chance * (20 - penalty) / 15 - penalty;
        if (chance < 0) chance = 0;
        else if (chance > 100) chance = 100;

        return chance;
    }

    // toggles the intelligence/wisdom label depending on the selected character classes
    private void toggleIntWisLabel(String selected_class) {
        TextView intwis = characterClassInputLabel();

        for (String int_class : int_classes) {
            if (int_class.equals(selected_class)) {
                intwis.setText(R.string.classes_calc_int_label);
                return;
            }
        }

        for (String wis_class : wis_classes) {
            if (wis_class.equals(selected_class)) {
                intwis.setText(R.string.classes_calc_wis_label);
                return;
            }
        }

        intwis.setText(R.string.classes_calc_int_wis_label);
    }
}
