package org.morsi.android.nethack.redux.trackers;

import android.content.SharedPreferences;
import android.widget.TextView;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.dialogs.ItemDialog;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.dialogs.PlayerDialog;

public class PlayerTracker {
    public int strength, dexterity, constitution, intelligence, wisdom, charisma;

    GameTrackerActivity activity;

    public PlayerTracker(GameTrackerActivity activity){
        this.activity = activity;
        reset();
    }

    public void onCreate() {
        restorePrefs();
        updateOutput();
    }

    public void newTrackerPopup(){
        activity.showDialog(AndroidMenu.DIALOG_PLAYER_ID);
    }

    private void resetFields(){
        strength     = 0;
        dexterity    = 0;
        constitution = 0;
        intelligence = 0;
        wisdom       = 0;
        charisma     = 0;
    }

    public void reset(){
        resetFields();
        storeFields();
        updateOutput();
    }

    ///

    private TextView strengthOutput(){
        return (TextView) activity.findViewById(R.id.strengthOutput);
    }

    private TextView dexterityOutput(){
        return (TextView) activity.findViewById(R.id.dexterityOutput);
    }

    private TextView constitutionOutput(){
        return (TextView) activity.findViewById(R.id.constitutionOutput);
    }

    private TextView intelligenceOutput(){
        return (TextView) activity.findViewById(R.id.intelligenceOutput);
    }

    private TextView wisdomOutput(){
        return (TextView) activity.findViewById(R.id.wisdomOutput);
    }

    private TextView charismaOutput(){
        return (TextView) activity.findViewById(R.id.charismaOutput);
    }

    ///

    // store values persistently
    public static final String PREFS_NAME = "PlayerTrackerValues";

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private int strengthPref(){ return sharedPrefs().getInt("strength", 0); }

    private int dexterityPref(){ return sharedPrefs().getInt("dexterity", 0); }

    private int constitutionPref(){ return sharedPrefs().getInt("constitution", 0); }

    private int intelligencePref(){ return sharedPrefs().getInt("intelligence", 0); }

    private int wisdomPref(){ return sharedPrefs().getInt("wisdom", 0); }

    private int charismaPref(){ return sharedPrefs().getInt("charisma", 0); }

    public void restorePrefs(){
        strength     = strengthPref();
        dexterity    = dexterityPref();
        constitution = constitutionPref();
        intelligence = intelligencePref();
        wisdom       = wisdomPref();
        charisma     = charismaPref();
    }

    public void storeFields(){
        SharedPreferences.Editor editor = sharedPrefs().edit();
        editor.putInt("strength",      strength);
        editor.putInt("dexterity",     dexterity);
        editor.putInt("constitution ", constitution);
        editor.putInt("intelligence", intelligence);
        editor.putInt("wisdom",       wisdom);
        editor.putInt("charisma",     charisma);
        editor.commit();
    }

    public void updateOutput(){
        strengthOutput().setText(Integer.toString(strength));
        dexterityOutput().setText(Integer.toString(dexterity));
        constitutionOutput().setText(Integer.toString(constitution));
        intelligenceOutput().setText(Integer.toString(intelligence));
        wisdomOutput().setText(Integer.toString(wisdom));
        charismaOutput().setText(Integer.toString(charisma));
    }
}
