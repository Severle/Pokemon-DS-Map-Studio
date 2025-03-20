
package formats.animationeditor;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * @author Trifindo
 */
@Log4j2
public class AnimationThread extends Thread {

    private final    AnimationHandler animHandler;
    @Getter
    private volatile boolean          running = true;

    public AnimationThread(AnimationHandler animHandler) {
        this.animHandler = animHandler;
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        while (running) {
            animHandler.incrementFrameIndex();
            animHandler.repaintDialog();

            try {
                Thread.sleep((long) ((animHandler.getCurrentDelay() / 30.0f) * 1000));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                log.warn(ex);
            }
        }
    }

    public void terminate() {
        this.running = false;
    }
}
