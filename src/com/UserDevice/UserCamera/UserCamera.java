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

	private static final String LOG_TAG = "GaoHCLog-->UserCamera";
	
	private  CameraType typeCam;
	
	private int CamIdFront=-1;
	private int CamIdBack;
	
	public Camera mCamera=null;
	public CameraPreview mPreview;
	
	public String pathPhotos; 	
	private static File  dirDCIM;
	private static final String dirPhotos = "AndroidCameraApp";
	
	public UserCamera(Context context,CameraType nCamType){
		
		dirDCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		pathPhotos = dirDCIM.toString() + "/" + dirPhotos;
		
		typeCam = nCamType;
        
		FindCamera();
		
		mCamera = getCameraInstance();

		if(mCamera != null){
			Log.i(LOG_TAG, "UserCamera: mCamera != null");
			mPreview = new CameraPreview(context, mCamera, typeCam);
		}
		else{
			Log.e(LOG_TAG, "UserCamera: mCamera == null");
		}
	}
 
	private int FindCamera() {
		try {
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			int cameraCount = Camera.getNumberOfCameras();
			Log.i(LOG_TAG, "FindCamera: cameraCount == " + String.valueOf(cameraCount));

			for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
				Camera.getCameraInfo(camIdx, cameraInfo);
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
					CamIdFront = camIdx;
				}
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
					CamIdBack = camIdx;
				}
			}
			Log.i(LOG_TAG, "FindCamera: CamIdFront == " + String.valueOf(CamIdFront));
			Log.i(LOG_TAG, "FindCamera: CamIdBack  == " + String.valueOf(CamIdBack));
		} catch (Exception e) {
			Log.e(LOG_TAG, "FindCamera: exception");
		}
		return -1;
	}  
    
	/** A safe way to get an instance of the Camera object. */
	public Camera getCameraInstance(){
		Camera c = null;
		try {
			switch(typeCam){
			case CAMERA_FRONT:
				Log.i(LOG_TAG, "getCameraInstance: typeCam == CAMERA_FRONT");
		        if(CamIdFront==-1){  
		        	c = Camera.open(CamIdBack);
		        }  
		        c = Camera.open(CamIdFront);
				break;
			case CAMERA_BACK:
				Log.i(LOG_TAG, "getCameraInstance: typeCam == CAMERA_BACK");
				c = Camera.open(CamIdBack);
				break;
			} 
		}
		catch (Exception e){
			Log.e(LOG_TAG, "getCameraInstance: exception");
		}
		return c;
	}
	
	public void SetPreviewSize(int width,int height){
		mPreview.getHolder().setFixedSize(width, height);
	}
	
	public void CaptureCamera(){
		mCamera.takePicture(shutterCallback, rawCallback,null, pictureCallback);
	}

	// Called when shutter is opened
	ShutterCallback shutterCallback = new ShutterCallback() { 
		public void onShutter() {
			Log.d(LOG_TAG, "onShutter'd");
		}
	};

	// Handles data for raw picture
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(LOG_TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback pictureCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File filePicture = getOutputMediaFile();
			if (filePicture == null) {
				//Toast.makeText(getActivity(), "Image retrieval failed.", Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				FileOutputStream fos = new FileOutputStream(filePicture);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
			}
			Log.d(LOG_TAG, "onPictureTaken - jpeg");
			camera.startPreview();
		}
	};

	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile() {
		File mediaStorageDir = new File(dirDCIM,dirPhotos);
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e(LOG_TAG, "getOutputMediaFile: failed to create directory");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}
}
