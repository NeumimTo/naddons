package cz.neumimto.utils.managers;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import cz.neumimto.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileStoreManager {

    void setConfigPath(Path configPath);

    Path getConfigPath();

    Object getDomain();

    default void load() {
        if (!Files.exists(getConfigPath())) {
            try {
                Files.createFile(getConfigPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        initializeDomain();
        try (FileConfig fileConfig = FileConfig.of(getConfigPath())){
            fileConfig.load();
            new ObjectConverter().toObject(fileConfig, getDomain());
        }
        Utils.logger.info("Loaded data from " + getConfigPath().toString() + " " + getDomain().toString());
        postLoad();
    }

    default void save()  {
        try (FileConfig fileConfig = FileConfig.of(getConfigPath())){
            new ObjectConverter().toConfig(getDomain(), fileConfig);
            fileConfig.save();
        }
        Utils.logger.info("Saved data " + getDomain().toString() + " to " + getConfigPath());
    }

    void initializeDomain();

    default void postLoad() {}
}
