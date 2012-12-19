package cn.ancore.dazzlealbum.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import android.util.Log;

/**
 * 文件操作相关工具�? * @author magicruan
 * @version 1.0 2012-12-14
 */
public class FileUtils {

	private static final String TAG = FileUtils.class.getSimpleName();
	private static final String CONTENT_ENCODING = "UTF-8";
	private static final int BUFFER_SIZE = 4096;
	
	/**
	 * 读取文件文本内容
	 * 
	 * @param fullPath
	 * @return
	 */
	public static String readStringFromFile(String fullPath) {
		return readStringFromFile(fullPath, CONTENT_ENCODING);
	}

	/**
	 * 读取文件文本内容
	 * @param fullPath
	 * @param encoding
	 * @return
	 */
	public static String readStringFromFile(String fullPath, String encoding) {
		File file = new File(fullPath);
		String content = "";
		if (file.exists() && file.isFile()) {
			StringWriter out = null;
			Reader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), encoding));
				out = new StringWriter();
				
				char[] buffer = new char[BUFFER_SIZE];
				int byteRead = -1;
				while((byteRead = reader.read(buffer)) != -1){
					out.write(buffer, 0, byteRead);
				}
				out.flush();
				content = out.toString();
			} catch (Exception e) {
				Log.e(TAG, "Could not read content from file. ", e);
			} finally{
				try{
					if(reader != null) reader.close();
				}catch(IOException e){
					Log.e(TAG, e.toString(), e);
				}
				try{
					if(out != null) out.close();
				}catch(IOException e){
					Log.e(TAG, e.toString(), e);
				}
			}
		}
		return content;
	}

	/**
	 * 读取文件byte内容
	 * 
	 * @param fullPath
	 * @return
	 */
	public static byte[] readByteFromFile(String fullPath) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;
		try {
			int bufferSize = 1024;
			bis = new BufferedInputStream(new FileInputStream(fullPath));
			baos = new ByteArrayOutputStream(bufferSize);
			byte[] temp = new byte[bufferSize];
			int size = 0;
			while ((size = bis.read(temp)) != -1) {
				baos.write(temp, 0, size);
			}
			data = baos.toByteArray();
		} catch (Exception e) {
			Log.e(TAG, "read byte from File Exception. ", e);
			data = null;
		} finally {
			try {
				if (bis != null)
					bis.close();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
		return data;
	}

	/**
	 * 写入文本内容到文�?	 * 
	 * @param fullPath
	 * @param content
	 */
	public static void writeContentToFile(String fullPath, String content) {
		writeContentToFile(fullPath, content, CONTENT_ENCODING);
	}

	/**
	 * 写入文本内容到文�?	 * 
	 * @param fullPath
	 * @param content
	 * @param encoding
	 */
	public static void writeContentToFile(String fullPath, String content,
			String encoding) {
		File file = new File(fullPath);
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), encoding));
			out.write(content);
			out.newLine();
			out.flush();
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Write content to File Exception. ", e);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Write content to File Exception. ", e);
		} catch (IOException e) {
			Log.e(TAG, "Write content to File Exception. ", e);
		} finally{
			try{
				if(out != null) out.close();
			}catch(IOException e){
				Log.e(TAG, e.toString(), e);
			}
		}

	}

	/**
	 * 写入byte数据到文�?	 * 
	 * @param fullPath
	 * @param data
	 */
	public static void writeByteToFile(String fullPath, byte[] data) {
		BufferedOutputStream bos = null;
		try {
			File file = new File(fullPath);
			if (!file.exists()) {
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
			}
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(data);
			bos.flush();
		} catch (Exception e) {
			Log.e(TAG, "Write byte to File Exception. " + fullPath, e);
		} finally {
			try {
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
	}

	/**
	 * 复制文件到指定目�?	 * @param source
	 * @param directory
	 */
	public static void copyFileToDirectory(File source, File directory){
		if(source != null && source.exists() && source.isFile()
				&& directory != null && directory.exists() && directory.isDirectory()){
			FileInputStream input = null;
			FileOutputStream output = null;
			try{
				File dest = new File(directory, source.getName());
				if(!source.getCanonicalPath().equals(dest.getCanonicalPath())
						&& dest.canWrite()){
					input = new FileInputStream(source);
					output = new FileOutputStream(dest);
					
					byte[] buffer = new byte[BUFFER_SIZE];
					int length = 0;
					while((length = input.read(buffer)) != -1){
						output.write(buffer, 0, length);
					}
					output.flush();
				}
			}catch(Exception e){
				Log.e(TAG, e.toString(), e);
			}finally{
				try{
					if(input != null) input.close();
				}catch(IOException e){
					Log.e(TAG, e.toString(), e);
				}
				try{
					if(output != null) output.close();
				}catch(IOException e){
					Log.e(TAG, e.toString(), e);
				}
			}
		}
	}
	
	/**
	 * 删除文件
	 * 
	 * @param fullName
	 */
	public static void removeFile(String fullName) {
		File file = new File(fullName);
		if (file.exists()) {
			file.delete();
		}
	}
	
	/**
	 * 删除目录
	 * @param fullName
	 */
	public static void removeDir(String fullName) {
		File dir = new File(fullName);
		if(dir.exists() && dir.isDirectory()){
			for(File file : dir.listFiles()){
				if(file.isFile()){
					file.delete();
				}else if(file.isDirectory()){
					removeDir(file.getAbsolutePath());
				}
			}
			dir.delete();
		}
	}

	/**
	 * 获取文件列表
	 * @param path
	 * @return
	 */
	public static File[] listFile(String path){
		return listFile(path, null);
	}
	
	public static File[] listFile(String path, FileFilter filter){
		File dir = new File(path);
		if(!dir.exists() || !dir.isDirectory()){
			dir.mkdirs();
		}
		if(filter == null){
			return dir.listFiles();
		}else{
			return dir.listFiles(filter);
		}
	}
	
	
	/**
	 * 创建临时文件
	 * @param file
	 * @return
	 */
	public static File createTempFile(File file) {
		String prefix = file.getName();
		File dir = file.getParentFile();
		File temp = null;
		try {
			temp = File.createTempFile(prefix, null, dir);
		} catch (IOException e) {

		}

		return temp;
	}
}
