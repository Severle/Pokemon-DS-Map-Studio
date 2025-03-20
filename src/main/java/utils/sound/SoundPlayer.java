
package utils.sound;

import lombok.extern.log4j.Log4j2;
import utils.LambdaUtils.VoidInterface;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Trifindo
 */
@Log4j2
public class SoundPlayer extends Thread {
    private final AtomicBoolean running = new AtomicBoolean(false);

    private String filename;
    private InputStream bufferedIn;
    private AudioInputStream audioStream;
    private SourceDataLine sourceLine;

    private VoidInterface endAction;

    public void init(String filename, VoidInterface endAction) {
        this.filename = filename;
        this.endAction = endAction;
    }

    private void playSound() {
        try {
            if (filename != null) {
                try {
                    InputStream inputStream = SoundPlayer.class.getResourceAsStream(filename);
                    if (inputStream != null) {
                        bufferedIn = new BufferedInputStream(inputStream);
                    }
                } catch (Exception e) {
                    log.error(e);
                }

                try {
                    audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                } catch (Exception e) {
                    log.error(e);
                }

                AudioFormat audioFormat = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                try {
                    sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                    sourceLine.open(audioFormat);
                } catch (Exception e) {
                    log.error(e);
                }

                sourceLine.start();

                int nBytesRead = 0;
                int BUFFER_SIZE = 128000;
                byte[] abData = new byte[BUFFER_SIZE];
                while (nBytesRead != -1 && running.get()) {
                    try {
                        nBytesRead = audioStream.read(abData, 0, abData.length);
                    } catch (IOException e) {
                        log.error(e);
                    }
                    if (nBytesRead >= 0) {
                        @SuppressWarnings("unused")
                        int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                    }
                }
                sourceLine.drain();
                sourceLine.close();
            }
        } catch (Exception ex) {
            log.error(ex);
        } finally {
            endAction.action();
        }
    }

    @Override
    public void run() {
        log.debug("Running");
        running.set(true);
        playSound();
        log.debug("Finished");
    }

    public void stopPlayer() {
        log.debug("Stopping");
        running.set(false);
        if (sourceLine != null) {
            sourceLine.stop();
        }
        if (sourceLine != null) {
            sourceLine.close();
        }
    }
}
