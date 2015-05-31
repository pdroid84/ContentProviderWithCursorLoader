package com.example.contentprovider;

import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.app.LoaderManager;
import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements  LoaderManager.LoaderCallbacks<Cursor> {
	private static final int LOADER_ID = 1;
	EditText sName, sClass,sID;
	ListView lView;
	SimpleCursorAdapter sCursorAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sName = (EditText) findViewById(R.id.sName);
        sClass = (EditText) findViewById(R.id.sClass);
        sID = (EditText) findViewById(R.id.sID);
        lView = (ListView) findViewById(R.id.listView1);
        String[] mFromCols = {MyDatabase.COLUMN_ID,MyDatabase.COLUMN_NAME,MyDatabase.COLUMN_CLASS};
        int[] mToCols = {R.id.idView,R.id.nameView,R.id.classView};
        getLoaderManager().initLoader(LOADER_ID, null, this);
        sCursorAdapter = new SimpleCursorAdapter(this,R.layout.single_item_in_list,null,mFromCols,mToCols,0);
        lView.setAdapter(sCursorAdapter);
    }
    public void addData(View v) {
    		Log.d("DEB","MainActivity - addData is called");
        	ContentValues cv1 = new ContentValues();
        	cv1.put(MyDatabase.COLUMN_NAME, sName.getText().toString());
        	cv1.put(MyDatabase.COLUMN_CLASS, sClass.getText().toString());
        	Uri uri = getContentResolver().insert(MyContentProvider.CONTENT_URI, cv1);
        	Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
        	Log.d("DEB", "Data added successfully - uri ="+uri.toString()); 
        }
    public void showData(View v) {
    	// Retrieve student records
    	Log.d("DEB","MainActivity - showData is called");
    	Uri uri = null;
        String[] projection = {MyDatabase.COLUMN_ID,MyDatabase.COLUMN_NAME,MyDatabase.COLUMN_CLASS};
        if (sID.getText().toString()==null) {
        	uri = MyContentProvider.CONTENT_URI;
        }
        else {
        	uri = Uri.parse(MyContentProvider.CONTENT_URI+"/"+sID.getText().toString());
        }
        Cursor c = getContentResolver().query(uri, projection, null, null, null);
        if (c.moveToFirst()) {
           do{
              Toast.makeText(this, 
              c.getString(c.getColumnIndex(MyDatabase.COLUMN_ID)) + 
              ", " +  c.getString(c.getColumnIndex(MyDatabase.COLUMN_NAME)) + 
              ", " + c.getString(c.getColumnIndex(MyDatabase.COLUMN_CLASS)), 
              Toast.LENGTH_SHORT).show();
           } while (c.moveToNext());
        } 
     }
    public void delData(View v) {
    	// Retrieve student records
    	Log.d("DEB","MainActivity - delData is called");
    	Uri uri = null;
        if (sID.getText().toString()==null) {
        	uri = MyContentProvider.CONTENT_URI;
        }
        else {
        	uri = Uri.parse(MyContentProvider.CONTENT_URI+"/"+sID.getText().toString());
        }
        int delCount = getContentResolver().delete(uri, null, null); 
        Toast.makeText(this, "Record deleted for id "+sID.getText().toString()+" and delete count is "+delCount, Toast.LENGTH_SHORT).show();
     }
    
    public void updateData(View v) {
    	// Retrieve student records
    	Log.d("DEB","MainActivity - delData is called");
    	Uri uri = null;
        if (sID.getText().toString()==null) {
        	uri = MyContentProvider.CONTENT_URI;
        }
        else {
        	uri = Uri.parse(MyContentProvider.CONTENT_URI+"/"+sID.getText().toString());
        }
        String studentName = sName.getText().toString();
        String studentClass = sClass.getText().toString();
        ContentValues cv = new ContentValues();
        if (!TextUtils.isEmpty(studentName)){
        	cv.put(MyDatabase.COLUMN_NAME, studentName);
        }
        if (!TextUtils.isEmpty(studentClass)){
        	cv.put(MyDatabase.COLUMN_CLASS, studentClass);
        }
        if(TextUtils.isEmpty(studentName) && TextUtils.isEmpty(studentClass)){
        	Toast.makeText(this, "Either enter name or class to update", Toast.LENGTH_SHORT).show();
        }
        else {
        	int updCount = getContentResolver().update(uri, cv, null, null); 
        	Toast.makeText(this, "Record updated for id "+sID.getText().toString()+" and delete count is "+updCount, Toast.LENGTH_SHORT).show();
        }
     }
    
    @Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		Log.d("DEB","MainActivity - onCreateLoader is called");
        Uri tName = MyContentProvider.CONTENT_URI;
		String[] columns={MyDatabase.COLUMN_ID,MyDatabase.COLUMN_NAME,MyDatabase.COLUMN_CLASS};
		/*
	     * Takes action based on the ID of the Loader that's being created
	     */
	    switch (id) {
	        case LOADER_ID:
	            // Returns a new CursorLoader
	        	Log.d("DEB","Loader id is "+id);
	            return new CursorLoader(
	                        this,   // Parent activity context
	                        tName,        // Table to query (URI)
	                        columns,     // Projection to return
	                        null,            // No selection clause
	                        null,            // No selection arguments
	                        null             // Default sort order
	        );
	        default:
	            // An invalid id was passed in
	            return null;
	    }
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		/*
		 * Defines the callback that CursorLoader calls
		 * when it's finished its query
		 */
		  /*
	     * Moves the query results into the adapter, causing the
	     * ListView fronting this adapter to re-display
	     */
		Log.d("DEB","MainActivity - onLoadFinished is called");
		sCursorAdapter.changeCursor(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		/*
		 * Invoked when the CursorLoader is being reset. For example, this is
		 * called if the data in the provider changes and the Cursor becomes stale.
		 */
		/*
	     * Clears out the adapter's reference to the Cursor.
	     * This prevents memory leaks.
	     */
		Log.d("DEB","MainActivity - onLoaderReset is called");
		sCursorAdapter.changeCursor(null);
	}
}
