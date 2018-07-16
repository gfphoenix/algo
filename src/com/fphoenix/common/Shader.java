package com.fphoenix.common;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

public class Shader{
String vert;
String frag;

int programID;
//ShaderProgram program;
float []vertexdata;
int idx;

//static class Buffer {
	FloatBuffer buffer_vbo;
	ShortBuffer buffer_index;
	IntBuffer id_vbo;
	IntBuffer id_index;
	
	Map<Integer, Attribute> attributes;
	Map<String, Uniform> uniforms;
	
	
	void initBuffer(int M, int N){
		buffer_vbo = FloatBuffer.allocate(M);
		buffer_index = ShortBuffer.allocate(N);
		id_vbo = IntBuffer.allocate(1);
		id_index = IntBuffer.allocate(1);
	}
	boolean initProgram(String vertSrc, String fragSrc){
		programID = ShaderHelper.compileShaderProgram(vertSrc, fragSrc);
		return programID>0;
	}
//}
// put attribute data in float format
	public Shader putFloat(float v){
		if(idx>=vertexdata.length){
			flush();
		}
		vertexdata[idx++] = v;
		return this;
	}
	public Shader putFloat(float []array, int offset, int length){
		while(length>0){
			int L = Math.min(vertexdata.length-idx, length);
			System.arraycopy(array, offset, vertexdata, idx, L);
			offset += L;
			length -= L;
			idx += L;
			if(vertexdata.length<=idx){
				flush();
			}
		}
		return this;
	}
	public Shader putFloat(float []array){
		return putFloat(array, 0, array.length);
	}
	public void flush(){
		
		// last
		idx = 0;
	}
	Texture []last = new Texture[8];
	public void setTexture(int index, Texture t){
		if(t==null) return;
		if(index<0 || index>=last.length) return;
		if(t==last[index]) return;
		flush();
		last[index] = t;
	}
	public void setTexture(Texture t){
		setTexture(0, t);
	}


	static class Uniform {
		String name;
		int location;
	}
	static class Attribute {
		String name;
		int size;
		int type;
		int stride;
		int offset;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
