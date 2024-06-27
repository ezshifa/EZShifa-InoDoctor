package com.app.ehealthaidoctor.network

import com.app.ehealthai.doctor.models.responses.*
import com.app.ehealthai.doctor.models.responses.KioskAccsoriesResp.KioskAccsoriesResp
import com.app.ehealthai.doctor.models.responses.LogoutResponse.LogoutResonse
import com.app.ehealthai.doctor.models.responses.getOstacleDataREsponse.GetOstacleDataREsponse
import com.app.ehealthai.doctor.models.responses.patientTests.PatientTests
import com.app.ehealthai.doctor.models.responses.uploadSnapResponse.SnapResponse
import com.app.ehealthai.models.responses.PatientBillingResponse
import com.app.ehealthaidoctor.models.local.Notification.NotificationSentResponse
import com.app.ehealthaidoctor.models.local.Notification.Sender
import com.app.ehealthaidoctor.models.responses.*
import com.example.example.PatientScreeningResponse
import com.example.example.PrescriptionHistory
import com.example.example.StudentScreening
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


public interface ApiInterface {

    @POST("restapi/RestController.php?view=get_obstacle_data")
    @FormUrlEncoded
    fun getPatientChildListResponse(
        @Field("pid") patientId: String
    ): Call<GetOstacleDataREsponse>

    @POST("restapi/RestController.php?view=login")
    @FormUrlEncoded
    fun loginCall(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("usertype") usertype: String
    ): Call<DoctorLoginResponse>

