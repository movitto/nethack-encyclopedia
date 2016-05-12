package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Scroll;

import java.util.LinkedHashSet;
import java.util.Set;

public class ScrollsDialog {
    ItemDialog item_dialog;

    public ScrollsDialog(ItemDialog item_dialog) {
        this.item_dialog = item_dialog;
    }

    private Items all_scrolls(){
        return item_dialog.item_tracker().item_db.filter(new Item.ItemTypeFilter(Scroll.type()));
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.scroll_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void setAppearanceSelection(){
        item_dialog.appearanceInput().setAdapter(itemAppearanceAdapter());
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

    private String[] allReadEffects(){
        Set<String> effects = new LinkedHashSet<String>();
        effects.add("Select Effect...");

        for(Item item : all_scrolls())
            for(String effect : ((Scroll)item).read_effects)
                effects.add(effect);

        return effects.toArray(new String[effects.size()]);
    }

    private ArrayAdapter<CharSequence> readEffectAdapter() {
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(item_dialog.activity,
                        android.R.layout.simple_spinner_item,
                        allReadEffects());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private boolean readSpecified(){
        return readEffectInput().getSelectedItemPosition() != 0;
    }

    public void initializeSpinners() {
        readEffectInput().setAdapter(readEffectAdapter());
    }

    public Item itemFromInput() {
        Scroll scroll = new Scroll();
        scroll.read_effect = readEffect();
        return scroll;
    }

    public void inputFromItem(Scroll scroll){
        setAppearanceSelection();

        if(scroll.hasReadEffect()) setReadEffect(scroll.read_effect);
    }

    public void resetDialog(){
        readEffectInput().setSelected(false);
    }

    public boolean filterSpecified(){
        return item_dialog.filterSpecified() || readSpecified();
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        readEffectInput().setOnItemSelectedListener(listener);
        readEffectInput().setSelection(0);
    }

    ///

    public String reidentify(){
        if(!filterSpecified()) return "";

        Items items = all_scrolls();

        if(item_dialog.appearanceSpecified())
            items = items.filter(new Scroll.AppearanceFilter(item_dialog.itemAppearance()));

        if(item_dialog.buyPriceSpecified())
            items = items.filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice()));

        if(item_dialog.sellPriceSpecified())
            items = items.filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice()));

        if(readSpecified())
            items = items.filter(new Scroll.ReadFilter(readEffect()));

        return TextUtils.join(", ", items.names());
    }
}
