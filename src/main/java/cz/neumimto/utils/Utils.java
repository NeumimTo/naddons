package cz.neumimto.utils;

import co.aikar.commands.*;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cz.neumimto.utils.commands.*;
import cz.neumimto.utils.listeners.*;
import cz.neumimto.utils.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.redcastlemedia.multitallented.civs.items.CVItem;
import org.redcastlemedia.multitallented.civs.items.CivItem;
import org.redcastlemedia.multitallented.civs.items.ItemManager;
import org.redcastlemedia.multitallented.civs.nations.Nation;
import org.redcastlemedia.multitallented.civs.nations.NationManager;
import org.redcastlemedia.multitallented.civs.towns.Town;
import org.redcastlemedia.multitallented.civs.towns.TownManager;

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
            put(ResourcepackManager.class, "resourcepack.conf");
            put(ArmorstandManager.class, "armorstandcommands.conf");
            put(CivsTeamManager.class, "civsteams.conf");
            put(CompassManager.class, "compass.conf");
            put(CivsExtensionsManager.class, "civsextensions.conf");
        }});

        registerCommands(Arrays.asList(
                ResourcePackCommands.class,
                ArmorStandCommands.class,
                CivsTeamsCommands.class,
                HudModCommands.class,
                CompassCommands.class,
                CivsExtensionsCommands.class));

        registerListeners(Arrays.asList(
                ResourcepackListener.class,
                ArmorstandListener.class,
                CivsTeamListener.class,
                CompassListener.class,
                CivsExtensionListener.class));

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

        paperCommandManager.getCommandCompletions().registerAsyncCompletion("nation", c->{
            NationManager instance = NationManager.getInstance();
            return instance.getAllNations().stream().map(Nation::getName).collect(Collectors.toList());
        });
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("Town", c->{
            TownManager instance = TownManager.getInstance();
            return instance.getTownNames();
        });
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("cvitem", c->{
            ItemManager itemManager = new ItemManager();
            Map<String, CivItem> allItemTypes = itemManager.getAllItemTypes();
            return allItemTypes.entrySet().stream()
                    .filter(a->a.getValue().getItemType() == CivItem.ItemType.TOWN || a.getValue().getItemType() == CivItem.ItemType.REGION)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        });
        paperCommandManager.getCommandContexts().registerContext(CVItem.class, c -> {
            ItemManager itemManager = ItemManager.getInstance();

            String lookup = c.popFirstArg();

            boolean allowMissing = c.isOptional();

            CVItem cvItem = itemManager.getAllItemTypes().get(lookup);
            if (cvItem == null && !allowMissing) {
                throw new InvalidCommandArgument(true);
            }
            return cvItem;
        });
        paperCommandManager.getCommandContexts().registerContext(Nation.class, c -> {
            NationManager instance = NationManager.getInstance();

            String lookup = c.popFirstArg();
            Nation nation = instance.getNation(lookup);
            boolean allowMissing = c.isOptional();

            if (nation == null && !allowMissing) {
                throw new InvalidCommandArgument(true);
            }
            return nation;
        });
        paperCommandManager.getCommandContexts().registerContext(Town.class, c -> {
            TownManager instance = TownManager.getInstance();

            String lookup = c.popFirstArg();
            Town town = instance.getTown(lookup);
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
