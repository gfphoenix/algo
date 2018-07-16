package com.fphoenix.common;


public class InputUtils {
	private static long backTs=0;
	private static boolean ok=false;
	public static void setBack(){
		ok = true;
		backTs = System.currentTimeMillis();
	}
	public static boolean isBackKeyOK(){
		if(ok){
			long last = backTs;
			long cur = System.currentTimeMillis();
			long d = cur - last;
			ok = false;
			return d >=0 && d<1000;
		}
		return false;
	}
}
