package com.esotericsoftware.spine;

public class MyUevent implements Comparable<MyUevent> {
	float percent;
	String animation;
	Object userData;

	public MyUevent(){}
	public MyUevent(String animation, float percent, Object o) {
		reset(animation, percent, o);
	}
	public MyUevent reset(String animation, float percent, Object o) {
		this.animation = animation;
		this.percent = percent;
		this.userData = o;
		return this;
	}
    public float getPercent() {
        return percent;
    }
    public String getAnimation() {
        return animation;
    }

    public Object getUserData() {
        return userData;
    }

    // true means to remove this event hook
	public boolean invoke(int i, Object o) {
		return true;
	}

	@Override
	public int compareTo(MyUevent o) {
		float df = percent - o.percent;
		return df > 0 ? 1 : (df < 0 ? -1 : 0);
	}
}
