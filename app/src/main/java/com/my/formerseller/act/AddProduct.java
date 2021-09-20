package com.my.formerseller.act;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import android.widget.AdapterView;
import android.widget.Toast;

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
import com.my.formerseller.adapter.CategorySpinnerAdapter;
import com.my.formerseller.adapter.SubSpinnerAdapter;
import com.my.formerseller.databinding.ActivityAddProductBinding;
import com.my.formerseller.model.AddProductModel;
import com.my.formerseller.model.CategoryModelData;
import com.my.formerseller.model.Categorymodel;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.model.SubcaegoryModel;
import com.my.formerseller.model.SubcaegoryModelData;
import com.my.formerseller.utils.RetrofitClients;
import com.my.formerseller.utils.SessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {

    ActivityAddProductBinding binding;
    private Bitmap bitmap;
    private Uri resultUri;
    private SessionManager sessionManager;
    public static File UserProfile_img, codmpressedImage, compressActualFile;

    String ProductName="";
    String ProductPrice="";
    String ProductDescription="";

    private String[] code = {"Male", "Female"};
    /* access modifiers changed from: private */
    public int[] flags = {R.drawable.logout, R.drawable.logout, R.drawable.logout, R.drawable.logout    };
    public ArrayList<CategoryModelData> modelist = new ArrayList<>();
    public ArrayList<SubcaegoryModelData> modelist_sub = new ArrayList<>();

    String Cat_Id ="";
    String subCat_Id ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_product);

        sessionManager = new SessionManager(AddProduct.this);


        setUi();

        if (sessionManager.isNetworkAvailable()) {

            binding.progressBar.setVisibility(View.VISIBLE);

            getCategory();

        }else {
            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void setUi() {

        binding.spinnerCatgory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                Cat_Id = modelist.get(pos).getId();

                if (sessionManager.isNetworkAvailable()) {

                    binding.progressBar.setVisibility(View.VISIBLE);

                    getSubCategory(Cat_Id);

                }else {
                    Toast.makeText(AddProduct.this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        binding.spinnerSbcategoy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long arg3){

                subCat_Id = modelist_sub.get(pos).getId();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        binding.RRBack.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.AddProduct.setOnClickListener(v -> {
            validation();
           // finish();

        });

        binding.RRAddImage.setOnClickListener(v -> {
            Dexter.withActivity(AddProduct.this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).getIntent(AddProduct.this);
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

    private void showSettingDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
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

                    Glide.with(this).load(bitmap).circleCrop().into(binding.imgeProduct);

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


   private void validation() {


        ProductName = binding.edtprodduct.getText().toString();
        ProductPrice = binding.edtprodductprice.getText().toString();
        ProductDescription = binding.edtproductDdescription.getText().toString();

        if(ProductName.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Product Name.", Toast.LENGTH_SHORT).show();

        }else if(ProductPrice.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Product Price.", Toast.LENGTH_SHORT).show();

        }else if(ProductDescription.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please Enter Product Description", Toast.LENGTH_SHORT).show();

        }else
        {
            if (sessionManager.isNetworkAvailable()) {

                binding.progressBar.setVisibility(View.VISIBLE);

                AddProducApi();

            }else {
                Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void AddProducApi(){

        String SellerId= Preference.get(AddProduct.this,Preference.KEYType_login);

        MultipartBody.Part imgFile = null;

        if (SignUpActivity.UserProfile_img == null) {

        } else {
            RequestBody requestFileOne = RequestBody.create(MediaType.parse("image/*"), SignUpActivity.UserProfile_img);
            imgFile = MultipartBody.Part.createFormData("image", SignUpActivity.UserProfile_img.getName(), requestFileOne);

        }

        RequestBody Seller_Id = RequestBody.create(MediaType.parse("text/plain"), SellerId);
        RequestBody Cat_id = RequestBody.create(MediaType.parse("text/plain"), Cat_Id);
        RequestBody Sub_Cat_id = RequestBody.create(MediaType.parse("text/plain"), subCat_Id);
        RequestBody Productname = RequestBody.create(MediaType.parse("text/plain"), ProductName);
        RequestBody Productprice = RequestBody.create(MediaType.parse("text/plain"), ProductPrice);
        RequestBody Productdescription = RequestBody.create(MediaType.parse("text/plain"), ProductDescription);
        RequestBody Stock = RequestBody.create(MediaType.parse("text/plain"), "InStock");
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), "75.255");
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), "45.64");

        Call<AddProductModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .add_product(Seller_Id,Cat_id,Productname,Productdescription,Productprice,Stock,lat,lon,Sub_Cat_id,imgFile);

        call.enqueue(new Callback<AddProductModel>() {
            @Override
            public void onResponse(Call<AddProductModel> call, Response<AddProductModel> response) {

                binding.progressBar.setVisibility(View.GONE);

                AddProductModel finallyPr = response.body();

                String status = finallyPr.getStatus();

                if (status.equalsIgnoreCase("1")) {
                    Toast.makeText(AddProduct.this, ""+finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddProduct.this, MainActivity.class));

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddProduct.this, finallyPr.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AddProductModel> call, Throwable t) {

                binding.progressBar.setVisibility(View.GONE);

                Toast.makeText(AddProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    public void getCategory() {

        Call<Categorymodel> call = RetrofitClients
                .getInstance()
                .getApi()
                .category_list();
        call.enqueue(new Callback<Categorymodel>() {
            @Override
            public void onResponse(Call<Categorymodel> call, Response<Categorymodel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    Categorymodel myclass= response.body();

                    String status = myclass.getStatus();
                    String result = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")){

                        modelist= (ArrayList<CategoryModelData>) myclass.getResult();
                        CategorySpinnerAdapter customAdapter=new CategorySpinnerAdapter(AddProduct.this,flags,modelist);
                        binding.spinnerCatgory.setAdapter(customAdapter);

                    }else {
                        Toast.makeText(AddProduct.this, result, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Categorymodel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSubCategory(String id) {

        Call<SubcaegoryModel> call = RetrofitClients
                .getInstance()
                .getApi()
                .get_subcategory(id);
        call.enqueue(new Callback<SubcaegoryModel>(){
            @Override
            public void onResponse(Call<SubcaegoryModel> call, Response<SubcaegoryModel> response) {
                try {

                    binding.progressBar.setVisibility(View.GONE);

                    SubcaegoryModel myclass= response.body();

                    String status = myclass.getStatus();
                    String result = myclass.getMessage();

                    if (status.equalsIgnoreCase("1")){

                        modelist_sub= (ArrayList<SubcaegoryModelData>) myclass.getResult();
                        SubSpinnerAdapter customAdapter=new SubSpinnerAdapter(AddProduct.this,flags,modelist_sub);
                        binding.spinnerSbcategoy.setAdapter(customAdapter);

                    }else {
                        Toast.makeText(AddProduct.this, result, Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SubcaegoryModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}