
package formats.nsbtx2;

import lombok.extern.log4j.Log4j2;
import utils.Utils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Trifindo
 */
@Log4j2
@SuppressWarnings({"SpellCheckingInspection", "DuplicatedCode"})
public class NsbtxWriter {

    //TODO: improve all of this by not using the converter

    public static byte[] writeNsbtx(Nsbtx2 nsbtx, String fileName) throws IOException {

        log.debug("EXPORTING NSBTX IMD!");

        String path = System.getProperty("user.dir") + File.separator + fileName;
        log.debug(path);
        path = Utils.addExtensionToPath(path, "imd");
        log.debug(path);

        NsbtxImd imd = new NsbtxImd(nsbtx);

        try {
            imd.saveToFile(path);

            return imdToNsbmd(path);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            throw new IOException();
        }
    }

    private static byte[] imdToNsbmd(String imdPath) throws IOException {
        File file = new File(imdPath);
        log.debug("EXPORTING NSBMD");
        byte[] data = null;
        if (file.exists()) {
            log.debug("File exists!");
            String filename = new File(imdPath).getName();
            filename = Utils.removeExtensionFromPath(filename);
            String converterPath = "converter/g3dcvtr.exe";
            String[] cmd;
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                cmd = new String[]{converterPath, imdPath, "-etex", "-o", filename};
            } else {
                cmd = new String[]{"wine", converterPath, imdPath, "-etex", "-o", filename};
                // NOTE: wine call works only with relative path
            }

            if (!Files.exists(Paths.get(converterPath))) {
                log.debug("Converter not found!");
                throw new IOException();
            }

            Process p = new ProcessBuilder(cmd).start();

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            StringBuilder outputString = new StringBuilder();
            String        line;
            while ((line = stdError.readLine()) != null) {
                outputString.append(line).append("\n");
            }
            log.debug(outputString.toString());

            try {
                p.waitFor();
            } catch (InterruptedException ex) {
                throw new IOException();
            }

            p.destroy();

            String nsbPath = Utils.removeExtensionFromPath(imdPath);
            Utils.addExtensionToPath(nsbPath, "nsbtx");

            filename = Utils.removeExtensionFromPath(filename);
            filename = Utils.addExtensionToPath(filename, "nsbtx");

            File srcFile = new File(System.getProperty("user.dir") + File.separator + filename);
            if (srcFile.exists()) {
                data = Files.readAllBytes(srcFile.toPath());

                if (file.exists()) {
                    Files.delete(file.toPath());
                }
                if (srcFile.exists()) {
                    Files.delete(srcFile.toPath());
                }
            } else {
                throw new IOException();
            }
        }

        return data;
    }

}
