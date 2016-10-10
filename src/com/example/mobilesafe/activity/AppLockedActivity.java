package com.example.mobilesafe.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.dao.LockedDataDao;
import com.example.mobilesafe.db.LockedDataDB;
import com.example.mobilesafe.difineView.BaseAppLockedFragment;
import com.example.mobilesafe.difineView.CustomAppLockedView;
import com.example.mobilesafe.difineView.CustomAppLockedView.onLockChangedListener;
import com.example.mobilesafe.difineView.LockedAppFragment;
import com.example.mobilesafe.difineView.UnlockedAppFragment;
import com.example.mobilesafe.utils.GetAppInfoUtils;

/**
 * 
 * @author Administrator
 * @company Newbie
 * @date 2016-10-6
 * @des 是否开启程序锁的界面
 */
public class AppLockedActivity extends FragmentActivity {

	private CustomAppLockedView customAppLockedView;
	private FrameLayout fl_lockedMess;
	private BaseAppLockedFragment lockedFragment;
	private BaseAppLockedFragment unLockedFragment;
	private LockedDataDao dataDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		initView();

		initFragment();

		initEvent();

		initData();
	}

	private void initFragment() {

		List<AppInforBean> installAppInfos = GetAppInfoUtils.getInstallAppInfos(this);
		
		
		installAppInfos.remove(new AppInforBean(getPackageName()));
		
		//对集合进行排序
		Collections.sort(installAppInfos, new Comparator<AppInforBean>() {

			@Override
			public int compare(AppInforBean lhs, AppInforBean rhs) {
				
				if (rhs.isSystem()) {
					return -1;
				}else {
					return 3;
				}
			}

		
		});
		
		dataDao = new LockedDataDao(this);
		final List<String> allLockedApp = dataDao.getAllLockedApp();
		
		
		
		
		lockedFragment = new LockedAppFragment();
		unLockedFragment = new UnlockedAppFragment();
		
		lockedFragment.setLockedDao(dataDao);
		unLockedFragment.setLockedDao(dataDao);
		
		lockedFragment.setInstallAppInfos(installAppInfos);
		unLockedFragment.setInstallAppInfos(installAppInfos);

		lockedFragment.setAllInstallAppNames(allLockedApp);
		unLockedFragment.setAllInstallAppNames(allLockedApp);
		
		
		//注册内容观察者,监听数据库的改变
		ContentObserver observer=new ContentObserver(new Handler()) {
			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				//清理原来集合中的数据
				allLockedApp.clear();
				//添加数据库里的数据
				allLockedApp.addAll(dataDao.getAllLockedApp());
			}
		};
		getContentResolver().registerContentObserver(LockedDataDB.URI, true, observer);
	}

	private void initData() {

		selectFragmentAppLocked(true);
	}

	/**
	 * 选择要显示的Fragment
	 * 
	 * @param isLocked
	 *            isLocked:true 显示加锁app界面 ; false 显示未加锁app界面
	 */
	public void selectFragmentAppLocked(boolean isLocked) {
		// 获得fragment管理器
		FragmentManager fragmentManager = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		if (!isLocked) {

			// 程序已加锁
			fragmentTransaction.replace(R.id.fl_app_locked_mess,
					lockedFragment, "locked");

		} else {

			// 程序未加锁
			fragmentTransaction.replace(R.id.fl_app_locked_mess,
					unLockedFragment, "unlocked");
		}
		// 提交事务
		fragmentTransaction.commit();
	}

	private void initEvent() {

		customAppLockedView
				.setOnLockChangedListener(new onLockChangedListener() {

					@Override
					public void onLockChanged(boolean isLocked) {

						selectFragmentAppLocked(isLocked);

					}
				});

	}

	private void initView() {

		setContentView(R.layout.activty_app_locked_tools);

		customAppLockedView = (CustomAppLockedView) findViewById(R.id.custom_app_locked_view);

		fl_lockedMess = (FrameLayout) findViewById(R.id.fl_app_locked_mess);

	}

}
