package cz.neumimto.utils.managers;

import cz.neumimto.utils.model.ItemString;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ItemCommandCostManager {

    public boolean hasSufficientItems(List<ItemString> itemStringList, Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return true;
        }
        for (ItemString itemString : itemStringList) {
            if (itemString.modelTag == null) {
                if (!player.getInventory().contains(itemString.type, itemString.amount)) {
                    return false;
                }
            } else {
                ItemStack itemStack = new ItemStack(itemString.type, itemString.amount);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setCustomModelData(itemString.modelTag);
                itemStack.setItemMeta(itemMeta);
                if (!player.getInventory().contains(itemStack)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void removeItemsFromInventory(List<ItemString> itemStringList, Player player) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        itemStringList.stream().map(itemString -> {
            ItemStack is = new ItemStack(itemString.type, itemString.amount);
            if (itemString.modelTag != null) {
                ItemMeta itemMeta = is.getItemMeta();
                itemMeta.setCustomModelData(itemString.modelTag);
                is.setItemMeta(itemMeta);
            }
            return is;
        }).forEach(a->player.getInventory().removeItem(a));
    }
}
