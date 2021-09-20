package com.my.formerseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductModel {

    @SerializedName("result")
    @Expose
    private AddProductModeldata result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public AddProductModeldata getResult() {
        return result;
    }

    public void setResult(AddProductModeldata result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
