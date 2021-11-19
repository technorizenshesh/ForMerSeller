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
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.my.formerseller.databinding.ActivitySignUpBinding;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    ActivitySignUpBinding binding;
    private Bitmap bitmap;
    private Uri resultUri;
    public static File UserProfile_img, codmpressedImage, compressActualFile;
    public static File UserProfile_img_one, compressedImage_two;
    String result="";
    String UserNae="";
    String Email="";
    String Mobile="";
    String Password="";
    String ShopName="";
    String ShopAddress="";
    String about="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        sessionManager = new SessionManager(SignUpActivity.this);

        SetupUI();

    }

    private void SetupUI() {

        binding.llSingin.setOnClickListener(v -> {
            startActivity(new Intent(this, Login.class));
        });
        binding.SignUp.setOnClickListener(v -> {
             validation();
            //startActivity(new Intent(this, VerificationActivity.class));
        });

        binding.RRUsername.setOnClickListener(v -> {

            Dexter.withActivity(SignUpActivity.this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).getIntent(SignUpActivity.this);
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

        binding.RRAddProducutImage.setOnClickListener(v -> {

            Dexter.withActivity(SignUpActivity.this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).getIntent(SignUpActivity.this);
                                startActivityForResult(intent, 2);
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

        binding.imgAddProduct1.setOnClickListener(v -> {

            Dexter.withActivity(SignUpActivity.this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).getIntent(SignUpActivity.this);
                                startActivityForResult(intent, 2);
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


    private void showSettingDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
        }else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(SignUpActivity.this.getContentResolver(), resultUri);

                    UserProfile_img_one = FileUtil.from(SignUpActivity.this, resultUri);
                    // img_two.setImageBitmap(bitmap);
                    //    img_one_certificate.setImageResource(R.drawable.check_one);
                    binding.RRAddProducutImage.setVisibility(View.GONE);
                    binding.imgAddProduct1.setVisibility(View.VISIBLE);

                    Glide.with(this).load(bitmap).into(binding.imgAddProduct1);
                 //   isCertificate=true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    compressedImage_two = new Compressor(SignUpActivity.this)
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(UserProfile_img_one);
                    Log.e("ActivityTag", "imageFilePAth: " + compressedImage_two);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void validation() {

         UserNae = binding.edtUserNae.getText().toString();
         Email = binding.etEmaillogin.getText().toString();
         Mobile = binding.edtMobile.getText().toString();
         Password =  binding.etPasswordlogin.getText().toString();
         ShopName =  binding.edtShopName.getText().toString();
         ShopAddress =  binding.edtShopAddress.getText().toString();
         about =  binding.edtabout.getText().toString();

        if(UserNae.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter User Name.", Toast.LENGTH_SHORT).show();

        }else if(!isValidEmail(Email))
        {
            Toast.makeText(this, "Please Enter email.", Toast.LENGTH_SHORT).show();

        }else if(Mobile.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Mobile.", Toast.LENGTH_SHORT).show();

        }else if(Password.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show();

        }else if(ShopName.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Shop Name.", Toast.LENGTH_SHORT).show();

        }else if(ShopAddress.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Shop Address.", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                SignUpapi();

            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private void SignUpapi(){

        MultipartBody.Part imgFile = null;
        MultipartBody.Part imgFileOne = null;

        if (SignUpActivity.UserProfile_img == null) {

        } else {
            RequestBody requestFileOne = RequestBody.create(MediaType.parse("image/*"), SignUpActivity.UserProfile_img);
            imgFile = MultipartBody.Part.createFormData("image", SignUpActivity.UserProfile_img.getName(), requestFileOne);

            RequestBody requestFileOne1 = RequestBody.create(MediaType.parse("image/*"), SignUpActivity.UserProfile_img_one);
            imgFileOne = MultipartBody.Part.createFormData("firm_image", SignUpActivity.UserProfile_img_one.getName(), requestFileOne1);
        }
        
        RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), UserNae);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), Email);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), Mobile);
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), Password);
        RequestBody frm_name = RequestBody.create(MediaType.parse("text/plain"), ShopName);
        RequestBody frm_address = RequestBody.create(MediaType.parse("text/plain"), ShopAddress);
        RequestBody About = RequestBody.create(MediaType.parse("text/plain"), about);
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), "75.255");
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), "45.64");
        RequestBody register_id = RequestBody.create(MediaType.parse("text/plain"), "464");

        Call<LoginModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .seller_signup(Name,email,mobile,password,frm_name,frm_address,About,lat,lon,register_id,imgFile,imgFileOne);

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                LoginModel finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {

                    Preference.save(SignUpActivity.this,Preference.KEYType_login,finallyPr.getResult().getId());

                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpActivity.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}