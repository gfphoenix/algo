package graphics;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by wuhao on 7/28/16.
 */
public abstract class ShaderHelper extends Shader {
    protected Mesh mesh;
    final Color color = new Color(Color.WHITE);
    protected float [] vertices;
    protected short [] indices;
    protected float colorBits = Color.WHITE.toFloatBits();
    protected int vertexIndex = 0;
    protected int shortIndex = 0;
    protected int program;
    @Override
    public int getActiveShader() {
        return program;
    }
    @Override
    public Mesh getMesh() {
        return mesh;
    }
    protected void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
    @Override
    public void invalidateProgram() {
        super._invalidateProgram();
        program = 0;
    }
    public Color getColor() {
        return color;
    }
    public float getColorBits() {
        return this.colorBits;
    }
    public void setColor(Color color) {
        this.color.set(color);
        this.colorBits = this.color.toFloatBits();
    }
    public void setColor(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
        this.colorBits = this.color.toFloatBits();
    }
    protected void initBuffers(int floats, int shorts) {
    	this.vertices = new float[floats];
        this.indices = new short[shorts];
	}
    protected final void init(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms, Mesh mesh, int floats, int shorts) {
        int program = compileShaderProgram(vert_source, frag_source);
        if (program<=0)
            throw new RuntimeException("init sprite-shader failed");
        this.program = program;
        setSource(vert_source, frag_source);
        int badLoc = setup(program, attribs, uniforms);
        System.out.println("bad loc = "+badLoc);
        setMesh(mesh);
        initBuffers(floats, shorts);
        
        System.out.println("sprite shader vertices attribs= "+attribs);
        System.out.println("sprite shader uniforms = "+uniforms);
    }
    protected final void init(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms, Mesh mesh) {
        final int floats = getFloatsByMesh(mesh);
        final int shorts = getShortsByMesh(mesh);
        init(vert_source, frag_source, attribs, uniforms, mesh, floats, shorts);
    }
    protected final void init(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms) {
        init(vert_source, frag_source, attribs, uniforms, createDefaultMesh());
    }
    protected abstract Mesh createDefaultMesh();
    protected int getFloatsByMesh(Mesh mesh) {
        return mesh.getVertexBufferSizeInFloats();
    }
    protected int getShortsByMesh(Mesh mesh) {
        return mesh.getIndexBufferSizeInShorts();
    }
    
    @Override
    public void dispose() {
    	gl.glUseProgram(0);
    	gl.glDeleteProgram(program);
    	program = 0;
    }
}
