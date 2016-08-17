package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.homeToolsBean;
import com.example.mobilesafe.spUtils.myConstantValue;
import com.example.mobilesafe.spUtils.splashUtils;
import com.nineoldandroids.animation.ObjectAnimator;

public class HomeActivity extends Activity {

	private ImageView logo_home;
	private ImageView setting;
	private GridView gv_tools;

	// 功能界面标题文字。
	private String[] titles = { "手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀",
			"缓存清理", "高级工具" };
	// 标题描述
	private String[] desc = { "手机丢失好找", "防监听防骚扰", "方便管理软件", "保持手机流畅", "注意流量超标",
			"手机安全保障", "清理垃圾数据", "魔法革好工具" };

	// 图片Id数组。
	private int[] icons = { R.drawable.sjfd, R.drawable.srlj, R.drawable.rjgj,
			R.drawable.jcgl, R.drawable.lltj, R.drawable.sjsd, R.drawable.hcql,
			R.drawable.szzx };
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 初始化界面
		initView();
		// 给主界面logo设置主界面。
		startAnimationForLogo();

		initDate();

		initAnimation();

		initEvent();
	}

	/**
	 * 给主界面logo设置动画。
	 */
	private void startAnimationForLogo() {

		ObjectAnimator animator = ObjectAnimator.ofFloat(logo_home,
				"rotationY", 0, 60, 120, 180, 240, 300, 360);
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
	 * @company Newbie
	 * @date 2016-8-16
	 * @des 给gridView设置适配器。
	 */
	class myAdapter extends BaseAdapter {

		private List<homeToolsBean> toolsBeans;
		private LayoutInflater inflater;

		public myAdapter(String[] titles, String[] desc, int[] images,
				Context context) {
			super();
			toolsBeans = new ArrayList<homeToolsBean>();
			inflater = LayoutInflater.from(context);
			for (int i = 0; i < images.length; i++) {

				homeToolsBean toolsBean = new homeToolsBean(titles[i], desc[i],
						images[i]);
				toolsBeans.add(toolsBean);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (toolsBeans != null) {
				return toolsBeans.size();
			} else {

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
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_gv_home_tools,
						null);

				viewHolder = new ViewHolder();
				viewHolder.titles = (TextView) convertView
						.findViewById(R.id.tv_title);
				viewHolder.desc = (TextView) convertView
						.findViewById(R.id.tv_desc);
				viewHolder.imageId = (ImageView) convertView
						.findViewById(R.id.iv_gv_item_icon);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.titles.setText(toolsBeans.get(position).getTitle());
			viewHolder.desc.setText(toolsBeans.get(position).getDesc());
			viewHolder.imageId.setImageResource(toolsBeans.get(position)
					.getIconId());
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
	 * @company Newbie
	 * @date 2016-8-16
	 * @des 新增一个内部类ViewHolder,用于对控件的实例进行缓存。
	 */
	class ViewHolder {
		public TextView titles;
		public TextView desc;
		public ImageView imageId;
	}

	private void initDate() {

		myAdapter adapter = new myAdapter(titles, desc, icons,
				getApplicationContext());
		gv_tools.setAdapter(adapter);
	}

	/**
	 * 给主界面的各个功能设置点击事件。
	 */
	private void initEvent() {
		gv_tools.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position) {
				case 0:
					// 设置密码。
					String password = splashUtils.getString(
							getApplicationContext(), myConstantValue.PASSWORD,
							"");
					if (TextUtils.isEmpty(password)) {
						// 首次进入需要设置密码。
						showDialogSetting();
					} else {
						// 再次进入需要输入密码。
						showEnterDialog();
					}
					break;

				default:
					break;
				}

			}

		});
	}

	/*
	 * 显示输入密码对话框
	 */
	protected void showEnterDialog() {
		// TODO Auto-generated method stub
		showDialogPassword(false);
	}

	/**
	 * 显示设置密码对话框。
	 * 
	 * @param isSetting
	 */
	protected void showDialogSetting() {
		showDialogPassword(true);
	}

	/**
	 * 显示对话框，
	 * 
	 * @param isPassword
	 */
	public void showDialogPassword(final boolean isPassword) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_set_password, null);

		final EditText ed_input_password = (EditText) view
				.findViewById(R.id.ed_input_password);
		final EditText ed_confirm_password = (EditText) view
				.findViewById(R.id.ed_confirm_password);
		TextView tv_dialog_title = (TextView) view.findViewById(R.id.dialog_title);

		Button bt_confirm = (Button) view.findViewById(R.id.bt_dialog_confirm);
		Button bt_cancle = (Button) view.findViewById(R.id.bt_dialog_cancle);

		// 判断是否是输入密码，是。。修改标题，隐藏输入框，改变按钮文字。
		if (!isPassword) {

			tv_dialog_title.setText("输入密码");

			ed_confirm_password.setVisibility(View.GONE);

		}

		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				// 点击确定按钮
				case R.id.bt_dialog_confirm:

					if (!isPassword) {
						ed_confirm_password.setText("周杰伦");

					}

					if (TextUtils.isEmpty(ed_input_password.getText()
							.toString().trim())
							|| TextUtils.isEmpty(ed_confirm_password.getText()
									.toString().trim())) {

						Toast.makeText(getApplicationContext(), "密码不能为空", 0)
								.show();
						return;
					}
					if (!isPassword) {

						//设置密码
						if (ed_input_password.getText().toString().trim().equals(splashUtils.getString(getApplicationContext(), myConstantValue.PASSWORD, ""))) {
							
							//密码设置成功，跳转到手机防盗界面。
							Toast.makeText(getApplicationContext(), "跳转到手机防盗界面", 0).show();
							
						}else {
							//输入密码错误。
							Toast.makeText(getApplicationContext(),
									"输入密码错误", 0).show();
							return;
						}
						
					} else {
						if (ed_confirm_password
								.getText()
								.toString()
								.trim()
								.equals(ed_input_password.getText().toString()
										.trim())) {
							// 密码相同
							splashUtils.putString(getApplicationContext(),
									myConstantValue.PASSWORD,
									ed_confirm_password.getText().toString());
							Toast.makeText(getApplicationContext(),
									"密码设置成功！！！", 0).show();

							// 关闭对话框
							dialog.dismiss();
						} else {
							// 两次输入密码不相同。
							Toast.makeText(getApplicationContext(),
									"两次输入密码不一致", 0).show();
							return;
						}
					}

					break;

				// 点击取消按钮。
				case R.id.bt_dialog_cancle:
					dialog.dismiss();
					break;
				default:
					break;
				}
			}
		};

		bt_confirm.setOnClickListener(listener);
		bt_cancle.setOnClickListener(listener);
		builder.setView(view);

		dialog = builder.create();
		dialog.show();
	}

	private void initAnimation() {
		// TODO Auto-generated method stub

	}

}
