package com.fphoenix.common.action;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.DelegateAction;

public class RepeatTimeAction  extends DelegateAction {
	private float duration;
	private float elapsed;
		private boolean finished;

		protected boolean delegate (float delta) {
			if (elapsed>=duration) return true;
			elapsed += delta;
			if (action.act(delta)) {
				if (finished) return true;
				if (elapsed>=duration) return true;
				if (action != null) action.restart();
			}
			return false;
		}

		/** Causes the action to not repeat again. */
		public void finish () {
			finished = true;
		}

		public void restart () {
			super.restart();
			elapsed = 0;
			finished = false;
		}
		public RepeatTimeAction(Action a, float duration){
			setAction(a);
			this.duration = duration;
			this.elapsed = 0;
			this.finished = false;
		}
}
