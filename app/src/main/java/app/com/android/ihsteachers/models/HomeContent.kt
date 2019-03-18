package app.com.android.ihsteachers.models

import app.com.android.ihsteachers.R
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object HomeContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<HomeItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, HomeItem> = HashMap()

    private val COUNT = 4

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: HomeItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): HomeItem {

        val titleValues: Array<String> = arrayOf("ASSIGNED SUBJECT 1", "ASSIGNED SUBJECT 2", "NEXT LESSON", "LESSONS COVERED SO FAR")
        val contentValues: Array<String> = arrayOf("BIOLOGY", "AGRICULTURE", "ANATOMY 101", "12 LESSONS COVERED")
        val subContentValues: Array<String> = arrayOf("Class 1A", "Class 2A", "Class 1A", "SEP 15 - SEP 31")
        val homeIcons: Array<Int> = arrayOf(R.drawable.ic_appointment, R.drawable.ic_appointment, R.drawable.ic_history_symbol,
                R.drawable.ic_classes_so_far)
        val homeBackgroundImgs: Array<Int> = arrayOf(R.drawable.light_blue_gradient_curve, R.drawable.fiery_rose_gradient_curve,
                R.drawable.blue_cerulean_gradient_curve, R.drawable.light_green_gradient_curve)



        return HomeItem(position.toString(), titleValues[position - 1], contentValues[position - 1],contentValues[position - 1],
                subContentValues[position - 1], homeIcons[position - 1], homeBackgroundImgs[position - 1])
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class HomeItem(val id: String, val title: String, val content: String, val subContent: String, val subject: String,
                        val imgName: Int, val backgroundImage: Int)
}
