package com.UserDevice.Sensors;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class OrientationSensor implements SensorEventListener{

	private ImageView imgView;
	private int xImgCircle;
	private int yImgCircle;
	private GradientDrawable backgroundGradientCircle;
	private SensorManager sensorManager;
	private Sensor orientationSensor;
	
	public OrientationSensor(
			Context context, 
			ImageView view,
			int xImgCircleInit,
			int yImgCircleInit) {
		
		this.imgView = view;
		xImgCircle = xImgCircleInit;
		yImgCircle = yImgCircleInit;
		backgroundGradientCircle = (GradientDrawable)view.getBackground();
		
		//get a hook to the sensor service  
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);	
		if (orientationSensor == null) {
			Toast.makeText(context, "Orientation Sensor not exist!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		//if sensor is unreliable, return void  
		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)  
		{  
			return;  
		}  

		//output the Roll, Pitch and Yawn values  
		float x = event.values[2];//Orientation X (Roll)
		float y = event.values[1];//Orientation Y (Pitch)
		//float z = event.values[0];//Orientation Z (Yaw)

		//tvGyro.setText("X: " + String.valueOf(x) +"\nY: " + String.valueOf(y));  

		imgView.setTranslationX(xImgCircle + (int)(x)*3);
		imgView.setTranslationY(yImgCircle + (int)(y)*3);
		
		if(Math.abs(x)<2 && Math.abs(y)<2){
			backgroundGradientCircle.setColor(Color.GREEN);
		}else{
			backgroundGradientCircle.setColor(Color.RED);
		}
	}
	
	public void RegisterListener(){
		/*register the sensor listener to listen to the gyroscope sensor, use the 
        callbacks defined in this class, and gather the sensor information as quick 
        as possible*/  
		sensorManager.registerListener(this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	public void UnRegisterListener(){
		//unregister the sensor listener  
		sensorManager.unregisterListener(this);
	}
}
