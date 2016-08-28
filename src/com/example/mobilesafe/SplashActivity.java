package com.example.mobilesafe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.activity.HomeActivity;
import com.example.mobilesafe.bean.versionInfo;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @author Administrator
 * @company Newbie
 * @date 2016-8-15
 * @des 初始化Splash界面。
 */
public class SplashActivity extends Activity {

	private static final int LOAD_HOME_ACTIVITY = 1;
	private static final int UPDATE_APK = 2;
	private TextView splash_version_name;
	private RelativeLayout rl_splash_root;
	private String versionName;
	private AnimationSet mAnimationSet;
	private versionInfo version_info;
	private int version_code;
	private int responseCode;

	/**
	 * 消息处理，异常，错误处理。
	 */
	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_HOME_ACTIVITY:
				startHomeActivity();
				break;
			case UPDATE_APK:
				showUpdateDialog();
				break;
			default:
				switch (msg.what) {
				case 404:
					Toast.makeText(getApplicationContext(), "网络连接错误！！", 0).show();
					break;
				case 500:
					Toast.makeText(getApplicationContext(), "服务器内部错误.....", 0).show();
					break;
				case 1010:
					Toast.makeText(getApplicationContext(), "url 格式错误。。。。", 0).show();
					break;
				case 1011:
					Toast.makeText(getApplicationContext(), "资源找不到。。。。", 0).show();
					break;
				case 1012:
					Toast.makeText(getApplicationContext(), "网络不可用，请连接网络", 0).show();
					break;
				case 1013:
					Toast.makeText(getApplicationContext(), "json 格式解析错误.......", 0).show();
					break;
				 default:
					break;
				}
				startHomeActivity();
				break;
			}
			

		};
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// 初始化界面。
		initView();
		// 显示数据到界面。
		initDate();

		// 设置动画
		initAnimation();

		initEvent();

	}

	private void initAnimation() {
		RotateAnimation ra = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(2000);
		ra.setFillAfter(true);

		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(2000);
		sa.setFillAfter(true);

		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		aa.setFillAfter(true);

		mAnimationSet = new AnimationSet(false);
		mAnimationSet.addAnimation(sa);
		mAnimationSet.addAnimation(ra);
		mAnimationSet.addAnimation(aa);
		rl_splash_root.startAnimation(mAnimationSet);
	}

	/**
	 * 初始化splash界面
	 */
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_splash);
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		splash_version_name = (TextView) findViewById(R.id.tv_splash_version_name);
	}

	/**
	 * 填充splash界面初始化数据
	 */
	private void initDate() {
		// TODO Auto-generated method stub
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			version_code = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			splash_version_name.setText(versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initEvent() {



		mAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (splashUtils.getbBoolean(getApplicationContext(),
						myConstantValue.AUTO_VERSION_UPDATE, false)) {
					// 版本更新，连接网络，获取数据。
					checkUpdate();
				} else {

				}

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (splashUtils.getbBoolean(getApplicationContext(),
						myConstantValue.AUTO_VERSION_UPDATE, false)) {
					// 版本更新。

				} else {
					// 跳转到主界面
					startHomeActivity();
				}
			}
		});
	}

	// 检测版本
	protected void checkUpdate() {
		// 检测版本，
		// 安装新版本。
		// 跳转界面。
		new Thread() {
			@Override
			public void run() {
				 readDataFromUrl();
			}
		}.start();
	}

	
	/**
	 * 连接网络，从网络里获得数据。
	 */
	protected void readDataFromUrl() {
		// TODO Auto-generated method stub

		Message message=handler.obtainMessage();
		long startTime = System.currentTimeMillis();
		try {
			URL url = new URL(getResources().getString(R.string.download_url));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");

			responseCode = conn.getResponseCode();
			if (responseCode == 200) {

				String jsonContent = readData(conn.getInputStream());
				parseJsonData(jsonContent);

				if (version_code == version_info.getVersion_code()) {
					//获得消息，动画播放。
					message.what=LOAD_HOME_ACTIVITY;
				} else {
					//有新版本。
					message.what=UPDATE_APK;
				}

			}else {
				message.what=responseCode;
			}

		} catch (MalformedURLException e) {
			// URL格式错误。
			message.what=1010;
			e.printStackTrace();
		} catch (NotFoundException e) {
			// 找不到地址错误。
			message.what=1011;
			e.printStackTrace();
		} catch (IOException e) {
			// io异常错误。
			message.what=1012;
			e.printStackTrace();
		} catch (JSONException e) {
			// 解析json数据格式出错
			message.what=1013;
			e.printStackTrace();
		} finally{
			long endTime = System.currentTimeMillis();
			if (endTime-startTime < 2000) {
				SystemClock.sleep(2000-(endTime-startTime));
			}
			
			//发送消息，确保动画播完，才进入主界面。
			handler.sendMessage(message);
		}

	}

	/**
	 * 发现新版本，并弹出是否更新对话框，
	 */
	private void showUpdateDialog() {

		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		
		builder.setTitle("警告");
		builder.setMessage("新版功能：\n"+version_info.getDesc());
		builder.setPositiveButton("下载", new OnClickListener(){
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载apk.
				downloadApk();
			}
		}).setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startHomeActivity();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				
				startHomeActivity();
			}
		});
		builder.show();
		
	}

	
	
	/**
	 * 发现新版本，从服务器上下载相关Apk.
	 */
	protected void downloadApk() {
		// TODO Auto-generated method stub
		String path = Environment.getExternalStorageDirectory()+"/news.apk";
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.download(version_info.getDownloadUrl(), path, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// TODO Auto-generated method stub

				 installApk();
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "下载失败"+msg, 0).show();
				startHomeActivity();
			}
		});
	}

	/**
	 * 安装下载好的Apk。
	 */
	protected void installApk() {
	 
		String fileName = Environment.getExternalStorageDirectory()+"/news.apk";
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
		startActivityForResult(intent, 1);
	}

	/**
	 * 解析json格式的数据。
	 * 
	 * @param jsonContent
	 * @throws JSONException
	 */
	protected void parseJsonData(String jsonContent) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(jsonContent);
		version_info = new versionInfo();

		version_info.setVersion_name(jsonObject.getString("version_name"));
		version_info.setVersion_code(jsonObject.getInt("version_code"));
		version_info.setDesc(jsonObject.getString("desc"));
		version_info.setDownloadUrl(jsonObject.getString("download_url"));

	}

	/**
	 * 从流中读取数据。
	 * 
	 * @param inputStream
	 * @return 流中的数据。
	 * @throws IOException
	 */
	private String readData(InputStream inputStream) throws IOException {
		// TODO Auto-generated method stub
		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}

	/**
	 * 跳转到主界面。
	 */
	public void startHomeActivity() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	
	/**
	 * 监控安装过程中取消操作。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		startHomeActivity();
		super.onActivityResult(requestCode, resultCode, data);
	}
 
}
