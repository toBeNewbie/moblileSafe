package com.example.mobilesafe.difineView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomDialogView extends Dialog {

	

	private ListView lv_displayStyle;

	//电话归属地来电样式名称
	public static final String[] locationStyleNames = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
	
	//电话归属地来定背景样式。
	public static final int[] locationStyleIma = {R.drawable.call_locate_white,
										R.drawable.call_locate_orange,
										R.drawable.call_locate_blue,
										R.drawable.call_locate_gray,
										R.drawable.call_locate_green};
	
	public CustomDialogView(Context context, int theme) {
		
		super(context, theme);
		
		//设置样式。
		Window mWindow = getWindow();
		LayoutParams layoutParams = mWindow.getAttributes();
		
		//设置自定义对话框的底部对齐。
		layoutParams.gravity=Gravity.BOTTOM;
		
		//重新设置参数。
		mWindow.setAttributes(layoutParams);
	}

	private SettingCustomView settingCustomView = null;
	public CustomDialogView(Context context, SettingCustomView settingCustomView) {
		
		this(context, R.style.Theme_Dialog_Phone_Location);//0               代表默认的对话框样式。
		
		this.settingCustomView=settingCustomView;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		
		initEvent();
	}
	
	
	
	private void initEvent() {
		
		//给电话归属地样式listview，设置点击事件。
	 lv_displayStyle.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			//保存点击的位置。
			splashUtils.putInt(getContext(), myConstantValue.PHONE_LOCATION_STYLE_INDEX, position);
			
			//设置电话归属地
			settingCustomView.setText("归属地显示样式("+locationStyleNames[position]+")");
			
			//关闭对话框。
			dismiss();
			
		}

		 
	 
	 });
		
	}



	//给归属地样式设置样式。
	public class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return locationStyleNames.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView==null) {
				convertView=View.inflate(getContext(), R.layout.item_lv_phone_location_toast_style, null);
			
			} 
			
			View phoneLocationImage = convertView.findViewById(R.id.v_phone_location_style_image);
			TextView phoneLocationText = (TextView) convertView.findViewById(R.id.tv_phone_location_style_text);
			ImageView phoneStyleSelect = (ImageView) convertView.findViewById(R.id.iv_phone_location_style_select_image);
			
			phoneLocationImage.setBackgroundResource(locationStyleIma[position]);
			phoneLocationText.setText(locationStyleNames[position]);
			
			
			//判断是否显示选择标记。
			if (splashUtils.getInt(getContext(), myConstantValue.PHONE_LOCATION_STYLE_INDEX, 0)==position) {
				phoneStyleSelect.setVisibility(View.VISIBLE);
			}else {
				phoneStyleSelect.setVisibility(View.GONE);
			}
			
			return convertView;
			
		}
			
	}
			
		

	private void initView() {
		
		setContentView(R.layout.dialog_item_phone_toast_style);
		
		lv_displayStyle = (ListView) findViewById(R.id.lv_item_display_phone_style);
		
		MyAdapter myAdapter = new MyAdapter();
		
		
		lv_displayStyle.setAdapter(myAdapter);
		
	}

}
