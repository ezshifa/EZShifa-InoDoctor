
package com.app.ehealthai.doctor.models.responses.KioskAccsoriesResp;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class List {

    @SerializedName("accessory_id")
    @Expose
    private Integer accessoryId;
    @SerializedName("accessory_name")
    @Expose
    private String accessoryName;
    @SerializedName("kiosk_id")
    @Expose
    private Integer kioskId;
    @SerializedName("serial_no")
    @Expose
    private String serialNo;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("purchase_date")
    @Expose
    private Object purchaseDate;
    @SerializedName("installation_date")
    @Expose
    private Object installationDate;
    @SerializedName("warranty_days")
    @Expose
    private Object warrantyDays;
    @SerializedName("supplier_name")
    @Expose
    private Object supplierName;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("contact_no")
    @Expose
    private Object contactNo;

    public Integer getAccessoryId() {
        return accessoryId;
    }

    public void setAccessoryId(Integer accessoryId) {
        this.accessoryId = accessoryId;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }

    public Integer getKioskId() {
        return kioskId;
    }

    public void setKioskId(Integer kioskId) {
        this.kioskId = kioskId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Object getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Object purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Object getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Object installationDate) {
        this.installationDate = installationDate;
    }

    public Object getWarrantyDays() {
        return warrantyDays;
    }

    public void setWarrantyDays(Object warrantyDays) {
        this.warrantyDays = warrantyDays;
    }

    public Object getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(Object supplierName) {
        this.supplierName = supplierName;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getContactNo() {
        return contactNo;
    }

    public void setContactNo(Object contactNo) {
        this.contactNo = contactNo;
    }

}
