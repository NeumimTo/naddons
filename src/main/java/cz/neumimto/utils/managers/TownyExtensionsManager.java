package cz.neumimto.utils.managers;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import cz.neumimto.utils.model.TownyExtensionsConfig;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class TownyExtensionsManager implements FileStoreManager {

    private Path configPath;
    private TownyExtensionsConfig extensionsConfig;

    public void toggleBorderMessage(Town town) {
        if (extensionsConfig.blockBorderMsg.contains(town.getName())) {
            extensionsConfig.blockBorderMsg.remove(town.getName());
        } else {
            extensionsConfig.blockBorderMsg.add(town.getName());
        }
        save();
    }

    public boolean isEnterExitNotificationBlocked(Town town) {
        return extensionsConfig.blockBorderMsg.contains(town.getName());
    }

    @Override
    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public Path getConfigPath() {
        return configPath;
    }

    @Override
    public Object getDomain() {
        return extensionsConfig;
    }

    @Override
    public void initializeDomain() {
        extensionsConfig = new TownyExtensionsConfig();
    }

    @Override
    public void postLoad() {
        if (extensionsConfig.blockBorderMsg == null) {
            extensionsConfig.blockBorderMsg = new ArrayList<>();
        }
    }

    public void setNationalWorld(String name, Nation nation) {
        if (extensionsConfig.nationalWorlds == null) {
            extensionsConfig.nationalWorlds = new HashMap<>();
        }
        extensionsConfig.nationalWorlds.put(name, nation.getName());
    }

    public Optional<Nation> getNationalWorld(String name) {
        if (extensionsConfig.nationalWorlds == null ||!extensionsConfig.nationalWorlds.containsKey(name)) {
            return Optional.empty();
        }
        String s = extensionsConfig.nationalWorlds.get(name);
        return Optional.ofNullable(TownyAPI.getInstance().getNation(s));
    }
}
