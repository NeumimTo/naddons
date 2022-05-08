package cz.neumimto.utils.listeners;

import com.palmergames.bukkit.towny.event.NationAddTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.town.TownLeaveEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import cz.neumimto.utils.managers.TownyTeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TownyTeamListener implements Listener {

    @Inject
    private TownyTeamManager manager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!manager.teamsEnabled()) {
            return;
        }
        Player player = event.getPlayer();
        manager.updateTeam(player);
    }

    @EventHandler
    public void onPlayerJoinTown(TownLeaveEvent event) {
        if (!manager.teamsEnabled()) {
            return;
        }
        Player player = event.getResident().getPlayer();
        manager.updateTeam(player);
    }

    @EventHandler
    public void onTownJoinNation(NationAddTownEvent event) {
        if (!manager.teamsEnabled()) {
            return;
        }
        Town town = event.getTown();
        List<Resident> residents = town.getResidents();
        for (Resident resident : residents) {
            Player player = Bukkit.getPlayer(resident.getUUID());
            if (player != null) {
                manager.updateTeam(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (!manager.teamsEnabled()) {
            return;
        }
        manager.deleteTeam(event.getPlayer());
    }

}
