package cn.ancore.dazzlealbum.components.log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import android.os.Build;
import android.util.Log;
import cn.ancore.dazzlealbum.commons.Config;
import cn.ancore.dazzlealbum.commons.Settings;
import cn.ancore.dazzlealbum.utils.DateUtil;
import cn.ancore.dazzlealbum.utils.Utils;
/**
 * 日志操作实现类
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class LoggerImpl extends AbstractLogger{

	protected LoggerImpl(HashMap<String, Level> lv, FileAppender appender) {
		super(lv, appender);
	}
	
	@Override
	public boolean isDebugEnabled() {
		return this.mLevels.containsKey(Level.DEBUG.toString());
	}

	@Override
	public boolean isErrorEnabled() {
		return this.mLevels.containsKey(Level.ERROR.toString());
	}

	@Override
	public boolean isInfoEnabled() {
		return this.mLevels.containsKey(Level.INFO.toString());
	}

	@Override
	public boolean isVerboseEnabled() {
		return this.mLevels.containsKey(Level.VERBOSE.toString());
	}

	@Override
	public boolean isWarnEnabled() {
		return this.mLevels.containsKey(Level.WARN.toString());
	}

	@Override
	public void verbose(String tag, String msg) {
		if (isVerboseEnabled()) {
			Log.v(tag, msg);
			this.mFileAppender.writeFile(Level.VERBOSE, appendTime(msg));
		}
	}

	@Override
	public void verbose(String tag, String msg, Throwable throwable) {
		if (isVerboseEnabled()) {
			Log.v(tag, msg, throwable);
			this.mFileAppender.writeFile(Level.VERBOSE, appendTime(msg));
		}
	}

	@Override
	public void debug(String tag, String msg) {
		if (isDebugEnabled()) {
			Log.d(tag, msg);
			this.mFileAppender.writeFile(Level.DEBUG, appendTime(msg));
		}
	}

	@Override
	public void debug(String tag, String msg, Throwable throwable) {
		if (isDebugEnabled()) {
			Log.d(tag, msg, throwable);
			this.mFileAppender.writeFile(Level.DEBUG, appendTime(msg));
		}
	}

	@Override
	public void info(String tag, String msg) {
		if (isInfoEnabled()) {
			Log.i(tag, msg);
			this.mFileAppender.writeFile(Level.INFO, appendTime(msg));
		}
	}

	@Override
	public void info(String tag, String msg, Throwable throwable) {
		if (isInfoEnabled()) {
			Log.i(tag, msg, throwable);
			this.mFileAppender.writeFile(Level.INFO, appendTime(msg));
		}
	}

	@Override
	public void warn(String tag, String msg) {
		if (isWarnEnabled()) {
			Log.w(tag, msg);
			this.mFileAppender.writeFile(Level.WARN, appendTime(msg));
		}
	}

	@Override
	public void warn(String tag, String msg, Throwable throwable) {
		if (isWarnEnabled()) {
			Log.w(tag, msg, throwable);
			this.mFileAppender.writeFile(Level.WARN, getExceptionReport(throwable));
		}
	}

	@Override
	public void error(String tag, String msg) {
		if (isErrorEnabled()) {
			Log.e(tag, msg);
			this.mFileAppender.writeFile(Level.ERROR, appendTime(msg));
		}
	}

	@Override
	public void error(String tag, String msg, Throwable throwable) {
		if (isErrorEnabled()) {
			Log.e(tag, msg, throwable);
			this.mFileAppender.writeFile(Level.ERROR, getCrashReport(throwable));
		}
	}

	@Override
	public String getErrorLog() {
		return this.mFileAppender.getLogContent(Level.ERROR);
	}

	@Override
	public void clearErrorLog() {
		this.mFileAppender.clearLog(Level.ERROR);
	}
	
	/**
	 * 拼接时间
	 * @param data
	 * @return
	 */
	private String appendTime(String data){
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		sbuffer.append("--->");
		sbuffer.append(data);
		return sbuffer.toString();
	}
	
	/**
	 * 生成error log
	 * @param exception
	 * @return
	 */
	private String getExceptionReport(Throwable exception){
		NumberFormat theFormatter = new DecimalFormat("#0.");
		StringBuffer sbuffer = new StringBuffer();
		StackTraceElement[] theStackTrace = exception.getStackTrace();
		Throwable theCause = exception.getCause();

		sbuffer.append(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		sbuffer.append("--->");
		sbuffer.append(exception.toString() + "\n");
		if(theStackTrace.length > 0){
			sbuffer.append("======== Stack trace =======\n");
			int length = theStackTrace.length;
			for(int i=0;i<length;i++){
				sbuffer.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
			}
			sbuffer.append("=====================\n");
		}
		if(theCause != null){
			sbuffer.append("======== Cause ========\n");
			sbuffer.append(theCause.toString()+"\n\n");
			theStackTrace = theCause.getStackTrace();
			int length = theStackTrace.length;
			for(int i=0;i<length;i++){
				sbuffer.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
			}
			sbuffer.append("=====================\n");
		}
		sbuffer.append("\n\n");
		return sbuffer.toString();
	}
	
	/**
	 * 生成crash log
	 * @param exception
	 * @return
	 */
	private String getCrashReport(Throwable exception){
		if(Config.DEBUG){
			return getExceptionReport(exception);
		}else{
			try{
				JSONObject json = new JSONObject();
				json.put("version", Settings.getInstance().getAppVersion());
				json.put("osversion", Settings.getInstance().getSdkVersion());
				json.put("phonetype", Build.MODEL);
				json.put("memory", Utils.getAvailMemory());
				json.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				//拼接crash内容
				NumberFormat theFormatter = new DecimalFormat("#0.");
				StringBuffer sbuffer = new StringBuffer();
				StackTraceElement[] theStackTrace = exception.getStackTrace();
				Throwable theCause = exception.getCause();
				sbuffer.append(exception.toString() + "\n");
				if(theStackTrace.length > 0){
					sbuffer.append("Stack trace: \n");
					int length = theStackTrace.length;
					for(int i=0;i<length;i++){
						sbuffer.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
					}
					sbuffer.append("\n");
				}
				if(theCause != null){
					sbuffer.append("Cause: \n");
					sbuffer.append(theCause.toString()+"\n");
					theStackTrace = theCause.getStackTrace();
					int length = theStackTrace.length;
					for(int i=0;i<length;i++){
						sbuffer.append(theFormatter.format(i+1)+"\t"+theStackTrace[i].toString()+"\n");
					}
					sbuffer.append("\n");
				}
				json.put("crash", sbuffer.toString());
				return Utils.authEncode(json.toString());
			}catch(Exception e){
				return "";
			}
		}
	}

}
