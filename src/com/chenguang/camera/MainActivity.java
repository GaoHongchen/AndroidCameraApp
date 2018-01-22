package com.chenguang.camera;

import com.UserDevice.Sensors.OrientationSensor;
import com.UserDevice.UserCamera.CameraPreview;
import com.UserDevice.UserCamera.UserCamera;
import com.chenguang.camera.R;
import com.ndk.test.FFTW3;
import com.ndk.test.JNITest;
import com.ndk.test.NEONTest;

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
	  
	private static String[] PERMISSIONS_CAMERA = { Manifest.permission.CAMERA };
	private static String[] PERMISSIONS_STORAGE = { 
	        Manifest.permission.WRITE_EXTERNAL_STORAGE,  
	        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
	
	private static final int REQUEST_PERMISSION_CAMERA = 1;
	private static final int REQUEST_EXTERNAL_STORAGE  = 2;
	
	private boolean bCameraGranted=false;
	
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
		nCamSelected = 1;//default back camera
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
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
			
		CheckStoragePermissions(this);
		
		bCameraGranted = CheckCameraPermission(this);
		if(true == bCameraGranted){		
			InitUserCamera();		
		}
		
		Button btnCapture = (Button)findViewById(R.id.button_capture);
		btnCapture.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub 
				if(true == bCameraGranted){
					mUserCamera.CaptureCamera();
					Toast.makeText(
							MainActivity.this,
							"Picture saved!\r\n"+mUserCamera.pathPhotos,Toast.LENGTH_SHORT
							).show();
				}else{
					Toast.makeText(MainActivity.this,"Camera Permission not Granted!!!",Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode){
		case REQUEST_PERMISSION_CAMERA:
			if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
				Log.i(LOG_TAG, "onRequestPermissionsResult REQUEST_PERMISSION_CAMERA: Granted");
				InitUserCamera();
				bCameraGranted = true;
			}else{
				CloseApp();
			}
			break;
		case REQUEST_EXTERNAL_STORAGE:
			if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
				Log.i(LOG_TAG, "onRequestPermissionsResult REQUEST_EXTERNAL_STORAGE: Granted");
			}else{
				CloseApp();
			}
			break;
		}
	}

	private boolean CheckCameraPermission(Context context) {
		// check Android 6 permission
        int checkCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
		if (checkCameraPermission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_CAMERA, REQUEST_PERMISSION_CAMERA);
			return false;
		} else {
			Log.i(LOG_TAG, "CheckCameraPermission: Granted");
            return true;
		}
	}  
	
	private boolean CheckStoragePermissions(Activity activity) {  
	    int checkStoragePermissions = ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);  	  
	    if (checkStoragePermissions != PackageManager.PERMISSION_GRANTED) {  
	        ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE); 
	        return false;
	    } else {
	    	Log.i(LOG_TAG, "CheckStoragePermissions: Granted");
	    	return true;
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
		
		Intent intent = new Intent();
		switch(item.getItemId()){
		case R.id.action_settings:
			try{
				intent.setClass(MainActivity.this, SettingActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
			catch(Exception e){
				Log.e(LOG_TAG, "onOptionsItemSelected: action_settings: "+e.getMessage());
			}
			break;
		case R.id.action_camselect:
			intent.setClass(MainActivity.this, CamSelectActivity.class);
//			Bundle b = new Bundle();
//			b.putInt("action_camselect", nCamSelected);
//			intent.putExtras(b);
			intent.putExtra("action_camselect", nCamSelected);
			startActivity(intent);
			MainActivity.this.finish();
			break;
		case R.id.action_resolutions:
			try{
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
			break;
		case R.id.action_ndktest:
			String strJNI = JNITest.AddString("", "");
//			FFTW3.DFT2DfromPath(mUserCamera.pathPhotos);
			NEONTest.test();
			Toast.makeText(MainActivity.this, strJNI, Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
		//return super.onOptionsItemSelected(item);
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
	
	private void CloseApp(){
		android.os.Process.killProcess(android.os.Process.myPid());
	    System.exit(0);
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
