package org.isageek.dasbrennen.CalibrateScreen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalibrateDBAdapter {
	private static final String DATABASE_NAME = "calvalues";
	private static final int DATABASE_VERSION = 1;
	private SQLiteDatabase db;
	
	private Context ctx;
	
	private class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context ctx) {
			super (ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table data (xmin integer, xmax integer, ymin integer, ymax integer);");			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}
	private DatabaseHelper helper;
	
	public CalibrateDBAdapter (Context ctx) {
		this.ctx = ctx;
	}
	
	public CalibrateDBAdapter open() throws SQLException {
		helper = new DatabaseHelper(ctx);
		db = helper.getWritableDatabase();
		return this;
	}
	
	public void close () {
		helper.close();
	}
	
	public void setValues (CalibrationValues cb) {
		ContentValues cv = new ContentValues();
		cv.put("xmin", cb.xmin);
		cv.put("xmax", cb.xmax);
		cv.put("ymin", cb.ymin);
		cv.put("ymax", cb.ymax);
		
		db.execSQL("delete from data;");
		db.insert("data", null, cv);
	}
	
	public CalibrationValues getValues () {
		Cursor c = db.query("data", new String [] {"xmin", "xmax", "ymin", "ymax"}, null,
				null, null, null, null);
		
		if (c.getCount() < 1)
			return null;
		
		CalibrationValues cv = new CalibrationValues();
		c.moveToFirst();
		cv.xmin = c.getInt(0);
		cv.xmax = c.getInt(1);
		cv.ymin = c.getInt(2);
		cv.ymax = c.getInt(3);
		
		return cv;
	}
}
