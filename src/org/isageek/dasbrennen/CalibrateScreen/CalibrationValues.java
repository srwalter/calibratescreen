package org.isageek.dasbrennen.CalibrateScreen;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import android.widget.Toast;

public class CalibrationValues {
	public int xmin, xmax, ymin, ymax;
	
	public CalibrationValues () {
	}
	
	private static void setSysfs(String filename, int val) {
		try {
			BufferedWriter f = new BufferedWriter(new FileWriter(filename));
			f.write(String.valueOf(val) + "\n");
			f.flush();
		} catch (IOException e) {
			int duration = Toast.LENGTH_LONG;
			CharSequence msg = "write fail!";
			Toast t = Toast.makeText(null, msg, duration);
			t.show();
		}
		//int duration = Toast.LENGTH_SHORT;
		//CharSequence msg = "write " + String.valueOf(val);
		//Toast t = Toast.makeText(ctx, msg, duration);
		//t.show();
	}
	
	public void writeToSysfs() {
		setSysfs("/sys/class/vogue_ts/xmin", xmin);
		setSysfs("/sys/class/vogue_ts/xmax", xmax);
		setSysfs("/sys/class/vogue_ts/ymin", ymin);
		setSysfs("/sys/class/vogue_ts/ymax", ymax);
	}
	
	public static CalibrationValues createFromSysfs() {
		CalibrationValues cv = new CalibrationValues();
		cv.xmin = getSysfs("/sys/class/vogue_ts/xmin");
		cv.xmax = getSysfs("/sys/class/vogue_ts/xmax");
		cv.ymin = getSysfs("/sys/class/vogue_ts/ymin");
		cv.ymax = getSysfs("/sys/class/vogue_ts/ymax");
		return cv;
	}
	
	private static int getSysfs(String filename) {
		int val = 0;
		try {
			FileReader f = new FileReader(filename);
			Scanner s = new Scanner(f);
			val = s.nextInt();
		} catch (IOException e) {
			int duration = Toast.LENGTH_LONG;
			CharSequence msg = "read fail!";
			Toast t = Toast.makeText(null, msg, duration);
			t.show();
		}
		//int duration = Toast.LENGTH_SHORT;
		//CharSequence msg = "read " + String.valueOf(val);
		//Toast t = Toast.makeText(ctx, msg, duration);
		//t.show();
		return val;
	}
}
