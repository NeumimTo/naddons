package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import cz.neumimto.utils.Utils;
import cz.neumimto.utils.listeners.HudModListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;

@Singleton
@CommandAlias("hud")
@CommandPermission("nutils.hudmod")
public class HudModCommands extends BaseCommand {

    @Subcommand("sendPacket")
    public void sendPacket(OnlinePlayer player, String msg) {
        player.player.sendPluginMessage(Utils.getInstance(),
                HudModListener.CHANNEL,
                msg.getBytes(StandardCharsets.UTF_8));
    }

    @Subcommand("sendCooldown")
    public void sendCooldown(OnlinePlayer player, long durationInMillis, String icon) {

        durationInMillis += System.currentTimeMillis();
        String msg = "CD;"+durationInMillis+";" + icon;
        player.player.sendPluginMessage(Utils.getInstance(),
                HudModListener.CHANNEL,
                msg.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *     String textureLocation = "gecko/bat.png";
     *     String animationLocation = "gecko/bat.animation.json";
     *     String modelLocation = "gecko/bat.geo.json";
     *     gecko/bat.png;gecko/bat.animation.json;gecko/bat.geo.json
     */
    @Subcommand("setModel")
    public void setModel(CommandSender commandExecutor, String modelData) {
        ArmorStand targettedEntity = getTargettedEntity((Player) commandExecutor);
        byte[] msg = ("SM;" + targettedEntity.getUniqueId() + ";" + modelData).getBytes(StandardCharsets.UTF_8);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendPluginMessage(Utils.getInstance(),
                    HudModListener.CHANNEL, msg);
        }
    }

    private ArmorStand getTargettedEntity(Player executor) {
        Entity targetEntity = executor.getTargetEntity(20);
        if (targetEntity instanceof ArmorStand) {
            return (ArmorStand) targetEntity;
        }
        throw new CommandException("You must look at armor stand");
    }

}
