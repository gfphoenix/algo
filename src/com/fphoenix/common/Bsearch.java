package com.fphoenix.common;

public class Bsearch {
	
// if ret==data then all values are less than or equal to key
public static int b_min(float []data, float key){
	int len = data.length;
	if(data[len-1]<=key) return len;
	int l=0, r=len-1;
	while(l<=r){
		int m = (r+l)/2;
		float v = data[m];
		if(v<=key){
			l = m+1;
		}else{
			r = m-1;
		}
	}
	return l;
}
public static int be_min(float []data, float key){
	int len = data.length;
	if(data[len-1]<key) return len;
	int l=0, r=len-1;
	while(l<=r){
		int m = (r+l)/2;
		float v = data[m];
		if(v<key){
			l = m+1;
		}else{
			r = m-1;
		}
	}
	return l;
}
public static int l_max(float []data, float key){
	if(data[0]>=key) return -1;
	int l=0, r=data.length-1;
	while(l<=r){
		int m = (r+l)/2;
		float v = data[m];
		if(v>=key){
			r = m-1;
		}else{
			l = m+1;
		}
	}
	return r;
}
}
