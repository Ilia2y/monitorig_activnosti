package com.example.monitoringactivnosti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class otgimaniya extends Activity implements SensorEventListener {
	public int timem, times;
	private int  t_beep_sound, c_beep_sound, pretimes;
	private Boolean timer_started = false, beep = true;
	Timer timer = new Timer();
	TimerTask timertask;
	private SensorManager senSensorManager;
	private Sensor senProximity;
	int otgimaniya_total_score = 0;
	private long lastUpdate = 0;
	private float prochloe = 5;
	SoundPool mSoundPool;
	AssetManager assets;
	public static float otgimaniya_kalorii = 0;
	private int kolvo_za_vrem = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanseStatte) {
		super.onCreate(savedInstanseStatte);
		setContentView(R.layout.otgimaniya);
		mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		assets = getAssets();
		t_beep_sound = obh_func.loadSound(obh_func.timer_sound_file_name, assets, mSoundPool);
		c_beep_sound = obh_func.loadSound(obh_func.chetchik_sound_file_name, assets, mSoundPool);
		obh_func.obr_check_box(findViewById(R.id.otgimaniya_timer_checkBox), findViewById(R.id.otgimaniya_timer_textView), findViewById(R.id.otgimaniya_timer_up), findViewById(R.id.otgimaniya_timer_down));
		Switch otgimaniya_datchik_switch = findViewById(R.id.otgimaniya_datchik_switch);
		TextView otgimaniya_total_score_text = findViewById(R.id.otgimaniya_total_score);
		TextView otgimaniya_kalorii_text = findViewById(R.id.otgimaniya_kalorii_textView);
		Button otgimaniya_nouse_button = findViewById(R.id.otgimaniya_nouse_button);

		otgimaniya_datchik_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					otgimaniya_datchik_switch.setText(otgimaniya_datchik_switch.getTextOn());
					otgimaniya_total_score_text.setVisibility(View.INVISIBLE);
					otgimaniya_nouse_button.setVisibility(View.VISIBLE);

				}
				else {
					otgimaniya_datchik_switch.setText(otgimaniya_datchik_switch.getTextOff());
					otgimaniya_total_score_text.setVisibility(View.VISIBLE);
					otgimaniya_nouse_button.setVisibility(View.INVISIBLE);

				}
			}
		});
		otgimaniya_nouse_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				otgimaniya_total_score ++;
				if(kolvo_za_vrem > 0){
					otgimaniya_kalorii += MainActivity.massa * 0.022;
				}else {
					otgimaniya_kalorii += MainActivity.massa * 0.011;
				}
				kolvo_za_vrem = 0;
				otgimaniya_kalorii_text.setText(Math.round(otgimaniya_kalorii*1000)/1000 + " KKal");
				otgimaniya_nouse_button.setText(String.valueOf(otgimaniya_total_score));
				mSoundPool.play(c_beep_sound, 1, 1, 1, 0, 1);
			}
		});
		otgimaniya_nouse_button.setEnabled(false);
	}
	
	public void otgimaniya_on_up_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.otgimaniya_timer_textView);
			String buff = obh_func.update_timer(5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void otgimaniya_on_down_button_click(View view) {
		if (!timer_started) {
			TextView timer_textView = findViewById(R.id.otgimaniya_timer_textView);
			String buff = obh_func.update_timer(-5, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
		}
	}
	
	public void otgimaniya_on_timer_start(View view) {
		Button up_button = findViewById(R.id.otgimaniya_timer_up);
		Button down_button = findViewById(R.id.otgimaniya_timer_down);
		CheckBox timer_chek_box = findViewById(R.id.otgimaniya_timer_checkBox);
		Button btn = (Button) findViewById(R.id.otgimaniya_timer_start_button);
		TextView total_score_text_view = findViewById(R.id.otgimaniya_total_score);
		Switch otgimaniya_datchik_switch = findViewById(R.id.otgimaniya_datchik_switch);
		Button otgimaniya_nouse_button = findViewById(R.id.otgimaniya_nouse_button);
		TextView kalorii_text = findViewById(R.id.otgimaniya_kalorii_textView);
		kalorii_text.setVisibility(View.VISIBLE);
		
		if (!timer_started) {
			otgimaniya_total_score = 0;

			MainActivity.total_kalori += otgimaniya_kalorii;
			otgimaniya_kalorii = 0;

			if(!otgimaniya_datchik_switch.isChecked()) {
				lastUpdate = System.currentTimeMillis();
				senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
				senProximity = senSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
				senSensorManager.registerListener(this, senProximity, SensorManager.SENSOR_DELAY_NORMAL);
			}else{
				otgimaniya_nouse_button.setText("0");
			}
			beep = true;
			TextView otgimaniya_timer_textView = findViewById(R.id.otgimaniya_timer_textView);
			
			pretimes = obh_func.on_timer_start(up_button, down_button, timer_chek_box, btn, total_score_text_view);
			otgimaniya_datchik_switch.setVisibility(View.INVISIBLE);
			
			if(timer_chek_box.isChecked()) {
				timer.scheduleAtFixedRate(timertask = new TimerTask() {
					
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String[] buffer = obh_func.timer_update(otgimaniya_timer_textView, beep, mSoundPool, t_beep_sound, times, timem, btn, up_button, down_button, timer_chek_box, timer_started, pretimes, timertask).split(",");
								beep = Boolean.valueOf(buffer[0]);
								kolvo_za_vrem ++;
								timer_started = Boolean.valueOf(buffer[1]);
								pretimes = Integer.parseInt(buffer[2]);
								timem = Integer.parseInt(buffer[3]);
								times = Integer.parseInt(buffer[4]);
								if(pretimes <= 0){
									otgimaniya_nouse_button.setEnabled(true);
								}
								if (times <= 0 & timem <= 0) {
									otgimaniya_nouse_button.setEnabled(false);
									otgimaniya_datchik_switch.setVisibility(View.VISIBLE);
								}
							}
						});
					}
				}, 0, 1000);
			}else{
				otgimaniya_timer_textView.setVisibility(View.VISIBLE);
				btn.setVisibility(View.INVISIBLE);
				timer.scheduleAtFixedRate(timertask = new TimerTask() {

					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String[] buffer = obh_func.pred_otchet(otgimaniya_timer_textView, mSoundPool, t_beep_sound, timer_started, pretimes, timertask).split(",");
								timer_started = Boolean.valueOf(buffer[0]);
								pretimes = Integer.parseInt(buffer[1]);
								if(timer_started){
									btn.setVisibility(View.VISIBLE);
									otgimaniya_nouse_button.setEnabled(true);
//									otgimaniya_datchik_switch.setVisibility(View.INVISIBLE);
								}
							}
						});
					}
				}, 0, 1000);
			}
		} else {
			if(!otgimaniya_datchik_switch.isChecked()) {
				senSensorManager.unregisterListener(this);
			}else{
				otgimaniya_nouse_button.setEnabled(false);
			}
			
			timer_started = obh_func.on_timer_not_started(timer_chek_box, btn, up_button, down_button, timertask, timer_started);
			otgimaniya_datchik_switch.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		TextView kalorii_text = findViewById(R.id.otgimaniya_kalorii_textView);
		if (timer_started){
			Sensor mySensor = sensorEvent.sensor;
			if (mySensor.getType() == Sensor.TYPE_PROXIMITY) {
				float x = sensorEvent.values[0];
				System.out.println(x);MainActivity.total_kalori += otgimaniya_kalorii;
				
				if (prochloe != x & x == 5){
					otgimaniya_total_score ++;
					if(kolvo_za_vrem < 1){
						otgimaniya_kalorii += MainActivity.massa * 0.022;
					}else {
						otgimaniya_kalorii += MainActivity.massa * 0.011;
					}
					kolvo_za_vrem = 0;
					kalorii_text.setText(Math.round(otgimaniya_kalorii*1000)/1000 + " KKal");
					mSoundPool.play(c_beep_sound, 1, 1, 1, 0, 1);
					TextView total_score_text_view = findViewById(R.id.otgimaniya_total_score);
					total_score_text_view.setText(String.valueOf(otgimaniya_total_score));
				}
				prochloe = x;
			}
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int i) {
	
	}
	
	public void otgimaniya_back(View view){
		MainActivity.total_kalori += otgimaniya_kalorii;
		Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
		main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(main_activity);
	}
}
