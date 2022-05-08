package cz.neumimto.utils.managers;

import cz.neumimto.utils.model.CompassConfig;
import cz.neumimto.utils.model.PerWorldGamemodeConfig;
import org.bukkit.GameMode;

import javax.inject.Singleton;
import java.io.File;
import java.nio.file.FileStore;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

@Singleton
public class PerWorldGamemodeManager implements FileStoreManager {

    private Path configPath;
    private PerWorldGamemodeConfig perWorldGamemodeConfig;
    private Path playerDataDir;

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
        return perWorldGamemodeConfig;
    }

    @Override
    public void initializeDomain() {
        playerDataDir = configPath.getParent().resolve("PerWorldGamemode/");
        File file = playerDataDir.toFile();
        if (!file.exists()) {
            file.mkdir();
        }
        perWorldGamemodeConfig = new PerWorldGamemodeConfig();
    }

    public Optional<GameMode> getGamemodeForWorld(String name) {
        if (perWorldGamemodeConfig.worldGamemode == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(perWorldGamemodeConfig.worldGamemode.get(name));
    }

    public void setGamemodeForWorld(String world, GameMode gameMode) {
        if (perWorldGamemodeConfig.worldGamemode == null) {
            perWorldGamemodeConfig.worldGamemode = new HashMap<>();
        }
        perWorldGamemodeConfig.worldGamemode.put(world, gameMode);
        save();
    }
}
