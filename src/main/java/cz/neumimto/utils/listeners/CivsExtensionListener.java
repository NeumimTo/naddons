package cz.neumimto.utils.listeners;

import cz.neumimto.utils.managers.CivsExtensionsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.redcastlemedia.multitallented.civs.events.PlayerEnterRegionEvent;
import org.redcastlemedia.multitallented.civs.events.PlayerEnterTownEvent;
import org.redcastlemedia.multitallented.civs.events.PlayerExitTownEvent;
import org.redcastlemedia.multitallented.civs.regions.Region;
import org.redcastlemedia.multitallented.civs.towns.Town;
import org.redcastlemedia.multitallented.civs.towns.TownManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CivsExtensionListener implements Listener {

    @Inject
    private CivsExtensionsManager manager;

    @EventHandler
    public void onRegionEnter(PlayerEnterRegionEvent event) {
        Region region = event.getRegion();
        Town townAt = TownManager.getInstance().getTownAt(region.getLocation());
        if (manager.isEnterExitNotificationBlocked(townAt)) {
            event.setNotify(false);
        }
    }

    @EventHandler
    public void onTownEnter(PlayerEnterTownEvent event) {
        if (manager.isEnterExitNotificationBlocked(event.getTown())) {
            event.setNotify(false);
        }
    }

    @EventHandler
    public void onTownExit(PlayerExitTownEvent event) {
        if (manager.isEnterExitNotificationBlocked(event.getTown())) {
            event.setNotify(false);
        }
    }
}
