package com.app.ehealthaidoctor.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.ehealthaidoctor.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_view_image.view.*


class ViewImageDialog(var image: String) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_view_image, container, false)
        Picasso.get().load(image).placeholder(R.drawable.ic_image_black_24dp).into(view.image_view)
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

}