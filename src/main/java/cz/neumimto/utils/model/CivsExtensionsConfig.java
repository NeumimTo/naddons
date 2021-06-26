package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.conversion.Path;

import java.util.List;

public class CivsExtensionsConfig {

    @Path("block-border-msg")
    public List<String> blockBorderMsg;

    @Override
    public String toString() {
        return "CivsExtensionsConfig{" +
                "blockBorderMsg=" + blockBorderMsg +
                '}';
    }
}
