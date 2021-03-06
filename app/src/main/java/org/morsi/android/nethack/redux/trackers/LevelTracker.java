package org.morsi.android.nethack.redux.trackers;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.dialogs.LevelDialog;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.Level;

import java.util.ArrayList;

public class LevelTracker {
    ArrayList<Level> levels;

    GameTrackerActivity activity;

    public LevelTracker(GameTrackerActivity activity){
        this.activity = activity;
        levels = new ArrayList<Level>();
    }

    public void onCreate() {
        restorePrefs();
        updateOutput();
    }

    public void newTrackerPopup(){
        editing_level = null;
        activity.showDialog(AndroidMenu.DIALOG_LEVEL_ID);
    }

    public void reset(){
        levels.clear();
        storeFields();
        removeAllViews();
    }

    private boolean hasLevel(String id){
        for(Level l : levels)
            if(l.id().equals(id))
                return true;
        return false;
    }

    private void removeLevel(String id){
        ArrayList<Level> to_remove = new ArrayList<Level>();
        for(Level l : levels)
            if(l.id().equals(id))
                to_remove.add(l);
        for(Level l : to_remove)
            levels.remove(l);
    }

    public void addLevel(Level level){
        if(hasLevel(level.id()))
            removeLevel(level.id());
        levels.add(level);
    }

    ///

    private LinearLayout levelsList(){
        return (LinearLayout)activity.findViewById(R.id.levels_list);
    }

    ///

    // store values persistently
    public static final String PREFS_NAME = "LevelTrackerValues";

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private String levelsPref(){ return sharedPrefs().getString("levels", ""); }

    public void restorePrefs() {
        for(String level : levelsPref().split(",")) {
            if(!level.equals(""))
                levels.add(Level.extract(level));
        }
    }

    private String levelsStr(){
        ArrayList<String> lvls = new ArrayList<String>();
        for(Level l : levels)
            lvls.add(l.compact());
        return TextUtils.join(",", lvls);
    }

    public void storeFields(){
        SharedPreferences.Editor editor = sharedPrefs().edit();
        editor.putString("levels", levelsStr());
        editor.commit();
    }

    private void removeAllViews(){
        levelsList().removeAllViews();
    }

    public void updateOutput(){
        removeAllViews();
        for(Level l : levels)
            displayLevel(l);
    }

    private void displayLevel(Level level){
        LinearLayout layout = new LinearLayout(activity);
        TextView level_tv   = new TextView(activity);
        TextView attrs_tv   = new TextView(activity);
        ImageView remove    = new ImageView(activity);

        level_tv.setText(level.id());
        attrs_tv.setText(level.toString());
        remove.setBackgroundResource(R.drawable.minus);

        level_tv.setTypeface(null, Typeface.BOLD);

        level_tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f));
        attrs_tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f));
        remove.setLayoutParams(new LinearLayout.LayoutParams(0, 30, 0.05f));

        level_tv.setPadding(10, 0, 0, 0);
        remove.setPadding(0, 0, 10, 0);

        EditLevelListener edit_listener = new EditLevelListener(level);
        RemoveLevelListener remove_listener = new RemoveLevelListener(layout, level);

        level_tv.setOnClickListener(edit_listener);
        attrs_tv.setOnClickListener(edit_listener);
        remove.setOnClickListener(remove_listener);

        layout.addView(level_tv);
        layout.addView(attrs_tv);
        layout.addView(remove);

        levelsList().addView(layout);

        ImageView seperator = new ImageView(activity);
        seperator.setBackgroundResource(R.drawable.divider);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 20);
        seperator.setLayoutParams(params);
        seperator.setPadding(0, 2, 0, 2);
        levelsList().addView(seperator);

        remove_listener.seperator_to_remove = seperator;
    }

    public Level editing_level;

    class EditLevelListener implements Button.OnClickListener {
        Level level_to_edit;

        EditLevelListener(Level level) {
            level_to_edit = level;
        }

        public void onClick(View v) {
            editing_level = level_to_edit;
            activity.showDialog(AndroidMenu.DIALOG_LEVEL_ID);
        }
    }

    class RemoveLevelListener implements Button.OnClickListener {
        LinearLayout level_view_to_remove;
        Level level_to_remove;

        ImageView seperator_to_remove;

        RemoveLevelListener(LinearLayout level_view, Level level) {
            level_view_to_remove = level_view;
            level_to_remove = level;
            seperator_to_remove = null;
        }

        public void onClick(View v) {
            levels.remove(level_to_remove);
            levelsList().removeView(level_view_to_remove);
            if(seperator_to_remove != null) levelsList().removeView(seperator_to_remove);
            storeFields();
        }
    }
}
