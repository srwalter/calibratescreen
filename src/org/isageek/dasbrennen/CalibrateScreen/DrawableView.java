package org.isageek.dasbrennen.CalibrateScreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

public class DrawableView extends View {
	private ShapeDrawable[] d;

	public DrawableView(Context context, ShapeDrawable[] d) {
		super(context);
		this.d = d;
	}

	public void onDraw(Canvas c) {
		for (int i=0; i<this.d.length; i++) {
			this.d[i].draw(c);
		}
	}
}
