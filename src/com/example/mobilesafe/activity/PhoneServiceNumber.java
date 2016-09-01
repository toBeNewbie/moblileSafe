package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.PhoneServiceNameBean;
import com.example.mobilesafe.bean.PhoneServiceNumberBean;
import com.example.mobilesafe.dao.AddressPhoneLocationDao;

public class PhoneServiceNumber extends Activity {
	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private ExpandableListView expandableListView;
	private LinearLayout linearLayout;

	private List<PhoneServiceNameBean> nameBeans = new ArrayList<PhoneServiceNameBean>();

	private List<List<PhoneServiceNumberBean>> numlLists = new ArrayList<List<PhoneServiceNumberBean>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();

		initEvent();
	}

	Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				linearLayout.setVisibility(View.VISIBLE);
				expandableListView.setVisibility(View.GONE);
				break;
			case 2:
				linearLayout.setVisibility(View.GONE);
				expandableListView.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};

	private void initEvent() {
		
		
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				//获取服务手机电话号码。
				String phoneNumber=numlLists.get(groupPosition).get(childPosition).getServiceNumber();
				//启动打电话界面。
				Intent phoneIntent=new Intent(Intent.ACTION_CALL);
				phoneIntent.setData(Uri.parse("tel:"+phoneNumber));
				startActivity(phoneIntent);
				return true;
			}
		});
		
		
		
		new Thread() {
			public void run() {

				// 开始加载数据。
				myHandler.obtainMessage(LOADING).sendToTarget();
				
				nameBeans = AddressPhoneLocationDao.getPhoneName();
				for (PhoneServiceNameBean nameBean : nameBeans) {

					List<PhoneServiceNumberBean> phoneNumber = AddressPhoneLocationDao
							.getPhoneNumber(nameBean);

					numlLists.add(phoneNumber);

				}
				
				SystemClock.sleep(2000);
				// 加载数据完成。

				myHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();

	}

	private void initView() {
		setContentView(R.layout.phone_service_number_name);
		expandableListView = (ExpandableListView) findViewById(R.id.elv_phone_number_service_message);
		//消除系统自带的选择器。
		expandableListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		
		SortExpandableAdapter expandableAdapter = new SortExpandableAdapter();
		expandableListView.setAdapter(expandableAdapter);

		linearLayout = (LinearLayout) findViewById(R.id.ll_progressbar_root);

	}

	// 给expandlist设置适配器。
	public class SortExpandableAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return nameBeans.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return numlLists.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// 组的view显示。
			TextView textView = null;
			if (convertView == null) {
				textView = new TextView(getApplicationContext());
				textView.setTextSize(20);
				textView.setTextColor(Color.BLACK);
				textView.setBackgroundResource(R.drawable.dialog_set_password_selector);
			} else {
				textView = (TextView) convertView;
			}

			// 设置组view的文本框内容。
			textView.setText(nameBeans.get(groupPosition).getName());
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// 子view的显示。
			// 组的view显示。
			TextView textView = null;
			if (convertView == null) {
				textView = new TextView(getApplicationContext());
				textView.setTextSize(15);
				textView.setTextColor(Color.RED);
				textView.setBackgroundResource(R.drawable.middle_setting_selector);
			} else {
				textView = (TextView) convertView;
			}
			PhoneServiceNumberBean	numberService = numlLists.get(groupPosition).get(childPosition);
			textView.setText(numberService.getServiceName()+" : "+numberService.getServiceNumber());
			 return textView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}

}
