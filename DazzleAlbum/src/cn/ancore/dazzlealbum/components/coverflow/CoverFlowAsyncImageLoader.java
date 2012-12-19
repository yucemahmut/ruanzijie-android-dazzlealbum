package cn.ancore.dazzlealbum.components.coverflow;

import android.graphics.Bitmap;

/**
 * 获取封面图片数据
 * 
 * @author magicruan
 * @version 1.0 2012-12-17
 */
public interface CoverFlowAsyncImageLoader {

	public void requestBitmapForIndex(CoverFlowView coverFlow, int index);

	public Bitmap getDefaultBitmap();

}
