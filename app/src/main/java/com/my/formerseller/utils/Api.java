package com.my.formerseller.utils;

import com.my.formerseller.model.AddProductModel;
import com.my.formerseller.model.Categorymodel;
import com.my.formerseller.model.ConverSationList;
import com.my.formerseller.model.ForogtPassword;
import com.my.formerseller.model.GetOrderNotification;
import com.my.formerseller.model.InsertChat;
import com.my.formerseller.model.LoginModel;
import com.my.formerseller.model.MyProductModel;
import com.my.formerseller.model.ProductDetailsUser;
import com.my.formerseller.model.SubcaegoryModel;
import com.my.formerseller.model.SubcaegoryModelData;
import com.my.formerseller.model.UpdateProductModel;

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
    String social_login ="social_login";
    String forgot_password ="forgot_password";
    String get_profile ="get_profile";
    String seller_signup ="seller_signup";
    String  add_product="add_product";
    String  category_list="category_list";
    String  get_subcategory="get_subcategory";
    String update_buyer_profile ="update_buyer_profile";
    String get_product ="get_product";
    String get_order ="get_order";
    String update_order_status ="update_order_status";
    String order_detail ="order_detail";
    String get_product_detail ="get_product_detail";
    String update_product ="update_product";

    //chat Api
    String insert_chat ="insert_chat";
    String get_conversation ="get_conversation";


    @FormUrlEncoded
    @POST(login)
    Call<LoginModel>login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("register_id") String register_id
    );

    @FormUrlEncoded
    @POST(social_login)
    Call<LoginModel>social_login(
            @Field("social_id") String social_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("register_id") String register_id,
            @Field("firm_name") String firm_name,
            @Field("firm_address") String firm_address,
            @Field("lat") String lat,
            @Field("lon") String lon,
            @Field("type") String type
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

    @FormUrlEncoded
    @POST(get_conversation)
    Call<ConverSationList>get_conversation(
            @Field("receiver_id") String receiver_id
    );

    @FormUrlEncoded
    @POST(insert_chat)
    Call<InsertChat>insert_chat(
            @Field("sender_id") String sender_id,
            @Field("receiver_id") String receiver_id,
            @Field("chat_message") String chat_message
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

   @Multipart
    @POST(update_product)
    Call<UpdateProductModel>update_product(
            @Part("product_id") RequestBody product_id,
            @Part("seller_id") RequestBody seller_id,
            @Part("category_id") RequestBody category_id,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("stock_status") RequestBody stock_status,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
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

    @FormUrlEncoded
    @POST(get_order)
    Call<GetOrderNotification>get_order(
            @Field("seller_id") String seller_id,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST(update_order_status)
    Call<ResponseBody>update_order_status(
            @Field("id") String id,
            @Field("status") String status
    );

@FormUrlEncoded
    @POST(order_detail)
    Call<ProductDetailsUser>order_detail(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST(get_product_detail)
    Call<UpdateProductModel>get_product_detail(
            @Field("product_id") String product_id
    );

}
