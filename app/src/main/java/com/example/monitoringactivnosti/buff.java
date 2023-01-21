//package com.example.monitoringactivnosti;
//
//import java.io.IOException;
//
//import android.app.Activity;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.view.SurfaceView;
//
//import org.opencv.videoio.VideoCapture;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//
//
//import android.app.Activity;
//import android.graphics.Matrix;
//import android.graphics.RectF;
//import android.hardware.Camera;
//import android.hardware.Camera.CameraInfo;
//import android.hardware.Camera.Size;
//import android.os.Bundle;
//import android.view.Display;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.Window;
//import android.view.WindowManager;
//
//import java.util.List;
//import android.app.Activity;
//import android.hardware.Camera;
//import android.hardware.Camera.Parameters;
//import android.os.Bundle;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//
//public class buff extends Activity{
//
//	SurfaceView surfaceView;
//	Camera camera;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.podtagivaniya);
//
//		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
//
//		SurfaceHolder holder = surfaceView.getHolder();
//		holder.addCallback(new SurfaceHolder.Callback() {
//			@Override
//			public void surfaceCreated(SurfaceHolder holder) {
//				try {
//					camera.setPreviewDisplay(holder);
//					camera.startPreview();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void surfaceChanged(SurfaceHolder holder, int format,
//			                           int width, int height) {
//				setCameraDisplayOrientation(0);
//			}
//
//			@Override
//			public void surfaceDestroyed(SurfaceHolder holder) {
//			}
//		});
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		camera = Camera.open();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (camera != null)
//			camera.release();
//		camera = null;
//	}
//
//	void setCameraDisplayOrientation(int cameraId) {
//		// определяем насколько повернут экран от нормального положения
//		int rotation = getWindowManager().getDefaultDisplay().getRotation();
//		int degrees = 0;
//		switch (rotation) {
//			case Surface.ROTATION_0:
//				degrees = 0;
//				break;
//			case Surface.ROTATION_90:
//				degrees = 90;
//				break;
//			case Surface.ROTATION_180:
//				degrees = 180;
//				break;
//			case Surface.ROTATION_270:
//				degrees = 270;
//				break;
//		}
//
//		int result = 0;
//
//		// получаем инфо по камере cameraId
//		CameraInfo info = new CameraInfo();
//		Camera.getCameraInfo(cameraId, info);
//
//		// задняя камера
//		if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
//			result = ((360 - degrees) + info.orientation);
//		} else
//			// передняя камера
//			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
//				result = ((360 - degrees) - info.orientation);
//				result += 360;
//			}
//		result = result % 360;
//		camera.setDisplayOrientation(result);
//	}
//
//}