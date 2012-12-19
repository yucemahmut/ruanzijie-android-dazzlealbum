package cn.ancore.dazzlealbum.components.slideshow.vintage;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * slideshow 页面适配器
 * @author magicruan
 * @version 4.2 2012-12-12
 */
public class VintagePagerAdapter extends PagerAdapter{

	private static final String TAG = VintagePagerAdapter.class.getSimpleName();
	
	private HashMap<Integer, WeakReference<VintageItemView>> mItemViews;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
