package com.fphoenix.common;

import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class ShaderHelper {
	public static int loadShader (int type, String source) {
		GL20 gl = Gdx.graphics.getGL20();
		IntBuffer intbuf = IntBuffer.wrap(new int[1]);

		int shader = gl.glCreateShader(type);
		if (shader == 0) return -1;

		gl.glShaderSource(shader, source);
		gl.glCompileShader(shader);
		gl.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, intbuf);

		int compiled = intbuf.get(0);
		if (compiled == 0) {
			String infoLog = gl.glGetShaderInfoLog(shader);
			
			return -1;
		}

		return shader;
	}

	public static int linkProgram (int vShader, int fShader) {
		GL20 gl = Gdx.graphics.getGL20();
		int program = gl.glCreateProgram();
		if (program == 0) return -1;

		gl.glAttachShader(program, vShader);
		gl.glAttachShader(program, fShader);
		gl.glLinkProgram(program);

//		ByteBuffer tmp = ByteBuffer.allocateDirect(4);
//		tmp.order(ByteOrder.nativeOrder());
//		IntBuffer intbuf = tmp.asIntBuffer();

		IntBuffer intbuf = IntBuffer.wrap(new int[1]);
		gl.glGetProgramiv(program, GL20.GL_LINK_STATUS, intbuf);
		int linked = intbuf.get(0);
		if (linked == 0) {
			return -1;
		}
		return program;
	}
	
	public static int compileShaderProgram(String v, String f){
		int vid = loadShader(GL20.GL_VERTEX_SHADER, v);
		int fid = loadShader(GL20.GL_FRAGMENT_SHADER, f);
		int program=-1;
		if(vid>0 && fid>0){
			program = linkProgram(vid, fid);
		}
		GL20 gl = Gdx.graphics.getGL20();
		
		if(vid>0){
			gl.glDeleteShader(vid);
		}
		if(fid>0){
			gl.glDeleteShader(fid);
		}
		return program;
	}
}
