import editor.MainFrame;
import editor.mapmatrix.MapMatrix;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tileset.Tileset;
import utils.ThemeUtil;

import java.awt.*;

import static editor.MainFrame.preferences;
import static utils.LambdaUtils.systemPropertyNotOverwrite;

@SuppressWarnings("SpellCheckingInspection")
public class Main {
    static {
        systemPropertyNotOverwrite("log4j.skipJansi", false);
        systemPropertyNotOverwrite("java.util.logging.manager", org.apache.logging.log4j.jul.LogManager.class);
    }

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            // install theme
            String theme = preferences.get(ThemeUtil.KEY, ThemeUtil.defaultTheme().name());
            ThemeUtil.getOrDefault(theme).install();

            // load recent map
            MainFrame.initialize();
        } catch (Exception ex) {
            log.error("Failed to initialize LaF", ex);
        }

        /* Create and display the form */
        EventQueue.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);

            if (args.length > 0) {
                try {
                    if (args[0].endsWith(MapMatrix.fileExtension)) {
                        mainFrame.openMap(args[0]);
                    } else if (args[0].endsWith(Tileset.fileExtension)) {
                        mainFrame.openTileset(args[0]);
                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }
        });
    }
}
