package cn.ancore.dazzlealbum.commons;

import java.io.File;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.StatFs;
import cn.ancore.dazzlealbum.components.exception.DefaultUncaughtExceptionHandler;
import cn.ancore.dazzlealbum.components.log.Logger;
/**
 * 应用application
 * 
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class DazzleAlbumApplication extends Application {

	private static final String TAG = DazzleAlbumApplication.class
			.getSimpleName();

	private static SharedPreferences mAppPreferences = null;
	private static DazzleAlbumApplication mInstance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mAppPreferences = getSharedPreferences(Config.APP_NAME,
				Activity.MODE_PRIVATE);
		mAppPreferences.edit().commit();

		Settings.getInstance().setSdkVersion(android.os.Build.VERSION.SDK_INT);
		new MediaCardStateBroadcastReceiver().register();
		Thread.setDefaultUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler());
		initAppVersion();
		readSystem();
	}

	public SharedPreferences getAppPreferences() {
		return mAppPreferences;
	}

	public static Context getContext() {
		if (mInstance != null) {
			return mInstance.getApplicationContext();
		} else {
			return null;
		}
	}

	private void initAppVersion() {
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			Settings.getInstance().setAppVersion(pInfo.versionName);
			Settings.getInstance().setAppVersionCode(pInfo.versionCode);
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			Logger.getInstance().w(TAG, e.toString(), e);
		}
	}

	private class MediaCardStateBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("android.intent.action.MEDIA_UNMOUNTED".equals(intent
					.getAction())) {
				Settings.getInstance().setSDCardAvailable(false);
			} else if ("android.intent.action.MEDIA_MOUNTED".equals(intent
					.getAction())) {
				Settings.getInstance().setSDCardAvailable(true);
			}
		}

		public void register() {
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
			intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
			intentFilter.addDataScheme("file");
			DazzleAlbumApplication.this.registerReceiver(this, intentFilter);
		}
	}

	private void readSystem() {
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long blockCount = sf.getBlockCount();
		long availCount = sf.getAvailableBlocks();
		boolean vast = false;
		if (availCount * blockSize > 200 * 1024 * 1024) {
			vast = true;
			Settings.getInstance().setSystemMemoryVast(vast);
		}
		Logger.getInstance().d(
				TAG,
				"Internal block size:" + blockSize + ",block num:" + blockCount
						+ ",total:" + blockSize * blockCount / 1024 + "KB");
		Logger.getInstance().d(
				TAG,
				"Internal available block num:" + availCount
						+ ",available size:" + availCount * blockSize / 1024
						+ "KB");
	}

}