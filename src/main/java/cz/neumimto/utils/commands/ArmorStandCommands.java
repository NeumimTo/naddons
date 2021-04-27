package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import cz.neumimto.utils.managers.ArmorstandManager;
import org.bukkit.command.CommandException;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@CommandAlias("armorstandcommand|amc")
@CommandPermission("nutils.armorstandcommand")
public class ArmorStandCommands extends BaseCommand {

    @Inject
    private ArmorstandManager manager;

    @Subcommand("add-command")
    @Description("Adds command to the targetted armor stand")
    public void addCommand(Player executor, String command) {
        ArmorStand armorStand = getTargettedEntity(executor);
        manager.addCommand(armorStand.getUniqueId(), command);
    }

    @Subcommand("removeAll")
    @Description("remove all armor stand actions from an entity")
    public void removeCommands(Player executor) {
        ArmorStand armorStand = getTargettedEntity(executor);
        manager.removeCommands(armorStand.getUniqueId());
    }

    @Subcommand("list")
    @Description("remove all armor stand actions from an entity")
    public void listCommands(Player executor) {
        ArmorStand armorStand = getTargettedEntity(executor);
        List<String> list = manager.listCommands(armorStand.getUniqueId());
        for (int i = 0; i < list.size(); i++) {
            executor.sendMessage("[" + i + "] " + list.get(i));
        }
    }

    @Subcommand("remove")
    @Description("remove all armor stand actions from an entity")
    public void removeCommandById(Player executor, int id) {
        ArmorStand armorStand = getTargettedEntity(executor);
        try {
            manager.removeCommandById(armorStand.getUniqueId(), id);
        } catch (IllegalArgumentException e) {
            executor.sendMessage("Unable to remove " + id);
        }
    }

    @Subcommand("reload")
    @Description("reloads armor stand actions from file")
    public void reload() {
        manager.load();
    }

    private ArmorStand getTargettedEntity(Player executor) {
        Entity targetEntity = executor.getTargetEntity(20);
        if (targetEntity instanceof ArmorStand) {
            return (ArmorStand) targetEntity;
        }
        throw new CommandException("You must look at armor stand");
    }
}
