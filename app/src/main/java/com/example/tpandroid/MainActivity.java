package com.example.tpandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    // Liste des liens vers les images
    private ArrayList<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getAllShownImagesPath();
        setContentView(new Touch(this,imageList));
    }

    /**
     * getAllShownImagesPath : demande la permission de lire les images si ce n'est pas déjà fait
     */
    private void getAllShownImagesPath() {

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

    /**
     * getAllPicture : Récupère toutes les images du téléphone et ajoute l'adresse dans imageList
     */
    public void getAllPicture(){

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC";
        imageList = new ArrayList<>();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder);

        try {
            if (null != cursor) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(column_index);
                    imageList.add(path);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * onRequestPermissionsResult : Récupère les photos si la permission a été accordée
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        if (requestCode==PICK_FROM_GALLERY && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAllPicture();
        }
    }

}