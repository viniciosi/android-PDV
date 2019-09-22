package com.example.pdv;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class SyncDB {
    public static void backupDB(final Context context, final TextView inTxt){
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

// Create a reference to "mountains.jpg"
        StorageReference ref = storageRef.child("db/pdv.db");

        Uri file = Uri.fromFile(new File(Environment.getDataDirectory() + "/data/com.example.pdv/databases/pdv.db"));

        ref.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        getUltimoBackup(context, inTxt);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });

// Create a reference to 'images/mountains.jpg'
        //StorageReference mountainImagesRef = storageRef.child("db/pdv.db");

// While the file names are the same, the references point to different files
        //ref.getName().equals(mountainImagesRef.getName());    // true
        //ref.getPath().equals(mountainImagesRef.getPath());    // falseStorageActivity.java
    }

    static TextView txt = null;

    public static void getUltimoBackup(final Context context, TextView inTxt) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child("db/pdv.db");

        txt = inTxt;

        ref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                txt.setText("Último Backup: " + new Date(storageMetadata.getCreationTimeMillis()).toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void restoreDB(final Context context){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child("db/pdv.db");

        File localFile = new File(Environment.getDataDirectory() + "/data/com.example.pdv/databases/pdv.db");

        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Restore Feito! ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
