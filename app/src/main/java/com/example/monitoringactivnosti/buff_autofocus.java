//package com.example.monitoringactivnosti;
//
//		import android.app.Activity;
//		import android.graphics.Bitmap;
//		import android.hardware.Camera;
//		import android.os.Bundle;
//		import android.view.SurfaceView;
//
//
//		import android.view.Surface;
//		import android.view.SurfaceHolder;
//		import android.view.View;
//
//		import org.opencv.android.BaseLoaderCallback;
//		import org.opencv.android.LoaderCallbackInterface;
//		import org.opencv.android.OpenCVLoader;
//		import org.opencv.android.Utils;
//		import org.opencv.core.Mat;
//		import org.opencv.core.MatOfDouble;
//		import org.opencv.core.MatOfFloat;
//		import org.opencv.core.MatOfRect;
//		import org.opencv.core.Point;
//		import org.opencv.core.Rect;
//		import org.opencv.core.Scalar;
//		import org.opencv.imgproc.Imgproc;
//		import org.opencv.objdetect.HOGDescriptor;
//
//		import java.util.List;
//
//
//public class buff_autofocus extends Activity {
//
//	SurfaceView surfaceView;
//	Camera camera;
//	Mat img;
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
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		camera = Camera.open();
//		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);
//	}
//	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//		@Override
//		public void onManagerConnected(int status) {
//			switch (status) {
//				case LoaderCallbackInterface.SUCCESS:
//				{
//					//Мы готовы использовать OpenCV
//				} break;
//				default:
//				{
//					super.onManagerConnected(status);
//				} break;
//			}
//		}
//	};
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
//
//	public Bitmap peopleDetect (String path ) {
//		Bitmap bitmap = null;
//		float execTime;
//		try {
//
//			bitmap = camera.decodeStream(input, null, opts);
//			long time = System.currentTimeMillis();
//			// Создаем матрицу изображения для OpenCV и помещаем в нее нашу фотографию
//			Mat mat = new Mat();
//			Utils.bitmapToMat(bitmap, mat);
//			// Переконвертируем матрицу с RGB на градацию серого
//			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY, 4);
//			HOGDescriptor hog = new HOGDescriptor();
//			//Получаем стандартный определитель людей и устанавливаем его нашему дескриптору
//			MatOfFloat descriptors = HOGDescriptor.getDefaultPeopleDetector();
//			hog.setSVMDetector(descriptors);
//			// Определяем переменные, в которые будут помещены результаты поиска ( locations - прямоугольные области, weights - вес (можно сказать релевантность) соответствующей локации)
//			MatOfRect locations = new MatOfRect();
//			MatOfDouble weights = new MatOfDouble();
//			// Собственно говоря, сам анализ фотографий. Результаты запишутся в locations и weights
//			hog.detectMultiScale(mat, locations, weights);
//			execTime = ( (float)( System.currentTimeMillis() - time ) ) / 1000f;
//			//Переменные для выделения областей на фотографии
//			Point rectPoint1 = new Point();
//			Point rectPoint2 = new Point();
//			Scalar fontColor = new Scalar(0, 0, 0);
//			Point fontPoint = new Point();
//			// Если есть результат - добавляем на фотографию области и вес каждой из них
//			if (locations.rows() > 0) {
//				List<Rect> rectangles = locations.toList();
//				int i = 0;
//				List<Double> weightList = weights.toList();
//				for (Rect rect : rectangles) {
//					float weigh = weightList.get(i++).floatValue();
//
//					rectPoint1.x = rect.x;
//					rectPoint1.y = rect.y;
//					fontPoint.x  = rect.x;
//					fontPoint.y  = rect.y - 4;
//					rectPoint2.x = rect.x + rect.width;
//					rectPoint2.y = rect.y + rect.height;
//					final Scalar rectColor = new Scalar( 0  , 0 , 0  );
//					// Добавляем на изображения найденную информацию
//					Core.rectangle(mat, rectPoint1, rectPoint2, rectColor, 2);
//					Core.putText(mat,
//							String.format("%1.2f", weigh),
//							fontPoint, Core.FONT_HERSHEY_PLAIN, 1.5, fontColor,
//							2, Core.LINE_AA, false);
//
//				}
//			}
//			fontPoint.x = 15;
//			fontPoint.y = bitmap.getHeight() - 20;
//			// Добавляем дополнительную отладочную информацию
//			Core.putText(mat,
//					"Processing time:" + execTime + " width:" + bitmap.getWidth() + " height:" + bitmap.getHeight() ,
//					fontPoint, Core.FONT_HERSHEY_PLAIN, 1.5, fontColor,
//					2, Core.LINE_AA, false);
//			Utils.matToBitmap( mat , bitmap );
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return bitmap;
//	}
//}
