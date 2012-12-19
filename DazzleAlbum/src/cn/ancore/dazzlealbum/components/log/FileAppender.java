package cn.ancore.dazzlealbum.components.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;
import cn.ancore.dazzlealbum.commons.Settings;
import cn.ancore.dazzlealbum.utils.FileFilter;
import cn.ancore.dazzlealbum.utils.FileUtils;

/**
 * 日志文件操作类
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public abstract class FileAppender {

	private static final String TAG = FileAppender.class.getSimpleName();
	private Object FILE_LOCK = new Object();

	protected abstract String getLogFileName(Level level);

	protected abstract String getLogPath();
	
	protected abstract String getLogContent(Level level);
	
	protected abstract void clearLog(Level level);

	protected void writeFile(Level level, String data) {
		if (Settings.getInstance().isSDCardAvailable() && !TextUtils.isEmpty(data)) {
			synchronized (FILE_LOCK) {
				File dir = new File(getLogPath());
				if ((!dir.exists()) || !(dir.isDirectory())) {
					dir.mkdirs();
				}
				String fileName = getLogFileName(level);
				if (!TextUtils.isEmpty(fileName)) {
					try{
						File logfile = new File(fileName);
						if(!logfile.exists()){
							File parent = logfile.getParentFile();
							if(!parent.exists()){
								parent.mkdirs();
							}
							logfile.createNewFile();
						}
						FileUtils.writeContentToFile(logfile.getAbsolutePath(), data);
					}catch(IOException e){
						Log.e(TAG, e.toString(), e);
					}
				}
			}
		}
	}

	protected HashMap<String, StringBuffer> readLogFromFile(String[] fileNames) {
		HashMap<String, StringBuffer> data = new HashMap<String, StringBuffer>();
		if (Settings.getInstance().isSDCardAvailable()) {
			synchronized (FILE_LOCK) {
				File f;
				if ((fileNames != null) && (fileNames.length > 0)) {
					int length = fileNames.length;
					for (int i = 0; i < length; i++) {
						String fName = fileNames[i];
						f = new File(fName);
						if ((f.exists()) && (f.isFile())) {
							String fullName = f.getAbsolutePath();
							StringBuffer buffer = readFileToString(fullName);
							if (buffer.length() > 0) {
								data.put(fullName, buffer);
							}
						}
					}
				} else {
					File dir = new File(getLogPath());
					if (dir.isDirectory()) {
						File[] files = dir.listFiles();
						for (File fi : files) {
							if (fi.isFile()) {
								String fullName = fi.getAbsolutePath();
								StringBuffer buffer = readFileToString(fullName);
								if (buffer.length() > 0) {
									data.put(fullName, buffer);
								}
							}
						}
					}
				}
			}
		}
		return data;
	}

	protected StringBuffer readFileToString(String fullName) {
		StringBuffer buffer = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(fullName));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line + "|");
			}
		} catch (Exception e) {
			Log.e(TAG, "Read File To String Exception. ", e);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ie) {
				Log.e(TAG, "Close Buffer Reader Exception. ", ie);
			}
		}
		return buffer;
	}

	protected void deleteFile(List<String> fileNames) {
		if (Settings.getInstance().isSDCardAvailable()) {
			synchronized (FILE_LOCK) {
				if ((fileNames != null) && (fileNames.size() > 0)) {
					int length = fileNames.size();
					for (int i = 0; i < length; i++) {
						String fName = fileNames.get(i);
						FileUtils.removeFile(fName);
					}
				}
			}
		}
	}

	protected boolean hasLogFile(String fileName) {
		if (Settings.getInstance().isSDCardAvailable()) {
			synchronized (FILE_LOCK) {
				File f = new File(fileName);
				if ((f.exists()) && (f.isFile())) {
					return true;
				}
			}
		}
		return false;
	}

	protected List<String> queryLogFileName(String pattern) {
		ArrayList<String> result = new ArrayList<String>();
		if (Settings.getInstance().isSDCardAvailable()) {
			synchronized (FILE_LOCK) {
				File dir = new File(getLogPath());
				if ((!dir.exists()) || (!dir.isDirectory())) {
					dir.mkdirs();
				}
				File[] files;
				if (TextUtils.isEmpty(pattern)) {
					files = dir.listFiles();
				} else {
					files = dir.listFiles(new FileFilter(pattern, FileFilter.FILTER_TYPE_CONTAIN));
				}
				if(files != null){
					for (File f : files) {
						result.add(f.getAbsolutePath());
					}
				}
			}
		}
		return result;
	}
	
}
