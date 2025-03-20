

package utils;

import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GLContext;

import static com.jogamp.opengl.GL2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2.GL_VERTEX_SHADER;


/**
 * @author Trifindo
 */
@SuppressWarnings("unused")
public class GlUtils {
    public static int createShaderProgram(String vertPath, String fragPath) {
        GL2ES2   gl = GLContext.getCurrentGL().getGL2ES2();
        String[] vShaderSource;
        String[] fShaderSource;

        int vShader = gl.glCreateShader(GL_VERTEX_SHADER);
        int fShader = gl.glCreateShader(GL_FRAGMENT_SHADER);

        vShaderSource = Utils.readShaderAsResource(vertPath);
        fShaderSource = Utils.readShaderAsResource(fragPath);

        gl.glShaderSource(vShader, vShaderSource.length, vShaderSource, null, 0);
        gl.glShaderSource(fShader, fShaderSource.length, fShaderSource, null, 0);

        gl.glCompileShader(vShader);
        gl.glCompileShader(fShader);

        int vfProgram = gl.glCreateProgram();
        gl.glAttachShader(vfProgram, vShader);
        gl.glAttachShader(vfProgram, fShader);
        gl.glLinkProgram(vfProgram);

        gl.glDeleteShader(vShader);
        gl.glDeleteShader(fShader);
        return vfProgram;
    }
}
