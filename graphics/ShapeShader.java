package graphics;

import java.nio.FloatBuffer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by wuhao on 7/27/16.
 */
public class ShapeShader extends ShaderHelper {
	static final int VERTEX_SIZE = 3; // 3 floats per vertex
	int minP;
	int minL;
    // make sure you know how to determine the buffer size
    public ShapeShader(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms, Mesh mesh, int floats, int shorts) {
        init(vert_source, frag_source, attribs, uniforms, mesh, floats, shorts);
    }
    // set buffer size based on the size of mesh
    public ShapeShader(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms, Mesh mesh) {
        init(vert_source, frag_source, attribs, uniforms, mesh);
    }
    public ShapeShader(String vert_source, String frag_source, ShaderAttribs attribs, ShaderUniforms uniforms) {
        init(vert_source, frag_source, attribs, uniforms);
    }
    private void init(Mesh mesh, int floats, int shorts) {
        String v_source =
                		"attribute vec2 a_pos;\n" +
                        "attribute vec4 a_color;\n"+
                        "uniform mat4 u_pv;\n" +
                        "varying vec4 ocolor;\n" +
                        "void main(void){\n" +
                        "gl_Position= u_pv * vec4(a_pos, 0.0, 1.0);\n" +
                        "gl_PointSize=10.0;\n" +
                        "ocolor = a_color;\n" +
                        "}\n";
        String f_source =
                "#ifdef GL_ES\nprecision mediump float;\n#endif\n" +
                        "varying vec4 ocolor;\n" +
                        "void main(){\n" +
                        "   gl_FragColor = ocolor;\n" +
                        "}\n";
        final ShaderAttribs attribs = new ShaderAttribs(
                new ShaderAttribs.ShaderAttrib(ShaderVarType.VEC2, "a_pos")
                , new ShaderAttribs.ShaderAttrib(ShaderVarType.C4B, "a_color")
        ) ;
        final ShaderUniforms uniforms = new ShaderUniforms(new ShaderVar(ShaderVarType.Mat4, "u_pv")){
            @Override
            public void setUniformValues(Shader shader) {
                setUniformValues__(renderer.getFloatBuffer());
            }
        };
        init(v_source, f_source, attribs, uniforms, mesh, floats, shorts);
    }
    private void init(Mesh mesh) {
        init(mesh, getFloatsByMesh(mesh), getShortsByMesh(mesh));
    }
    public ShapeShader() {
        init(createDefaultMesh());
    }
    public ShapeShader(Mesh mesh){
        init(mesh);
    }
    public ShapeShader(Mesh mesh, int floats, int shorts){
        init(mesh, floats, shorts);
    }
    @Override
    protected Mesh createDefaultMesh() {
        Mesh mesh = new Mesh();
        final int floats = 1000;
        final int shorts = 1000;
        boolean ok = mesh.initBuffers(floats, shorts);
        System.out.println("shape shader mesh init = "+ok);
        return mesh;
    }
    
    @Override
    protected void initBuffers(int floats, int shorts) {
    	super.initBuffers(floats, shorts);
    	minP = Math.min(floats/VERTEX_SIZE, 200);
    	minL = Math.min(floats/(2*VERTEX_SIZE), 300);
    }
    @Override
    public void flush() {
        if (vertexIndex ==0) return;
//        renderer.use(this);
        mesh.useShader(this);
        mesh.setVertices(vertices, 0, vertexIndex);
        if(mesh.isUseIndex())
        	mesh.setIndices(indices, 0, shortIndex);
        mesh.flush();
        vertexIndex = 0;
        shortIndex = 0;
    }
    private void setUniformValues__(FloatBuffer buffer){
        L.i("start setting uniform values");
        OrthographicCamera camera = renderer.getCamera();
        camera.update();
        buffer.clear();
        buffer.put(camera.combined.val);
        buffer.flip();
        ShaderUniforms us = getUniforms();
        gl.glUniformMatrix4fv(us.uniformAt(0).location(), 1, false, buffer);
        L.i("after set M4");
    }
    // expose raw data pointer
    public float []getVertexArray() {
    	return this.vertices;
    }
    public int getVertexArrayPos() {
    	return this.vertexIndex;
    }
    public short []getIndexArray() {
    	return this.indices;
    }
    public int getIndexArrayPos() {
    	return this.shortIndex;
    }
    public void drawPoint(float x1, float y1) {
    	mesh.setDrawModeSafe(GL20.GL_POINTS);
    	float cc = this.colorBits;
    	if (vertexIndex>=minP)
            flush();
    	int idx = this.vertexIndex;
    	vertices[idx++] = x1;
    	vertices[idx++] = y1;
    	vertices[idx++] = cc;
    	this.vertexIndex = idx;
    }

