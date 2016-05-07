package org.morsi.android.nethack.redux.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import org.morsi.android.nethack.redux.GameTrackerActivity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.trackers.LevelTracker;
import org.morsi.android.nethack.redux.util.Input;
import org.morsi.android.nethack.redux.util.Level;

public class LevelDialog {
    Activity activity;

    Dialog dialog;

    GameTrackerActivity game_tracker(){
        return (GameTrackerActivity) activity;
    }

    LevelTracker level_tracker(){
        return game_tracker().level_tracker;
    }

    private Button closeButton(){
        return (Button) dialog.findViewById(R.id.level_close);
    }

    ///

    private Spinner parentInput(){
        return (Spinner) dialog.findViewById(R.id.levelParentInput);
    }

    private String parentValue(){
        return parentInput().getSelectedItem().toString();
    }

    private String[] parents(){
        return dialog.getContext().getResources().getStringArray(R.array.level_parents);
    }

    private int parentIndex(String parent){
        String[] parents = parents();
        for(int p = 0; p < parents.length; ++p)
            if(parents[p].equals(parent))
                return p;
        return -1;
    }

    private ArrayAdapter<String> parentAdapter(){
        ArrayAdapter<String> aa = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, parents());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return aa;
    }

    private Spinner numInput(){
        return (Spinner) dialog.findViewById(R.id.levelNumInput);
    }

    private String numValueString(){
        return numInput().getSelectedItem().toString();
    }

    private Integer[] nums(){
        Integer[] i = new Integer[30];
        for(int ii = 0; ii < 30; ++ii)
            i[ii] = ii + 1;
        return i;
    }

    private ArrayAdapter<Integer> numAdapter(){
        ArrayAdapter<Integer> aa = new ArrayAdapter<Integer>(activity, android.R.layout.simple_spinner_item, nums());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return aa;
    }

    private int numValue(){
        return Integer.parseInt(numValueString());
    }

    ///

    private ToggleButton storeInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelStoreInput);
    }

    private boolean store(){
        return storeInput().isChecked();
    }

    private ToggleButton bookStoreInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelBookStoreInput);
    }

    private boolean bookStore(){
        return bookStoreInput().isChecked();
    }

    private ToggleButton armorStoreInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelArmorStoreInput);
    }

    private boolean armorStore(){
        return armorStoreInput().isChecked();
    }

    private ToggleButton liquorStoreInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelLiquorStoreInput);
    }

    private boolean liquorStore(){
        return liquorStoreInput().isChecked();
    }

    private ToggleButton weaponShopInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelWeaponShopInput);
    }

    private boolean weaponShop(){
        return weaponShopInput().isChecked();
    }

    private ToggleButton delicatessanInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelDelicatessanInput);
    }

    private boolean delicatessan(){
        return delicatessanInput().isChecked();
    }

    private ToggleButton jewelerInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelJewelerInput);
    }

    private boolean jeweler(){
        return jewelerInput().isChecked();
    }

    private ToggleButton apparelShopInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelApparelShopInput);
    }

    private boolean apparelShop(){
        return apparelShopInput().isChecked();
    }

    private ToggleButton hardwareStoreInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelHardwareStoreInput);
    }

    private boolean hardwareStore(){
        return hardwareStoreInput().isChecked();
    }

    private ToggleButton rareBookStoreInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelRareBookStoreInput);
    }

    private boolean rareBookStore(){
        return rareBookStoreInput().isChecked();
    }

    private ToggleButton lightingStoreInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelLightingStoreInput);
    }

    private boolean lightingStore(){
        return lightingStoreInput().isChecked();
    }

    ///

    private EditText fountainsInput(){
        return (EditText) dialog.findViewById(R.id.levelFountainsInput);
    }

    private String fountainsInputString(){
        return fountainsInput().getText().toString();
    }

    private int fountains(){
        return Integer.parseInt(fountainsInputString());
    }

    private EditText alignedAltarsInput(){
        return (EditText) dialog.findViewById(R.id.levelAlignedAltarsInput);
    }

    private String alignedAltarsInputString(){
        return alignedAltarsInput().getText().toString();
    }

    private int alignedAltars(){
        return Integer.parseInt(alignedAltarsInputString());
    }

    private EditText xAlignedAltarsInput(){
        return (EditText) dialog.findViewById(R.id.levelXAlignedAltarsInput);
    }

    private String xAlignedAltarsInputString(){
        return xAlignedAltarsInput().getText().toString();
    }

    private int xAlignedAltars(){
        return Integer.parseInt(xAlignedAltarsInputString());
    }

    private EditText neutralAltarsInput(){
        return (EditText) dialog.findViewById(R.id.levelNeutralAltarsInput);
    }

    private String neutralAltarsInputString(){
        return neutralAltarsInput().getText().toString();
    }

    private int neutralAltars(){
        return Integer.parseInt(neutralAltarsInputString());
    }

    private EditText sinksInput(){
        return (EditText) dialog.findViewById(R.id.levelSinksInput);
    }

    private String sinksInputString(){
        return sinksInput().getText().toString();
    }

    private int sinks(){
        return Integer.parseInt(sinksInputString());
    }

    private EditText thronesInput(){
        return (EditText) dialog.findViewById(R.id.levelThronesInput);
    }

    private String thronesInputString(){
        return thronesInput().getText().toString();
    }

    private int thrones(){
        return Integer.parseInt(thronesInputString());
    }

    private EditText templesInput(){
        return (EditText) dialog.findViewById(R.id.levelTemplesInput);
    }

    private String templesInputString(){
        return templesInput().getText().toString();
    }

    private int temples(){
        return Integer.parseInt(templesInputString());
    }

    private ToggleButton stashInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelStashInput);
    }

    private boolean stash(){
        return stashInput().isChecked();
    }

    ///

    private ToggleButton oracleInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelOracleInput);
    }

    private boolean oracle(){
        return oracleInput().isChecked();
    }

    private ToggleButton minesInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelMinesInput);
    }

    private boolean mines(){
        return minesInput().isChecked();
    }

    private ToggleButton mineTownInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelMineTownInput);
    }

    private boolean mineTown(){
        return mineTownInput().isChecked();
    }

    private ToggleButton minesEndInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelMinesEndInput);
    }

    private boolean minesEnd(){
        return minesEndInput().isChecked();
    }

    private ToggleButton sokobanInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelSokobanInput);
    }

    private boolean sokoban(){
        return sokobanInput().isChecked();
    }

    private ToggleButton ludiosInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelLudiosInput);
    }

    private boolean ludios(){
        return ludiosInput().isChecked();
    }

    private ToggleButton rogueInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelRogueInput);
    }

    private boolean rogue(){
        return rogueInput().isChecked();
    }

    private ToggleButton questInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelQuestInput);
    }

    private boolean quest(){
        return questInput().isChecked();
    }

    private ToggleButton medusaInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelMedusaInput);
    }

    private boolean medusa(){
        return medusaInput().isChecked();
    }

    private ToggleButton castleInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelCastleInput);
    }

    private boolean castle(){
        return castleInput().isChecked();
    }

    private ToggleButton gehennomInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelGehennomInput);
    }

    private boolean gehennom(){
        return gehennomInput().isChecked();
    }

    private ToggleButton valleyInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelValleyInput);
    }

    private boolean valley(){
        return valleyInput().isChecked();
    }

    private ToggleButton azmodeusInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelAzmodeusInput);
    }

    private boolean azmodeus(){
        return azmodeusInput().isChecked();
    }

    private ToggleButton juiblexInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelJuiblexInput);
    }

    private boolean juiblex(){
        return juiblexInput().isChecked();
    }

    private ToggleButton baazlebubInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelBaazlebubInput);
    }

    private boolean baazlebub(){
        return baazlebubInput().isChecked();
    }

    private ToggleButton orcusTownInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelOrcusTownInput);
    }

    private boolean orcusTown(){
        return orcusTownInput().isChecked();
    }

    private ToggleButton wizardsTowerInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelWizardsTowerInput);
    }

    private boolean wizardsTower(){
        return wizardsTowerInput().isChecked();
    }

    private ToggleButton fakeWizardsTowerInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelFakeWizardsTowerInput);
    }

    private boolean fakeWizardsTower(){
        return fakeWizardsTowerInput().isChecked();
    }

    private ToggleButton vibratingSquareInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelVibratingSquareInput);
    }

    private boolean vibratingSquare(){
        return vibratingSquareInput().isChecked();
    }

    private ToggleButton molochInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelMolochInput);
    }

    private boolean moloch(){
        return molochInput().isChecked();
    }

    private ToggleButton vladInput(){
        return (ToggleButton) dialog.findViewById(R.id.levelVladInput);
    }

    private boolean vlad(){
        return vladInput().isChecked();
    }

    private LevelDialog(Activity activity){
        this.activity = activity;
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.level_dialog);
        dialog.setTitle("Level Properties");

        dialog_listener = new LevelDialogListener(dialog);

        // wire up close button
        closeButton().setOnClickListener(dialog_listener);

        initializeSpinners();

        if(level_tracker().editing_level != null)
            inputFromLevel(level_tracker().editing_level);
    }

    public static Dialog create(Activity activity) {
        return new LevelDialog(activity).dialog;
    }

    private void initializeSpinners(){
        parentInput().setAdapter(parentAdapter());
        numInput().setAdapter(numAdapter());
    }

    private Level levelFromInput(){
        Level level              = new Level();

        level.parent             = parentValue();
        level.num                = numValue();
        level.general_shop       = store();
        level.armor_shop         = armorStore();
        level.bookstore          = bookStore();
        level.liquor_shop        = liquorStore();
        level.weapon_shop        = weaponShop();
        level.delicatessan       = delicatessan();
        level.jeweler            = jeweler();
        level.apparel_shop       = apparelShop();
        level.hardware_store     = hardwareStore();
        level.rare_book_store    = rareBookStore();
        level.lighting_store     = lightingStore();
        level.fountains          = Input.validInt(fountainsInputString())      ? fountains()      : 0;
        level.aligned_altars     = Input.validInt(alignedAltarsInputString())  ? alignedAltars()  : 0;
        level.x_aligned_altars   = Input.validInt(xAlignedAltarsInputString()) ? xAlignedAltars() : 0;
        level.neutal_altars      = Input.validInt(neutralAltarsInputString())  ? neutralAltars()  : 0;
        level.sinks              = Input.validInt(sinksInputString())          ? sinks()          : 0;
        level.thrones            = Input.validInt(thronesInputString())        ? thrones()        : 0;
        level.temples            = Input.validInt(templesInputString())        ? temples()        : 0;
        level.stash              = stash();
        level.oracle             = oracle();
        level.mines              = mines();
        level.minetown           = mineTown();
        level.mines_end          = minesEnd();
        level.sokoban            = sokoban();
        level.quest              = quest();
        level.ludios             = ludios();
        level.rogue              = rogue();
        level.medusa             = medusa();
        level.castle             = castle();
        level.gehennom           = gehennom();
        level.valley             = valley();
        level.azmodeus           = azmodeus();
        level.juiblex            = juiblex();
        level.baalzebub          = baazlebub();
        level.orcus_town         = orcusTown();
        level.wizards_tower      = wizardsTower();
        level.fake_wizards_tower = fakeWizardsTower();
        level.vibrating_square   = vibratingSquare();
        level.moloch             = moloch();
        level.vlad               = vlad();

        return level;
    }

    private void inputFromLevel(Level level){
        parentInput().setSelection(parentIndex(level.parent));
        numInput().setSelection(level.num - 1);
        storeInput().setChecked(level.general_shop);
        armorStoreInput().setChecked(level.armor_shop);
        bookStoreInput().setChecked(level.bookstore);
        liquorStoreInput().setChecked(level.liquor_shop);
        weaponShopInput().setChecked(level.weapon_shop);
        delicatessanInput().setChecked(level.delicatessan);
        jewelerInput().setChecked(level.jeweler);
        apparelShopInput().setChecked(level.apparel_shop);
        hardwareStoreInput().setChecked(level.hardware_store);
        rareBookStoreInput().setChecked(level.rare_book_store);
        lightingStoreInput().setChecked(level.lighting_store);
        fountainsInput().setText(Integer.toString(level.fountains));
        alignedAltarsInput().setText(Integer.toString(level.aligned_altars));
        xAlignedAltarsInput().setText(Integer.toString(level.x_aligned_altars));
        neutralAltarsInput().setText(Integer.toString(level.neutal_altars));
        sinksInput().setText(Integer.toString(level.sinks));
        thronesInput().setText(Integer.toString(level.thrones));
        templesInput().setText(Integer.toString(level.temples));
        stashInput().setChecked(level.stash);
        oracleInput().setChecked(level.oracle);
        minesInput().setChecked(level.mines);
        mineTownInput().setChecked(level.minetown);
        minesEndInput().setChecked(level.mines_end);
        sokobanInput().setChecked(level.sokoban);
        questInput().setChecked(level.quest);
        rogueInput().setChecked(level.rogue);
        medusaInput().setChecked(level.medusa);
        castleInput().setChecked(level.castle);
        gehennomInput().setChecked(level.gehennom);
        valleyInput().setChecked(level.valley);
        azmodeusInput().setChecked(level.azmodeus);
        juiblexInput().setChecked(level.juiblex);
        baazlebubInput().setChecked(level.baalzebub);
        orcusTownInput().setChecked(level.orcus_town);
        wizardsTowerInput().setChecked(level.wizards_tower);
        fakeWizardsTowerInput().setChecked(level.fake_wizards_tower);
        vibratingSquareInput().setChecked(level.vibrating_square);
        molochInput().setChecked(level.moloch);
        vladInput().setChecked(level.vlad);
    }

    LevelDialogListener dialog_listener;

    class LevelDialogListener extends DialogListener{
        LevelDialogListener(Dialog dialog){
            super(dialog);
        }

        public void onClick (View v){
            level_tracker().addLevel(levelFromInput());
            super.onClick(v);
        }
    }
}
