
package com.app.ehealthai.doctor.models.responses.KioskAccsoriesResp;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class KioskAccsoriesResp {

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
