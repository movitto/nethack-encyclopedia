package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import org.morsi.android.nethack.redux.items.Potion;
import org.morsi.android.nethack.redux.items.Ring;
import org.morsi.android.nethack.redux.items.Scroll;
import org.morsi.android.nethack.redux.items.SpellBook;
import org.morsi.android.nethack.redux.items.Wand;
import org.morsi.android.nethack.redux.trackers.ItemTracker;
import org.morsi.android.nethack.redux.util.Input;
import org.morsi.android.nethack.redux.util.UI;

public class ItemDialog extends Dialog{
    Activity activity;

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
        return (Button) findViewById(R.id.item_close);
    }

    private Spinner itemTypeInput() {
        return (Spinner) findViewById(R.id.itemTypeInput);
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
        return itemTypeSpecified() ? itemTypeInput().getSelectedItem().toString() : "";
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

    private boolean itemTypeSpecified(){ return itemTypeInput().getSelectedItemPosition() != 0; }

    private EditText itemNameOutput() {
        return (EditText) findViewById(R.id.itemNameOutput);
    }

    private void setItemName(String name) {
        itemNameOutput().setText(name);
    }

    public Spinner appearanceInput() {
        return (Spinner) findViewById(R.id.itemAppearanceInput);
    }

    public String itemAppearance() {
        return appearanceSpecified() ? appearanceInput().getSelectedItem().toString() : "";
    }

    public int itemAppearanceIndex(String appearance){
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

    public void setItemAppearance(String appearance){
        appearanceInput().setSelection(itemAppearanceIndex(appearance));
    }

    public boolean appearanceSpecified(){
        return appearanceInput().getSelectedItemPosition() != 0;
    }

    private EditText buyPriceInput() {
        return (EditText) findViewById(R.id.itemBuyInput);
    }

    private String buyPriceString() {
        return buyPriceInput().getText().toString();
    }

    public int buyPrice(){ return Integer.parseInt(buyPriceString()); }

    private void setBuyPrice(int buyPrice){
        buyPriceInput().setText(Integer.toString(buyPrice));
    }

    public boolean buyPriceSpecified(){
        return !buyPriceString().equals("");
    }

    private EditText sellPriceInput() {
        return (EditText) findViewById(R.id.itemSellInput);
    }

    private String sellPriceString() {
        return sellPriceInput().getText().toString();
    }

    public int sellPrice(){ return Integer.parseInt(sellPriceString()); }

    private void setSellPrice(int sellPrice){
        sellPriceInput().setText(Integer.toString(sellPrice));
    }

    public boolean sellPriceSpecified(){
        return !sellPriceString().equals("");
    }

    private TextView itemTypeLabel() {
        return (TextView) findViewById(R.id.itemTypeLabel);
    }

    private void setItemTypeLabel(String label) {
        itemTypeLabel().setText(label);
    }

    public boolean filterSpecified(){
        return appearanceSpecified() || sellPriceSpecified() || buyPriceSpecified();
    }

    private void resetDialog(){
        itemTypeInput().setSelection(0);
        buyPriceInput().setText("");
        sellPriceInput().setText("");
        itemTypeLabel().setText("");
        itemNameOutput().setText("");

        potions_dialog.resetDialog();
        scrolls_dialog.resetDialog();
        wands_dialog.resetDialog();
        spellbooks_dialog.resetDialog();
        rings_dialog.resetDialog();
        amulets_dialog.resetDialog();
        gems_dialog.resetDialog();

        itemTypeInput().requestFocus();
    }

    private void hide_all(){
        UI.hideView(findViewById(R.id.potions_dialog));
        UI.hideView(findViewById(R.id.scrolls_dialog));
        UI.hideView(findViewById(R.id.wands_dialog));
        UI.hideView(findViewById(R.id.spellbooks_dialog));
        UI.hideView(findViewById(R.id.rings_dialog));
        UI.hideView(findViewById( R.id.amulets_dialog));
        UI.hideView(findViewById(R.id.gems_dialog));
    }

    ///

    public ItemDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.item_dialog);
        setTitle("Item Tracker");

        potions_dialog = new PotionsDialog(this);
        scrolls_dialog = new ScrollsDialog(this);
        wands_dialog = new WandsDialog(this);
        spellbooks_dialog = new SpellBooksDialog(this);
        rings_dialog = new RingsDialog(this);
        amulets_dialog = new AmuletsDialog(this);
        gems_dialog = new GemsDialog(this);

        close_listener = new ItemDialogCloseListener(this);
        type_listener = new ItemTypeListener();
        show_listener = new ItemDialogShowListener();

        // wire up close button
        closeButton().setOnClickListener(close_listener);

        itemTypeInput().setOnItemSelectedListener(type_listener);

        setOnShowListener(show_listener);

        initializeSpinners();
        setListeners();
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

        setItemType(item.itemType());
        if(item.hasAppearance())
            setItemAppearance(item.appearance);

        if(item.identified())
            setItemName(item.name);

        if(item.hasBuyPrice())
            setBuyPrice(item.buy_price);

        if(item.hasSellPrice())
            setSellPrice(item.sell_price);
    }

    ItemDialogCloseListener close_listener;

    class ItemDialogCloseListener extends DialogListener {
        ItemDialogCloseListener(Dialog dialog) {
            super(dialog);
        }

        public void onClick(View v) {
            Item item = itemFromInput();
            if(item != null){
                item_tracker().addItem(itemFromInput());
                item_tracker().storeFields();
                item_tracker().updateOutput();
            }
            resetDialog();
            super.onClick(v);
        }
    }

    ItemDialogShowListener show_listener;

    class ItemDialogShowListener implements DialogInterface.OnShowListener {
        public void onShow(DialogInterface i){
            if(item_tracker().editing_item != null) {
                inputFromItem(item_tracker().editing_item);

            }else {
                // reset item dialog
                name = Item.UNIDENTIFIED;
                setItemName(name);
                initializeSpinners();
                appearanceInput().setAdapter(null);
            }
        }
    }

    ItemTypeListener type_listener;

    class ItemTypeListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            hide_all();

            if (potionsSelected()) {
                potions_dialog.setAppearanceSelection();
                UI.showView(findViewById(R.id.potions_dialog));

            } else if (scrollsSelected()) {
                scrolls_dialog.setAppearanceSelection();
                UI.showView(findViewById(R.id.scrolls_dialog));


            } else if (wandSelected()) {
                wands_dialog.setAppearanceSelection();
                UI.showView(findViewById(R.id.wands_dialog));


            } else if (spellbookSelected()) {
                spellbooks_dialog.setAppearanceSelection();
                UI.showView(findViewById(R.id.spellbooks_dialog));


            } else if (ringsSelected()) {
                rings_dialog.setAppearanceSelection();
                UI.showView(findViewById(R.id.rings_dialog));


            } else if (amuletsSelected()) {
                amulets_dialog.setAppearanceSelection();
                UI.showView(findViewById(R.id.amulets_dialog));

            } else if (gemSelected()) {
                gems_dialog.setAppearanceSelection();
                UI.showView(findViewById(R.id.gems_dialog));
            } else
                return;

            // XXX will be triggered twice when editing item (inputFromItem sets type spinner triggering again)
            //     need to ensure, item appearance is preserved
            if(item_tracker().editing_item != null)
                setItemAppearance(item_tracker().editing_item.appearance);
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    ///

    InputChangedListener event_listener;

    private void setListeners() {
        event_listener = new InputChangedListener();
        //itemTypeInput().setOnItemSelectedListener(event_listener);
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
