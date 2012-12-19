package cn.ancore.dazzlealbum.components.exception;

import cn.ancore.dazzlealbum.components.log.Logger;

/**
 * 默认异常捕获类
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class DefaultUncaughtExceptionHandler extends
		AbstractUncaughtExceptionHandler {

	private static final String TAG = DefaultUncaughtExceptionHandler.class
			.getSimpleName();

	@Override
	protected void catchException(Thread thread, Throwable throwable) {
		Logger.getInstance().e(TAG, throwable.toString(), throwable);
		if (this.mExceptionHandler != null) {
			this.mExceptionHandler.uncaughtException(thread, throwable);
		}
	}

}
