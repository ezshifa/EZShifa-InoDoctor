package com.app.ehealthaidoctor.ui.fragments.documents

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.app.ehealthaidoctor.R
import com.app.ehealthaidoctor.adapters.recyclerViewAdapters.UserDocumentsRecyclerViewAdapter
import com.app.ehealthaidoctor.ui.activities.PatientDocumentsActivity
import com.app.ehealthaidoctor.ui.dialogs.ViewImageDialog
import kotlinx.android.synthetic.main.fragment_misc.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MiscFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: LabReportsFragment.OnFragmentInteractionListener? = null
    var userDocumentsRecyclerViewAdapter: UserDocumentsRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_misc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (PatientDocumentsActivity.miscList.isEmpty()) {
            view.misc_no_docs_image.visibility = View.VISIBLE
        } else {
            view.misc_no_docs_image.visibility = View.GONE
            view.misc_recycler_view.layoutManager = LinearLayoutManager(context)
            userDocumentsRecyclerViewAdapter =
                UserDocumentsRecyclerViewAdapter(
                    context!!,
                    PatientDocumentsActivity.miscList,
                    openDialog = {
                        val newDialog = ViewImageDialog(it)
                        newDialog.show(fragmentManager!!, "open Image")
                        fragmentManager!!.executePendingTransactions()
                    })
            view.misc_recycler_view.adapter = userDocumentsRecyclerViewAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LabReportsFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MiscFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}