package com.app.ehealthai.doctor.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.print.PDFPrint
import android.provider.Settings
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.ehealthaidoctor.R
import com.example.ehealthai.utils.Constants
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tejpratapsingh.pdfcreator.utils.FileManager
import com.tejpratapsingh.pdfcreator.utils.PDFUtil
import kotlinx.android.synthetic.main.activity_view_report_activty.btnSaveAsPdf
import kotlinx.android.synthetic.main.activity_view_report_activty.webView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ViewReportActivity : AppCompatActivity() {
    private lateinit var url: String
    private var schoolId: String = ""
    private var studentId: String = ""
    private var studentName: String = ""
    lateinit var progressDialog: ProgressDialog
    lateinit var print: WebView
    var alreadyDownloaded = false
    var isAlreadyPrinting = false
    private var isButtonClickEnabled = false
    override fun onBackPressed() {
        super.onBackPressed()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_report_activty)

        try {
            progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Please wait report is loading")
            progressDialog.show()

            if (intent.hasExtra(Constants.INTENT_KEY_SCHOOL_ID)
                && intent.hasExtra(Constants.INTENT_KEY_STUDENT_ID)
                && intent.hasExtra(Constants.INTENT_KEY_STUDENT_NAME)

            ) {
                schoolId = intent.getStringExtra(Constants.INTENT_KEY_SCHOOL_ID)!!
                studentId = intent.getStringExtra(Constants.INTENT_KEY_STUDENT_ID)!!
                studentName = intent.getStringExtra(Constants.INTENT_KEY_STUDENT_NAME)!!
            }

            try {
                if (schoolId.isNotEmpty() && studentId.isNotEmpty()) {
                    url =
                        "${Constants.BASE_URL_REPORT}school_id=$schoolId&student_id=$studentId&language=en&ispdf=false"
                    val webView: WebView = webView
                    val webSettings = webView.settings
                    webSettings.javaScriptEnabled = true
                    webSettings.useWideViewPort = true
                    webSettings.loadWithOverviewMode = true
                    webSettings.domStorageEnabled = true

                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            progressDialog.dismiss()
                            if (view != null) {
                                print = view
                                Handler(Looper.getMainLooper()).postDelayed({
                                    isButtonClickEnabled = true
                                }, 4000)
                            }
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            view?.settings?.javaScriptEnabled = true
                            view?.loadUrl(url)
                            return true
                        }

                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            if (error != null) {
                                // Check error description or code
                                val errorCode = error.errorCode
                                val description = error.description.toString()

                                // Handle the error as needed
                                if (errorCode == -2 || description.contains("net::ERR_INTERNET_DISCONNECTED")) {
                                    // Internet connection issue
                                    // Handle the error appropriately (e.g., show a message to the user)
                                    btnSaveAsPdf.visibility = View.GONE
                                }
                            }
                        }
                    }


                    webView.loadUrl(url)

                    btnSaveAsPdf.setOnClickListener {
                        if (isButtonClickEnabled) {
                            // Perform button click action
                            // Disable button click for 5 seconds
                            isButtonClickEnabled = false
                            btnSaveAsPdf.isEnabled = false

                            // Post a delayed action to enable the button after 5 seconds
                            Handler(Looper.getMainLooper()).postDelayed({
                                btnSaveAsPdf.isEnabled = true
                                isButtonClickEnabled = true
                            }, 5000) // 5000 milliseconds = 5 seconds

                            if (!alreadyDownloaded) {
                                if (this::print.isInitialized) {
                                    Dexter.withContext(this@ViewReportActivity)
                                        .withPermissions(
                                            Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        )
                                        .withListener(object : MultiplePermissionsListener {
                                            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                                report?.let {
                                                    if (report.areAllPermissionsGranted()) {
                                                        if (studentName == "") {
                                                            if (print != null) {
                                                                savePDF(
                                                                    "Student Report $studentName",
                                                                    print
                                                                )
                                                            }
                                                        } else {
                                                            if (print != null) {
                                                                savePDF(studentName, print)
                                                            }
                                                        }
                                                    } else {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                            showPermissionRationale()
                                                        }
                                                    }
                                                }
                                            }

                                            override fun onPermissionRationaleShouldBeShown(
                                                permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                                                token: PermissionToken?
                                            ) {
                                                // Remember to invoke this method when the custom rationale is closed
                                                // or just by default if you don't want to use any custom rationale.
                                                token?.continuePermissionRequest()
                                            }


                                        })
                                        .withErrorListener {
                                            Toast.makeText(
                                                this@ViewReportActivity,
                                                it.name,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .check()
                                } else
                                    Toast.makeText(
                                        this@ViewReportActivity,
                                        "Please wait loading",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            } else {
                                Toast.makeText(
                                    this@ViewReportActivity,
                                    "Already downloaded in document folder",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        }
                    }

                } else {
                    Toast.makeText(
                        this@ViewReportActivity,
                        "Patient record not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
//            Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun savePDF(fileName: String, webView: WebView) {
        try {

            // Generate Pdf From Html
            if (!isAlreadyPrinting) {
                Toast.makeText(this@ViewReportActivity, "Saving file", Toast.LENGTH_SHORT).show()
                isAlreadyPrinting = true
                val savedPDFFile = FileManager.getInstance().createTempFile(
                    applicationContext, "pdf", false
                )
                PDFUtil.generatePDFFromWebView(savedPDFFile, webView, object :
                    PDFPrint.OnPDFPrintListener {
                    override fun onSuccess(file: File) {
                        if (saveFileInDocuments("$fileName.pdf", savedPDFFile)) {
                            Toast.makeText(
                                this@ViewReportActivity,
                                "File saved to Documents folder",
                                Toast.LENGTH_LONG
                            ).show()
                            alreadyDownloaded = true
                            isAlreadyPrinting = true
                        } else {
                            Toast.makeText(
                                this@ViewReportActivity,
                                "Failed to save file",
                                Toast.LENGTH_SHORT
                            ).show()
                            alreadyDownloaded = false
                            isAlreadyPrinting = false
                        }

                    }

                    override fun onError(exception: java.lang.Exception) {
                        alreadyDownloaded = false
                        isAlreadyPrinting = false
                        exception.printStackTrace()
                    }
                })
            }


        } catch (e: Exception) {
            alreadyDownloaded = false
            e.printStackTrace()
            Toast.makeText(
                this@ViewReportActivity,
                "Failed to save file",
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    private fun saveFileInDocuments(fileName: String, existingFile: File): Boolean {
        try {
            val newFile = createFileInDocumentsDirectory(fileName)
            if (newFile != null) {
                FileInputStream(existingFile).use { inp ->
                    FileOutputStream(newFile).use { out ->
                        copyFile(inp, out, existingFile)
                        return true
                    }
                }
            } else {
                Toast.makeText(
                    this@ViewReportActivity,
                    "failed to download file",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun createFileInDocumentsDirectory(fileName: String?): File? {
        val documentsDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        if (!documentsDirectory.exists()) {
            documentsDirectory.mkdirs()
        }
        val newFile = File(documentsDirectory, fileName!!)
        return try {
            if (newFile.createNewFile()) {
                // File created successfully
                newFile
            } else {
                // File already exists or unable to create
                if (newFile.exists()) {
                    if (fileName.contains(".pdf")) {
                        val updatedName = fileName.replace(".pdf", "")
                        return File(
                            documentsDirectory,
                            updatedName.plus(System.currentTimeMillis()).plus(".pdf")
                        )
                    } else {
                        return File(
                            documentsDirectory,
                            fileName.plus(System.currentTimeMillis()).plus(".pdf")
                        )
                    }

                } else {
                    null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun copyFile(inp: InputStream, out: OutputStream, existingFile: File) {
        try {
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inp.read(buffer).also { bytesRead = it } != -1) {
                out.write(buffer, 0, bytesRead)
            }
            existingFile.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showPermissionRationale() {
        // You can show a dialog or message explaining why the permission is needed
        // and then request the permission again if the user agrees.
        // Example: Show an AlertDialog explaining the need for the permission.
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("This app needs to write to external storage to perform certain actions.")
            .setPositiveButton("OK") { dialog, which ->
                // Request the permission again
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, which -> }
            .create()
            .show()
    }

    private fun openAppSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}