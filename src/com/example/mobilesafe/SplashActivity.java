package com.example.mobilesafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mobilesafe.activity.HomeActivity;
import com.example.mobilesafe.bean.versionInfo;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
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

/**
 * @author Administrator
 * @company Newbie
 * @date 2016-8-15
 * @des 初始化Splash界面。
 */
public class SplashActivity extends Activity {

	private static final int LOAD_HOME_ACTIVITY = 1;
	private TextView splash_version_name;
	private RelativeLayout rl_splash_root;
	private String versionName;
	private AnimationSet mAnimationSet;
	private versionInfo version_info;
	private int version_code;

	Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_HOME_ACTIVITY:
				startHomeActivity();
				break;

			default:
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

		// 测试代码....
		splashUtils.putBoolean(getApplicationContext(),
				myConstantValue.AUTO_VERSION_UPDATE, true);

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

		Message message=null;
		long startTime = System.currentTimeMillis();
		try {
			URL url = new URL(getResources().getString(R.string.download_url));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() == 200) {

				String jsonContent = readData(conn.getInputStream());
				parseJsonData(jsonContent);

				if (version_code == version_info.getVersion_code()) {
					//获得消息，动画播放。
					message = handler.obtainMessage(LOAD_HOME_ACTIVITY);
				} else {

				}

			}

		} catch (MalformedURLException e) {
			// URL格式错误。
			e.printStackTrace();
		} catch (NotFoundException e) {
			// 找不到地址错误。
			e.printStackTrace();
		} catch (IOException e) {
			// io异常错误。
			e.printStackTrace();
		} catch (JSONException e) {
			// 解析json数据格式出错
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
		version_info.setDesc("desc");
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

}
