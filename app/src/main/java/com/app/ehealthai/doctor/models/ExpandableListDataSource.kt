package com.app.ehealthaidoctor.models

import android.content.Context
import com.example.ehealthai.models.GroupListItem
import java.util.*

object ExpandableListDataSource {

    fun getData(context: Context): Map<GroupListItem, List<String>> {
        val expandableListData = LinkedHashMap<GroupListItem, List<String>>()

/*        val categories = Arrays.asList(*context.resources.getStringArray(R.array.categories))

        val health = Arrays.asList(*context.resources.getStringArray(R.array.my_health))
        val speetarCare = Arrays.asList(*context.resources.getStringArray(R.array.EHealthAi_care))

        expandableListData[GroupListItem(categories[0], R.drawable.messages, true)] = ArrayList(1)
        expandableListData[GroupListItem(categories[1], R.drawable.ic_home, true)] = ArrayList(1)
        expandableListData[GroupListItem(categories[2], R.drawable.my_health_grey, true)] = health
        expandableListData[GroupListItem(categories[3], R.drawable.my_calendar_grey, true)] = ArrayList(1)
        expandableListData[GroupListItem(categories[4], R.drawable.find_a_doctor, true)] = ArrayList(1)
        expandableListData[GroupListItem(categories[5], R.drawable.settings, true)] = ArrayList(1)
        //expandableListData[GroupListItem(categories[6], R.drawable.speeter_care, true)] = speetarCare
        expandableListData[GroupListItem(categories[6], R.drawable.ic_appointment, true)] = ArrayList(1)
        expandableListData[GroupListItem(categories[7], R.drawable.signout, true)] = ArrayList(1)*/

        return expandableListData
    }
}