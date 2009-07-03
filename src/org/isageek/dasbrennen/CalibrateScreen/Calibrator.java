package org.isageek.dasbrennen.CalibrateScreen;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Calibrator implements OnTouchListener {
	private final int TL_X = 0;
	private final int TL_Y = 0;
	private final int BR_X = 0;
	private final int BR_Y = 0;
	
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
	
	private class ComputeMinMax {
		public int new_min;
		public int new_max;
		
		public ComputeMinMax(float curmin, float curmax, float e1, float e2, float t1, float t2, float max) {
			/* Convert coordinate to touch values */
			e1 = e1 * (curmax - curmin);
			e1 = e1 + curmin;
			e2 = e2 * (curmax - curmin);
			e2 = e2 + curmin;
			
			/* Get our linear equation */
			float m = (e2 - e1) / (t2 - t1);
			float b = e1 - m*t1;
			
			new_min = (int)b;
			new_max = (int)(m * max + b);
		}
	}
	
	private void recalibrate() {
		if (topleft == null)
			return;
		if (bottomright == null)
			return;
		
		int duration = Toast.LENGTH_LONG;
		CharSequence msg = "Recalibrated!";
		Toast t = Toast.makeText(ctx, msg, duration);
		t.show();
	
		/* XXX: read from sysfs */
		int xmin = 100;
		int xmax = 600;
		
		topleft.x = topleft.x * 240 / 320;
		bottomright.x = bottomright.x * 240 / 320;
		ComputeMinMax cmm = new ComputeMinMax(xmin, xmax, topleft.x, bottomright.x, TL_X, BR_X, 240);
		xmin = cmm.new_min;
		xmax = cmm.new_max;
		
		/* XXX: write to sysfs */
		
		int ymin = 50;
		int ymax = 1000;
		
		topleft.y = topleft.y * 320 / 480;
		bottomright.y = bottomright.y * 320 / 480;
		cmm = new ComputeMinMax(ymin, ymax, topleft.y, bottomright.y, TL_Y, BR_Y, 320);
		ymin = cmm.new_min;
		ymax = cmm.new_max;
		
		/* XXX: write to sysfs */
	}

}
