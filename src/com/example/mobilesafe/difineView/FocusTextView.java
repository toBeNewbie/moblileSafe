package com.example.mobilesafe.difineView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class FocusTextView extends TextView {

	
	/**
	 * 配置文件反射调用。
	 * @param context
	 * @param attrs
	 */
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 实例化中调用。
	 * @param context
	 */
	public FocusTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 永远获取焦点。
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
