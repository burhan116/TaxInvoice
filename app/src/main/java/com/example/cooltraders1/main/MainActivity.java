package com.example.cooltraders1.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.cooltraders1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button chooseimage,uploadimage;
    ImageView imageView;
    ProgressDialog progressDialog;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseimage=findViewById(R.id.chooseimage);
        uploadimage=findViewById(R.id.uploadimage);
        imageView=findViewById(R.id.imageView);


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading !...");


        FirebaseStorage firebaseStorage =FirebaseStorage.getInstance();
        final StorageReference storageReference=firebaseStorage.getInstance().getReference();

        // Choose an image
        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_REQUEST);
            }
        });

        //Upload an image
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    progressDialog.show();

                    StorageReference childReference = storageReference.child("/image");

                    UploadTask uploadTask = childReference.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Upload Successful", Toast.LENGTH_LONG).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Upload Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == PICK_IMAGE_REQUEST && requestCode==RESULT_OK && data != null && data.getData() != null)
        {
            filePath=data.getData();
            try {
                //getting image from gallery
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}