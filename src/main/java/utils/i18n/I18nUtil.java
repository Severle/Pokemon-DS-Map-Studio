package utils.i18n;

import lombok.NonNull;

import java.util.Enumeration;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class I18nUtil {
    public static final String I18N_PREFIX = "lang.";
    public static final String DEFAULT_I18N_NAME = "appDefault";

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(I18N_PREFIX + DEFAULT_I18N_NAME);
    }

    public static ResourceBundle getResourceBundle(String bundleName) {
        return ResourceBundle.getBundle(I18N_PREFIX + bundleName);
    }

    @NonNull
    public static String getString(@NonNull String key) {
        return getResourceBundle().getString(key);
    }

    public static String getStringNullable(String key) {
        try {
            return getString(key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getStringWithDefault(@NonNull String key, String defaultValue) {
        try {
            return getString(key);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    @NonNull
    public static String[] getStringArray(@NonNull String key) {
        return getResourceBundle().getStringArray(key);
    }

    @NonNull
    public static String[] getStringArrayNullable(String key) {
        try {
            return getStringArray(key);
        } catch (NullPointerException e) {
            return new String[0];
        }
    }

    public static Enumeration<String> getKeys() {
        return getResourceBundle().getKeys();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(@NonNull String key) {
        try {
            return (T) getResourceBundle().getObject(key);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T getValue(@NonNull String key, @NonNull T defaultValue) {
        var value = getValue(key);
        if (value == null)
            return defaultValue;

        try {
            return (T) value;
        } catch (ClassCastException | NullPointerException e) {
            return defaultValue;
        }
    }

    @NonNull
    public static String getString(@NonNull String bundleName, @NonNull String key) {
        return getResourceBundle(bundleName).getString(key);
    }

    public static String getStringNullable(@NonNull String bundleName, String key) {
        try {
            return getString(bundleName, key);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static String getStringWithDefault(@NonNull String bundleName, @NonNull String key, String defaultValue) {
        try {
            return getString(bundleName, key);
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    @NonNull
    public static String[] getStringArray(@NonNull String bundleName, @NonNull String key) {
        return getResourceBundle(bundleName).getStringArray(key);
    }

    @NonNull
    public static String[] getStringArrayNullable(@NonNull String bundleName, String key) {
        try {
            return getStringArray(bundleName, key);
        } catch (NullPointerException e) {
            return new String[0];
        }
    }

    public static Enumeration<String> getKeys(@NonNull String bundleName) {
        return getResourceBundle(bundleName).getKeys();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(@NonNull String bundleName, @NonNull String key) {
        try {
            return (T) getResourceBundle(bundleName).getObject(key);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T getValue(@NonNull String bundleName, @NonNull String key, @NonNull T defaultValue) {
        var value = getValue(bundleName, key);
        if (value == null)
            return defaultValue;

        try {
            return (T) value;
        } catch (ClassCastException | NullPointerException e) {
            return defaultValue;
        }
    }
}
