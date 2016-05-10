package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Potion;

public class PotionsDialog {
    ItemDialog item_dialog;

    public PotionsDialog(ItemDialog item_dialog){
        this.item_dialog = item_dialog;
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.potion_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private Spinner quaffEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.potionQuaffEffectInput);
    }

    private String quaffEffect(){
        return quaffEffectInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> quaffEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.potion_quaff_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int quaffEffectIndex(String effect){
        return quaffEffectAdapter().getPosition(effect);
    }

    private void setQuaffEffect(String effect){
        quaffEffectInput().setSelection(quaffEffectIndex(effect));
    }

    private boolean quaffSpecified(){
        return quaffEffectInput().isSelected();
    }

    private Spinner throwEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.potionThrowEffectInput);
    }

    private String throwEffect(){
        return throwEffectInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> throwEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.potion_throw_effects,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int throwEffectIndex(String effect){
        return throwEffectAdapter().getPosition(effect);
    }

    private void setThrowEffect(String effect){
        throwEffectInput().setSelection(throwEffectIndex(effect));
    }

    private boolean throwSpecified(){
        return throwEffectInput().isSelected();
    }

    public void resetDialog(){
        quaffEffectInput().setSelected(false);
        throwEffectInput().setSelected(false);
    }

    public boolean filterSpecified(){
        return item_dialog.filterSpecified() || quaffSpecified() || throwSpecified();
    }

    ///

    public void initializeSpinners() {
        quaffEffectInput().setAdapter(quaffEffectAdapter());
        throwEffectInput().setAdapter(throwEffectAdapter());
    }

    public Item itemFromInput() {
        Potion potion = new Potion();
        potion.quaff_effect = quaffEffect();
        potion.throw_effect = throwEffect();
        return potion;
    }

    public void inputFromItem(Potion potion){
        setQuaffEffect(potion.quaff_effect);
        setThrowEffect(potion.throw_effect);
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        quaffEffectInput().setOnItemSelectedListener(listener);
        throwEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    public String reidentify(){
        if(!filterSpecified()) return "";

        Items items = item_dialog.item_tracker().item_db.filter(new Item.ItemTypeFilter(Potion.type()));

        if(item_dialog.appearanceSpecified())
            items = items.filter(new Item.ItemAppearanceFilter(item_dialog.itemAppearance()));

        if(item_dialog.buyPriceSpecified())
            items = items.filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice()));

        if(item_dialog.sellPriceSpecified())
            items = items.filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice()));

        if(quaffSpecified())
            items = items.filter(new Potion.QuaffFilter(quaffEffect()));

        if(throwSpecified())
            items = items.filter(new Potion.ThrowFilter(throwEffect()));

        return TextUtils.join(", ", items.names());
    }
}
