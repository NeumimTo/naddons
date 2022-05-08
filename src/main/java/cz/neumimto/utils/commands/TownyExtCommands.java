package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.palmergames.bukkit.towny.object.Nation;
import cz.neumimto.utils.managers.TownyExtensionsManager;
import cz.neumimto.utils.managers.TownyTeamManager;
import cz.neumimto.utils.model.TownyTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
@CommandAlias("townyext")
@CommandPermission("nutils.townyext")
public class TownyExtCommands extends BaseCommand {

    @Inject
    private TownyExtensionsManager manager;

    @Subcommand("set-national-world")
    @CommandCompletion("@nation @nothing")
    public void set(OnlinePlayer onlinePlayer, Nation nation) {
        manager.setNationalWorld(onlinePlayer.getPlayer().getWorld().getName(), nation);
    }

}
