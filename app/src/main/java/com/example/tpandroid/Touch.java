package com.example.tpandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;


class Touch extends View {

    // Echelle du zoom
    private float mScale = 1f;
    // Echelle du zoom précédent
    private float mScaleBefore = 1f;
    // Nombre d'images par ligne
    private int nbCols = 3;
    // Chemin vers les images
    private ArrayList<String> imageList;
    // Evite les doubles zoom
    private boolean alreadyTouched = false;

    private ScaleGestureDetector mScaleGestureDetector;


    public Touch(Context context, ArrayList<String> imageList) {
        super(context);
        this.imageList = imageList;
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
    }

    /**
     * onDraw : Change le nombre d'images en fonction du zoom et les réaffiche
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mScaleBefore - mScale < 0) {
            // Zoom
            if (nbCols > 1) nbCols--;
        } else if (mScaleBefore - mScale > 0) {
            // Dézoom
            if (nbCols < 7) nbCols++;
        }
        mScaleBefore = mScale;
        afficherImages(canvas);
    }

    /**
     * afficherImages : Affiche les images dans le canvas en fonction du nombre d'images par ligne
     * (nbCols) et de la liste des images (imageList)
     * @param canvas
     */
    private void afficherImages (Canvas canvas){

        int widthPhone = getWidth();
        int compteur = 0;

        if (imageList != null) {

            int i = 0, j = 0;

            // Affiche un maximum de 8 images car le zoom ne fonctionne plus bien après
            while (compteur < imageList.size() && compteur < 8) {

                // Récupère les images par rapport au chemin
                Drawable d1 = Drawable.createFromPath(imageList.get(i * nbCols + j));
                Bitmap bitmap = ((BitmapDrawable) d1).getBitmap();

                if (bitmap != null) {
                    // On resize pour diminuer la taille en pixel des images
                    Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 800 / nbCols, 800 / nbCols, true));
                    d.setBounds(widthPhone * j / nbCols, widthPhone * i / nbCols, widthPhone * (j + 1) / nbCols, widthPhone * (i + 1) / nbCols);
                    d.draw(canvas);
                }

                // On change les index des images à récupérer
                j++;
                if (j >= nbCols) {
                    i++;
                    j = 0;
                }

                compteur++;
            }
        }
    }

    /**
     * onTouchEvent : Appel la méthode de zoom si elle n'a pas déjà été appelée
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (alreadyTouched){
            alreadyTouched = false;
        }else{
            // On appelle la méthode de zoom
            mScaleGestureDetector.onTouchEvent(event);
            alreadyTouched = true;
        }
        return true;
    }

    /**
     * ScaleGesture : Récupère l'échelle du zoom effectué
     */
    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScale *= detector.getScaleFactor();
            invalidate();
            return true;
        }
    }
}

