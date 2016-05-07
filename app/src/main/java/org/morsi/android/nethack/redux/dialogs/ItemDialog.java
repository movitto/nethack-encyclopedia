package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.items.Amulet;
import org.morsi.android.nethack.redux.items.Gem;
import org.morsi.android.nethack.redux.items.Item;
import org.morsi.android.nethack.redux.items.Items;
import org.morsi.android.nethack.redux.items.Potion;
import org.morsi.android.nethack.redux.items.Ring;
import org.morsi.android.nethack.redux.items.Scroll;
import org.morsi.android.nethack.redux.items.SpellBook;
import org.morsi.android.nethack.redux.items.Wand;
import org.morsi.android.nethack.redux.trackers.ItemTracker;
import org.morsi.android.nethack.redux.util.Input;
import org.morsi.android.nethack.redux.util.UI;

public class ItemDialog {
    Activity activity;

    Dialog dialog;

    PotionsDialog    potions_dialog;
    ScrollsDialog    scrolls_dialog;
    WandsDialog      wands_dialog;
    SpellBooksDialog spellbooks_dialog;
    RingsDialog      rings_dialog;
    AmuletsDialog    amulets_dialog;
    GemsDialog       gems_dialog;

    GameTrackerActivity game_tracker() {
        return (GameTrackerActivity) activity;
    }

    ItemTracker item_tracker() {
        return game_tracker().item_tracker;
    }

    private Button closeButton() {
        return (Button) dialog.findViewById(R.id.item_close);
    }

    private Spinner itemTypeInput() {
        return (Spinner) dialog.findViewById(R.id.itemTypeInput);
    }

    private ArrayAdapter<CharSequence> itemTypeAdapter() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(activity,
                        R.array.item_types,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private String itemType() {
        return itemTypeInput().getSelectedItem().toString();
    }

    private int itemTypeIndex(String type){
        return itemTypeAdapter().getPosition(type);
    }

    private void setItemType(String type){
        itemTypeInput().setSelection(itemTypeIndex(type));
    }

    private boolean nothingSelected(){ return itemTypeInput().getSelectedItemPosition() == 0; }

    private boolean potionsSelected() {
        return itemType().equals("Potion");
    }

    private boolean scrollsSelected() {
        return itemType().equals("Scroll");
    }

    private boolean wandSelected() {
        return itemType().equals("Wand");
    }

    private boolean spellbookSelected() {
        return itemType().equals("SpellBook");
    }

    private boolean ringsSelected() {
        return itemType().equals("Ring");
    }

    private boolean amuletsSelected() {
        return itemType().equals("Amulet");
    }

    private boolean gemSelected() {
        return itemType().equals("Gem");
    }

    private EditText itemNameOutput() {
        return (EditText) dialog.findViewById(R.id.itemNameOutput);
    }

    private void setItemName(String name) {
        itemNameOutput().setText(name);
    }

    private Spinner appearanceInput() {
        return (Spinner) dialog.findViewById(R.id.itemAppearanceInput);
    }

    public String itemAppearance() {
        return appearanceInput().getSelectedItem().toString();
    }

    private int itemAppearanceIndex(String appearance){
        if (potionsSelected())
            return potions_dialog.itemAppearanceAdapter().getPosition(appearance);

        else if (scrollsSelected())
            return scrolls_dialog.itemAppearanceAdapter().getPosition(appearance);

        else if (wandSelected())
            return wands_dialog.itemAppearanceAdapter().getPosition(appearance);

        else if (spellbookSelected())
            return spellbooks_dialog.itemAppearanceAdapter().getPosition(appearance);

        else if (ringsSelected())
            return rings_dialog.itemAppearanceAdapter().getPosition(appearance);

        else if (amuletsSelected())
            return amulets_dialog.itemAppearanceAdapter().getPosition(appearance);

        else if (gemSelected())
            return gems_dialog.itemAppearanceAdapter().getPosition(appearance);

        return -1;
    }

    private void setItemAppearance(String appearance){
        appearanceInput().setSelection(itemAppearanceIndex(appearance));
    }

    private EditText buyPriceInput() {
        return (EditText) dialog.findViewById(R.id.itemBuyInput);
    }

    private String buyPriceString() {
        return buyPriceInput().getText().toString();
    }

    public int buyPrice(){ return Integer.parseInt(buyPriceString()); }

    private void setBuyPrice(int buyPrice){
        buyPriceInput().setText(Integer.toString(buyPrice));
    }

    private EditText sellPriceInput() {
        return (EditText) dialog.findViewById(R.id.itemSellInput);
    }

    private String sellPriceString() {
        return sellPriceInput().getText().toString();
    }

    public int sellPrice(){ return Integer.parseInt(sellPriceString()); }

    private void setSellPrice(int sellPrice){
        sellPriceInput().setText(Integer.toString(sellPrice));
    }

    private TextView itemTypeLabel() {
        return (TextView) dialog.findViewById(R.id.itemTypeLabel);
    }

    private void setItemTypeLabel(String label) {
        itemTypeLabel().setText(label);
    }

    // output: specified attrs, possible identifications

    private ItemDialog(Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.item_dialog);
        dialog.setTitle("Item Tracker");

        potions_dialog = new PotionsDialog(this);
        scrolls_dialog = new ScrollsDialog(this);
        wands_dialog = new WandsDialog(this);
        spellbooks_dialog = new SpellBooksDialog(this);
        rings_dialog = new RingsDialog(this);
        amulets_dialog = new AmuletsDialog(this);
        gems_dialog = new GemsDialog(this);

        dialog_listener = new ItemDialogListener(dialog);
        type_listener = new ItemTypeListener();

        // wire up close button
        closeButton().setOnClickListener(dialog_listener);
        itemTypeInput().setOnItemSelectedListener(type_listener);

        initializeSpinners();
        setListeners();

        if (item_tracker().editing_item != null)
            inputFromItem(item_tracker().editing_item);

    }

