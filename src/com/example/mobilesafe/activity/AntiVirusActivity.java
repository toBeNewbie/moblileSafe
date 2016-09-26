package com.example.mobilesafe.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.dao.AntiVirusFileDao;
import com.example.mobilesafe.utils.GetAppInfoUtils;
import com.example.mobilesafe.utils.GetFileMD5;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 
 * @author Administrator
 * @company Newbie
 * @date 2016-9-19
 * @des 显示手机病毒查杀
 */
public class AntiVirusActivity extends Activity {
	protected static final int START_SCANNING = 1;
	protected static final int SCANNING = 2;
	protected static final int FINISH_SCANNING = 3;
	private LinearLayout ll_scanningResult;
	private Button bt_rescanning;
	private TextView tv_scanningResult;
	private LinearLayout ll_scanningProcess;
	private CircleProgress cp_scaningProcess;
	private TextView tv_scanningContent;
	private LinearLayout ll_virusAppContent;
	private boolean iF_VIRUS;
	private AnimatorSet mShowScanningAgain;

	private LinearLayout ll_animation;
	private ImageView iv_animation_left;
	private ImageView iv_animation_right;
	
	private boolean isInitEventAndAnimator=false;

	private boolean isInterrectScanning=false;
	private boolean isShowPoint=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		initView();


