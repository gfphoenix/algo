package graphics;

import java.nio.FloatBuffer;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by wuhao on 7/21/16.
 */
public class Renderer {
    Shader lastShader =null;
    OrthographicCamera camera;
    final FloatBuffer floatBuffer;
    int render_call =0;
    int last_call;

    public Renderer(OrthographicCamera camera) {
        this.camera = camera;
        this.floatBuffer = Buffers.dtmp.asFloatBuffer();
    }
    public OrthographicCamera getCamera() {
        return camera;
    }
    public FloatBuffer getFloatBuffer(){
        return floatBuffer;
    }
    // call this method at the beginning of rendering of one frame
    public void resetCounter() {
        render_call = 0;
    }

    // use this method before drawing any shape or texture,
    // it will automatically commit draw batch, if necessary.
    public void use(Shader shader) {
    	if(lastShader != shader) {
    		if(lastShader!=null)
    			lastShader.flush();
    		lastShader = shader;
    		shader.setRenderer(this);
    	}
//        if (lastShader ==shader) return;
//        if (lastShader != null)
//            lastShader.flush();
//        lastShader = shader;
//        shader.setRenderer(this);
    }
    // call this method after render one frame,
    // force flush the lastShader shader
    public void flush() {
        if (lastShader != null)
            lastShader.flush();
        lastShader = null;
        last_call = render_call;
    }
    public Shader getLastShader() {
        return lastShader;
    }
    public void inc_rendercall(){
        render_call++;
    }
    public int getRendercall(){
        return last_call;
    }
}
