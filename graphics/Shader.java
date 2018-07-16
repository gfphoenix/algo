package graphics;

import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by wuhao on 7/21/16.
 */
public abstract class Shader implements Disposable{

    public static class ShaderVar {
        private ShaderVarType type_;
        private String name_;
        private int location_ = -1;

        public ShaderVar(ShaderVarType type, String name) {
            this.type_ = type;
            this.name_ = name;
        }
        public ShaderVarType type() {
            return this.type_;
        }
        public String name() {
            return this.name_;
        }
        public int location() {
            return this.location_;
        }
        // should be called by shader after compiled shader program
        private void setLocation(int location){
            this.location_ = location;
        }

        @Override
        public String toString() {
            return type().name()+" name="+name()+" location="+location();
        }
    }
    Renderer renderer;
    String vertex_source;
    String frag_source;
    ShaderAttribs attribs;
    ShaderUniforms uniforms;
    GL20 gl;
    
    public Shader() {
        gl = Gdx.graphics.getGL20();
    }

    protected void setSource(String vertex, String frag) {
    	this.vertex_source = vertex;
    	this.frag_source = frag;
    }
    protected void setShaderVar(ShaderAttribs attribs, ShaderUniforms uniforms) {
    	this.attribs = attribs;
    	this.uniforms = uniforms;
    }
    public Renderer getRenderer(){
        return renderer;
    }
    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }
    public GL20 gl20() {
        return gl;
    }
    public abstract int getActiveShader();
    public ShaderAttribs getAttribs() {
        return this.attribs;
    }
    public ShaderUniforms getUniforms() {
        return this.uniforms;
    }

    public abstract Mesh getMesh();
    public abstract void flush();
    //
    public abstract void invalidateProgram();
    protected void _invalidateProgram() {
        int id = getActiveShader();
        if (id>0)
            gl.glDeleteProgram(id);
    }
    public void bindAttribs() {
        ShaderAttribs as = getAttribs();
        int Vsize = as.getVertexSize();
        for (int i=0,n=as.getAttribNumber(); i<n; i++) {
            ShaderAttribs.ShaderAttrib v = as.attribAt(i);
            int loc = v.location();
            if (loc<0) continue;
            ShaderVarType vt = v.type();
            gl.glEnableVertexAttribArray(loc);
            gl.glVertexAttribPointer(
                    loc,
                    vt.getNComponents(),
                    vt.getGLType(),
                    v.isNormalized(),
                    Vsize,
                    v.offset()
                    );
        }
    }
    public void unbindAttribs() {
        ShaderAttribs as = getAttribs();
        for (int i=0,n=as.getAttribNumber(); i<n; i++) {
            ShaderVar v = as.attribAt(i);
            int loc = v.location();
            if (loc<0) continue;
            gl.glDisableVertexAttribArray(loc);
        }
    }

    protected int updateLocations() {
        int program = getActiveShader();
        int badLoc = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = attribs.getAttribNumber(); i < n; i++) {
            ShaderVar attrib = attribs.attribAt(i);
            int location = gl.glGetAttribLocation(program, attrib.name());
            attrib.setLocation(location);
            if (location < 0) {
                badLoc++;
                sb.append("attrib ['").append(i).append("] = ");
                sb.append(attrib.name()).append("' location = ");
                sb.append(location).append('\n');
            }
        }
        if (uniforms != null && uniforms.getUniformNumber() > 0)
            for (int i = 0, n = uniforms.getUniformNumber(); i < n; i++) {
                ShaderVar uniform = uniforms.uniformAt(i);
                int location = gl.glGetUniformLocation(program, uniform.name());
                uniform.setLocation(location);
                if (location < 0){
                    badLoc++;
                    sb.append("uniform ['").append(i).append("] = ");
                    sb.append(uniform.name()).append("' location = ");
                    sb.append(location).append('\n');
                }
            }
        if (badLoc > 0)
            System.out.println("bad location **=> :\n"+sb);
        return badLoc;
    }
    // return the number of bad locations
    public int setup(int program, ShaderAttribs attribs, ShaderUniforms uniforms) {
        if (program <= 0)
            throw new IllegalArgumentException("invalid program="+program);
        if (attribs == null)
            throw new IllegalArgumentException("attribs must not null");
        if (attribs.getAttribNumber()<1)
            throw new IllegalArgumentException("attribs must has at least one attrib");

        this.attribs = attribs;
        this.uniforms = uniforms;
        return updateLocations();
    }

    public static int compileShaderProgram(String vertex_source, String frag_source) {
        GL20 gl = Gdx.graphics.getGL20();

        int vertex_id = loadShader(GL20.GL_VERTEX_SHADER, vertex_source);
        int frag_id = loadShader(GL20.GL_FRAGMENT_SHADER, frag_source);
        int program = -1;
        boolean vok = vertex_id > 0;
        boolean fok = frag_id > 0;
        if (vok && fok) {
            program = linkProgram(vertex_id, frag_id);
        }
        if (vok) gl.glDeleteShader(vertex_id);
        if (fok) gl.glDeleteShader(frag_id);

        return program;
    }
// must native direct buffer
    public static int loadShader(int type, String source) {
        GL20 gl = Gdx.graphics.getGL20();
        IntBuffer intbuf = Buffers.dtmp.asIntBuffer();
        intbuf.clear();
        int shader = gl.glCreateShader(type);
        if (shader == 0) return -1;

        gl.glShaderSource(shader, source);
        gl.glCompileShader(shader);
        gl.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, intbuf);

        int compiled = intbuf.get(0);
        if (compiled == 0) {
            String infoLog = gl.glGetShaderInfoLog(shader);
            System.out.println(infoLog);
            return -1;
        }

        return shader;
    }

    public static int linkProgram(int vertexShaderHandle, int fragmentShaderHandle) {
        GL20 gl = Gdx.graphics.getGL20();
        int program = gl.glCreateProgram();
        if (program == 0) return -1;

        gl.glAttachShader(program, vertexShaderHandle);
        gl.glAttachShader(program, fragmentShaderHandle);
        gl.glLinkProgram(program);

        IntBuffer intbuf = Buffers.dtmp.asIntBuffer();
        intbuf.clear();

        gl.glGetProgramiv(program, GL20.GL_LINK_STATUS, intbuf);
        int linked = intbuf.get(0);
        if (linked == 0) {
            String infoLog = Gdx.gl20.glGetProgramInfoLog(program);
            System.out.println(infoLog);
            return -1;
        }
        return program;
    }
}
