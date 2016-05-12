package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Ring;

public class RingsDialog {
    ItemDialog item_dialog;

    public RingsDialog(ItemDialog item_dialog){
        this.item_dialog = item_dialog;
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.ring_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void setAppearanceSelection(){
        item_dialog.appearanceInput().setAdapter(itemAppearanceAdapter());
    }

    private Spinner sinkEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.ringSinkEffectInput);
    }

    private String sinkEffect(){
        return sinkEffectInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> sinkEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.ring_sink_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int sinkEffectIndex(String effect){
        return sinkEffectAdapter().getPosition(effect);
    }

    private void setSinkEffect(String effect){
        sinkEffectInput().setSelection(sinkEffectIndex(effect));
    }

    private boolean sinkSpecified(){
        return sinkEffectInput().isSelected();
    }

    private Spinner wearEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.ringWearEffectInput);
    }

    private String wearEffect(){
        return wearEffectInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> wearEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.ring_wear_effects,
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

    private boolean wearSpecified(){
        return wearEffectInput().isSelected();
    }

    ///

    public void initializeSpinners() {
        sinkEffectInput().setAdapter(sinkEffectAdapter());
        wearEffectInput().setAdapter(wearEffectAdapter());
    }

    public Ring itemFromInput() {
        Ring ring = new Ring();
        ring.sink_effect = sinkEffect();
        ring.wear_effect = wearEffect();
        return ring;
    }

    public void inputFromItem(Ring ring){
        setSinkEffect(ring.sink_effect);
        setWearEffect(ring.wear_effect);
    }

    public void resetDialog(){
        sinkEffectInput().setSelected(false);
        wearEffectInput().setSelected(false);
    }

    public boolean filterSpecified(){
        return item_dialog.filterSpecified() || sinkSpecified() || wearSpecified();
    }

    ///


    public void setListeners(ItemDialog.InputChangedListener listener){
        sinkEffectInput().setOnItemSelectedListener(listener);
        wearEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    public String reidentify(){
        if(!filterSpecified()) return "";

        Items items = item_dialog.item_tracker().item_db.filter(new Item.ItemTypeFilter(Ring.type()));


        if(item_dialog.appearanceSpecified())
            items = items.filter(new Item.ItemAppearanceFilter(item_dialog.itemAppearance()));

        if(item_dialog.buyPriceSpecified())
            items = items.filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice()));

        if(item_dialog.sellPriceSpecified())
            items = items.filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice()));

        if(wearSpecified())
            items = items.filter(new Ring.WearFilter(wearEffect()));

        if(sinkSpecified())
            items = items.filter(new Ring.SinkFilter(sinkEffect()));

        return TextUtils.join(", ", items.names());
    }
}
