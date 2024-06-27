package com.app.ehealthaidoctor.ui.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.ehealthaidoctor.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.example.ehealthai.utils.Constants
import com.example.ehealthai.utils.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.activity_splash_screen.gif
import kotlinx.android.synthetic.main.activity_splash_screen.tvEnvironment


class splashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        FirebaseApp.initializeApp(applicationContext)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        when(Constants.ENVIRONMENT_NAME)
        {
            "dev."->
                tvEnvironment.text = "Development"
            "uat"->
                tvEnvironment.text = "Testing"
            ""->
                tvEnvironment.text = ""
        }
        try {
            val imageViewTarget = GlideDrawableImageViewTarget(gif)
            Glide.with(this@splashScreen).load(R.raw.loaderwhite).into(imageViewTarget)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            askNotificationPermission()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                /* Create an Intent that will start the Menu-Activity. */
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }, 3000)
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Can post notifications.
            Handler(Looper.getMainLooper()).postDelayed({
                /* Create an Intent that will start the Menu-Activity. */
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }, 3000)
        } else {
            // Inform user that that your app will not show notifications.
            Toast.makeText(
                this@splashScreen,
                "Notification permission is required, to show notification for calls",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showNotificationPermissionRationale() {
        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(this@splashScreen)
        builder.setMessage("Notification permission is required, to show notification for calls ")
        // Set Alert Title
        builder.setTitle("Permission required !")
        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)
        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton(
            "Ok"
        ) { _: DialogInterface?, _: Int ->
            if (Build.VERSION.SDK_INT >= 33) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                // If user click no then dialog box is canceled.
                finish()
                dialog.cancel()
            } as DialogInterface.OnClickListener)

        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Can post notifications.
                Handler(Looper.getMainLooper()).postDelayed({
                    /* Create an Intent that will start the Menu-Activity. */
                    val mainIntent = Intent(this, LoginActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }, 3000)
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showNotificationPermissionRationale()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
