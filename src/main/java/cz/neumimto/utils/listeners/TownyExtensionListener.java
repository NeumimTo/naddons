package cz.neumimto.utils.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.event.PlayerLeaveTownEvent;
import com.palmergames.bukkit.towny.event.nation.NationTownLeaveEvent;
import cz.neumimto.utils.managers.TownyExtensionsManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TownyExtensionListener implements Listener {

    @Inject
    private TownyExtensionsManager manager;

    @EventHandler
    public void onTownEnter(PlayerEnterTownEvent event) {
        if (manager.isEnterExitNotificationBlocked(event.getEnteredtown())) {
            //event.setNotify(false);
        }
    }

    @EventHandler
    public void onTownExit(PlayerLeaveTownEvent event) {
        if (manager.isEnterExitNotificationBlocked(event.getLefttown())) {
            //event.setNotify(false);
        }
    }
    @EventHandler
    public void newTownEvent(NewTownEvent event) {
        String name = event.getTown().getHomeBlockOrNull().getWorld().getName();
        manager.getNationalWorld(name).ifPresent(a->{
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "townyadmin nation " + a.getName() + " add " + event.getTown().getName());
        });
    }

}
