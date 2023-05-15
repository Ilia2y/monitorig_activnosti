//package com.example.monitoringactivnosti;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Rect;
//import android.hardware.Camera;
//import android.os.Bundle;
//import android.view.SurfaceView;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.View;
//
//import org.pytorch.IValue;
//import org.pytorch.Module;
//import org.pytorch.Tensor;
//import org.pytorch.torchvision.TensorImageUtils;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//public class podtagivaniya extends Activity {
//
//	SurfaceView surfaceView;
//	Camera camera;
//	Module module;
//	Bitmap bitmap;
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
//					camera.autoFocus(podtagivaniya.this::onAutoFocus);
//					surfaceView.setOnClickListener(podtagivaniya.this::startFocus);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void surfaceChanged(SurfaceHolder holder, int format,
//			                           int width, int height) {
//				setCameraDisplayOrientation(0, camera);
//			}
//
//			@Override
//			public void surfaceDestroyed(SurfaceHolder holder) {
//			}
//		});
//
//		//module = Module.load(assetFilePath(this, "model.ptl"));
//
//		Timer timer = new Timer();
//		TimerTask timerTask = new TimerTask() {
//			@Override
//			public void run() {
//
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Surface s = holder.getSurface();
//						camera.takePicture(null, null, new Camera.PictureCallback() {
//							@Override
//							public void onPictureTaken(byte[] data, Camera camera) {
//								bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//								Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
//										TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
//								Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
//								float[] scores = outputTensor.getDataAsFloatArray();
//								float maxScore = -Float.MAX_VALUE;
//								int maxScoreIdx = -1;
//								for (int i = 0; i < scores.length; i++) {
//									if (scores[i] > maxScore) {
//										maxScore = scores[i];
//										maxScoreIdx = i;
//									}
//								}
//								//String className = ImageNetClasses.IMAGENET_CLASSES[maxScoreIdx];
//
//							}
//						});
//						camera = Camera.open();
//					}
//				});
//
//			}
//		};
//		timer.schedule(timerTask, 0, 1000);
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		camera = Camera.open();
//	}
//
//
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		if (camera != null)
//			camera.release();
//		camera = null;
//	}
//
//	public void setCameraDisplayOrientation(int cameraId, Camera camera) {
//		android.hardware.Camera.CameraInfo info =
//				new android.hardware.Camera.CameraInfo();
//		android.hardware.Camera.getCameraInfo(cameraId, info);
//		int rotation = getWindowManager().getDefaultDisplay()
//				.getRotation();
//		int degrees = 0;
//		switch (rotation) {
//			case Surface.ROTATION_0:
//				break;
//			case Surface.ROTATION_90: degrees = 90; break;
//			case Surface.ROTATION_180: degrees = 180; break;
//			case Surface.ROTATION_270: degrees = 270; break;
//		}
//
//		int result;
//		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//			result = (info.orientation + degrees) % 360;
//			result = (360 - result) % 360;  // compensate the mirror
//		} else {  // back-facing
//			result = (info.orientation - degrees + 360) % 360;
//		}
//		camera.setDisplayOrientation(result);
//	}
//
//	void onAutoFocus(boolean success,
//	                 Camera camera){
//		System.out.println(success);
//
//	}
//
//	private void startFocus(View view) {
//		camera.autoFocus(podtagivaniya.this::onAutoFocus);
//	}
//}
