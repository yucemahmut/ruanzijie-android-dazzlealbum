package cn.ancore.dazzlealbum.components.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import cn.ancore.dazzlealbum.components.log.Logger;
import cn.ancore.dazzlealbum.components.model.Album;

/**
 * 媒体库操作类
 * @author magicruan
 * @version 1.0.0 2012-12-25
 */
public class MediaDatabaseHandler {

	private static final String TAG = MediaDatabaseHandler.class.getSimpleName();
	
	public static ArrayList<Album> queryAlbumFromMediaStore(Context context){
		ArrayList<Album> result = new ArrayList<Album>();
		
		String[] projection = {MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "COUNT(*) AS photo_count"};
		String selection = "0=0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;
		String orderBy = MediaStore.Images.Media.BUCKET_ID + " ASC";
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				projection, selection, null, orderBy);
		
		if(cursor.moveToFirst()){
			do{
				print(cursor);
			}while(cursor.moveToNext());
		}
		return result;
	}
	
	private static void print(Cursor cursor){
		String[] columnNames = cursor.getColumnNames();
		for(String colName : columnNames){
			String data = cursor.getString(cursor.getColumnIndex(colName));
			Logger.getInstance().d(TAG, "======= columnName:"+colName+"   value:"+data);
		}
	}
	
}
