
package utils;

/**
 * @author Trifindo
 */
public class LambdaUtils {
    public interface VoidInterface {
        void action();
    }

    public static void systemPropertyNotOverwrite(String propertyName, Object value) {
        System.setProperty(propertyName, System.getProperty(propertyName, value.toString()));
    }
}
