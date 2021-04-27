package cz.neumimto.utils.model;

import com.electronwill.nightconfig.core.conversion.Path;

public class ResourcePack {

    @Path("url")
    public String url;

    @Path("hash")
    public String hash;

    @Path("onError")
    public String error;

    @Override
    public String toString() {
        return "ResourcePack{" +
                "url='" + url + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
