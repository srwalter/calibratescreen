package org.isageek.dasbrennen.CalibrateScreen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CalibrateScreen extends Activity {
	DrawableView dv;
	
	public boolean onCreateOptionsMenu (Menu menu) {
		menu.add(0, 1, 0, "Restore saved values");
		menu.add(0, 2, 0, "Run pointer location");
		menu.add(0, 3, 0, "Save current values");
		return true;
	}
	
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			RestoreCalibration.restore(this);
			return true;
			
		case 2:
			Intent i = new Intent(Intent.ACTION_RUN);
			i.setClassName("com.android.development", "com.android.development.PointerLocation");
			this.startActivity(i);
			return true;
			
		case 3:
			CalibrateDBAdapter adapter = new CalibrateDBAdapter(this);
			try {
				adapter.open();
				CalibrationValues cv = CalibrationValues.createFromSysfs();
				adapter.setValues(cv);
				adapter.close();
			} catch (SQLiteException e) {
				int duration = Toast.LENGTH_LONG;
				CharSequence msg = "Unable to save values " + e.toString();
				Toast t = Toast.makeText(this, msg, duration);
				t.show();
			}
			return true;
		}
		return false;
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        						  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);        
        Display d = wm.getDefaultDisplay();
        
        ShapeDrawable l[] = new ShapeDrawable[2];
        
        /* Draw the "targets" */
        float dotsize = 5 * 240 / d.getWidth();
        ShapeDrawable sd1 = new ShapeDrawable(new OvalShape());
        sd1.getPaint().setColor(0xffff0000);
        int x = d.getWidth()/4;
        int y = d.getHeight()/4;
        sd1.setBounds((int)(x-dotsize/2), (int)(y-dotsize/2), (int)(x+dotsize/2), (int)(y+dotsize/2));
        l[0] = sd1;
        
        ShapeDrawable sd2 = new ShapeDrawable(new OvalShape());
        sd2.getPaint().setColor(0xffff0000);
        x = d.getWidth()*3/4;
        y = d.getHeight()*3/4;
        sd2.setBounds(x, y, (int)(x+dotsize), (int)(y+dotsize));
        l[1] = sd2;
        
        dv = new DrawableView(this, l);
        Calibrator c = new Calibrator(this, d.getWidth(), d.getHeight());
        dv.setOnTouchListener(c);
        setContentView(dv);
    }
}