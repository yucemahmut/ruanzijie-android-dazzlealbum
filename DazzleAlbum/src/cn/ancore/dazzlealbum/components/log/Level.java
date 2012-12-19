package cn.ancore.dazzlealbum.components.log;

/**
 * 日志级别
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class Level {

	public static final int OFF_INT = Integer.MAX_VALUE;
	public static final int VERBOSE_INT = 50000;
	public static final int ERROR_INT = 40000;
	public static final int WARN_INT = 30000;
	public static final int INFO_INT = 20000;
	public static final int DEBUG_INT = 10000;
	public static final int ALL_INT = Integer.MIN_VALUE;

	public static final Level OFF = new Level(Integer.MAX_VALUE, "OFF");
	public static final Level VERBOSE = new Level(50000, "VERBOSE");
	public static final Level ERROR = new Level(40000, "ERROR");
	public static final Level WARN = new Level(30000, "WARN");
	public static final Level INFO = new Level(20000, "INFO");
	public static final Level DEBUG = new Level(10000, "DEBUG");
	public static final Level ALL = new Level(Integer.MIN_VALUE, "ALL");

	private int mLevel;
	private String mLevelStr;

	protected Level(int level, String levelStr) {
		this.mLevel = level;
		this.mLevelStr = levelStr;
	}

	public boolean equals(Object o) {
		if ((o instanceof Level)) {
			Level l = (Level) o;
			return this.mLevel == l.mLevel;
		}
		return false;
	}

	public boolean isGreaterOrEqual(Level l) {
		return this.mLevel >= l.mLevel;
	}

	public final String toString() {
		return this.mLevelStr;
	}

	public final int toInt() {
		return this.mLevel;
	}

	public static Level toLevel(String sArg) {
		return toLevel(sArg, DEBUG);
	}

	public static Level toLevel(int val) {
		return toLevel(val, DEBUG);
	}

	public static Level toLevel(int val, Level defaultLevel) {
		switch (val) {
		case ALL_INT:
			return ALL;
		case DEBUG_INT:
			return DEBUG;
		case INFO_INT:
			return INFO;
		case WARN_INT:
			return WARN;
		case ERROR_INT:
			return ERROR;
		case VERBOSE_INT:
			return VERBOSE;
		case OFF_INT:
			return OFF;
		}
		return defaultLevel;
	}

	public static Level toLevel(String sArg, Level defaultLevel) {
		if (sArg == null) {
			return defaultLevel;
		}
		String s = sArg.toUpperCase();
		if (s.equals("ALL"))
			return ALL;
		if (s.equals("DEBUG"))
			return DEBUG;
		if (s.equals("INFO"))
			return INFO;
		if (s.equals("WARN"))
			return WARN;
		if (s.equals("ERROR"))
			return ERROR;
		if (s.equals("VERBOSE"))
			return VERBOSE;
		if (s.equals("OFF"))
			return OFF;
		return defaultLevel;
	}
	
}
