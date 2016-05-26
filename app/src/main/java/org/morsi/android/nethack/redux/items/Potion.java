package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Potion extends Item {
    ///

    public String quaff_effect;

    public boolean hasQuaffEffect(){
        return !quaff_effect.equals("");
    }

    public ArrayList<String> quaff_effects;

    public String throw_effect;

    public boolean hasThrowEffect(){
        return !throw_effect.equals("");
    }

    public ArrayList<String> throw_effects;

    public static String type(){ return "Potion"; }

    public String itemType() { return "Potion"; }

    public static String quickStatsCategoryName(){ return "Potions"; }

    public Potion(){
        quaff_effects = new ArrayList<String>();
        throw_effects = new ArrayList<String>();
    }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Potion");
        columns.add("$");
        columns.add("Weight");
        columns.add("%");
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
                    else if(element_name.equals("quaff"))
                        current_potion.quaff_effects.add(xpp.getText());
                    else if(element_name.equals("throw"))
                        current_potion.throw_effects.add(xpp.getText());

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

    public static class AppearanceFilter implements  Items.filter{
        String appearance;

        public AppearanceFilter(String appearance){ this.appearance = appearance; }

        private boolean a_clear_appearance(){
            return clear_appearance() || cursed_clear_appearance() || uncursed_clear_appearance();
        }

        private boolean clear_appearance(){
            return appearance.equals("clear");
        }

        private boolean cursed_clear_appearance(){
            return appearance.equals("cursed clear");
        }

        private boolean uncursed_clear_appearance(){
            return appearance.equals("uncursed clear");
        }

        private boolean water(Item item){
            return item.name.equals("water");
        }

        private boolean unholy_water(Item item){
            return item.name.equals("unholy water");
        }

        private boolean holy_water(Item item){
            return item.name.equals("holy water");
        }

        private boolean a_water_potion(Item item){
            return (water(item) || unholy_water(item) || holy_water(item));
        }

        public boolean matches(Item item){
            return  (!a_clear_appearance()       && !a_water_potion(item)) ||
                    (clear_appearance()          && a_water_potion(item)) ||
                    (cursed_clear_appearance()   && unholy_water(item)) ||
                    (uncursed_clear_appearance() && holy_water(item));
        }
    }

    public static class QuaffFilter implements Items.filter {
        String effect;

        public QuaffFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
        return ((Potion)item).quaff_effects.contains(effect);
    }
    };

    public static class ThrowFilter implements Items.filter {
        String effect;

        public ThrowFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
        return ((Potion)item).throw_effects.contains(effect);
    }
    };
}
