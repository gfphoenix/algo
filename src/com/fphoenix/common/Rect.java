package com.fphoenix.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Rect {

	public float centerX(){
		return x + width/2;
	}
	public float centerY(){
		return y + height/2;
	}
	public Rect intersect(Rect r){
		return intersect(this, r, new Rect());
	}

	public Rect moveCenterTo(float cx, float cy) {
		x = cx - width/2;
		y = cy - height/2;
		return this;
	}

	public Rect resizeFixCenter(float w, float h) {
		final float dw = w - width;
		final float dh = h - height;
		x = x - dw/2;
		y = y - dh/2;
		width = w;
		height = h;
		return this;
	}
	public static Rect centerRect(float centerX, float centerY, float width, float height){
		float x = centerX - width/2;
		float y = centerY - height/2;
		return new Rect(x, y, width, height);
	}


	public float x, y;
	public float width, height;

	public Rect() {
	}
	public Rect(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public static Rect intersect(Rect a, Rect b, Rect result) {
		result.x = Math.max(a.x, b.x);
		result.y = Math.max(a.y, b.y);
		result.width = Math.min(a.x + a.width, b.x + b.width) - result.x;
		result.height = Math.min(a.y + a.height, b.y + b.height) - result.y;
		return (result.width <= 0 || result.height <= 0) ? null : result;
	}

	// result = a - b;
	public static boolean minus(Rect a, Rect b, Rect tmp, List<Rect> result) {
		Rect in = intersect(a, b, tmp);
		if (in == null) {
			result.add(a);
			return true;
		}
		if (a.x < in.x) {
			result.add(new Rect(a.x, a.y, in.x - a.x, a.height));
		}
		if (in.x + in.width < a.x + a.width) {
			float w = a.x + a.width - (in.x + in.width);
			result.add(new Rect(in.x + in.width, a.y, w, a.height));
		}
		if (a.y < in.y) {
			result.add(new Rect(in.x, a.y, in.width, in.y - a.y));
		}
		if (in.y + in.height < a.y + a.height) {
			float h = a.y + a.height - (in.y + in.height);
			result.add(new Rect(in.x, in.y + in.height, in.width, h));
		}
		return false;
	}
	public static List<Rect> minus(Rect a, List<Rect> b) {
		ArrayList<Rect> A = new ArrayList<>();
		A.add(a);
		return minus(A, b);
	}
	// L - l
	public static List<Rect> minus(List<Rect> A, List<Rect> B) {
		ArrayList<Rect> tmp = new ArrayList<Rect>(8);
		Rect tmpR = new Rect();
		for (int i=B.size()-1; i>=0; i--) {
			Rect b = B.get(i);
			tmp.clear();
			for(int k=A.size()-1; k>=0; k--){
				Rect a = A.get(k);
				minus(a, b, tmpR, tmp);
			}
			A.clear();
			A.addAll(tmp);
		}
		return A;
	}
	// output = A - rects
	public static List<Rect> minus(Rect A, Rect ...rects) {
		ArrayList<Rect> L = new ArrayList<Rect>();
		L.add(A);
		if (rects==null || rects.length==0) return L;
		ArrayList<Rect> B = new ArrayList<Rect>(rects.length);
		Collections.addAll(B, rects);
		return minus(L, B);
	}
	@Override
	public String toString() {
		return String.format(Locale.US, "(%.1f, %.1f) [%.1f x %.1f]", x, y, width, height);
	}
}