	final int bound = minP * VERTEX_SIZE;
	public void drawPoints2(float []data, int offset, int n) {
    	if(data==null || offset<0 || n<=0 || offset+n+n>data.length){
    		StringBuilder sb = new StringBuilder();
    		sb.append("data = ").append(data);
    		if(data!=null) 
    			sb.append(" len = ")
    			.append(data.length)
    			.append(" | ");
    		sb.append(", offset=")
    		.append(offset)
    		.append(", pairs = ")
    		.append(n);
    		throw new IllegalArgumentException(sb.toString());
    	}
    	mesh.useShader(this);
    	mesh.setDrawModeSafe(GL20.GL_POINTS);
    	final int one = minP;
    	int remaining = one - this.vertexIndex/VERTEX_SIZE;
    	if(remaining<=0){
    		flush();
    		remaining = one;
    	}
    	int min = Math.min(remaining, n);
    	int vertexIndex = this.vertexIndex;
    	float cc = this.colorBits;
    	float []vertices = this.vertices;
    	for(int i=0; i<min; i++) {
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = cc;
    	}
    	this.vertexIndex = vertexIndex;
    	if(n == min) return;
    	flush();
    	n -= min;
    	int N = n/one;
    	for(int i=0; i<N; i++) {
    		vertexIndex = 0;
    		for(int k=0; k<one; k++){
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = cc;
    		}
    		this.vertexIndex = vertexIndex;
    		flush();
    		n -= one;
    	}
    	vertexIndex = 0;
    	for(int k=0; k<n; k++){
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = cc;
    	}
    	this.vertexIndex = vertexIndex;
    }
    // data : ... x1, y1, x2, y2, ...
    public void drawPoints(float []data, int offset, int n) {
    	if(data==null || offset<0 || n<=0 || offset+n+n>data.length){
    		StringBuilder sb = new StringBuilder();
    		sb.append("data = ").append(data);
    		if(data!=null) 
    			sb.append(" len = ")
    			.append(data.length)
    			.append(" | ");
    		sb.append(", offset=")
    		.append(offset)
    		.append(", pairs = ")
    		.append(n);
    		throw new IllegalArgumentException(sb.toString());
    	}
    	mesh.useShader(this);
    	mesh.setDrawModeSafe(GL20.GL_POINTS);
    	final int one = this.vertices.length / VERTEX_SIZE;
    	int remaining = (this.vertices.length - this.vertexIndex)/VERTEX_SIZE;
    	if(remaining<=0){
    		flush();
    		remaining = one;
    	}
    	int min = Math.min(remaining, n);
    	int vertexIndex = this.vertexIndex;
    	float cc = this.colorBits;
    	float []vertices = this.vertices;
    	for(int i=0; i<min; i++) {
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = cc;
    	}
    	this.vertexIndex = vertexIndex;
    	if(n == min) return;
    	flush();
    	n -= min;
    	int N = n/one;
    	for(int i=0; i<N; i++) {
    		vertexIndex = 0;
    		for(int k=0; k<one; k++){
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = cc;
    		}
    		this.vertexIndex = vertexIndex;
    		flush();
    		n -= one;
    	}
    	int left = n;
    	vertexIndex = 0;
    	for(int k=0; k<left; k++){
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = data[offset++];
    		vertices[vertexIndex++] = cc;
    	}
    	this.vertexIndex = vertexIndex;
    }
    public void drawLine(float x1, float y1, float x2, float y2) {
    	mesh.useShader(this);
        mesh.setDrawModeSafe(GL20.GL_LINES);
        float cc = this.colorBits;
//        if (vertexIndex+2*6>vertices.length || shortIndex+2>indices.length)
        if (vertexIndex>=minL*VERTEX_SIZE*2)
            flush();

        int idx = this.vertexIndex/3;
        int shortIndex = this.shortIndex;
        indices[shortIndex++] = (short)(idx);
        indices[shortIndex++] = (short)(idx+1);
        this.shortIndex = shortIndex;
        
        idx = this.vertexIndex;
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = cc;
        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = cc;
        this.vertexIndex = idx;
    }
    public void drawLine(float x1, float y1, float x2, float y2, Color c1, Color c2) {
    	mesh.useShader(this);
    	mesh.setDrawModeSafe(GL20.GL_LINES);
        if (vertexIndex+2*6>vertices.length || shortIndex+2>indices.length)
            flush();

        int idx = this.vertexIndex/3;
        int shortIndex = this.shortIndex;
        indices[shortIndex++] = (short)(idx);
        indices[shortIndex++] = (short)(idx+1);
        this.shortIndex = shortIndex;
        
        idx = this.vertexIndex;
        vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = c1.toFloatBits();
        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = c2.toFloatBits();
        this.vertexIndex = idx;
    }
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
    	mesh.useShader(this);
    	mesh.setDrawModeSafe(GL20.GL_TRIANGLES);
    	if(vertexIndex + 3*VERTEX_SIZE > vertices.length || shortIndex+3>indices.length)
    		flush();
    	int idx = this.vertexIndex/VERTEX_SIZE;
    	int shortIndex = this.shortIndex;
    	indices[shortIndex++] = (short)(idx);
    	indices[shortIndex++] = (short)(idx+1);
    	indices[shortIndex++] = (short)(idx+2);
    	this.shortIndex = shortIndex;
    	
    	idx = this.vertexIndex;
    	float cc = this.colorBits;
    	vertices[idx++] = x1;
        vertices[idx++] = y1;
        vertices[idx++] = cc;
        vertices[idx++] = x2;
        vertices[idx++] = y2;
        vertices[idx++] = cc;
        vertices[idx++] = x3;
        vertices[idx++] = y3;
        vertices[idx++] = cc;
        this.vertexIndex = idx;
    }

}
