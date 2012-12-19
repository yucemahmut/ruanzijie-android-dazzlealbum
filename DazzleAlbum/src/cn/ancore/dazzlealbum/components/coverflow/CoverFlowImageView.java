package cn.ancore.dazzlealbum.components.coverflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * CoverFlow ImageView控件
 * 
 * @author magicruan
 * @version 1.0 2012-12-14
 */
public class CoverFlowImageView extends ImageView {

	// 当前图片位置
	private int mCoverFlowPosition;
	// 图片原始高度
	private int mOriginalImageHeight;
	// 图片宽度
	private int mBitmapWidth = 0;
	// 图片高度
	private int mBitmapHeight = 0;
	// X轴缩放比�?
	private float mScaleX = 1;
	// Y轴缩放比�?
	private float mScaleY = 1;

	public CoverFlowImageView(Context context) {
		super(context);
	}

	public CoverFlowImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CoverFlowImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setScaleX(float x) {
		mScaleX = x;
	}

	public void setScaleY(float y) {
		mScaleY = y;
	}

	public void setCoverFlowPosition(int position) {
		mCoverFlowPosition = position;
	}

	public int getCoverFlowPosition() {
		return mCoverFlowPosition;
	}

	/**
	 * 封面宽度
	 * 
	 * @return
	 */
	public int getCoverWidth() {
		return (int) (mBitmapWidth * mScaleX);
	}

	/**
	 * 封面高度
	 * 
	 * @return
	 */
	public int getCoverHeight() {
		return (int) (mBitmapHeight * mScaleY);
	}

	/**
	 * 原始封面高度
	 * 
	 * @return
	 */
	public int getOriginalCoverHeight() {
		return (int) (mOriginalImageHeight * mScaleY);
	}

	/**
	 * 设置封面图片
	 * 
	 * @param bitmap
	 * @param originalImageHeight
	 */
	public void setImageBitmap(Bitmap bitmap, int originalImageHeight) {
		mOriginalImageHeight = originalImageHeight;
		mBitmapWidth = bitmap.getWidth();
		mBitmapHeight = bitmap.getHeight();
		setLayoutParams(new ViewGroup.LayoutParams(
				(int) (mBitmapWidth * mScaleX), (int) (mBitmapHeight * mScaleY)));
		setImageBitmap(bitmap);
	}

	/**
	 * 创建图片倒影
	 * 
	 * @param b
	 * @param reflectionFraction
	 * @param dropShadowRadius
	 * @return
	 */
	public static Bitmap createReflectedBitmap(Bitmap bitmap,
			float reflectionFraction, int dropShadowRadius) {
		if (reflectionFraction == 0 && dropShadowRadius == 0) {
			return bitmap;
		}

		Bitmap result;
		int padding = dropShadowRadius;

		// Create the result bitmap, in which we'll print the
		// original bitmap and its reflection
		result = Bitmap.createBitmap(bitmap.getWidth() + padding * 2, 2
				* padding
				+ (int) (bitmap.getHeight() * (1 + reflectionFraction)),
				Config.ARGB_8888);

		// We'll work in a canvas
		Canvas canvas = new Canvas(result);

		// Add a drop shadow
		Paint dropShadow = new Paint();
		dropShadow.setShadowLayer(padding, 0, 0, 0xFF000000);
		canvas.drawRect(padding, padding, bitmap.getWidth() + padding,
				result.getHeight() - padding, dropShadow);

		// draw the original image
		canvas.drawBitmap(bitmap, padding, padding, null);

		// draw the reflection
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		canvas.setMatrix(matrix);
		int reflectionHeight = Math.round(reflectionFraction
				* bitmap.getHeight());
		canvas.drawBitmap(
				bitmap,
				new Rect(0, bitmap.getHeight() - reflectionHeight, bitmap
						.getWidth(), bitmap.getHeight()),
				new Rect(padding, -reflectionHeight - padding
						- bitmap.getHeight(), padding + bitmap.getWidth(),
						-padding - bitmap.getHeight()), null);
		canvas.setMatrix(new Matrix());

		// draw the gradient
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				result.getHeight(), 0x40000000, 0xFF000000, TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DARKEN));
		canvas.drawRect(padding, padding + bitmap.getHeight(),
				padding + bitmap.getWidth(), padding + bitmap.getHeight()
						* (1 + reflectionFraction), paint);
		return result;
	}

}
