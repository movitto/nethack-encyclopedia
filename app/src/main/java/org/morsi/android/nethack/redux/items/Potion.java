package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Potion extends Item {
    public String quaff_effect;

    public String throw_effect;

    public static String type(){ return "Potion"; }

    public String itemType() { return "Potion"; }

    public static String quickStatsCategoryName(){ return "Potions"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Potion");
        columns.add("Cost");
        columns.add("Weight");
        columns.add("Probability");
        columns.add("Appearance");
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

    public ArrayList<String> columns(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(name);
        columns.add(Integer.toString(cost));
        columns.add(Integer.toString(weight));
        columns.add(probability_str);
        columns.add(appearance);
        return columns;
    }

    public static Potion extract(String str) {
        String attrs[] = str.split("-");
        Potion potion = new Potion();
        potion.quaff_effect = attrs[0];
        potion.throw_effect = attrs[1];
        return potion;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(quaff_effect);
        s.add(throw_effect);
        return s;
    }

    protected ArrayList<String> stringList(){
        ArrayList<String> s = super.stringList();
        s.add(quaff_effect);
        s.add(throw_effect);
        return s;
    }

    public static ArrayList<Potion> fromXML(XmlResourceParser xpp){
        ArrayList<Potion> potions = new ArrayList<Potion>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Potion current_potion = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("potion"))
                        current_potion = new Potion();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_potion.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_potion.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_potion.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("probability"))
                        current_potion.probability_str = xpp.getText();
                    else if(element_name.equals("appearance"))
                        current_potion.appearance = xpp.getText();
                    else if(element_name.equals("buy"))
                        current_potion.buy_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sell"))
                        current_potion.sell_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("quaff"))
                        current_potion.quaff_effect = xpp.getText();
                    else if(element_name.equals("throw"))
                        current_potion.throw_effect = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("potion")) {
                    potions.add(current_potion);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return potions;
    }

    ///

    public static class QuaffFilter implements Items.filter {
        String effect;

        public QuaffFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
        return ((Potion)item).quaff_effect.equals(effect);
    }
    };

    public static class ThrowFilter implements Items.filter {
        String effect;

        public ThrowFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
        return ((Potion)item).throw_effect.equals(effect);
    }
    };
}
