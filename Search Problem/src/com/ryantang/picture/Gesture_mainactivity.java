package com.ryantang.picture;

import android.R.drawable;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.graphics.Bitmap;

public class Gesture_mainactivity extends Activity implements OnTouchListener  {
	private static final String TAG = "TouchActivity";
	private Matrix mMatrix = new Matrix();
    private float mScaleFactor = .4f;
    private float mRotationDegrees = 0.f;
    private float mFocusX = 0.f;
    private float mFocusY = 0.f;  
    private int mAlpha = 255;
    private int mImageHeight, mImageWidth;

    private ScaleGestureDetector mScaleDetector;
    private RotateGestureDetector mRotateDetector;
    private MoveGestureDetector mMoveDetector;
    private ShoveGestureDetector mShoveDetector; 

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//modify by zhang jiayuan 
		////Bitmap bitmap;
		MainActivity bitmap_picture=new MainActivity();
		////bitmap=bitmap_picture.smallBitmap;
        //get default display
		Display display = getWindowManager().getDefaultDisplay();
		mFocusX = display.getWidth()/2f;
		mFocusY = display.getHeight()/2f;
		
		ImageView view = (ImageView) findViewById(R.id.imageView);
		//parameters is the touch listener to attach to this view,in this is the view itself
		view.setOnTouchListener(this);
		
		////BitmapDrawable d 		=new  BitmapDrawable(getResources(),bitmap);
		Drawable  d    =this.getResources().getDrawable(R.id.img);
		mImageHeight 	= d.getIntrinsicHeight();
		mImageWidth 	= d.getIntrinsicWidth();
		Log.i("INFO","已经跳转到gesture页面");
		//mImageHeight 	= bitmap_picture.smallBitmap.getHeight()/10;
		//mImageWidth 	= bitmap_picture.smallBitmap.getWidth()/10;
        float scaledImageCenterX = (mImageWidth * mScaleFactor)/2; 
        float scaledImageCenterY = (mImageHeight * mScaleFactor)/2;
        
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
		view.setImageMatrix(mMatrix);
		
		mScaleDetector 	= new ScaleGestureDetector(this, new ScaleListener());
		mRotateDetector = new RotateGestureDetector(this, new RotateListener());
		mMoveDetector 	= new MoveGestureDetector(this, new MoveListener());
		mShoveDetector 	= new ShoveGestureDetector(this, new ShoveListener());
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        mRotateDetector.onTouchEvent(event);
        mMoveDetector.onTouchEvent(event);
        mShoveDetector.onTouchEvent(event);

        float scaledImageCenterX = (mImageWidth*mScaleFactor)/2;
        float scaledImageCenterY = (mImageHeight*mScaleFactor)/2;
        
        mMatrix.reset();
        mMatrix.postScale(mScaleFactor, mScaleFactor);
        mMatrix.postRotate(mRotationDegrees,  scaledImageCenterX, scaledImageCenterY);
        mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);
        
		ImageView view = (ImageView) v;
		view.setImageMatrix(mMatrix);
		view.setAlpha(mAlpha);

		return true; 
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();  
			
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f)); 

			return true;
		}
	}
	
	private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
		@Override
		public boolean onRotate(RotateGestureDetector detector) {
			mRotationDegrees -= detector.getRotationDegreesDelta();
			System.err.println("lili mRotationDegrees===" + mRotationDegrees);
			return true;
		}
	}	
	
	private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			PointF d = detector.getFocusDelta();
			mFocusX += d.x;
			mFocusY += d.y;		

			// mFocusX = detector.getFocusX();
			// mFocusY = detector.getFocusY();
			return true;
		}
	}		
	
	private class ShoveListener extends ShoveGestureDetector.SimpleOnShoveGestureListener {
		@Override
		public boolean onShove(ShoveGestureDetector detector) {
			mAlpha += detector.getShovePixelsDelta();
			if (mAlpha > 255)
				mAlpha = 255;
			else if (mAlpha < 0)
				mAlpha = 0;
			
			return true;
		}
	}	

}
