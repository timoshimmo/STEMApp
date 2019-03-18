package app.com.android.ihsteachers.providers

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import java.util.HashMap

class InputStreamVolleyRequestKotlin(method: Int, url: String?, listener: Response.Listener<ByteArray>, errorListener: Response.ErrorListener?) : Request<ByteArray>(method, url, errorListener) {

    private var mListener: Response.Listener<ByteArray>? = null
    private var responseHeaders: Map<String, String>? = null

    init {
        setShouldCache(false)
        mListener = listener
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<ByteArray> {

        responseHeaders = response!!.headers

        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: ByteArray?) {
        mListener!!.onResponse(response)
    }
}