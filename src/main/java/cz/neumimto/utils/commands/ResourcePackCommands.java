package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import cz.neumimto.utils.managers.ResourcepackManager;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@CommandAlias("respack|rp|rpack")
@CommandPermission("nutils.resourcepack")
public class ResourcePackCommands extends BaseCommand {

    @Inject
    private ResourcepackManager resourcepackManager;

    @Subcommand("relaod")
    @Description("Reloads configuration file")
    public void reload() {
        resourcepackManager.load();
    }

    @Subcommand("set")
    @Description("Updates configuration file")
    public void set(String url, String hash) {
        resourcepackManager.update(url, hash);
    }

    @Subcommand("error-msg")
    public void errMsg(CommandSender commandSender, @Optional String msg) {
        if (msg == null) {
            commandSender.sendMessage(resourcepackManager.getResourcePack().error);
            return;
        }
        resourcepackManager.setErrMsg(msg);
    }
}
