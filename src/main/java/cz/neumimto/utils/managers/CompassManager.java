package cz.neumimto.utils.managers;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import cz.neumimto.utils.Utils;
import cz.neumimto.utils.model.CompassConfig;
import cz.neumimto.utils.model.PlayerCompassConfig;
import cz.neumimto.utils.model.MapDescriptor;
import org.bukkit.Location;

import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
public class CompassManager implements FileStoreManager {

    private Path configPath;
    private CompassConfig compassConfig;
    private Path playerDataDir;

    private Path getPlayerFile(UUID uuid) {
        return playerDataDir.resolve(uuid.toString() + ".conf");
    }

    public CompletableFuture<PlayerCompassConfig> getPlayerData(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
           PlayerCompassConfig pdata = new PlayerCompassConfig();
           Path path = getPlayerFile(uuid);

           if (!path.toFile().exists()) {
               return pdata;
           }

           try (FileConfig fileConfig = FileConfig.of(path)){
               fileConfig.load();
               new ObjectConverter().toObject(fileConfig, pdata);
           }
           return pdata;
        });
    }

    public boolean isEnabled() {
        return Boolean.TRUE.equals(compassConfig.enabled);
    }

    public void disable() {
        compassConfig.enabled = false;
        save();
    }

    public void enable() {
        compassConfig.enabled = true;
        save();
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
        return compassConfig;
    }

    @Override
    public void initializeDomain() {
        playerDataDir = configPath.getParent().resolve("CompassData/");
        File file = playerDataDir.toFile();
        if (!file.exists()) {
            file.mkdir();
        }
        compassConfig = new CompassConfig();
    }

    public MapDescriptor createMapDescriptor(String displayName, Location location) {
        MapDescriptor mapDescriptor = new MapDescriptor();
        mapDescriptor.mapName = displayName;
        mapDescriptor.location = location;
        mapDescriptor.worldName = location.getWorld().getName();
        return mapDescriptor;
    }

    public void addMapDescriptor(PlayerCompassConfig config, UUID uniqueId, MapDescriptor mapDescriptor) {
        if (config.savedLocations == null) {
            config.savedLocations = new TreeSet<>();
        }
        config.savedLocations.add(mapDescriptor);
        save(config, uniqueId);
    }

    private void save(PlayerCompassConfig config, UUID uuid) {
        try (FileConfig fileConfig = FileConfig.of(getPlayerFile(uuid))){
            new ObjectConverter().toConfig(config, fileConfig);
            fileConfig.save();
        }
        Utils.logger.info("Saved compass data for " + uuid);
    }

    public void removeWaypoint(PlayerCompassConfig config, UUID uniqueId, String mapName) {
        Iterator<MapDescriptor> iterator = config.savedLocations.iterator();
        while (iterator.hasNext()) {
            MapDescriptor next = iterator.next();
            if (next.mapName.equalsIgnoreCase(mapName)) {
                iterator.remove();
                break;
            }
        }
        save(config, uniqueId);
    }
}
