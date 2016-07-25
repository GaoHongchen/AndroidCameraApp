package com.example.UserCamera;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

	private static final String TAG = "ErrorTag";
	
	public String strResolutions="";
	
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
			//构造方法的字符格式这里如果小数不足2位,会以0补足.
			DecimalFormat decimalFormat=new DecimalFormat(".00");
			for (Size size : mSupportedPictureSizes) {					
				int longer = size.height>=size.width ? size.height : size.width;
				int smaller= size.height< size.width ? size.height : size.width;
				ratio = (float)longer / smaller;
				strResolutions += 
						decimalFormat.format(ratio) + "  " + 
						longer + " * " + smaller + "\r\n";
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
			Log.e(TAG, "Camera error on surfaceCreated " + e.getMessage());
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
