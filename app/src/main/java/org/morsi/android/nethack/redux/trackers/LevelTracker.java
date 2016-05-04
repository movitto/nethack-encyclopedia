package org.morsi.android.nethack.redux.trackers;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.util.AndroidMenu;
import org.morsi.android.nethack.redux.util.Level;

import java.util.ArrayList;

public class LevelTracker {
    ArrayList<Level> levels;

    GameTrackerActivity activity;

    public LevelTracker(GameTrackerActivity activity){
        this.activity = activity;
    }

    public void onCreate() {
    }

    public void newTrackerPopup(){
        activity.showDialog(AndroidMenu.DIALOG_LEVEL_ID);
    }

    public void reset(){

    }
}
