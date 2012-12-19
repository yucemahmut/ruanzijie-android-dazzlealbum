package cn.ancore.dazzlealbum.components.coverflow;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * CoverFlow 控件
 * 
 * @author magicruan
 * @version 1.0 2012-12-17
 */
public class CoverFlowView extends LinearLayout {

	// 异步获取放封面图片接�?
	private WeakReference<CoverFlowAsyncImageLoader> mAsyncImageLoader;
	// coverflow动作监听�?
	private WeakReference<CoverFlowListener> mCoverFlowListener;
	// 未在屏幕中显示的图片列表
	private Set<CoverFlowImageView> mOffscreenCovers = new HashSet<CoverFlowImageView>();
	// 在屏幕中显示的图片列�?
	private HashMap<Integer, CoverFlowImageView> mOnscreenCovers = new HashMap<Integer, CoverFlowImageView>();
	// 封面图片列表
	private HashMap<Integer, Bitmap> mCoverImages = new HashMap<Integer, Bitmap>();
	// 封面图片高度列表
	private HashMap<Integer, Integer> mCoverImageHeights = new HashMap<Integer, Integer>();
	// 默认图片
	private Bitmap mDefaultBitmap;
	// 默认图片高度
	private int mDefaultBitmapHeight;
	// 横向滑动控件
	private HorizontalScrollView mScrollView;

	private ViewGroup mItemContainer;

	private int mLowerVisibleCover = -1;
	private int mUpperVisibleCover = -1;
	private int mNumberOfImages;
	private int mBeginningCover;

	private CoverFlowImageView mSelectedCoverView = null;

	private int mHalfScreenHeight;
	private int mHalfScreenWidth;

	private boolean mIsSingleTap;
	private float mStartScrollX;
	private float mStartX;

	private SortedSet<Integer> mTouchedCovers = new TreeSet<Integer>();

	public CoverFlowView(Context context) {
		super(context);
		setUpInitialState();
	}

