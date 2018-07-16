package graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by wuhao on 7/27/16.
 */
public class L {
    public static void i(String msg) {
        GL20 gl = Gdx.graphics.getGL20();
        int e = gl.glGetError();
        if (e==GL20.GL_NO_ERROR) return;
        System.out.printf("%s error = %d 0x%x\n", msg, e, e);
    }
}
