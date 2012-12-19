package cn.ancore.dazzlealbum.components.log;

import java.io.File;
import java.util.List;

import android.os.Environment;
import cn.ancore.dazzlealbum.commons.Config;
import cn.ancore.dazzlealbum.commons.Settings;

/**
 * 默认日志文件操作类
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class DefaultFileAppender extends FileAppender{

	private String mLogPath;
	private String mLogFileName;

	public DefaultFileAppender() {
		mLogFileName = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separatorChar
				+ Config.APP_NAME
				+ File.separator + "log"
				+ File.separatorChar + "dazzlealbum.log";
		mLogPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ File.separatorChar 
				+ Config.APP_NAME 
				+ File.separator + "log";
	}

	@Override
	protected String getLogFileName(Level level) {
		StringBuffer newName = new StringBuffer();
		newName.append("_")
			.append(Settings.getInstance().getAppVersion())
			.append("_")
			.append(level.toString());
		if(level.toInt() == Level.ERROR_INT && Config.DEBUG){
			newName.append("-D.");
		}else{
			newName.append(".");
		}
		String fileName = mLogFileName.replace(".", newName.toString());
		return fileName;
	}

	@Override
	protected String getLogPath() {
		return mLogPath;
	}

	@Override
	protected String getLogContent(Level level) {
		String pattern = Level.ERROR.toString()+".";
		List<String> fileList = queryLogFileName(pattern);
		StringBuffer data = new StringBuffer();
		if(fileList.size() > 0){
			for(int i=0, size=fileList.size(); i<size; i++){
				StringBuffer buffer = readFileToString(fileList.get(i));
				data.append(buffer);
				if(i != size - 1){
					data.append("|");
				}
			}
		}
		return data.toString();
	}

	@Override
	protected void clearLog(Level level) {
		String pattern = Level.ERROR.toString()+".";
		List<String> fileList = queryLogFileName(pattern);
		if(fileList.size() > 0){
			deleteFile(fileList);
		}
	}
	
}
