package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.homeToolsBean;
import com.nineoldandroids.animation.ObjectAnimator;

public class HomeActivity extends Activity {

	private ImageView logo_home;
	private ImageView setting;
	private GridView gv_tools;
 
	//功能界面标题文字。
	private String[] titles = {"手机防盗","通讯卫士",
								"软件管家","进程管理",
								"流量统计","病毒查杀",
								"缓存清理","高级工具"};
	//标题描述
	private String[] desc = {"手机丢失好找","防监听防骚扰",
								"方便管理软件","保持手机流畅",
								"注意流量超标","手机安全保障",
								"清理垃圾数据","魔法革好工具"};
	
	//图片Id数组。
	private int[] icons = {R.drawable.sjfd,R.drawable.srlj,
							R.drawable.rjgj,R.drawable.jcgl,
							R.drawable.lltj,R.drawable.sjsd,
							R.drawable.hcql,R.drawable.szzx};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//初始化界面
		initView();
		//给主界面logo设置主界面。
		startAnimationForLogo();
		
		initDate();
	
		initAnimation();
		
		
		initEvent();
	}
	
	/**
	 * 给主界面logo设置动画。
	 */
	private void startAnimationForLogo() {
		
		ObjectAnimator animator = ObjectAnimator.ofFloat(logo_home, "rotationY", 0,60,120,180,240,300,360);
		animator.setDuration(2000);
		animator.setRepeatCount(ObjectAnimator.INFINITE);
		animator.start();
 
	}

	/**
	 * 初始主化界面
	 */
	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_home);
		logo_home = (ImageView) findViewById(R.id.logo);
		setting = (ImageView) findViewById(R.id.setting);
		gv_tools = (GridView) findViewById(R.id.gv_home_tools);
	}

	
	/**
	 * 
	 * @author Administrator
	 *@company Newbie
	 *@date 2016-8-16
	 *@des 给gridView设置适配器。
	 */
	class myAdapter extends BaseAdapter{

		private List<homeToolsBean> toolsBeans;
		private LayoutInflater inflater; 
		public myAdapter(String[] titles,String[] desc, int[] images, Context context){
			super();
			toolsBeans = new ArrayList<homeToolsBean>();
			inflater = LayoutInflater.from(context);
			for (int i = 0; i < images.length; i++) {

				homeToolsBean toolsBean = new homeToolsBean(titles[i], desc[i], images[i]);
				toolsBeans.add(toolsBean);
			}
		}
		
		
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (toolsBeans!=null) {
				return toolsBeans.size();
			}else {
				
				return 0;
			}
		}
		
		
 
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			  ViewHolder viewHolder; 
		        if (convertView == null) 
		        { 
		            convertView = inflater.inflate(R.layout.item_gv_home_tools, null); 
		            
		            viewHolder = new ViewHolder(); 
		            viewHolder.titles = (TextView) convertView.findViewById(R.id.tv_title); 
		            viewHolder.desc = (TextView) convertView.findViewById(R.id.tv_desc);
		            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_gv_item_icon); 
		            
		            convertView.setTag(viewHolder); 
		        } else
		        { 
		            viewHolder = (ViewHolder) convertView.getTag(); 
		        } 
		        viewHolder.titles.setText(toolsBeans.get(position).getTitle()); 
		        viewHolder.image.setImageResource(toolsBeans.get(position).getIconId()); 
		        return convertView; 
		}



		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return toolsBeans.get(position);
		}
		
	}
	
	
	/**
	 * 
	 * @author Administrator
	 *@company Newbie
	 *@date 2016-8-16
	 *@des 新增一个内部类ViewHolder,用于对控件的实例进行缓存。
	 */
	class ViewHolder{
		 public TextView titles; 
		 public TextView desc;
		 public ImageView image;
	}
	
	
	private void initDate() {

		myAdapter adapter = new myAdapter(titles, desc, icons, getApplicationContext());
		gv_tools.setAdapter(adapter);
	}
	
	private void initEvent() {
		// TODO Auto-generated method stub
		
	}


	private void initAnimation() {
		// TODO Auto-generated method stub
		
	}

}
