package com.fphoenix.common;

public class LineMLayout {
	int []lens;
	int padding;
	int border0;
	int border1;
	float t;
	
	public LineMLayout(){}
	public void setTabs(int []lens){this.lens=lens;}
	public int getTabNumber(){return this.lens.length;}
	public void setT(float t){this.t = t;}
	public float getT(){return t;}
	public void setBorder(int b0, int b1){
		this.border0 = b0;
		this.border1 = b1;
	}
	public int getBorder0(){return border0;}
	public int getBorder1(){return border1;}
	public void setPadding(int padding){this.padding=padding;}
	public int getPadding(){return padding;}
	public float at(int idx){ // center
		if(idx<0 || idx>=lens.length)
			return -1;
		int i=0;
		float tt = t + border0;
		while(i<idx){
			tt += lens[i++];
		}
		tt += (float)lens[i]/2;
		return tt;
	}
	public float at(int idx, float anchor){
		if(idx<0 || idx>=lens.length)
			return -1;
		int i=0;
		float tt = t + border0;
		while(i<idx){
			tt += lens[i++] + padding;
		}
		tt += lens[i]*anchor;
		return tt;
	}
	public int lenAt(int idx){return lens[idx];}
	public int length(){
		int L = border0 + border1;
		for(int tmp : lens)
			L += tmp;
		return L + padding * (lens.length-1);
	}
}
