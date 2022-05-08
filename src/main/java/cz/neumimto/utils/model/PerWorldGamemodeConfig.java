package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.Conversion;
import com.electronwill.nightconfig.core.conversion.Converter;
import com.electronwill.nightconfig.core.conversion.Path;
import org.bukkit.GameMode;

import java.util.HashMap;
import java.util.Map;

public class PerWorldGamemodeConfig {

    @Path("worlds")
    @Conversion(MapStringString.class)
    public Map<String, GameMode> worldGamemode = new HashMap<>();


    public static class MapStringString implements Converter<Map<String, GameMode>, Config> {

        @Override
        public Map convertToField(Config value) {
            Map<String, GameMode> map = new HashMap();
            if (value != null) {
                for (Map.Entry<String, Object> entry : value.valueMap().entrySet()) {
                    String key = entry.getKey();
                    GameMode gameMode = GameMode.valueOf(entry.getValue().toString());

                    map.put(key, gameMode);
                }
            }
            return map;
        }

        @Override
        public Config convertFromField(Map<String, GameMode> value) {
            if (value == null) {
                value = new HashMap<>();
            }
            Config config = Config.inMemory();
            for (Map.Entry<String, GameMode> e : value.entrySet()) {
                config.add(e.getKey(), e.getValue().name());
            }
            return config;
        }

    }
}
