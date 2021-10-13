package com.example.visionstask;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.visionstask.databinding.ActivityAddDataBinding;
import com.example.visionstask.databinding.ActivityMainBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener, MediaUtils.GetImg {
    ActivityAddDataBinding activityMainBinding;
    MediaUtils mediaUtils;
    Dialog picture_dialog;
    String imagePath = "";
    private DBHandler dbHandler;
    boolean isAllPermissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityAddDataBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        mediaUtils = new MediaUtils(this);
        dbHandler = new DBHandler(AddDataActivity.this);

        activityMainBinding.cameraIV.setOnClickListener(this);
        activityMainBinding.saveBT.setOnClickListener(this);
        requestMultiplePermissions();

    }

    private void requestMultiplePermissions() {
        Dexter.withContext(this)
                .withPermissions(
                        android.Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Log.d("STORAGE_DIALOG","All permissions are granted by user!");
                            isAllPermissionGranted = true;
                        }else {
                            requestMultiplePermissions();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            Log.d("STORAGE_DIALOG","openSettingsDialog");

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Log.d("ERROR",error.toString());
                    }
                })
                .onSameThread()
                .check();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.camera_IV:
                if (isAllPermissionGranted){
                    showPictureDialog();
                }else {
                    Toast.makeText(this, "Camera & Storage Permission Not allowed ", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.save_BT:
                String userName = activityMainBinding.enterNameTIET.getText().toString();
                String gender = "";
                if (activityMainBinding.maleRB.isChecked()) {
                    gender = "Male";

                } else if (activityMainBinding.femaleRB.isChecked()) {
                    gender = "Female";

                } else if (activityMainBinding.otherRB.isChecked()) {
                    gender = "Other";
                }


                if (userName.equals("")) {
                    activityMainBinding.enterNameTIL.setError("Enter user name");
                    break;
                }
                if (gender.equals("")) {
                    Toast.makeText(this, "Select gender !", Toast.LENGTH_SHORT).show();
                    break;
                }
//                if (imagePath.equals("")) {
//                    Toast.makeText(this, "Select Image !", Toast.LENGTH_SHORT).show();
//                    break;
//                }

                dbHandler.addNewUser(userName,gender,imagePath);

                Toast.makeText(this, "User Added ", Toast.LENGTH_SHORT).show();

                finish();



                break;
        }

    }

    private void showPictureDialog() {
        picture_dialog = new Dialog(this);
        picture_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        picture_dialog.setCancelable(true);
        picture_dialog.setContentView(R.layout.select_image_dialog);
        ImageView dialog_camera_IV = picture_dialog.findViewById(R.id.dialog_camera_IV);
        ImageView dialog_gallery_IV = picture_dialog.findViewById(R.id.dialog_gallery_IV);

        dialog_camera_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaUtils.openCamera();
                picture_dialog.dismiss();
            }
        });


        dialog_gallery_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaUtils.openGallery();
                picture_dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = picture_dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        picture_dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void imgdata(String imgPath) {
        Log.d("ImagePath",imgPath);
        imagePath = imgPath;
        File imgFile = new File(imgPath);
        Glide.with(this).load(imgPath).into(activityMainBinding.profilePicIV);

    }
}