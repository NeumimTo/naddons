package cz.neumimto.utils.model;

import org.bukkit.Material;

import java.util.Locale;

public class ItemString {
    public int amount = 1;
    public Material type;
    public Integer modelTag;

    public static ItemString of(String str) {
        ItemString itemString = new ItemString();
        String[] split = str.split(";");
        if (split.length >= 1) {
            itemString.type = Material.valueOf(split[0].toUpperCase(Locale.ROOT));
            if (split.length >= 2) {
                itemString.amount = Integer.parseInt(split[1]);
                if (split.length >= 3) {
                    itemString.modelTag = Integer.parseInt(split[2]);
                }
            }
        }
        return itemString;
    }
}
