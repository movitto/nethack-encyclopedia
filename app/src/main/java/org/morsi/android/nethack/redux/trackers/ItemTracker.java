package org.morsi.android.nethack.redux.trackers;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
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
        items = new Items();
    }

    public void onCreate() {
        restorePrefs();
        updateOutput();
    }


    public void newTrackerPopup(){
        editing_item = null;
        activity.showDialog(AndroidMenu.DIALOG_ITEM_ID);
    }

    public void reset(){
        items.clear();
        storeFields();
        removeAllViews();
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

    private SharedPreferences sharedPrefs(){
        return activity.getSharedPreferences(PREFS_NAME, 0);
    }

    private String itemsPref(){ return sharedPrefs().getString("items", ""); }

    public void restorePrefs() {
        for(String item : itemsPref().split("="))
            if(!item.equals("")) {
                Item extracted = Item.extract(item);
                if(extracted != null) items.add(extracted);
            }
    }

    private String itemsStr(){
        ArrayList<String> itms = new ArrayList<String>();
        for(Item i : items)
            itms.add(i.compact());
        return TextUtils.join("=", itms);
    }

    public void storeFields(){
        SharedPreferences.Editor editor = sharedPrefs().edit();
        editor.putString("items", itemsStr());
        editor.commit();
    }

    private void removeAllViews(){
        itemsList().removeAllViews();
    }

    public void updateOutput(){
        removeAllViews();
        for(Item i : items)
            displayItem(i);
    }

    private void displayItem(Item item){
        LinearLayout layout    = new LinearLayout(activity);
        TextView type_tv       = new TextView(activity);
        TextView name_tv       = new TextView(activity);
        TextView properties_tv = new TextView(activity);
        ImageView remove       = new ImageView(activity);

        type_tv.setText(item.itemType());
        properties_tv.setText(item.toString());
        if(item.identified()) name_tv.setText(item.name);
        remove.setBackgroundResource(R.drawable.minus);

        type_tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f));
        name_tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.35f));
        properties_tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.45f));
        remove.setLayoutParams(new LinearLayout.LayoutParams(0, 30, 0.05f));

        type_tv.setPadding(10, 0, 0, 0);
        remove.setPadding(0, 0, 10, 0);

        EditItemListener edit_listener = new EditItemListener(item);
        RemoveItemListener remove_listener = new RemoveItemListener(layout, item);

        type_tv.setOnClickListener(edit_listener);
        name_tv.setOnClickListener(edit_listener);
        properties_tv.setOnClickListener(edit_listener);
        remove.setOnClickListener(remove_listener);

        layout.addView(type_tv);
        layout.addView(name_tv);
        layout.addView(properties_tv);
        layout.addView(remove);
        itemsList().addView(layout);

        ImageView seperator = new ImageView(activity);
        seperator.setBackgroundResource(R.drawable.divider);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 20);
        seperator.setLayoutParams(params);
        seperator.setPadding(0, 2, 0, 2);
        itemsList().addView(seperator);

        remove_listener.seperator_to_remove = seperator;
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

        ImageView seperator_to_remove;

        RemoveItemListener(LinearLayout item_view, Item item) {
            item_view_to_remove = item_view;
            item_to_remove = item;
            seperator_to_remove = null;
        }

        public void onClick(View v) {
            items.remove(item_to_remove);
            itemsList().removeView(item_view_to_remove);
            if(seperator_to_remove != null) itemsList().removeView(seperator_to_remove);
            storeFields();
        }
    }
}
