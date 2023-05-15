package com.example.monitoringactivnosti;
		
		import android.app.Activity;
		import android.content.Context;
		import android.content.Intent;
		import android.graphics.Color;
		import android.hardware.Sensor;
		import android.hardware.SensorEvent;
		import android.hardware.SensorEventListener;
		import android.hardware.SensorManager;
		import android.os.Bundle;
		import android.view.View;
		import android.widget.Button;
		import android.widget.TextView;
		
		import java.io.FileInputStream;
		import java.io.IOException;
		import java.util.Timer;
		import java.util.TimerTask;

public class planka extends Activity implements SensorEventListener {
	public int timem, times;
	private Boolean timer_started = false;
	Timer timer = new Timer();
	TimerTask timertask;
	private SensorManager senSensorManager;
	private Sensor senProximity;
	int planka_total_score = 0;
	private long lastUpdate = 0;
	private float prochloe = 5;
	public static float planka_kalorii = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanseStatte) {
		super.onCreate(savedInstanseStatte);
		setContentView(R.layout.planka);
	}
	
	public String update_timer(int i) {
		if (!(times == 0 & timem == 0 & i < 0)) {
			times += i;
		}
		if (times <= 0 & timem > 0) {
			times += 60;
			timem -= 1;
		}
		if (times >= 60) {
			times -= 60;
			timem += 1;
		}
		String a, s, m;
		if (times < 10) {
			s = "0" + Integer.toString(times);
		} else {
			s = Integer.toString(times);
		}
		if (timem < 10) {
			m = "0" + Integer.toString(timem);
		} else {
			m = Integer.toString(timem);
		}
		a = m + ":" + s;
		return a;
	}

//	private String calibrate_file_text(){
//		FileInputStream fin = null;
//		String text;
//		try {
//			fin = openFileInput("calibrate_podtagivaniya_data.txt");
//			byte[] bytes = new byte[fin.available()];
//			fin.read(bytes);
//			text = new String (bytes);
//		}
//		catch(IOException ex) {
//			text = "-1 -1";
//		}
//		finally{
//			try{
//				if(fin!=null)
//					fin.close();
//			}
//			catch(IOException ex){
//				text = "-1 -1";
//			}
//		}
//		return text;
//	}
	
	public void planka_on_up_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.planka_timer_textView);
			timer_textView.setText(update_timer(5));
		}
	}
	
	public void planka_on_down_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.planka_timer_textView);
			timer_textView.setText(update_timer(-5));
		}
	}
	
	public void planka_on_timer_start(View view) {
		if (!timer_started) {
			planka_total_score = 0;

			MainActivity.total_kalori += planka_kalorii;
			planka_kalorii = 0;

			lastUpdate = System.currentTimeMillis();
			senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			senProximity = senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
			senSensorManager.registerListener(this, senProximity, SensorManager.SENSOR_DELAY_NORMAL);
			timer_started = true;
			timem = 0;
			times = 0;
			TextView kalorii_text = findViewById(R.id.planka_kalorii_textView);
			kalorii_text.setVisibility(View.VISIBLE);
			
			Button btn = (Button) findViewById(R.id.planka_timer_start_button);
			btn.setBackgroundColor(Color.parseColor("#2E23C5"));
			
			btn.setText("Стоп");
			TextView otgimaniya_timer_textView = findViewById(R.id.planka_timer_textView);
			timer.scheduleAtFixedRate(timertask = new TimerTask() {
				
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							otgimaniya_timer_textView.setText(update_timer(1));
							planka_kalorii += MainActivity.massa * 0.0009777777777777783;
							kalorii_text.setText(Math.round(planka_kalorii*1000)/1000 + " KKal");
							if (times <= 0 & timem <= 0) {
								timertask.cancel();
								otgimaniya_timer_textView.setText("Time Over");
								timer_started = false;
								Button btn = (Button) findViewById(R.id.planka_timer_start_button);
								btn.setBackgroundColor(Color.parseColor("#FF0000"));
								btn.setText("Начать");
							}
						}
					});
				}
			}, 0, 1000);
		} else {
			senSensorManager.unregisterListener(this);
			timer_started = false;
			timertask.cancel();
			Button btn = (Button) findViewById(R.id.planka_timer_start_button);
			btn.setBackgroundColor(Color.parseColor("#FF0000"));
			btn.setText("Начать");
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		if (!timer_started) {
			senSensorManager.unregisterListener(this);
		} else {
			Sensor mySensor = sensorEvent.sensor;
			if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
				float x = sensorEvent.values[0];
				
				if (x == 0){
					senSensorManager.unregisterListener(this);
					timer_started = false;
					timertask.cancel();
					Button btn = (Button) findViewById(R.id.planka_timer_start_button);
					btn.setBackgroundColor(Color.parseColor("#FF0000"));
					btn.setText("Начать");
				}
			}
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	
	}
	
	public void planka_back(View view){
		MainActivity.total_kalori += planka_kalorii;
		Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
		main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(main_activity);
	}
}
