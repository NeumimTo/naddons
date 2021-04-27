package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.Conversion;
import com.electronwill.nightconfig.core.conversion.Converter;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.conversion.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ArmorstandCommandsData {

    @Path("commands")
    @Conversion(MapUUIDStringListConverter.class)
    public Map<UUID, List<String>> commands = new HashMap<>();

    @Override
    public String toString() {
        return "ArmorstandCommandsData{" +
                "commands=" + commands +
                '}';
    }

    public static class MapUUIDStringListConverter implements Converter<Map<UUID, List<String>>, Config> {

       @Override
       public Map convertToField(Config value) {
           Map<UUID, List<String>> map = new HashMap();
           if (value != null) {
               for (Map.Entry<String, Object> entry : value.valueMap().entrySet()) {
                   UUID key = UUID.fromString(entry.getKey());
                   List<String> value1 = (List<String>) entry.getValue();
                   map.put(key, value1);
               }
           }
           return map;
       }

       @Override
       public Config convertFromField(Map<UUID, List<String>> value) {
           if (value == null) {
               value = new HashMap<>();
           }
           Config config = Config.inMemory();
           for (Map.Entry<UUID, List<String>> e : value.entrySet()) {
               config.add(e.getKey().toString(), e.getValue());
           }
           return config;
       }

   }

}
