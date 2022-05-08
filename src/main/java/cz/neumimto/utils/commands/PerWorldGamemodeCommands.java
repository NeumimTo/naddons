package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import cz.neumimto.utils.managers.PerWorldGamemodeManager;
import cz.neumimto.utils.model.PerWorldGamemodeConfig;
import org.bukkit.GameMode;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@CommandAlias("pwg")
@CommandPermission("nutils.perworldgamemode")
public class PerWorldGamemodeCommands extends BaseCommand {

    @Inject
    private PerWorldGamemodeManager manager;

    @Subcommand("get")
    public void get(OnlinePlayer player) {
        String world = player.getPlayer().getWorld().getName();
        Optional<GameMode> gamemodeForWorld = manager.getGamemodeForWorld(world);
        player.player.sendMessage(world + ": " + gamemodeForWorld.orElse(null));
    }

    @Subcommand("set")
    public void get(OnlinePlayer player, GameMode gameMode) {
        String world = player.getPlayer().getWorld().getName();
        manager.setGamemodeForWorld(world, gameMode);
    }

    @Subcommand("reload")
    public void reload() {
        manager.load();
    }
}
