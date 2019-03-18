package app.com.android.ihsteachers.utils.volley

import org.json.JSONObject

interface ServiceInterface {

    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
}