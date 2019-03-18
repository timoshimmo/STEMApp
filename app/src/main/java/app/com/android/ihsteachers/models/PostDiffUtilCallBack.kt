package app.com.android.ihsteachers.models

import android.support.v7.util.DiffUtil

class PostDiffUtilCallBack(private val oldList: List<TimeTableItem>, private val newList: List<TimeTableItem>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
}