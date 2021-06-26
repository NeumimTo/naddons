package cz.neumimto.utils.managers;

import cz.neumimto.utils.model.CivsExtensionsConfig;
import org.redcastlemedia.multitallented.civs.towns.Town;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;

@Singleton
public class CivsExtensionsManager implements FileStoreManager {

    private Path configPath;
    private CivsExtensionsConfig extensionsConfig;

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
        extensionsConfig = new CivsExtensionsConfig();
    }

    @Override
    public void postLoad() {
        if (extensionsConfig.blockBorderMsg == null) {
            extensionsConfig.blockBorderMsg = new ArrayList<>();
        }
    }
}
