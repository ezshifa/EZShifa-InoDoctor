
package com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ObsDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("patientid")
    @Expose
    private Integer patientid;
    @SerializedName("last_manstrual_date")
    @Expose
    private String lastManstrualDate;
    @SerializedName("no_of_kids")
    @Expose
    private Integer noOfKids;
    @SerializedName("patient_nature")
    @Expose
    private String patientNature;
    @SerializedName("cousion_marriage")
    @Expose
    private String cousionMarriage;
    @SerializedName("husband_blood_group")
    @Expose
    private String husbandBloodGroup;
    @SerializedName("kids_alive")
    @Expose
    private String kidsAlive;
    @SerializedName("total_kids_alive")
    @Expose
    private Integer totalKidsAlive;
    @SerializedName("delivery_mode")
    @Expose
    private String deliveryMode;
    @SerializedName("mother_stat")
    @Expose
    private String motherStat;
    @SerializedName("pregnancy_problem")
    @Expose
    private String pregnancyProblem;
    @SerializedName("birth_status")
    @Expose
    private String birthStatus;
    @SerializedName("birth_anomilies")
    @Expose
    private String birthAnomilies;
    @SerializedName("husband_profession")
    @Expose
    private String husbandProfession;
    @SerializedName("abortion")
    @Expose
    private String abortion;

    public String getAbortionReason() {
        return abortionReason;
    }

    public void setAbortionReason(String abortionReason) {
        this.abortionReason = abortionReason;
    }

    @SerializedName("abortion_reason")
    @Expose
    private String abortionReason;
    @SerializedName("marriage_period")
    @Expose
    private String marriagePeriod;
    @SerializedName("patient_bloodgroup")
    @Expose
    private String patient_bloodgroup;

    public String getHusbandProfession() {
        return husbandProfession;
    }

    public void setHusbandProfession(String husbandProfession) {
        this.husbandProfession = husbandProfession;
    }

    public String getAbortion() {
        return abortion;
    }

    public void setAbortion(String abortion) {
        this.abortion = abortion;
    }

    public String getMarriagePeriod() {
        return marriagePeriod;
    }

    public void setMarriagePeriod(String marriagePeriod) {
        this.marriagePeriod = marriagePeriod;
    }

    public String getPatient_bloodgroup() {
        return patient_bloodgroup;
    }

    public void setPatient_bloodgroup(String patient_bloodgroup) {
        this.patient_bloodgroup = patient_bloodgroup;
    }

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

    public String getLastManstrualDate() {
        return lastManstrualDate;
    }

    public void setLastManstrualDate(String lastManstrualDate) {
        this.lastManstrualDate = lastManstrualDate;
    }

    public Integer getNoOfKids() {
        return noOfKids;
    }

    public void setNoOfKids(Integer noOfKids) {
        this.noOfKids = noOfKids;
    }

    public String getPatientNature() {
        return patientNature;
    }

    public void setPatientNature(String patientNature) {
        this.patientNature = patientNature;
    }

    public String getCousionMarriage() {
        return cousionMarriage;
    }

    public void setCousionMarriage(String cousionMarriage) {
        this.cousionMarriage = cousionMarriage;
    }

    public String getHusbandBloodGroup() {
        return husbandBloodGroup;
    }

    public void setHusbandBloodGroup(String husbandBloodGroup) {
        this.husbandBloodGroup = husbandBloodGroup;
    }

    public String getKidsAlive() {
        return kidsAlive;
    }

    public void setKidsAlive(String kidsAlive) {
        this.kidsAlive = kidsAlive;
    }

    public Integer getTotalKidsAlive() {
        return totalKidsAlive;
    }

    public void setTotalKidsAlive(Integer totalKidsAlive) {
        this.totalKidsAlive = totalKidsAlive;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getMotherStat() {
        return motherStat;
    }

    public void setMotherStat(String motherStat) {
        this.motherStat = motherStat;
    }

    public String getPregnancyProblem() {
        return pregnancyProblem;
    }

    public void setPregnancyProblem(String pregnancyProblem) {
        this.pregnancyProblem = pregnancyProblem;
    }

    public String getBirthStatus() {
        return birthStatus;
    }

    public void setBirthStatus(String birthStatus) {
        this.birthStatus = birthStatus;
    }

    public String getBirthAnomilies() {
        return birthAnomilies;
    }

    public void setBirthAnomilies(String birthAnomilies) {
        this.birthAnomilies = birthAnomilies;
    }

}
