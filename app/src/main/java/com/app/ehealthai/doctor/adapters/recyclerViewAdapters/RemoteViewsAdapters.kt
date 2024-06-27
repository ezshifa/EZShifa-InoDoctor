//package com.app.ehealthaidoctor.adapters.recyclerViewAdapters
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.SurfaceView
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.app.ehealthaidoctor.R
//import com.app.ehealthaidoctor.ui.activities.VideoSessionActivity
//
//class RemoteViewsAdapters(
//    private val activity: VideoSessionActivity,
//    private var items: ArrayList<Int>
//) : RecyclerView.Adapter<RemoteViewsAdapters.ViewHolder>() {
//
//    private var userMaps = HashMap<Int, Pair<SurfaceView, FrameLayout>>()
//
//    fun addRemoteView(uid: Int) {
//        items.add(uid)
//        notifyItemInserted(items[items.size - 1])
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun remoteRemoteView(uid: Int) {
//        items.remove(uid)
//        for (e in userMaps) {
//            if (e.key == uid) {
//                val mPair = userMaps[e.key]
//                mPair!!.first.visibility = View.GONE
//                userMaps.remove(e.key)
//                break
//            }
//        }
//        notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun remoteUserVideoChanged(uid: Int, videoOff: Boolean) {
//        for (e in userMaps) {
//            if (e.key == uid) {
//                val mPair = userMaps[e.key]
//                if (videoOff) {
//                    mPair!!.second.removeView(mPair.first)
//                    mPair.first.visibility = View.GONE
//                    mPair.second.visibility = View.GONE
//                } else {
//                    mPair!!.second.visibility = View.VISIBLE
//                    mPair.first.visibility = View.VISIBLE
//                }
//                break
//            }
//        }
//        notifyDataSetChanged()
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun removeAllRemoteRemoteView() {
//        for (e in userMaps) {
//            val mPair = userMaps[e.key]
//            mPair!!.first.visibility = View.GONE
//        }
//        items.clear()
//        notifyDataSetChanged()
//    }
//
//    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val remoteVideoViewContainer: FrameLayout =
//            itemView.findViewById(R.id.remote_video_view_container)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.list_item_remote_video_views, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        try {
//            val remoteSurfaceView = SurfaceView(activity.baseContext)
//            userMaps[items[position]] = Pair(remoteSurfaceView, holder.remoteVideoViewContainer)
//            remoteSurfaceView.setZOrderMediaOverlay(true)
//            holder.remoteVideoViewContainer.addView(remoteSurfaceView)
//            activity.addRemoteVideoInAgoraEngine(
//                remoteSurfaceView,
//                items[position]
//            )
//            holder.itemView.setOnClickListener {
//                val mPair = userMaps[items[position]]
//                activity.toggleVideos(items[position], mPair!!.second, mPair.first)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun returnListItems(): ArrayList<Int> {
//        return items
//    }
//
//    override fun setHasStableIds(hasStableIds: Boolean) {
//        super.setHasStableIds(hasStableIds)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//}