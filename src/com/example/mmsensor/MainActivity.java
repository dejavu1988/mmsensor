package com.example.mmsensor;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private Button button;
	private LinearLayout linearLayout;
	private TextView show;
	private SensorManager sm;
	private List<TextView> sensorNameViews, sensorValueViews;
	private List<Sensor> allSensors;
	private boolean clickFlag = false;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button = (Button) findViewById(R.id.button);
		linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
		show = (TextView) findViewById(R.id.show);
		button.setOnClickListener(new ButtonListener());
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		allSensors = sm.getSensorList(Sensor.TYPE_ALL);
		sensorNameViews = new ArrayList<TextView>();
		sensorValueViews = new ArrayList<TextView>();
		
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		if(clickFlag) {
			for (Sensor s: allSensors) {
				sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
	}



	@Override
	protected void onPause() {
		
		if(clickFlag) sm.unregisterListener(this);
		super.onPause();
	}

	
	class ButtonListener implements OnClickListener {
		  public void onClick(View v) {
			  clickFlag = true;
			  linearLayout.removeViews(0, linearLayout.getChildCount());
			  show.setText("There are " + allSensors.size() + " sensors:");

			  for (Sensor s: allSensors) {
				
				  sm.registerListener(MainActivity.this, s, SensorManager.SENSOR_DELAY_NORMAL);
				  
				  TextView tv = new TextView(MainActivity.this);
				  TextView tp = new TextView(MainActivity.this);				  
				  
				  tv.setText(String.valueOf(allSensors.indexOf(s)+1) + ".\tName: " + s.getName() + "\n\tVendor: " + s.getVendor());
				  tp.setText("\t0");				  
				  tv.setTextColor(Color.BLUE);
				  tp.setTextColor(Color.BLUE);
				  
				  sensorNameViews.add(tv);
				  sensorValueViews.add(tp);
				  linearLayout.addView(tv);
				  linearLayout.addView(tp);
	        	
			  }
		  }
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
				
	}



	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		String values = "";
		for(float i: event.values){
			values = values.concat("\t" + String.valueOf(i) + " ");
		}
		sensorValueViews.get(allSensors.indexOf(event.sensor)).setText(values);
	}		
	    
}