package app.com.android.ihsteachers.main.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import app.com.android.ihsteachers.R
import app.com.android.ihsteachers.providers.PreferencesHelper
import app.com.android.ihsteachers.providers.PreferencesHelper.userId
import app.com.android.ihsteachers.utils.volley.ApiController
import app.com.android.ihsteachers.utils.volley.ServiceVolley
import kotlinx.android.synthetic.main.activity_change_password.*
import org.jetbrains.anko.toast
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {

    val CUSTOM_PREF_NAME = "UserID"

    var currentPass: EditText? = null
    var newPass: EditText? = null
    var confirmPass: EditText? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        currentPass = findViewById(R.id.txtCurrentPassword)
        newPass = findViewById(R.id.txtNewPassword)
        confirmPass = findViewById(R.id.txtConfirmPassword)
        progressBar = findViewById(R.id.progressBarChangePassword)

        currentPass!!.clearFocus()
        newPass!!.clearFocus()
        confirmPass!!.clearFocus()

        btnUpdatePassword.setOnClickListener {
            attemptChangePassword(currentPass!!, newPass!!, confirmPass!!)
        }

    }

    private fun editPassword(newPass: String) {

        val service = ServiceVolley()
        val apiController = ApiController(service)

        val path = "editPassword.php"
        val params = JSONObject()

        val prefs = PreferencesHelper.customPreference(applicationContext, CUSTOM_PREF_NAME)
        val userId = prefs.userId

        params.put("userid", userId)
        params.put("userpass", newPass)


        apiController.post(path, params) {response ->

            val obj = JSONObject(response.toString())

            if(obj.getInt("status") == 1) {

                progressBar!!.visibility = View.GONE

                val success = obj.getString("msg")
                toast(success)
                onBackPressed()

            }
            else {
                val unsuccessful = obj.getString("msg")
                toast(unsuccessful)
            }
        }
    }

    private fun attemptChangePassword(mCurrent: EditText, mNewPass: EditText, mConfirm: EditText) {

        // Reset errors.
        mCurrent.error = null
        mNewPass.error = null
        mConfirm.error = null

        val current = mCurrent.text.toString()
        val newPass = mNewPass.text.toString()
        val cnfPass = mConfirm.text.toString()

        var cancel = false
        lateinit var focusView: View

        if (TextUtils.isEmpty(current) && TextUtils.isEmpty(newPass) && TextUtils.isEmpty(cnfPass)) run {
            mCurrent.error = getString(R.string.field_required_error)
            mNewPass.error = getString(R.string.field_required_error)
            mConfirm.error = getString(R.string.field_required_error)
            focusView = mCurrent

            focusView.requestFocus()

        }

        if (TextUtils.isEmpty(current)) {
            mCurrent.error = getString(R.string.field_required_error)
            focusView = mCurrent
            cancel = true
        }
        if (TextUtils.isEmpty(newPass)) {
            mNewPass.error = getString(R.string.field_required_error)
            focusView = mNewPass
            cancel = true
        }

        if (TextUtils.isEmpty(cnfPass)) {
            mConfirm.error = getString(R.string.field_required_error)
            focusView = mConfirm
            cancel = true
        }

        if (!TextUtils.isEmpty(newPass) && !isPassLengthValid(newPass)) {
            mCurrent.error = "Password is too short"
            focusView = mNewPass
            cancel = true
        }

        if (!TextUtils.isEmpty(cnfPass) && !isPassLengthValid(cnfPass)) {
            mConfirm.error = "Password is too short"
            focusView = mConfirm
            cancel = true
        }

        if(cancel) {
            focusView.requestFocus()
        }
        else {
            if(newPass != cnfPass) {
                mConfirm.error = "Password does not match"
            }else {
                progressBar!!.visibility = View.VISIBLE
                editPassword(newPass)
                focusView = mCurrent
            }
        }
    }

    private fun isPassLengthValid(strPass: String): Boolean {
        //TODO: Replace this with your own logic
        return strPass.length > 5
    }
}
