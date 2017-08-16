package com.chenguang.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ResolutionSelectActivity extends Activity {

	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_resolutions);

		setTitle(R.string.activity_name_resolutions);

		lv = (ListView) findViewById(R.id.lv_resolutions);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,
				getIntent().getExtras().getStringArray("action_resolutions")));
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		lv.setItemChecked(1, true);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
			}	
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);
		
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Intent intent = new Intent();
			intent.setClass(ResolutionSelectActivity.this, MainActivity.class);
			startActivity(intent);
			ResolutionSelectActivity.this.finish();
		}
		
		return false;
	}	
}
