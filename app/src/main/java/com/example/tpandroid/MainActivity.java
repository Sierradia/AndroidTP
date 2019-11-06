package com.example.tpandroid;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;

    private ArrayList<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getAllShownImagesPath();
        setContentView(new MyView(this));

    }

    public class MyView extends View {

        public MyView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);

            int largeurPhotos = 3;
            int compteur = 0;
            int i=0,j=0;
            int widthPhone = getWidth();

            if (imageList != null) {
                while (compteur < imageList.size() && compteur < 10) {

                    Drawable d = Drawable.createFromPath(imageList.get(i * 3 + j));
                    d.setBounds(widthPhone * j / 3, widthPhone * i / 3, widthPhone * (j + 1) / 3, widthPhone * (i + 1) / 3);
                    d.draw(canvas);

                    j++;
                    if (j >= largeurPhotos) {
                        i++;
                        j = 0;
                    }

                    compteur++;
                }
            }

            /*
            Drawable d1 = Drawable.createFromPath(imageList.get(0));
            Drawable d2 = Drawable.createFromPath(imageList.get(1));
            Drawable d3 = Drawable.createFromPath(imageList.get(2));

            d1.setBounds(0, 0, getWidth()*1/3, getWidth()*1/3);
            d2.setBounds(getWidth()*1/3, 0, getWidth()*2/3, getWidth()*1/3);
            d3.setBounds(getWidth()*2/3, 0, getWidth()*3/3, getWidth()*1/3);
            d1.draw(canvas);
            d2.draw(canvas);
            d3.draw(canvas);
*/

        }
    }


    public void getAllShownImagesPath() {

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
                getAllPicture();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getAllPicture(){

        Uri uri;
        Cursor cursor;
        int column_index;
        String path;
        String sortOrder;
        imageList = new ArrayList<>();
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA};
        sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC";
        cursor = getContentResolver().query(uri, projection, null, null, sortOrder);

        try {
            if (null != cursor) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                while (cursor.moveToNext()) {
                    path = cursor.getString(column_index);
                    imageList.add(path);
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllPicture();
                } else {
                }
                break;
        }
    }

}