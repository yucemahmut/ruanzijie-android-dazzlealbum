package cn.ancore.dazzlealbum.commons;

/**
 * 应用配置常量
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class Config {
	
	public static final String APP_NAME = "DazzleAlbum";
	public static final boolean DEBUG = true;
	public static final String LOG_LEVEL = "DEBUG,ERROR,INFO,WARN";
	
	//数据库配置
	public static final String DATABASE_NAME = "dazzlealbum.db";
	public static final int DATABASE_VERSION = 1;
	//数据库表
	public static final String DATABASE_TABLE_ALBUM = "t_album";
	public static final String DATABASE_TABLE_PHOTO = "t_photo";
	public static final String DATABASE_TABLE_AUDIO = "t_audio";
	
}
