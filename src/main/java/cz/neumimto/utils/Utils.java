package cz.neumimto.utils;

import co.aikar.commands.*;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyObject;
import cz.neumimto.utils.commands.*;
import cz.neumimto.utils.listeners.*;
import cz.neumimto.utils.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class Utils extends JavaPlugin {

    public static Logger logger = null;
    private static Utils inst;

    private static Injector injector;
    private static ExecutorService asyncExecutors;

    public static Utils getInstance() {
        return inst;
    }

    @Override
    public void onEnable() {
        asyncExecutors = Executors.newFixedThreadPool(3);

        inst = this;
        logger = getLogger();
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                super.configure();
            }
        });

        try {
            Files.createDirectories(getDataFolder().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        initManagers(new HashMap<Class<? extends FileStoreManager>, String>() {{
            put(ArmorstandManager.class, "armorstandcommands.conf");
            put(TownyTeamManager.class, "civsteams.conf");
            put(CompassManager.class, "compass.conf");
            put(TownyExtensionsManager.class, "civsextensions.conf");
            put(PerWorldGamemodeManager.class, "perworldgamemode.conf");
        }});

        registerCommands(Arrays.asList(
                ArmorStandCommands.class,
                TownyTeamsCommands.class,
                HudModCommands.class,
                CompassCommands.class,
                PerWorldGamemodeCommands.class));

        registerListeners(Arrays.asList(
                ArmorstandListener.class,
                TownyTeamListener.class,
                CompassListener.class,
                TownyExtensionListener.class,
                WorldGamemodeListener.class));

        getServer().getMessenger().registerOutgoingPluginChannel(this, HudModListener.CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel( this, HudModListener.CHANNEL, injector.getInstance(HudModListener.class));
    }

    private void initManagers(Map<Class<? extends FileStoreManager>, String> map) {
        for (Map.Entry<Class<? extends FileStoreManager>, String> entry : map.entrySet()) {
            FileStoreManager manager = injector.getInstance(entry.getKey());
            manager.setConfigPath(Paths.get(getDataFolder().getPath(), entry.getValue()));
            manager.load();
        }
    }

    private void registerCommands(List<Class<? extends BaseCommand>> cmds) {
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);

        paperCommandManager.getCommandCompletions().registerAsyncCompletion("nation", c-> TownyUniverse.getInstance().getNations().stream().map(TownyObject::getName).collect(Collectors.toList()));
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("Town", c-> TownyUniverse.getInstance().getTowns().stream().map(TownyObject::getName).collect(Collectors.toList()));
        paperCommandManager.getCommandContexts().registerContext(Nation.class, c -> {
            String lookup = c.popFirstArg();
            Nation nation = TownyAPI.getInstance().getNation(lookup);
            boolean allowMissing = c.isOptional();

            if (nation == null && !allowMissing) {
                throw new InvalidCommandArgument(true);
            }
            return nation;
        });
        paperCommandManager.getCommandContexts().registerContext(Town.class, c -> {
            String lookup = c.popFirstArg();
            Town town = TownyAPI.getInstance().getTown(lookup);
            boolean allowMissing = c.isOptional();

            if (town == null && !allowMissing) {
                throw new InvalidCommandArgument(true);
            }
            return town;
        });
        for (Class<? extends BaseCommand> cmd : cmds) {
            paperCommandManager.registerCommand(injector.getInstance(cmd));
        }
    }

    private void registerListeners(List<Class<? extends Listener>> list) {
        for (Class<? extends Listener> aClass : list) {
            Bukkit.getServer().getPluginManager().registerEvents(injector.getInstance(aClass), this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Executor getAsycExecServ() {
        return asyncExecutors;
    }
}
