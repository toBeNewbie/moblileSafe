package com.example.mobilesafe.difineView;

import com.example.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextProgressView extends RelativeLayout {

	private ProgressBar pb_textScale;
	private TextView tv_text_mess;



	public TextProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//初始化界面。
		initView();
	}

	
	
	private void initView() {

		View rootView_progress = View.inflate(getContext(), R.layout.view_mess_progress, this);
		pb_textScale = (ProgressBar) rootView_progress.findViewById(R.id.pb_progresstext_progressscale);
		tv_text_mess = (TextView) rootView_progress.findViewById(R.id.tv_progresstext_message);
	
		pb_textScale.setMax(100);
	}

	/**
	 * 设置进度条显示。
	 * @param scale 格式： 百分比   10% 或者  0.1
	 */
	public void setProgress(double scale){
		pb_textScale.setProgress((int) Math.round(100*scale));
	}


	/**
	 * 
	 * @param mess  显示的信息内容。
	 */
	public void setMess(String mess){
		tv_text_mess.setText(mess);
	}
	
	
	public TextProgressView(Context context) {
		this(context,null);

	}

	
	
}