    public static Dialog create(Activity activity) {
        return new ItemDialog(activity).dialog;
    }

    ///

    private void initializeSpinners() {
        itemTypeInput().setAdapter(itemTypeAdapter());
        potions_dialog.initializeSpinners();
        scrolls_dialog.initializeSpinners();
        wands_dialog.initializeSpinners();
        spellbooks_dialog.initializeSpinners();
        rings_dialog.initializeSpinners();
        amulets_dialog.initializeSpinners();
        gems_dialog.initializeSpinners();
    }

    private Item itemFromInput() {
        if(nothingSelected()) return null;

        Item item = new Item();
        if (potionsSelected())
            item = potions_dialog.itemFromInput();
        else if (scrollsSelected())
            item = scrolls_dialog.itemFromInput();
        else if (wandSelected())
            item = wands_dialog.itemFromInput();
        else if (spellbookSelected())
            item = spellbooks_dialog.itemFromInput();
        else if (ringsSelected())
            item = rings_dialog.itemFromInput();
        else if (amuletsSelected())
            item = amulets_dialog.itemFromInput();
        else if (gemSelected())
            item = gems_dialog.itemFromInput();

        item.appearance = itemAppearance();
        item.name       = name;
        item.buy_price  = Input.validInt(buyPriceString())  ? buyPrice()  : 0;
        item.sell_price = Input.validInt(sellPriceString()) ? sellPrice() : 0;

        return item;
    }

    private void inputFromItem(Item item) {
        setItemAppearance(item.appearance);
        setItemName(item.name);
        setBuyPrice(item.buy_price);
        setSellPrice(item.sell_price);
        if(item instanceof Potion)
            potions_dialog.inputFromItem((Potion)item);
        else if(item instanceof Scroll)
            scrolls_dialog.inputFromItem((Scroll)item);
        else if(item instanceof Wand)
            wands_dialog.inputFromItem((Wand)item);
        else if(item instanceof SpellBook)
            spellbooks_dialog.inputFromItem((SpellBook)item);
        else if(item instanceof Ring)
            rings_dialog.inputFromItem((Ring)item);
        else if(item instanceof Amulet)
            amulets_dialog.inputFromItem((Amulet) item);
        else if(item instanceof Gem)
            gems_dialog.inputFromItem((Gem)item);
    }

