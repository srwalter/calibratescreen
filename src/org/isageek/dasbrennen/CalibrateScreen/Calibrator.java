package org.isageek.dasbrennen.CalibrateScreen;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Calibrator implements OnTouchListener {
	private Context ctx;
	
	private class XY {
		public int x;
		public int y;
	}
	XY topleft;
	XY bottomright;
	
	public Calibrator (Context ctx) {
		this.ctx = ctx;
		
		topleft = null;
		bottomright = null;
	}

	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getRawX();
		float y = event.getRawY();
		
		XY coord = new XY();
		coord.x = (int) x;
		coord.y = (int) y;
		
		if (x < 160 && y < 240) {
			this.topleft = coord;
		} else if (x > 160 && y > 240) {
			this.bottomright = coord;
		}
		recalibrate();
		
		return false;
	}
	
	private void recalibrate() {
		if (topleft == null)
			return;
		if (bottomright == null)
			return;
		
		int duration = Toast.LENGTH_LONG;
		CharSequence msg = "recalibrate!";
		Toast t = Toast.makeText(ctx, msg, duration);
		t.show();
	}

}
