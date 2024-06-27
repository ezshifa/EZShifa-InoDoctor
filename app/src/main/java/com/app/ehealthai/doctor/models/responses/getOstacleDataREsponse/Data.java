
package com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("obs_data")
    @Expose
    private List<ObsDatum> obsData = null;
    @SerializedName("childlist")
    @Expose
    private List<Child> childlist = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ObsDatum> getObsData() {
        return obsData;
    }

    public void setObsData(List<ObsDatum> obsData) {
        this.obsData = obsData;
    }

    public List<Child> getChildlist() {
        return childlist;
    }

    public void setChildlist(List<Child> childlist) {
        this.childlist = childlist;
    }

}
