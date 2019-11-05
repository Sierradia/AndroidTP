package com.example.tpandroid;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //imageView = findViewById(R.id.imageView);
        //GALLERY_REQUEST_CODE= 10;
        getAllShownImagesPath();
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