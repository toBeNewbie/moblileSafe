package com.example.mobilesafe.difineView;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.dao.LockedDataDao;

public class BaseAppLockedFragment extends Fragment {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private MyAdapter mAdapter;
	private List<AppInforBean> mApps = new ArrayList<AppInforBean>();
	private LockedDataDao mLockedDataDao;

	
	//构造函数，初始化程序锁访问数据dao层
	public BaseAppLockedFragment(){
		
	}
	
	public void setLockedDao(LockedDataDao dataDao){
		this.mLockedDataDao=dataDao;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	
	Handler mHandler=new Handler(){
		private ProgressDialog progressDialog;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING://正在加载数据
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setTitle("魔法革提醒");
				progressDialog.setMessage("魔法革正在玩命加载数据中.......");
				progressDialog.show();
				break;
			case FINISH://数据加载完成
				if (progressDialog!=null && progressDialog.isShowing()) {
					
					progressDialog.dismiss();
					
					progressDialog=null;
				}
				
				//显示数据
				mAdapter.notifyDataSetChanged();
				
				break;
			default:
				break;
			}
		};
	};
	
	List<AppInforBean> installAppInfos;
	public void setInstallAppInfos(List<AppInforBean> installAppInfos){
		this.installAppInfos=installAppInfos;
	}
	
	List<String> allIstallAppNames;
	public void setAllInstallAppNames(List<String> allIstallAppNames){
		this.allIstallAppNames = allIstallAppNames;
	}
	
	private void initData(){
		new Thread(){
			public void run() {
				//发送加载数据的消息
				
				mHandler.obtainMessage(LOADING).sendToTarget();
				//清除原集合数据
				mApps.clear();
				
				
				for (AppInforBean appInforBean : installAppInfos) {
					if ((BaseAppLockedFragment.this instanceof LockedAppFragment) && 
							allIstallAppNames.contains(appInforBean.getPackageName())) {
						//应用程序是加锁的
						mApps.add(appInforBean);
						
					}else if ((BaseAppLockedFragment.this instanceof UnlockedAppFragment) && 
							!allIstallAppNames.contains(appInforBean.getPackageName())) {
						//应用程序是未加锁的
						mApps.add(appInforBean);
					}else {
						//不作处理
					}
				}
				
				//发送数据加载完成的消息
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}
	
	
	private class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mApps.size();
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
			// TODO Auto-generated method stub
			ViewHolder mViewHolder=null;
			if (convertView==null) {
				
				mViewHolder=new ViewHolder();
				convertView=View.inflate(getActivity(), R.layout.item_apps_locked_info_mess, null);
				
				mViewHolder.appIcon = (ImageView) convertView.findViewById(R.id.iv_item_app_locked_icon);
				mViewHolder.tvPackageName = (TextView) convertView.findViewById(R.id.tv_item_locked_app_name);
				mViewHolder.isLocked=(ImageView) convertView.findViewById(R.id.iv_item_app_if_locked);
				
				convertView.setTag(mViewHolder);
				
			}else {
				
				mViewHolder = (ViewHolder) convertView.getTag();
			
			}
			
			//赋值显示数据
			if (position<0 || position>=mApps.size()) {
				return convertView;
			}
			
			final AppInforBean inforBean = mApps.get(position);
			mViewHolder.appIcon.setImageDrawable(inforBean.getIcon());
			mViewHolder.tvPackageName.setText(inforBean.getAppName());
			
			if (BaseAppLockedFragment.this instanceof LockedAppFragment) {
				//加锁
				mViewHolder.isLocked.setImageResource(R.drawable.unlock);
				
			}else {
				//未加锁
				mViewHolder.isLocked.setImageResource(R.drawable.lock);
			}
			
			final View mView=convertView;
			mViewHolder.isLocked.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (BaseAppLockedFragment.this instanceof LockedAppFragment) {
						//app加锁
						mLockedDataDao.removeLockedPackName(inforBean.getPackageName());
						
						//给convertView添加左移动画
						mView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.unlocked_app_anim));
						
					}else {
						mLockedDataDao.addLockedPackName(inforBean.getPackageName());
						//给convertView添加右移动画
						mView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.locked_app_anim));
					}
					
					
					//子线程更新界面
					new Thread(){
						public void run() {
							SystemClock.sleep(500);
							getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									mApps.remove(inforBean);
									mAdapter.notifyDataSetChanged();
									
								}
							});
						};
					}.start();
				}
			});
			
			return convertView;
		}

		
		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			
			TextView tv_tag = new TextView(getActivity());
			tv_tag.setBackgroundColor(Color.GRAY);
			tv_tag.setTextSize(20);
			tv_tag.setTextColor(Color.WHITE);
			
			
			//条件
			AppInforBean appInforBean = mApps.get(position);
			if (appInforBean.isSystem()) {
				//系统软件
				tv_tag.setText("系统软件");
			}else {
				//用户软件
				tv_tag.setText("用户软件");
			}
			return tv_tag;
		}

		@Override
		public long getHeaderId(int position) {
			AppInforBean appInforBean = mApps.get(position);
			if (appInforBean.isSystem()) {
				//系统软件
				return 1;
			}else {
				//用户软件
				return 2;
			}
		}
		
	}
	
	
	private class ViewHolder{
		ImageView appIcon;
		ImageView isLocked;
		TextView tvPackageName;
	}
	
	
	/**
	 * 初始化fragment界面显示
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		StickyListHeadersListView appLockedListView = (StickyListHeadersListView) inflater.inflate(R.layout.fragment_apps_locked, null);

		//保证适配器只被实例化一次
		if (mAdapter==null) {
			
			mAdapter = new MyAdapter();
		}
		
		appLockedListView.setAdapter(mAdapter);
		return appLockedListView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		//初始化数据
		initData();
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
