package cz.neumimto.utils.managers;

import cz.neumimto.utils.model.ResourcePack;

import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton
public class ResourcepackManager implements FileStoreManager {

    private ResourcePack resourcePack;

    private Path configPath;

    public void update(String url, String hash) {
        resourcePack.hash = hash;
        resourcePack.url = url;
        save();
    }

    public void setErrMsg(String errMsg) {
        this.resourcePack.error = errMsg;
        save();
    }

    public ResourcePack getResourcePack() {
        return resourcePack;
    }

    @Override
    public void initializeDomain() {
        resourcePack = new ResourcePack();
    }

    @Override
    public Object getDomain() {
        return resourcePack;
    }

    @Override
    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    @Override
    public Path getConfigPath() {
        return configPath;
    }

}
