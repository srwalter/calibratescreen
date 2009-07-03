package org.isageek.dasbrennen.CalibrateScreen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class CalibrateScreen extends Activity {
	DrawableView dv;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShapeDrawable l[] = new ShapeDrawable[2];
        
        ShapeDrawable sd1 = new ShapeDrawable(new OvalShape());
        sd1.getPaint().setColor(0xffff0000);
        sd1.setBounds(10,10,20,20);
        l[0] = sd1;
        
        ShapeDrawable sd2 = new ShapeDrawable(new OvalShape());
        sd2.getPaint().setColor(0xffff0000);
        sd2.setBounds(290,440,300,450);
        l[1] = sd2;
        
        dv = new DrawableView(this, l);
        setContentView(dv);
        final Context ctx = (Context)this;
        
        android.view.View.OnClickListener cl = new android.view.View.OnClickListener() {
        	public void onClick(View v) {
        		CharSequence msg = "click!";
        		int duration = Toast.LENGTH_LONG;
        		Toast t = Toast.makeText(ctx, msg, duration);
        		t.show();
        	}
        };
        //dv.setOnClickListener(cl);
        
        android.view.View.OnTouchListener tl = new android.view.View.OnTouchListener() {
   			public boolean onTouch(View v, MotionEvent event) { 
   				int duration = Toast.LENGTH_LONG;
   				
   				float x = event.getX();
   				float y = event.getY();
   				
   				CharSequence msg = "touch " + String.valueOf(x) + " " + String.valueOf(y);
   				Toast t = Toast.makeText(ctx, msg, duration);
   				t.show();

				return false;
			}
        };
        dv.setOnTouchListener(tl);
        
        CharSequence msg = "toast!";
		int duration = Toast.LENGTH_LONG;
		Toast t = Toast.makeText(ctx, msg, duration);
		t.show();
    }
}