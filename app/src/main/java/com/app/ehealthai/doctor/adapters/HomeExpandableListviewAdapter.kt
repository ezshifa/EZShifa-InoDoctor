//package com.egrb.ehealthaidoctor.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseExpandableListAdapter
//import android.widget.ExpandableListView
//import android.widget.ImageView
//import android.widget.TextView
//import com.egrb.ehealthaidoctor.R
//import com.example.ehealthai.models.GroupListItem
//
//class HomeExpandableListviewAdapter(val context:Context, val listView:ExpandableListView,val  expandableListTitle: List<GroupListItem>,
//                                    val expandableListDetail: Map<GroupListItem, List<String>>) : BaseExpandableListAdapter(){
//
//    private var mLayoutInflater: LayoutInflater
//    init {
//        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    }
//
//    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
//        return expandableListDetail[expandableListTitle[listPosition]]!!.get(expandedListPosition)
//    }
//
//    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
//        return expandedListPosition.toLong()
//    }
//
//    override fun getChildView(
//        listPosition: Int, expandedListPosition: Int,
//        isLastChild: Boolean, convertView: View?, parent: ViewGroup
//    ): View {
//        var convertView = convertView
//        val expandedListText = getChild(listPosition, expandedListPosition) as String
//        if (convertView == null) {
//            convertView = mLayoutInflater.inflate(R.layout.list_item, null)
//        }
//        val expandedListTextView = convertView!!
//            .findViewById(R.id.title) as TextView
//        expandedListTextView.text = expandedListText
//        return convertView
//    }
//
//    override fun getChildrenCount(listPosition: Int): Int {
//        return expandableListDetail[expandableListTitle[listPosition]]!!.size
//    }
//
//    override fun getGroup(listPosition: Int): Any {
//        return expandableListTitle[listPosition]
//    }
//
//    override fun getGroupCount(): Int {
//        return expandableListTitle.size
//    }
//
//    override fun getGroupId(listPosition: Int): Long {
//        return listPosition.toLong()
//    }
//
//    override fun getGroupView(
//        listPosition: Int, isExpanded: Boolean,
//        convertView: View?, parent: ViewGroup
//    ): View {
//        var convertView = convertView
//        val item = getGroup(listPosition) as GroupListItem
//
//        if (convertView == null) {
//            convertView = mLayoutInflater.inflate(R.layout.list_group, null)
//        }
//        val listTitleTextView = convertView!!
//            .findViewById(R.id.title) as TextView
//        val image = convertView.findViewById(R.id.image) as ImageView
//
//        val arrow = convertView.findViewById(R.id.arrow) as ImageView
//
//        listTitleTextView.setText(item.title)
//        image.setImageResource(item.imageResourceId)
//
//
//        if (getChildrenCount(listPosition) != 0) {
//            arrow.visibility = View.VISIBLE
//
//            arrow.setImageResource(if (isExpanded) R.drawable.arrow_down else R.drawable.arrow)
//
//
//        } else {
//            arrow.visibility = View.INVISIBLE
//        }
//        return convertView
//    }
//
//    override fun hasStableIds(): Boolean {
//        return false
//    }
//
//    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
//        return true
//    }
//
//
//}