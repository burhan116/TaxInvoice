package com.example.taxinvoice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {



    Button uploadimage, saveInfo;
    ImageView imageView;
    EditText description;
    ProgressDialog progressDialog;
    Uri file;
    private static final int IMAGE_SELECT_REQUEST_CODE = 122;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadimage = findViewById(R.id.uploadimage);
        imageView = findViewById(R.id.imageView);
        saveInfo = findViewById(R.id.savedata);
        description = findViewById(R.id.description);

        progressDialog = new ProgressDialog(this);

        FirebaseFirestore fStore=FirebaseFirestore.getInstance();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
           uploadimage.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_SELECT_REQUEST_CODE);
        }

        //Upload an image
        uploadimage.setOnClickListener(v -> {
            openGallery();
        });

        saveInfo.setOnClickListener(v -> {
            if (file == null||description.getText()==null) {
                Toast.makeText(UploadActivity.this, "Please select an image",
                        Toast.LENGTH_LONG).show();
            } else {
                progressDialog.setMessage("Uploading Image ...");
                progressDialog.show();

                String uid= UUID.randomUUID().toString();
                String path = "images/"+ uid;

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                final StorageReference reference = storageReference.child(path);
                UploadTask uploadTask = reference.putFile(file);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        Date date = Calendar.getInstance().getTime();
                        InformationModel informationModel = new InformationModel(uri.toString(),
                                description.getText().toString(), date);
                        fStore.collection("data").add(informationModel)
                                .addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        Toast.makeText(UploadActivity.this,
                                                "data inserted well", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(UploadActivity.this,
                                                "data not inserted well",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    });
                    Toast.makeText(UploadActivity.this, "Uploading Finished ...",
                            Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> Toast.makeText(UploadActivity.this,
                        "Failed to Upload", Toast.LENGTH_SHORT).show());

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            file = data.getData();
            imageView.setImageURI(file);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMAGE_SELECT_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadimage.setEnabled(true);
                Toast.makeText(this, "Camera Permission Granted",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Camera Permission Denied",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
}