package cz.neumimto.utils.managers;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import cz.neumimto.utils.model.TownyTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.*;

@Singleton
public class TownyTeamManager implements FileStoreManager {

    private Path configPath;
    private TownyTeams ct;
    private Scoreboard civsboard;
    private Map<UUID, Team> teamByPlayer = new HashMap<>();

    public TextColor getColor(String nationName) {
        return ct.teams.get(nationName);
    }

    public Map<String, TextColor> getColors() {
        return ct.teams;
    }

    private Nation getNation(Player player) {
        Resident resident = TownyAPI.getInstance().getResident(player);
        return TownyAPI.getInstance().getResidentNationOrNull(resident);
    }

    private TextColor getPlayerColor(Player player) {
        Nation nation = getNation(player);
        if (nation != null) {
            return getColor(nation.getName());
        } else {
            return null;
        }
    }

    public void remove(Nation nation) {
        ct.teams.remove(nation.getName());
        save();
    }

    public void addColor(Nation nation, TextColor fromHexString) {
        ct.teams.put(nation.getName(), fromHexString);
        save();
    }

    public void updateTeam(Player player) {
        Nation nation = getNation(player);
        if (nation == null) {
            return;
        }
        TextColor textColor = ct.teams.get(nation.getName());
        if (textColor == null) {
            return;
        }
        Team team1 = teamByPlayer.get(player.getUniqueId());
        if (team1 != null) {
            team1.unregister();
        }

        Team team = civsboard.registerNewTeam(player.getName());
        switch (ct.display) {
            case PREFIX:
                team.prefix(Component.text(nation.getName()).color(textColor));
                break;
            case NAME_TAG:
                team.color(NamedTextColor.nearestTo(textColor));
                break;
            case SUFFIX:
                team.suffix(Component.text(nation.getName()).color(textColor));
        }

        team.addEntry(player.getName());

        teamByPlayer.put(player.getUniqueId(), team);
    }

    public void deleteTeam(Player player) {
        Team remove = teamByPlayer.remove(player.getUniqueId());
        if (remove != null) {
            remove.unregister();
        }
    }

    private void updateAllPlayers() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            updateTeam(onlinePlayer);
        }
    }

    public void updateDisplayType(TownyTeams.Type type) {
        ct.display = type;
        save();
        updateAllPlayers();
    }

    public void enable() {
        ct.enabled = true;
        save();
    }

    public void disable() {
        ct.enabled = false;
        save();

        for (Map.Entry<UUID, Team> entry : teamByPlayer.entrySet()) {
            entry.getValue().unregister();
        }
    }

    public boolean teamsEnabled() {
        return Boolean.TRUE.equals(ct.enabled);
    }

    @Override
    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public Path getConfigPath() {
        return configPath;
    }

    @Override
    public Object getDomain() {
        return ct;
    }

    @Override
    public void initializeDomain() {
        ct = new TownyTeams();
        civsboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }
}
