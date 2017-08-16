package com.chenguang.camera;

import com.UserDevice.Sensors.OrientationSensor;
import com.UserDevice.UserCamera.CameraPreview;
import com.UserDevice.UserCamera.UserCamera;
import com.chenguang.camera.R;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String LOG_TAG = "GaoHCLog-->MainActivity";
	
	private UserCamera mUserCamera=null;
	private int nCamSelected;
	private int widthCameraPreview;
	private int heightCameraPreview;
	
	private ImageView imgCircleStatic;
	private ImageView imgCircle;
	private int xImgCircle;
	private int yImgCircle;
	private OrientationSensor orientationSensor;
	
	public MainActivity(){
		nCamSelected = 1;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		CheckCameraPermission(this);
		
		setContentView(R.layout.activity_main);
		setTitle(R.string.activity_name_main);
		
		Window window = getWindow();
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		WindowManager manager = this.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int widthWin = outMetrics.widthPixels;
		//int heightWin = outMetrics.heightPixels;
			
		widthCameraPreview = widthWin;
		heightCameraPreview = widthWin*4/3;
		
		InitOrientationSensor();
		
		InitUserCamera();
		
		Button btnCapture = (Button)findViewById(R.id.button_capture);
		btnCapture.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 
				mUserCamera.CaptureCamera();
				Toast.makeText(
						MainActivity.this,
						"Picture saved!\r\n"+mUserCamera.pathPhotos,Toast.LENGTH_SHORT
						).show();
			}
		});
	}

	private void CheckCameraPermission(Context context) {
		// check Android 6 permission
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
			Log.i(LOG_TAG, "CheckCameraPermission: Granted");
		} else {
			// 1 can be another integer
			ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.CAMERA }, 1);
		}
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
			try{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SettingActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
			catch(Exception e){
				Log.e(LOG_TAG, "onOptionsItemSelected: action_settings: "+e.getMessage());
			}
			return true;
		}
		
		if(id == R.id.action_camselect) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, CamSelectActivity.class);
//			Bundle b = new Bundle();
//			b.putInt("action_camselect", nCamSelected);
//			intent.putExtras(b);
			intent.putExtra("action_camselect", nCamSelected);
			startActivity(intent);
			MainActivity.this.finish();
			return true;
		}
		
		if(id == R.id.action_resolutions){
			try{
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ResolutionSelectActivity.class);
				String[] arrResolutions = 
						CameraPreview.listResolutionsString.toArray(new String[CameraPreview.listResolutionsString.size()]);
				intent.putExtra("action_resolutions", arrResolutions);
				startActivity(intent);
				MainActivity.this.finish();
			}
			catch(Exception e){
				Log.e(LOG_TAG, "onOptionsItemSelected: action_resolutions: "+e.getMessage());
			}
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		orientationSensor.RegisterListener();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub		
		super.onStop();
		
		orientationSensor.UnRegisterListener();
	}
	
	
	private void InitOrientationSensor(){
		imgCircleStatic  = (ImageView)findViewById(R.id.img_circle_static);
		imgCircle = (ImageView)findViewById(R.id.img_circle);
		
		ViewGroup.LayoutParams parasImgCircleStatic = imgCircleStatic.getLayoutParams();
		int diameterCircleStatic = 100;
		parasImgCircleStatic.width  = diameterCircleStatic;
		parasImgCircleStatic.height = diameterCircleStatic;
		imgCircleStatic.setLayoutParams(parasImgCircleStatic);
		
		int xImgCircleStatic = (widthCameraPreview-diameterCircleStatic)/2;
		int yImgCircleStatic = (heightCameraPreview-diameterCircleStatic)/2;
		imgCircleStatic.setTranslationX(xImgCircleStatic);
		imgCircleStatic.setTranslationY(yImgCircleStatic);
			
		ViewGroup.LayoutParams parasImgCircle = imgCircle.getLayoutParams();
		int diameterCircle = 90;
		parasImgCircle.width  = diameterCircle;
		parasImgCircle.height = diameterCircle;
		imgCircle.setLayoutParams(parasImgCircle);
		
		xImgCircle = (widthCameraPreview-diameterCircle)/2;
		yImgCircle = (heightCameraPreview-diameterCircle)/2;
		imgCircle.setTranslationX(xImgCircle);
		imgCircle.setTranslationY(yImgCircle);	
		
		orientationSensor = new OrientationSensor(this,imgCircle,xImgCircle,yImgCircle);
	}
	
	private void InitUserCamera(){
		try{
			nCamSelected = getIntent().getExtras().getInt("camType");
		}
		catch(Exception e){
			Log.e(LOG_TAG, "InitUserCamera: getIntent().getExtras().getInt(camType) exception");
		}
		
		Log.i(LOG_TAG, "InitUserCamera: nCamType = "+String.valueOf(nCamSelected));

		try{
			if(nCamSelected == 0){
				mUserCamera = new UserCamera(this,UserCamera.CameraType.CAMERA_FRONT);	
			}
			if(nCamSelected == 1){
				mUserCamera = new UserCamera(this,UserCamera.CameraType.CAMERA_BACK);	
			}
			
			if(mUserCamera.mCamera != null){
				Log.i(LOG_TAG, "InitUserCamera: mUserCamera.mCamera != null");
				mUserCamera.SetPreviewSize(widthCameraPreview, heightCameraPreview);
				//set it as the content of our activity.
				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
				preview.addView(mUserCamera.mPreview);
			}
			else{
				Log.e(LOG_TAG, "InitUserCamera: mUserCamera.mCamera == null");
			}
		}
		catch(Exception e){
			Log.e(LOG_TAG, "InitUserCamera: new UserCamera() exception");
		}
	}
}
