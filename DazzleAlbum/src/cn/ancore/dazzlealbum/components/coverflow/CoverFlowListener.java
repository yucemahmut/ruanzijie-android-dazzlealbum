package cn.ancore.dazzlealbum.components.coverflow;

/**
 * CoverFlow 监听事件
 * 
 * @author magicruan
 * @version 1.0 2012-12-17
 */
public interface CoverFlowListener {

	/**
	 * 封面切换�?
	 * @param coverFlow
	 * @param index
	 */
	public void onSelectionChanging(CoverFlowView coverFlow, int index);

	/**
	 * 封面切换完毕
	 * @param coverFlow
	 * @param index
	 */
	public void onSelectionChanged(CoverFlowView coverFlow, int index);

	/**
	 * 封面点击事件
	 * @param coverFlow
	 * @param index
	 */
	public void onSelectionClicked(CoverFlowView coverFlow, int index);

}
