package cz.neumimto.utils.managers;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArmorstandManagerTest {

    @Test
    public static void main(String[]a) {
        ArmorstandManager manager = new ArmorstandManager();
        manager.setConfigPath(Paths.get("/tmp/", System.currentTimeMillis() + ".conf"));
        manager.load();
        manager.addCommand(UUID.randomUUID(), "aaa");
        manager.save();
    }
}