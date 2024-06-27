package com.example.ehealthai.models

data class GroupListItem(
    var title: String,
    var imageResourceId: Int,
    var isCollapsed: Boolean
) : Comparator<GroupListItem> {

    override fun compare(groupListItem: GroupListItem, t1: GroupListItem): Int {
        return groupListItem.title.compareTo(t1.title)
    }
}
