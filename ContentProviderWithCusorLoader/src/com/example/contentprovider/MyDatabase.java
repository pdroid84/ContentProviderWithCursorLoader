package com.example.contentprovider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class MyDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "DEBASHIS_STUDENT_DB";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "Students_Data";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "studentName";
	public static final String COLUMN_CLASS = "class";
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
			" (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME +
			" VARCHAR(255), " + COLUMN_CLASS + " VARCHAR(255))";
	private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	private Context context;
	
	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Toast.makeText(context, "MyDatabase - MyDatabase Constructor is called", Toast.LENGTH_LONG).show();
		Log.d("DEB", "MyDatabase - MyDatabase Constructor is called");
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL(CREATE_TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(context, "MyDatabase - OnCreate is called", Toast.LENGTH_LONG).show();
		Log.d("DEB", "MyDatabase - OnCreate is called");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		try {
			db.execSQL(DROP_TABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toast.makeText(context, "MyDatabase - OnUpgrade is called."+ "DB Old ver="+oldVersion+" ,DB New ver="+newVersion, Toast.LENGTH_LONG).show();
		Log.d("DEB", "MyDatabase - OnUpgrade is called."+ "DB Old ver="+oldVersion+" ,DB New ver="+newVersion);
		onCreate(db);
	}
	@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
			// TODO Auto-generated method stub
			//super.onDowngrade(db, oldVersion, newVersion);
			Toast.makeText(context, "MyDatabase - OnDowngrade is called."+ "DB Old ver="+oldVersion+" ,DB New ver="+newVersion, Toast.LENGTH_LONG).show();
			Log.d("DEB", "MyDatabase - OnDowngrade is called."+ "DB Old ver="+oldVersion+" ,DB New ver="+newVersion);
		}
}

