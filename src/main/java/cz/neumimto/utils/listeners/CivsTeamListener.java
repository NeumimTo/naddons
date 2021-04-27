package cz.neumimto.utils.listeners;

import cz.neumimto.utils.managers.CivsTeamManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.redcastlemedia.multitallented.civs.events.PlayerAcceptsTownInviteEvent;
import org.redcastlemedia.multitallented.civs.events.TownJoinedNationEvent;
import org.redcastlemedia.multitallented.civs.towns.Town;
import org.redcastlemedia.multitallented.civs.towns.TownManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.UUID;

@Singleton
public class CivsTeamListener implements Listener {

    @Inject
    private CivsTeamManager manager;

    private TownManager townManager;

    public CivsTeamListener() {
        townManager = TownManager.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!manager.teamsEnabled()) {
            return;
        }
        Player player = event.getPlayer();
        manager.updateTeam(player);
    }

    @EventHandler
    public void onPlayerJoinTown(PlayerAcceptsTownInviteEvent event) {
        if (!manager.teamsEnabled()) {
            return;
        }
        Player player = Bukkit.getPlayer(event.getUuid());
        manager.updateTeam(player);
    }

    @EventHandler
    public void onTownJoinNation(TownJoinedNationEvent event) {
        if (!manager.teamsEnabled()) {
            return;
        }
        Town town = event.getTown();
        HashMap<UUID, String> rawPeople = town.getRawPeople();
        for (UUID uuid : rawPeople.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
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
