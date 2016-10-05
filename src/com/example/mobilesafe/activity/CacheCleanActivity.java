package com.example.mobilesafe.activity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.utils.GetAppInfoUtils;
import com.example.mobilesafe.utils.ShowToastUtils;

/**
 * 
 * @author Administrator
 * @company Newbie
 * @date 2016-10-4
 * @des 显示缓存信息
 */
public class CacheCleanActivity extends Activity {

	protected static final int BEGINGING = 1;
	protected static final int SCANNING = 2;
	protected static final int FINISH = 3;
	private ImageView iv_scanning;
	private TextView tv_scanning_content;
	private ProgressBar pb_process;
	private LinearLayout ll_result;

	// 保存扫描信息的容器
	private List<ScanningCacheInfosBean> ScanningInfos = new Vector<CacheCleanActivity.ScanningCacheInfosBean>();

	// 保存有缓存信息的容器
	private List<ResultAppCacheInfo> resultInfos = new Vector<ResultAppCacheInfo>();

	private RotateAnimation mRa;
	private TextView tv_noCache;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BEGINGING:
				// 开始播放动画
				iv_scanning.startAnimation(mRa);
				ll_result.setVisibility(View.VISIBLE);
				tv_noCache.setVisibility(View.GONE);
				break;
			case SCANNING:

			{
				// 加载扫描信息和进度信息
				// 获取封装类扫描信息
				ScanningCacheInfosBean antiVirusApp = (ScanningCacheInfosBean) msg.obj;

				// 设置扫描进度
				pb_process.setProgress((int) Math
						.round(antiVirusApp.currentProcess * 100.0
								/ antiVirusApp.maxProcess));

				// 封装显示加载进程的条目
				View mView = View.inflate(getApplicationContext(),
						R.layout.item_anti_virus_info_process, null);
				ImageView iv_itemIcon = (ImageView) mView
						.findViewById(R.id.iv_item_virus_icon);
				ImageView iv_ifVirus = (ImageView) mView
						.findViewById(R.id.iv_item_virus_if_virus);
				iv_ifVirus.setVisibility(View.GONE);
				TextView tv_itemName = (TextView) mView
						.findViewById(R.id.tv_item_virus_app_name);

				// 显示内容
				iv_itemIcon.setImageDrawable(antiVirusApp.icon);
				tv_itemName.setText(antiVirusApp.appName);

				// 添加到显示列表中 0表示最新的扫描数据放在最上面
				ll_result.addView(mView, 0);
				tv_scanning_content.setText("正在扫描:" + antiVirusApp.appName);

				break;
			}
			case FINISH:

