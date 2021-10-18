package com.my.formerseller.utils;

import com.my.formerseller.model.AddProductModel;
import com.my.formerseller.model.Categorymodel;
import com.my.formerseller.model.ForogtPassword;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.model.MyProductModel;
import com.my.formerseller.model.SubcaegoryModel;
import com.my.formerseller.model.SubcaegoryModelData;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {

    String login ="seller_login";
    String forgot_password ="forgot_password";
    String get_profile ="get_profile";
    String seller_signup ="seller_signup";
    String  add_product="add_product";
    String  category_list="category_list";
    String  get_subcategory="get_subcategory";
    String update_buyer_profile ="update_buyer_profile";
    String get_product ="get_product";


    @FormUrlEncoded
    @POST(login)
    Call<LoginModel>login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("register_id") String register_id
    );

 @FormUrlEncoded
    @POST(forgot_password)
    Call<ForogtPassword>forgot_password(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST(get_profile)
    Call<LoginModel>get_profile(
            @Field("user_id") String user_id
    );

    @Multipart
    @POST(update_buyer_profile)
    Call<LoginModel>update_buyer_profile(
            @Part("user_id") RequestBody user_id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part MultipartBody.Part part
    );


    @Multipart
    @POST(seller_signup)
    Call<LoginModel>seller_signup(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("mobile") RequestBody mobile,
            @Part("password") RequestBody password,
            @Part("firm_name") RequestBody firm_name,
            @Part("firm_address") RequestBody firm_address,
            @Part("about") RequestBody about,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part("register_id") RequestBody register_id,
           @Part MultipartBody.Part part,
           @Part MultipartBody.Part part1
   );

   @Multipart
    @POST(add_product)
    Call<AddProductModel>add_product(
            @Part("seller_id") RequestBody seller_id,
            @Part("category_id") RequestBody category_id,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("stock_status") RequestBody stock_status,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part("sub_cat_id") RequestBody sub_cat_id,
           @Part MultipartBody.Part part1
   );

    @POST(category_list)
    Call<Categorymodel> category_list();


    @FormUrlEncoded
    @POST(get_subcategory)
    Call<SubcaegoryModel>get_subcategory(
            @Field("cat_id") String cat_id
    );

    @FormUrlEncoded
    @POST(get_product)
    Call<MyProductModel>get_product(
            @Field("seller_id") String seller_id,
            @Field("stock_status") String stock_status
    );

}
