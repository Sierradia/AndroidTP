package com.example.tpandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import java.util.ArrayList;

public class Touch extends View {

    private float mScale = 1f;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    public class ZoomGesture extends GestureDetector.SimpleOnGestureListener {
        private boolean normal = true;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mScale = normal ? 3f : 1f;
            normal = !normal;
            invalidate();
            return true;
        }
    }


    /////////////////////////////////////////////////////////////////////////////////


    private float mScaleBefore = 1f;
    private int nbCols = 3;


    ArrayList<String> imageList;

    public Touch(Context context, ArrayList<String> imageList) {

        super(context);
        this.imageList = imageList;

        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int largeurPhotos = 3;
        int compteur = 0;
        int widthPhone = getWidth();

        if (mScaleBefore - mScale < 0) {
            // Agrandir
            if (nbCols > 1)
                nbCols--;
        } else if (mScaleBefore - mScale > 0) {
            // Retrécir
            if (nbCols < 7)
                nbCols++;
        }

        mScaleBefore = mScale;

        if (imageList != null) {

            int i = 0, j = 0;

            while (compteur < imageList.size() && compteur < 5) {


                Drawable d1 = Drawable.createFromPath(imageList.get(i * 3 + j));
                Bitmap bitmap = ((BitmapDrawable) d1).getBitmap();

                if (bitmap != null) {
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 800 / nbCols, 800 / nbCols, true));

                    d.setBounds(widthPhone * j / nbCols, widthPhone * i / nbCols, widthPhone * (j + 1) / nbCols, widthPhone * (i + 1) / nbCols);
                    d.draw(canvas);
                }


                j++;
                if (j >= largeurPhotos) {
                    i++;
                    j = 0;
                }

                compteur++;
            }
        }
    }


    private boolean alreadyTouched = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (alreadyTouched){
            alreadyTouched = false;
        }else{
            mGestureDetector.onTouchEvent(event);
            mScaleGestureDetector.onTouchEvent(event);
            alreadyTouched = true;
        }
        return true;
    }


    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScale *= detector.getScaleFactor();
            Log.d("Coucou", "MScale : " + mScale);
            invalidate();
            return true;
        }
    }
}

