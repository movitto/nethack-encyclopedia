package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Scroll;

public class ScrollsDialog {
    ItemDialog item_dialog;

    public ScrollsDialog(ItemDialog item_dialog) {
        this.item_dialog = item_dialog;
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.scroll_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private Spinner readEffectInput() {
        return (Spinner) item_dialog.findViewById(R.id.scrollReadEffectInput);
    }

    private String readEffect() {
        return readEffectInput().getSelectedItem().toString();
    }

    private int readEffectIndex(String effect){
        return readEffectAdapter().getPosition(effect);
    }

    private void setReadEffect(String effect){
        readEffectInput().setSelection(readEffectIndex(effect));
    }

    private ArrayAdapter<CharSequence> readEffectAdapter() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.scroll_read_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private boolean readSpecified(){
        return readEffectInput().isSelected();
    }

    private Spinner dropEffectInput() {
        return (Spinner) item_dialog.findViewById(R.id.scrollDropEffectInput);
    }

    private String dropEffect() {
        return dropEffectInput().getSelectedItem().toString();
    }

    private int dropEffectIndex(String effect){
        return dropEffectAdapter().getPosition(effect);
    }

    private void setDropEffect(String effect){
        dropEffectInput().setSelection(dropEffectIndex(effect));
    }

    private ArrayAdapter<CharSequence> dropEffectAdapter() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.scroll_drop_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private boolean dropSpecified(){
        return dropEffectInput().isSelected();
    }

    public void initializeSpinners() {
        readEffectInput().setAdapter(readEffectAdapter());
        dropEffectInput().setAdapter(dropEffectAdapter());
    }

    public Item itemFromInput() {
        Scroll scroll = new Scroll();
        scroll.read_effect = readEffect();
        scroll.drop_effect = dropEffect();
        return scroll;
    }

    public void inputFromItem(Scroll scroll){
        setReadEffect(scroll.read_effect);
        setDropEffect(scroll.drop_effect);
    }

    public void resetDialog(){
        readEffectInput().setSelected(false);
        dropEffectInput().setSelected(false);
    }

    public boolean filterSpecified(){
        return item_dialog.filterSpecified() || readSpecified() || dropSpecified();
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        readEffectInput().setOnItemSelectedListener(listener);
        dropEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    public String reidentify(){
        if(!filterSpecified()) return "";

        Items items = item_dialog.item_tracker().item_db.filter(new Item.ItemTypeFilter(Scroll.type()));

        if(item_dialog.appearanceSpecified())
            items = items.filter(new Item.ItemAppearanceFilter(item_dialog.itemAppearance()));

        if(item_dialog.buyPriceSpecified())
            items = items.filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice()));

        if(item_dialog.sellPriceSpecified())
            items = items.filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice()));

        if(readSpecified())
            items = items.filter(new Scroll.ReadFilter(readEffect()));

        if(dropSpecified())
            items = items.filter(new Scroll.DropFilter(dropEffect()));

        return TextUtils.join(", ", items.names());
    }
}
