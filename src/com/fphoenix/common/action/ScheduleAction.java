package com.fphoenix.common.action;

import com.badlogic.gdx.scenes.scene2d.Action;

// 定时，固定时间间隔地执行任务
public class ScheduleAction extends Action {
	float delay;
	float interval=0;
	float elapsed;
	float tmp=0;
	int count = 0;
	int repeat = -1; // 默认永久
	ScheduleUnit su;
	public ScheduleAction(ScheduleUnit su){
		this.delay = 0;
		this.elapsed = 0;
		this.tmp = interval;
		this.su = su;
	}
	public ScheduleAction(ScheduleUnit su, float delay, float interval){
		this.su = su;
		this.delay = Math.max(0, delay);
		this.interval = interval;
		this.elapsed = 0;
		this.tmp = interval;
	}
	public ScheduleAction(ScheduleUnit su, float delay, float interval, int repeat){
		this(su, delay, interval);
		this.repeat = repeat;
	}
	@Override
	public boolean act(float delta) {
		boolean ok=false;
		elapsed += delta;
		if(elapsed<delay+delta)
			return false;
		tmp += delta;
		if(tmp<interval){
			return false;
		}
		tmp -= interval;
		if(su!=null)
			ok = su.schedule(count);
		count++;
		ok = ok || (repeat>0 && count>=repeat);
		return ok;
	}
}
