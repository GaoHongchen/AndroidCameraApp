package com.chenguang.camera;

import com.chenguang.camera.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CamSelectActivity extends Activity {

	private ListView lv;
	private final String[] values = new String[]{"前置摄像头", "后置摄像头"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_camselect);
		
		setTitle(R.string.activity_name_camselect);
		
		lv = (ListView) findViewById(R.id.lv_camselect);
		lv.setAdapter(new ArrayAdapter<String>(this,
		                android.R.layout.simple_list_item_single_choice, values));
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		lv.setItemChecked(getIntent().getExtras().getInt("action_camselect"), true);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				lv.setItemChecked(arg2, true);
				
				Intent intent = new Intent();
				intent.setClass(CamSelectActivity.this, MainActivity.class);
				Bundle b = new Bundle();
				b.putInt("camType", arg2);
				intent.putExtras(b);
				startActivity(intent);
				CamSelectActivity.this.finish();
			}	
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);

		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Intent intent = new Intent();
			intent.setClass(CamSelectActivity.this, MainActivity.class);
			startActivity(intent);
			CamSelectActivity.this.finish();
		}
		
		return false;
	}

}
