package com.lmn.Arbiter_Android.DatabaseHelpers.TableHelpers;

import com.lmn.Arbiter_Android.BaseClasses.Layer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class FailedSync implements BaseColumns{
	public static final String TABLE_NAME = "failed_sync";
	public static final String KEY = "key";
	public static final String DATA_TYPE = "data_type";
	public static final String SYNC_TYPE = "sync_type";
	public static final String LAYER_ID = "layer_id";
	
	// vector data upload, download and media download
	// media upload is already handled by mediaToSend
	// in the preferences table
	public static class DataType {
		public static final int VECTOR = 0;
		public static final int MEDIA = 1;
	}
	
	public static class SyncType {
		public static final int UPLOAD = 0;
		public static final int DOWNLOAD = 1;
	}
	
	private FailedSync(){}
	
	private static FailedSync helper = null;
	
	public static FailedSync getHelper(){
		if(helper == null){
			helper = new FailedSync();
		}
		
		return helper;
	}
	
	public void createTable(SQLiteDatabase db){
		String sql = "CREATE TABLE " + TABLE_NAME + " (" +
					_ID +
					" INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY + " TEXT, " +
					DATA_TYPE + " INTEGER, " +
					SYNC_TYPE + " INTEGER, " + 
					LAYER_ID + " INTEGER);";
		
		db.execSQL(sql);
	}
	
	private String getFeatureType(SQLiteDatabase db, int layerId){
		
		Log.w("FailedSync", "FailedSync layerId = " + layerId);
		Layer layer = LayersHelper.getLayersHelper().get(db, layerId);
		String featureType = null;
		
		if(layer != null){
			featureType = layer.getFeatureTypeNoPrefix();	
		}
		
		
		return featureType;
	}
	
	public String[] getFailedVectorUploads(SQLiteDatabase db){
		
		String selection = DATA_TYPE + "=? AND " + SYNC_TYPE + "=?";
		
		String[] columns = {
			LAYER_ID
		};
		
		String[] selectionArgs = {
			Integer.toString(DataType.VECTOR),
			Integer.toString(SyncType.UPLOAD)
		};
		
		Cursor cursor = db.query(TABLE_NAME, columns, selection,
				selectionArgs, null, null, null);
		
		String[] results = null;
		
		int count = cursor.getCount();
		
		if(count > 0){
			results = new String[cursor.getCount()];
			
			int i = 0;
			
			int layerId = -1;
			
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				layerId = cursor.getInt(0);
				
				results[i++] = getFeatureType(db, layerId);
			}
		}
		
		cursor.close();
		
		return results;
	}
	
	public String[] getFailedVectorDownloads(SQLiteDatabase db){
		
		String selection = DATA_TYPE + "=? AND " + SYNC_TYPE + "=?";
		
		String[] columns = {
			LAYER_ID
		};
		
		String[] selectionArgs = {
			Integer.toString(DataType.VECTOR),
			Integer.toString(SyncType.DOWNLOAD)
		};
		
		Cursor cursor = db.query(TABLE_NAME, columns, selection,
				selectionArgs, null, null, null);
		
		String[] results = null;
		
		int count = cursor.getCount();
		
		if(count > 0){
			results = new String[cursor.getCount()];
			
			int i = 0;
			
			int layerId = -1;
			
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				layerId = cursor.getInt(0);
				
				results[i++] = getFeatureType(db, layerId);
			}
		}
		
		cursor.close();
		
		return results;
	}

	public String[] getFailedMediaDownloads(SQLiteDatabase db){
	
		String selection = DATA_TYPE + "=? AND " + SYNC_TYPE + "=?";
		
		String[] columns = {
			KEY
		};
		
		String[] selectionArgs = {
			Integer.toString(DataType.MEDIA),
			Integer.toString(SyncType.DOWNLOAD)
		};
		
		Cursor cursor = db.query(TABLE_NAME, columns, selection,
				selectionArgs, null, null, null);
		
		String[] results = null;
		
		int count = cursor.getCount();
		
		if(count > 0){
			results = new String[cursor.getCount()];
			
			int i = 0;
			
			String key = null;
			
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				key = cursor.getString(0);
				
				results[i++] = key;
			}
		}
		
		cursor.close();
		
		return results;
	}
}
