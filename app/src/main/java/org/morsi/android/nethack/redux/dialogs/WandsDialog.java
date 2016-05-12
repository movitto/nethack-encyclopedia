package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Wand;

import java.util.LinkedHashSet;
import java.util.Set;

public class WandsDialog {
    ItemDialog item_dialog;

    public WandsDialog(ItemDialog item_dialog){
        this.item_dialog = item_dialog;
    }

    private Items all_wands(){
        return item_dialog.item_tracker().item_db.filter(new Item.ItemTypeFilter(Wand.type()));
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.wand_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void setAppearanceSelection(){
        item_dialog.appearanceInput().setAdapter(itemAppearanceAdapter());
    }

    private Spinner engraveEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.wandEngraveEffectInput);
    }

    private String engraveEffect(){
        return engraveEffectInput().getSelectedItem().toString();
    }

    private String[] allEngraveEffects(){
        Set<String> effects = new LinkedHashSet<String>();
        effects.add("Select Effect...");

        for(Item item : all_wands())
            for(String effect : ((Wand)item).engrave_effects)
            effects.add(effect);

        return effects.toArray(new String[effects.size()]);
    }

    private ArrayAdapter<CharSequence> engraveEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(item_dialog.activity,
                        android.R.layout.simple_spinner_item,
                        allEngraveEffects());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int engraveEffectIndex(String effect){
        return engraveEffectAdapter().getPosition(effect);
    }

    private void setEngraveEffect(String effect){
        engraveEffectInput().setSelection(engraveEffectIndex(effect));
    }

    private boolean engraveSpecified(){
        return engraveEffectInput().getSelectedItemPosition() != 0;
    }

    private Spinner zapEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.wandZapEffectInput);
    }

    private String zapEffect(){
        return zapEffectInput().getSelectedItem().toString();
    }

    private String[] allZapEffects(){
        Set<String> effects = new LinkedHashSet<String>();
        effects.add("Select Effect...");

        for(Item item : all_wands())
            for(String effect : ((Wand)item).zap_effects)
                effects.add(effect);

        return effects.toArray(new String[effects.size()]);
    }

    private ArrayAdapter<CharSequence> zapEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(item_dialog.activity,
                        android.R.layout.simple_spinner_item,
                        allZapEffects());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int zapEffectIndex(String effect){
        return zapEffectAdapter().getPosition(effect);
    }

    private void setZapEffect(String effect){
        zapEffectInput().setSelection(zapEffectIndex(effect));
    }

    private boolean zapSpecified(){
        return zapEffectInput().getSelectedItemPosition() != 0;
    }

    public void resetDialog(){
        engraveEffectInput().setSelected(false);
        zapEffectInput().setSelected(false);
    }

    public boolean filterSpecified(){
        return item_dialog.filterSpecified() || engraveSpecified() || zapSpecified();
    }

    ///

    public void initializeSpinners() {
        engraveEffectInput().setAdapter(engraveEffectAdapter());
        zapEffectInput().setAdapter(zapEffectAdapter());
        engraveEffectInput().setSelection(0);
        zapEffectInput().setSelection(0);
    }

    public Item itemFromInput() {
        Wand wand = new Wand();
        wand.engrave_effect = engraveEffect();
        wand.zap_effect     = zapEffect();
        return wand;
    }

    public void inputFromItem(Wand wand){
        setAppearanceSelection();

        if(wand.hasEngraveEffect()) setEngraveEffect(wand.engrave_effect);
        if(wand.hasZapEffect()) setZapEffect(wand.zap_effect);
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        engraveEffectInput().setOnItemSelectedListener(listener);
        zapEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    public String reidentify(){
        if(!filterSpecified()) return "";

        Items items = all_wands();

        // all wand appearances are randomized
        //if(item_dialog.appearanceSpecified())
        //    items = items.filter(new Item.ItemAppearanceFilter(item_dialog.itemAppearance()));

        if(item_dialog.buyPriceSpecified())
            items = items.filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice()));

        if(item_dialog.sellPriceSpecified())
            items = items.filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice()));

        if(engraveSpecified())
            items = items.filter(new Wand.EngravingFilter(engraveEffect()));

        if(zapSpecified())
            items = items.filter(new Wand.ZapFilter(zapEffect()));

        return TextUtils.join(", ", items.names());
    }

}
