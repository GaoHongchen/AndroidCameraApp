package com.chenguang.camera;

import com.chenguang.camera.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_setting);
		
		setTitle(R.string.activity_name_setting);
		
		String[] values = new String[] { "设置","关于" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//super.onListItemClick(l, v, position, id);
		
		String item = (String) getListAdapter().getItem(position);
	    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);
		
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Intent intent = new Intent();
			intent.setClass(SettingActivity.this, MainActivity.class);
			startActivity(intent);
			SettingActivity.this.finish();
		}
		
		return false;
	}
}
