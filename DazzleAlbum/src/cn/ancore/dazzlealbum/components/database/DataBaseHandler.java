package cn.ancore.dazzlealbum.components.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import cn.ancore.dazzlealbum.commons.Config;
import cn.ancore.dazzlealbum.components.log.Logger;
import cn.ancore.dazzlealbum.components.model.Album;
import cn.ancore.dazzlealbum.components.model.Audio;
import cn.ancore.dazzlealbum.components.model.Photo;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * 数据库操作类
 * 
 * @author magicruan
 * @version 1.0.0 2012-12-25
 */
public class DataBaseHandler extends OrmLiteSqliteOpenHelper {

	private static final String TAG = DataBaseHandler.class.getSimpleName();

	private Dao<Album, Integer> mAlbumDao = null;
	private Dao<Photo, Integer> mPhotoDao = null;
	private Dao<Audio, Integer> mAudioDao = null;

	public DataBaseHandler(Context context) {
		super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Logger.getInstance().i(TAG, "Database on create");
			TableUtils.createTable(connectionSource, Album.class);
			TableUtils.createTable(connectionSource, Photo.class);
			TableUtils.createTable(connectionSource, Audio.class);
		} catch (Exception e) {
			Logger.getInstance().w(TAG, "Can't create database", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Logger.getInstance().i(TAG, "Database on upgrade");
			TableUtils.dropTable(connectionSource, Album.class, true);
			TableUtils.dropTable(connectionSource, Photo.class, true);
			TableUtils.dropTable(connectionSource, Audio.class, true);
			onCreate(db, connectionSource);
		} catch (Exception e) {
			Logger.getInstance().w(TAG, "Can't upgrade database", e);
		}
	}

	@Override
	public void close() {
		super.close();
		mAlbumDao = null;
		mPhotoDao = null;
		mAudioDao = null;
	}

	public Dao<Album, Integer> getAlbumDao() {
		try {
			if (mAlbumDao == null) {
				mAlbumDao = getDao(Album.class);
			}
			return mAlbumDao;
		} catch (SQLException e) {
			Logger.getInstance().w(TAG, e.toString(), e);
		}
		return null;
	}

	public Dao<Photo, Integer> getPhotoDao() {
		try {
			if (mPhotoDao == null) {
				mPhotoDao = getDao(Photo.class);
			}
			return mPhotoDao;
		} catch (SQLException e) {
			Logger.getInstance().w(TAG, e.toString(), e);
		}
		return null;
	}

	public Dao<Audio, Integer> getAudioDao() {
		try {
			if (mAudioDao == null) {
				mAudioDao = getDao(Audio.class);
			}
			return null;
		} catch (SQLException e) {
			Logger.getInstance().w(TAG, e.toString(), e);
		}
		return null;
	}

}
