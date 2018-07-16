package graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by wuhao on 7/21/16.
 */
    // Mesh treat the buffer data as raw memory buffer,
    // it doesn't check its data
public class Mesh implements Disposable {
    FloatBuffer vertex;
    ShortBuffer index; // null if not use indices buffer
    private ByteBuffer byte_vertex;
    private ByteBuffer byte_index;
    GL20 gl;
    Shader lastShader;

    int vertexBufferHandle;
    int indexBufferHandle;
    int drawMode;

    public Mesh() {
        gl = Gdx.graphics.getGL20();
        this.drawMode = GL20.GL_TRIANGLES;
        this.vertex = null;
        this.index = null;
    }

    public int getDrawMode() {
        return drawMode;
    }
    public void setDrawMode(int mode) {
    	drawMode = mode;
    }
    public void setDrawModeSafe(int mode) {
        if (mode!=drawMode){
        	if(lastShader!=null)
        		lastShader.flush();
            this.drawMode = mode;
        }
    }
    // send data but not flush immediately
    public void useShader(Shader shader) {
        if (lastShader != shader) {
        	if(lastShader!=null)
        		lastShader.flush();
            lastShader = shader;
        }
    }
    public boolean initBuffers(int floats) {
        return initBuffers(floats, 2);
    }
    public boolean initBuffers(int floats, int shorts) {
        if (floats<=0 || shorts<=0)
            throw new IllegalArgumentException("buffer size is neg: floats="+floats+", shorts="+shorts);
        byte_vertex = ByteBuffer.allocateDirect(floats * 4);
        vertex = byte_vertex.order(ByteOrder.nativeOrder()).asFloatBuffer();
        byte_index = ByteBuffer.allocateDirect(shorts * 2);
        index = byte_index.order(ByteOrder.nativeOrder()).asShortBuffer();
        vertexBufferHandle = createBufferObject();
        indexBufferHandle = createBufferObject();
        return vertexBufferHandle > 0 && indexBufferHandle > 0;
    }
    public int getVertexBufferSizeInFloats() {
    	return vertex.capacity();
    }
    public int getIndexBufferSizeInShorts() {
        return index.capacity();
    }
    public int getVertexBufferPos() {
        return vertex.position();
    }
    public int getIndexBufferPos() {
        return index.position();
    }
    private int createBufferObject() {
    	IntBuffer tmpHandle = Buffers.dtmp.asIntBuffer();
        tmpHandle.clear();
        gl.glGenBuffers(1, tmpHandle);
        return tmpHandle.get(0);
    }

    public Mesh setIndices(short []indices){
        return setIndices(indices, 0, indices.length);
    }
    public Mesh setIndices(short []indices, int offset, int count) {
        this.index.clear();
        this.index.put(indices, offset, count);
        return this;
    }
    public Mesh setVertices(float []vertices) {
        return setVertices(vertices, 0, vertices.length);
    }
    public Mesh setVertices(float []vertices, int offset, int count) {
    	this.vertex.clear();
        this.vertex.put(vertices, offset, count);
        return this;
    }
    public Mesh appendIndices(short []indices){
        return appendIndices(indices, 0, indices.length);
    }
    public Mesh appendIndices(short []indices, int offset, int count) {
        this.index.put(indices, offset, count);
        return this;
    }
    public Mesh appendVertices(float []vertices) {
        return appendVertices(vertices, 0, vertices.length);
    }
    public Mesh appendVertices(float []vertices, int offset, int count) {
        this.vertex.put(vertices, offset, count);
        return this;
    }

    // bind, render, unbind
    public void bind(Shader shader) {
        int program = shader.getActiveShader();
        gl.glUseProgram(program);
        L.i("mesh: use shader program=");
        ShaderUniforms uniforms = shader.getUniforms();
        if (uniforms != null)
            uniforms.setUniformValues(shader);
        bindBuffers();
        shader.bindAttribs();
    }

    public void unbind(Shader shader) {
        shader.unbindAttribs();
        unbindBuffers();
        gl.glUseProgram(0);
    }

    void bindBuffers() {
        L.i("start bind vertex buffer");
        gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, vertexBufferHandle);
        L.i("bind vertex buffer");
        gl.glBufferData(GL20.GL_ARRAY_BUFFER, byte_vertex.limit(), byte_vertex, GL20.GL_DYNAMIC_DRAW);
        L.i("set buffer data");
        if (isUseIndex()) {
            gl.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, indexBufferHandle);
            L.i("bind index buffer");
            gl.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, byte_index.limit(), byte_index, GL20.GL_DYNAMIC_DRAW);
            L.i("set index buffer");
        }
    }

    void unbindBuffers() {
    	gl.glBufferData(GL20.GL_ARRAY_BUFFER, byte_vertex.limit(), null, GL20.GL_DYNAMIC_DRAW);
        gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
        if (isUseIndex())
            gl.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public boolean isUseIndex() {
        return drawMode != GL20.GL_POINTS;
    }
// called by flush automatically
    public void render() {
        // send data to vertices buffer and indices buffer
        // AND commit draw command to the opengl server
    	L.i("before draw");
        if (isUseIndex()) {
            gl.glDrawElements(drawMode, index.limit(), GL20.GL_UNSIGNED_SHORT, 0);
        } else {
            gl.glDrawArrays(drawMode, 0, vertex.limit());
        }
        L.i("after draw");
    }

    public void flush() {
        Shader shader = lastShader;
        FloatBuffer vertex = this.vertex;
        if (shader == null || vertex.position() == 0) return;
        vertex.flip();
        ByteBuffer byte_vertex = this.byte_vertex;
        byte_vertex.position(vertex.limit()*4);
        byte_vertex.flip();

        if (isUseIndex()){
            index.flip();
            byte_index.position(index.limit()*2);
            byte_index.flip();
        }
        L.i("before bind ...");
        bind(shader);
        render();
        unbind(shader);

        vertex.clear();
        byte_vertex.clear();
        if (isUseIndex()){
            index.clear();
            byte_index.clear();
        }
        lastShader = null;
    }

    protected boolean reValidateBufferObject() {
        deleteBufferObject();
        vertexBufferHandle = createBufferObject();
        indexBufferHandle = createBufferObject();
        return vertexBufferHandle > 0 && indexBufferHandle > 0;
    }

    protected void deleteBufferObject() {
        IntBuffer tmpHandle = Buffers.dtmp.asIntBuffer();
        if (vertexBufferHandle > 0) {
            tmpHandle.clear();
            tmpHandle.put(vertexBufferHandle);
            tmpHandle.flip();
            gl.glDeleteBuffers(1, tmpHandle);
            vertexBufferHandle = 0;
        }
        if (indexBufferHandle > 0) {
            tmpHandle.clear();
            tmpHandle.put(indexBufferHandle);
            tmpHandle.flip();
            gl.glDeleteBuffers(1, tmpHandle);
            indexBufferHandle = 0;
        }
    }

    @Override
    public void dispose() {
        deleteBufferObject();
    }
}
