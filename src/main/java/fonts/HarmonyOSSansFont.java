package fonts;

import com.formdev.flatlaf.util.FontUtils;

@SuppressWarnings("unused")
public class HarmonyOSSansFont {
    public static final String FAMILY = "HarmonyOS Sans SC";
    public static final String STYLE_BLACK = "HarmonyOS_Sans_SC_Black.ttf";
    public static final String STYLE_BOLD = "HarmonyOS_Sans_SC_Bold.ttf";
    public static final String STYLE_LIGHT = "HarmonyOS_Sans_SC_Light.ttf";
    public static final String STYLE_MEDIUM = "HarmonyOS_Sans_SC_Medium.ttf";
    public static final String STYLE_REGULAR = "HarmonyOS_Sans_SC_Regular.ttf";
    public static final String STYLE_THIN = "HarmonyOS_Sans_SC_Thin.ttf";

    public static void install() {
        installStyle(STYLE_BLACK);
        installStyle(STYLE_BOLD);
        installStyle(STYLE_LIGHT);
        installStyle(STYLE_MEDIUM);
        installStyle(STYLE_REGULAR);
        installStyle(STYLE_THIN);
    }

    public static void installLazy() {
        FontUtils.registerFontFamilyLoader(FAMILY, HarmonyOSSansFont::install);
    }

    private static void installStyle(String fontName) {
        FontUtils.installFont(HarmonyOSSansFont.class.getResource(fontName));
    }
}
