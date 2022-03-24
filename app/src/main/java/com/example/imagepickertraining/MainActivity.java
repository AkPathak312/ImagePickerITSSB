package com.example.imagepickertraining;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

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
        final View view1 =getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
        builder.setView(view1);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("TAGG","Inside onCLick");
                ImageView gallery=view1.findViewById(R.id.opengallery);
                gallery.setOnClickListener(item->{
                    Log.d("TAGG","Inside Gallery");
                    Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(i,101);
                });
                ImageView camera=view1.findViewById(R.id.opencamera);
                camera.setOnClickListener(item->{

                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            //Setting image to IMage View
            Uri uri=data.getData();
            imageView.setImageURI(uri);

            //Getting the Path of image
            String[] stringPath={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(uri,stringPath,null,null,null);
            cursor.moveToFirst();
            int index=cursor.getColumnIndex(stringPath[0]);
            String filePath=cursor.getString(index);
            Log.d("File Path: ",filePath);
        }

    }
}