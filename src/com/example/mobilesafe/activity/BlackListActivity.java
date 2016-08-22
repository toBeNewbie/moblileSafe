package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.BlcakListBean;
import com.example.mobilesafe.dao.BlackListDao;
import com.example.mobilesafe.db.BlackListDB;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-21
 *@des 黑名单显示的界面。
 */
public class BlackListActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private ImageView iv_add_blacklist;
	private LinearLayout ll_progressbar_root;
	private ListView lv_black_showdata;
	private ImageView iv_black_nodata;
	
	private MyAdapter myAdapter;
	private BlackListDao blackListDao;

	private List<BlcakListBean> mListBeans = new ArrayList<BlcakListBean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//初始化界面
		initView();
		
		//初始化数据。
		initData();
		
		//初始化事件
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		
	}

	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING://加载数据
				
				//显示加载数据的对话框，隐藏listview 和 nodata
				ll_progressbar_root.setVisibility(View.VISIBLE);
				lv_black_showdata.setVisibility(View.GONE);
				iv_black_nodata.setVisibility(View.GONE);
				break;
			
			case FINISH://加载数据完成。
				
				//隐藏加载数据的对话框，
				ll_progressbar_root.setVisibility(View.GONE);
				if (mListBeans.isEmpty()) {
					iv_black_nodata.setVisibility(View.VISIBLE);
					lv_black_showdata.setVisibility(View.GONE);
				}else {
					//有数据显示listview 没有数据显示nodata.
					iv_black_nodata.setVisibility(View.GONE);
					lv_black_showdata.setVisibility(View.VISIBLE);
				}
				//刷新界面。
				myAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	


	
	
	//显示黑名单数据的适配器。
	public class MyAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListBeans.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListBeans.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHoderBlacklist hoderBlacklist= null;
			if (convertView==null) {
				convertView = View.inflate(BlackListActivity.this, R.layout.item_black_list, null);
				hoderBlacklist = new ViewHoderBlacklist();
				hoderBlacklist.tv_phone=(TextView) convertView.findViewById(R.id.tv_black_lv_phone);
				hoderBlacklist.tv_mode=(TextView) convertView.findViewById(R.id.tv_black_lv_mode);
				hoderBlacklist.iv_delete=(ImageView) convertView.findViewById(R.id.iv_delete_item_blacklist);
				convertView.setTag(hoderBlacklist);
				
			}else {
				hoderBlacklist = (ViewHoderBlacklist) convertView.getTag();
			}
			
			final BlcakListBean listBean =(BlcakListBean) getItem(position);
			//显示黑名单号码
			hoderBlacklist.tv_phone.setText(listBean.getPhone());
			
			switch (listBean.getMode()) {
			case BlackListDB.SMS_MODE:
				hoderBlacklist.tv_mode.setText("短信拦截");
				break;
			case BlackListDB.PHONE_MODE:
				hoderBlacklist.tv_mode.setText("电话拦截");
				break;
			case BlackListDB.ALL_MODE:
				hoderBlacklist.tv_mode.setText("全部拦截");
				break;
			default:
				break;
			}
			hoderBlacklist.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mListBeans.remove(listBean);
					blackListDao.deleteBlacklist(listBean.getPhone());
					myAdapter.notifyDataSetChanged();
				}
			});
			
			return convertView;
		}
		
	}
	
	
	class ViewHoderBlacklist {
		
		TextView tv_phone;
		
		TextView tv_mode;
		
		ImageView iv_delete;
		
	}
	
	
	private void initView() {
		setContentView(R.layout.activity_blacklist);
		
		//添加黑名单按钮。
		iv_add_blacklist = (ImageView) findViewById(R.id.iv_add_blacklist);
		
		//加载数据的对话框布局
		ll_progressbar_root = (LinearLayout) findViewById(R.id.ll_progressbar_root);
		
		//不显示数据时的布局。
		iv_black_nodata = (ImageView) findViewById(R.id.iv_black_nodata);
		
		blackListDao = new BlackListDao(this);
		
		myAdapter = new MyAdapter();
		lv_black_showdata = (ListView) findViewById(R.id.lv_black_showdata);
		lv_black_showdata.setAdapter(myAdapter);
 
			
		
		
	}
	
	
	private void initData() {
		
		new Thread(){
			public void run() {
				//发送加载数据的消息
				mHandler.obtainMessage(LOADING).sendToTarget();
				
				//加载数据。
			mListBeans = blackListDao.getBlackList();
			System.out.println(mListBeans);
			SystemClock.sleep(2000);
				//加载数据完成
			mHandler.obtainMessage(FINISH).sendToTarget();
			}
			
			
		}.start();
		
	}
	
	
}
