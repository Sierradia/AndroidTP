package com.example.tpandroid;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

import static com.example.tpandroid.R.drawable.ic_launcher_background;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = new Button(this);
        button.setText("Press to scan");
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        //ImageView imageView = new ImageView(this);
        //TextView text = new TextView(this);
        RelativeLayout relativeLayout = new RelativeLayout(this);

        //relativeLayout.addView(text);
        //relativeLayout.addView(imageView);
        relativeLayout.addView(button);
        //relativeLayout.addView(canvas);
        setContentView(new MyView(this));

        //GALLERY_REQUEST_CODE= 10;

        //imageView = findViewById(R.id.imageView);
        //GALLERY_REQUEST_CODE= 10;
        getAllShownImagesPath();
    }

    public class MyView extends View {
        public MyView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            /*int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 100;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle(x / 2, y / 2, radius, paint);*/
            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), ic_launcher_background);
            canvas.drawBitmap(bitmap1,0,0, null);

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
        ArrayList<String> imageList = new ArrayList<>();
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
            TextView text = findViewById(R.id.coucou);
            text.setText("Nombre d'images " + imageList.size());
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