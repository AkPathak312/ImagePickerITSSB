package com.example.imagepickertraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    final int GALLERY_CODE=101;
    final int CAMERA_CODE=102;
    final int CAMERA_PERMISSION=201;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.image_view);
    }

    public void selectImage(View view) {
        //Custom Alert Dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        View customDialogView =getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
        ImageView galleryView=customDialogView.findViewById(R.id.opengallery);
        galleryView.setOnClickListener(item->{
            Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(i,GALLERY_CODE);
        });
        ImageView cameraView=customDialogView.findViewById(R.id.opencamera);

        //AppNAme/Picures
        cameraView.setOnClickListener(item->{
            openCamera();
        });
        builder.setView(customDialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

        //Popup with List
        /*String[] selection={"Gallery","Camera","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select");
        builder.setItems(selection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selection[which].equals("Gallery")){
                    //OPen Gallery
                    Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(i,101);
                }
                if(selection[which].equals("Camera")){
                    //OPen Camera
                }
            }
        });*/

        //Basic Popup with Cancel and OK
       /* AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select");
        builder.setMessage("This Is a Popup");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
       // builder.show();
    }

    private void openCamera() {
        if(this.checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
        }else{
            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i,CAMERA_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            //Setting image to IMage View
            Uri uri=data.getData();
            imageView.setImageURI(uri);

            //Getting the Path of image
            String[] stringPath={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(uri,stringPath,null,null,null);
            cursor.moveToFirst();
            int index=cursor.getColumnIndex(stringPath[0]);
            String filePath=cursor.getString(index);
           // File file=new File(filePath);
            Log.d("File Path: ",filePath);
        }
        if(requestCode==CAMERA_CODE && resultCode==RESULT_OK){
            //Set Image
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            Uri uri=createImageUri(bitmap);
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();
            int index=cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String imagePath=cursor.getString(index);
            cursor.close();
            Log.d("CAMERA IMAGE PATH ",imagePath);

            //D/CAMER IMAGE PATH: /storage/emulated/0/Pictures/Title.jpg
            ///storage/emulated/0/AppName/Sachhidanand/Title.jpg
            //Picasso : Library for ANdroid
        }
    }

    private Uri createImageUri(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        String imagePath= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Title",null);
        return Uri.parse(imagePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_PERMISSION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,CAMERA_CODE);
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}