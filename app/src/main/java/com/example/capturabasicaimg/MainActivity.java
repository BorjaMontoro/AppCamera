package com.example.capturabasicaimg;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static int RC_PHOTO_PICKER = 0;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    private ActivityResultLauncher<Intent> someActivityResultLauncher2;
    public Uri photoURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button galeria=findViewById(R.id.galeria);
        Button camara=findViewById(R.id.camera);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            ImageView imageView = findViewById(R.id.img);
                            imageView.setImageURI(uri);
                        }
                    }
                });
        someActivityResultLauncher2= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //Intent data = result.getData();
                            //Bundle extras = data.getExtras();
                            // imageBitmap = (Bitmap) extras.get("data");
                            ImageView imageView = findViewById(R.id.img);
                            //imageView.setImageBitmap(imageBitmap);
                            imageView.setImageURI(photoURI);
                        }

                    }
                });
        galeria.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Create Intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                //Launch activity to get result
                someActivityResultLauncher.launch(intent);
            }
        });
        camara.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //Create Intent
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(MainActivity.this, "com.example.android.fileprovider", photoFile);
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    someActivityResultLauncher2.launch(intent2) ;
                }
            }
        });
    }
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}