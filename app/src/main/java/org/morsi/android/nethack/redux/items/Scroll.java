package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Scroll extends Item {
    public String read_effect;

    public boolean hasReadEffect(){
        return !read_effect.equals("");
    }

    public ArrayList<String> read_effects;

    public int ink;

    public static String type(){ return "Scroll"; }

    public String itemType() { return "Scroll"; }

    public static String quickStatsCategoryName(){ return "Scrolls"; }

    public Scroll(){
        read_effects = new ArrayList<String>();
    }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Scroll");
        columns.add("$");
        columns.add("Wgt");
        columns.add("%");
        columns.add("Ink");
        columns.add("Appearance");
        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.35);
        weights.add(0.1);
        weights.add(0.1);
        weights.add(0.1);
        weights.add(0.1);
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
        columns.add(Integer.toString(ink));
        columns.add(appearance);
        return columns;
    }

    public static Scroll extract(String str) {
        String attrs[] = str.split("-");
        Scroll scroll = new Scroll();
        scroll.read_effect = attrs[0];
        return scroll;
    }

    protected ArrayList<String> compactStringList(){
        ArrayList<String> s = super.compactStringList();
        s.add(read_effect);
        return s;
    }

    public static ArrayList<Scroll> fromXML(XmlResourceParser xpp){
        ArrayList<Scroll> scrolls = new ArrayList<Scroll>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Scroll current_scroll = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("scroll"))
                        current_scroll = new Scroll();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_scroll.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_scroll.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_scroll.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("probability"))
                        current_scroll.probability_str = xpp.getText();
                    else if(element_name.equals("appearance"))
                        current_scroll.appearance = xpp.getText();
                    else if(element_name.equals("read"))
                        current_scroll.read_effects.add(xpp.getText());
                    else if(element_name.equals("ink"))
                        current_scroll.ink = Integer.parseInt(xpp.getText());

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("scroll")) {
                    scrolls.add(current_scroll);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return scrolls;
    }

    ///

    public static class AppearanceFilter implements  Items.filter {
        String appearance;

        public AppearanceFilter(String appearance) {
            this.appearance = appearance;
        }

        private boolean stamped_appearance(){
            return appearance.equals("stamped");
        }

        private boolean unlabeled_appearance(){
            return appearance.equals("unlabeled");
        }

        private boolean is_mail(Item item){
            return item.name.equals("mail");
        }

        private boolean is_blank(Item item){
            return item.name.equals("blank paper");
        }

        private boolean item_with_random_appearance(Item item){
            return !is_mail(item) && !is_blank(item);
        }

        public boolean matches(Item item) {
            return  (stamped_appearance()   && is_mail(item))    ||
                    (unlabeled_appearance() && is_blank(item))   ||
                    (!stamped_appearance()  && !unlabeled_appearance() &&
                            !is_mail(item)  && !is_blank(item));

        }
    }

    public static class ReadFilter implements Items.filter {
        String effect;

        public ReadFilter(String effect) { this.effect = effect; }

        public boolean matches(Item item){
            return ((Scroll)item).read_effects.contains(effect);
        }
    };
}
