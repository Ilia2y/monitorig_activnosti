package com.example.monitoringactivnosti;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

public class inf_otgimaniya extends Activity {
	Button propusk_button, next_button, back_button, finich_button;
	
	@SuppressLint("MissingInflatedId")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inf_otgimaniya);
		
		propusk_button = (Button) findViewById(R.id.inf_otgimaniya_propusk_button);
		
		propusk_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent otgimaniya = new Intent(getApplicationContext(), otgimaniya.class);
				otgimaniya.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(otgimaniya);
			}
		});
		next_button = (Button) findViewById(R.id.inf_otgimaniya_next_button);
		
		next_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ConstraintLayout first_constraintLayout = findViewById(R.id.inf_otgimaniya_next_first_constraintLayout);
				ConstraintLayout second_constraintLayout = findViewById(R.id.inf_otgimaniya_next_second_constraintLayout);
				first_constraintLayout.setVisibility(View.INVISIBLE);
				second_constraintLayout.setVisibility(View.VISIBLE);
			}
		});
		
		finich_button = (Button) findViewById(R.id.inf_otgimaniya_finich_button);
		
		finich_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent otgimaniya = new Intent(getApplicationContext(), otgimaniya.class);
				otgimaniya.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(otgimaniya);
			}
		});
		
		
		back_button = (Button) findViewById(R.id.inf_otgimaniya_back_button);
		
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent main_activity = new Intent(getApplicationContext(), MainActivity.class);
				main_activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(main_activity);
			}
		});
	}
}
