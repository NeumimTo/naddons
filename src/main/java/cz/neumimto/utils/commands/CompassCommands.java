package cz.neumimto.utils.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import cz.neumimto.utils.managers.CompassManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@CommandAlias("ncompas")
@CommandPermission("nutils.ncompass-setup")
public class CompassCommands extends BaseCommand {

    @Inject
    private CompassManager compassManager;

    @CommandAlias("enable")
    public void enable() {
        compassManager.enable();
    }

    @CommandAlias("disable")
    public void disable() {
        compassManager.disable();
    }

}
