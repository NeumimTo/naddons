package cz.neumimto.utils.listeners;

import cz.neumimto.utils.Utils;
import cz.neumimto.utils.managers.ResourcepackManager;
import cz.neumimto.utils.model.ResourcePack;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class ResourcepackListener implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(ResourcepackListener.class);

    @Inject
    private ResourcepackManager resourcepackManager;

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        ResourcePack rp = resourcepackManager.getResourcePack();
        if (rp == null || rp.url == null) {
            return;
        }
        logger.info("Sending resourcepack to {}", event.getPlayer().getName());

        Bukkit.getScheduler().scheduleSyncDelayedTask(Utils.getInstance(),
                () -> event.getPlayer().setResourcePack(rp.url, rp.hash),
                1L);

    }


    @EventHandler
    public void onPlayerRPStatus(PlayerResourcePackStatusEvent event) {
        logger.info("Player {} responded to resource pack request {}", event.getPlayer(), event.getStatus());
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
            event.getPlayer().sendMessage(resourcepackManager.getResourcePack().error);
        }
    }
}
