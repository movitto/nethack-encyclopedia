package org.morsi.android.nethack.redux.util;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by mmorsi on 5/3/16.
 */
public class Level {
    int num;

    // if set 'num' will be relative to parent, eg 3rd level of mines, or 11th level of ghennom
    String parent;

    public boolean general_shop;
    public boolean armor_shop;
    public boolean bookstore;
    public boolean liquor_shop;
    public boolean weapon_shop;
    public boolean delicatessan;
    public boolean jewler;
    public boolean apparel_shop;
    public boolean hardware_store;
    public boolean rare_book_store;
    public boolean lighting_store;

    public int fountains;
    public int aligned_altar;
    public int x_aligned_altar;
    public int neutal_altar;
    public int sinks;
    public int throne;
    public int temples;

    public boolean stash;

    public boolean oracle;
    public boolean mines;
    public boolean minetown;
    public boolean mines_end;
    public boolean ludios;
    public boolean rogue;
    public boolean quest;
    public boolean medusa;
    public boolean castle;
    public boolean gehennom;
    public boolean valley;
    public boolean azmodeus;
    public boolean juiblex;
    public boolean baalzebub;
    public boolean orcus_town;
    public boolean wizards_tower;
    public boolean fake_wizards_tower;
    public boolean vibrating_square;
    public boolean moloch;
    public boolean vlad;

    public String toString(){
        return parent + "-" + Integer.toString(num) + "-" +
                Boolean.toString(general_shop)      + "-" +
                Boolean.toString(armor_shop)        + "-" +
                Boolean.toString(bookstore)         + "-" +
                Boolean.toString(liquor_shop)       + "-" +
                Boolean.toString(weapon_shop)       + "-" +
                Boolean.toString(delicatessan)      + "-" +
                Boolean.toString(jewler)            + "-" +
                Boolean.toString(apparel_shop)      + "-" +
                Boolean.toString(hardware_store)    + "-" +
                Boolean.toString(rare_book_store)   + "-" +
                Boolean.toString(lighting_store)    + "-" +
                Integer.toString(fountains)         + "-" +
                Integer.toString(aligned_altar)     + "-" +
                Integer.toString(x_aligned_altar)   + "-" +
                Integer.toString(neutal_altar)      + "-" +
                Integer.toString(sinks)             + "-" +
                Integer.toString(throne)            + "-" +
                Integer.toString(temples)           + "-" +
                Boolean.toString(stash);
    }

    public static Level fromString(String str){
        String attrs[] = str.split("-");
        Level level = new Level();
        level.parent          = attrs[0];
        level.num             = Integer.parseInt(attrs[1]);
        level.general_shop    = Boolean.parseBoolean(attrs[2]);
        level.armor_shop      = Boolean.parseBoolean(attrs[3]);
        level.bookstore       = Boolean.parseBoolean(attrs[4]);
        level.liquor_shop     = Boolean.parseBoolean(attrs[5]);
        level.weapon_shop     = Boolean.parseBoolean(attrs[6]);
        level.delicatessan    = Boolean.parseBoolean(attrs[7]);
        level.jewler          = Boolean.parseBoolean(attrs[8]);
        level.apparel_shop    = Boolean.parseBoolean(attrs[9]);
        level.hardware_store  = Boolean.parseBoolean(attrs[10]);
        level.rare_book_store = Boolean.parseBoolean(attrs[11]);
        level.lighting_store  = Boolean.parseBoolean(attrs[12]);
        level.fountains       = Integer.parseInt(attrs[13]);
        level.aligned_altar   = Integer.parseInt(attrs[14]);
        level.x_aligned_altar = Integer.parseInt(attrs[15]);
        level.neutal_altar    = Integer.parseInt(attrs[16]);
        level.sinks           = Integer.parseInt(attrs[17]);
        level.throne          = Integer.parseInt(attrs[18]);
        level.temples         = Integer.parseInt(attrs[19]);
        level.stash           = Boolean.parseBoolean(attrs[20]);
        return level;
    }
}
