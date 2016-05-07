package org.morsi.android.nethack.redux.items;

import android.app.Activity;
import org.morsi.android.nethack.redux.R;
import org.morsi.android.nethack.redux.util.QuickStat;
import org.morsi.android.nethack.redux.util.QuickStatCategory;

import java.util.ArrayList;

public class Items extends ArrayList<Item> {
    public interface filter{
        boolean matches(Item item);
    }

    public Items filter(filter f){
        Items result = new Items();
        for(Item i : this)
            if(f.matches(i))
                result.add(i);
        return result;
    }

    public ArrayList<String> names(){
        ArrayList<String> names = new ArrayList<String>();
        for(Item i : this)
            names.add(i.name);
        return names;
    }

    public static Items fromXML(Activity activity){
        Items items = new Items();

        for(Item potion : Potion.fromXML(activity.getResources().getXml(R.xml.potions)))
            items.add(potion);

        for(Item gem : Gem.fromXML(activity.getResources().getXml(R.xml.gems)))
            items.add(gem);

        for(Item scroll : Scroll.fromXML(activity.getResources().getXml(R.xml.scrolls)))
            items.add(scroll);

        for(Item armor : Armor.fromXML(activity.getResources().getXml(R.xml.armor)))
            items.add(armor);

        for(Item ring : Ring.fromXML(activity.getResources().getXml(R.xml.rings)))
            items.add(ring);

        for(Item wand : Wand.fromXML(activity.getResources().getXml(R.xml.wands)))
            items.add(wand);

        for(Item corpse : Corpse.fromXML(activity.getResources().getXml(R.xml.corpses)))
            items.add(corpse);

        for(Item tool : Tool.fromXML(activity.getResources().getXml(R.xml.tools)))
            items.add(tool);

        for(Item amulet : Amulet.fromXML(activity.getResources().getXml(R.xml.amulets)))
            items.add(amulet);

        for(Item spellbook : SpellBook.fromXML(activity.getResources().getXml(R.xml.spellbooks)))
            items.add(spellbook);

        return items;
    }

    public Items potions(){
        return filter(new Item.ItemTypeFilter(Potion.type()));
    }

    public Items gems(){
        return filter(new Item.ItemTypeFilter(Gem.type()));
    }

    public Items scrolls(){
        return filter(new Item.ItemTypeFilter(Scroll.type()));
    }

    public Items armor(){
        return filter(new Item.ItemTypeFilter(Armor.type()));
    }

    public Items rings(){
        return filter(new Item.ItemTypeFilter(Ring.type()));
    }

    public Items tools(){
        return filter(new Item.ItemTypeFilter(Tool.type()));
    }

    public Items wands(){
        return filter(new Item.ItemTypeFilter(Wand.type()));
    }

    public Items corpses(){
        return filter(new Item.ItemTypeFilter(Corpse.type()));
    }

    public ArrayList<QuickStatCategory> toStats(){
        QuickStatCategory potions_category = Potion.toQuickStatsCategory();
        QuickStatCategory gems_category    = Gem.toQuickStatsCategory();
        QuickStatCategory scrolls_category = Scroll.toQuickStatsCategory();
        QuickStatCategory armor_category   = Armor.toQuickStatsCategory();
        QuickStatCategory ring_category    = Ring.toQuickStatsCategory();
        QuickStatCategory tools_category   = Tool.toQuickStatsCategory();
        QuickStatCategory wand_category    = Wand.toQuickStatsCategory();
        QuickStatCategory corpse_category  = Corpse.toQuickStatsCategory();

        for(Item i : potions())
            potions_category.stats.add(i.toQuickStat());
        for(Item i : gems())
            gems_category.stats.add(i.toQuickStat());
        for(Item i : scrolls())
            scrolls_category.stats.add(i.toQuickStat());
        for(Item i : armor())
            armor_category.stats.add(i.toQuickStat());
        for(Item i : rings())
            ring_category.stats.add(i.toQuickStat());
        for(Item i : tools())
            tools_category.stats.add(i.toQuickStat());
        for(Item i : wands())
            wand_category.stats.add(i.toQuickStat());
        for(Item i : corpses())
            corpse_category.stats.add(i.toQuickStat());

        ArrayList<QuickStatCategory> categories = new ArrayList<QuickStatCategory>();
        categories.add(potions_category);
        categories.add(gems_category);
        categories.add(scrolls_category);
        categories.add(armor_category);
        categories.add(ring_category);
        categories.add(tools_category);
        categories.add(wand_category);
        categories.add(corpse_category);
        return categories;
    }
}
