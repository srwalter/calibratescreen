package org.isageek.dasbrennen.CalibrateScreen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CalibrateScreen extends Activity {
	DrawableView dv;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        if (d.getWidth() != 320 || d.getHeight() != 480) {
    		int duration = Toast.LENGTH_LONG;
    		CharSequence msg = "Must be run at 320x480!";
    		Toast t = Toast.makeText(this, msg, duration);
    		t.show();
    		return;
        }
        
        ShapeDrawable l[] = new ShapeDrawable[2];
        
        /* Draw the "targets" */
        ShapeDrawable sd1 = new ShapeDrawable(new OvalShape());
        sd1.getPaint().setColor(0xffff0000);
        sd1.setBounds(100,100,110,110);
        l[0] = sd1;
        
        ShapeDrawable sd2 = new ShapeDrawable(new OvalShape());
        sd2.getPaint().setColor(0xffff0000);
        sd2.setBounds(200,320,210,330);
        l[1] = sd2;
        
        dv = new DrawableView(this, l);
        Calibrator c = new Calibrator(this);
        dv.setOnTouchListener(c);
        setContentView(dv);
    }
}