package com.example.androidcameraapp;

import com.UserDevice.UserCamera.UserCamera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private UserCamera mUserCamera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		Window window = getWindow();//得到窗口
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		//window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//设置高亮

		//获取屏幕宽度和高度
		WindowManager manager = this.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int widthWin = outMetrics.widthPixels;
		//int heightWin = outMetrics.heightPixels;
		
		mUserCamera = new UserCamera(this);	
		mUserCamera.SetPreviewSize(widthWin, widthWin/3*4);
		//set it as the content of our activity.
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mUserCamera.mPreview);

		Button btnCapture = (Button)findViewById(R.id.button_capture);
		btnCapture.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 
				mUserCamera.CaptureCamera();
				//弹出Toast提示按钮被点击了
				Toast.makeText(
						MainActivity.this,
						"照片保存成功\r\n"+mUserCamera.pathPhotos,Toast.LENGTH_SHORT
						).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			MainActivity.this.finish();//跳转要关闭前一个activity
			
			//setContentView(R.layout.activity_setting);
			return true;
		}
		if(id == R.id.action_resolutions){
			new AlertDialog.Builder(MainActivity.this)
        	.setTitle("分辨率支持")
        	.setMessage(mUserCamera.mPreview.strResolutions)
        	.setPositiveButton("确定",null)
        	.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}	
}
