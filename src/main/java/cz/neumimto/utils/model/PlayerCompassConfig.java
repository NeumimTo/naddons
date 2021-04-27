package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.conversion.Path;

import java.util.TreeSet;

public class PlayerCompassConfig {

    @Path("waypoints")
    public TreeSet<MapDescriptor> savedLocations;

}
