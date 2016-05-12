package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Potion;

import java.util.LinkedHashSet;
import java.util.Set;

public class PotionsDialog {
    ItemDialog item_dialog;

    public PotionsDialog(ItemDialog item_dialog){
        this.item_dialog = item_dialog;
    }

    private Items all_potions(){
        return item_dialog.item_tracker().item_db.filter(new Item.ItemTypeFilter(Potion.type()));
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.potion_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public void setAppearanceSelection(){
        item_dialog.appearanceInput().setAdapter(itemAppearanceAdapter());
    }

    private Spinner quaffEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.potionQuaffEffectInput);
    }

    private String quaffEffect(){
        return quaffSpecified() ? quaffEffectInput().getSelectedItem().toString() : "";
    }

    private String[] allQuaffEffects(){
        Set<String> effects = new LinkedHashSet<String>();
        effects.add("Select Effect...");

        for(Item item : all_potions())
            for(String effect : ((Potion)item).quaff_effects)
                effects.add(effect);

        return effects.toArray(new String[effects.size()]);
    }

    private ArrayAdapter<CharSequence> quaffEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(item_dialog.activity,
                        android.R.layout.simple_spinner_item,
                        allQuaffEffects());
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
        return quaffEffectInput().getSelectedItemPosition() != 0;
    }

    private Spinner throwEffectInput(){
        return (Spinner) item_dialog.findViewById(R.id.potionThrowEffectInput);
    }

    private String throwEffect(){
        return throwSpecified() ? throwEffectInput().getSelectedItem().toString() : "";
    }

    private String[] allThrowEffects(){
        Set<String> effects = new LinkedHashSet<String>();
        effects.add("Select Effect...");

        for(Item item : all_potions())
            for(String effect : ((Potion)item).throw_effects)
                effects.add(effect);

        return effects.toArray(new String[effects.size()]);
    }

    private ArrayAdapter<CharSequence> throwEffectAdapter(){
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<CharSequence>(item_dialog.activity,
                        android.R.layout.simple_spinner_item,
                        allThrowEffects());
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
        return throwEffectInput().getSelectedItemPosition() != 0;
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
        quaffEffectInput().setSelection(0);
        throwEffectInput().setSelection(0);
    }

    public Item itemFromInput() {
        Potion potion = new Potion();
        potion.quaff_effect = quaffEffect();
        potion.throw_effect = throwEffect();
        return potion;
    }

    public void inputFromItem(Potion potion){
        setAppearanceSelection();

        if(potion.hasQuaffEffect()) setQuaffEffect(potion.quaff_effect);
        if(potion.hasThrowEffect()) setThrowEffect(potion.throw_effect);
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        quaffEffectInput().setOnItemSelectedListener(listener);
        throwEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    public String reidentify(){
        if(!filterSpecified()) return "";

        Items items = all_potions();

        if(item_dialog.appearanceSpecified())
            items = items.filter(new Potion.AppearanceFilter(item_dialog.itemAppearance()));

        if(item_dialog.buyPriceSpecified())
            items = items.filter(new Item.ItemBuyPriceFilter(item_dialog.buyPrice()));

        if(item_dialog.sellPriceSpecified())
            items = items.filter(new Item.ItemSellPriceFilter(item_dialog.sellPrice()));

        if(quaffSpecified())
            items = items.filter(new Potion.QuaffFilter(quaffEffect()));

        if(throwSpecified())
            items = items.filter(new Potion.ThrowFilter(throwEffect()));

        // TODO if we've identified item (items.size == 1), set buy and sell price & other fields, remove from subsequent 'new' potion dialogs
        //      if not fully identified, limit other fields to their possible values.
        //      & in other item dialogs

        return TextUtils.join(", ", items.names());
    }
}
