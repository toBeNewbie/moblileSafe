package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.ContactBean;
import com.example.mobilesafe.dao.ReadContact;
import com.example.mobilesafe.spUtils.myConstantValue;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-19
 *@des 显示联系人界面。
 */
public class ContactList extends ListActivity {
	protected static final int LOADING = 1;
	protected static final int DONE = 2;
	private ListView listView;
	private myAdapter adapter;
	public List<ContactBean> contactBeans = new ArrayList<ContactBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//初始化界面
		initView();
	 
		//加载数据。
		initData();
		
		initEvent();
	}

	/**
	 * 获取数据，传递数据到上一个界面。
	 */
	private void initEvent() {
		 listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ContactBean contac = (ContactBean) listView.getItemAtPosition((int) listView.getItemIdAtPosition(position));
				Intent intent=new Intent();
				intent.putExtra(myConstantValue.MOBILE_SAFE_NUMBER, contac.getPhoneNumber());
				setResult(1, intent);
				finish();
			}
		});
	}

	Handler mHandler=new Handler(){
		private ProgressDialog dialog;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				listView.setVisibility(View.GONE);
				dialog = new ProgressDialog(ContactList.this);
				dialog.setTitle("警告");
				dialog.setMessage("魔法革正在玩命加载数据中。。。。");
				dialog.show();
				break;
			case 2:
				dialog.dismiss();
				adapter.notifyDataSetChanged();
				listView.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};
	
	
	/**
	 * 加载界面数据。
	 */
	private void initData() {
		
		new Thread(){
			@Override
			public void run() {
				//提醒用户正在更新数据
				mHandler.obtainMessage(LOADING).sendToTarget();
				
				//加载数据完成。
				contactBeans=(ArrayList<ContactBean>) ReadContact.readContacts(getApplicationContext());
				
				SystemClock.sleep(1000);
				mHandler.obtainMessage(DONE).sendToTarget();
				
			}
		}.start();
		
	}


	/**
	 * 
	 * @author Administrator
	 *@company Newbie
	 *@date 2016-8-19
	 *@des 联系人列表的适配器
	 */
	class myAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contactBeans.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return contactBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView=View.inflate(getApplicationContext(), R.layout.contact_item_list, null);
			
				holder.tv_contact_name = (TextView) convertView.findViewById(R.id.tv_contact_name);
				holder.tv_contact_number=(TextView) convertView.findViewById(R.id.tv_contact_number);
			
				convertView.setTag(holder);
			}else {
				holder=(ViewHolder) convertView.getTag();
			}
			
			ContactBean contactBean = (ContactBean) getItem(position);
			
			//给界面设置联系人数据。
			holder.tv_contact_name.setText(contactBean.getContactName());
			holder.tv_contact_number.setText(contactBean.getPhoneNumber());
			
			return convertView;
		}
		
	}
	
	class ViewHolder{
		TextView tv_contact_name;
		TextView tv_contact_number;
	}
	
	/**
	 * 初始化界面。
	 */
	private void initView() {
		listView = getListView();
		adapter = new myAdapter();
		listView.setAdapter(adapter);
	}
}
