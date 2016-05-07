package org.morsi.android.nethack.redux.trackers;

import android.content.SharedPreferences;
import android.text.TextUtils;
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
        reset();
    }

    public void onCreate() {
        restorePrefs();
    }

    public void newTrackerPopup(){
        editing_level = null;
        activity.showDialog(AndroidMenu.DIALOG_LEVEL_ID);
    }

    public void reset(){
        levels = new ArrayList<Level>();
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

    SharedPreferences settings;

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private String levelsPref(){ return settings.getString("levels", ""); }

    public void restorePrefs() {
        settings = sharedPrefs();
        for(String level : levelsPref().split(",")) {
            if(!level.equals(""))
                levels.add(Level.extract(level));
        }
    }

    private String levelsStr(){
        ArrayList<String> lvls = new ArrayList<String>();
        for(Level l : levels)
            lvls.add(l.compact());
        return TextUtils.join(",", levels);
    }

    public void storeFields(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("levels", levelsStr());
        editor.commit();
    }

    public void updateOutput(){
        levelsList().removeAllViews();
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

        level_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));
        attrs_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        remove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.25f));

        level_tv.setOnClickListener(new EditLevelListener(level));
        attrs_tv.setOnClickListener(new EditLevelListener(level));
        remove.setOnClickListener(new RemoveLevelListener(layout, level));

        layout.addView(level_tv);
        layout.addView(attrs_tv);
        layout.addView(remove);

        levelsList().addView(layout);
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

        RemoveLevelListener(LinearLayout level_view, Level level) {
            level_view_to_remove = level_view;
            level_to_remove = level;
        }

        public void onClick(View v) {
            levels.remove(level_to_remove);
            levelsList().removeView(level_view_to_remove);
        }
    }
}
