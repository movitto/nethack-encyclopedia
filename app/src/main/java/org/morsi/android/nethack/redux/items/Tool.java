package org.morsi.android.nethack.redux.items;

import android.content.res.XmlResourceParser;

import org.morsi.android.nethack.redux.util.QuickStatCategory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Tool extends Item {
    String use;

    public static String type(){ return "Tool"; }

    public static String quickStatsCategoryName(){ return "Tools"; }

    public static ArrayList<String> columnNames(){
        ArrayList<String> columns = new ArrayList<String>();
        columns.add("Name");
        columns.add("Cost");
        columns.add("Weight");
        columns.add("Use");
        return columns;
    }

    public static ArrayList<Double> columnWeights(){
        ArrayList<Double> weights = new ArrayList<Double>();
        weights.add(0.32);
        weights.add(0.13);
        weights.add(0.13);
        weights.add(0.42);
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
        columns.add(use);
        return columns;
    }

    public static ArrayList<Tool> fromXML(XmlResourceParser xpp){
        ArrayList<Tool> tools = new ArrayList<Tool>();

        try {
            int eventType = xpp.next();
            String element_name = "";
            Tool current_tool = null;
            ArrayList<String> current_row = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    element_name = xpp.getName();
                    if(element_name.equals("tool"))
                        current_tool = new Tool();

                } else if (eventType == XmlPullParser.TEXT) {
                    if(element_name.equals("name"))
                        current_tool.name = xpp.getText();
                    else if(element_name.equals("cost"))
                        current_tool.cost = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("weight"))
                        current_tool.weight = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("probability"))
                        current_tool.probability_str = xpp.getText();
                    else if(element_name.equals("appearance"))
                        current_tool.appearance = xpp.getText();
                    else if(element_name.equals("buy"))
                        current_tool.buy_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("sell"))
                        current_tool.sell_price = Integer.parseInt(xpp.getText());
                    else if(element_name.equals("ues"))
                        current_tool.use = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equals("tool")) {
                    tools.add(current_tool);
                }

                eventType = xpp.next();
            }

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        return tools;
    }
}
