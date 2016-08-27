package com.example.androidcameraapp;

import com.UserDevice.Sensors.OrientationSensor;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private UserCamera mUserCamera;
	private int nCamType;
	private int widthCameraPreview;
	private int heightCameraPreview;
	
	//���򴫸�����ر���
	private ImageView imgCircleStatic;
	private ImageView imgCircle;
	private int xImgCircle;
	private int yImgCircle;
	private OrientationSensor orientationSensor;
	
	public MainActivity(){
		nCamType = 1;//Ĭ�Ϻ�������ͷ
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle(R.string.activity_name_main);
		
		Window window = getWindow();//�õ�����
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//û�б���
		//window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//���ø���

		//��ȡ��Ļ��Ⱥ͸߶�
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
				//����Toast��ʾ��ť�������
				Toast.makeText(
						MainActivity.this,
						"��Ƭ����ɹ�\r\n"+mUserCamera.pathPhotos,Toast.LENGTH_SHORT
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
			MainActivity.this.finish();//��תҪ�ر�ǰһ��activity
			return true;
		}
		
		if(id == R.id.action_camselect) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, CamSelectActivity.class);
			startActivity(intent);
			MainActivity.this.finish();//��תҪ�ر�ǰһ��activity
			return true;
		}
		
		if(id == R.id.action_resolutions){
			new AlertDialog.Builder(MainActivity.this)
        	.setTitle("�ֱ���֧��")
        	.setMessage(mUserCamera.mPreview.strResolutions)
        	.setPositiveButton("ȷ��",null)
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
		
		//��ʼ��Բͼ�ε�λ��
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
			nCamType = getIntent().getExtras().getInt("camType");
		}
		catch(Exception e){
		}
		
		if(nCamType == 0){
			mUserCamera = new UserCamera(this,UserCamera.CameraType.CAMERA_FRONT);	
		}
		if(nCamType == 1){
			mUserCamera = new UserCamera(this,UserCamera.CameraType.CAMERA_BACK);	
		}

		mUserCamera.SetPreviewSize(widthCameraPreview, heightCameraPreview);
		//set it as the content of our activity.
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mUserCamera.mPreview);
	}
}
