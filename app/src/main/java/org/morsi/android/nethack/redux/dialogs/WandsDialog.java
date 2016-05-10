package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Wand;

public class WandsDialog {
    ItemDialog item_dialog;

    public WandsDialog(ItemDialog item_dialog){
        this.item_dialog = item_dialog;
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.wand_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private Spinner engraveEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.wandEngraveEffectInput);
    }

    private String engraveEffect(){
        return engraveEffectInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> engraveEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.wand_engrave_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int engraveEffectIndex(String effect){
        return engraveEffectAdapter().getPosition(effect);
    }

    private void setEngraveEffect(String effect){
        engraveEffectInput().setSelection(engraveEffectIndex(effect));
    }

    private Spinner zapEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.wandZapEffectInput);
    }

    private String zapEffect(){
        return zapEffectInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> zapEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.wand_zap_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int zapEffectIndex(String effect){
        return zapEffectAdapter().getPosition(effect);
    }

    private void setZapEffect(String effect){
        zapEffectInput().setSelection(zapEffectIndex(effect));
    }

    public void resetDialog(){
        engraveEffectInput().setSelected(false);
        zapEffectInput().setSelected(false);
    }

    ///

    public void initializeSpinners() {
        engraveEffectInput().setAdapter(engraveEffectAdapter());
        zapEffectInput().setAdapter(zapEffectAdapter());
    }

    public Item itemFromInput() {
        Wand wand = new Wand();
        wand.engrave_effect = engraveEffect();
        wand.zap_effect     = zapEffect();
        return wand;
    }

    public void inputFromItem(Wand wand){
        setEngraveEffect(wand.engrave_effect);
        setZapEffect(wand.zap_effect);
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        engraveEffectInput().setOnItemSelectedListener(listener);
        zapEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    public String reidentify(){
        Items items = item_dialog.item_tracker().item_db.
                filter(new Item.ItemTypeFilter(Wand.type())).
                filter(new Item.ItemAppearanceFilter(item_dialog.itemAppearance())).
                filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice())).
                filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice())).
                filter(new Wand.EngravingFilter(engraveEffect())).
                filter(new Wand.ZapFilter(zapEffect()));

        return TextUtils.join(", ", items.names());
    }

}
