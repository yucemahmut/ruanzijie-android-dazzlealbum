package cn.ancore.dazzlealbum.components.log;

import java.util.HashMap;

import cn.ancore.dazzlealbum.commons.Config;

/**
 * 日志操作单例类
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class Logger {

	private static Logger _instance;
	private HashMap<String, Level> mLevels;
	private LoggerImpl mLoggerImpl;
	private FileAppender mFileAppender;
	
	private Logger(){
		String[] levelArray = Config.LOG_LEVEL.split(",");
		mLevels = new HashMap<String, Level>();
		for(String s : levelArray){
			mLevels.put(s.toUpperCase(), Level.toLevel(s.toUpperCase()));
		}
		mFileAppender = new DefaultFileAppender();
		mLoggerImpl = new LoggerImpl(mLevels, mFileAppender);
	}
	
	public static synchronized Logger getInstance(){
		if(_instance == null){
			_instance = new Logger();
		}
		return _instance;
	}
	
	public void v(String tag, String msg){
		mLoggerImpl.verbose(tag, msg);
	}

	public void v(String tag, String msg, Throwable throwable){
		mLoggerImpl.verbose(tag, msg, throwable);
	}

	public void d(String tag, String msg){
		mLoggerImpl.debug(tag, msg);
	}

	public void d(String tag, String msg, Throwable throwable){
		mLoggerImpl.debug(tag, msg, throwable);
	}

	public void i(String tag, String msg){
		mLoggerImpl.info(tag, msg);
	}

	public void i(String tag, String msg, Throwable throwable){
		mLoggerImpl.info(tag, msg, throwable);
	}

	public void w(String tag, String msg){
		mLoggerImpl.warn(tag, msg);
	}

	public void w(String tag, String msg, Throwable throwable){
		mLoggerImpl.warn(tag, msg, throwable);
	}

	public void e(String tag, String msg){
		mLoggerImpl.error(tag, msg);
	}

	public void e(String tag, String msg, Throwable throwable){
		mLoggerImpl.error(tag, msg, throwable);
	}
	
	public String getErrorLog(){
		return mLoggerImpl.getErrorLog();
	}
	
	public void clearErrorLog(){
		mLoggerImpl.clearErrorLog();
	}
	
}
