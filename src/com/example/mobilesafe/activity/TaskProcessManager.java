package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.difineView.SettingCustomView;
import com.example.mobilesafe.difineView.SettingCustomView.onToggleChangeListener;
import com.example.mobilesafe.service.LockScreenCleanTaskService;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.example.mobilesafe.utils.AntiThrefServiceUtils;
import com.example.mobilesafe.utils.ShowToastUtils;
import com.example.mobilesafe.utils.TastInfosUtils;

public class TaskProcessManager extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private ProgressBar pb_progressNum;
	private TextView tv_progressNum;
	private ProgressBar pb_memoryNum;
	private TextView tv_memorySpace;
	private MyAdapter myAdapter;
	private StickyListHeadersListView mHeadersListView;
	private LinearLayout ll_progress;
	private SlidingDrawer sd_task;
	private ImageView iv_drawer1;
	private ImageView iv_drawer2;
	
	private long availMem;
	private long totalMem;
	
	private AlphaAnimation aa1;
	private AlphaAnimation aa2;
	private SettingCustomView scv_showSystemProcess;
	private SettingCustomView scv_lockScreen;



	private int processTotalNum;
	private int processRunningNum;

	private ActivityManager manager;
	private List<AppInforBean> allRunningInfos = new ArrayList<AppInforBean>();
	// 用户软件的集合
	private List<AppInforBean> userAppInforBeans = new Vector<AppInforBean>();
	// 系统软件的集合。
	private List<AppInforBean> sysAppInforBeans = new Vector<AppInforBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();

		initEvent();
		
