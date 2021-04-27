package cz.neumimto.utils.listeners;

import cz.neumimto.utils.managers.ArmorstandManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArmorstandListener implements Listener {

    private Map<UUID, Long> cooldowns = new HashMap<>();

    @Inject
    private ArmorstandManager manager;

    private boolean hasCooldown(Player player) {
        Long aLong = cooldowns.get(player.getUniqueId());
        return aLong == null ||  aLong < System.currentTimeMillis();
    }

    private void addCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + 2000);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        List<String> commands = manager.getCommands(event.getRightClicked().getUniqueId());
        if (commands != null) {
            if (!hasCooldown(event.getPlayer())) {
                executeCommand(commands, event.getPlayer());
            }
            event.setCancelled(true);
            addCooldown(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerInteractRightClick(PlayerInteractAtEntityEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }
        List<String> commands = manager.getCommands(event.getRightClicked().getUniqueId());
        if (commands != null) {
            if (!hasCooldown(event.getPlayer())) {
                executeCommand(commands, event.getPlayer());
            }
            event.setCancelled(true);
            addCooldown(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDestroy(EntityDeathEvent event) {
        List<String> commands = manager.getCommands(event.getEntity().getUniqueId());
        if (commands != null) {
            manager.removeCommands(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        List<String> commands = manager.getCommands(event.getEntity().getUniqueId());
        if (commands == null) {
            return;
        }
        if (event.getDamager() instanceof Player) {
            Player pl = (Player) event.getDamager();
            if (pl.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            executeCommand(commands, pl);
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDamage(EntityDamageByBlockEvent event) {
        if (manager.getCommands(event.getEntity().getUniqueId()) != null) {
            event.setCancelled(true);
        }
    }

    public void executeCommand(List<String> commands, Player clicked) {
        for (String command : commands) {
            command = command.replaceAll("%player%", clicked.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    
}
