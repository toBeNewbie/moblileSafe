package com.example.mobilesafe.difineView;

import com.example.mobilesafe.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-10-6
 *@des 自定义程序锁界面标题view
 */
public class CustomAppLockedView extends RelativeLayout {

	private TextView tv_locked;
	private TextView tv_unlocked;

	private onLockChangedListener mLockChangedListener;
	private View mHeadView;
	
	public void setOnLockChangedListener( onLockChangedListener lockChangedListener){
		
		this.mLockChangedListener=lockChangedListener;
	}

	
	
	public CustomAppLockedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView();
		
		initEvent();
	}

	
	//定义一个接口供使用者调用
		public interface onLockChangedListener{
			
			void onLockChanged(boolean isLocked);
		}
	
	private void initEvent() {
		OnClickListener mOnClickListener=new OnClickListener() {
			
			boolean isLocked=false;
			
			@Override
			public void onClick(View v) {
				
				switch (v.getId()) {
				case R.id.tv_custom_view_locked:
					isLocked=false;
					tv_locked.setTextColor(Color.WHITE);
					tv_unlocked.setTextColor(Color.GRAY);
					tv_locked.setBackgroundResource(R.drawable.tab_left_pressed);
					tv_unlocked.setBackgroundResource(R.drawable.tab_right_default);
					break;
				case R.id.tv_custom_view_unlocked:
					tv_locked.setTextColor(Color.GRAY);
					tv_unlocked.setTextColor(Color.WHITE);
					tv_locked.setBackgroundResource(R.drawable.tab_left_default);
					tv_unlocked.setBackgroundResource(R.drawable.tab_right_pressed);
					isLocked=true;
					break;
				default:
					break;
				}
				
				if (mLockChangedListener!=null) {
					
					mLockChangedListener.onLockChanged(isLocked);
				}
			}
		};
		
		tv_locked.setOnClickListener(mOnClickListener);
		tv_unlocked.setOnClickListener(mOnClickListener);
		
	}

	private void initView() {
		
		mHeadView = View.inflate(getContext(), R.layout.custom_view_applock_head_title, this);
		tv_locked = (TextView) mHeadView.findViewById(R.id.tv_custom_view_locked);
		tv_unlocked = (TextView) mHeadView.findViewById(R.id.tv_custom_view_unlocked);
	}

	public CustomAppLockedView(Context context) {
		this(context,null);
		
	}

}
