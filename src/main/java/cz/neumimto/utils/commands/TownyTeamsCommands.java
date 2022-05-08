package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.palmergames.bukkit.towny.object.Nation;
import cz.neumimto.utils.managers.TownyTeamManager;
import cz.neumimto.utils.model.TownyTeams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
@CommandAlias("townyteams")
@CommandPermission("nutils.townyteams")
public class TownyTeamsCommands extends BaseCommand {

    @Inject
    private TownyTeamManager manager;
    
    @Subcommand("list")
    public void list(CommandSender CommandSender) {
        Map<String, TextColor> teams = manager.getColors();
        for (Map.Entry<String, TextColor> entry : teams.entrySet()) {
            TextComponent text = Component.text(">>")
                    .append(Component.text(entry.getKey()).color(entry.getValue()));
            CommandSender.sendMessage(text);
        }
    }

    @Subcommand("set")
    @CommandCompletion("@nation @nothing")
    public void set(CommandSender CommandSender, Nation nation, String hex) {
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }
        manager.addColor(nation, TextColor.fromHexString(hex));
    }

    @Subcommand("remove")
    @CommandCompletion("@nation")
    public void remove(CommandSender CommandSender, Nation nation) {
        manager.remove(nation);
    }

    @Subcommand("enable")
    public void enable(CommandSender commandSender) {
        manager.enable();
        commandSender.sendMessage("TownyTeams enabled");
    }

    @Subcommand("disable")
    public void disable(CommandSender commandSender) {
        manager.disable();
        commandSender.sendMessage("TownyTeams disabled");
    }

    @Subcommand("set-display")
    public void setDisplay(CommandSender commandSender, TownyTeams.Type type) {
        manager.updateDisplayType(type);
        commandSender.sendMessage("Updated");
    }
}
