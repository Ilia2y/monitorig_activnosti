package com.example.monitoringactivnosti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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

import java.util.Timer;
import java.util.TimerTask;

public class chagomer extends Activity implements SensorEventListener {
	private int timem, times, pretimes, t_beep_sound, c_beep_sound;
	private float  pitch, kalibrovka = 1.4F;
	private Boolean timer_started = false, verh = true, beep = true;
	private SensorManager senSensorManager;
	private Sensor senRotationVector;
	int chagomer_total_score = 0;
	SoundPool mSoundPool;
	AssetManager assets;
	Timer timer = new Timer();
	static TimerTask timertask;
	
	@Override
	protected void onCreate(Bundle savedInstanseStatte) {
		super.onCreate(savedInstanseStatte);
		setContentView(R.layout.chagomer);
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		assets = getAssets();
		t_beep_sound = obh_func.loadSound(obh_func.timer_sound_file_name, assets, mSoundPool);
		c_beep_sound = obh_func.loadSound(obh_func.chetchik_sound_file_name, assets, mSoundPool);
		obh_func.obr_check_box(findViewById(R.id.chagomer_timer_checkBox), findViewById(R.id.chagomer_timer_textView), findViewById(R.id.chagomer_timer_up), findViewById(R.id.chagomer_timer_down));
	}
	
	public void chagomer_on_up_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.chagomer_timer_textView);
			String buff = obh_func.update_timer(5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void chagomer_on_down_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.chagomer_timer_textView);
			String buff = obh_func.update_timer(-5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void chagomer_on_timer_start(View view) {
		Button up_button = findViewById(R.id.chagomer_timer_up);
		Button down_button = findViewById(R.id.chagomer_timer_down);
		CheckBox timer_chek_box = findViewById(R.id.chagomer_timer_checkBox);
		Button btn = findViewById(R.id.chagomer_timer_start_button);
		TextView total_score_text_view = findViewById(R.id.chagomer_total_score);
		
		if (!timer_started) {
			chagomer_total_score = 0;
			senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			senRotationVector = senSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
			senSensorManager.registerListener(this, senRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
			beep = true;
			TextView chagomer_timer_textView = findViewById(R.id.chagomer_timer_textView);
			
			pretimes = obh_func.on_timer_start(up_button, down_button, timer_chek_box, btn, total_score_text_view);
			
			if(timer_chek_box.isChecked()) {
				
				timer.scheduleAtFixedRate(timertask = new TimerTask() {
					
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String[] buffer = obh_func.timer_update(chagomer_timer_textView, beep, mSoundPool, t_beep_sound, times, timem, btn, up_button, down_button, timer_chek_box, timer_started, pretimes, timertask).split(",");
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
		if(!beep) {
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
//				double theta = Math.acos(sensorEvent.values[3]) * 2;
//				double sinv = Math.sin(theta / 2);
//
//				xa = sensorEvent.values[0] / sinv;
//				ya = sensorEvent.values[1] / sinv;
//				za = sensorEvent.values[2] / sinv;
					
					double sinp = 2 * (w * y - z * x);
					if (Math.abs(sinp) >= 1) {
						pitch = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
					} else {
						pitch = (float) Math.asin(sinp);
					}
					pitch = pitch / kalibrovka * 90;
//				System.out.println(pitch);
//
//				System.out.println(xa + " " + ya + " " + za);
//					System.out.println(String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(z));
					
					if (Math.abs(pitch) < 20 && verh) {
						verh = false;
						chagomer_total_score += 1;
						mSoundPool.play(c_beep_sound, 1, 1, 1, 0, 1);
						TextView total_score_text_view = findViewById(R.id.chagomer_total_score);
						total_score_text_view.setText(String.valueOf(chagomer_total_score));
						System.out.println(chagomer_total_score);
					} else {
						if (Math.abs(pitch) > 23 && !verh) {
							verh = true;
						}
//					if(Math.abs(pitch) > koff && !verh && prohod){
//						verh = true;
//						prohod = false;
//					}
					}
				}
			}
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	
	}
	
	public void chagomer_back(View view){
		Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
		main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(main_activity);
	}
}
