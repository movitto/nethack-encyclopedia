package org.morsi.android.nethack.redux.util;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by mmorsi on 5/3/16.
 */
public class Level {
    public String parent;

    // 'num' will be relative to parent, eg 12th level of dungeons, 3rd level of mines, 11th level of gehennom
    public int num;

    public String id(){
        return parent + Integer.toString(num);
    }

    // shops present
    public boolean general_shop;
    public boolean armor_shop;
    public boolean bookstore;
    public boolean liquor_shop;
    public boolean weapon_shop;
    public boolean delicatessan;
    public boolean jeweler;
    public boolean apparel_shop;
    public boolean hardware_store;
    public boolean rare_book_store;
    public boolean lighting_store;

    // other level features present
    public int fountains;
    public int aligned_altars;
    public int x_aligned_altars;
    public int neutal_altars;
    public int sinks;
    public int thrones;
    public int temples;

    // true if level holds a player stash
    public boolean stash;

    // special levels / enterances to them
    public boolean oracle;
    public boolean mines;
    public boolean minetown;
    public boolean mines_end;
    public boolean sokoban;
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
        ArrayList<String> s = new ArrayList<String>();

        if(oracle)               s.add("oracle");
        if(mines)                s.add("mines");
        if(minetown)             s.add("minetown");
        if(mines_end)            s.add("mines end");
        if(sokoban)              s.add("sokoban");
        if(ludios)               s.add("ludios");
        if(rogue)                s.add("rogue");
        if(quest)                s.add("quest");
        if(medusa)               s.add("medusa");
        if(castle)               s.add("castle");
        if(gehennom)             s.add("gehennom");
        if(valley)               s.add("valley");
        if(azmodeus)             s.add("azmodeus");
        if(juiblex)              s.add("juilblex");
        if(baalzebub)            s.add("baalzebub");
        if(orcus_town)           s.add("ocrus_town");
        if(wizards_tower)        s.add("wizards tower");
        if(fake_wizards_tower)   s.add("fake wizards tower");
        if(vibrating_square)     s.add("vibrating square");
        if(moloch)               s.add("moloch");
        if(vlad)                 s.add("vlad");

        if(stash)                s.add("stash");

        if(general_shop)         s.add("general store");
        if(armor_shop)           s.add("armor store");
        if(bookstore)            s.add("bookstore");
        if(liquor_shop)          s.add("liquor store");
        if(weapon_shop)          s.add("weapon shop");
        if(delicatessan)         s.add("delicatessan");
        if(jeweler)              s.add("jeweler");
        if(apparel_shop)         s.add("apparel_shop");
        if(hardware_store)       s.add("hardware store");
        if(rare_book_store)      s.add("rare book store");
        if(lighting_store)       s.add("lighting store");

        if(fountains > 0)        s.add(Integer.toString(fountains)        + " fountains");
        if(aligned_altars > 0)   s.add(Integer.toString(aligned_altars)   + " aligned altars");
        if(x_aligned_altars > 0) s.add(Integer.toString(x_aligned_altars) + " x aligned altars");
        if(neutal_altars > 0)    s.add(Integer.toString(neutal_altars)    + " neutral altars");
        if(sinks > 0)            s.add(Integer.toString(sinks)            + " sinks");
        if(thrones > 0)          s.add(Integer.toString(thrones)          + " thrones");
        if(temples > 0)          s.add(Integer.toString(temples)          + " temples");