//		initData();
		
		initAnimation();
		
		//初始化界面开始动画
		showDown();

	}


	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}

	
	private void initAnimation() {
		aa1 = new AlphaAnimation(0.5f, 1.0f);
		aa2 = new AlphaAnimation(1.0f, 0.5f);
		aa1.setDuration(500);
		aa2.setDuration(500);
		
		aa1.setRepeatCount(Animation.INFINITE);
		aa2.setRepeatCount(Animation.INFINITE);
	}

	/**
	 * 全部选择
	 * 
	 * @param view
	 */
	public void selectAll(View view) {
		// 用户软件
		for (AppInforBean inforBean : allRunningInfos) {
			if (inforBean.getPackageName().equals(getPackageName())) {
				continue;
			}
			inforBean.setChecked(true);
		}

		// 刷新ui
		myAdapter.notifyDataSetChanged();
		
	}
		
	/**
	 * 抽屉向上拉伸状态
	 */
	public void showUp(){
		
		iv_drawer1.clearAnimation();
		iv_drawer2.clearAnimation();
		
		iv_drawer1.setImageResource(R.drawable.drawer_arrow_down);
		iv_drawer2.setImageResource(R.drawable.drawer_arrow_down);
	}
	

	/**
	 * 抽屉菜单初始化动画
	 */
	public void showDown(){

		iv_drawer1.setImageResource(R.drawable.drawer_arrow_up);
		iv_drawer2.setImageResource(R.drawable.drawer_arrow_up);
		
		iv_drawer1.startAnimation(aa1);
		iv_drawer2.startAnimation(aa2);
	}

	/**
	 * 反选
	 * 
	 * @param view
	 */
	public void reverseSelect(View view) {
		// 用户软件
		for (AppInforBean inforBean : allRunningInfos) {
			if (inforBean.getPackageName().equals(getPackageName())) {
				continue;
			}
			inforBean.setChecked(!inforBean.isChecked());
		}

		
		// 刷新ui界面
		myAdapter.notifyDataSetChanged();
	}

	/**
	 * 清理手机缓存
	 * 
	 * @param view
	 */
	public void cleanTaskCache(View view) {
		int cleanNum = 0;
		long cleanSize=0;
		// 清理用户软件缓存
		for (int i = allRunningInfos.size()-1; i >= 0; i--) {
			
				AppInforBean inforBean = allRunningInfos.get(i);
				if (inforBean.isChecked()) {
					
					if (inforBean.isSystem()) {
						sysAppInforBeans.remove(inforBean);
					}else {
						userAppInforBeans.remove(inforBean);
					}
					cleanNum++;
					
					cleanSize+=inforBean.getAppRunningSize();
					// 清理进程
					manager.killBackgroundProcesses(inforBean.getPackageName());

					
					// 界面效果
					allRunningInfos.remove(i);
				}
				
			
		}
		if ((userAppInforBeans.size()+sysAppInforBeans.size())<=3) {
			
			splashUtils.putLong(getApplicationContext(), myConstantValue.ENTER_TASK_TIME,System.currentTimeMillis());
		}
		//提醒清理刷新界面
		ShowToastUtils.showToast("清理了"+cleanNum+"个进程，释放了"+Formatter.formatFileSize(getApplicationContext(), cleanSize), this);
		
		//初始化界面
		
		//内存显示。
		processTotalNum-=cleanNum;
		availMem += cleanSize;
		
		taskInfoMess();
		
		//刷新界面
		myAdapter.notifyDataSetChanged();


	}
	
	private void initEvent() {
		
		/**
		 * 抽屜菜单的点击事件
		 */
		scv_showSystemProcess.setOnToggleListener(new onToggleChangeListener() {
			
			@Override
			public void toggleChange(View view, boolean isOpen) {
				//记录被选择的状态
				splashUtils.putBoolean(getApplicationContext(), myConstantValue.SHOW_SYSTEM_PROCESS ,isOpen);
				
				//刷新界面
				myAdapter.notifyDataSetChanged();
			}
		});
		scv_lockScreen.setOnToggleListener(new onToggleChangeListener() {
			
			@Override
			public void toggleChange(View view, boolean isOpen) {
				if (isOpen) {
					Intent serviceIntent = new Intent(TaskProcessManager.this, LockScreenCleanTaskService.class);
					startService(serviceIntent);
				}else {
					Intent serviceIntent = new Intent(TaskProcessManager.this, LockScreenCleanTaskService.class);
					stopService(serviceIntent);
				}
				
			}
		});
		
		//给抽屉菜单添加点击事件
		sd_task.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened() {
				showUp();
				
			}
		});
		
		sd_task.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				showDown();
			}
		});
		
		// 给listView添加点击事件。
		mHeadersListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击条目改变checkBox的状态
				AppInforBean appInforBean = allRunningInfos.get(position);
				// 改变复选框初始化状态
				appInforBean.setChecked(!appInforBean.isChecked());

				// 如果是自己，则初始化为false
				if (appInforBean.getPackageName().equals(getPackageName())) {
					appInforBean.setChecked(false);
				}

				// 刷新界面
				myAdapter.notifyDataSetChanged();
			}

		});

	}

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:// 加载数据中。
				ll_progress.setVisibility(View.VISIBLE);
				mHeadersListView.setVisibility(View.GONE);
				break;

			case FINISH:// 加载数据完成。
				ll_progress.setVisibility(View.GONE);
				mHeadersListView.setVisibility(View.VISIBLE);

				processTotalNum = sysAppInforBeans.size()+userAppInforBeans.size();

				processRunningNum = TastInfosUtils
						.getRunningTotalProcessNum(getApplicationContext());
				
				availMem = TastInfosUtils
						.getAvailMem(getApplicationContext());
				totalMem = TastInfosUtils.getTotalMem();
				
				
				
				//初始化显示界面
				taskInfoMess();
				
				//刷新界面
				myAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}

		
	};

	public void taskInfoMess() {

		tv_progressNum.setText("运行的总进程：" + processTotalNum);
		pb_progressNum
				.setProgress((int) (processTotalNum * 100 / processRunningNum));

	
		tv_memorySpace.setText("总内存/可用内存"
				+ " : "
				+ (Formatter.formatFileSize(getApplicationContext(),
						totalMem))
				+ "/"
				+ (Formatter.formatFileSize(getApplicationContext(),
						availMem)));
		
		
		pb_memoryNum.setProgress((int) (availMem * 100 / totalMem));
	};
	
	
	private void initData() {
		
		//初始化锁屏清理
		scv_lockScreen.setToggleOn(AntiThrefServiceUtils.serviceRunning(getApplicationContext(), "com.example.mobilesafe.service.LockScreenCleanTaskService"));
		
		//初始化是否显示系统进程
		scv_showSystemProcess.setToggleOn(splashUtils.getbBoolean(getApplicationContext(), myConstantValue.SHOW_SYSTEM_PROCESS, false));
		
		
		new Thread() {

			public void run() {

				// 发送加载数据的消息
				mHandler.obtainMessage(LOADING).sendToTarget();

				allRunningInfos = TastInfosUtils
						.getAllRunningInfos(getApplicationContext());
				
				userAppInforBeans.clear();
				sysAppInforBeans.clear();
				
				for (AppInforBean appInforBean : allRunningInfos) {
					if (!appInforBean.isSystem()) {
						userAppInforBeans.add(appInforBean);
					} else {
						sysAppInforBeans.add(appInforBean);
					}
				}

				allRunningInfos.clear();
				allRunningInfos.addAll(userAppInforBeans);
				allRunningInfos.addAll(sysAppInforBeans);

				// 发送加载数据完成。
				mHandler.obtainMessage(FINISH).sendToTarget();
				
			};
		}.start();

	}

	private class MyAdapter extends BaseAdapter implements
			StickyListHeadersAdapter {

		@Override
		public int getCount() {
			if (splashUtils.getbBoolean(getApplicationContext(), myConstantValue.SHOW_SYSTEM_PROCESS, true)) {
				
				return allRunningInfos.size();
			}else {
				return userAppInforBeans.size();
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return allRunningInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView != null) {
				// 已经有缓存数据
				viewHolder = (ViewHolder) convertView.getTag();
			} else {

				// 没有缓存。
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_task_app_info_mess, null);

				viewHolder = new ViewHolder();

				// 封装缓存数据。
				viewHolder.cb_taskBox = (CheckBox) convertView
						.findViewById(R.id.cb_task_app_is_selected);
				viewHolder.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_task_app_info_icon);
				viewHolder.tv_taskName = (TextView) convertView
						.findViewById(R.id.tv_task_app_name_mess);
				viewHolder.tv_taskRunningSize = (TextView) convertView
						.findViewById(R.id.tv_task_app_location_mess);

				convertView.setTag(viewHolder);

			}

			final AppInforBean inforBean = allRunningInfos.get(position);

			// 显示数据
			// 显示图标。
			viewHolder.iv_icon.setImageDrawable(inforBean.getIcon());
			// 显示占用运行内大小
			viewHolder.tv_taskRunningSize.setText(Formatter.formatFileSize(
					getApplicationContext(), inforBean.getAppRunningSize())
					+ "");
			// 显示应用名称。
			viewHolder.tv_taskName.setText(inforBean.getAppName());
			viewHolder.tv_taskRunningSize.setText(Formatter.formatFileSize(
					getApplicationContext(), inforBean.getAppRunningSize())
					+ "");
			// 复选框点击事件
			viewHolder.cb_taskBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// 记录复选框的选择状态。
							inforBean.setChecked(isChecked);

						}
					});

			// 初始化复选框状态
			viewHolder.cb_taskBox.setChecked(inforBean.isChecked());

			// 设置隐藏自己
			if (inforBean.getPackageName().equals(getPackageName())) {
				viewHolder.cb_taskBox.setVisibility(View.GONE);
			} else {
				viewHolder.cb_taskBox.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		@Override
		public View getHeaderView(int position, View convertView,
				ViewGroup parent) {
			AppInforBean appInforBean = allRunningInfos.get(position);

			TextView tv_systemAppInfos = new TextView(getApplicationContext());
			tv_systemAppInfos.setTextColor(Color.GREEN);
			tv_systemAppInfos.setBackgroundColor(Color.GRAY);

			if (!appInforBean.isSystem()) {
				tv_systemAppInfos.setText("用户软件(" + userAppInforBeans.size()
						+ ")");
			} else {
				tv_systemAppInfos.setText("系统软件(" + sysAppInforBeans.size()
						+ ")");

			}

			return tv_systemAppInfos;
		}

		@Override
		public long getHeaderId(int position) {

			AppInforBean appInforBean = allRunningInfos.get(position);

			if (!appInforBean.isSystem()) {
				return 1;
			} else {
				return 2;
			}
		}

	}

	private class ViewHolder {
		TextView tv_taskName;
		TextView tv_taskRunningSize;
		CheckBox cb_taskBox;
		ImageView iv_icon;
	}

	private void initView() {
		setContentView(R.layout.activity_process_management);

		// 显示进程界面
		pb_progressNum = (ProgressBar) findViewById(R.id.pb_task_progress_process_num);
		tv_progressNum = (TextView) findViewById(R.id.tv_task_display_process_num);

		// 显示手机运行内存界面。
		pb_memoryNum = (ProgressBar) findViewById(R.id.pb_task_progress_memory_space);
		tv_memorySpace = (TextView) findViewById(R.id.tv_task_display_memory_space);

		// 加载数据的listview
		mHeadersListView = (StickyListHeadersListView) findViewById(R.id.lv_task_app_info_manager);
		myAdapter = new MyAdapter();
		mHeadersListView.setAdapter(myAdapter);

		// 加载的进度条。
		ll_progress = (LinearLayout) findViewById(R.id.ll_progressbar_root);

		manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		
		
		iv_drawer1 = (ImageView) findViewById(R.id.iv_task_drawer_1);
		iv_drawer2 = (ImageView) findViewById(R.id.iv_task_drawer_2);
		
		
		sd_task = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
		
		
		scv_showSystemProcess = (SettingCustomView) findViewById(R.id.scv_task_show_system_process);
		scv_lockScreen = (SettingCustomView) findViewById(R.id.scv_task_lock_screen_process);
	}

	
}

			


