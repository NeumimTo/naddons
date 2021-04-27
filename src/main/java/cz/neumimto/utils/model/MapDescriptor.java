package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.conversion.Conversion;
import com.electronwill.nightconfig.core.conversion.Converter;
import com.electronwill.nightconfig.core.conversion.Path;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class MapDescriptor implements Comparable<MapDescriptor> {

    @Path("name")
    public String mapName;

    @Path("world")
    public String worldName;

    @Path("location")
    @Conversion(LocationConverter.class)
    public Location location;

    @Override
    public int compareTo(@NotNull MapDescriptor o) {
        return mapName.compareTo(o.mapName);
    }

    private static class LocationConverter implements Converter<Location, String> {

        @Override
        public Location convertToField(String value) {
            String[] split = value.split(";");
            return new Location(null, Double.parseDouble(split[0]), 0, Double.parseDouble(split[1]));
        }

        @Override
        public String convertFromField(Location value) {
            return value.getX() + ";" + value.getZ();
        }
    }
}
