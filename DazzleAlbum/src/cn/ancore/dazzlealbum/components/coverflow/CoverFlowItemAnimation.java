package cn.ancore.dazzlealbum.components.coverflow;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 封面图片切换动画
 * 
 * @author magicruan
 * @version 1.0 2012-12-17
 */
public class CoverFlowItemAnimation extends Animation {

	private int mViewWidth;
	private int mViewHeight;
	private int mStartZOffset;
	private int mStopZOffset;
	private int mStartXOffset;
	private int mStopXOffset;
	private float mStopAngleDegrees = 0;
	// private double mStopAngleRadians = 0;
	private float mStartAngleDegrees = 0;
	private boolean mStatic = false;

	// private double mStartAngleRadians = 0;

	public CoverFlowItemAnimation() {
		super();
		setFillAfter(true);
		setFillBefore(true);
	}

	public void setStatic() {
		mStatic = true;
		setDuration(0);
	}

	public void setRotation(float start, float stop) {
		mStartAngleDegrees = start;
		mStopAngleDegrees = stop;
	}

	public void setXTranslation(int start, int stop) {
		mStartXOffset = start;
		mStopXOffset = stop;
	}

	public void setZTranslation(int start, int stop) {
		mStartZOffset = start;
		mStopZOffset = stop;
	}

	public void setViewDimensions(int width, int height) {
		mViewWidth = width;
		mViewHeight = height;
	}

	public float getStopAngleDegrees() {
		return mStopAngleDegrees;
	}

	public float getStartAngleDegrees() {
		return mStartAngleDegrees;
	}

	public int getStartXOffset() {
		return mStartXOffset;
	}

	public int getStopXOffset() {
		return mStopXOffset;
	}

	public int getStopZOffset() {
		return mStopZOffset;
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		t.setTransformationType(mStatic ? Transformation.TYPE_BOTH
				: Transformation.TYPE_MATRIX);

		if (mStatic)
			t.setAlpha(interpolatedTime < 1.0f ? 0 : 1);

		float angleDegrees = mStartAngleDegrees + interpolatedTime
				* (mStopAngleDegrees - mStartAngleDegrees);
		float zOffset = mStartZOffset + interpolatedTime
				* (mStopZOffset - mStartZOffset);
		int xOffset = mStartXOffset
				+ (int) (interpolatedTime * (mStopXOffset - mStartXOffset));
		Matrix m = new Matrix();
		Camera camera = new Camera();
		camera.translate(0, 0, zOffset);

		camera.rotateY(angleDegrees);

		camera.getMatrix(m);
		m.preTranslate(-(mViewWidth / 2), -(mViewHeight / 2));
		m.postTranslate((mViewWidth / 2) + xOffset, (mViewHeight / 2));

		t.getMatrix().set(m);
		super.applyTransformation(interpolatedTime, t);
	}

}
