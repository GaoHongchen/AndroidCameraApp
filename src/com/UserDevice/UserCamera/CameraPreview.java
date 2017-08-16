package com.UserDevice.UserCamera;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.UserDevice.UserCamera.UserCamera.CameraType;

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

	private static final String LOG_TAG = "GaoHCLog-->CameraPreview";

	private CameraType typeCam;
	
	private SurfaceHolder mHolder;
	
	private Camera mCamera;
	private Camera.Parameters parameters;
	private List<Camera.Size> mSupportedPreviewSizes;
	private List<Camera.Size> mSupportedPictureSizes;
	
	public static List<String> listResolutionsString = new ArrayList<String>();
	
	public CameraPreview(Context context, Camera camera,CameraType nCamType) {
		super(context);
		if(camera == null){
			Log.e(LOG_TAG, "CameraPreview: camera == null");
			return;
		}
		mCamera = camera;
		
		typeCam = nCamType;
		
		try{
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
		catch(Exception e){
			Log.e(LOG_TAG, "CameraPreview: exception");
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if(typeCam == CameraType.CAMERA_BACK){
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
			}
			if(typeCam == CameraType.CAMERA_FRONT){
				if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
					parameters.set("orientation", "portrait");
					mCamera.setDisplayOrientation(90);
					// Uncomment for Android 2.0 and above
					parameters.setRotation(270);
				} else {
					parameters.set("orientation", "landscape");
					mCamera.setDisplayOrientation(90);
					// Uncomment for Android 2.0 and above
					parameters.setRotation(90);
				}
			}
			mCamera.setParameters(parameters);

			boolean bBestSizeSelected = false;
			listResolutionsString.clear();
			for (Size size : mSupportedPictureSizes) {					
				int longer = size.height>=size.width ? size.height : size.width;
				int smaller= size.height< size.width ? size.height : size.width;
				float ratio = longer / (float)smaller;
				listResolutionsString.add(new DecimalFormat(".00").format(ratio) + "  " + longer + " * " + smaller + "\r\n");
				if(ratio>1.3 && ratio<1.4 && longer>1000 && longer<5000 && !bBestSizeSelected){
					parameters.setPictureSize(size.width, size.height);
					bBestSizeSelected = true;
				}
			}
			mCamera.setParameters(parameters);
				
			//when the surface is created, we can set the camera to draw images in this surfaceholder
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview(); 
		} catch (IOException e) {
			mCamera.release();
			Log.e(LOG_TAG, "Camera error on surfaceCreated " + e.getMessage());
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
			boolean bBestSizeSelected = false;
			for (Size size : mSupportedPreviewSizes) {					
				int longer = size.height>=size.width ? size.height : size.width;
				int smaller= size.height< size.width ? size.height : size.width;
				float ratio = longer / (float)smaller;
				if(ratio>1.3 && ratio<1.4 && longer>1000 && longer<5000 && !bBestSizeSelected){
					parameters.setPreviewSize(size.width, size.height);
					bBestSizeSelected = true;
				}
			}
			mCamera.setParameters(parameters);
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e){
			Log.e(LOG_TAG, "surfaceChanged: exception, starting camera preview: " + e.getMessage());
		}
	}
}
