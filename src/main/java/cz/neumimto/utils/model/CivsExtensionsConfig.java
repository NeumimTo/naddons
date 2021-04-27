package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.conversion.Conversion;
import com.electronwill.nightconfig.core.conversion.Path;

import java.util.HashSet;
import java.util.Set;

public class CivsExtensionsConfig {

    @Path("block-border-msg")
    public Set<String> blockBorderMsg = new HashSet<>();

}