        return TextUtils.join(" ", s);
    }

    public String compact(){
        return parent + "-" + Integer.toString(num) + "-" +   // 0,1
                Boolean.toString(general_shop)       + "-" +
                Boolean.toString(armor_shop)         + "-" +
                Boolean.toString(bookstore)          + "-" +
                Boolean.toString(liquor_shop)        + "-" +  // 5
                Boolean.toString(weapon_shop)        + "-" +
                Boolean.toString(delicatessan)       + "-" +
                Boolean.toString(jeweler)            + "-" +
                Boolean.toString(apparel_shop)       + "-" +
                Boolean.toString(hardware_store)     + "-" +  // 10
                Boolean.toString(rare_book_store)    + "-" +
                Boolean.toString(lighting_store)     + "-" +

                Integer.toString(fountains)          + "-" +
                Integer.toString(aligned_altars)     + "-" +
                Integer.toString(x_aligned_altars)   + "-" +  // 15
                Integer.toString(neutal_altars)      + "-" +
                Integer.toString(sinks)              + "-" +
                Integer.toString(thrones)            + "-" +
                Integer.toString(temples)            + "-" +

                Boolean.toString(stash)              + "-" +  // 20

                Boolean.toString(oracle)             + "-" +
                Boolean.toString(mines)              + "-" +
                Boolean.toString(minetown)           + "-" +
                Boolean.toString(mines_end)          + "-" +
                Boolean.toString(sokoban)            + "-" +  // 25
                Boolean.toString(ludios)             + "-" +
                Boolean.toString(rogue)              + "-" +
                Boolean.toString(quest)              + "-" +
                Boolean.toString(medusa)             + "-" +
                Boolean.toString(castle)             + "-" +  // 30
                Boolean.toString(gehennom)           + "-" +
                Boolean.toString(valley)             + "-" +
                Boolean.toString(azmodeus)           + "-" +
                Boolean.toString(juiblex)            + "-" +
                Boolean.toString(baalzebub)          + "-" +  // 35
                Boolean.toString(orcus_town)         + "-" +
                Boolean.toString(wizards_tower)      + "-" +
                Boolean.toString(fake_wizards_tower) + "-" +
                Boolean.toString(vibrating_square)   + "-" +
                Boolean.toString(moloch)             + "-" +  // 40
                Boolean.toString(vlad)               + "-";
    }

    public static Level extract(String str){
        String attrs[] = str.split("-");
        Level level = new Level();
        level.parent             = attrs[0];
        level.num                = Integer.parseInt(attrs[1]);
        level.general_shop       = Boolean.parseBoolean(attrs[2]);
        level.armor_shop         = Boolean.parseBoolean(attrs[3]);
        level.bookstore          = Boolean.parseBoolean(attrs[4]);
        level.liquor_shop        = Boolean.parseBoolean(attrs[5]);
        level.weapon_shop        = Boolean.parseBoolean(attrs[6]);
        level.delicatessan       = Boolean.parseBoolean(attrs[7]);
        level.jeweler            = Boolean.parseBoolean(attrs[8]);
        level.apparel_shop       = Boolean.parseBoolean(attrs[9]);
        level.hardware_store     = Boolean.parseBoolean(attrs[10]);
        level.rare_book_store    = Boolean.parseBoolean(attrs[11]);
        level.lighting_store     = Boolean.parseBoolean(attrs[12]);

        level.fountains          = Integer.parseInt(attrs[13]);
        level.aligned_altars     = Integer.parseInt(attrs[14]);
        level.x_aligned_altars   = Integer.parseInt(attrs[15]);
        level.neutal_altars      = Integer.parseInt(attrs[16]);
        level.sinks              = Integer.parseInt(attrs[17]);
        level.thrones            = Integer.parseInt(attrs[18]);
        level.temples            = Integer.parseInt(attrs[19]);

        level.stash              = Boolean.parseBoolean(attrs[20]);

        level.oracle             = Boolean.parseBoolean(attrs[21]);
        level.mines              = Boolean.parseBoolean(attrs[22]);
        level.minetown           = Boolean.parseBoolean(attrs[23]);
        level.mines_end          = Boolean.parseBoolean(attrs[24]);
        level.sokoban            = Boolean.parseBoolean(attrs[25]);
        level.ludios             = Boolean.parseBoolean(attrs[26]);
        level.rogue              = Boolean.parseBoolean(attrs[27]);
        level.quest              = Boolean.parseBoolean(attrs[28]);
        level.medusa             = Boolean.parseBoolean(attrs[29]);
        level.castle             = Boolean.parseBoolean(attrs[30]);
        level.gehennom           = Boolean.parseBoolean(attrs[31]);
        level.valley             = Boolean.parseBoolean(attrs[32]);
        level.azmodeus           = Boolean.parseBoolean(attrs[33]);
        level.juiblex            = Boolean.parseBoolean(attrs[34]);
        level.baalzebub          = Boolean.parseBoolean(attrs[35]);
        level.orcus_town         = Boolean.parseBoolean(attrs[36]);
        level.wizards_tower      = Boolean.parseBoolean(attrs[37]);
        level.fake_wizards_tower = Boolean.parseBoolean(attrs[38]);
        level.vibrating_square   = Boolean.parseBoolean(attrs[39]);
        level.moloch             = Boolean.parseBoolean(attrs[40]);
        level.vlad               = Boolean.parseBoolean(attrs[41]);

        return level;
    }
}
