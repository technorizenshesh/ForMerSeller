package com.my.formerseller.act;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.my.formerseller.FileUtil;
import com.my.formerseller.MainActivity;
import com.my.formerseller.Preference;
import com.my.formerseller.R;
import com.my.formerseller.databinding.ActivityEditProfileBinding;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.my.formerseller.act.Login.isValidEmail;


public class EditProfile extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    private Bitmap bitmap;
    private Uri resultUri;
    private SessionManager sessionManager;
    public static File UserProfile_img, codmpressedImage, compressActualFile;
    String UserNae="";
    String Email="";
    String Mobile="";
    String Password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

        binding.RRBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.Update.setOnClickListener(v -> {

            validation();

        });

        sessionManager = new SessionManager(EditProfile.this);

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getProfileMethod();

        }else {
            Toast.makeText(EditProfile.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        binding.RRUserimag.setOnClickListener(v ->{

            Dexter.withActivity(EditProfile.this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).getIntent(EditProfile.this);
                                startActivityForResult(intent, 1);
                            } else {
                                showSettingDialogue();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        });
    }


    private void validation() {

        UserNae = binding.edtName.getText().toString();
        Email = binding.etEmaillogin.getText().toString();
        Mobile = binding.edtMobile.getText().toString();

        if(UserNae.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter User Name.", Toast.LENGTH_SHORT).show();

        }else if(!isValidEmail(Email))
        {
            Toast.makeText(this, "Please Enter email.", Toast.LENGTH_SHORT).show();

        }else if(Mobile.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Mobile.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                Updateapi();

            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showSettingDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                openSetting();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }

    private void openSetting() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    // Glide.with(this).load(bitmap).circleCrop().into(img_userProfile);
                    UserProfile_img = FileUtil.from(this, resultUri);

                    Glide.with(this).load(bitmap).circleCrop().into(binding.imgUserProfile);

                    //isProfileImage = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    codmpressedImage = new Compressor(this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(UserProfile_img);
                    Log.e("ActivityTag", "imageFilePAth: " + codmpressedImage);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void getProfileMethod(){

        String UsserId= Preference.get(EditProfile.this,Preference.KEYType_login);

        Call<LoginModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_profile(UsserId);

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                LoginModel finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    binding.edtName.setText(finallyPr.getResult().getName());
                    binding.edtMobile.setText(finallyPr.getResult().getMobile());
                    binding.etEmaillogin.setText(finallyPr.getResult().getEmail());


                    Glide.with(EditProfile.this).load(finallyPr.getResult().getImage()).circleCrop().into(binding.imgUserProfile);

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfile.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Updateapi(){

        String UsserId= Preference.get(EditProfile.this,Preference.KEYType_login);

        MultipartBody.Part imgFile = null;

        if (UserProfile_img == null) {

        } else {
            RequestBody requestFileOne = RequestBody.create(MediaType.parse("image/*"),UserProfile_img);
            imgFile = MultipartBody.Part.createFormData("image",UserProfile_img.getName(), requestFileOne);
        }

        RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), UserNae);
        RequestBody Usser_Id = RequestBody.create(MediaType.parse("text/plain"), UsserId);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), Email);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), Mobile);

        Call<LoginModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .update_buyer_profile(Usser_Id,Name,email,mobile,imgFile);

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                LoginModel finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    Toast.makeText(EditProfile.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(EditProfile.this, MainActivity.class));

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfile.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}