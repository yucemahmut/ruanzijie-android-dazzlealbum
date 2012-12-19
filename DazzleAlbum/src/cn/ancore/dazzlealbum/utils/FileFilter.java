package cn.ancore.dazzlealbum.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 文件过滤�?
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class FileFilter implements FilenameFilter{

	public static final int FILTER_TYPE_PREFIX = 0;
	public static final int FILTER_TYPE_AFTERFIX = 1;
	public static final int FILTER_TYPE_CONTAIN = 2;

	private String mPattern;
	private int mFilterType;

	public FileFilter(String pattern, int type) {
		mPattern = pattern;
		mFilterType = type;
	}

	@Override
	public boolean accept(File file, String fileName) {
		switch (mFilterType) {
		case FILTER_TYPE_PREFIX:
			return fileName.startsWith(mPattern);
		case FILTER_TYPE_AFTERFIX:
			return fileName.endsWith(mPattern);
		case FILTER_TYPE_CONTAIN:
			return fileName.contains(mPattern);
		}
		return false;
	}
	
}
