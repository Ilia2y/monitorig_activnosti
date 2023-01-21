package com.example.monitoringactivnosti;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class calibrovka_podtagivaniya extends Activity implements SensorEventListener {

    private Boolean calibrating = false;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private double last_len_x = 0, last_len_y = 0, last_len_z = 0, obch_len_x = 0, obch_len_y = 0, obch_len_z = 0, obch_len = 0, len_x = 0, len_y = 0, len_z = 0, vx = 0, vy = 0, vz = 0, last_vx = 0, last_vy = 0, last_vz = 0, last_x = 0, last_y = 0, last_z = 0, last_vg = 0, vg = 0, len_g = 0, last_len_g = 0, nakop_time = 0;
    private static final int SHAKE_THRESHOLD =1000;
    private long lastUpdate = 0;
    private int chetchik = 0;

    TextView chetchik_text;

    @Override
    protected void onCreate(Bundle savedInstanseStatte) {
        super.onCreate(savedInstanseStatte);
        setContentView(R.layout.calibrovaka_podtagivaniya);
        try {
            calibrovka();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void calibrovka() throws InterruptedException {
        Button myButton = findViewById(R.id.start_calibrate_button);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calibrating){
                    calibrating = false;
                }else {
                    calibrating = true;
                    nakop_time = 0;
                }
            }
        });
//        while (calibrate_result == -1){
//            Thread.sleep(100);
//        }
//        return calibrate_result;
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        chetchik_text = findViewById(R.id.chetchik_text);
        if (calibrating) {
            Sensor mySensor = sensorEvent.sensor;
            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                long curTime = System.currentTimeMillis();
                long diffTime = (curTime - lastUpdate);

                lastUpdate = curTime;
                
                if(diffTime > 200){
                    System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
                }
                if (diffTime < 1000000000) {
                    nakop_time += diffTime;
                    System.out.println(diffTime);
                    String buffer = x + " " + y + " " + z + " " + nakop_time;
                    save_calibrate_file(buffer);

//                    vx = last_x * diffTime / 1000;
//                    vy = last_y * diffTime / 1000;
//                    vz = last_z * diffTime / 1000;
//                    vg = 9.81 * diffTime / 1000;
//                    len_x = x * diffTime / 1000 * diffTime / 1000 / 2 + last_vx * diffTime / 1000;
//                    len_y = y * diffTime / 1000 * diffTime / 1000 / 2 + last_vy * diffTime / 1000;
//                    len_z = z * diffTime / 1000 * diffTime / 1000 / 2 + last_vz * diffTime / 1000;
//                    len_g = 9.81 * diffTime / 1000 * diffTime / 1000 / 2 + last_vg * diffTime / 1000;
//                    double lenght = Math.sqrt((last_len_x - len_x) * (last_len_x - len_x) + (last_len_y - len_y) * (last_len_y - len_y) + (last_len_z - len_z) * (last_len_z - len_z)) - (len_g - last_len_g);
//
//                    System.out.println(lenght);
//
//                   if (lenght < 0) {
//                       chetchik += 1;
//	                   System.out.println(chetchik);
////	                   Intent podtagivaniya = new Intent(getApplicationContext(), podtagivaniya.class);
////	                   podtagivaniya.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////	                   startActivity(podtagivaniya);
////                       calibrate_lenght = Math.abs(square(obch_len_x) + square(obch_len_y) + square(obch_len_z));
////                       driganiya = obch_len - calibrate_lenght;
////                       calibrating = false;
////                       //save_calibrate_file();
//                  }
//
//                   obch_len += lenght;
//                   obch_len_x += len_x;
//                   obch_len_y += len_y;
//                   obch_len_z += len_z;
//                   String buffer = x + " " + y + " " + z + " " + diffTime + " " + len_x + " " + len_y + " " + len_z + " " + len_g + " " + lenght + " " + obch_len_x + " " + obch_len_y + " " + obch_len_z + " " + obch_len + " " + last_len_x + " " + last_len_y + " " + last_len_z + " " + last_len_g + " " + last_x + " " + last_y + " " + last_z + " " + last_vx + " " + last_vy + " " + last_vz + " " + last_vg + " " + vx + " " + vy + " " + vz + " " + vg;
//                   save_calibrate_file(buffer);
//                   last_len_x = len_x;
//                   last_len_y = len_y;
//                   last_len_z = len_z;
//                   last_len_g = len_g;
                }
//                last_x = x;
//                last_y = y;
//                last_z = z;
//                last_vx = vx;
//                last_vy = vy;
//                last_vz = vz;
//                last_vg = vg;
            }
        }
        chetchik_text.setText(String.valueOf(chetchik));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private Boolean save_calibrate_file(String text){
        //String text = calibrate_lenght + " " + driganiya;
        FileOutputStream fos = null;
        Boolean uspeh = false;
        text = calibrate_file_text() + "\n" + text;
        try {
            fos = openFileOutput("calibrate_podtagivaniya_data.txt", MODE_PRIVATE);
            fos.write(text.getBytes());
            uspeh = true;
        }
        catch(IOException ex) {
            uspeh = false;
        }
        finally{
            try{
                if(fos!=null)
                    fos.close();
            }
            catch(IOException ex){
                uspeh = false;
            }
        }
        return uspeh;
    }

    private String calibrate_file_text(){
        FileInputStream fin = null;
        String text;
        try {
            fin = openFileInput("calibrate_podtagivaniya_data.txt");
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            text = new String (bytes);
        }
        catch(IOException ex) {
            text = "-1 -1";
        }
        finally{
            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){
                text = "-1 -1";
            }
        }
        return text;
    }

}