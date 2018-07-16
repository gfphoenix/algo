package com.fphoenix.common.utils;

import com.badlogic.gdx.math.MathUtils;

public class Selector {
	public static interface SelectOp {
		int getSize();
		float getWeight(int index);// weight must be >=0
	}
	public static class IndexHelper implements SelectOp{
		float []p;
		public IndexHelper(float []p){
			this.p = p;
		}
		@Override
		public int getSize() {
			return p.length;
		}
		@Override
		public float getWeight(int index) {
			return p[index];
		}
	}
	SelectOp op;
	float []acc;
	public Selector(float []weight){
		reset(new IndexHelper(weight));
	}
	public Selector(SelectOp op){
		reset(op);
	}
	public void reset(SelectOp op){
		this.op = op;
		go();
	}
	void go(){
		int n = op.getSize();
		acc = new float[n];
		float a=0f;
		for(int i=0; i<n; i++){
			a += op.getWeight(i);
			acc[i] = a;
		}
	}
	float getSum(){
		return acc[acc.length-1];
	}
	public static int select(SelectOp op){
		return new Selector(op).select();
	}
	public int select(){
		float s = getSum();
		float x = MathUtils.random(s);
		int n = acc.length;
		for(int i=0; i< n; i++){
			if(x < acc[i])
				return i;
		}
		return n-1;
	}
}
