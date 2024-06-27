
package com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Child {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("patientid")
    @Expose
    private Integer patientid;
    @SerializedName("childno")
    @Expose
    private Integer childno;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("live_status")
    @Expose
    private String liveStatus;
    @SerializedName("death_reason")
    @Expose
    private Object deathReason;
    @SerializedName("create_date")
    @Expose
    private String createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientid() {
        return patientid;
    }

    public void setPatientid(Integer patientid) {
        this.patientid = patientid;
    }

    public Integer getChildno() {
        return childno;
    }

    public void setChildno(Integer childno) {
        this.childno = childno;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public Object getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(Object deathReason) {
        this.deathReason = deathReason;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}
