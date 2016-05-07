package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Amulet;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;

public class AmuletsDialog {
    ItemDialog item_dialog;

    public AmuletsDialog(ItemDialog item_dialog){
        this.item_dialog = item_dialog;
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.amulet_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private Spinner wearEffectInput(){
        return (Spinner) item_dialog.dialog.findViewById(R.id.amuletWearEffectInput);
    }

    private String wearEffect(){
        return wearEffectInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> wearEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.amulet_wear_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int wearEffectIndex(String effect){
        return wearEffectAdapter().getPosition(effect);
    }

    private void setWearEffect(String effect){
        wearEffectInput().setSelection(wearEffectIndex(effect));
    }

    ///

    public void initializeSpinners() {
        wearEffectInput().setAdapter(wearEffectAdapter());
    }

    public Item itemFromInput() {
        Amulet amulet = new Amulet();
        amulet.wear_effect = wearEffect();
        return amulet;
    }

    public void inputFromItem(Amulet amulet){
        setWearEffect(amulet.wear_effect);
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        wearEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    public String reidentify(){
        Items items = item_dialog.item_tracker().item_db.
                filter(new Item.ItemTypeFilter(Amulet.type())).
                filter(new Item.ItemAppearanceFilter(item_dialog.itemAppearance())).
                filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice())).
                filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice())).
                filter(new Amulet.WearFilter(wearEffect()));

        return TextUtils.join(", ", items.names());
    }
}
