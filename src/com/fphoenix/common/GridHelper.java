package com.fphoenix.common;

public class GridHelper {
float x,y;
float w,h;
float xx_,yy_,ww_,hh_;

float borderLeft;
float borderRight;
float borderUp;
float borderDown;
float rowPadding;
float colPadding;
int row;
int col;
boolean yDown;
public GridHelper(){}
public GridHelper(float x, float y, float w, float h){
	reset(x,y,w,h);
}
public void reset(float x, float y, float w, float h){
	this.x=this.xx_=x;
	this.y=this.yy_=y;
	this.w=this.ww_=w;
	this.h=this.hh_=h;
	borderLeft=borderRight=0;
	borderDown=borderUp=0;
	row=col=1;
	yDown=true;
}
public void setRect(float x, float y, float w, float h){
	this.x=x;
	this.y=y;
	this.w=w;
	this.h=h;
}
public float getRowPadding(){return this.rowPadding;}
public float getColPadding(){return this.colPadding;}
public void setPadding(float rowPadding, float colPadding) {
	this.rowPadding = rowPadding;
	this.colPadding = colPadding;
}
public void setBorder(float left, float right, float up, float down){
	this.borderLeft		= left;
	this.borderRight	= right;
	this.borderDown		= down;
	this.borderUp		= up;
	update();
}
public float getBorderLeft() {return borderLeft;}
public void setBorderLeft(float borderLeft) {
	this.borderLeft = borderLeft;
	update();
}
public float getBorderRight() {return borderRight;}
public void setBorderRight(float borderRight) {
	this.borderRight = borderRight;
	update();
}
public float getBorderUp() {return borderUp;}
public void setBorderUp(float borderUp) {
	this.borderUp = borderUp;
	update();
}
public float getBorderDown() {
	return borderDown;
}
public void setBorderDown(float borderDown) {
	this.borderDown = borderDown;
	update();
}
public int getRow() {return row;}
public int getCol() {return col;}
public void setRow(int row){
	this.row=row;
	if(row<=0)
		this.row=1;
}
public void setCol(int col){
	this.col=col;
	if(col<=0)
		this.col = col;
}
public void setSize(int row, int col){
	this.row = row;
	this.col = col;
	if(row<=0) this.row=1;
	if(col<=0) this.col=1;
	update();
}
public boolean isyDown() {	return yDown;}
public void setyDown(boolean down){this.yDown = down;}

public float xAt(int x){
	return xx_+(x+.5f)*cellWidth() + x*colPadding;
}
public float yAt(int y){
	if(yDown)
		y = row-1-y;
	// calculate for yUP
	return yy_+(y+.5f)*cellHeight() + y*rowPadding;
}
public float cellWidth(){return (this.ww_-(col-1)*colPadding)/this.col;}
public float cellHeight(){return (this.hh_-(row-1)*rowPadding)/this.row;}

private void update(){
	this.xx_ = x+borderLeft;
	this.yy_ = y+borderDown;
	this.ww_ = w-(borderLeft+borderRight);
	this.hh_ = h-(borderDown+borderUp);
}
}
