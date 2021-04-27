package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import cz.neumimto.utils.Utils;
import cz.neumimto.utils.listeners.HudModListener;

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
}
