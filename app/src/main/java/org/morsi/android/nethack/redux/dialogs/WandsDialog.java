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
        return (Spinner) item_dialog.dialog.findViewById(R.id.wandEngraveEffectInput);
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
        return (Spinner) item_dialog.dialog.findViewById(R.id.wandZapEffectInput);
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

    class EngravingFilter implements Items.filter {
        String effect;

        EngravingFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Wand)item).engrave_effect.equals(effect);
        }
    };

    class ZapFilter implements Items.filter {
        String effect;

        ZapFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Wand)item).zap_effect.equals(effect);
        }
    };

    public String reidentify(){
        Items items = item_dialog.item_tracker().item_db.
                filter(new ItemDialog.ItemTypeFilter(Wand.type())).
                filter(new ItemDialog.ItemAppearanceFilter(item_dialog.itemAppearance())).
                filter(new ItemDialog.ItemBuyPriceFilter(item_dialog.buyPrice())).
                filter(new ItemDialog.ItemSellPriceFilter(item_dialog.sellPrice())).
                filter(new EngravingFilter(engraveEffect())).
                filter(new ZapFilter(zapEffect()));

        return TextUtils.join(", ", items.names());
    }

}
