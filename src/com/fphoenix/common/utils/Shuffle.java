package com.fphoenix.common.utils;

public class Shuffle {
	public static int random(int n){
		return (int)(Math.random()*n);
	}
	public static <T> void swap(T []arr, int i, int j){
		T tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
	public static <T> void shuffle(T []arr, int startI, int endI){
		if(startI > endI || startI<0 || endI>=arr.length)
			return ;
		int N = endI - startI + 1;
		int end = endI;
		while(N>1){
			swap(arr, startI + random(N), end);
			N--;
			end--;
		}
	}
	public static <T> void shuffle(T []arr){
		int len = arr.length;
		while(len>1){
			swap(arr, random(len), len-1);
			len--;
		}
	}
	public static void shuffle(char []arr){
		int len = arr.length;
		while(len>1){
			swap(arr, random(len), len-1);
			len--;
		}
	}
	public static void swap(char[] arr, int i, int j) {
		// TODO Auto-generated method stub
		char tmp = arr[i];
		arr[i]	= arr[j];
		arr[j]	= tmp;
	}
	
	public static void shuffle(int []arr){
		int len = arr.length;
		while(len>1){
			swap(arr, random(len), len-1);
			len--;
		}
	}
	public static void swap(int[] arr, int i, int j) {
		// TODO Auto-generated method stub
		int tmp = arr[i];
		arr[i]	= arr[j];
		arr[j]	= tmp;
	}
	public static void shuffle(float []arr){
		int len = arr.length;
		while(len>1){
			swap(arr, random(len), len-1);
			len--;
		}
	}
	public static void swap(float[] arr, int i, int j) {
		// TODO Auto-generated method stub
		float tmp = arr[i];
		arr[i]	= arr[j];
		arr[j]	= tmp;
	}
}