		//检查版本
		checkVersion();
	}

	/**
	 * 获取病毒库版本，判断是否更新
	 */
	private void checkVersion() {
		
		
		//显示联网进度
		AlertDialog.Builder mAlertdialog=new AlertDialog.Builder(this);
		mAlertdialog.setTitle("注意");
		mAlertdialog.setMessage("正在尝试联网......");
		final AlertDialog alertDialog = mAlertdialog.create();
		//显示对话框
		alertDialog.show();
		
		new Thread(){
			public void run() {
				isShowPoint=true;
				class DataPoint{
					int number = 1;
				}
				
				final DataPoint dataPoint=new DataPoint();
				dataPoint.number = 1;
				while (isShowPoint) {
					runOnUiThread(new Runnable() {
						public void run() {
							alertDialog.setMessage("正在尝试联网"+getPointNum(dataPoint.number++ % 7));
						}
					});
				}
			};
		}.start();
		
		HttpUtils mHttpUtils = new HttpUtils();
		mHttpUtils.configTimeout(5000);
		mHttpUtils.send(HttpMethod.GET, getResources().getString(R.string.virus_update_url_version), new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				isShowPoint=false;
				//隐藏对话框
				alertDialog.dismiss();
				// 主线程中执行
				Toast.makeText(getApplicationContext(), "没有网络", 0).show();
				//没有网络，开始扫描病毒库
				startScanningVirus();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				//关闭对话框
				alertDialog.dismiss();
				// 主线程中执行
				//获取网络病毒版本号
				final int serviceVersion=Integer.parseInt(arg0.result);
				
				//获取当前的病毒版本号
				int currentVersion=AntiVirusFileDao.getCurrentVersion();
				if (serviceVersion != currentVersion) {
					//有新版本病毒库，需要更新
					AlertDialog.Builder dialogbBuilder = new AlertDialog.Builder(AntiVirusActivity.this);
					dialogbBuilder.setTitle("有新病毒");
					dialogbBuilder.setMessage("是否下载更新病毒？");
					dialogbBuilder.setPositiveButton("下载更新", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							HttpUtils httpUtils=new HttpUtils();
							httpUtils.configTimeout(5000);
							httpUtils.send(HttpMethod.GET, getResources().getString(R.string.virus_update_url_content), new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// 更新病毒失败
									Toast.makeText(getApplicationContext(), "更新病毒库失败", 0).show();
									//扫描病毒
									startScanningVirus();
									
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// 下载更新病毒库
									String jsonData = arg0.result;
									try {
										JSONObject jsonObject=new JSONObject(jsonData);
										String MD5=jsonObject.getString("md5");
										String desc =jsonObject.getString("desc");
										
										//更新数据库
										AntiVirusFileDao.updateDBVirus(MD5, desc);
										//更新版本号
										AntiVirusFileDao.updateDBVersion(serviceVersion);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									//开始扫描病毒
									startScanningVirus();
									Toast.makeText(getApplicationContext(), "更新病毒库成功", 1).show();
								}
							});
							
						}
					});
					
					dialogbBuilder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//扫描病毒库
							startScanningVirus();
						}
					});
					
					//显示病毒库更新对话框
					dialogbBuilder.show();
					
				}else {
					//没有新版本病毒库，不需要更新
					Toast.makeText(getApplicationContext(), "当前病毒库已经是最新版本", 0).show();
					
					//开始扫描病毒
					startScanningVirus();
				}
			}
		});
		
	}

	private void initEvent() {
		mShowResultAnimation.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				//动画开始设置按钮不可用
				bt_rescanning.setEnabled(false);
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				// 动画播放结束按钮可用
				bt_rescanning.setEnabled(true);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		// 给再次扫描动画集播放添加事件
		mShowScanningAgain.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// 动画结束重新开始扫描
				startScanningVirus();

			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});

		// 给重新扫描按钮添加点击事件逻辑
		bt_rescanning.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 重新扫描手机文件
				ll_virusAppContent.removeAllViews();
				showScanningAgainAnimation();
			}
		});
	}

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_SCANNING:// 开始扫描
				ll_scanningResult.setVisibility(View.GONE);
				ll_scanningProcess.setVisibility(View.VISIBLE);
				ll_animation.setVisibility(View.GONE);
				break;
			case SCANNING:// 正在扫描

				// 加载扫描信息和进度信息
				// 获取封装类扫描信息
				AntiVirusApp antiVirusApp = (AntiVirusApp) msg.obj;

				// 设置扫描进度
				cp_scaningProcess.setProgress((int) Math
						.round(antiVirusApp.currentProcess * 100.0
								/ antiVirusApp.maxProcess));

				// 封装显示加载进程的条目
				View mView = View.inflate(getApplicationContext(),
						R.layout.item_anti_virus_info_process, null);
				ImageView iv_itemIcon = (ImageView) mView
						.findViewById(R.id.iv_item_virus_icon);
				ImageView iv_ifVirus = (ImageView) mView
						.findViewById(R.id.iv_item_virus_if_virus);
				TextView tv_itemName = (TextView) mView
						.findViewById(R.id.tv_item_virus_app_name);

				// 显示内容
				iv_itemIcon.setImageDrawable(antiVirusApp.icon);
				tv_itemName.setText(antiVirusApp.appName);
				if (antiVirusApp.isVirus) {
					// 标志病毒文件
					iF_VIRUS = antiVirusApp.isVirus;
					// 是病毒文件
					iv_ifVirus
							.setBackgroundResource(R.drawable.check_status_red);
				} else {
					// 不是病毒文件
					iv_ifVirus
							.setBackgroundResource(R.drawable.check_status_green);
				}

				// 添加到显示列表中 0表示最新的扫描数据放在最上面
				ll_virusAppContent.addView(mView, 0);
				tv_scanningContent.setText("魔法革正在扫描"+antiVirusApp.appName);
				break;
			case FINISH_SCANNING:// 扫描完成

				// 拍照
				ll_scanningProcess.setDrawingCacheEnabled(true);
				ll_scanningProcess
						.setDrawingCacheQuality(LinearLayout.DRAWING_CACHE_QUALITY_HIGH);
				Bitmap bt_process = ll_scanningProcess.getDrawingCache();
				// 获取左边扫描结束图片
				Bitmap leftImage = getLeftImage(bt_process);
				Bitmap rightImage = getRightImage(bt_process);
				// 设置图片
				iv_animation_left.setImageBitmap(leftImage);
				iv_animation_right.setImageBitmap(rightImage);
				if(!isInitEventAndAnimator){
					
					initShowResultAnimation();
					initShowScanningAgainAnimation();
					initEvent();
					
					isInitEventAndAnimator=true;
				}
				// 显示动画背景
				ll_animation.setVisibility(View.VISIBLE);

				if (iF_VIRUS) {
					// 是病毒
					tv_scanningResult.setText("存在病毒文件");
				} else {
					// 不是病毒
					tv_scanningResult.setText("手机很安全");
				}

				// 显示结果布局
				ll_scanningResult.setVisibility(View.VISIBLE);
				// 显示动画布局
				ll_animation.setVisibility(View.VISIBLE);
				// 隐藏进度布局
				ll_scanningProcess.setVisibility(View.GONE);

				// 扫描结果动画
				showResultAnimation();
				
				break;
			default:
				break;
			}
		};
	};
	private AnimatorSet mShowResultAnimation;

	// 开始扫描文件内容
	private void startScanningVirus() {

		new Thread() {
			public void run() {
				// 开始扫描
				mHandler.obtainMessage(START_SCANNING).sendToTarget();
				// 开始扫描所有安装的APK
				int process = 0;
				List<AppInforBean> installAppInfos = GetAppInfoUtils
						.getInstallAppInfos(getApplicationContext());
				for (AppInforBean appInforBean : installAppInfos) {
					
					//结束扫描进程
					if(isInterrectScanning){
						return;
					}
					
					process++;
					// 获取安装路径
					String sourceDir = appInforBean.getSourceDir();
					// 获取文件的MD5值
					String fileMD5 = GetFileMD5.fileMD5(sourceDir);
					// 判断MD5值是否是病毒
					boolean ifVirus = AntiVirusFileDao.isVirus(fileMD5);
					// 封装扫描信息

					// 创建检测扫描病毒封装类信息
					AntiVirusApp virusApp = new AntiVirusApp();
					virusApp.appName = appInforBean.getAppName();

					virusApp.icon = appInforBean.getIcon();

					virusApp.isVirus = ifVirus;
					virusApp.maxProcess = installAppInfos.size();
					virusApp.currentProcess = process;

					SystemClock.sleep(200);
					// 发送扫描进度信息消息
					Message obtainMessage = mHandler.obtainMessage(SCANNING);
					obtainMessage.obj = virusApp;
					mHandler.sendMessage(obtainMessage);
				}

				mHandler.obtainMessage(FINISH_SCANNING).sendToTarget();
			};
		}.start();
	}

	/**
	 * 初始化再次扫描事件的动画基本参数
	 */
	private void initShowScanningAgainAnimation() {

		mShowScanningAgain = new AnimatorSet();
		// 添加属性动画
		// 左边图片平移动画
		ll_animation.measure(0, 0);
		int width = iv_animation_left.getMeasuredWidth();
		ObjectAnimator leftTransAnimator = ObjectAnimator.ofFloat(
				iv_animation_left, "translationX", -width, 0);

		// 左边图片渐变动画
		ObjectAnimator leftAlphaAnimator = ObjectAnimator.ofFloat(
				iv_animation_left, "alpha", 0.0f, 1.0f);

		// 右边图片平移动画
		ObjectAnimator rightTransAnimator = ObjectAnimator.ofFloat(
				iv_animation_right, "translationX", width, 0);
		// 右边图片渐变动画
		ObjectAnimator rightAlphaAnimator = ObjectAnimator.ofFloat(
				iv_animation_right, "alpha", 0.0f, 1.0f);
		// 扫描结果渐变动画
		ObjectAnimator resultAlphaAnimator = ObjectAnimator.ofFloat(
				ll_scanningResult, "alpha", 1.0f, 0.0f);
		mShowScanningAgain.playTogether(resultAlphaAnimator, leftAlphaAnimator,
				leftTransAnimator, rightAlphaAnimator, rightTransAnimator);
		mShowScanningAgain.setDuration(2000);
	}

	/**
	 * 扫描结束后的动画初始化
	 */
	private void initShowResultAnimation(){

	mShowResultAnimation = new AnimatorSet();
	// 添加属性动画
	// 左边图片平移动画
	ll_animation.measure(0, 0);
	int width = iv_animation_left.getMeasuredWidth();
	ObjectAnimator leftTransAnimator = ObjectAnimator.ofFloat(
			iv_animation_left, "translationX", 0, -width);

	// 左边图片渐变动画
	ObjectAnimator leftAlphaAnimator = ObjectAnimator.ofFloat(
			iv_animation_left, "alpha", 1.0f, 0.0f);

	// 右边图片平移动画
	ObjectAnimator rightTransAnimator = ObjectAnimator.ofFloat(
			iv_animation_right, "translationX", 0, width);
	// 右边图片渐变动画
	ObjectAnimator rightAlphaAnimator = ObjectAnimator.ofFloat(
			iv_animation_right, "alpha", 1.0f, 0.0f);
	// 扫描结果渐变动画
	ObjectAnimator resultAlphaAnimator = ObjectAnimator.ofFloat(
			ll_scanningResult, "alpha", 0.0f, 1.0f);
	mShowResultAnimation.playTogether(resultAlphaAnimator, leftAlphaAnimator,
			leftTransAnimator, rightAlphaAnimator, rightTransAnimator);
	mShowResultAnimation.setDuration(2000);
}
	
	
	
	/**
	 * 再次病毒扫描动画
	 */
	protected void showScanningAgainAnimation() {

		mShowScanningAgain.start();

	}

	/**
	 * 病毒扫描完成后的动画
	 */
	protected void showResultAnimation() {

		
		mShowResultAnimation.start();
	}

	/**
	 * 动态获取点的个数
	 * @param num
	 * @return
	 */
	public String getPointNum(int num){
		String str="";
		for (int i = 0; i <= num; i++) {
			str+=".";
		}
		return str;
	}
	
	
	/**
	 * 获取左半部分图片
	 * 
	 * @param bt_process
	 *            需要截得图片
	 * @return 返回截取的左半部分图片
	 */
	protected Bitmap getLeftImage(Bitmap bt_process) {
		int width = bt_process.getWidth() / 2;
		int height = bt_process.getHeight();

		// 空的画纸
		Bitmap leftImage = Bitmap.createBitmap(width, height,
				bt_process.getConfig());
		// 获得画板
		Canvas leftCanvas = new Canvas(leftImage);

		Matrix matrix = new Matrix();
		Paint paint = new Paint();

		leftCanvas.drawBitmap(bt_process, matrix, paint);
		return leftImage;
	}

	/**
	 * 获取右半部分图片
	 * 
	 * @param bt_process
	 * 
	 *            需要截取的图片
	 * @return 返回截取的右半部分图片
	 */
	protected Bitmap getRightImage(Bitmap bt_process) {

		int width = bt_process.getWidth() / 2;
		int height = bt_process.getHeight();

		// 空的画纸
		Bitmap rightImage = Bitmap.createBitmap(width, height,
				bt_process.getConfig());
		// 获得画板
		Canvas leftCanvas = new Canvas(rightImage);
		Matrix matrix = new Matrix();

		// 获得原图的右半部分
		matrix.setTranslate(-width, 0);

		Paint paint = new Paint();
		leftCanvas.drawBitmap(bt_process, matrix, paint);
		return rightImage;
	}

	private class AntiVirusApp {
		Drawable icon;
		String appName;
		boolean isVirus;

		int maxProcess;
		int currentProcess;
	}

	private void initView() {
		setContentView(R.layout.activity_anti_virus);

		// 获取扫描成功后的界面

		ll_scanningResult = (LinearLayout) findViewById(R.id.ll_virus_scanning_result);
		bt_rescanning = (Button) findViewById(R.id.bt_virus_rescanning);
		tv_scanningResult = (TextView) findViewById(R.id.tv_virus_scanning_result);

		// 获取扫描过程中的相关组件

		ll_scanningProcess = (LinearLayout) findViewById(R.id.ll_virus_process_scanning);
		cp_scaningProcess = (CircleProgress) findViewById(R.id.cp_virus_process_scanning);
		tv_scanningContent = (TextView) findViewById(R.id.tv_virus_scanning_content);

		// 获取扫描进度的相关内容

		ll_virusAppContent = (LinearLayout) findViewById(R.id.ll_virus_scanning_content);

		// 获取创建动画的布局组件
		ll_animation = (LinearLayout) findViewById(R.id.ll_anti_ivrus_animation);
		iv_animation_left = (ImageView) findViewById(R.id.iv_anti_virus_animation_left);
		iv_animation_right = (ImageView) findViewById(R.id.iv_anti_virus_animation_right);
	}
	
	
	@Override
	protected void onDestroy() {
		isInterrectScanning = true;
		super.onDestroy();
	}
}
