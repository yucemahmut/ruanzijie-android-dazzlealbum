package cn.ancore.dazzlealbum.components.log;

import java.util.HashMap;

/**
 * 日志操作抽象类
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public abstract class AbstractLogger {

	protected HashMap<String, Level> mLevels = new HashMap<String, Level>();
	protected FileAppender mFileAppender;

	protected AbstractLogger(HashMap<String, Level> lv, FileAppender appender) {
		mLevels.putAll(lv);
		mFileAppender = appender;
	}

	public abstract boolean isDebugEnabled();

	public abstract boolean isErrorEnabled();

	public abstract boolean isInfoEnabled();

	public abstract boolean isVerboseEnabled();

	public abstract boolean isWarnEnabled();

	public abstract void verbose(String tag, String msg);

	public abstract void verbose(String tag, String msg, Throwable throwable);

	public abstract void debug(String tag, String msg);

	public abstract void debug(String tag, String msg, Throwable throwable);

	public abstract void info(String tag, String msg);

	public abstract void info(String tag, String msg, Throwable throwable);

	public abstract void warn(String tag, String msg);

	public abstract void warn(String tag, String msg, Throwable throwable);

	public abstract void error(String tag, String msg);

	public abstract void error(String tag, String msg, Throwable throwable);
	
	public abstract String getErrorLog();
	
	public abstract void clearErrorLog();
	
}