    ItemDialogListener dialog_listener;

    class ItemDialogListener extends DialogListener {
        ItemDialogListener(Dialog dialog) {
            super(dialog);
        }

        public void onClick(View v) {
            Item item = itemFromInput();
            if(item != null)
                item_tracker().addItem(itemFromInput());
            super.onClick(v);
        }
    }

    private void hide_all(){
        UI.hideView(activity, R.id.potions_dialog);
        UI.hideView(activity, R.id.scrolls_dialog);
        UI.hideView(activity, R.id.wands_dialog);
        UI.hideView(activity, R.id.spellbooks_dialog);
        UI.hideView(activity, R.id.rings_dialog);
        UI.hideView(activity, R.id.amulets_dialog);
        UI.hideView(activity, R.id.gems_dialog);
    }

    ItemTypeListener type_listener;

    class ItemTypeListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            hide_all();

            if (potionsSelected()) {
                appearanceInput().setAdapter(potions_dialog.itemAppearanceAdapter());
                UI.showView(activity, R.id.potions_dialog);

            } else if (scrollsSelected()) {
                appearanceInput().setAdapter(scrolls_dialog.itemAppearanceAdapter());
                UI.showView(activity, R.id.scrolls_dialog);


            } else if (wandSelected()) {
                appearanceInput().setAdapter(wands_dialog.itemAppearanceAdapter());
                UI.showView(activity, R.id.wands_dialog);


            } else if (spellbookSelected()) {
                appearanceInput().setAdapter(spellbooks_dialog.itemAppearanceAdapter());
                UI.showView(activity, R.id.spellbooks_dialog);


            } else if (ringsSelected()) {
                appearanceInput().setAdapter(rings_dialog.itemAppearanceAdapter());
                UI.showView(activity, R.id.rings_dialog);


            } else if (amuletsSelected()) {
                appearanceInput().setAdapter(amulets_dialog.itemAppearanceAdapter());
                UI.showView(activity, R.id.amulets_dialog);

            } else if (gemSelected()) {
                appearanceInput().setAdapter(gems_dialog.itemAppearanceAdapter());
                UI.showView(activity, R.id.gems_dialog);
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    ///

    InputChangedListener event_listener;

    private void setListeners() {
        event_listener = new InputChangedListener();
        itemTypeInput().setOnItemSelectedListener(event_listener);
        appearanceInput().setOnItemSelectedListener(event_listener);
        buyPriceInput().addTextChangedListener(event_listener);
        sellPriceInput().addTextChangedListener(event_listener);
        potions_dialog.setListeners(event_listener);
        scrolls_dialog.setListeners(event_listener);
        wands_dialog.setListeners(event_listener);
        spellbooks_dialog.setListeners(event_listener);
        rings_dialog.setListeners(event_listener);
        amulets_dialog.setListeners(event_listener);
        gems_dialog.setListeners(event_listener);
    }

    public class InputChangedListener implements TextWatcher, View.OnClickListener, AdapterView.OnItemSelectedListener {
        public InputChangedListener() {
        }

        public void afterTextChanged(Editable s) {
            reidentify();
            updateView();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void onClick(View v) {
            reidentify();
            updateView();
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            reidentify();
            updateView();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    ///

    String name = Item.UNIDENTIFIED;

    private void reidentify(){
        if(potionsSelected())
            name = potions_dialog.reidentify();

        else if(scrollsSelected())
            name = scrolls_dialog.reidentify();

        else if(wandSelected())
            name = wands_dialog.reidentify();

        else if(spellbookSelected())
            name = spellbooks_dialog.reidentify();

        else if(ringsSelected())
            name = rings_dialog.reidentify();

        else if(amuletsSelected())
            name = amulets_dialog.reidentify();

        else if(gemSelected())
            name = gems_dialog.reidentify();
    }

    private void updateView(){
        itemNameOutput().setText(name);
    }
}
