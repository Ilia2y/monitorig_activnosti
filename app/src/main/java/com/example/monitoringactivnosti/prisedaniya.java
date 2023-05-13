package com.example.monitoringactivnosti;

import static java.lang.Math.atan2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class prisedaniya extends Activity implements SensorEventListener {
	private int timem, times;
	private int  t_beep_sound, c_beep_sound, pretimes;
	private float  pitch, kalibrovka = 1.4F;
	private Boolean timer_started = false, verh = true, beep = true;
	Timer timer = new Timer();
	TimerTask timertask;
	private SensorManager senSensorManager;
	private Sensor senRotationVector;
	int prisedaniya_total_score = 0;
	SoundPool mSoundPool;
	AssetManager assets;
	private double last = 0 ;
	public static float prisedaniya_kalorii = 0;

	
	@SuppressLint("MissingInflatedId")
	@Override
	protected void onCreate(Bundle savedInstanseStatte) {
		super.onCreate(savedInstanseStatte);
		setContentView(R.layout.prisedaniya);
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		assets = getAssets();
		t_beep_sound = obh_func.loadSound(obh_func.timer_sound_file_name, assets, mSoundPool);
		c_beep_sound = obh_func.loadSound(obh_func.chetchik_sound_file_name, assets, mSoundPool);
		obh_func.obr_check_box(findViewById(R.id.prisedaniya_timer_checkBox), findViewById(R.id.prisedaniya_timer_textView), findViewById(R.id.prisedaniya_timer_up), findViewById(R.id.prisedaniya_timer_down));
	}
	
	public void prisedaniya_on_up_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.prisedaniya_timer_textView);
			String buff = obh_func.update_timer(5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void prisedaniya_on_down_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.prisedaniya_timer_textView);
			String buff = obh_func.update_timer(-5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void prisedaniya_on_timer_start(View view) {
		Button up_button = findViewById(R.id.prisedaniya_timer_up);
		Button down_button = findViewById(R.id.prisedaniya_timer_down);
		CheckBox timer_chek_box = findViewById(R.id.prisedaniya_timer_checkBox);
		Button btn = (Button) findViewById(R.id.prisedaniya_timer_start_button);
		TextView kalorii_text = findViewById(R.id.prisedaniya_kalorii_textView);
		kalorii_text.setVisibility(View.VISIBLE);
		if (!timer_started) {
			prisedaniya_total_score = 0;

			MainActivity.total_kalori += prisedaniya_kalorii;
			prisedaniya_kalorii = 0;

			TextView total_score_text_view = findViewById(R.id.prisedaniya_total_score);
//			total_score_text_view.setText(String.valueOf(prisedaniya_total_score));
			senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			senRotationVector = senSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
			senSensorManager.registerListener(this, senRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
			beep = true;
			TextView prisedaniya_timer_textView = findViewById(R.id.prisedaniya_timer_textView);
			
			pretimes = obh_func.on_timer_start(up_button, down_button, timer_chek_box, btn, total_score_text_view);
			
			if(timer_chek_box.isChecked()) {
				timer.scheduleAtFixedRate(timertask = new TimerTask() {
					
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String[] buffer = obh_func.timer_update(prisedaniya_timer_textView, beep, mSoundPool, t_beep_sound, times, timem, btn, up_button, down_button, timer_chek_box, timer_started, pretimes, timertask).split(",");
								beep = Boolean.valueOf(buffer[0]);
								timer_started = Boolean.valueOf(buffer[1]);
								pretimes = Integer.parseInt(buffer[2]);
								timem = Integer.parseInt(buffer[3]);
								times = Integer.parseInt(buffer[4]);
							}
						});
					}
				}, 0, 1000);
			}else{
				timer_started = true;
			}
		} else {
			senSensorManager.unregisterListener(this);
			
			timer_started = obh_func.on_timer_not_started(timer_chek_box, btn, up_button, down_button, timertask, timer_started);
		}
	}
	
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		TextView kalorii_text = findViewById(R.id.prisedaniya_kalorii_textView);
		if (!timer_started) {
			if(pretimes <= 0) {
				senSensorManager.unregisterListener(this);
			}
		} else {
			Sensor mySensor = sensorEvent.sensor;
			if (mySensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
				float w = sensorEvent.values[0];
				float x = sensorEvent.values[1];
				float y = sensorEvent.values[2];
				float z = sensorEvent.values[3];
//			double theta = Math.acos(sensorEvent.values[3]) * 2;
//			double sinv = Math.sin(theta / 2);
//
//			xa = sensorEvent.values[0] / sinv;
//			ya = sensorEvent.values[1] / sinv;
//			za = sensorEvent.values[2] / sinv;
				
				double sinp = 2 * (w * y - z * x);
				
				double roll = atan2(2 * (z * w + x * y), 1 - 2 * (y*y + z*z));
				
				if (Math.abs(sinp) >= 1) {
					pitch = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
				} else {
					pitch = (float) Math.asin(sinp);
				}
				pitch = pitch / kalibrovka * 90;
				double angle = pitch * roll;
				
//
//			System.out.println(xa + " " + ya + " " + za);
//				System.out.println(String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(z));
				
				if (Math.abs(pitch) - last < 4 && verh) {
					verh = false;
					prisedaniya_total_score ++;
					prisedaniya_kalorii += MainActivity.massa * 0.0077;
					kalorii_text.setText(Math.round(prisedaniya_kalorii*1000)*1000 + " KKal");
					mSoundPool.play(c_beep_sound, 1, 1, 1, 0, 1);
					TextView total_score_text_view = findViewById(R.id.prisedaniya_total_score);
					total_score_text_view.setText(String.valueOf(prisedaniya_total_score));
				} else {
					if (Math.abs(pitch) - last > 4 && !verh) {
						verh = true;
					}
//				if(Math.abs(pitch) > koff && !verh && prohod){
//					verh = true;
//					prohod = false;
//				}
				}
				last = Math.abs(pitch);
			}
		}
		
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	
	}
	
	public void prisedaniya_back(View view){
		Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
		main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(main_activity);
	}
}
