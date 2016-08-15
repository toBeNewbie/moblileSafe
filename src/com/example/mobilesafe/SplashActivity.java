package com.example.mobilesafe;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author Administrator
 *@company Newbie
 *@date 2016-8-15
 *@des 初始化Splash界面。
 */
public class SplashActivity extends Activity {

	
	 
	private TextView splash_version_name;
	private RelativeLayout rl_splash_root;
	private String versionName;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        initView();
        
        initDate();
        
        initEvent();
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
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionName = packageInfo.versionName;
			splash_version_name.setText(versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

     
}
