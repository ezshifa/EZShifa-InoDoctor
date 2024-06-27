package com.app.ehealthai.doctor.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.widget.ProgressBar;

import com.app.ehealthai.doctor.adapters.recyclerViewAdapters.MedicinelistAdapter;
import com.app.ehealthai.doctor.models.MedicalAdapterModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Global {


    public static boolean endcalluttonperdormed = false;
    @NotNull
    public static String docame;
    public static boolean IntentFromHandler = false;
    public static ProgressDialog progressDialog;
    public static boolean isfromVideoSessionActivity = false;

    public static void showProgressDialog(Activity activity, String message) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        ProgressBar progressbar = progressDialog.findViewById(android.R.id.progress);
        progressbar.getIndeterminateDrawable()
                .setColorFilter(Color.parseColor("#d32e33"),
                        android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
            progressDialog.dismiss();
        }
    }

    @NotNull
    public static String usertype;
    @Nullable
    public static String username;
    @NotNull
    public static String selectedid;
    @Nullable
    public static String appointmentIdd;
    @Nullable
    public static String patientIdd;
}
