package com.example.monitoringactivnosti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class press extends Activity implements SensorEventListener {
	public int timem, times;
	private int  t_beep_sound, c_beep_sound, pretimes;
	private Boolean timer_started = false, beep = true;
	Timer timer = new Timer();
	TimerTask timertask;
	private SensorManager senSensorManager;
	private Sensor senProximity;
	int press_total_score = 0;
	private long lastUpdate = 0;
	private float prochloe = 5;
	SoundPool mSoundPool;
	AssetManager assets;
	public static float press_kalorii = 0;
	private int kolvo_za_vrem = 0;

	
	
	@Override
	protected void onCreate(Bundle savedInstanseStatte) {
		super.onCreate(savedInstanseStatte);
		setContentView(R.layout.press);
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		assets = getAssets();
		t_beep_sound = obh_func.loadSound(obh_func.timer_sound_file_name, assets, mSoundPool);
		c_beep_sound = obh_func.loadSound(obh_func.chetchik_sound_file_name, assets, mSoundPool);
		obh_func.obr_check_box(findViewById(R.id.press_timer_checkBox), findViewById(R.id.press_timer_textView), findViewById(R.id.press_timer_up), findViewById(R.id.press_timer_down));
	}
	
	public void press_on_up_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.press_timer_textView);
			String buff = obh_func.update_timer(5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void press_on_down_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.press_timer_textView);
			String buff = obh_func.update_timer(-5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void press_on_timer_start(View view) {
		Button up_button = findViewById(R.id.press_timer_up);
		Button down_button = findViewById(R.id.press_timer_down);
		CheckBox timer_chek_box = findViewById(R.id.press_timer_checkBox);
		Button btn = (Button) findViewById(R.id.press_timer_start_button);
		TextView total_score_text_view = findViewById(R.id.press_total_score);
		TextView kalorii_text = findViewById(R.id.press_kalorii_textView);
		kalorii_text.setVisibility(View.VISIBLE);
		
		if (!timer_started) {
			press_total_score = 0;

			MainActivity.total_kalori += press_kalorii;
			press_kalorii = 0;

			lastUpdate = System.currentTimeMillis();
			senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			senProximity = senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
			senSensorManager.registerListener(this, senProximity, SensorManager.SENSOR_DELAY_NORMAL);
			beep = true;
			TextView press_timer_textView = findViewById(R.id.press_timer_textView);
			
			pretimes = obh_func.on_timer_start(up_button, down_button, timer_chek_box, btn, total_score_text_view);
			
			if(timer_chek_box.isChecked()) {
				timer.scheduleAtFixedRate(timertask = new TimerTask() {
					
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String[] buffer = obh_func.timer_update(press_timer_textView, beep, mSoundPool, t_beep_sound, times, timem, btn, up_button, down_button, timer_chek_box, timer_started, pretimes, timertask).split(",");
								beep = Boolean.valueOf(buffer[0]);
								kolvo_za_vrem ++;
								timer_started = Boolean.valueOf(buffer[1]);
								pretimes = Integer.parseInt(buffer[2]);
								timem = Integer.parseInt(buffer[3]);
								times = Integer.parseInt(buffer[4]);
							}
						});
					}
				}, 0, 1000);
			}else{
				press_timer_textView.setVisibility(View.VISIBLE);
				btn.setVisibility(View.INVISIBLE);
				timer.scheduleAtFixedRate(timertask = new TimerTask() {

					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String[] buffer = obh_func.pred_otchet(press_timer_textView, mSoundPool, t_beep_sound, timer_started, pretimes, timertask).split(",");
								timer_started = Boolean.valueOf(buffer[0]);
								pretimes = Integer.parseInt(buffer[1]);
								if(timer_started){
									btn.setVisibility(View.VISIBLE);
								}
							}
						});
					}
				}, 0, 1000);
			}
		} else {
			senSensorManager.unregisterListener(this);
			
			timer_started = obh_func.on_timer_not_started(timer_chek_box, btn, up_button, down_button, timertask, timer_started);
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		TextView kalorii_text = findViewById(R.id.press_kalorii_textView);
		if (timer_started){
			Sensor mySensor = sensorEvent.sensor;
			if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
				float x = sensorEvent.values[0];
				
				if (prochloe != x & x == 5){
					press_total_score ++;
					if(kolvo_za_vrem < 1){
						press_kalorii += MainActivity.massa * 0.014666;
					}else {
						press_kalorii += MainActivity.massa * 0.007333;
					}
					kolvo_za_vrem = 0;
					kalorii_text.setText(Math.round(press_kalorii*1000)/1000 + " KKal");
					mSoundPool.play(c_beep_sound, 1, 1, 1, 0, 1);
					TextView total_score_text_view = findViewById(R.id.press_total_score);
					total_score_text_view.setText(String.valueOf(press_total_score));
				}
				prochloe = x;
			}
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	
	}
	
	public void press_back(View view){
		MainActivity.total_kalori += press_kalorii;
		Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
		main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(main_activity);
	}
}
