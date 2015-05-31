package com.example.contentprovider;

import java.util.Arrays;
import java.util.HashSet;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class MyContentProvider extends ContentProvider {
	MyDatabase database;
	private static final int STUDENTS = 1;
	private static final int STUDENT_ID = 2;
	private static final String AUTHORITY = "com.example.contentprovider.provider" +
			"";
	private static final String BASE_PATH = "students";
	public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/students";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/student";
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		Log.d("DEB", "MyContentProvider - static portion is called");
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, STUDENTS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH+"/#", STUDENT_ID);
		Log.d("DEB", "MyContentProvider - end of static portion is called");
	}
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.d("DEB", "MyContentProvider - onCreate is called");
		database = new MyDatabase(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Log.d("DEB", "MyContentProvider - query is called");
		checkColumns(projection);
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(MyDatabase.TABLE_NAME);
		switch(sURIMatcher.match(uri)){
		case STUDENTS:
			Log.d("DEB", "MyContentProvider->query->STUDENTS-> uri="+uri.toString());			
			break;
		case STUDENT_ID:
			Log.d("DEB", "MyContentProvider->query->STUDENTS_ID-> uri="+uri.toString());
			queryBuilder.appendWhere(MyDatabase.COLUMN_ID+"="+uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		 if (sortOrder == null || sortOrder == ""){
	         /** 
	          * By default sort on student names
	          */
	         sortOrder = MyDatabase.COLUMN_ID;
	         
	      }
		 SQLiteDatabase sqlDBase = database.getWritableDatabase();
	      Cursor cur = queryBuilder.query(sqlDBase, projection, selection, selectionArgs, null, null, sortOrder);
	      /** 
	       * register to watch a content URI for changes
	       */
	      cur.setNotificationUri(getContext().getContentResolver(), uri);
	      return cur;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		Log.d("DEB", "MyContentProvider - getType is called");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Log.d("DEB", "MyContentProvider - insert is called");
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long rowID = sqlDB.insert(MyDatabase.TABLE_NAME, null, values);
		if (rowID > 0) {
			Uri uri_1 = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri_1, null);
			return uri_1;
		}
		 throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.d("DEB", "MyContentProvider - delete is called");
		int count = 0;
	    switch (sURIMatcher.match(uri)){
	      case STUDENTS:
	         Toast.makeText(getContext(), "Entire table delete not allowed", Toast.LENGTH_SHORT).show();
	         break;
	      case STUDENT_ID:
	         String id = uri.getPathSegments().get(1);
	         Log.d("DEB", "MyContentProvider - Record to be deleted for id = "+id);
	         SQLiteDatabase sqlDBdel = database.getWritableDatabase();
	         if(TextUtils.isEmpty(selection)){
	        	 Log.d("DEB", "MyContentProvider - Selection is empty. Selection="+selection);
	        	 count = sqlDBdel.delete(MyDatabase.TABLE_NAME, MyDatabase.COLUMN_ID +  " = "+id,null);
	         }
	         else {
	        	 Log.d("DEB", "MyContentProvider - Selection is NOT empty. Selection="+selection);
	        	 count = sqlDBdel.delete(MyDatabase.TABLE_NAME, MyDatabase.COLUMN_ID + "=" +id + 
	        			 " and " + selection, selectionArgs);
	         }
	         break;
	      default: 
	         throw new IllegalArgumentException("Unknown URI " + uri);
	      }
	      Log.d("DEB", "MyContentProvider - Record Deleted. Count="+count);
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		Log.d("DEB", "MyContentProvider - update is called");
		int count = 0;   
	      switch (sURIMatcher.match(uri)){
	      case STUDENTS:
	    	  Toast.makeText(getContext(), "Entire table update not allowed", Toast.LENGTH_SHORT).show();
	         break;
	      case STUDENT_ID:
	    	  String id = uri.getPathSegments().get(1);
		         Log.d("DEB", "MyContentProvider - Record to be updated for id = "+id);
		         SQLiteDatabase sqlDBupd = database.getWritableDatabase();
		         if(TextUtils.isEmpty(selection)){
		        	 Log.d("DEB", "MyContentProvider - Selection is empty. Selection="+selection);
		        	 count = sqlDBupd.update(MyDatabase.TABLE_NAME, values, MyDatabase.COLUMN_ID +  " = "+id,null);
		         }
		         else {
		        	 Log.d("DEB", "MyContentProvider - Selection is NOT empty. Selection="+selection);
		        	 count = sqlDBupd.update(MyDatabase.TABLE_NAME, values, MyDatabase.COLUMN_ID + "=" +id + 
		        			 " and " + selection, selectionArgs);
		         }
		         break;
	      default: 
	         throw new IllegalArgumentException("Unknown URI " + uri );
	      }
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
	}
	
	// CheckColumns - check to ensure that the requested columns are valid column names
	private void checkColumns(String[] projection) {
		Log.d("DEB", "MyContentProvider - checkColumns is called");
	    String[] available = {MyDatabase.COLUMN_ID,MyDatabase.COLUMN_NAME,
	        MyDatabase.COLUMN_CLASS };
	    if (projection != null) {
	      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
	      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
	      // check if all columns which are requested are available
	      if (!availableColumns.containsAll(requestedColumns)) {
	        throw new IllegalArgumentException("Unknown columns in projection");
	      }
	      else {
	    	  Log.d("DEB", "MyContentProvider->checkColumn-> all columns are valid");
	      }
	    }
	  }
}
