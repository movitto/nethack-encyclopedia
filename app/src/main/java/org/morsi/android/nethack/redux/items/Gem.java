package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Gem extends Item {
    public String engraving_type;

    public String streak_color;

    public static String type(){ return "Gem"; }

    public String itemType() { return "Gem"; }

    public static String quickStatsCategoryName(){ return "Gems"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Gem");
        columns.add("Desc");
        columns.add("Cost");
        columns.add("Weight");
        columns.add("Hardness");

        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.32);
        weights.add(0.15);
        weights.add(0.15);
        weights.add(0.13);
        weights.add(0.25);
        return weights;
    }

    public static QuickStatCategory toQuickStatsCategory(){
        QuickStatCategory category = new QuickStatCategory(quickStatsCategoryName());
        category.column_names      = columnNames();
        category.column_weights    = columnWeights();
        return category;
    }

    public ArrayList<String> columns() {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(name);
        columns.add(appearance);
        columns.add(Integer.toString(cost));
        columns.add(Integer.toString(weight));
        columns.add(engraving_type);
        return columns;
    }

    public static Gem extract(String str) {
        String attrs[] = str.split("-");
        Gem gem = new Gem();
        gem.engraving_type = attrs[0];
        gem.streak_color = attrs[1];
        return gem;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(engraving_type);
        s.add(streak_color);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(engraving_type);
        s.add(streak_color);
        return s;
    }

    public static ArrayList<Gem> fromXML(XmlResourceParser xpp) {
        ArrayList<Gem> gems = new ArrayList<Gem>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Gem current_gem = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("gem"))
                        current_gem = new Gem();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_gem.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_gem.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_gem.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("appearance"))
                        current_gem.appearance = xpp.getText();
                    else if(element_name.equals("buy"))
                        current_gem.buy_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sell"))
                        current_gem.sell_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("engraving"))
                        current_gem.engraving_type = xpp.getText();
                    else if(element_name.equals("streak"))
                        current_gem.streak_color = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("gem")) {
                    gems.add(current_gem);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return gems;
    }

    ///

    public static class EngravingFilter implements Items.filter {
        String effect;

        public EngravingFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Gem)item).engraving_type.equals(effect);
        }
    };

    public static class StreakFilter implements Items.filter {
        String effect;

        public StreakFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Gem)item).streak_color.equals(effect);
        }
    };
}
