package com.example.mobilesafe.testUnit;

import com.example.mobilesafe.dao.ReadContact;

import android.test.AndroidTestCase;
import android.util.Log;

public class readContactsTest extends AndroidTestCase {
	public void testReadContact(){
		Log.d("mimetype", ReadContact.readContacts(getContext()).toString());
	}
}
