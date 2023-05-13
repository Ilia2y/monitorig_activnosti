package com.example.monitoringactivnosti;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageButton podtagivaniya_button, otgimaniya_button, press_button, planka_button, prisedaniya_button, chagomer_button, berpi_button;
    Button sohranit_button;
    TextView vsego_kalorii_text;

    public static int massa = 60;
    public static float total_kalori = 0;

    public void saveText(){

        FileOutputStream fos = null;
        try {
            EditText massa_editText = findViewById(R.id.massa_editText);
            String buff = String.valueOf(massa_editText.getText());
            fos = openFileOutput("buffer", MODE_PRIVATE);
            fos.write(buff.getBytes());
        }
        catch(IOException ex) {
            Log.e(TAG, "", ex);
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                Log.e(TAG, "", ex);
            }
        }
    }
    // открытие файла
    public void openText(){

        FileInputStream fin = null;
        try {
            fin = openFileInput("buffer");
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);

            massa = Integer.parseInt(text);

        }
        catch(IOException ex) {
            Log.e(TAG, "", ex);
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    protected void onResume() {
        super.onResume();
//        podtagivaniya_button =  findViewById(R.id.podtagivaniya_button);
//
//        podtagivaniya_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent podtagivaniya = new Intent(getApplicationContext(), podtagivaniya.class);
//                podtagivaniya.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(podtagivaniya);
//            }
//        });

        vsego_kalorii_text = findViewById(R.id.vsego_kalorii);
        vsego_kalorii_text.setText(Math.round(total_kalori*1000)*1000 + " KKal");

        sohranit_button = findViewById(R.id.sohranit_button);
        sohranit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        berpi_button = findViewById(R.id.berpi_button);

        berpi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inf_berpi = new Intent(getApplicationContext(), berpi.class);
                inf_berpi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(inf_berpi);
            }
        });
    }
}