package graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.IntBuffer;

/**
 * Created by wuhao on 7/28/16.
 */
public class FrameBuffer implements Disposable {
    GL20 gl;
    int framebufferHandle;
    int renderbufferHandle;
    int width;
    int height;
    int pixelFormat;
    public enum PixelFormat {
        RGB8(GL20.GL_RGB),
        RGBA4(GL20.GL_RGBA4),
        RGBA565(GL20.GL_RGB565),
        RGBA5551(GL20.GL_RGB5_A1),
        RGBA8(GL20.GL_RGBA);
        int format;
        PixelFormat(int type){
            this.format = type;
        }
        public int getGLFormat() {
            return format;
        }
    }
    public static class Config {
        public int width;
        public int height;
        public PixelFormat format;
        public int depBits=0; // default no depth
    }
    public FrameBuffer(){
        gl = Gdx.graphics.getGL20();
    }
    public boolean init(){
        IntBuffer b = Buffers.dtmp.asIntBuffer();
        b.clear();
        gl.glGenFramebuffers(1, b);
        // glGetError()
        framebufferHandle = b.get(0);
        return framebufferHandle >0;
    }
    public void use(){
        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle);
    }
    public void unuse(){
        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0);
    }

    @Override
    public void dispose() {
        IntBuffer b = Buffers.dtmp.asIntBuffer();
        b.clear();
        b.put(framebufferHandle);
        b.flip();
        gl.glDeleteFramebuffers(1, b);
    }
}
