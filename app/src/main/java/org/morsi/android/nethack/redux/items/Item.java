package org.morsi.android.nethack.redux.items;

import android.text.TextUtils;

import org.morsi.android.nethack.redux.util.QuickStat;
import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class Item {

    public static final String UNIDENTIFIED = "unidentified";

    public String id(){ return type() + name; }

    // should be overridden in subclasses (supporting quick stats or game tracker)
    public static String type(){ return ""; }

    // should be overriden in subclasses (supporting quick stats or game tracker)
    public String itemType() { return ""; }

    // should be override in subclasses (supporting quick stats)
    public static String quickStatsCategoryName(){ return ""; }

    // should be overriden in subclasses (supporting quick stats)
    public static ArrayList<String> columnNames(){ return new ArrayList<String>(); }

    // should be overriden in subclasses (supporting quick stats)
    public static ArrayList<Double> columnWeights(){ return new ArrayList<Double>(); }

    // should be overriden in subclasses (supporting quick stats)
    public ArrayList<String> columns(){
        return new ArrayList<String>();
    }

    public QuickStat toQuickStat(){
        return new QuickStat(columns());
    }

    public String name;

    public boolean identified(){
        return !name.equals(UNIDENTIFIED);
    }

    public String appearance = "";

    public boolean hasAppearance(){
        return !appearance.equals("");
    }

    public int cost;

    // stored buy and sell prices
    public int buy_price;
    public int sell_price;

    public boolean hasBuyPrice(){
        return buy_price != 0;
    }

    public boolean hasSellPrice(){
        return sell_price != 0;
    }

    // these are assuming you're not a 'sucker':
    // - wearing an uncovered tshirt
    // - wearing dunce cap
    // - tourist under lvl 14
    // https://nethackwiki.com/wiki/Price_identification

    public int buyPrice(){
        return buyPrice(false);
    }

    // 25% of the time unidentified items receive a 33% surcharge)
    public int buyPrice(boolean identified_modifier){
        long id_surcharge = Math.round(identified_modifier ? (cost * 0.33) : 0);
        long ch_surcharge = Math.round(cost * charismaModifier());
        return (int)(cost + id_surcharge + ch_surcharge);
    };

    /// charisma of buyer affects buy price
    public int buyer_charisma = 0;

    private double charismaModifier(){
        if(buyer_charisma > 18)
            return -0.5;
        else if(buyer_charisma > 17)
            return -0.33;
        else if(buyer_charisma > 15)
            return -0.25;
        else if(buyer_charisma > 10)
            return 0;
        else if(buyer_charisma > 7)
            return 0.33;
        else if(buyer_charisma > 5)
            return 0.50;
        else
            return 1;
    }

    public int sellPrice(){
        return sellPrice(false);
    }

    // 25% of the time in game, you'll only be offered 3/4 of this value, or 3/8 of the overall base cost)
    public int sellPrice(boolean sell_modifier){
        double sell = cost / 2;
        return (int)(sell_modifier ? (sell * 0.75) : sell);
    };

    public int weight;

    public double probability;

    public String probability_str;

    public String notes;

    // should be overridden in subclasses (supporting game tracker)
    public static Item extract(String str){
        String attrs[] = str.split("-");
        String type = attrs[0];

        boolean valid_type = type.equals(Potion.type()) ||
                type.equals(Scroll.type()) ||
                type.equals(Wand.type()) ||
                type.equals(SpellBook.type()) ||
                type.equals(Ring.type()) ||
                type.equals(Amulet.type()) ||
                type.equals(Gem.type());

        if(attrs.length < 5 || !valid_type)
            return null;

        // XXX
        int num_remaining = attrs.length - 5;
        String remaining_attrs[] = new String[num_remaining];
        System.arraycopy(attrs, 5, remaining_attrs, 0, num_remaining);
        String remaining = TextUtils.join("-", remaining_attrs);

        Item item = new Item();
        if(Potion.type().equals(type))
            item = Potion.extract(remaining);
        else if(Scroll.type().equals(type))
            item = Scroll.extract(remaining);
        else if(Wand.type().equals(type))
            item = Wand.extract(remaining);
        else if(SpellBook.type().equals(type))
            item = SpellBook.extract(remaining);
        else if(Ring.type().equals(type))
            item = Ring.extract(remaining);
        else if(Amulet.type().equals(type))
            item = Amulet.extract(remaining);
        else if(Gem.type().equals(type))
            item = Gem.extract(remaining);

        item.name       = attrs[1];
        item.appearance = attrs[2];
        item.buy_price  = Integer.parseInt(attrs[3]);
        item.sell_price = Integer.parseInt(attrs[4]);

        return item;
    }

    // should be overridden in subclasses (supprting game tracker)
    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = new ArrayList<String>();
        s.add(itemType());
        s.add(name);
        s.add(appearance);
        s.add(Integer.toString(buy_price));
        s.add(Integer.toString(sell_price));
        return s;
    }

    public String compact(){
        return TextUtils.join("-", compactStringList());
    }

    // can be overridden to add more attributes to toString output
    protected ArrayList<String> stringList(){
        ArrayList<String> s = new ArrayList<String>();
        if(!appearance.equals(""))
            s.add(appearance);
        if(buy_price  != 0)
            s.add(Integer.toString(buy_price));
        if(sell_price != 0)
            s.add(Integer.toString(sell_price));
        return s;
    }

    public String toString() {
        return TextUtils.join(" ", stringList());
    }

    ///

    public static class ItemTypeFilter implements Items.filter {
        String type;

        public ItemTypeFilter(String type){ this.type = type; }

        public boolean matches(Item item){
            return item.itemType().equals(type);
        }
    }

    public static class ItemAppearanceFilter implements Items.filter {
        String appearance;

        public ItemAppearanceFilter(String appearance){ this.appearance = appearance; }

        public boolean matches(Item item){
            return item.appearance.equals(appearance);
        }
    }

    public static class ItemBuyPriceFilter implements Items.filter {
        int buy;

        public ItemBuyPriceFilter(int buy){ this.buy = buy; }

        public boolean matches(Item item){
            return item.buyPrice() == buy || item.buyPrice(true) == buy;
        }
    }

    public static class ItemSellPriceFilter implements Items.filter {
        int sell;

        public ItemSellPriceFilter(int sell){ this.sell = sell; }

        public boolean matches(Item item){
            return item.sellPrice() == sell || item.sellPrice(true) == sell;
        }
    }
}
