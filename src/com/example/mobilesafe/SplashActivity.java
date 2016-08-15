package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;


/**
 * @author Administrator
 *@company Newbie
 *@date 2016-8-15
 *@des 初始化Splash界面。
 */
public class SplashActivity extends Activity {

	
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

     
}
