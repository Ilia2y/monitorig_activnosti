package com.example.monitoringactivnosti;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.util.List;

public class obh_func {
	public static String timer_sound_file_name = "timer.mp3", chetchik_sound_file_name = "chetchik.mp3";
	
	protected static int loadSound(String fileName, AssetManager assets, SoundPool mSoundPool) {
		int result = 0;
		AssetFileDescriptor afd = null;
		try {
			afd = assets.openFd(fileName);
		} catch (IOException e) {
			e.printStackTrace();
			result = -1;
		}
		if (result == 0) {
			result = mSoundPool.load(afd, 1);
		}
		return result;
	}
	
	public static String update_timer(int i, int timem, int times) {
		
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
	
	public static void obr_check_box(CheckBox checkBox, TextView text, Button up_button, Button down_button){
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked) {
					up_button.setVisibility(View.VISIBLE);
					down_button.setVisibility(View.VISIBLE);
					text.setVisibility(View.VISIBLE);
				}
				else {
					up_button.setVisibility(View.INVISIBLE);
					down_button.setVisibility(View.INVISIBLE);
					text.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	public static Integer on_timer_start(Button up_button, Button down_button, CheckBox timer_chek_box, Button btn, TextView total_score_text_view) {
		Integer pretimes = 5;
		btn.setBackgroundColor(Color.parseColor("#2E23C5"));
		
		total_score_text_view.setText("0");
		
		up_button.setVisibility(View.INVISIBLE);
		down_button.setVisibility(View.INVISIBLE);
		timer_chek_box.setVisibility(View.INVISIBLE);
		
		btn.setText("Стоп");
		return pretimes;
	}
	
	public static Boolean on_timer_not_started(CheckBox timer_chek_box, Button btn, Button up_button, Button down_button, TimerTask timertask, boolean timer_started) {
		if (timer_chek_box.isChecked()) {
			up_button.setVisibility(View.VISIBLE);
			down_button.setVisibility(View.VISIBLE);
			timertask.cancel();
		}
		timer_started = false;
		btn.setBackgroundColor(Color.parseColor("#FF0000"));
		btn.setText("Начать");
		timer_chek_box.setVisibility(View.VISIBLE);
		return timer_started;
	}
	
	public static String timer_update(TextView timer_textView, boolean beep, SoundPool mSoundPool, int beep_sound, int times, int timem, Button btn, Button up_button, Button down_button, CheckBox timer_chek_box, boolean timer_started, int pretimes, TimerTask timertask){
		
		if(pretimes <= 0) {
			btn.setVisibility(View.VISIBLE);
			if(beep){
				mSoundPool.play(beep_sound, 1, 1, 1, 0, 1);
				beep = false;
			}
			String buff = obh_func.update_timer(-1, timem, times);
			String[] subStr;
			timer_textView.setText(buff);
			subStr = buff.split(":");
			timem = Integer.parseInt(subStr[0]);
			times = Integer.parseInt(subStr[1]);
			if (times <= 0 & timem <= 0) {
				timertask.cancel();
				timer_textView.setText("Time Over");
				mSoundPool.play(beep_sound, 1, 1, 1, 0, 1);
				timer_started = false;
			//	Button btn = (Button) findViewById(R.id.prisedaniya_timer_start_button);
				btn.setBackgroundColor(Color.parseColor("#FF0000"));
				btn.setText("Начать");
				up_button.setVisibility(View.VISIBLE);
				down_button.setVisibility(View.VISIBLE);
				timer_chek_box.setVisibility(View.VISIBLE);
			}
		}else{
			btn.setVisibility(View.INVISIBLE);
			timer_textView.setText(Integer.toString(pretimes));
			pretimes--;
			if(pretimes < 1) {
				timer_started = true;
			}
		}
		String buffer = beep + "," + timer_started + "," + pretimes + "," + timem + "," + times;
		return buffer;
	}

	public static String pred_otchet(TextView timer_textView, SoundPool mSoundPool, int beep_sound, boolean timer_started, int pretimes, TimerTask timertask){
		if(pretimes >= 0) {
			timer_textView.setText(Integer.toString(pretimes));
			pretimes--;
		}else{
			mSoundPool.play(beep_sound, 1, 1, 1, 0, 1);
			timertask.cancel();
			timer_textView.setVisibility(View.INVISIBLE);
			timer_started = true;
		}
		String buffer = timer_started + "," + pretimes;
		return buffer;
	}
	
}
