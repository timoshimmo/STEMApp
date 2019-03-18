package app.com.android.ihsteachers.models

import app.com.android.ihsteachers.R
import java.util.ArrayList
import java.util.HashMap

object AssignedContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<AssignedItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, AssignedItem> = HashMap()

    private val COUNT = 3

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: AssignedItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): AssignedItem {

        val titleValues: Array<String> = arrayOf("1st TERM", "2nd TERM", "3rd TERM")
        val contentValues: Array<String> = arrayOf("Ongoing Term", "Next Term", "Final Term")
        val dateValues: Array<String> = arrayOf("Sep - Dec", "Jan - Apr", "May - July")
        val termsIcons: Array<Int> = arrayOf(R.drawable.read_hand_book, R.drawable.computer, R.drawable.grad_cert)


        return AssignedItem(position.toString(), titleValues[position - 1], contentValues[position - 1],
                dateValues[position - 1], termsIcons[position - 1])
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class AssignedItem(val id: String, val title: String, val content: String, val dateContent: String, val iconImage: Int)
}