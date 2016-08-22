package com.UserDevice.UserCamera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Environment;
import android.util.Log;

@SuppressWarnings("deprecation")
public class UserCamera{
		
	public static enum CameraType{CAMERA_FRONT,CAMERA_BACK};
	private CameraType typeCam;
	public Camera mCamera;
	public CameraPreview mPreview;
	
	//照片相关变量
	public String pathPhotos; 	
	private static File  dirDCIM;
	private static final String dirPhotos = "AndroidCameraApp";
	
	public UserCamera(Context context,CameraType nCamType){
		//照片路径初始化
		dirDCIM = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		pathPhotos = dirDCIM.toString() + "/" + dirPhotos;
		
		typeCam = nCamType;
		
		// Create an instance of Camera
		mCamera = getCameraInstance();
		// Create our Preview view
		mPreview = new CameraPreview(context, mCamera, typeCam);
	}

	private int FindFrontCamera(){  
        int cameraCount = 0;  
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();  
        cameraCount = Camera.getNumberOfCameras(); // get cameras number  
                
        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {  
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo  
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_FRONT ) {   
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置  
               return camIdx;  
            }  
        }  
        return -1;  
    }  
 
    private int FindBackCamera(){  
        int cameraCount = 0;  
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();  
        cameraCount = Camera.getNumberOfCameras(); // get cameras number  
                
        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {  
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo  
            if ( cameraInfo.facing ==Camera.CameraInfo.CAMERA_FACING_BACK ) {   
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置  
               return camIdx;  
            }  
        }  
        return -1;  
    }  
    
	/** A safe way to get an instance of the Camera object. */
	public Camera getCameraInstance(){
		Camera c = null;
		try {
			//c = Camera.open(); // attempt to get a Camera instance
			switch(typeCam){
			case CAMERA_FRONT:
				int CammeraIndex=FindFrontCamera();  
		        if(CammeraIndex==-1){  
		            CammeraIndex=FindBackCamera();  
		        }  
		        c = Camera.open(CammeraIndex);
				break;
			case CAMERA_BACK:
				c = Camera.open(FindBackCamera());
				break;
			} 
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
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
