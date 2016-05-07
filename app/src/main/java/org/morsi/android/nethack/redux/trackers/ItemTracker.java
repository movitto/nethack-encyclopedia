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
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.util.AndroidMenu;

import java.util.ArrayList;

public class ItemTracker {
    public Items item_db;

    Items items;

    GameTrackerActivity activity;

    public ItemTracker(GameTrackerActivity activity){
        this.activity = activity;
        reset();
    }

    public void onCreate() {
        restorePrefs();
    }


    public void newTrackerPopup(){
        editing_item = null;
        activity.showDialog(AndroidMenu.DIALOG_ITEM_ID);
    }

    public void reset(){
        items = new Items();
    }

    ///

    private boolean hasItem(String id){
        for(Item item : items)
            if(item.id().equals(id))
                return true;
        return false;
    }

    private void removeItem(String id){
        ArrayList<Item> to_remove = new ArrayList<Item>();
        for(Item i : items)
            if(i.id().equals(id))
                to_remove.add(i);
        for(Item i : to_remove)
            items.remove(i);
    }

    public void addItem(Item item){
        if(hasItem(item.id()))
            removeItem(item.id());
        items.add(item);
    }
    ///

    private LinearLayout itemsList(){
        return (LinearLayout)activity.findViewById(R.id.items_list);
    }

    ///

    // store values persistently
    public static final String PREFS_NAME = "ItemTrackerValues";

    SharedPreferences settings;

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private String itemsPref(){ return settings.getString("items", ""); }

    public void restorePrefs() {
        settings = sharedPrefs();
        for(String item : itemsPref().split(","))
            if(!item.equals(""))
                items.add(Item.extract(item));
    }

    private String levelsStr(){
        ArrayList<String> itms = new ArrayList<String>();
        for(Item i : items)
            itms.add(i.compact());
        return TextUtils.join(",", itms);
    }

    public void storeFields(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("items", levelsStr());
        editor.commit();
    }

    public void updateOutput(){
        itemsList().removeAllViews();
        for(Item i : items)
            displayItem(i);
    }

    private void displayItem(Item item){
        LinearLayout layout    = new LinearLayout(activity);
        TextView type_tv       = new TextView(activity);
        TextView name_tv       = new TextView(activity);
        TextView properties_tv = new TextView(activity);
        ImageView remove       = new ImageView(activity);

        type_tv.setText(item.type());
        properties_tv.setText(item.toString());
        if(item.identified()) name_tv.setText(item.name);
        remove.setBackgroundResource(R.drawable.minus);

        type_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f));
        name_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f));
        properties_tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.55f));
        remove.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f));

        type_tv.setOnClickListener(new EditItemListener(item));
        name_tv.setOnClickListener(new EditItemListener(item));
        properties_tv.setOnClickListener(new EditItemListener(item));
        remove.setOnClickListener(new RemoveItemListener(layout, item));

        layout.addView(type_tv);
        layout.addView(name_tv);
        layout.addView(properties_tv);
        layout.addView(remove);

        itemsList().addView(layout);
    }

    public Item editing_item;

    class EditItemListener implements Button.OnClickListener {
        Item item_to_edit;

        EditItemListener(Item item) {
            item_to_edit = item;
        }

        public void onClick(View v) {
            editing_item = item_to_edit;
            activity.showDialog(AndroidMenu.DIALOG_ITEM_ID);
        }
    }

    class RemoveItemListener implements Button.OnClickListener {
        LinearLayout item_view_to_remove;
        Item item_to_remove;

        RemoveItemListener(LinearLayout item_view, Item item) {
            item_view_to_remove = item_view;
            item_to_remove = item;
        }

        public void onClick(View v) {
            items.remove(item_to_remove);
            itemsList().removeView(item_view_to_remove);
        }
    }
}
