package com.example.mobilesafe.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.utils.GetAppInfoUtils;
import com.example.mobilesafe.utils.ShowToastUtils;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.RootToolsException;

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
	
	private RemoveAppPackage mAppPackage;

	
	private PopupWindow mPM;
	private LinearLayout ll_startApp;
	private LinearLayout ll_unstallApp;
	private LinearLayout ll_settingApp;
	private LinearLayout ll_shareApp;
	
	private AppInforBean clickedInforBean;
	
	//存储所有的软件App信息。
	private List<AppInforBean> installAppInfos = new Vector<AppInforBean>();
	
	//存储用户软件信息。
	private List<AppInforBean> userAppInfos=new ArrayList<AppInforBean>();
	
	//存储系统软件信息。
	private List<AppInforBean> systemAppInfos=new ArrayList<AppInforBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
//		initData();
		
		registerRemoveReceiver();
		
		initPopWindow();
		
		initEvent();
	}

	
	private void registerRemoveReceiver() {
		mAppPackage = new RemoveAppPackage();
		IntentFilter mFilter=new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		mFilter.addDataScheme("package");
		registerReceiver(mAppPackage, mFilter);
	}


	@Override
	protected void onResume() {
		//初始化数据。
		initData();
		
		super.onResume();
	}
	
	
	private class RemoveAppPackage extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			initData();
			
		}
		
	}
	
	
	private void initPopWindow() {

		View mPouWindowView = View.inflate(getApplicationContext(), R.layout.popup_app_manager_mess, null);
	
		//获取popwindow界面的组件。
		ll_startApp = (LinearLayout) mPouWindowView.findViewById(R.id.ll_app_manager_start_app);
		ll_unstallApp = (LinearLayout) mPouWindowView.findViewById(R.id.ll_app_manager_unstall_app);
		ll_settingApp = (LinearLayout) mPouWindowView.findViewById(R.id.ll_app_manager_setting_app);
		ll_shareApp = (LinearLayout) mPouWindowView.findViewById(R.id.ll_app_manager_share_app);
		
		//给popwindow组件设置点击事件逻辑。
		OnClickListener mClickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				
				
				case R.id.ll_app_manager_share_app:
					//分享App.
					shareApp();
					break;
				
				
				case R.id.ll_app_manager_start_app:
					//启动App.
					startApp();
					break;
					
				case R.id.ll_app_manager_unstall_app:
					//卸载App.
					unIstallApp();
					break;
				
				case R.id.ll_app_manager_setting_app:
					//设置App.
					settingApp();
					break;
				default:
					break;
				}
				
				mPM.dismiss();
			}
		};

		//给popupwindow设置监听器。
		ll_settingApp.setOnClickListener(mClickListener);
		ll_unstallApp.setOnClickListener(mClickListener);
		ll_shareApp.setOnClickListener(mClickListener);
		ll_startApp.setOnClickListener(mClickListener);
		
		
		mPM = new PopupWindow(mPouWindowView,-2,-2);//-2表示包括内容。
		
		//设置可以获得焦点。
		mPM.setFocusable(true);
		
		//设置透明背景。
		mPM.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		
		//设置外部点击消失
		mPM.setOutsideTouchable(true);
		
		//显示。
		mPM.setAnimationStyle(R.style.popup_dialog);
	}

	
	
	protected void settingApp() {
		 
		Intent setting=new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
		setting.setData(Uri.parse("package:com.example.mobilesafe"));
		startActivity(setting);
		
	}

	protected void unIstallApp() {
		if (clickedInforBean.isSystem()) {
			
			
			//系统软件。
			try {
				
				ShowToastUtils.showToast("系统软件被删除。", this);
				
				//卸载系统软件。
				RootTools.sendShell("mount -o remount rw /system", 3000);
				RootTools.sendShell("rm -r "+clickedInforBean.getSourceDir(), 3000);
				RootTools.sendShell("mount -o remount -r /system", 3000);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RootToolsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
		
			//用户软件。
			Intent userAppIntent=new Intent("android.intent.action.DELETE");
			userAppIntent.setData(Uri.parse("package:"+clickedInforBean.getPackageName()));
			startActivity(userAppIntent);
			
		
		}
	}

	protected void startApp() {
		Intent intentForPackage = getPackageManager().getLaunchIntentForPackage(clickedInforBean.getPackageName());
		startActivity(intentForPackage);
	}

	protected void shareApp() {

		showShare();
	}

	
	
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle(getString(R.string.share));
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("魔法革");
		 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		 oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
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
			return installAppInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			 
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
				
				//清空userAppInfos和systemAppInfos里的数据。
				userAppInfos.clear();
				systemAppInfos.clear();
				
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

		 lv_appInfo.setOnItemClickListener(new OnItemClickListener() {



			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				//显示popupWindow窗口。
				mPM.showAsDropDown(view, 45, -view.getHeight());
				
				//保存卸载文件数据。
				
				clickedInforBean = installAppInfos.get(position);
			}
		});
		
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
		
		
		//ROM 进度条内存空间大小比例显示。
		pb_romSpace = (ProgressBar) findViewById(R.id.pb_progress_rom_space);
	}
	
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mAppPackage);
		super.onDestroy();
	}
}
