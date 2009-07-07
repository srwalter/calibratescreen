package org.isageek.dasbrennen.CalibrateScreen;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class Calibrator implements OnTouchListener {
	/* These are the "screen" (240x320) coordinates of the targets */
	private final int TL_X = 60;
	private final int TL_Y = 80;
	private final int BR_X = 180;
	private final int BR_Y = 240;
	
	private Context ctx;
	private int width;
	private int height;
	
	private class XY {
		public int x;
		public int y;
	}
	XY topleft;
	XY bottomright;
	
	public Calibrator (Context ctx, int width, int height) {
		this.ctx = ctx;
		this.width = width;
		this.height = height;
		
		topleft = null;
		bottomright = null;
	}

	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getRawX();
		float y = event.getRawY();
		
		int duration = Toast.LENGTH_SHORT;
		CharSequence msg = "touch " + String.valueOf(x) + " " + String.valueOf(y);
		Toast t = Toast.makeText(ctx, msg, duration);
		t.show();
		
		XY coord = new XY();
		coord.x = (int) x;
		coord.y = (int) y;
		
		if (x < width/2 && y < height/2) {
			this.topleft = coord;
		} else {
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
			e1 = e1 * (curmax - curmin) / max;
			e1 = e1 + curmin;
			e2 = e2 * (curmax - curmin) / max;
			e2 = e2 + curmin;
			
			/* Get our linear equation */
			float m = (e2 - e1) / (t2 - t1);
			float b = e1 - m*t1;
			
			if (b < 0)
				b = 0;
			
			new_min = (int)b;
			new_max = (int)(m * max + b);
		}
	}

	private void recalibrate() {
		if (topleft == null)
			return;
		if (bottomright == null)
			return;
		
		/* Compute XMIN and XMAX */
		CalibrationValues old_cv = CalibrationValues.createFromSysfs();
		int xmin = old_cv.xmin;
		int xmax = old_cv.xmax;
		
		topleft.x = topleft.x * 240 / width;
		bottomright.x = bottomright.x * 240 / width;
		ComputeMinMax cmm = new ComputeMinMax(xmin, xmax, topleft.x, bottomright.x, TL_X, BR_X, 240);
			
		CalibrationValues cv = new CalibrationValues ();
		cv.xmin = cmm.new_min;
		cv.xmax = cmm.new_max;
		
		/* Compute YMIN and YMAX */
		int ymin = old_cv.ymin;
		int ymax = old_cv.ymax;
		
		topleft.y = topleft.y * 320 / height;
		bottomright.y = bottomright.y * 320 / height;
		cmm = new ComputeMinMax(ymin, ymax, topleft.y, bottomright.y, TL_Y, BR_Y, 320);
		
		cv.ymin = cmm.new_min;
		cv.ymax = cmm.new_max;
		cv.writeToSysfs();
		
		int duration = Toast.LENGTH_LONG;
		CharSequence msg = "Recalibrated!";
		Toast t = Toast.makeText(ctx, msg, duration);
		t.show();
		
		topleft = null;
		bottomright = null;
	}

}