	public CoverFlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setUpInitialState();
	}

	public CoverFlowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		setUpInitialState();
	}

	/**
	 * 设置默认图片
	 * 
	 * @param bitmap
	 */
	public void setDefaultBitmap(Bitmap bitmap) {
		mDefaultBitmapHeight = bitmap != null ? bitmap.getHeight() : 0;
		mDefaultBitmap = bitmap;
	}

	public void setAsyncImageLoader(CoverFlowAsyncImageLoader imageLoader) {
		mAsyncImageLoader = new WeakReference<CoverFlowAsyncImageLoader>(
				imageLoader);
		setDefaultBitmap(imageLoader.getDefaultBitmap());
	}

	public void seCoverFlowtListener(CoverFlowListener listener) {
		mCoverFlowListener = new WeakReference<CoverFlowListener>(listener);
	}

	/**
	 * 控件初始�?
	 */
	private void setUpInitialState() {
		// Create the scrollView
		mScrollView = new HorizontalScrollView(getContext()) {
			// Catch trackball events
			@Override
			public boolean onTrackballEvent(MotionEvent event) {
				return CoverFlowView.this.onTrackballEvent(event);
			}

		};
		mScrollView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return CoverFlowView.this.onTouchEvent(event);
			}
		});

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mScrollView.setLayoutParams(params);
		mScrollView
				.setHorizontalScrollBarEnabled(CoverFlowConfig.HORIZONTAL_SCROLLBAR_ENABLED);
		mScrollView
				.setHorizontalFadingEdgeEnabled(CoverFlowConfig.FADING_EDGES_ENABLED);
		addView(mScrollView);

		// Create an intermediate LinearLayout
		LinearLayout linearLayout = new LinearLayout(getContext());
		mScrollView.addView(linearLayout);

		// Create the item container
		mItemContainer = new FrameLayout(getContext());
		linearLayout.addView(mItemContainer);
	}

	/**
	 * 在队里中获取未在屏幕中显示的图片
	 * 
	 * @return
	 */
	private CoverFlowImageView dequeueReusableCover() {
		CoverFlowImageView item = null;
		if (!mOffscreenCovers.isEmpty()) {
			item = mOffscreenCovers.iterator().next();
			mOffscreenCovers.remove(item);
		}
		return item;
	}

	/**
	 * 生成封面图片控件
	 * 
	 * @param coverIndex
	 * @return
	 */
	private CoverFlowImageView coverForIndex(int coverIndex) {
		CoverFlowImageView coverItem = dequeueReusableCover();
		if (coverItem == null) {
			coverItem = new CoverFlowImageView(getContext());
			coverItem.setScaleType(ImageView.ScaleType.FIT_XY);
			coverItem.setScaleX(CoverFlowConfig.IMAGE_SCALE_X);
			coverItem.setScaleY(CoverFlowConfig.IMAGE_SCALE_Y);
			coverItem.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					onTouchItem((CoverFlowImageView) v, event);
					return false;
				}
			});
		}
		coverItem.setCoverFlowPosition(coverIndex);
		return coverItem;
	}

	/**
	 * 显示封面图片
	 * 
	 * @param cover
	 */
	private void updateCoverBitmap(CoverFlowImageView cover) {
		int coverNumber = cover.getCoverFlowPosition();
		Bitmap bitmap = mCoverImages.get(coverNumber);
		if (bitmap != null) {
			int coverImageHeight = mCoverImageHeights.get(coverNumber);
			if (coverImageHeight > 0)
				cover.setImageBitmap(bitmap, coverImageHeight);
		} else {
			cover.setImageBitmap(mDefaultBitmap, mDefaultBitmapHeight);
			if (mAsyncImageLoader != null && mAsyncImageLoader.get() != null) {
				mAsyncImageLoader.get()
						.requestBitmapForIndex(this, coverNumber);
			}
		}
	}

	/**
	 * 
	 * @param cover
	 * @param selectedCover
	 * @param animated
	 */
	private void layoutCover(CoverFlowImageView cover, int selectedCover,
			boolean animated) {
		if (null == cover)
			return;

		int coverNumber = cover.getCoverFlowPosition();
		int newX = mHalfScreenWidth + cover.getCoverFlowPosition()
				* CoverFlowConfig.COVER_SPACING
				- (int) (cover.getCoverWidth() / 2.0f);
		int newY = mHalfScreenHeight - cover.getCoverHeight() / 2;

		CoverFlowItemAnimation oldAnimation = (CoverFlowItemAnimation) cover
				.getAnimation();
		float oldAngle = oldAnimation != null ? oldAnimation
				.getStopAngleDegrees() : 0;
		int oldZOffset = oldAnimation != null ? oldAnimation.getStopZOffset()
				: 0;
		int oldXOffset = oldAnimation != null ? oldAnimation.getStopXOffset()
				: 0;

		CoverFlowItemAnimation anim = null;

		if (coverNumber < selectedCover) {
			if (oldAngle != CoverFlowConfig.SIDE_COVER_ANGLE
					|| oldXOffset != -CoverFlowConfig.CENTER_COVER_OFFSET
					|| oldZOffset != CoverFlowConfig.SIDE_COVER_ZPOSITION) {
				anim = new CoverFlowItemAnimation();
				anim.setRotation(oldAngle, CoverFlowConfig.SIDE_COVER_ANGLE);
				anim.setViewDimensions(cover.getCoverWidth(),
						cover.getOriginalCoverHeight());
				anim.setXTranslation(oldXOffset,
						-CoverFlowConfig.CENTER_COVER_OFFSET);
				anim.setZTranslation(oldZOffset,
						CoverFlowConfig.SIDE_COVER_ZPOSITION);
				if (animated) {
					anim.setDuration(CoverFlowConfig.BLUR_ANIMATION_DURATION);
				} else {
					anim.setStatic();
				}
			}
		} else if (coverNumber > selectedCover) {
			if (oldAngle != -CoverFlowConfig.SIDE_COVER_ANGLE
					|| oldXOffset != CoverFlowConfig.CENTER_COVER_OFFSET
					|| oldZOffset != CoverFlowConfig.SIDE_COVER_ZPOSITION) {
				anim = new CoverFlowItemAnimation();
				anim.setRotation(oldAngle, -CoverFlowConfig.SIDE_COVER_ANGLE);
				anim.setViewDimensions(cover.getCoverWidth(),
						cover.getOriginalCoverHeight());
				anim.setXTranslation(oldXOffset,
						CoverFlowConfig.CENTER_COVER_OFFSET);
				anim.setZTranslation(oldZOffset,
						CoverFlowConfig.SIDE_COVER_ZPOSITION);
				if (animated) {
					anim.setDuration(CoverFlowConfig.BLUR_ANIMATION_DURATION);
				} else {
					anim.setStatic();
				}
			}
		} else {
			if (oldAngle != 0 || oldXOffset != 0 || oldZOffset != 0) {
				anim = new CoverFlowItemAnimation();
				anim.setRotation(oldAngle, 0);
				anim.setViewDimensions(cover.getCoverWidth(),
						cover.getOriginalCoverHeight());
				anim.setXTranslation(oldXOffset, 0);
				anim.setZTranslation(oldZOffset, 0);
				if (animated) {
					anim.setDuration(CoverFlowConfig.FOCUS_ANIMATION_DURATION);
				} else {
					anim.setStatic();
				}
				anim.setAnimationListener(new Animation.AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						mSelectedCoverView.bringToFront();
						layoutZ(mSelectedCoverView.getCoverFlowPosition(),
								mLowerVisibleCover, mUpperVisibleCover);
					}
				});
			}
		}

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				cover.getLayoutParams());
		params.setMargins(newX, newY, 0, 0);
		params.gravity = Gravity.LEFT | Gravity.TOP;
		cover.setLayoutParams(params);

		if (anim != null) {
			cover.startAnimation(anim);
		}
	}

	/**
	 * 
	 * @param selected
	 * @param lowerBound
	 * @param upperBound
	 */
	private void layoutCovers(int selected, int lowerBound, int upperBound) {
		CoverFlowImageView cover;
		for (int i = lowerBound; i <= upperBound; i++) {
			cover = mOnscreenCovers.get(i);
			layoutCover(cover, selected, true);
		}
	}

	/**
	 * 
	 * @param selected
	 * @param lowerBound
	 * @param upperBound
	 */
	private void layoutZ(int selected, int lowerBound, int upperBound) {
		CoverFlowImageView cover;
		for (int i = upperBound; i > selected; i--) {
			cover = mOnscreenCovers.get(i);
			if (cover != null) {
				mItemContainer.bringChildToFront(cover);
			}
		}
		for (int i = lowerBound; i <= selected; i++) {
			cover = mOnscreenCovers.get(i);
			if (cover != null) {
				mItemContainer.bringChildToFront(cover);
			}
		}
	}

	/**
	 * 
	 * @param bitmap
	 * @param index
	 */
	public void setBitmapForIndex(Bitmap bitmap, int index) {
		Bitmap bitmapWithReflection = CoverFlowImageView.createReflectedBitmap(
				bitmap, CoverFlowConfig.REFLECTION_FRACTION,
				CoverFlowConfig.DROP_SHADOW_RADIUS);
		setReflectedBitmapForIndex(bitmapWithReflection, index);
	}

	/**
	 * CoverFlowConfig
	 * 
	 * @param bitmapWithReflection
	 * @param index
	 */
	public void setReflectedBitmapForIndex(Bitmap bitmapWithReflection,
			int index) {
		mCoverImages.put(index, bitmapWithReflection);
		int originalHeight = (int) ((int) (bitmapWithReflection.getHeight() - 2 * CoverFlowConfig.DROP_SHADOW_RADIUS) / (1 + CoverFlowConfig.REFLECTION_FRACTION));
		mCoverImageHeights.put(index, originalHeight);

		// If this cover is onscreen, set its image and call layoutCover.
		CoverFlowImageView cover = mOnscreenCovers.get(index);
		if (null != cover) {
			cover.setImageBitmap(bitmapWithReflection, originalHeight);
			layoutCover(cover, mSelectedCoverView.getCoverFlowPosition(), false);
		}
	}

	public Bitmap[] getReflectedBitmaps() {
		Bitmap[] result = new Bitmap[mCoverImages.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = mCoverImages.get(i);
		return result;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				mNumberOfImages * CoverFlowConfig.COVER_SPACING
						+ MeasureSpec.getSize(widthMeasureSpec),
				LayoutParams.MATCH_PARENT);
		mItemContainer.setLayoutParams(params);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mHalfScreenWidth = w / 2;
		mHalfScreenHeight = h / 2;

		int lowerBound = Math.max(
				-1,
				(mSelectedCoverView != null ? mSelectedCoverView
						.getCoverFlowPosition() : 0)
						- CoverFlowConfig.COVER_BUFFER);
		int upperBound = Math.min(
				mNumberOfImages - 1,
				(mSelectedCoverView != null ? mSelectedCoverView
						.getCoverFlowPosition() : 0)
						+ CoverFlowConfig.COVER_BUFFER);
		layoutCovers(
				mSelectedCoverView != null ? mSelectedCoverView.getCoverFlowPosition()
						: 0, lowerBound, upperBound);
		centerOnSelectedCover(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mIsSingleTap = event.getPointerCount() == 1;
			if (mIsSingleTap) {
				mStartX = event.getX(0);
			}
			mBeginningCover = mSelectedCoverView.getCoverFlowPosition();
			mStartScrollX = event.getX(0) + mScrollView.getScrollX();
			break;
		case MotionEvent.ACTION_MOVE:
			int scrollOffset = (int) (mStartScrollX - event.getX(0));
			int xOffset = (int) Math.abs(event.getX(0) - mStartX);

			// If finger moves too much, not a single tap anymore:
			mIsSingleTap = mIsSingleTap && (xOffset < 20);

			if (!mIsSingleTap) {
				// Update the scroll position
				mScrollView.scrollTo(scrollOffset, mScrollView.getScrollY());

				// Select new cover
				int newCover = scrollOffset / CoverFlowConfig.COVER_SPACING;

				// make sure we're not out of bounds:
				if (newCover < 0) {
					newCover = 0;
				} else if (newCover >= mNumberOfImages) {
					newCover = mNumberOfImages - 1;
				}

				// Select newCover if appropriate
				if (newCover != mSelectedCoverView.getCoverFlowPosition()) {
					setSelectedCover(newCover);
					// Notify listener
					if (mCoverFlowListener != null
							&& mCoverFlowListener.get() != null) {
						mCoverFlowListener.get().onSelectionChanging(this,
								mSelectedCoverView.getCoverFlowPosition());
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsSingleTap && 0 < mTouchedCovers.size()) {
				int lowest = mTouchedCovers.first();
				int highest = mTouchedCovers.last();
				if (mSelectedCoverView.getCoverFlowPosition() < lowest) {
					setSelectedCover(lowest);
				} else if (mSelectedCoverView.getCoverFlowPosition() > highest) {
					setSelectedCover(highest);
				} else if (lowest <= mSelectedCoverView.getCoverFlowPosition()
						&& highest >= mSelectedCoverView.getCoverFlowPosition()
						&& mCoverFlowListener != null
						&& mCoverFlowListener.get() != null) {
					mCoverFlowListener.get().onSelectionClicked(this,
							mSelectedCoverView.getCoverFlowPosition());
				}
			}
			// Smooth scroll to the center of the cover
			mScrollView.smoothScrollTo(
					mSelectedCoverView.getCoverFlowPosition()
							* CoverFlowConfig.COVER_SPACING,
					mScrollView.getScrollY());

			if (mBeginningCover != mSelectedCoverView.getCoverFlowPosition()) {
				// Notify listener
				if (mCoverFlowListener != null
						&& mCoverFlowListener.get() != null) {
					mCoverFlowListener.get().onSelectionChanged(this,
							mSelectedCoverView.getCoverFlowPosition());
				}
			}
			// Clear touched covers
			mTouchedCovers.clear();
			break;
		}
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int newCover = -1;
			if (event.getX(0) > 0)
				newCover = mSelectedCoverView.getCoverFlowPosition() + 1;
			else if (event.getX(0) < 0)
				newCover = mSelectedCoverView.getCoverFlowPosition() - 1;

			if (0 <= newCover && mNumberOfImages > newCover
					&& mSelectedCoverView.getCoverFlowPosition() != newCover) {
				setSelectedCover(newCover);
				mScrollView.smoothScrollTo(newCover
						* CoverFlowConfig.COVER_SPACING,
						mScrollView.getScrollY());
				// Notify listener
				if (mCoverFlowListener != null
						&& mCoverFlowListener.get() != null) {
					mCoverFlowListener.get().onSelectionChanged(this,
							mSelectedCoverView.getCoverFlowPosition());
				}
			}
			break;
		}
		return true;
	}

	void onTouchItem(CoverFlowImageView cover, MotionEvent event) {
		mTouchedCovers.add(cover.getCoverFlowPosition());
	}

	public void clear() {
		mNumberOfImages = 0;
		mSelectedCoverView = null;
		mOffscreenCovers.clear();
		mOnscreenCovers.clear();
		mCoverImages.clear();
		mCoverImageHeights.clear();
		mDefaultBitmap = null;
		mLowerVisibleCover = -1;
		mUpperVisibleCover = -1;

		// Recreate the item container to force free memory
		LinearLayout parent = (LinearLayout) mItemContainer.getParent();
		parent.removeView(mItemContainer);
		mItemContainer = new FrameLayout(getContext());
		parent.addView(mItemContainer);

	}

	public void setNumberOfImages(int numberOfImages) {
		mNumberOfImages = numberOfImages;

		int lowerBound = Math.max(
				-1,
				(mSelectedCoverView != null ? mSelectedCoverView
						.getCoverFlowPosition() : 0)
						- CoverFlowConfig.COVER_BUFFER);
		int upperBound = Math.min(
				mNumberOfImages - 1,
				(mSelectedCoverView != null ? mSelectedCoverView
						.getCoverFlowPosition() : 0)
						+ CoverFlowConfig.COVER_BUFFER);
		if (null != mSelectedCoverView) {
			layoutCovers(mSelectedCoverView.getCoverFlowPosition(), lowerBound,
					upperBound);
		} else {
			setSelectedCover(0);
		}
		centerOnSelectedCover(false);
	}

	public void centerOnSelectedCover(final boolean animated) {
		if (null == mSelectedCoverView)
			return;

		final int offset = CoverFlowConfig.COVER_SPACING
				* mSelectedCoverView.getCoverFlowPosition();
		mScrollView.post(new Runnable() {
			public void run() {
				if (animated)
					mScrollView.smoothScrollTo(offset, 0);
				else
					mScrollView.scrollTo(offset, 0);
			}
		});
	}

	public void setSelectedCover(int newSelectedCover) {
		if (null != mSelectedCoverView
				&& newSelectedCover == mSelectedCoverView
						.getCoverFlowPosition())
			return;

		if (newSelectedCover >= mNumberOfImages)
			return;

		CoverFlowImageView cover;
		int newLowerBound = Math.max(0, newSelectedCover
				- CoverFlowConfig.COVER_BUFFER);
		int newUpperBound = Math.min(mNumberOfImages - 1, newSelectedCover
				+ CoverFlowConfig.COVER_BUFFER);
		if (null == mSelectedCoverView) {
			// Allocate and display covers from newLower to newUpper bounds.
			for (int i = newLowerBound; i <= newUpperBound; i++) {
				cover = coverForIndex(i);
				mOnscreenCovers.put(i, cover);
				updateCoverBitmap(cover);
				if (i == newSelectedCover) {
					// We'll add it later
					continue;
				} else if (i < newSelectedCover) {
					mItemContainer.addView(cover);
				} else {
					mItemContainer.addView(cover, 0);
				}
				layoutCover(cover, newSelectedCover, false);
			}
			// Add the selected cover
			cover = mOnscreenCovers.get(newSelectedCover);
			mItemContainer.addView(cover);
			layoutCover(cover, newSelectedCover, false);

			mLowerVisibleCover = newLowerBound;
			mUpperVisibleCover = newUpperBound;
			mSelectedCoverView = cover;
			return;
		} else {
			layoutZ(mSelectedCoverView.getCoverFlowPosition(),
					mLowerVisibleCover, mUpperVisibleCover);

		}

		if ((newLowerBound > mUpperVisibleCover)
				|| (newUpperBound < mLowerVisibleCover)) {
			// They do not overlap at all.
			// This does not animate--assuming it's programmatically set from
			// view controller.
			// Recycle all onscreen covers.
			for (int i = mLowerVisibleCover; i <= mUpperVisibleCover; i++) {
				cover = mOnscreenCovers.get(i);
				mOffscreenCovers.add(cover);
				mItemContainer.removeView(cover);
				mOnscreenCovers.remove(i);
			}

			// Move all available covers to new location.
			for (int i = newLowerBound; i <= newUpperBound; i++) {
				cover = coverForIndex(i);
				mOnscreenCovers.put(i, cover);
				updateCoverBitmap(cover);
				if (i == newSelectedCover) {
					// We'll add it later
					continue;
				} else if (i < newSelectedCover) {
					mItemContainer.addView(cover);
				} else {
					mItemContainer.addView(cover, 0);
				}
			}
			cover = mOnscreenCovers.get(newSelectedCover);
			mItemContainer.addView(cover);

			mLowerVisibleCover = newLowerBound;
			mUpperVisibleCover = newUpperBound;
			mSelectedCoverView = cover;
			layoutCovers(newSelectedCover, newLowerBound, newUpperBound);

			return;

		} else if (newSelectedCover > mSelectedCoverView.getCoverFlowPosition()) {
			// Move covers that are now out of range on the left to the right
			// side,
			// but only if appropriate (within the range set by newUpperBound).
			for (int i = mLowerVisibleCover; i < newLowerBound; i++) {
				cover = mOnscreenCovers.get(i);
				if (mUpperVisibleCover < newUpperBound) {
					// Tack it on the right side.
					mUpperVisibleCover++;
					cover.setCoverFlowPosition(mUpperVisibleCover);
					updateCoverBitmap(cover);
					mOnscreenCovers.put(cover.getCoverFlowPosition(), cover);
					layoutCover(cover, newSelectedCover, false);
				} else {
					// Recycle this cover.
					mOffscreenCovers.add(cover);
					mItemContainer.removeView(cover);
				}
				mOnscreenCovers.remove(i);
			}

			mLowerVisibleCover = newLowerBound;

			// Add in any missing covers on the right up to the newUpperBound.
			for (int i = mUpperVisibleCover + 1; i <= newUpperBound; i++) {
				cover = coverForIndex(i);
				mOnscreenCovers.put(i, cover);
				updateCoverBitmap(cover);
				mItemContainer.addView(cover, 0);
				layoutCover(cover, newSelectedCover, false);
			}
			mUpperVisibleCover = newUpperBound;
		} else {
			// Move covers that are now out of range on the right to the left
			// side,
			// but only if appropriate (within the range set by newLowerBound).
			for (int i = mUpperVisibleCover; i > newUpperBound; i--) {
				cover = mOnscreenCovers.get(i);
				if (mLowerVisibleCover > newLowerBound) {
					// Tack it on the left side.
					mLowerVisibleCover--;
					cover.setCoverFlowPosition(mLowerVisibleCover);
					updateCoverBitmap(cover);
					mOnscreenCovers.put(cover.getCoverFlowPosition(), cover);
					layoutCover(cover, newSelectedCover, false);

				} else {
					// Recycle this cover.
					mOffscreenCovers.add(cover);
					mItemContainer.removeView(cover);
				}
				mOnscreenCovers.remove(i);
			}

			mUpperVisibleCover = newUpperBound;

			// Add in any missing covers on the left down to the newLowerBound.
			for (int i = mLowerVisibleCover - 1; i >= newLowerBound; i--) {
				cover = coverForIndex(i);
				mOnscreenCovers.put(i, cover);
				updateCoverBitmap(cover);
				mItemContainer.addView(cover, 0);
				layoutCover(cover, newSelectedCover, false);
			}

			mLowerVisibleCover = newLowerBound;
		}

		if (mSelectedCoverView.getCoverFlowPosition() > newSelectedCover) {
			layoutCovers(newSelectedCover, newSelectedCover,
					mSelectedCoverView.getCoverFlowPosition());
		} else if (newSelectedCover > mSelectedCoverView.getCoverFlowPosition()) {
			layoutCovers(newSelectedCover,
					mSelectedCoverView.getCoverFlowPosition(), newSelectedCover);
		}

		mSelectedCoverView = mOnscreenCovers.get(newSelectedCover);
	}

}
