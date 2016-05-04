package org.morsi.android.nethack.redux.trackers;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.util.AndroidMenu;

/**
 * Created by mmorsi on 4/30/16.
 */
public class ItemTracker {
    GameTrackerActivity activity;

    public ItemTracker(GameTrackerActivity activity){
        this.activity = activity;
    }

    public void onCreate() {
    }


    public void newTrackerPopup(){
        activity.showDialog(AndroidMenu.DIALOG_ITEM_ID);
    }

    public void reset(){

    }
}
