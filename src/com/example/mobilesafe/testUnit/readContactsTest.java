package com.example.mobilesafe.testUnit;

import com.example.mobilesafe.utils.AntiThrefServiceUtils;

import android.test.AndroidTestCase;

public class readContactsTest extends AndroidTestCase {
	public void testReadContact(){
		AntiThrefServiceUtils.serviceRunning(getContext(), "");
//		Log.d("mimetype", ReadContact.readContacts(getContext()).toString());
	}
}
