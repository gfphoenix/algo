package com.fphoenix.common.utils;

public class FreqUtil {
	private double []acc;
	private double sum;
	public FreqUtil(double []freq){

		calSum(freq);
	}
	public FreqUtil(){
		acc = null;
	}
	private void calSum(double []freqs){
		acc = new double[freqs.length];

		acc[0] = freqs[0];
		for(int i=1; i<freqs.length; i++){
			acc[i] = acc[i-1] + freqs[i];
		}
		sum = acc[acc.length-1];
	}
	private int findMinGe(double v){
		int i=0;
		int j=acc.length-1;
		while(i<=j){
			int m = (i+j)/2;
			if(acc[m]<v)
				i = m+1;
			else
				j = m-1;
		}
		if(i>=acc.length)
			i = acc.length-1;
		return i;
	}
	public int getRandom(){
		double v = Math.random() * sum;
		int idx = findMinGe(v);
		// prevent array out of index exception caused by calculating error
		if(idx>=acc.length)
			return acc.length-1;
		return idx;
	}
}
