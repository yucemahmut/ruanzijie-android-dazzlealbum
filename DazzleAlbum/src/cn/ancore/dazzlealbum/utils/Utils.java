package cn.ancore.dazzlealbum.utils;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import cn.ancore.dazzlealbum.commons.Constants;

import com.twmacinta.util.MD5;

/**
 * 常规工具�?
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class Utils {

	private static final String TAG = Utils.class.getSimpleName();
	
	/**
	 * md5加密
	 * 
	 * @param text
	 * @return
	 */
	public static String md5(String text) {
		MD5 md5 = new MD5();
		try {
			md5.Update(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.toString(), e);
		}
		return md5.asHex();
	}
	
	/**
	 * show the virtual keyborad on current context
	 * 
	 * @param c
	 */
	public static void popSoftInput(final Context c) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager imm = (InputMethodManager) c
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}, 500);
	}

	/**
	 * hide the virtual keyborad on screen;
	 * 
	 * @param c
	 * @param a
	 */
	public static void hideSoftInput(final Context c, final Activity a) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) c
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(a.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
	}
	
	// 字符串文本解�?
		public static String authDecode(String text) {
			String result = "";
			try {
				StringBuffer decodeStr = new StringBuffer();
				byte[] base64Str = Base64Util.decode(text, Base64Util.NO_WRAP);
				int strLen = base64Str.length;
				byte[] base64Key = Base64Util.encode(
						Constants.AUTH_CRYPT_KEY.getBytes("UTF-8"),
						Base64Util.NO_WRAP);
				int keyLen = base64Key.length;
				for (int i = 0; i < strLen; i++) {
					int ord = (int) base64Str[i];
					int keyOrd = (int) base64Key[i % keyLen];
					int strOrd = ord ^ keyOrd;
					decodeStr.append(String.valueOf((char) strOrd));
				}
				byte[] decodeByte = Base64Util.decode(decodeStr.toString(),
						Base64Util.NO_WRAP);
				result = new String(decodeByte, "UTF-8");
			} catch (Exception e) {
				Log.e("Base64", e.toString(), e);
				result = "";
			}
			return result;
		}

		// 字符串文本加�?
		public static String authEncode(String text) {
			String result = "";
			if (!TextUtils.isEmpty(text)) {
				try {
					StringBuffer encodeStr = new StringBuffer();
					byte[] base64Str = Base64Util.encode(text.getBytes("UTF-8"),
							Base64Util.NO_WRAP);
					int strLen = base64Str.length;
					byte[] base64Key = Base64Util.encode(
							Constants.AUTH_CRYPT_KEY.getBytes("UTF-8"),
							Base64Util.NO_WRAP);
					int keyLen = base64Key.length;
					for (int i = 0; i < strLen; i++) {
						int strOrd = (int) base64Str[i];
						int keyOrd = (int) base64Key[i % keyLen];
						int ord = strOrd ^ keyOrd;
						encodeStr.append(String.valueOf((char) ord));
					}
					byte[] encodeByte = Base64Util.encode(encodeStr.toString()
							.getBytes("UTF-8"), Base64Util.NO_WRAP);
					result = new String(encodeByte, "UTF-8");
				} catch (Exception e) {
					Log.e("Base64", e.toString(), e);
					result = "";
				}
			}
			return result;
		}

		// 查看当前内存
		public static String getAvailMemory() {
			int usedMegs = (int) (Debug.getNativeHeapAllocatedSize() / 1024L);
			int usedHeaps = (int) (Debug.getNativeHeapFreeSize() / 1024L);
			int heaps = (int) (Debug.getNativeHeapSize() / 1024L);
			String usedMegsString = String
					.format(" - Memory Used: %d KB", usedMegs);
			String usedHeapsString = String.format(" - Freed Heaps: %d KB",
					usedHeaps);
			String heapsString = String.format(" - Heaps: %d KB", heaps);
			return usedMegsString + usedHeapsString + heapsString;
		}
		
		public static int dip2px(Context context, float dipValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dipValue * scale + 0.5f);
		}

		public static int px2dip(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}
	
}
