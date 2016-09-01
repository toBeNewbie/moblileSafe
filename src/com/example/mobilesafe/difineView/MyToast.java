package com.example.mobilesafe.difineView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

public class MyToast implements OnTouchListener{
	private WindowManager.LayoutParams mParams;
	private WindowManager mWM;
	private View mView;
	private Context mContext;
	private float rawX;
	private float rawY;

	public MyToast(Context context) {

		mContext=context;
		// 获取窗口管理器。
		mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		// 设置参数。
		mParams = new WindowManager.LayoutParams();
		 mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
         mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
         mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                 | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                 | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
         mParams.format = PixelFormat.TRANSLUCENT;
         mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
         mParams.gravity=Gravity.LEFT | Gravity.TOP;
         
         //初始化Toast的位置。
         mParams.x=splashUtils.getInt(mContext, myConstantValue.TOAST_X, 0);
         mParams.y=splashUtils.getInt(mContext, myConstantValue.TOAST_Y, 0);
         
         
         mParams.setTitle("Toast");
         
	}
	
	
	public void show(String phoneLocation){
		mView = View.inflate(mContext, R.layout.syst_toast, null);
		TextView toastText = (TextView) mView.findViewById(R.id.tv_toast_text);
		toastText.setText(phoneLocation);
		mView.setOnTouchListener(this);
		mWM.addView(mView, mParams);
	}
	
	public void hiden(){
		if (mView != null) {
 
            if (mView.getParent() != null) {
            	
                mWM.removeView(mView);
            }

            mView = null;
        };
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN://按下。
			//获取Toast原始坐标。
			rawX = event.getRawX();
			rawY = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE://移动。
			
			//获取移动后的Toast坐标。
			float moveX = event.getRawX();
			float moveY = event.getRawY();
			
			float dx = moveX - rawX;
			float dy = moveY - rawY;
			
			mParams.x+=dx;
			mParams.y+=dy;
			
			//判断是否越界。
			if (mParams.x<0) {
				mParams.x=0;
			}else if (mParams.x>mWM.getDefaultDisplay().getWidth()-mView.getWidth()) {
				mParams.x=mWM.getDefaultDisplay().getWidth()-mView.getWidth();
			}
			
			if (mParams.y<0) {
				mParams.y=0;
			}else if (mParams.y>mWM.getDefaultDisplay().getHeight()-mView.getWidth()) {
				mParams.y=mWM.getDefaultDisplay().getHeight()-mView.getHeight();
			}
			
			mWM.updateViewLayout(mView, mParams);
			
			rawX = moveX;
			rawY = moveY;
			break;
		
		case MotionEvent.ACTION_UP://抬起。
			//存储Toast位置。
			splashUtils.putInt(mContext, myConstantValue.TOAST_X, mParams.x);
			splashUtils.putInt(mContext, myConstantValue.TOAST_Y, mParams.y);
			break;
		default:
			break;
		}
		return true;
	}

}
