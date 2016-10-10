package com.example.mobilesafe.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.AppInforBean;
import com.example.mobilesafe.utils.GetAppInfoUtils;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-10-10
 *@des 流量查询监控
 */
public class ConnectivityPhoneActivity extends ListActivity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	private MyAdapter mAdapter;
	private ListView lv_phoneConnectivity;
	private ConnectivityManager mCM;
	private List<ConnectivityBean> mConnectivityBeans=new ArrayList<ConnectivityPhoneActivity.ConnectivityBean>();
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		initData();
	}

	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mConnectivityBeans.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder mHolder=null;
			if (convertView!=null) {
				mHolder=(ViewHolder) convertView.getTag();
			}else {
				mHolder=new ViewHolder();
				convertView=View.inflate(getApplicationContext(), R.layout.item_phone_connectivity_mess, null);
				mHolder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_phone_connectivity_icon);
				mHolder.tv_rcvSize=(TextView) convertView.findViewById(R.id.tv_phone_connectivity_rcv);
				mHolder.tv_sndSize=(TextView) convertView.findViewById(R.id.tv_phone_connectivity_snd);
				mHolder.tv_type=(TextView) convertView.findViewById(R.id.tv_phone_connectivity_type);
				
				convertView.setTag(mHolder);
			}
			
			//显示数据
			ConnectivityBean connectivityBean = mConnectivityBeans.get(position);
			
			mHolder.tv_rcvSize.setText("接收流量："+Formatter.formatFileSize(getApplicationContext(), connectivityBean.rcvSize));
			
			mHolder.tv_sndSize.setText("发送流量："+Formatter.formatFileSize(getApplicationContext(), connectivityBean.sndSize));
			mHolder.tv_type.setText("["+connectivityBean.typeConnectivity+"]");
			mHolder.iv_icon.setImageDrawable(connectivityBean.appIcon);
			
			return convertView;
		}
		
	}
	
	
	private class ViewHolder{
		TextView tv_sndSize;
		TextView tv_rcvSize;
		TextView tv_type;
		ImageView iv_icon;
	}
	
	/**
	 * 
	 * @param filePath
	 * 		要读取的流量文件
	 * @return
	 * 		读取的流量字节大小
	 */
	private long readFile(String filePath){
		
		long size=-1;
		File file=new File(filePath);
		try {
			FileInputStream fis=new FileInputStream(file);
			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fis));
			String readLine = bufferedReader.readLine();
			size=Long.parseLong(readLine);
			fis.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
	
	}
	
	
	Handler mHandler=new Handler(){
		private ProgressDialog pb;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:
				pb = new ProgressDialog(ConnectivityPhoneActivity.this);
				pb.setTitle("提醒");
				pb.setMessage("魔法革正在玩命加载数据中......");
				pb.show();
				break;
			case FINISH:
				
				pb.dismiss();
				
				
				//更新数据
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};


	
	private class ConnectivityBean{
		public long rcvSize;
		public long sndSize;
		public Drawable appIcon;
		public String typeConnectivity;
	}
	
	
	private void initData() {
		// 加载数据
		new Thread(){
			public void run() {
				//发送加载数据的消息
				mHandler.obtainMessage(LOADING).sendToTarget();
				//加载数据
				List<AppInforBean> installAppInfos = GetAppInfoUtils.getInstallAppInfos(getApplicationContext());
				for (AppInforBean appInforBean : installAppInfos) {
					String rcv_path="/proc/uid_stat/"+appInforBean.getUid()+"/tcp_rcv";
					String snd_path="/proc/uid_stat/"+appInforBean.getUid()+"/tcp_snd";
					
					//判断是否有流量信息
					long rcvConnectivity = readFile(rcv_path);
					long sndConnectivity = readFile(snd_path);
					
					if (rcvConnectivity == -1 && sndConnectivity == -1) {
						//没有流量信息
						
					}else {
						//有流量信息
						ConnectivityBean connectivityBean=new ConnectivityBean();
						connectivityBean.appIcon=appInforBean.getIcon();
						connectivityBean.rcvSize=rcvConnectivity;
						connectivityBean.sndSize=sndConnectivity;
						connectivityBean.typeConnectivity=mCM.getActiveNetworkInfo().getTypeName();
						
						
						mConnectivityBeans.add(connectivityBean);
					}
					
				}
				
				//发送数据家在完成的消息
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	private void initView() {
		lv_phoneConnectivity = getListView();
		mAdapter = new MyAdapter();
		lv_phoneConnectivity.setAdapter(mAdapter);
		mCM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
}
