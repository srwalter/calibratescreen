package org.isageek.dasbrennen.CalibrateScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

public class RestoreCalibration extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// XXX
		//if (intent.getAction() != android.content.Intent.ACTION_BOOT_COMPLETED)
		//	return;
		
		CalibrateDBAdapter db = new CalibrateDBAdapter(context);
		CalibrationValues cv = null;
		
		try {
			db.open();
			cv = db.getValues();
			db.close();
		} catch (SQLiteException e) {
			int duration = Toast.LENGTH_LONG;
			CharSequence msg = "Unable to read values " + e.toString();
			Toast t = Toast.makeText(context, msg, duration);
			t.show();
			return;
		}
		
		cv.writeToSysfs();
		int duration = Toast.LENGTH_LONG;
		CharSequence msg = "Boot recalibrated!";
		Toast t = Toast.makeText(context, msg, duration);
		t.show();
	}

}
