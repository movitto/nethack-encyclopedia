package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.SpellBook;

public class SpellBooksDialog {
    ItemDialog item_dialog;

    public SpellBooksDialog(ItemDialog item_dialog){
        this.item_dialog = item_dialog;
    }

    public void initializeSpinners() {
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.spellbook_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
    }

    public Item itemFromInput() {
        return new SpellBook();
    }

    public void inputFromItem(SpellBook spellbook){

    }

    ///
    public String reidentify(){
        Items items = item_dialog.item_tracker().item_db.
                filter(new ItemDialog.ItemTypeFilter(SpellBook.type())).
                filter(new ItemDialog.ItemAppearanceFilter(item_dialog.itemAppearance())).
                filter(new ItemDialog.ItemBuyPriceFilter(item_dialog.buyPrice())).
                filter(new ItemDialog.ItemSellPriceFilter(item_dialog.sellPrice()));

        return TextUtils.join(", ", items.names());
    }
}
