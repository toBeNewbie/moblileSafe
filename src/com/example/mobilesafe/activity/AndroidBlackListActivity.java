package com.example.mobilesafe.activity;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.BlcakListBean;
import com.example.mobilesafe.dao.BlackListDao;
import com.example.mobilesafe.db.BlackListDB;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.utils.ShowToastUtils;

/**
 * 
 * @author Administrator
 * @company Newbie
 * @date 2016-8-21
 * @des 黑名单显示的界面。
 */
public class AndroidBlackListActivity extends Activity {

	protected static final int LOADING = 1;
	protected static final int FINISH = 2;
	protected static final int COUNTPERPAGE = 10;
	private ImageView iv_add_blacklist;
	private LinearLayout ll_progressbar_root;
	private ListView lv_black_showdata;
	private ImageView iv_black_nodata;

	private PopupWindow mPopupWindow;
	private View contentView;
	private Animation mAnimation;

	private AlertDialog mAlertDialog;

	private MyAdapter myAdapter;
	private BlackListDao blackListDao;
	private boolean mIsFirst;

	private List<BlcakListBean> mListBeans = new Vector<BlcakListBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 初始化界面
		initView();

		
		// 初始化数据。
		initData();
		// 初始化事件
		initEvent();

		initPopup();

		initAlertDialog();

	}

	private void initAlertDialog() {

		View mView = View.inflate(getApplicationContext(),
				R.layout.dialog_add_blacklist, null);
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

		inputBlack = (EditText) mView.findViewById(R.id.ed_input_blacklist);
		phoneIntercept = (CheckBox) mView.findViewById(R.id.cb_phone_intercept);
		smsIntercept = (CheckBox) mView.findViewById(R.id.cb_sms_intercept);

		Button blacklistAdd = (Button) mView
				.findViewById(R.id.bt_blacklist_add);
		Button blacklistCancle = (Button) mView
				.findViewById(R.id.bt_blacklist_cancle);

		// 点击按钮添加黑名单到列表中。
		blacklistAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String phone = inputBlack.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					ShowToastUtils.showToast("黑名单号码不能为空",
							AndroidBlackListActivity.this);
					return;
				}

				if (!phoneIntercept.isChecked() && !smsIntercept.isChecked()) {
					ShowToastUtils.showToast("拦截模式至少选一个",
							AndroidBlackListActivity.this);
					return;
				}

				BlcakListBean listBean = new BlcakListBean();
				listBean.setPhone(phone);
				int mode = 0;
				if (phoneIntercept.isChecked()) {
					mode |= BlackListDB.PHONE_MODE;
				}

				if (smsIntercept.isChecked()) {
					mode |= BlackListDB.SMS_MODE;
				}

				listBean.setMode(mode);

				blackListDao.update(listBean);

				mIsFirst = true;
				// 显示最新添加的数据。
				mListBeans.clear();
				initData();

				mAlertDialog.dismiss();

			}
		});

		// 点击按钮取消对话框。
		blacklistCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertDialog.dismiss();
			}
		});

		mBuilder.setView(mView);
		mAlertDialog = mBuilder.create();

	}

	/**
	 * 初始化popup界面。
	 */
	private void initPopup() {
		contentView = View.inflate(getApplicationContext(),
				R.layout.ativity_add_blacklist_popup, null);

		mPopupWindow = new PopupWindow(contentView, 100, -2);
		mPopupWindow.setFocusable(true);
		mPopupWindow
				.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setOutsideTouchable(true);

		// 获取子组件，添加点击事件。
		TextView manual_add = (TextView) contentView
				.findViewById(R.id.tv_popup_manual_add);
		TextView sms_add = (TextView) contentView
				.findViewById(R.id.tv_popup_sms_add);
		TextView phone_add = (TextView) contentView
				.findViewById(R.id.tv_popup_phone_add);
		TextView friend_add = (TextView) contentView
				.findViewById(R.id.tv_popup_friend_add);

		OnClickListener mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_popup_manual_add:// 手动添加。
					manualAdd();
					break;
				case R.id.tv_popup_sms_add:
					smsAdd();
					break;

				case R.id.tv_popup_phone_add:
					phoneAdd();
					break;

				case R.id.tv_popup_friend_add:
					friendAdd();
					break;
				default:
					break;
				}
				mPopupWindow.dismiss();

			}
		};

		// 给子组件添加按钮点击事件。
		manual_add.setOnClickListener(mClickListener);
		sms_add.setOnClickListener(mClickListener);
		phone_add.setOnClickListener(mClickListener);
		friend_add.setOnClickListener(mClickListener);

		mAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.0f);
		mAnimation.setDuration(400);
	}

	// 好友添加黑名单。
	protected void friendAdd() {
		// 启动显示联系人界面。
		Intent intent = new Intent(this, ContactList.class);
		startActivityForResult(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {

			String safeNumber = data
					.getStringExtra(myConstantValue.MOBILE_SAFE_NUMBER);
			showDialog(safeNumber);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 电话添加黑名单。
	protected void phoneAdd() {

		Intent intent = new Intent(this, TelAddActivity.class);
		startActivityForResult(intent, 0);
	}

	// 短信添加黑名单。
	protected void smsAdd() {

		Intent intent = new Intent(this, SmsAddActivity.class);
		startActivityForResult(intent, 0);
	}

	
	private void showDialog(String phone){
		
		inputBlack.setText(phone);
		
		mAlertDialog.show();
	}
	
	// 手动添加黑名单。
	protected void manualAdd() {
		// ShowToastUtils.showToast("manualAdd", this);
		showDialog("");
	}

	private void initEvent() {
		
		/*//给listview添加点击事件。
		lv_black_showdata.setOnRefreshingDataListener(new OnRefreshingDataListener() {
			
			@Override
			public void onHeadRefreshing() {
				// 添加自己的代码下拉刷新。
				mListBeans.clear();
				initData();
				
			}
			
			@Override
			public void onFooterFreshing() {
				// TODO Auto-generated method stub
				initData();
			}
		});*/
		
		
		lv_black_showdata.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					
					//最后一条数据显示的位置。
					int lastVisiblePosition = lv_black_showdata.getLastVisiblePosition();
					if (lastVisiblePosition>=(mListBeans.size()-1)) {
						initData();
					}
				}
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// 初始化popup界面。
		iv_add_blacklist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPopupWindow.isShowing() && mPopupWindow != null) {

					mPopupWindow.dismiss();
				} else {
					// 显示popup界面。
					mPopupWindow.showAsDropDown(v);
					contentView.startAnimation(mAnimation);

					inputBlack.setText("");
					phoneIntercept.setChecked(false);
					smsIntercept.setChecked(false);
				}
			}
		});

	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:// 加载数据

				lv_black_showdata.setVisibility(View.GONE);
				ll_progressbar_root.setVisibility(View.VISIBLE);
				// 显示加载数据的对话框，隐藏listview 和 nodata
				iv_black_nodata.setVisibility(View.GONE);
				break;

			case FINISH:// 加载数据完成。

				//完成刷新。
				// 隐藏加载数据的对话框，
				ll_progressbar_root.setVisibility(View.GONE);
				if (mListBeans.isEmpty()) {
					iv_black_nodata.setVisibility(View.VISIBLE);
					lv_black_showdata.setVisibility(View.GONE);
				} else {
					// 有数据显示listview 没有数据显示nodata.
					iv_black_nodata.setVisibility(View.GONE);
					lv_black_showdata.setVisibility(View.VISIBLE);
				}

				// 刷新界面。
				myAdapter.notifyDataSetChanged();

				if (mIsFirst) {
					// 滚动到第0条。
					lv_black_showdata.smoothScrollToPosition(0);
					mIsFirst = false;
				}
				break;
			default:
				break;
			}
		};
	};
	private EditText inputBlack;
	private CheckBox phoneIntercept;
	private CheckBox smsIntercept;

	// 显示黑名单数据的适配器。
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListBeans.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHoderBlacklist hoderBlacklist = null;
			if (convertView == null) {
				convertView = View.inflate(AndroidBlackListActivity.this,
						R.layout.item_black_list, null);
				hoderBlacklist = new ViewHoderBlacklist();

				hoderBlacklist.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_black_lv_phone);
				hoderBlacklist.tv_mode = (TextView) convertView
						.findViewById(R.id.tv_black_lv_mode);
				hoderBlacklist.iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_delete_item_blacklist);

				convertView.setTag(hoderBlacklist);

			} else {

				hoderBlacklist = (ViewHoderBlacklist) convertView.getTag();
			}

			final BlcakListBean listBean = (BlcakListBean) getItem(position);
			// 显示黑名单号码
			hoderBlacklist.tv_phone.setText(listBean.getPhone());

			switch (listBean.getMode()) {
			case BlackListDB.SMS_MODE:
				hoderBlacklist.tv_mode.setText("短信拦截");
				break;
			case BlackListDB.PHONE_MODE:
				hoderBlacklist.tv_mode.setText("电话拦截");
				break;
			case BlackListDB.ALL_MODE:
				hoderBlacklist.tv_mode.setText("全部拦截");
				break;
			default:
				break;
			}

			hoderBlacklist.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AlertDialog.Builder mAlertdialog=new AlertDialog.Builder(AndroidBlackListActivity.this);
					mAlertdialog.setTitle("魔法革提示你");
					mAlertdialog.setMessage("真的要刪除该数据");
					mAlertdialog.setCancelable(false);
					mAlertdialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mListBeans.remove(listBean);
							blackListDao.deleteBlacklist(listBean.getPhone());
							myAdapter.notifyDataSetChanged();
						
						}
					});	
					
					mAlertdialog.setNegativeButton("cancle",null);
						
					mAlertdialog.show();
				}
			});

			return convertView;
		}

	}

	class ViewHoderBlacklist {

		TextView tv_phone;

		TextView tv_mode;

		ImageView iv_delete;

	}

	private void initView() {
		setContentView(R.layout.activity_blacklist);

		// 添加黑名单按钮。
		iv_add_blacklist = (ImageView) findViewById(R.id.iv_add_blacklist);

		// 加载数据的对话框布局
		ll_progressbar_root = (LinearLayout) findViewById(R.id.ll_progressbar_root);

		// 不显示数据时的布局。
		iv_black_nodata = (ImageView) findViewById(R.id.iv_black_nodata);

		blackListDao = new BlackListDao(this);

		myAdapter = new MyAdapter();
		lv_black_showdata = (ListView) findViewById(R.id.lv_black_showdata);
		lv_black_showdata.setAdapter(myAdapter);

	}

	private void initData() {

		new Thread() {
			public void run() {
				// 发送加载数据的消息
				mHandler.obtainMessage(LOADING).sendToTarget();

				// 加载数据。
				//添加新增的数据。
//				mListBeans.addAll(blackListDao.loadMore(mListBeans.size(), COUNTPERPAGE));
					
					List<BlcakListBean> blcakListBeans = blackListDao.loadMore(mListBeans.size(), COUNTPERPAGE);
					for (int i = 0; i < blcakListBeans.size(); i++) {
						mListBeans.add(blcakListBeans.get(i));
					}
				
				SystemClock.sleep(3000);
				// 加载数据完成
				mHandler.obtainMessage(FINISH).sendToTarget();
			}

		}.start();

	}

}