				// 移除扫描添加的View组件
				ll_result.removeAllViews();
				tv_scanning_content.setText("扫描完成");
				// 停止播放动画
				iv_scanning.clearAnimation();
				if (resultInfos.size() > 0) {
					// 有缓存信息
					ShowToastUtils.showToast("请点击右上角清理缓存按钮",
							CacheCleanActivity.this);
					// 读取缓存信息
					for (final ResultAppCacheInfo appCacheInfo : resultInfos) {

						View view = View.inflate(getApplicationContext(),
								R.layout.item_clean_cache_info_ll, null);
						ImageView iv_icon = (ImageView) view
								.findViewById(R.id.iv_clean_cache_info_icon);
						TextView tv_appName = (TextView) view
								.findViewById(R.id.tv_clean_cache_app_name);
						TextView tv_cacheSize = (TextView) view
								.findViewById(R.id.tv_clean_cache_size);

						iv_icon.setImageDrawable(appCacheInfo.icon);
						tv_appName.setText(appCacheInfo.appName);
						tv_cacheSize.setText(Formatter
								.formatFileSize(getApplicationContext(),
										appCacheInfo.cacheSize));

						view.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent settingIntent = new Intent(
										"android.settings.APPLICATION_DETAILS_SETTINGS");
								settingIntent.setData(Uri.parse("package:"
										+ appCacheInfo.packageName));
								startActivity(settingIntent);
							}
						});

						ll_result.addView(view, 0);

					}

				} else {
					// 没有缓存
					tv_noCache.setVisibility(View.VISIBLE);
					ll_result.setVisibility(View.GONE);
				}

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 初始化缓存清理界面
		initView();

		// 初始化动画
		initAnimation();

		// 开始扫描缓存
		BeginScanningCache();

	}

	private void initAnimation() {
		mRa = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRa.setDuration(2000);
		mRa.setRepeatCount(-1);
		// 设置动画插入器
		mRa.setInterpolator(new Interpolator() {

			@Override
			public float getInterpolation(float input) {

				return input * 2;

			}
		});
	}

	private void BeginScanningCache() {
		new Thread() {
			public void run() {
				mHandler.obtainMessage(BEGINGING).sendToTarget();

				List<AppInforBean> installAppInfos = GetAppInfoUtils
						.getInstallAppInfos(getApplicationContext());
				int process = 0;
				for (AppInforBean appInforBean : installAppInfos) {
					process++;
					// 获取缓存大小
					getCacheSize(appInforBean);

					ScanningCacheInfosBean scanningBean = new ScanningCacheInfosBean();
					scanningBean.currentProcess = process;
					scanningBean.maxProcess = installAppInfos.size();
					scanningBean.appName = appInforBean.getAppName();
					scanningBean.icon = appInforBean.getIcon();

					Message obtainMessage = mHandler.obtainMessage(SCANNING);
					obtainMessage.obj = scanningBean;
					mHandler.sendMessage(obtainMessage);

					SystemClock.sleep(200);
				}

				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private void initView() {
		setContentView(R.layout.activity_clean_cache);
		// 扫描动画

		iv_scanning = (ImageView) findViewById(R.id.iv_clean_cache_scanning);

		// 扫描内容App名字显示

		tv_scanning_content = (TextView) findViewById(R.id.tv_clean_cache_scanning_content);
		// 扫描进度条

		pb_process = (ProgressBar) findViewById(R.id.pb_clean_cache_process);
		// 扫描的结果显示

		ll_result = (LinearLayout) findViewById(R.id.ll_clean_cache_items);

		tv_noCache = (TextView) findViewById(R.id.tv_no_cache_mess);
	}

	// 清理缓存信息
	public void cleanAppCache(View view) {
		try {

			// 获取缓存信息的回调结果
			class mIPackageDataObserver extends IPackageDataObserver.Stub {

				@Override
				public void onRemoveCompleted(String packageName,
						boolean succeeded) throws RemoteException {
					runOnUiThread(new Runnable() {
						public void run() {
							tv_noCache.setVisibility(View.VISIBLE);
							ll_result.removeAllViews();
							ll_result.setVisibility(View.GONE);
						}
					});
				}

			}
			;

			PackageManager mPm = getPackageManager();

			// 利用反射调用方法
			// 获取类
			Class<?> loadClass = getClassLoader().loadClass(
					"android.content.pm.PackageManager");
			// 获取获得缓存信息的方法
			Method declaredMethod = loadClass.getDeclaredMethod(
					"freeStorageAndNotify", long.class,
					IPackageDataObserver.class);
			// 调用
			declaredMethod.invoke(mPm, Long.MAX_VALUE,
					new mIPackageDataObserver());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author Administrator
	 * @company Newbie
	 * @date 2016-10-4
	 * @des 封装扫描app的信息
	 */
	private class ScanningCacheInfosBean {
		String appName;
		int maxProcess;
		int currentProcess;
		Drawable icon;
	}

	/**
	 * 
	 * @author Administrator
	 * @company Newbie
	 * @date 2016-10-4
	 * @des 封装扫描存在缓存信息app
	 */
	private class ResultAppCacheInfo {
		Drawable icon;
		String appName;
		long cacheSize;
		String packageName;
	}

	private void getCacheSize(AppInforBean appInforBean) {

		try {

			// 获取缓存信息的回调结果
			class mIPackageStatsObserver extends IPackageStatsObserver.Stub {
				private AppInforBean appInforBean;

				public mIPackageStatsObserver(AppInforBean appInforBean) {
					this.appInforBean = appInforBean;
				}

				@Override
				public void onGetStatsCompleted(PackageStats pStats,
						boolean succeeded) throws RemoteException {
					// 回调结果

					if (pStats.cacheSize > 13000) {

						ResultAppCacheInfo resultAppCacheInfo = new ResultAppCacheInfo();
						resultAppCacheInfo.cacheSize = pStats.cacheSize;
						resultAppCacheInfo.icon = appInforBean.getIcon();
						resultAppCacheInfo.appName = appInforBean.getAppName();
						resultAppCacheInfo.packageName = appInforBean
								.getPackageName();

						resultInfos.add(resultAppCacheInfo);
					}

				}
			}
			;

			PackageManager mPm = getPackageManager();

			// 利用反射调用方法
			// 获取类
			Class<?> loadClass = getClassLoader().loadClass(
					"android.content.pm.PackageManager");
			// 获取获得缓存信息的方法
			Method declaredMethod = loadClass.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			// 调用
			declaredMethod.invoke(mPm, appInforBean.getPackageName(),
					new mIPackageStatsObserver(appInforBean));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
