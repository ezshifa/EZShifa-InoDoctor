package com.example.ehealthai.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.app.ehealthaidoctor.R

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun convertFromXToXX(monthOrDay: String): String {
    var newMonth = monthOrDay
    when {
        monthOrDay == "1" -> newMonth = "01"
        monthOrDay == "2" -> newMonth = "02"
        monthOrDay == "3" -> newMonth = "03"
        monthOrDay == "4" -> newMonth = "04"
        monthOrDay == "5" -> newMonth = "05"
        monthOrDay == "6" -> newMonth = "06"
        monthOrDay == "7" -> newMonth = "07"
        monthOrDay == "8" -> newMonth = "08"
        monthOrDay == "9" -> newMonth = "09"
    }
    return newMonth
}

fun convertTimeFormat(time: String, aside: String?): String {
    val timeString = time.split(":")
    var newTime: String = time
    val minute: String

    //set minutes for x to xx format
    when {
        timeString.get(1) == "0" -> minute = "00"
        timeString.get(1) == "1" -> minute = "01"
        timeString.get(1) == "2" -> minute = "02"
        timeString.get(1) == "3" -> minute = "03"
        timeString.get(1) == "4" -> minute = "04"
        timeString.get(1) == "5" -> minute = "05"
        timeString.get(1) == "6" -> minute = "06"
        timeString.get(1) == "7" -> minute = "07"
        timeString.get(1) == "8" -> minute = "08"
        timeString.get(1) == "9" -> minute = "09"
        else -> minute = timeString.get(1)
    }

    if (aside != null) {
        //set Hour from 12 to 24 hr format
        if (aside.equals("am")) {
            when {
                timeString.get(0) == "12" -> newTime = "00:" + minute
                timeString.get(0) == "1" || timeString.get(0) == "01" -> newTime = "01:" + minute
                timeString.get(0) == "2" || timeString.get(0) == "02" -> newTime = "02:" + minute
                timeString.get(0) == "3" || timeString.get(0) == "03" -> newTime = "03:" + minute
                timeString.get(0) == "4" || timeString.get(0) == "04" -> newTime = "04:" + minute
                timeString.get(0) == "5" || timeString.get(0) == "05" -> newTime = "05:" + minute
                timeString.get(0) == "6" || timeString.get(0) == "06" -> newTime = "06:" + minute
                timeString.get(0) == "7" || timeString.get(0) == "07" -> newTime = "07:" + minute
                timeString.get(0) == "8" || timeString.get(0) == "08" -> newTime = "08:" + minute
                timeString.get(0) == "9" || timeString.get(0) == "09" -> newTime = "09:" + minute
                timeString.get(0) == "10" -> newTime = "10:" + minute
                timeString.get(0) == "11" -> newTime = "11:" + minute
            }
        } else {
            when {
                timeString.get(0) == "12" -> newTime = "12:" + minute
                timeString.get(0) == "1" || timeString.get(0) == "01" -> newTime = "13:" + minute
                timeString.get(0) == "2" || timeString.get(0) == "02" -> newTime = "14:" + minute
                timeString.get(0) == "3" || timeString.get(0) == "03" -> newTime = "15:" + minute
                timeString.get(0) == "4" || timeString.get(0) == "04" -> newTime = "16:" + minute
                timeString.get(0) == "5" || timeString.get(0) == "05" -> newTime = "17:" + minute
                timeString.get(0) == "6" || timeString.get(0) == "06" -> newTime = "18:" + minute
                timeString.get(0) == "7" || timeString.get(0) == "07" -> newTime = "19:" + minute
                timeString.get(0) == "8" || timeString.get(0) == "08" -> newTime = "20:" + minute
                timeString.get(0) == "9" || timeString.get(0) == "09" -> newTime = "21:" + minute
                timeString.get(0) == "10" -> newTime = "22:" + minute
                timeString.get(0) == "11" -> newTime = "23:" + minute
            }
        }
    } else {
        //set Hour from 24 to 12 hr format
        when {
            timeString.get(0) == "0" || timeString.get(0) == "0" -> newTime = "12:" + minute + " am"
            timeString.get(0) == "1" || timeString.get(0) == "01" -> newTime = "01:" + minute + " am"
            timeString.get(0) == "2" || timeString.get(0) == "02" -> newTime = "02:" + minute + " am"
            timeString.get(0) == "3" || timeString.get(0) == "03" -> newTime = "03:" + minute + " am"
            timeString.get(0) == "4" || timeString.get(0) == "04" -> newTime = "04:" + minute + " am"
            timeString.get(0) == "5" || timeString.get(0) == "05" -> newTime = "05:" + minute + " am"
            timeString.get(0) == "6" || timeString.get(0) == "06" -> newTime = "06:" + minute + " am"
            timeString.get(0) == "7" || timeString.get(0) == "07" -> newTime = "07:" + minute + " am"
            timeString.get(0) == "8" || timeString.get(0) == "08" -> newTime = "08:" + minute + " am"
            timeString.get(0) == "9" || timeString.get(0) == "09" -> newTime = "09:" + minute + " am"
            timeString.get(0) == "10" -> newTime = "10:" + minute + " am"
            timeString.get(0) == "11" -> newTime = "11:" + minute + " am"

            timeString.get(0) == "12" -> newTime = "12:" + minute + " pm"
            timeString.get(0) == "13" -> newTime = "01:" + minute + " pm"
            timeString.get(0) == "14" -> newTime = "02:" + minute + " pm"
            timeString.get(0) == "15" -> newTime = "03:" + minute + " pm"
            timeString.get(0) == "16" -> newTime = "04:" + minute + " pm"
            timeString.get(0) == "17" -> newTime = "05:" + minute + " pm"
            timeString.get(0) == "18" -> newTime = "06:" + minute + " pm"
            timeString.get(0) == "19" -> newTime = "07:" + minute + " pm"
            timeString.get(0) == "20" -> newTime = "08:" + minute + " pm"
            timeString.get(0) == "21" -> newTime = "09:" + minute + " pm"
            timeString.get(0) == "22" -> newTime = "10:" + minute + " pm"
            timeString.get(0) == "23" -> newTime = "11:" + minute + " pm"
        }
    }
    return newTime
}


inline fun Context.showCancelDialog(crossinline returnSure: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle(this.resources.getString(R.string.cancel_confirm_dialog_title))
        .setMessage(this.resources.getString(R.string.cancel_confirm_dialog_message))
        .setPositiveButton(
            this.resources.getString(R.string.cancel_confirm_dialog_confirm),
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    returnSure()
                }
            })
        .setNegativeButton(
            this.resources.getString(R.string.cancel_confirm_dialog_dismiss), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
        .show()
}

inline fun Context.showCloseDialog(crossinline returnSure: () -> Unit) {
    AlertDialog.Builder(this)
            .setTitle(this.resources.getString(R.string.close_confirm_dialog_title))
            .setMessage(this.resources.getString(R.string.close_confirm_dialog_message))
            .setPositiveButton(
                    this.resources.getString(R.string.cancel_confirm_dialog_confirm),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            returnSure()
                        }
                    })
            .setNegativeButton(
                    this.resources.getString(R.string.cancel_confirm_dialog_dismiss), object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
            .show()
}

inline fun Context.showSuccessDialog(crossinline returnDismiss: () -> Unit) {
    AlertDialog.Builder(this)
        .setTitle(this.resources.getString(R.string.success_dialog_title))
        .setMessage(this.resources.getString(R.string.success_dialog_message_appt))
        .setPositiveButton(
            this.resources.getString(R.string.success_dialog_confirm),
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    returnDismiss()
                }
            }).show()

}
