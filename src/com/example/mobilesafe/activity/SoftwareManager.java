package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.utils.GetAppInfoUtils;

public class SoftwareManager extends Activity {
	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private LinearLayout ll_progress;
	private TextView tv_romSpace;
	private TextView tv_sdSpace;
	private StickyListHeadersListView lv_appInfo;
	private MyAdapter mAdapter;

	private ProgressBar pb_romSpace;
	private long totalSpace;
	private long romSpace;
	private long sdSpace;
	
	//存储所有的软件App信息。
	private List<AppInforBean> installAppInfos = new ArrayList<AppInforBean>();
	
	//存储用户软件信息。
	private List<AppInforBean> userAppInfos=new ArrayList<AppInforBean>();
	
	//存储系统软件信息。
	private List<AppInforBean> systemAppInfos=new ArrayList<AppInforBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		initData();
		
		initEvent();
	}

	Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING://正在加载数据。
				{
					
				lv_appInfo.setVisibility(View.GONE);
				ll_progress.setVisibility(View.VISIBLE);
				break;
				
				}
			case FINISH://加载数据完成。
			{	
				lv_appInfo.setVisibility(View.VISIBLE);
				ll_progress.setVisibility(View.GONE);
				
				//格式化手机内存空间。
				tv_romSpace.setText("ROM可用内存 :"+Formatter.formatFileSize(getApplicationContext(), romSpace));
				tv_sdSpace.setText("SD可用内存："+Formatter.formatFileSize(getApplicationContext(), sdSpace));
				
				
				//设置进度条的文字样式。
				pb_romSpace.setProgress((int) (romSpace*100/totalSpace));
				
				//刷新界面。
				mAdapter.notifyDataSetChanged();
				break;
			}
			default:
				break;
			}
		};
	};
	
	
	//给界面ListView设置适配器。
	private class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter{

		@Override
		public int getCount() {
//			return userAppInfos.size()+systemAppInfos.size();
			return installAppInfos.size();
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
			
			/*if (position == 0) {
				
				
				//用户软件的位置。
				TextView tv_userAppInfos = new TextView(getApplicationContext());
				tv_userAppInfos.setTextColor(Color.GREEN);
				tv_userAppInfos.setBackgroundColor(Color.GRAY);
				tv_userAppInfos.setText("用户软件("+ userAppInfos.size()+")");
				return tv_userAppInfos;
			
			
			}else if (position==(userAppInfos.size()+1)) {
				
				//系统软件的位置。
				TextView tv_systemAppInfos = new TextView(getApplicationContext());
				tv_systemAppInfos.setTextColor(Color.GREEN);
				tv_systemAppInfos.setBackgroundColor(Color.GRAY);
				tv_systemAppInfos.setText("用户软件("+ systemAppInfos.size()+")");
				return tv_systemAppInfos;
			}
			*/
			//用户软件或者是系统软件。
			ViewHolder mViewHolder=null;
			if (convertView!=null) {
				//有缓存，并且不是textView.
				mViewHolder = (ViewHolder) convertView.getTag();
			}else {
				//没有缓存。
				convertView = View.inflate(getApplicationContext(), R.layout.item_app_info_mess, null);
				
				
				mViewHolder = new ViewHolder();
				
				
				mViewHolder.tv_appLocation=(TextView) convertView.findViewById(R.id.tv_app_location_mess);
				mViewHolder.tv_appName=(TextView) convertView.findViewById(R.id.tv_app_name_mess);
				mViewHolder.tv_appSize=(TextView) convertView.findViewById(R.id.tv_app_take_space);
				mViewHolder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_app_info_icon);
			
				
				convertView.setTag(mViewHolder);
			}
			
			AppInforBean inforBean=null;
			
			/*//取值，赋值给界面。
			if (position<=userAppInfos.size()) {
				//用户软件数据界面
				inforBean = installAppInfos.get(position);
			}else {
				inforBean = installAppInfos.get(position);
			}*/
			
			inforBean=installAppInfos.get(position);
			
			//显示数据。
			
			//软件图标。
			mViewHolder.iv_icon.setImageDrawable(inforBean.getIcon());
			//软件的安装路径。
			mViewHolder.tv_appLocation.setText(inforBean.isInstallSD()?"安装在存储卡":"安装在系统空间");
			//软件的应用名称
			mViewHolder.tv_appName.setText(inforBean.getAppName());
			//软件的大小
			mViewHolder.tv_appSize.setText(Formatter.formatFileSize(getApplicationContext(), inforBean.getAppTakeUpSize()));
			
			return convertView;
		}

		
		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			AppInforBean appInforBean = installAppInfos.get(position);
			
			TextView tv_systemAppInfos = new TextView(getApplicationContext());
			tv_systemAppInfos.setTextColor(Color.GREEN);
			tv_systemAppInfos.setBackgroundColor(Color.GRAY);
			
			if (!appInforBean.isSystem()) {
				tv_systemAppInfos.setText("用户软件("+  userAppInfos.size()+")");
			}else {
				tv_systemAppInfos.setText("系统软件("+systemAppInfos.size()+")");
				
			}
			return tv_systemAppInfos;
		}

		@Override
		public long getHeaderId(int position) {

			AppInforBean appInforBean = installAppInfos.get(position);
			if (appInforBean.isSystem()) {
				return 1;
			}else {
				return 2;
			}
		}
		
	}
	
	private class ViewHolder{
		TextView tv_appName;
		TextView tv_appLocation;
		TextView tv_appSize;
		ImageView iv_icon;
	}
	
	
	private void initData() {
		new Thread(){


			public void run() {
				//发送加载数据的消息。
				mHandler.obtainMessage(LOADING).sendToTarget();
				romSpace = GetAppInfoUtils.getFreeSpace();
				sdSpace = GetAppInfoUtils.getSdFreeSpace();
				totalSpace = GetAppInfoUtils.getTotalSpace();
				
				
				installAppInfos=GetAppInfoUtils.getInstallAppInfos(getApplicationContext());
				//分类添加数据。
				for (AppInforBean inforBean : installAppInfos) {
					if (inforBean.isSystem()) {
						systemAppInfos.add(inforBean);
						
					}else {
						userAppInfos.add(inforBean);
					}
				}
				installAppInfos.clear();
				installAppInfos.addAll(userAppInfos);
				installAppInfos.addAll(systemAppInfos);
				
				SystemClock.sleep(2000);
				//加载数据完成。
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private void initEvent() {
/*
		//给listView设置滑动事件。
		lv_appInfo.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
//					//如果滑动到软件管家系统软件，改变文字。
//					if (firstVisibleItem>=((userAppInfos.size()+1))) {
//						tv_appInfoNum.setText("系统软件("+systemAppInfos.size()+")");
//					}else {
//						tv_appInfoNum.setText("用户软件("+userAppInfos.size()+")");
//					}
			}
		});*/
	}

	/**
	 * 初始化界面。
	 */
	private void initView() {
		setContentView(R.layout.activity_software_steward);
		
		ll_progress = (LinearLayout) findViewById(R.id.ll_progressbar_root);//显示进度条。
		
		tv_romSpace = (TextView) findViewById(R.id.tv_display_rom_space);//rom手机内存大小。
		
		tv_sdSpace = (TextView) findViewById(R.id.tv_display_sd_space);//sd卡手机内存大小。
		
		
		//应用程序信息。
		lv_appInfo = (StickyListHeadersListView) findViewById(R.id.lv_app_info_manager);
		mAdapter = new MyAdapter();
		lv_appInfo.setAdapter(mAdapter);
		
//		tv_appInfoNum = (TextView) findViewById(R.id.tv_app_info_number);
		
		//ROM 进度条内存空间大小比例显示。
		pb_romSpace = (ProgressBar) findViewById(R.id.pb_progress_rom_space);
	}
}
