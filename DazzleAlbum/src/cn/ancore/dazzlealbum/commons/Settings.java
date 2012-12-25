package cn.ancore.dazzlealbum.commons;

import android.os.Build;
import android.os.Environment;

/**
 * 全局变量
 * 
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class Settings {

	private static Settings mInstance;

	// SD卡是否可用
	private boolean mSDCardAvailable = true;
	// 应用版本号
	private String mAppVersion = "";
	// 应用版本号代码
	private int mAppVersionCode;
	// 系统版本号
	private int mSdkVersion;
	// 渠道号
	private String mMarketChannel = "";
	// 是否是大容量内存
	private boolean mSystemMemoryVast;

	public static synchronized Settings getInstance() {
		if (mInstance == null) {
			mInstance = new Settings();
		}
		return mInstance;
	}

	public void setSDCardAvailable(boolean available) {
		this.mSDCardAvailable = available;
	}

	public boolean isSDCardAvailable() {
		String status = Environment.getExternalStorageState();
		return (this.mSDCardAvailable)
				&& (status.equals(Environment.MEDIA_MOUNTED));
	}

	public void setAppVersion(String version) {
		this.mAppVersion = version;
	}

	public String getAppVersion() {
		return this.mAppVersion;
	}

	public void setAppVersionCode(int version) {
		this.mAppVersionCode = version;
	}

	public int getAppVersionCode() {
		return this.mAppVersionCode;
	}

	public void setSdkVersion(int sdk) {
		this.mSdkVersion = sdk;
	}

	public int getSdkVersion() {
		return this.mSdkVersion;
	}

	public void setSystemMemoryVast(boolean vast) {
		this.mSystemMemoryVast = vast
				|| Build.MODEL.equalsIgnoreCase("GT-I9000");
	}

	public boolean isSystemMemoryVast() {
		return this.mSystemMemoryVast;
	}

	public void setMarketChannel(String marketChannel) {
		this.mMarketChannel = marketChannel;
	}

	public String getMarketChannel() {
		return this.mMarketChannel;
	}

}
