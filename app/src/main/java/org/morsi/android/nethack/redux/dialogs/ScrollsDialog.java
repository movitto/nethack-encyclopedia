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
        return (Spinner) item_dialog.dialog.findViewById(R.id.scrollReadEffectInput);
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

    private Spinner dropEffectInput() {
        return (Spinner) item_dialog.dialog.findViewById(R.id.scrollDropEffectInput);
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

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        readEffectInput().setOnItemSelectedListener(listener);
        dropEffectInput().setOnItemSelectedListener(listener);
    }

    ///

    class ReadFilter implements Items.filter {
        String effect;

        ReadFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Scroll)item).read_effect.equals(effect);
        }
    };

    class DropFilter implements Items.filter {
        String effect;

        DropFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Scroll)item).drop_effect.equals(effect);
        }
    };

    public String reidentify(){
        Items items = item_dialog.item_tracker().item_db.
                filter(new ItemDialog.ItemTypeFilter(Scroll.type())).
                filter(new ItemDialog.ItemAppearanceFilter(item_dialog.itemAppearance())).
                filter(new ItemDialog.ItemBuyPriceFilter(item_dialog.buyPrice())).
                filter(new ItemDialog.ItemSellPriceFilter(item_dialog.sellPrice())).
                filter(new ReadFilter(readEffect())).
                filter(new DropFilter(dropEffect()));

        return TextUtils.join(", ", items.names());
    }
}
