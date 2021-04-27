package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.Conversion;
import com.electronwill.nightconfig.core.conversion.Converter;
import com.electronwill.nightconfig.core.conversion.Path;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class CivsTeams {

    @Path("enabled")
    public Boolean enabled = true;

    @Path("teams")
    @Conversion(CivsTeams.NationColorConverter.class)
    public Map<String, TextColor> teams = new HashMap<>();

    @Path("display")
    public Type display = Type.PREFIX;

    public enum Type {
        PREFIX, NAME_TAG, SUFFIX
    }

    public static class NationColorConverter implements Converter<Map<String, TextColor>, Config> {

        @Override
        public Map convertToField(Config value) {
            Map<String, TextColor> map = new HashMap<>();
            if (value != null) {
                for (Map.Entry<String, Object> entry : value.valueMap().entrySet()) {
                    map.put(entry.getKey(), TextColor.fromHexString(entry.getValue().toString()));
                }
            }
            return map;
        }

        @Override
        public Config convertFromField(Map<String, TextColor> value) {
            if (value == null) {
                value = new HashMap<>();
            }
            Config config = Config.inMemory();
            for (Map.Entry<String, TextColor> e : value.entrySet()) {
                config.add(e.getKey(), e.getValue().asHexString());
            }
            return config;
        }
    }

    @Override
    public String toString() {
        return "CivsTeams{" +
                "enabled=" + enabled +
                ", teams=" + teams +
                ", display=" + display +
                '}';
    }
}
