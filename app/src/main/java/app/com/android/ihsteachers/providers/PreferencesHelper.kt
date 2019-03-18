package app.com.android.ihsteachers.providers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferencesHelper {

    val USER_ID = "USER_ID"
    val CLASS_ID = "CLASS_ID"
    val TOKEN_ID = "TOKEN_ID"

    val COUNT_NO = "COUNT_NO"
    val COUNT_NOTIFICATION = "COUNT_NOTIFICATION"

    val NOTIFICATION_DATA = "NOTIFICATION_DATA"

    val BKGIMG_ONE = "BKGIMG_ONE"
    val BKGIMG_TWO = "BKGIMG_TWO"
    val BKGIMG_THREE = "BKGIMG_THREE"
    val BKGIMG_LOGIN = "BKGIMG_LOGIN"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.userId
        get() = getString(USER_ID, "")
        set(value) {
            editMe {
                it.putString(USER_ID, value)
            }
        }

    var SharedPreferences.tokenId
        get() = getString(TOKEN_ID, "")
        set(value) {
            editMe {
                it.putString(TOKEN_ID, value)
            }
        }

    var SharedPreferences.countVal
        get() = getInt(COUNT_NO, 0)
        set(value) {
            editMe {
                it.putInt(COUNT_NO, value)
            }
        }

    var SharedPreferences.countNotification
        get() = getInt(COUNT_NOTIFICATION, 0)
        set(value) {
            editMe {
                it.putInt(COUNT_NOTIFICATION, value)
            }
        }


    var SharedPreferences.bkgImgOne
        get() = getString(BKGIMG_ONE, "")
        set(value) {
            editMe {
                it.putString(BKGIMG_ONE, value)
            }
        }

    var SharedPreferences.bkgImgTwo
        get() = getString(BKGIMG_TWO, "")
        set(value) {
            editMe {
                it.putString(BKGIMG_TWO, value)
            }
        }

    var SharedPreferences.bkgImgThree
        get() = getString(BKGIMG_THREE, "")
        set(value) {
            editMe {
                it.putString(BKGIMG_THREE, value)
            }
        }

    var SharedPreferences.bkgImgLogin
        get() = getString(BKGIMG_LOGIN, "")
        set(value) {
            editMe {
                it.putString(BKGIMG_LOGIN, value)
            }
        }

    var SharedPreferences.notificationData
        get() = getString(NOTIFICATION_DATA, "")
        set(value) {
            editMe {
                it.putString(NOTIFICATION_DATA, value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }

    var SharedPreferences.classId
        get() = getString(CLASS_ID, "")
        set(value) {
            editMe {
                it.putString(CLASS_ID, value)
            }
        }
}