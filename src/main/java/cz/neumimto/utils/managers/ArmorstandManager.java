package cz.neumimto.utils.managers;

import cz.neumimto.utils.Utils;
import cz.neumimto.utils.model.ArmorstandCommandsData;

import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Singleton
public class ArmorstandManager implements FileStoreManager {

    private Path configPath;

    private ArmorstandCommandsData ac;

    public void addCommand(UUID id, String command) {
        if (ac.commands.containsKey(id)) {
            ac.commands.get(id).add(command);
        } else {
            List<String> list = new ArrayList<>();
            list.add(command);
            ac.commands.put(id, list);
        }
        Utils.logger.info("Added command to armorstand " + id + " command " + command);
        save();
    }

    public void removeCommands(UUID id) {
        ac.commands.remove(id);
        save();
        Utils.logger.info("Removed armor stand action from " + id);
    }

    public List<String> listCommands(UUID id) {
        if (ac.commands.containsKey(id)) {
            return ac.commands.get(id);
        }
        return Collections.emptyList();
    }

    public void removeCommandById(UUID id, int idx) {
        if (ac.commands.containsKey(id)) {
            List<String> strings = ac.commands.get(id);
            if (strings.size() >= idx){
                strings.remove(idx);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    public List<String> getCommands(UUID id) {
        return ac.commands.get(id);
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
        return ac;
    }

    @Override
    public void initializeDomain() {
        ac = new ArmorstandCommandsData();
    }

}
