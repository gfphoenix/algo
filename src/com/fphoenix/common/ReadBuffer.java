package com.fphoenix.common;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadBuffer {
	BufferedReader br;
	char []buffer;
	int off;
	int cap;
	public ReadBuffer(BufferedReader br, int bufferSize) throws IOException{
		this.br = br;
		this.buffer = new char[bufferSize];
		this.off = 0;
		this.cap = 0;
		refill();
	}
	public char [] getBuffer(){
		return buffer;
	}
	public int getOff(){
		return 0;
	}
	public Info readLine(Info info)throws IOException{
		if(info==null){
			info = new Info();
		}
		eatLineChar();
		int i=off+1;
		for(; i<cap; i++){
			if(buffer[i]=='\r'||buffer[i]=='\n'){
				info.buffer = buffer;
				info.off = off;
				info.len = i - off;
				info.ok = true;
				off += info.len+1;
				return info;
			}
		}
		info.ok = false;
		return info;
	}
	void eatLineChar()throws IOException{
		int i=-1;
		while(i!=0){
			for(i=off; i<cap; i++){
				if(buffer[i]!='\r'&&buffer[i]!='\n'){
					break;
				}
			}
			if(i>0){
				refill();
			}
		}
	}

	// refill off to cap
	void refill() throws IOException{
		int len = cap-off;
		if(len>0)
			System.arraycopy(buffer, off, buffer, 0, len);
		off = 0;
		cap = len;
		int n = br.read(buffer, len, buffer.length - len);
		if(n>0)
			cap = len + n;
	}
	public static class Info {
		public char buffer[];
		public int off;
		public int len;
		public boolean ok;
	}
	/*
	public static void main(String []args){
		Info info=null;
		try{
			File  file = new File("dict.txt");
			Reader r = new FileReader(file);
			BufferedReader br = new BufferedReader(r);
			ReadBuffer bb = new ReadBuffer(br, 32);
			int cc=0;
			do{
				info = bb.readLine(info);
				if(!info.ok)
					break;
				System.out.println("Word = "+new String(info.buffer, info.off, info.len)+", len="+info.len);
				cc++;
			}while(true);
			System.out.println("cc="+cc);
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	*/
}
