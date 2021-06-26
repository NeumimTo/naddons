package cz.neumimto.utils.listeners;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import cz.neumimto.utils.Utils;
import cz.neumimto.utils.managers.CompassManager;
import cz.neumimto.utils.model.MapDescriptor;
import cz.neumimto.utils.model.PlayerCompassConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CompassListener implements Listener {

    @Inject
    private CompassManager compassManager;

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null
                || !compassManager.isEnabled() || !player.hasPermission("nutil.compass")) {
            return;
        }

        CompletableFuture<PlayerCompassConfig> playerData = compassManager.getPlayerData(player.getUniqueId());
        if (event.getItem().getType() == Material.COMPASS) {
            playerData.thenAccept(data -> {
                if (data.savedLocations == null || data.savedLocations.isEmpty()) {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "You have no waypoints saved, click with an empty map on a banner to set one!");
                    return;
                }
                ChestGui gui = new ChestGui(6, "Waypoints");

                PaginatedPane pane = new PaginatedPane(0, 0, 9, 6);

                StaticPane pageOne = new StaticPane(0, 0, 9, 6);

                String worldName = player.getWorld().getName();
                int x = 0;
                int y = 0;
                for (MapDescriptor savedLocation : data.savedLocations) {
                     boolean currentWorld = savedLocation.worldName.equalsIgnoreCase(worldName);
                     if (currentWorld) {
                         ItemStack itemStack = new ItemStack(Material.MAP);
                         ItemMeta itemMeta = itemStack.getItemMeta();
                         itemMeta.setDisplayName(org.bukkit.ChatColor.GREEN + savedLocation.mapName);
                         List<String> lore = new ArrayList<>();
                         lore.add("");
                         lore.add(org.bukkit.ChatColor.YELLOW + savedLocation.worldName);
                         lore.add(org.bukkit.ChatColor.GREEN.toString() + savedLocation.location.getX() + " " + savedLocation.location.getZ());
                         itemMeta.setLore(lore);
                         itemStack.setItemMeta(itemMeta);
                         pageOne.addItem(new GuiItem(itemStack, e -> {
                             if (e.getClick() != ClickType.SHIFT_RIGHT) {
                                 event.getPlayer().sendMessage(ChatColor.GREEN + "Your compass now points to " + ChatColor.YELLOW + savedLocation.mapName + " " + ChatColor.GREEN + "!");
                                 event.getPlayer().setCompassTarget(new Location(event.getPlayer().getWorld(), savedLocation.location.getX(), 0, savedLocation.location.getZ()));
                             } else {
                                 compassManager.removeWaypoint(data, player.getUniqueId(), savedLocation.mapName);
                                 event.getPlayer().sendMessage(ChatColor.RED + "You have removed " + ChatColor.YELLOW + savedLocation.mapName + " " + ChatColor.RED + "waypoint!");
                             }
                             e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                             e.setCancelled(true);
                         }), x, y);
                     } else {
                         ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS);
                         ItemMeta itemMeta = itemStack.getItemMeta();
                         itemMeta.setDisplayName(org.bukkit.ChatColor.RED + savedLocation.mapName);
                         List<String> lore = new ArrayList<>();
                         lore.add("");
                         lore.add(org.bukkit.ChatColor.RED + savedLocation.worldName);
                         lore.add(org.bukkit.ChatColor.RED.toString() + savedLocation.location.getX() + " " + savedLocation.location.getZ());
                         itemMeta.setLore(lore);
                         itemStack.setItemMeta(itemMeta);
                         pageOne.addItem(new GuiItem(itemStack, e -> {
                             e.getWhoClicked().sendMessage(ChatColor.RED + "You cannot activate compass waypoint that is from another world!");
                             e.setCancelled(true);
                         }), x, y);
                     }
                     x++;
                     if (x % 9 == 0) {
                         y++;
                         x =0;
                     }
                }



                pane.addPane(0, pageOne);

                OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
                background.addItem(new GuiItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), e->e.setCancelled(true)));
                background.setRepeat(true);
                gui.addPane(background);

                gui.addPane(pane);

                //StaticPane back = new StaticPane(2, 6, 1, 1);
                //StaticPane forward = new StaticPane(6, 6, 1, 1);

                //back.addItem(new GuiItem(new ItemStack(Material.ARROW), e -> {
                //    pane.setPage(pane.getPage() - 1);
//
                //    if (pane.getPage() == 0) {
                //        back.setVisible(false);
                //    }
//
                //    forward.setVisible(true);
                //    gui.update();
                //}), 0, 0);
//
                //back.setVisible(false);
//
                //forward.addItem(new GuiItem(new ItemStack(Material.ARROW), e -> {
                //    pane.setPage(pane.getPage() + 1);
//
                //    if (pane.getPage() == pane.getPages() - 1) {
                //        forward.setVisible(false);
                //    }
//
                //    back.setVisible(true);
                //    gui.update();
                //}), 0, 0);

                //gui.addPane(back);
                //gui.addPane(forward);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getInstance(), () -> gui.show(player));
            });
        }  else if (event.getItem().getType() == Material.MAP && event.getClickedBlock() != null && event.getClickedBlock().getType().name().contains("BANNER")) {
            BlockBreakEvent event1 = new BlockBreakEvent(event.getClickedBlock(), event.getPlayer());
            Bukkit.getPluginManager().callEvent(event1);
            if (event1.isCancelled()) {
                return;
            }
            ItemMeta itemMeta = event.getItem().getItemMeta();
            String displayName = null;
            if (itemMeta.hasDisplayName()) {
                displayName = itemMeta.getDisplayName();
            } else {
                displayName = "Unnamed waypoint";
            }
            Location location = event.getClickedBlock().getLocation();
            MapDescriptor mapDescriptor = compassManager.createMapDescriptor(displayName, location);

            playerData.thenAcceptAsync(data -> {
                compassManager.addMapDescriptor(data, player.getUniqueId(), mapDescriptor);
            });
            event.setCancelled(true);

            if (event.getHand() == EquipmentSlot.OFF_HAND) {
                player.getInventory().setItemInOffHand(null);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
            player.sendMessage(ChatColor.GREEN + " waypoint has been set. Right click with your compass to activate it");
            if (displayName.equalsIgnoreCase("Unnamed waypoint")) {
                player.sendMessage(ChatColor.GREEN + "Rename the map item in anvil before interacting with banner to set waypoint`s name");
            }
        }
    }

}
