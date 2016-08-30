package com.example.mobilesafe.difineView;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingCustomView extends RelativeLayout {

	
	private TextView customTextView;
	private View customSettingView;
	private boolean isOpen=false;
	private ImageView customDisplay;

	/**
	 *布局文件中实例化调用。 
	 * @param context
	 * @param attrs
	 */
	
	public SettingCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView();
		
		initDate(attrs);

		initEvent();
	}

	/**
	 * 
	 * @author Administrator
	 *@company Newbie
	 *@date 2016-8-29
	 *@des 暴露一个接口，供其他程序员调用。
	 */
	public interface onToggleChangeListener{
		void toggleChange(View view,boolean isOpen);
	}
	
	private onToggleChangeListener mToggleChangeListener;
	
	public void setOnToggleListener(onToggleChangeListener toggleChangeListener){
		this.mToggleChangeListener=toggleChangeListener;
	}
	
	/**
	 * 初始化界面图标显示。
	 * @param isOpen
	 */
	public void setToggleOn(boolean isOpen){
		this.isOpen = isOpen;
		if (isOpen) {
			customDisplay.setImageResource(R.drawable.on);
		}else {
			customDisplay.setImageResource(R.drawable.off);
		}
	}
	
	private void initEvent() {
		//给customSettingView根布局添加点击事件。
		customSettingView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isOpen=!isOpen;
				if (isOpen) {
					customDisplay.setImageResource(R.drawable.on);
				}else {
					customDisplay.setImageResource(R.drawable.off);
				}
				
				
				if (mToggleChangeListener!=null) {
					
					//设置监听动作。
					mToggleChangeListener.toggleChange(SettingCustomView.this,isOpen);
				}
			}
			
			 
			
		});
		
	}

	private void initDate(AttributeSet attrs) {
		//取出属性值。
		String displayText = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "display_text");
		//获取背景选择器的内容。
		String attributeValue = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.example.mobilesafe", "bg_custom_selector");
		
		switch (Integer.parseInt(attributeValue)) {
		
		case 0:
			customSettingView.setBackgroundResource(R.drawable.first_setting_selector);
			break;

		case 1:
			customSettingView.setBackgroundResource(R.drawable.middle_setting_selector);
			break;
			
		case 2:
			customSettingView.setBackgroundResource(R.drawable.last_setting_selector);
			break;
			
		default:
			break;
		}
		
		//设置属性值。
		customTextView.setText(displayText);
		
	}

	//初始化界面。
	private void initView() {

		customSettingView = View.inflate(getContext(), R.layout.custom_view_setting_item, this);
		customTextView = (TextView) customSettingView.findViewById(R.id.custom_tv_display_text);
	
		customDisplay = (ImageView) customSettingView.findViewById(R.id.custom_iv_display_log);
	}

	/**
	 * 代码中实例化。
	 * @param context
	 */
	public SettingCustomView(Context context) {
		 this(context, null);
	}
	
	

}
