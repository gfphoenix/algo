package com.fphoenix.common;


public class LineLayout {
float t, t_;
float size, size_;
float border0;
float border1;
float padding;
int N;
public LineLayout(){}
public LineLayout reset(){
	t=t_=0;
	size=size_=0;
	border0=border1=0;
	padding=0;
	N=1;
	return this;
}
public void setRange(float t, float size){
	this.t = t;
	this.size = size;
	update();
}
public float getT(){return t;}
public float getSize(){return size;}
public int getN(){return N;}
public void setN(int n){N=Math.max(n, 1);}
public float getBorder0() {return border0;}
public float getBorder1() {return border1;}
public float getPadding() {return padding;}
public void setBorder0(float border0) {
	this.border0 = border0;
	update();
}
public void setBorder1(float border1) {
	this.border1 = border1;
	update();
}
public void setPadding(float padding) {
	this.padding = padding;
	update();
}
public float segSize(){return (size_-(N-1)*padding)/N;}
public float at(int idx){
	return t_ + (idx+.5f)*segSize() + idx*padding;
}

private void update(){
	t_ = t+border0;
	size_ = size - (border0+border1);
}
}
