package com.UserDevice.UserCamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

@SuppressWarnings("deprecation")
public class UserCamera{
		
	public static enum CameraType{CAMERA_FRONT,CAMERA_BACK};

	private static final String LOG_TAG = "GaoHCLog-->UserCamera";
	
	private  CameraType typeCam;	
	
	public Camera mCamera=null;
	public CameraPreview mPreview;
	
	//照片相关变量
	public String pathPhotos; 	
	private static File  dirDCIM;
	private static final String dirPhotos = "AndroidCameraApp";
	
	public UserCamera(Context context,CameraType nCamType){
		
		//获得操作Camera的权限
		CheckCameraPermission(context);
		
		//照片路径初始化
		dirDCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		pathPhotos = dirDCIM.toString() + "/" + dirPhotos;
		
		typeCam = nCamType;
        
		mCamera = getCameraInstance();

		if(mCamera != null){
			Log.i(LOG_TAG, "UserCamera: mCamera != null");
			mPreview = new CameraPreview(context, mCamera, typeCam);
		}
		else{
			Log.e(LOG_TAG, "UserCamera: mCamera == null");
		}
	}

	private void CheckCameraPermission(Context context) {
		// check Android 6 permission
		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
			Log.i(LOG_TAG, "Granted");
		} else {
			// 1 can be another integer
			ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.CAMERA }, 1);
		}
	}
	
	private int FindFrontCamera(){  
        int cameraCount = 0;  
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();  
        cameraCount = Camera.getNumberOfCameras(); // get cameras number  
                
        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {  
            Camera.getCameraInfo( camIdx, cameraInfo );
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT ) {   
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置  
               return camIdx;  
            }  
        }  
        return -1;  
    }  
 
    private int FindBackCamera(){  
        int cameraCount = 0;  
        try{
        	Camera.CameraInfo cameraInfo = new Camera.CameraInfo();  
            cameraCount = Camera.getNumberOfCameras(); // get cameras number   
            Log.i(LOG_TAG, "FindBackCamera: cameraCount == "+String.valueOf(cameraCount));
            
            for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {  
                Camera.getCameraInfo( camIdx, cameraInfo );
                if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK ) {   
                    return camIdx;  
                 }  
             	break;
            }  
        }
        catch (Exception e){
			Log.e(LOG_TAG, "FindBackCamera: exception");
		}
        return -1;  
    }  
    
	/** A safe way to get an instance of the Camera object. */
	public Camera getCameraInstance(){
		int CammeraIndex = -1;
		Camera c = null;
		try {
			switch(typeCam){
			case CAMERA_FRONT:
				Log.i(LOG_TAG, "getCameraInstance: typeCam == CAMERA_FRONT");
				CammeraIndex=FindFrontCamera();  
		        if(CammeraIndex==-1){  
		            CammeraIndex=FindBackCamera();  
		        }  
		        c = Camera.open(CammeraIndex);
				break;
			case CAMERA_BACK:
				Log.i(LOG_TAG, "getCameraInstance: typeCam == CAMERA_BACK");
				CammeraIndex=FindBackCamera(); 
				Log.i(LOG_TAG, "getCameraInstance: CammeraIndex == "+String.valueOf(CammeraIndex));
				c = Camera.open(CammeraIndex);//需要获得操作Camera的权限，否则会异常
				break;
			} 
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
			Log.e(LOG_TAG, "getCameraInstance: exception");
		}
		return c; // returns null if camera is unavailable
	}
	
	public void SetPreviewSize(int width,int height){
		mPreview.getHolder().setFixedSize(width, height);
	}
	
	public void CaptureCamera(){
		mCamera.takePicture(shutterCallback, rawCallback,null, mPicture);
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
