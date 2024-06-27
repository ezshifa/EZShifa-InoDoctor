
package com.app.ehealthai.doctor.models.responses.KioskAccsoriesResp;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("list")
    @Expose
    private java.util.List<List> list;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public java.util.List< List> getList() {
        return list;
    }

    public void setList(java.util.List< List> list) {
        this.list = list;
    }

}
