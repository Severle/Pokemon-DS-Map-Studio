package utils.i18n;

import java.util.ResourceBundle;
import java.util.spi.ResourceBundleControlProvider;

public class XmlResourceBundleControlProvider implements ResourceBundleControlProvider {
    @Override
    public ResourceBundle.Control getControl(String baseName) {
        return new XmlResourceBundleControl();
    }
}