package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.ContactBean;
import com.example.mobilesafe.dao.ReadContactDao;
import com.example.mobilesafe.spUtils.myConstantValue;


/**
 * 
 * @author Administrator
 *@company Newbie
 *@date 2016-8-19
 *@des 显示联系人界面。
 */
public class ContactList extends BaseSmsTelActivity {

	public List<ContactBean> getDatas() {
		// TODO Auto-generated method stub
		return (ArrayList<ContactBean>) ReadContactDao.readContacts(getApplicationContext());
	}
	 
}
