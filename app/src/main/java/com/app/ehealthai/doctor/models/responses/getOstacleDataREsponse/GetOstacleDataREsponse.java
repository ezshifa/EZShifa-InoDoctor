
package com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOstacleDataREsponse {

    @SerializedName("statuscode")
    @Expose
    private Integer statuscode;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}