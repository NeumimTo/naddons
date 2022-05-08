package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.Conversion;
import com.electronwill.nightconfig.core.conversion.Converter;
import com.electronwill.nightconfig.core.conversion.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TownyExtensionsConfig {

    @Path("block-border-msg")
    public List<String> blockBorderMsg;

    @Path("national-worlds")
    @Conversion(MapStringStringConverter.class)
    public Map<String, String> nationalWorlds;


    public static class MapStringStringConverter implements Converter<Map<String, String>, Config> {

        @Override
        public Map convertToField(Config value) {
            Map<String, String> map = new HashMap();
            if (value != null) {
                for (Map.Entry<String, Object> entry : value.valueMap().entrySet()) {
                    String key = entry.getKey();
                    String value1 = entry.getValue().toString();
                    map.put(key, value1);
                }
            }
            return map;
        }

        @Override
        public Config convertFromField(Map<String, String> value) {
            if (value == null) {
                value = new HashMap<>();
            }
            Config config = Config.inMemory();
            for (Map.Entry<String, String> e : value.entrySet()) {
                config.add(e.getKey(), e.getValue());
            }
            return config;
        }

    }


    @Override
    public String toString() {
        return "TownyExtensionsConfig{" +
                "blockBorderMsg=" + blockBorderMsg +
                ", nationalWorlds=" + (nationalWorlds == null ? "null" : nationalWorlds.entrySet().stream().map(a->a.getKey() + ": " +a.getValue()).collect(Collectors.joining(","))) +
                '}';
    }
}
