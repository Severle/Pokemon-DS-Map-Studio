package utils;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import fonts.HarmonyOSSansFont;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static utils.LambdaUtils.systemPropertyNotOverwrite;

@Log4j2
@SuppressWarnings("unused")
public class ThemeUtil {
    static {
        initSystemProperties();
        initFlatUIManagerProperties();
        initFonts();
    }

    public static final String KEY = "Theme";
    public static final Map<String, ThemeEntry> THEMES = new HashMap<>();

    public static final ThemeEntry FLAT_LIGHT = register("Flat Light", new FlatLightLaf());
    public static final ThemeEntry FLAT_DARK = register("Flat Dark", new FlatDarkLaf());
    public static final ThemeEntry INTELLIJ = register("Intellij", new FlatIntelliJLaf());
    public static final ThemeEntry DARCULA = register("Darcula", new FlatDarculaLaf());
    public static final ThemeEntry MAC_LIGHT = register("Mac Light", new FlatMacLightLaf());
    public static final ThemeEntry MAC_DARK = register("Mac Dark", new FlatMacDarkLaf());

    public static Vector<ThemeEntry> themeVector() {
        return new Vector<>(THEMES.values());
    }

    public static ThemeEntry defaultTheme() {
        return FLAT_LIGHT;
    }

    public static ThemeEntry getOrDefault(String name) {
        if (THEMES.containsKey(name)) {
            return THEMES.get(name);
        }
        return defaultTheme();
    }

    private static ThemeEntry register(String name, FlatLaf laf) {
        var entry = new ThemeEntry(name, laf);
        THEMES.put(name, entry);
        return entry;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void initSystemProperties() {
        systemPropertyNotOverwrite("flatlaf.animatedLafChange", true);
        systemPropertyNotOverwrite("flatlaf.useRoundedPopupBorder", true);
        systemPropertyNotOverwrite("flatlaf.useSubMenuSafeTriangle", true);
        if (SystemInfo.isMacOS) {
            // enable screen menu bar
            // (moves menu bar from JFrame window to top of screen)
            systemPropertyNotOverwrite("apple.laf.useScreenMenuBar", true);

            // application name used in screen menu bar
            // (in first menu after the "apple" menu)
            systemPropertyNotOverwrite("apple.awt.application.name", "Pokemon DS Map Studio");

            // appearance of window title bars
            // possible values:
            //   - "system": use current macOS appearance (light or dark)
            //   - "NSAppearanceNameAqua": use light appearance
            //   - "NSAppearanceNameDarkAqua": use dark appearance
            // (must be set on main thread and before AWT/Swing is initialized;
            //  setting it on AWT thread does not work)
            systemPropertyNotOverwrite("apple.awt.application.appearance", "system");
        }
        if (SystemInfo.isLinux) {
            // enable custom window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        if (SystemInfo.isJava_9_orLater && System.getProperty("flatlaf.uiScale") != null) {
            systemPropertyNotOverwrite("flatlaf.uiScale", "2x");
        }

        systemPropertyNotOverwrite("awt.useSystemAAFontSettings", "on");
        systemPropertyNotOverwrite("swing.aatext", true);
        systemPropertyNotOverwrite("javax.swing.adjustPopupLocationToFit", true);
        if (SystemInfo.isWindows) {
            log.trace("OS is windows, enable d3d");
            systemPropertyNotOverwrite("sun.java2d.d3d", true);
        } else {
            log.trace("Enable opengl");
            systemPropertyNotOverwrite("sun.java2d.opengl", true);
        }

        if (System.getProperty("development") != null) {
            installInspector();
        }
    }

    private static void installInspector() {
        log.trace("App running in develop mode.Installing inspector");
        FlatInspector.install(System.getProperty("app.inspector", "ctrl shift alt X"));
        FlatUIDefaultsInspector.install(System.getProperty("app.ui-defaults.inspector", "ctrl shift alt Y"));
    }

    private static void initFlatUIManagerProperties() {
        UIManager.put("ProgressBar.horizontalSize", new Dimension(8, 120));
        UIManager.put("ProgressBar.horizontalSize", new Dimension(120, 8));
        UIManager.put("ProgressBar.arc", 999);
        UIManager.put("Spinner.disableOnBoundaryValues", true);
        UIManager.put("SplitPaneDivider.style", "plain");
        UIManager.put("Component.roundRect", true);
        UIManager.put("Component.arrowType", "triangle");
        UIManager.put("Component.arc", 12);
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("Table.paintOutsideAlternateRows", true);
        UIManager.put("Tree.selectionInsets", new Insets(0, 2, 0, 2));
        UIManager.put("Tree.selectionArc", 12);
        UIManager.put("List.selectionInsets", new Insets(0, 2, 0, 2));
        UIManager.put("List.selectionArc", 12);
        UIManager.put("Table.selectionInsets", new Insets(0, 2, 0, 2));
        UIManager.put("Table.selectionArc", 12);
        UIManager.put("ScrollPane.smoothScrolling", true);
        UIManager.put("MenuItem.selectionType", "underline");
        UIManager.put("Component.hideMnemonics", false);
    }

    private static void initFonts() {
        // install font
        HarmonyOSSansFont.install();
        FlatLaf.setPreferredFontFamily(HarmonyOSSansFont.FAMILY);
    }

    public record ThemeEntry(String name, FlatLaf laf) {
        public void install() {
            FlatLaf.setup(laf);
            FlatLaf.updateUILater();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
