package com.example.androidcameraapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private static final String TAG = "ErrorTag";
	
	//相机预览相关变量
	private Camera mCamera;
	private CameraPreview mPreview;
	
	//照片相关变量
	private String pathPhotos; 	
	private static File  dirDCIM;
	private static final String dirPhotos = "AndroidCameraApp";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//照片路径初始化
		dirDCIM = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		pathPhotos = dirDCIM.toString() + "/" + dirPhotos;
		
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
		
		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		mPreview.getHolder().setFixedSize(widthWin, widthWin/3*4);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);

		Button btnCapture = (Button)findViewById(R.id.button_capture);
		btnCapture.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub       
				mCamera.takePicture(shutterCallback, rawCallback,null, mPicture);
				new AlertDialog.Builder(MainActivity.this)
            	.setTitle("照片保存成功")
            	.setMessage("照片路径：\r\n"+pathPhotos)
            	.setPositiveButton("确定",null)
            	.show();
				//弹出Toast提示按钮被点击了
				//Toast.makeText(MainActivity.this,"Capture Camera & Save Photo Completed",Toast.LENGTH_SHORT).show();
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	} 
	
	/** A basic Camera preview class */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

		private SurfaceHolder mHolder;
		private Camera mCamera;
		private Camera.Parameters parameters;
		private List<Camera.Size> mSupportedPreviewSizes;
		private List<Camera.Size> mSupportedPictureSizes;

		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			
			parameters = mCamera.getParameters();			
			// supported preview sizes
	        mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
	        // Check what resolutions are supported by your camera
			mSupportedPictureSizes = parameters.getSupportedPictureSizes();

			// Install a SurfaceHolder.Callback so we get notified when the
			// underlying surface is created and destroyed.
			mHolder = getHolder();
			mHolder.addCallback(this);
			//deprecated setting, but required on Android versions prior to 3.0
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			{
				mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			try {
				//orientation方向设置
				if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
					parameters.set("orientation", "portrait");
					mCamera.setDisplayOrientation(90);
					// Uncomment for Android 2.0 and above
					parameters.setRotation(90);
				} else {
					parameters.set("orientation", "landscape");
					mCamera.setDisplayOrientation(0);
					// Uncomment for Android 2.0 and above
					parameters.setRotation(0);
				}
				mCamera.setParameters(parameters);

				//resolution手机相机分辨率设置
				// Iterate through all available resolutions and choose one.
				// The chosen resolution will be stored in mSize.
				Size mSizeBest = null;
				Size sizeBetter = null;
				List<Size> sizesBetter=new ArrayList<Size>();
				float ratio=1.0f;
//				String strResolutions="";
//				//构造方法的字符格式这里如果小数不足2位,会以0补足.
//				DecimalFormat decimalFormat=new DecimalFormat(".00");
				for (Size size : mSupportedPictureSizes) {					
					int longer = size.height>=size.width ? size.height : size.width;
					int smaller= size.height< size.width ? size.height : size.width;
					ratio = (float)longer / smaller;
//					strResolutions += 
//							longer + " * " + smaller + 
//							"  " + decimalFormat.format(ratio) + "\r\n";
					
					//范围不合适，在某些手机上容易闪退
					if(longer==640 && smaller==480){
						mSizeBest = size;
						break;
					}
					if(ratio>1.3 && ratio<1.4 && longer>640 && longer<5000){
						sizesBetter.add(size);
					}
				} 
				if(mSizeBest!=null)
				{
					parameters.setPictureSize(mSizeBest.width, mSizeBest.height);
					mCamera.setParameters(parameters);
				}
				else
				{
					int nSize = sizesBetter.size();
					if(nSize>0){
						sizeBetter = sizesBetter.get(0);
						int longer = sizeBetter.height>=sizeBetter.width ? sizeBetter.height : sizeBetter.width;					
						int minLonger = longer;
						int indexMin = 0;
						for(int i=0;i<nSize;i++){
							sizeBetter = sizesBetter.get(i);
							longer = sizeBetter.height>=sizeBetter.width ? sizeBetter.height : sizeBetter.width;						
							if(longer < minLonger){
								minLonger = longer;
								indexMin = i;
							}
						}
						sizeBetter = sizesBetter.get(indexMin);
					}
					if(sizeBetter!=null){
						parameters.setPictureSize(sizeBetter.width, sizeBetter.height);
						mCamera.setParameters(parameters);
					}
				}
				//when the surface is created, we can set the camera to draw images in this surfaceholder
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview(); 
			} catch (IOException e) {
				mCamera.release();
				Log.e("ERROR", "Camera error on surfaceCreated " + e.getMessage());
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// empty. Take care of releasing the Camera preview in your activity.
			//our app has only one screen, so we'll destroy the camera in the surface
			//if you are unsing with more screens, please move this code your activity
			mCamera.stopPreview();
			mCamera.release();
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			// If your preview can change or rotate, take care of those events here.
			// Make sure to stop the preview before resizing or reformatting it.

			if (mHolder.getSurface() == null){
				// preview surface does not exist
				return;
			}

			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e){
				// ignore: tried to stop a non-existent preview
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here
			
			// start preview with new settings
			try {	
				Size mSizeBest = null;
				List<Size> sizesBetter=new ArrayList<Size>();
				float ratio=1.0f;
				for (Size size : mSupportedPreviewSizes) {					
					int longer = size.height>=size.width ? size.height : size.width;
					int smaller= size.height< size.width ? size.height : size.width;
					ratio = (float)longer / smaller;
					//范围不合适，在某些手机上容易闪退
					if(longer==640 && smaller==480){
						mSizeBest = size;
						break;
					}
					if(ratio>1.3 && ratio<1.4 && longer>640 && longer<5000){
						sizesBetter.add(size);
					}
				} 
				if(mSizeBest!=null)
				{
					parameters.setPreviewSize(mSizeBest.width, mSizeBest.height);
				}
				else
				{
					//parameters.setPreviewSize(mOptimalPreviewSize.width, mOptimalPreviewSize.height);
				}
				mCamera.setParameters(parameters);
	            
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();

			} catch (Exception e){
				Log.d(TAG, "Error starting camera preview: " + e.getMessage());
			}
		}
	}

	// Called when shutter is opened
	ShutterCallback shutterCallback = new ShutterCallback() { 
		public void onShutter() {
			Log.d("", "onShutter'd");
		}
	};

	// Handles data for raw picture
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("", "onPictureTaken - raw");
		}
	};

	PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File pictureFile = getOutputMediaFile();
			if (pictureFile == null) {
				//Toast.makeText(getActivity(), "Image retrieval failed.", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}
			Log.d("", "onPictureTaken - jpeg");
			camera.startPreview();
		}
	};

	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile() {
		File mediaStorageDir = new File(dirDCIM,dirPhotos);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}
}
