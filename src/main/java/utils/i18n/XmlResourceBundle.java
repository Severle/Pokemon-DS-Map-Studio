package utils.i18n;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XmlResourceBundle extends ResourceBundle {
    private final Properties props;

    XmlResourceBundle(InputStream stream) throws IOException {
        props = new Properties();
        props.loadFromXML(stream);
    }

    protected Object handleGetObject(@NonNull String key) {
        return props.getProperty(key);
    }

    @NonNull
    public Enumeration<String> getKeys() {
        Set<String> handleKeys = props.stringPropertyNames();
        return Collections.enumeration(handleKeys);
    }
}