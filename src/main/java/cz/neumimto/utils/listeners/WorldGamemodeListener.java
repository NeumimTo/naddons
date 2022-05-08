package cz.neumimto.utils.listeners;


import cz.neumimto.utils.managers.PerWorldGamemodeManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldGamemodeListener implements Listener {

    @Inject
    private PerWorldGamemodeManager manager;

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        changeGamemode(event.getPlayer());
    }

    private void changeGamemode(Player event) {
        Player player = event;
        if (player.hasPermission("perworldgamemode.bypass")) {
            return;
        }
        World world = player.getWorld();
        manager.getGamemodeForWorld(world.getName()).ifPresent(g->{
            player.setGameMode(g);
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        changeGamemode(event.getPlayer());
    }
}
