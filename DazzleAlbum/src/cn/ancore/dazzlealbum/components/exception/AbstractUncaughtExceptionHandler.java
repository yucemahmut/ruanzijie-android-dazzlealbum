package cn.ancore.dazzlealbum.components.exception;

/**
 * 异常捕获抽象类
 * 
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public abstract class AbstractUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {

	protected Thread.UncaughtExceptionHandler mExceptionHandler;

	protected abstract void catchException(Thread thread, Throwable throwable);

	protected AbstractUncaughtExceptionHandler() {
		this.mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	public Thread.UncaughtExceptionHandler getExceptionHandler() {
		return this.mExceptionHandler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		catchException(thread, ex);
	}

}
