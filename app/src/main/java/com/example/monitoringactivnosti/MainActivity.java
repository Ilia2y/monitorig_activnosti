package com.example.monitoringactivnosti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton podtagivaniya_button, otgimaniya_button, press_button, planka_button, prisedaniya_button, chagomer_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    protected void onResume() {
        super.onResume();
        podtagivaniya_button =  findViewById(R.id.podtagivaniya_button);
    
        podtagivaniya_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent podtagivaniya = new Intent(getApplicationContext(), podtagivaniya.class);
                podtagivaniya.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(podtagivaniya);
            }
        });
    
        otgimaniya_button = findViewById(R.id.otgimaniya_button);
    
        otgimaniya_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inf_otgimaniya = new Intent(getApplicationContext(), inf_otgimaniya.class);
                inf_otgimaniya.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inf_otgimaniya);
            }
        });
    
        press_button = findViewById(R.id.press_button);
    
        press_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent press = new Intent(getApplicationContext(), press.class);
                press.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(press);
            }
        });
    
        planka_button = findViewById(R.id.planka_button);
    
        planka_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent planka = new Intent(getApplicationContext(), planka.class);
                planka.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(planka);
            }
        });
    
        prisedaniya_button = findViewById(R.id.prisedaniya_button);
    
        prisedaniya_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prisedaniya = new Intent(getApplicationContext(), prisedaniya.class);
                prisedaniya.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(prisedaniya);
            }
        });
    
        chagomer_button = findViewById(R.id.chagomer_button);
    
        chagomer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chagomer = new Intent(getApplicationContext(), chagomer.class);
                chagomer.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(chagomer);
            }
        });
    }
}