package cn.ancore.dazzlealbum.components.slideshow.vintage;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/**
 * slideshow view
 * @author magicruan
 * @version 4.2 2012-12-12
 */
public class VintageItemView extends RelativeLayout{

	private static final String TAG = VintageItemView.class.getSimpleName();
	
	private static final long ANIMATION_DURATION = 5 * 1000;
	private static final int PHOTO_STROKE_WIDTH = 1;
	private static final int PHOTO_PADDING_WIDTH = 3;
	
	private ArrayList<String> mPhotoUrls;
	private int mPosition;
	private int mScreenWidth;
	private int mScreenHeight;
	private Bitmap mBackgroundImg;
	private Bitmap mMainImg;

	//页面背景
	private ImageView m_ivBackground;
	//中心图片
	private ImageView m_ivPhoto;
	
	public VintageItemView(Context context){
		super(context);
		setupViews(context);
	}
	
	public VintageItemView(Context context, AttributeSet attrs){
		super(context, attrs);
		setupViews(context);
	}
	
	public VintageItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setupViews(context);
	}
	
	/**
	 * 初始化页面控件
	 */
	private void setupViews(Context context){
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		m_ivBackground = new ImageView(context);
		m_ivBackground.setLayoutParams(lp);
		m_ivBackground.setScaleType(ScaleType.CENTER_CROP);
		addView(m_ivBackground);
		
		m_ivPhoto = new ImageView(context);
		m_ivPhoto.setLayoutParams(lp);
		m_ivPhoto.setScaleType(ScaleType.CENTER_CROP);
		addView(m_ivPhoto);
	}

	/**
	 * 填充数据，外部调用
	 * @param photos
	 */
	public void setPhotos(ArrayList<String> photoUrls, int position){
		mPhotoUrls = photoUrls;
		mPosition = position;
	}
	
	/**
	 * 生成多张图片拼接而成的背景
	 * @return
	 */
	private Bitmap createSpliceBackground(Bitmap bgPhoto, Bitmap[] photos){
		int scaleWidth = mScreenWidth + (mScreenWidth / 4);
		int scaleHeight = mScreenHeight + (mScreenHeight / 4);
		Bitmap background = Bitmap.createBitmap(scaleWidth, scaleHeight, Config.RGB_565);
		
		Canvas canvas = new Canvas(background);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		
		Rect srcRect = new Rect(0, 0, bgPhoto.getWidth(), bgPhoto.getHeight());
		Rect destRect = new Rect(0, 0, scaleWidth, scaleHeight);
		canvas.drawBitmap(bgPhoto, srcRect, destRect, paint);
		
		//随机叠放4张图片
		for(int i=0, size=photos.length; i<size; i++){
			int smallScaleWidth = scaleWidth * 3 / 8;
			int smallScaleHeight = scaleHeight * 3 / 8;
			Bitmap photo = createBorderPhoto(photos[i], smallScaleWidth);
			Matrix matrix = new Matrix();
			//计算位移
			int dx=0, dy=0;
			dx = (scaleWidth * (i + 1) / 4 - smallScaleWidth / 2) + scaleWidth / 2 * (i % 2);
			dy = (scaleHeight * (i + 1) / 4 - smallScaleHeight / 2) + scaleHeight / 2 * (i % 2);
			matrix.setTranslate(dx, dy);
			//计算随机旋转角度
			float degress = getRandomNum(0, 180);
			matrix.postRotate(degress, dx + photo.getWidth() / 2, dy + photo.getHeight() / 2);
			canvas.drawBitmap(photo, matrix, paint);
		}
		return background;
	}
	
	/**
	 * 生成带边框照片
	 * @param photo
	 * @param scaleWidth
	 * @param scaleHeight
	 * @return
	 */
	private Bitmap createBorderPhoto(Bitmap photo, int scaleWidth){
		int width = photo.getWidth();
		int height = photo.getHeight();
		//计算合适的图片大小
		int scaleHeight = scaleWidth * height / width;
		
		Bitmap image = Bitmap.createBitmap(scaleWidth, scaleHeight, Config.ARGB_8888);
		
		Canvas canvas = new Canvas(image);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setFilterBitmap(true);
		
		Rect greyBorderRect = new Rect(0, 0, 
				scaleWidth + PHOTO_STROKE_WIDTH + PHOTO_PADDING_WIDTH, 
				scaleHeight + PHOTO_STROKE_WIDTH + PHOTO_PADDING_WIDTH);
		int greyColor = 0xFFD3D3D3;
		paint.setColor(greyColor);
		canvas.drawRect(greyBorderRect, paint);
		
		Rect whiteBorderRect = new Rect(0, 0,
				scaleWidth + PHOTO_PADDING_WIDTH,
				scaleHeight + PHOTO_PADDING_WIDTH);
		int whiteColor = 0xFFFFFFFF;
		paint.setColor(whiteColor);
		canvas.drawRect(whiteBorderRect, paint);
		
		Rect srcRect = new Rect(0, 0, photo.getWidth(), photo.getHeight());
		Rect destRect = new Rect(0, 0, scaleWidth, scaleHeight);
		canvas.drawBitmap(photo, srcRect, destRect, paint);
	
		return image;
	}
	
	/**
	 * 生成中心图片
	 * @return
	 */
	private Bitmap createMainPhoto(Bitmap photo){
		//计算合适的图片大小
		int scaleWidth = mScreenWidth * 3 / 4;
		return createBorderPhoto(photo, scaleWidth);
	}
	
	/**
	 * 创建动画
	 * @return
	 */
	private AnimationSet createAnimation(){
		AnimationSet animSet = new AnimationSet(true);
		float fromDegrees = 0f, toDegrees=0f;
		//计算随机角度
		fromDegrees = getRandomNum(-45, 45);
		toDegrees = getRandomNum(-45, 45);
		//
		RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animSet.addAnimation(rotate);
		
		ScaleAnimation scale = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		animSet.addAnimation(scale);
		
		animSet.setFillAfter(true);
		animSet.setDuration(ANIMATION_DURATION);
		animSet.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		return animSet;
	}
	
	/**
	 * 重新加载，外部调用
	 */
	public void reload(){
		//TODO
		//图片已生成，直接显示
		if(mMainImg != null && !mMainImg.isRecycled()
				&& mBackgroundImg != null 
				&& !mBackgroundImg.isRecycled()){
			m_ivBackground.setImageBitmap(mBackgroundImg);
			m_ivPhoto.setImageBitmap(mMainImg);
			m_ivBackground.startAnimation(createAnimation());
			m_ivPhoto.startAnimation(createAnimation());
		}else{
			//图片未生成，需要从网络读取
			
		}
	}
	
	/**
	 * 内存图片回收，并停止动画，供外部调用
	 */
	public void recycle(boolean recycleBitmap){
		//TODO
		m_ivBackground.setImageBitmap(null);
		m_ivPhoto.setImageBitmap(null);
		//停止动画
		
		
		//
		if(recycleBitmap){
			if(mBackgroundImg != null && !mBackgroundImg.isRecycled()){
				mBackgroundImg.recycle();
				mBackgroundImg = null;
			}
			if(mMainImg != null && !mMainImg.isRecycled()){
				mMainImg.recycle();
				mMainImg = null;
			}
		}
	}
	
	private int getRandomNum(int min, int max){
		return (int)(Math.random() * (max - min) + min);
	}
	
}
