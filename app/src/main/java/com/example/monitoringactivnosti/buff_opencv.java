//package com.example.monitoringactivnosti;
//
//import java.text.SimpleDateFormat;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.ListIterator;
//
//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.CameraActivity;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
//import org.opencv.android.LoaderCallbackInterface;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.core.Mat;
//import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
//
//import android.annotation.SuppressLint;
//import android.hardware.Camera.Size;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.SubMenu;
//import android.view.SurfaceView;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//public class buff_opencv extends CameraActivity implements CvCameraViewListener2, OnTouchListener {
//	private static final String TAG = "OCVSample::Activity";
//
//	private Tutorial3View mOpenCvCameraView;
//	private List<Size> mResolutionList;
//	private Menu mMenu;
//	private boolean mCameraStarted = false;
//	private boolean mMenuItemsCreated = false;
//	private MenuItem[] mEffectMenuItems;
//	private SubMenu mColorEffectsMenu;
//	private MenuItem[] mResolutionMenuItems;
//	private SubMenu mResolutionMenu;
//
//	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
//		@Override
//		public void onManagerConnected(int status) {
//			switch (status) {
//				case LoaderCallbackInterface.SUCCESS:
//				{
//					Log.i(TAG, "OpenCV loaded successfully");
//					mOpenCvCameraView.enableView();
//					mOpenCvCameraView.setOnTouchListener(buff_opencv.this);
//				} break;
//				default:
//				{
//					super.onManagerConnected(status);
//				} break;
//			}
//		}
//	};
//
//	public buff_opencv() {
//		Log.i(TAG, "Instantiated new " + this.getClass());
//	}
//
//	/** Called when the activity is first created. */
//	@SuppressLint("WrongViewCast")
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		Log.i(TAG, "called onCreate");
//		super.onCreate(savedInstanceState);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//		setContentView(R.layout.tutorial3_surface_view);
//
//		mOpenCvCameraView = findViewById(R.id.tutorial3_activity_java_surface_view);
//
//		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
//
//		mOpenCvCameraView.setCvCameraViewListener(this);
//	}
//
//	@Override
//	public void onPause()
//	{
//		super.onPause();
//		if (mOpenCvCameraView != null)
//			mOpenCvCameraView.disableView();
//	}
//
//	@Override
//	public void onResume()
//	{
//		super.onResume();
//		if (!OpenCVLoader.initDebug()) {
//			Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
//		} else {
//			Log.d(TAG, "OpenCV library found inside package. Using it!");
//			mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
//		}
//	}
//
//	@Override
//	protected List<? extends CameraBridgeViewBase> getCameraViewList() {
//		return Collections.singletonList(mOpenCvCameraView);
//	}
//
//	public void onDestroy() {
//		super.onDestroy();
//		if (mOpenCvCameraView != null)
//			mOpenCvCameraView.disableView();
//	}
//
//	@Override
//	public void onCameraViewStarted(int width, int height) {
//		mCameraStarted = true;
//		setupMenuItems();
//	}
//
//	public void onCameraViewStopped() {
//	}
//
//	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
//		return inputFrame.rgba();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		mMenu = menu;
//		setupMenuItems();
//		return true;
//	}
//
//	private void setupMenuItems() {
//		if (mMenu == null || !mCameraStarted || mMenuItemsCreated) {
//			return;
//		}
//		List<String> effects = mOpenCvCameraView.getEffectList();
//
//		if (effects == null) {
//			Log.e(TAG, "Color effects are not supported by device!");
//			return;
//		}
//
//		mColorEffectsMenu = mMenu.addSubMenu("Color Effect");
//		mEffectMenuItems = new MenuItem[effects.size()];
//
//		int idx = 0;
//		ListIterator<String> effectItr = effects.listIterator();
//		for (String effect: effects) {
//			mEffectMenuItems[idx] = mColorEffectsMenu.add(1, idx, Menu.NONE, effect);
//			idx++;
//		}
//
//		mResolutionMenu = mMenu.addSubMenu("Resolution");
//		mResolutionList = mOpenCvCameraView.getResolutionList();
//		mResolutionMenuItems = new MenuItem[mResolutionList.size()];
//
//		idx = 0;
//		for (Size resolution: mResolutionList) {
//			mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
//					Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString());
//			idx++;
//		}
//		mMenuItemsCreated = true;
//	}
//
//	public boolean onOptionsItemSelected(MenuItem item) {
//		Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
//		if (item.getGroupId() == 1)
//		{
//			mOpenCvCameraView.setEffect((String) item.getTitle());
//			Toast.makeText(this, mOpenCvCameraView.getEffect(), Toast.LENGTH_SHORT).show();
//		}
//		else if (item.getGroupId() == 2)
//		{
//			int id = item.getItemId();
//			Size resolution = mResolutionList.get(id);
//			mOpenCvCameraView.setResolution(resolution);
//			resolution = mOpenCvCameraView.getResolution();
//			String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
//			Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
//		}
//
//		return true;
//	}
//
//	@SuppressLint("SimpleDateFormat")
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		Log.i(TAG,"onTouch event");
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//		String currentDateandTime = sdf.format(new Date());
////        String fileName = Environment.getExternalStorageDirectory().getPath() +
////                "/sample_picture_" + currentDateandTime + ".jpg";
//		String fileName = Environment.getExternalStorageDirectory() + "/DCIM" +
//				"/sample_picture_" + currentDateandTime + ".jpg";
//		mOpenCvCameraView.takePicture(fileName);
//		Toast.makeText(this, fileName + " saved", Toast.LENGTH_SHORT).show();
//		return false;
//	}
//}
