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
        return name != UNIDENTIFIED;
    }

    public String appearance;

    public int cost;

    public int buy_price;

    public int sell_price;

    public int weight;

    public double probability;

    public String probability_str;

    public String notes;

    // should be overridden in subclasses (supporting game tracker)
    public static Item extract(String str){
        String attrs[] = str.split("-");
        String type = attrs[0];

        // XXX
        int num_remaining = type.length() - 5;
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
        s.add(type());
        s.add(name);
        s.add(appearance);
        s.add(Integer.toString(buy_price));
        s.add(Integer.toString(sell_price));
        return s;
    }

    public String compact(){
        return TextUtils.join("-", compactStringList());
    }

    // should be overridden in subclasses (supporting game tracker)
    protected ArrayList<String> stringList(){
        ArrayList<String> s = new ArrayList<String>();
        s.add(appearance);
        s.add(Integer.toString(buy_price));
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
            return item.buy_price == buy;
        }
    }

    public static class ItemSellPriceFilter implements Items.filter {
        int sell;

        public ItemSellPriceFilter(int sell){ this.sell = sell; }

        public boolean matches(Item item){
            return item.sell_price == sell;
        }
    }
}
