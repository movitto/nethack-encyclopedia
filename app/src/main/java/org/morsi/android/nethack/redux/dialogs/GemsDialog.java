package org.morsi.android.nethack.redux.dialogs;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Gem;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;

public class GemsDialog {
    ItemDialog item_dialog;

    public GemsDialog(ItemDialog item_dialog) {
        this.item_dialog = item_dialog;
    }

    public ArrayAdapter<CharSequence> itemAppearanceAdapter() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.gem_appearances,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private Spinner engravingTypeInput(){
        return (Spinner) item_dialog.dialog.findViewById(R.id.gemEngravingTypeInput);
    }

    private String engravingType(){
        return engravingTypeInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> engravingTypeAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.gem_engraving_types,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int engravingTypeIndex(String effect){
        return engravingTypeAdapter().getPosition(effect);
    }

    private void setEngravingType(String effect){
        engravingTypeInput().setSelection(engravingTypeIndex(effect));
    }

    private Spinner streakColorInput(){
        return (Spinner) item_dialog.dialog.findViewById(R.id.gemStreakColorInput);
    }

    private String streakColor(){
        return streakColorInput().getSelectedItem().toString();
    }

    private ArrayAdapter<CharSequence> streakColorAdapter(){
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(item_dialog.activity,
                        R.array.gem_streak_colors,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int streakColorIndex(String effect){
        return streakColorAdapter().getPosition(effect);
    }

    private void setStreakColor(String effect){
        streakColorInput().setSelection(streakColorIndex(effect));
    }

    ///

    public void initializeSpinners() {
        engravingTypeInput().setAdapter(engravingTypeAdapter());
        streakColorInput().setAdapter(streakColorAdapter());
    }

    public Item itemFromInput() {
        Gem gem = new Gem();
        gem.engraving_type = engravingType();
        gem.streak_color = streakColor();
        return gem;
    }

    public void inputFromItem(Gem gem){
        setEngravingType(gem.engraving_type);
        setStreakColor(gem.streak_color);
    }

    ///

    public void setListeners(ItemDialog.InputChangedListener listener){
        engravingTypeInput().setOnItemSelectedListener(listener);
        streakColorInput().setOnItemSelectedListener(listener);
    }

    ///

    class EngravingFilter implements Items.filter {
        String effect;

        EngravingFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Gem)item).engraving_type.equals(effect);
        }
    };

    class StreakFilter implements Items.filter {
        String effect;

        StreakFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Gem)item).streak_color.equals(effect);
        }
    };

    public String reidentify(){
        Items items = item_dialog.item_tracker().item_db.
                filter(new ItemDialog.ItemTypeFilter(Gem.type())).
                filter(new ItemDialog.ItemAppearanceFilter(item_dialog.itemAppearance())).
                filter(new ItemDialog.ItemBuyPriceFilter(item_dialog.buyPrice())).
                filter(new ItemDialog.ItemSellPriceFilter(item_dialog.sellPrice())).
                filter(new EngravingFilter(engravingType())).
                filter(new StreakFilter(streakColor()));

        return TextUtils.join(", ", items.names());
    }
}
