package io.swagger.models.composition;

import java.net.MalformedURLException;
import java.net.URL;

public class ModelWithUrlProperty extends AbstractModelWithApiModel {

    private final URL url;

    public ModelWithUrlProperty(String type, String url) {
        super(type);
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public URL getUrl() {
        return url;
    }
}
