package cn.chaoren.util;

import java.util.List;

import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;

public class UIUtils {

	/**
	 * 用于UI界面中button的初始化
	 * 
	 * @param v
	 *            初始化的button
	 * @return OnPreDrawListener对象
	 */
	public static OnPreDrawListener initButtonState(final View v) {
		OnPreDrawListener op = new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				v.setSelected(true);
				return true;
			}
		};
		v.getViewTreeObserver().addOnPreDrawListener(op);
		return op;
	}

	/**
	 * 用于切换UI界面中button的状态图
	 * 
	 * @param vs
	 *            牵扯到要改变状态所有button的list
	 * @param v
	 *            当前点击的button
	 * @param op
	 *            初始化的view的OnPreDrawListener对象
	 */
	public static void buttonStateChange(List<View> vs, View v,
			OnPreDrawListener op) {
		for (View view : vs) {
			view.getViewTreeObserver().removeOnPreDrawListener(op);
			view.setSelected(false);
		}
		v.setSelected(true);
	}
}
