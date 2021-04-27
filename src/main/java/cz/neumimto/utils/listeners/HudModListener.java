package cz.neumimto.utils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class HudModListener implements PluginMessageListener {

    public static final String CHANNEL = "nth:gui";

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!CHANNEL.equalsIgnoreCase(channel)) {
            return;
        }
        System.out.println(new String(message));
    }

}