    @POST("restapi/RestController.php?view=get_medicine_list")
    @FormUrlEncoded
    fun get_medicine_list(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<MedicineList>


    @POST("restapi/RestController.php?view=appointmentlist")
    @FormUrlEncoded
    fun getAppointmentList(
        @Field("username") doctorUsername: String,
        @Field("doctorid") doctorId: String,
        @Field("apdate") appointmentDate: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetAppointmentDoctorList>

    @POST("restapi/RestController.php?view=doctor_billing_summary")
    @FormUrlEncoded
    fun BillingSummary(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<PatientBillingResponse>

    @POST("restapi/RestController.php?view=upload_patient_docs")
    @Multipart
    fun uploadPatientFile(
        @Part("appointmentid") appointmentId: RequestBody,
        @Part("pid") patientId: RequestBody,
        @Part("doctorid") doctorId: RequestBody,
        @Part("doc_type") docType: RequestBody,
        @Part uploadfile: MultipartBody.Part,
        @Part("doctor_notes") description: RequestBody,
        @Part("sessionid") sessionid: RequestBody,
        @Part("username") username: RequestBody,
        @Part("usertype") usertype: RequestBody,
        @Part("followup_date") followupdate: RequestBody
    ): Call<UploadPatientFileResponse>


    @POST("restapi/RestController.php?view=upload_patient_docs")
    @Multipart
    fun uploadPatientFile1(
        @Part("appointmentid") appointmentId: RequestBody,
        @Part("pid") patientId: RequestBody,
        @Part("doctorid") doctorId: RequestBody,
        @Part("doc_type") docType: RequestBody,
        @Part("doctor_notes") description: RequestBody,
        @Part("sessionid") sessionid: RequestBody,
        @Part("username") username: RequestBody,
        @Part("usertype") usertype: RequestBody,
        @Part("followup_date") followupdate: RequestBody
    ): Call<UploadPatientFileResponse>

    @POST("restapi/RestController.php?view=upload_callsnaps_docs")
    @Multipart
    fun uploadDoctorSnap(
        @Part("pid") patientId: RequestBody,
        @Part("username") username: RequestBody,
        @Part("description") description: RequestBody,
        @Part("appointmentid") appointmentId: RequestBody,
        @Part uploadfile: MultipartBody.Part,
        @Part("doc_type") docType: RequestBody,
    ): Call<SnapResponse>

    @POST("restapi/RestController.php?view=patient_documents")
    @FormUrlEncoded
    fun getPatientDocuments(
        @Field("pid") patientId: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetPatientDocumentsResponse>


    @POST("restapi/RestController.php?view=get_rapidtest")
    @FormUrlEncoded
    fun getPatientTests(
        @Field("pid") patientId: String,
        @Field("apptid") appointmentId: String,
    ): Call<PatientTests>


    @POST("restapi/RestController.php?view=logout_doctor")
    @FormUrlEncoded
    fun Logout(
        @Field("userid") patientId: String,
    ): Call<LogoutResonse>

    @POST("restapi/RestController.php?view=appointment_history")
    @FormUrlEncoded
    fun getAppointmentDoctors(
        @Field("doctorid") doctorId: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetAppointmentDoctorList>

    @Headers(
        "Content-Type: application/json",
        "Authorization:key=AAAA-NauI_0:APA91bEchZYw2vbUF2OjgVEb96Em7LbBBJU7zQ3yYwQJI2ucz_mQPwGv-ocInFscb6p3Ybrqx5g6sLbr0o69WRfXSo8mJ0_CMaeyV8SJJLMxFV4Ut8N4I0OyI7cV0vGf0fI7rlP-1_hi"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender): Call<NotificationSentResponse>


    @POST("restapi/RestController.php?view=set_firebase_data")
    @FormUrlEncoded
    fun sendFirebaseToken(
        @Field("doctorid") doctorId: String,
        @Field("deviceinfo") deviceinfo: String,
        @Field("device_detail") devicedetail: String,
        @Field("deviceid") deviceId: String,
        @Field("appversion") appversion: String,
        @Field("lastuseddate") lastuseddate: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<SendFirebaseTokenResponse>


    @POST("restapi/RestController.php?view=get_avg_rating")
    @FormUrlEncoded
    fun GetRating(
        @Field("doctorid") patientId: String, @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetRatingResponse>

    @POST("restapi/RestController.php?view=doctor_feedback")
    @FormUrlEncoded
    fun GetDoctorfeedback(
        @Field("doctorid") patientId: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetDoctorFeedbackResponse>

    @POST("restapi/RestController.php?view=send_notification")
    @FormUrlEncoded
    fun sendNotification(
        @Field("pid") patientId: String,
        @Field("message") message: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<SendNotificationResponse>

    @FormUrlEncoded
    @POST("restapi/RestController.php?view=country_list")
    fun getCities(
        @Field("sessionid") sessionid: String, @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetCitiesResponse>

    @FormUrlEncoded
    @POST("restapi/RestController.php?view=get_physician_type")
    fun getSpecialities(
        @Field("sessionid") sessionid: String, @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetSpecialityResponse>

    @FormUrlEncoded
    @POST("apis/facilities")
    fun getFacilities(
        @Field("sessionid") sessionid: String, @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetFacilitiesResponse>

    @POST("restapi/RestController.php?view=update_doctor_profile")
    @Multipart
    fun updateProfile(
        @Part("doctorid") doctorid: RequestBody,
        @Part("fname") fname: RequestBody,
        @Part("lname") lname: RequestBody,
        @Part("specialty") specialty: RequestBody,
        // @Part("facility") facility: RequestBody,
        @Part("title") title: RequestBody,
        @Part("country") country: RequestBody,
        @Part("city") city: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part profile_image: MultipartBody.Part?,
        @Part("experience") experience: RequestBody,
        @Part("degrees") degrees: RequestBody,
        @Part("sessionid") sessionid: RequestBody,
        @Part("username") username: RequestBody,
        @Part("usertype") usertype: RequestBody

    ): Call<GetUpdateProfileResponse>


    @POST("restapi/RestController.php?view=doctor_profile")
    @FormUrlEncoded
    fun getDoctorProfile(
        @Field("doctorid") doctorId: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<DoctorLoginResponse>

    @POST("restapi/RestController.php?view=delete_vitals")
    @FormUrlEncoded
    fun delete_vitals(
        @Field("vitalid") appointmentId: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<CancelAppointmentResponse>

    @POST("restapi/RestController.php?view=cancel_appointment")
    @FormUrlEncoded
    fun cancelAppointmentRequest(
        @Field("eid") appointmentId: String,
        @Field("doctorid") doctorId: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<CancelAppointmentResponse>


    @POST("restapi/RestController.php?view=close_appointment")
    @FormUrlEncoded
    fun closeAppointmentRequest(
        @Field("appointment_id") appointmentId: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<CloseAppointmentResponse>


    @POST("restapi/RestController.php?view=add_vitals")
    @FormUrlEncoded
    fun AddVitalsRequest(
        @Field("apptid") apptid: String,
        @Field("bp1") bp1: String,
        @Field("bp2") bp2: String,
        @Field("sugar") sugar: String,
        @Field("sugar_type") sugar_type: String,
        @Field("temprature_c") temprature_c: String,
        @Field("temprature_f") temprature_f: String,
        @Field("weight_kg") weight_kg: String,
        @Field("weight_lb") weight_lb: String,
        @Field("height_ft") heightf: String,
        @Field("height_cm") height_cm: String,
        @Field("height_inch") height_inch: String,
        @Field("pulse") pulse: String,
        @Field("pulse2") pulse2: String,
        @Field("oxygen") oxygen: String,
        @Field("oxygen2") oxygen2: String,
        @Field("breathing") breathing: String,
        @Field("breathing2") breathing2: String,
        @Field("sender_id") sender_id: String,
        @Field("patientid") patientid: String,
        @Field("sessionid") sessionid: String, @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<AddVitalsResponse>

    @POST("restapi/RestController.php?view=patient_vitals")
    @FormUrlEncoded
    fun GetVitalsRequest(
        @Field("apptid") apptid: String,
        @Field("patientid") patientid: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetVitalsResponse>

    @POST("restapi/RestController.php?view=patient_vitals_single")
    @FormUrlEncoded
    fun GetVitalsSingleRequest(
        @Field("apptid") apptid: String,
        @Field("patientid") patientid: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<GetVitalsResponse>

    @POST("restapi/RestController.php?view=forgot_password")
    @FormUrlEncoded
    fun forgotPassword(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("type") type: String,
        @Field("usertype") usertype: String

    ): Call<ForgotPasswordResponse>

    @POST("restapi/RestController.php?view=send_verification_code")
    @FormUrlEncoded
    fun SendVerificationCode(
        @Field("doc_username") username: String
    ): Call<ChangePasswordResponse>

    @POST("restapi/RestController.php?view=reset_doctor_password")
    @FormUrlEncoded
    fun ResetDocPassword(
        @Field("email") email: String,
        @Field("new_password") newpassword: String,
        @Field("confirm_password") confirmpassword: String
    ): Call<ForgotPasswordResponse>

    @POST("restapi/RestController.php?view=patient_medicine")
    @FormUrlEncoded
    fun patient_medicine(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("medicinedata") mid: String

    ): Call<ForgotPasswordResponse>

    @POST("restapi/RestController.php?view=get_room_data")
    @FormUrlEncoded
    fun get_room_data(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("roomid") rid: String

    ): Call<RoomDataResponse>

    @POST("restapi/RestController.php?view=get_doctor_documents_appt")
    @FormUrlEncoded
    fun get_doctor_documents_appt(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("apptid") aptid: String

    ): Call<DoctorNotesResponse>

    @POST("restapi/RestController.php?view=manage_doctor_status")
    @FormUrlEncoded
    fun manage_doctor_status(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("apptid") apptid: String,
        @Field("pid") pid: String,
        @Field("docid") docid: String,
        @Field("status") status: String

    ): Call<ForgotPasswordResponse>

    @POST("restapi/RestController.php?view=cnic_registration")
    @FormUrlEncoded
    fun cnic_registration(
        @Field("cnic") cnic: String,
        @Field("fname") fname: String,
        @Field("lname") lname: String,
        @Field("sex") sex: String,
        @Field("phone_cell") phone_cell: String,
        @Field("bloodgroup") bloodgroup: String,
        @Field("age") age: String,
        @Field("address") address: String,
        @Field("dob") dob: String,
        @Field("email") email: String,
        @Field("imei") imei: String
    ): Call<RegisterNewPatientResponse>

    @POST("restapi/RestController.php?view=set_appointment_razi")
    @FormUrlEncoded
    fun makeAppointment(
        @Field("doctorid") doctorId: String,
        @Field("pid") appointmentDate: String,
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String

    ): Call<MakeAppointmentResponse>

    @POST("restapi/RestController.php?view=get_appointment_medicine")
    @FormUrlEncoded
    fun getlastapptmeddata(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("pid") pid: String,
        @Field("doctorid") doctorid: String

    ): Call<LastapptPatientMedResponse>

    @POST("restapi/RestController.php?view=get_diagnosis_list")
    @FormUrlEncoded
    fun get_diagnosis_list(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String
    ): Call<DiagnosisList>

    @POST("restapi/RestController.php?view=get_patient_diagnosis")
    @FormUrlEncoded
    fun get_patient_diagnosis(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("apptid") apptid: String,
        @Field("pid") pid: String
    ): Call<DiagnosisList>

    @POST("restapi/RestController.php?view=add_patient_diagnosis")
    @FormUrlEncoded
    fun add_patient_diagnosis(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("diag_id") diag_id: String,
        @Field("apptid") apptid: String,
        @Field("pid") pid: String
    ): Call<AddDiagnosisResponse>
    @POST("restapi/RestController.php?view=add_patient_diagnosis")
    @FormUrlEncoded
    fun add_patient_diagnosis_with_Other_Diagnosis(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("diag_id") diag_id: String,
        @Field("other_diagnosis") otherDiagnosis: String,
        @Field("apptid") apptid: String,
        @Field("pid") pid: String
    ): Call<AddDiagnosisResponse>
    @POST("restapi/RestController.php?view=remove_patient_diagnosis")
    @FormUrlEncoded
    fun remove_patient_diagnosis(
        @Field("diag_id") diag_id: String,
        @Field("other_diag") otherDiagnosis: String,
        @Field("apptid") apptid: String,
        @Field("pid") pid: String,
    ): Call<RemovePatientDiagnosisResponse>

    @POST("restapi/RestController.php?view=remove_patient_test")
    @FormUrlEncoded
    fun remove_patient_test(
        @Field("apptid") apptid: String,
        @Field("test_name") testName: String
    ): Call<RemovePatientDiagnosisResponse>

    @POST("restapi/RestController.php?view=update_doctor_status")
    @FormUrlEncoded
    fun update_doctor_status(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("doctorid") doctorid: String,
        @Field("status") status: String,
        @Field("appversion") appversion: String,
        @Field("deviceid") deviceid: String
    ): Call<ForgotPasswordResponse>

    @POST("restapi/RestController.php?view=call_status")
    @FormUrlEncoded
    fun callstatus(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("apptid") apptid: String,
        @Field("status") status: String
    ): Call<ForgotPasswordResponse>


    @POST("restapi/RestController.php?view=kiosk_accessories")
    @FormUrlEncoded
    fun kioskAccesories(
        @Field("apptid") apptid: String,
        ): Call<KioskAccsoriesResp>

    @POST("restapi/RestController.php?view=call_status")
    @FormUrlEncoded
    fun callstatuswithextraparams(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("apptid") apptid: String,
        @Field("status") status: String,
        @Field("callstarttime") callstarttime: String,
        @Field("callendtime") callendtime: String,
        @Field("callduration") callduration: String
    ): Call<ForgotPasswordResponse>

    @POST("restapi/RestController.php?view=get_kiosk_services")
    @FormUrlEncoded
    fun get_kiosk_services(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String

    ): Call<KioskServicesList>

    @POST("restapi/RestController.php?view=get_appt_site_language")
    @FormUrlEncoded
    fun get_appt_site_language(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("apptid") apptid: String

    ): Call<SiteLangResponse>

    @POST("restapi/RestController.php?view=get_clinical_test")
    @FormUrlEncoded
    fun get_clinical_test(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String

    ): Call<ClinicalTestsResponse>

    @POST("restapi/RestController.php?view=add_patient_test")
    @FormUrlEncoded
    fun add_patient_test(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("tests") tests: String,
        @Field("apptid") apptid: String
    ): Call<AddDiagnosisResponse>

    @POST("restapi/RestController.php?view=update_doctor_online_status")
    @FormUrlEncoded
    fun update_doctor_onoff_status(
        @Field("sessionid") sessionid: String,
        @Field("username") username: String,
        @Field("usertype") usertype: String,
        @Field("docid") doctorid: String,
        @Field("status") status: String,
        @Field("reason") reason: String
    ): Call<ForgotPasswordResponse>

    @POST("restapi/RestController.php?view=get_doctor_status")
    @FormUrlEncoded
    fun get_doctor_status(
        @Field("docid") docid: String
    ): Call<DoctorStatusResponse>

    @POST("restapi/RestController.php?view=get_followup_date")
    @FormUrlEncoded
    fun get_followup_date(
        @Field("apptid") apptid: String
    ): Call<GetFollowupdateResponse>

    @POST("restapi/RestController.php?view=get_doctor_online_status")
    @FormUrlEncoded
    fun get_doctor_online_status(@Field("docid") docid: String): Call<GetDocOnlineResponse>

    @POST("restapi/RestController.php?view=get_appointment_byapptid")
    @FormUrlEncoded
    fun get_appointment_byapptid(
        @Field("apptid") apptid: String
    ): Call<GetAppointmentDoctorList>

    @POST("restapi/RestController.php?view=get_patient_emr_data")
    @FormUrlEncoded
    fun getDiagnosisAndTests(
        @Field("apptid") appointmentId: String,
    ): Call<PrescriptionHistory>

//    https://ezshifa.com/emr/restapi/RestController.php?view=get_pat_screening_data
    @POST("restapi/RestController.php?view=get_pat_screening_data")
    @FormUrlEncoded
    fun getPatientScreeningData(
        @Field("pat_id") patientId: String,
        @Field("apptid") appointmentId: String,
    ): Call<PatientScreeningResponse>

    @POST("RestController_ES.php?view=get_student_data")
    @FormUrlEncoded
    fun checkStudentScreened(
        @Field("studentid") studentId: String,
    ): Call<StudentScreening>
}