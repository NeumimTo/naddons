package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import cz.neumimto.utils.managers.CivsExtensionsManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.redcastlemedia.multitallented.civs.items.CVItem;
import org.redcastlemedia.multitallented.civs.towns.Town;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@CommandAlias("cvext")
@CommandPermission("civs.admin")
public class CivsExtensionsCommands extends BaseCommand {

    @Inject
    private CivsExtensionsManager  manager;

    @CommandCompletion("@cvitem")
    @Subcommand("give-item")
    public void give(CommandSender commandSender, CVItem item) {
        Player player = (Player) commandSender;
        player.getInventory().setItemInMainHand(item.createItemStack());
    }

    @CommandCompletion("@town")
    @Subcommand("toggle-notification-for-town")
    public void toggleNotification(CommandSender commandSender, Town town) {
        manager.toggleBorderMessage(town);
    }
}
